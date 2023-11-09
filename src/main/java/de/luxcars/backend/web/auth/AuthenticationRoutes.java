package de.luxcars.backend.web.auth;

import com.google.gson.JsonObject;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.image.profile.ProfileImageService;
import de.luxcars.backend.services.token.TokenService;
import de.luxcars.backend.util.AccountValidator;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationRoutes {

  public AuthenticationRoutes(Javalin javalin, AccountService accountService, ProfileImageService profileImageService, TokenService tokenService) {
    javalin.post("/auth/register", context -> {
      String firstName = context.formParam("firstName");
      String lastName = context.formParam("lastName");
      String email = context.formParam("email");
      String password = context.formParam("password");

      if (firstName == null || lastName == null || email == null || password == null || !AccountValidator.isPasswordValid(password) || !AccountValidator.isNameValid(firstName, lastName)) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      if (accountService.getAccount(email).isPresent()) { // account already exists
        context.status(HttpStatus.CONFLICT);
        return;
      }

      Account account = accountService.createAccount(email, firstName, lastName, password, false);
      UploadedFile uploadedFile = context.uploadedFile("profileImage");
      if (uploadedFile != null) {
        profileImageService.insertImage(account.getId(), uploadedFile.content());
      }

      JsonObject result = new JsonObject();
      result.addProperty("accountId", account.getId());
      result.addProperty("token", tokenService.generateToken(account.getId()));
      context.json(result);
    });

    javalin.post("/auth/login", context -> {
      String email = context.header("email");
      String password = context.header("password");

      if (email == null || password == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      accountService.getAccount(email).ifPresentOrElse(account -> {
        if (!BCrypt.checkpw(password, account.getPassword())) {
          context.status(HttpStatus.FORBIDDEN);
          return;
        }

        JsonObject result = new JsonObject();
        result.addProperty("accountId", account.getId());
        result.addProperty("token", tokenService.generateToken(account.getId()));
        context.json(result);
      }, () -> context.status(HttpStatus.FORBIDDEN));
    });

    javalin.post("/auth/logout", context -> {
      Account account = context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY);
      if (account == null) {
        return; // not possible
      }

      tokenService.invalidateAllTokens(account.getId());
    }, AuthenticationLevel.USER);
  }

}
