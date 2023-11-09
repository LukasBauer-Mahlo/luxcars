package de.luxcars.backend.web.chat;

import com.google.gson.JsonObject;
import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.chat.ChatRoomService;
import de.luxcars.backend.services.chat.message.MessageService;
import de.luxcars.backend.util.AccountOnlineState;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.NumberUtilities;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class ChatRoutes {

  public ChatRoutes(Javalin javalin, ChatRoomService chatRoomService, MessageService messageService, AccountService accountService) {
    javalin.get("/chats/contacts", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      context.json(chatRoomService.getChatRoomsByUser(account.getId()));
    }, AuthenticationLevel.USER);

    javalin.post("/chat/start/{targetId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return;
      }

      Integer targetId = NumberUtilities.getInteger(context.pathParam("targetId"));
      if (targetId == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      if (targetId == account.getId()) {
        context.result("Can't create a chat with yourself.");
        context.status(HttpStatus.NOT_ACCEPTABLE);
        return;
      }

      accountService.getAccount(targetId)
          .ifPresentOrElse(target -> context.result(String.valueOf(chatRoomService.getOrCreateChatRoom(account.getId(), target.getId()))), () -> context.status(HttpStatus.NOT_FOUND));
    }, AuthenticationLevel.USER);

    javalin.get("/chat/{chatRoomId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      Integer chatRoomId = NumberUtilities.getInteger(context.pathParam("chatRoomId"));
      if (chatRoomId == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      int chatPartnerId = chatRoomService.getChatPartnerId(chatRoomId, account.getId());
      if (chatPartnerId == -1) {
        context.result("Unable to find mapped chat partner to user " + account.getId()).status(HttpStatus.INTERNAL_SERVER_ERROR);
        return;
      }

      int verificationUserId = chatRoomService.getChatPartnerId(chatRoomId, chatPartnerId);
      if (verificationUserId == -1) {
        context.result("Unable to find mapped chat partner to user " + account.getId()).status(HttpStatus.INTERNAL_SERVER_ERROR);
        return;
      }

      if (verificationUserId != account.getId()) { // prevent other users for viewing the chatroom
        context.result("You are not permitted to view this chat room.").status(HttpStatus.FORBIDDEN);
        return;
      }

      LuxCarsBackend.getInstance().getServices().getChatReadService().readChat(chatRoomId, account.getId());
      LuxCarsBackend.getInstance().getServices().getMessageReadService().clearUnreadMessages(account.getId(), chatRoomId);
      accountService.getAccount(chatPartnerId).ifPresentOrElse(otherAccount -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("contactName", otherAccount.toString());
        jsonObject.addProperty("contactId", otherAccount.getId());
        jsonObject.addProperty("lastOnline", AccountOnlineState.getOnlineState(otherAccount));

        jsonObject.add("messages", Constants.GSON.toJsonTree(messageService.getMessages(chatRoomId)));
        context.json(jsonObject);
      }, () -> context.status(HttpStatus.NOT_FOUND));
    }, AuthenticationLevel.USER);

    javalin.post("/chat/message/{chatId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      String message = context.header("message");
      Integer targetChatId = NumberUtilities.getInteger(context.pathParam("chatId"));
      if (targetChatId == null || message == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      int chatPartnerId = LuxCarsBackend.getInstance().getServices().getChatRoomService().getChatPartnerId(targetChatId, account.getId());
      if (chatPartnerId == -1) {
        context.status(HttpStatus.NOT_FOUND);
        return;
      }

      messageService.createMessage(account.getId(), chatPartnerId, message);
    }, AuthenticationLevel.USER);
  }

}
