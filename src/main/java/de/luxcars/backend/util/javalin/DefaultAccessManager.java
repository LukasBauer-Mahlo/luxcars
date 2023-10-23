package de.luxcars.backend.util.javalin;

import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.token.TokenService;
import de.luxcars.backend.util.Constants;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class DefaultAccessManager implements AccessManager {

  private final TokenService tokenService;
  private final AccountService accountService;

  public DefaultAccessManager(TokenService tokenService, AccountService accountService) {
    this.tokenService = tokenService;
    this.accountService = accountService;
  }

  @Override
  public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> set) throws Exception {
    if (set.isEmpty()) {
      handler.handle(context);
      return;
    }

    String token = context.header("token");
    if (token == null) {
      context.status(HttpStatus.UNAUTHORIZED);
      return;
    }

    Integer userId = this.tokenService.getUserIdByToken(token).orElse(null);
    if (userId == null) {
      context.status(HttpStatus.UNAUTHORIZED);
      return;
    }

    Account account = this.accountService.getAccount(userId).orElse(null);
    if (account == null || account.isDisabled()) {
      context.status(HttpStatus.FORBIDDEN);
      return;
    }

    if (set.contains(AuthenticationLevel.ADMIN) && !account.isAdministrator()) {
      context.status(HttpStatus.FORBIDDEN);
      return;
    }

    context.attribute(Constants.ACCOUNT_ATTRIBUTE_KEY, account);
    handler.handle(context);
  }

}
