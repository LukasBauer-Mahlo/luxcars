package de.luxcars.backend.web.general;

import de.luxcars.backend.LuxCarsBackend;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class NameIdMappingRoute {

  public NameIdMappingRoute(Javalin javalin) {
    javalin.get("/account/id/{mail}", context -> {
      LuxCarsBackend.getInstance().getServices().getAccountService().getAccount(context.pathParam("mail")).ifPresentOrElse(account -> {
        context.result(String.valueOf(account.getId()));
      }, () -> context.status(HttpStatus.NOT_FOUND));
    });
  }

}
