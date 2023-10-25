package de.luxcars.backend.web.img;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.util.IntegerUtilities;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class ImageRoutes {

  public ImageRoutes(Javalin javalin) {
    javalin.get("/profile/image/{userId}", context -> {
      Integer userId = IntegerUtilities.getFromString(context.pathParam("userId"));
      if (userId == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      boolean loadDefaultImage = context.queryParam("default") != null;
      byte[] imageBytes = LuxCarsBackend.getInstance().getServices().getImageService().getImage(userId, loadDefaultImage);
      if (imageBytes == null) {
        context.status(HttpStatus.NOT_FOUND);
        return;
      }

      context.result(imageBytes);
    }, AuthenticationLevel.USER);
  }

}
