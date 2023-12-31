package de.luxcars.backend.web.chat;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.chat.ChatRoomService;
import de.luxcars.backend.services.chat.read.ChatReadService;
import de.luxcars.backend.services.token.TokenService;
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

  private static final String UPDATE_UNREAD_CHATS = "updateUnreadChats:";

  private final List<WsContext> connectedClients = new ArrayList<>();

  private final ChatRoomService chatRoomService;

  public ChatWebSocket(Javalin javalin, TokenService tokenService, ChatReadService chatReadService, ChatRoomService chatRoomService) {
    this.chatRoomService = chatRoomService;
    AccountService accountService = LuxCarsBackend.getInstance().getServices().getAccountService();

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
      for (WsContext connectedClient : this.connectedClients) {
        connectedClient.sendPing();
      }
    }, 0L, 15L, TimeUnit.SECONDS);

    javalin.ws("/chat", webSocket -> {
      webSocket.onConnect(connectedClients::add);
      webSocket.onClose(context -> {
        Integer userId = context.attribute(USER_ID_ATTRIBUTE);
        if (userId != null) {
          accountService.getAccount(userId).ifPresent(account -> {
            account.setLastOnline(System.currentTimeMillis());
            accountService.updateAccount(account);
          });
        }

        this.connectedClients.remove(context);
      });

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

        //other routes
      });
    });
  }

  public void publishChatRoomUpdate(int chatRoomId) {
    for (WsContext client : this.connectedClients) {
      Integer userId = client.attribute(USER_ID_ATTRIBUTE);
      if (userId == null) {
        continue;
      }

      if (this.chatRoomService.getChatRoomsByUser(userId).stream().noneMatch(chatRoom -> chatRoom.getChatRoomId() == chatRoomId)) {
        return;
      }

      client.send("updateChat:" + chatRoomId);
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

  public boolean isOnline(int userId) {
    for (WsContext client : this.connectedClients) {
      Integer currentUserId = client.attribute(USER_ID_ATTRIBUTE);
      if (currentUserId != null && currentUserId == userId) {
        return true;
      }
    }

    return false;
  }

}
