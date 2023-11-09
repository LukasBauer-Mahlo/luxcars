package de.luxcars.backend.web.cars;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.image.car.CarImageType;
import de.luxcars.backend.services.inventory.object.Car;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.util.NumberUtilities;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CarImageRoute {

  public CarImageRoute(Javalin javalin) {
    javalin.get("/car/image/{imageId}", context -> {
      try {
        context.result(Files.readAllBytes(Path.of(Constants.CAR_IMAGES, context.pathParam("imageId"))));
        return;
      } catch (IOException ignored) {
        ignored.printStackTrace();
      }

      context.status(HttpStatus.NOT_FOUND);
    });

    javalin.post("/car/image/{carId}", context -> {
      Integer carId = NumberUtilities.getInteger(context.pathParam("carId"));
      if (carId == null) {
        context.result("Unable to find car with given id");
        context.status(HttpStatus.NOT_FOUND);
        return;
      }

      Car car = LuxCarsBackend.getInstance().getServices().getCarInventoryService().getCar(carId);
      if (car == null) {
        context.result("Unable to find car with given id");
        context.status(HttpStatus.NOT_FOUND);
        return;
      }

      boolean primaryImage = context.queryParam("primary") != null;

      for (UploadedFile uploadedFile : context.uploadedFiles()) {
        LuxCarsBackend.getInstance().getServices().getCarImageService().insertImage(car.getId(), uploadedFile.content(), primaryImage ? CarImageType.PRIMARY_IMAGE : CarImageType.SECONDARY_IMAGE);
      }
    }, AuthenticationLevel.ADMIN); // Temporary set the auth level to admin
  }

}
