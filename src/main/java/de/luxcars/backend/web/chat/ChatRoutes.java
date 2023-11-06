package de.luxcars.backend.web.chat;

import com.google.gson.JsonObject;
import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.chat.ChatRoomService;
import de.luxcars.backend.services.chat.message.MessageService;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.IntegerUtilities;
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

    javalin.get("/chat/{chatRoomId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      Integer chatRoomId = IntegerUtilities.getFromString(context.pathParam("chatRoomId"));
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
        jsonObject.addProperty("lastOnline", "not implemented yet");

        jsonObject.add("messages", Constants.GSON.toJsonTree(messageService.getMessages(chatRoomId)));
        context.json(jsonObject);
      }, () -> context.status(HttpStatus.NOT_FOUND));
    }, AuthenticationLevel.USER);

    javalin.post("/chat/message/{userId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      String message = context.header("message");
      Integer targetUserId = IntegerUtilities.getFromString(context.pathParam("userId"));
      if (targetUserId == null || message == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      accountService.getAccount(targetUserId).ifPresentOrElse(other -> {
        messageService.createMessage(account.getId(), other.getId(), message);
      }, () -> context.status(HttpStatus.NOT_FOUND));
    }, AuthenticationLevel.USER);
  }

}
