package de.luxcars.backend.web.cars;

import com.google.gson.JsonArray;
import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.inventory.CarInventoryService;
import de.luxcars.backend.services.inventory.object.Car;
import de.luxcars.backend.util.NumberUtilities;
import io.javalin.Javalin;
import java.util.List;
import java.util.Map;

public class ListCarsRoute {

  public ListCarsRoute(Javalin javalin) {
    CarInventoryService carInventoryService = LuxCarsBackend.getInstance().getServices().getCarInventoryService();
    javalin.get("/cars", context -> {
      int brand = context.queryParamAsClass("brand", Integer.class).getOrDefault(-1);
      int[] types = extractOrdinals(context.queryParamMap(), "types");
      int[] doors = extractOrdinals(context.queryParamMap(), "doors");
      int[] places = extractOrdinals(context.queryParamMap(), "places");
      int[] fuel = extractOrdinals(context.queryParamMap(), "fuel");
      int[] transmission = extractOrdinals(context.queryParamMap(), "transmission");

      double minPrice = 0;
      double maxPrice = 0;
      String minPriceString = context.queryParamAsClass("minPrice", String.class).getOrDefault("");
      String maxPriceString = context.queryParamAsClass("maxPrice", String.class).getOrDefault("");
      if (!minPriceString.isEmpty()) {
        Double parsed = NumberUtilities.getDouble(minPriceString);
        if (parsed != null) {
          minPrice = parsed;
        }
      }

      if (!maxPriceString.isEmpty()) {
        Double parsed = NumberUtilities.getDouble(maxPriceString);
        if (parsed != null) {
          maxPrice = parsed;
        }
      }

      int minKilometer = 0;
      int maxKilometer = 0;
      String minKilometerString = context.queryParamAsClass("minKilometer", String.class).getOrDefault("");
      String maxKilometerString = context.queryParamAsClass("maxKilometer", String.class).getOrDefault("");
      if (!minKilometerString.isEmpty()) {
        Integer parsed = NumberUtilities.getInteger(minKilometerString);
        if (parsed != null) {
          minKilometer = parsed;
        }
      }

      if (!maxKilometerString.isEmpty()) {
        Integer parsed = NumberUtilities.getInteger(maxKilometerString);
        if (parsed != null) {
          maxKilometer = parsed;
        }
      }


      JsonArray cars = new JsonArray();
      for (Car car : carInventoryService.getCars(brand, types, maxPrice, minPrice, doors, places, minKilometer, maxKilometer, fuel, transmission)) {
        cars.add(car.toJson());
      }

      context.json(cars);
    });
  }

  private int[] extractOrdinals(Map<String, List<String>> paramMap, String key) {
    return paramMap.entrySet().stream()
        .filter(entry -> entry.getKey().equals(key))
        .flatMap(entry -> entry.getValue().stream())
        .filter(s -> !s.isEmpty())
        .map(Integer::parseInt)
        .mapToInt(Integer::intValue)
        .toArray();
  }

}
