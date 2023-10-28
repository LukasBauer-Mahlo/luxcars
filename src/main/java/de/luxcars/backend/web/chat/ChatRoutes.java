package de.luxcars.backend.web.chat;

import com.google.gson.JsonObject;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.message.MessageService;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.IntegerUtilities;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class ChatRoutes {

  public ChatRoutes(Javalin javalin, MessageService messageService, AccountService accountService) {
    javalin.get("/chats/contacts", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      context.json(messageService.getChats(account.getId()));
    }, AuthenticationLevel.USER);

    javalin.get("/chat/{userId}", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      Integer otherId = IntegerUtilities.getFromString(context.pathParam("userId"));
      if (otherId == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      accountService.getAccount(otherId).ifPresentOrElse(otherAccount -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("contactName", otherAccount.toString());
        jsonObject.addProperty("contactId", otherAccount.getId());
        jsonObject.addProperty("lastOnline", "not implemented yet");

        jsonObject.add("messages", Constants.GSON.toJsonTree(messageService.getMessages(account.getId(), otherId)));
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
        //TODO: Publish with websockets
      }, () -> context.status(HttpStatus.NOT_FOUND));
    }, AuthenticationLevel.USER);
  }

}
