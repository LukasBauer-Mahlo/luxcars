package de.luxcars.backend.web.cars;

import io.javalin.Javalin;

public class CarInfoRoute {

  public CarInfoRoute(Javalin javalin) {
    javalin.get("/car/info/{carId}", context -> {

    });
  }

}
