package de.luxcars.backend.web.account;

import com.google.gson.JsonObject;
import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.util.AccountValidator;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import org.mindrot.jbcrypt.BCrypt;

public class AccountRoutes {

  public AccountRoutes(Javalin javalin, AccountService accountService) {
    javalin.get("/account/id/{mail}", context -> {
      accountService.getAccount(context.pathParam("mail")).ifPresentOrElse(account -> {
        context.result(String.valueOf(account.getId()));
      }, () -> context.status(HttpStatus.NOT_FOUND));
    });

    javalin.get("/account/info", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // Not possible
      }

      JsonObject result = new JsonObject();
      result.addProperty("firstName", account.getFirstName());
      result.addProperty("lastName", account.getLastName());
      result.addProperty("location", account.getLocation() != null ? account.getLocation() : "");
      result.addProperty("mail", account.getMail());
      context.json(result);
    }, AuthenticationLevel.USER);

    javalin.post("/account/update/profile", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // Not possible
      }

      String firstName = context.formParam("firstName");
      if (firstName != null) {
        if (!AccountValidator.isNameValid(firstName)) {
          context.result("Invalid first name provided").status(HttpStatus.BAD_REQUEST);
          return;
        }

        account.setFirstName(firstName);
      }

      String lastName = context.formParam("lastName");
      if (lastName != null) {
        if (!AccountValidator.isNameValid(lastName)) {
          context.result("Invalid last name provided").status(HttpStatus.BAD_REQUEST);
          return;
        }

        account.setLastName(lastName);
      }

      String location = context.formParam("location");
      if (location != null) {
        if (!AccountValidator.isLocationValid(location)) {
          context.result("Invalid location set").status(HttpStatus.BAD_REQUEST);
          return;
        }

        if (location.trim().isEmpty()) {
          account.setLocation(null);
        } else {
          account.setLocation(location);
        }
      }

      UploadedFile uploadedFile = context.uploadedFile("profileImage");
      if (uploadedFile != null) {
        LuxCarsBackend.getInstance().getServices().getImageService().insertImage(account.getId(), uploadedFile.content());
      }

      accountService.updateAccount(account);
    }, AuthenticationLevel.USER);

    javalin.post("/account/update/password", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // Not possible
      }

      String password = context.header("password");
      String newPassword = context.header("newPassword");

      if (!BCrypt.checkpw(password, account.getPassword())) {
        context.status(HttpStatus.FORBIDDEN);
        return;
      }

      if (password == null || newPassword == null || !AccountValidator.isPasswordValid(newPassword)) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      account.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
      accountService.updateAccount(account);
    }, AuthenticationLevel.USER);

  }

}
