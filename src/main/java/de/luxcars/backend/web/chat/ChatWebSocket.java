package de.luxcars.backend.web.chat;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.chat.read.ChatReadService;
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

  private static final String CHAT_LOGIN_ENTRY = "Chat-Login:";
  private static final String USER_ID_ATTRIBUTE = "chat_userId";
  private static final String SELECTED_CHATROOM_ID = "chat_selectedRoom";

  private static final String SELECT_CHAT_ENTRY = "selectChat:";
  private static final String UPDATE_UNREAD_CHATS = "updateUnreadChats:";

  private final List<WsContext> connectedClients = new ArrayList<>();

  public ChatWebSocket(Javalin javalin, TokenService tokenService, ChatReadService chatReadService) {
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
        if (message.startsWith(CHAT_LOGIN_ENTRY)) {
          String token = message.replace(CHAT_LOGIN_ENTRY, "");
          tokenService.getUserIdByToken(token).ifPresent(userId -> {
            context.attribute(USER_ID_ATTRIBUTE, userId);

            context.send(UPDATE_UNREAD_CHATS + chatReadService.getUnreadChats(userId));
          });

          return;
        }

        Integer userIdAttribute = context.attribute(USER_ID_ATTRIBUTE);
        if (userIdAttribute == null) {
          context.closeSession(); // authentication failed
          return;
        }

        if (message.startsWith(SELECT_CHAT_ENTRY)) {
          Integer chatId = IntegerUtilities.getFromString(message.replace(SELECT_CHAT_ENTRY, ""));
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

  public void publishUnreadChatsUpdate(int userId) {
    int unreadChats = LuxCarsBackend.getInstance().getServices().getChatReadService().getUnreadChats(userId);
    for (WsContext client : this.connectedClients) {
      Integer currentUserId = client.attribute(USER_ID_ATTRIBUTE);
      if (currentUserId != null && currentUserId == userId) {
        client.send(UPDATE_UNREAD_CHATS + unreadChats);
        return;
      }
    }
  }

}
