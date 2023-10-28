package de.luxcars.backend.web.chat;

import de.luxcars.backend.services.token.TokenService;
import de.luxcars.backend.util.IntegerUtilities;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatWebSocket {

  //in general it is possible to view chat updates as not authorized user

  private static final String USER_ID_ATTRIBUTE = "chat_userId";
  private static final String SELECTED_CHATROOM_ID = "chat_selectedRoom";

  private final List<WsContext> connectedClients = new ArrayList<>();

  public ChatWebSocket(Javalin javalin, TokenService tokenService) {
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
      for (WsContext connectedClient : this.connectedClients) {
        connectedClient.sendPing();
      }
    }, 0L, 15L, TimeUnit.SECONDS);

    javalin.ws("/chat", webSocket -> {
      webSocket.onConnect(connectedClients::add);
      webSocket.onClose(connectedClients::remove);

      webSocket.onMessage(context -> {
        String message = context.message();
        if (message.startsWith("Chat-Login:")) {
          String token = message.replace("Chat-Login:", "");
          tokenService.getUserIdByToken(token).ifPresent(userId -> {
            context.attribute(USER_ID_ATTRIBUTE, userId);
          });

          System.out.println("auth is done");
          return;
        }

        Integer userIdAttribute = context.attribute(USER_ID_ATTRIBUTE);
        if (userIdAttribute == null) {
          context.closeSession(); // authentication failed
          return;
        }

        if (message.startsWith("selectChat:")) {
          Integer chatId = IntegerUtilities.getFromString(message.replace("selectChat:", ""));
          if (chatId == null) {
            return;
          }

          context.attribute(SELECTED_CHATROOM_ID, chatId);
        }
      });
    });
  }

  public void publishChatRoomUpdate(int chatRoomId) {
    for (WsContext client : this.connectedClients) {
      Integer selectedChat = client.attribute(SELECTED_CHATROOM_ID);
      if (selectedChat != null && selectedChat == chatRoomId) {
        client.send("updateChat:" + selectedChat);
      }
    }
  }

}
