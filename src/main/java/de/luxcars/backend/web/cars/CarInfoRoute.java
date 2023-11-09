package de.luxcars.backend.web.cars;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.inventory.CarInventoryService;
import de.luxcars.backend.services.inventory.object.Car;
import de.luxcars.backend.util.NumberUtilities;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class CarInfoRoute {

  public CarInfoRoute(Javalin javalin) {
    CarInventoryService carInventoryService = LuxCarsBackend.getInstance().getServices().getCarInventoryService();
    javalin.get("/car/info/{carId}", context -> {
      Integer carId = NumberUtilities.getInteger(context.pathParam("carId"));
      if (carId == null) {
        context.status(HttpStatus.BAD_REQUEST);
        return;
      }

      Car car = carInventoryService.getCar(carId);
      if (car == null) {
        context.status(HttpStatus.NOT_FOUND);
        return;
      }

      context.json(car.toJson(true));
    });
  }

}
