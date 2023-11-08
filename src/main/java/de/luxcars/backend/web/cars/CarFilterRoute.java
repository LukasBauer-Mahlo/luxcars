package de.luxcars.backend.web.cars;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.luxcars.backend.services.inventory.object.CarBrand;
import de.luxcars.backend.services.inventory.object.CarType;
import de.luxcars.backend.services.inventory.object.DoorsAmount;
import de.luxcars.backend.services.inventory.object.FuelType;
import de.luxcars.backend.services.inventory.object.PlacesAmount;
import de.luxcars.backend.services.inventory.object.TransmissionType;
import io.javalin.Javalin;

public class CarFilterRoute {

  public CarFilterRoute(Javalin javalin) {
    javalin.get("/car/filter", context -> {
      JsonObject globalResponse = new JsonObject();

      JsonArray brands = new JsonArray();
      for (CarBrand carBrand : CarBrand.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", carBrand.ordinal());
        jsonObject.addProperty("name", carBrand.getNiceName());
        brands.add(jsonObject);
      }

      globalResponse.add("brands", brands);

      JsonArray types = new JsonArray();
      for (CarType carType : CarType.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", carType.ordinal());
        jsonObject.addProperty("name", carType.getNiceName());
        types.add(jsonObject);
      }

      globalResponse.add("types", types);

      JsonArray transmissions = new JsonArray();
      for (TransmissionType transmissionType : TransmissionType.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", transmissionType.ordinal());
        jsonObject.addProperty("name", transmissionType.getNiceName());
        transmissions.add(jsonObject);
      }

      globalResponse.add("transmissions", transmissions);

      JsonArray fuelTypes = new JsonArray();
      for (FuelType fuelType : FuelType.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", fuelType.ordinal());
        jsonObject.addProperty("name", fuelType.getNiceName());
        fuelTypes.add(jsonObject);
      }

      globalResponse.add("fuelTypes", fuelTypes);

      JsonArray doorAmounts = new JsonArray();
      for (DoorsAmount doorAmount : DoorsAmount.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", doorAmount.ordinal());
        jsonObject.addProperty("name", doorAmount.getNiceName());
        doorAmounts.add(jsonObject);
      }

      globalResponse.add("doorAmounts", doorAmounts);

      JsonArray placesAmounts = new JsonArray();
      for (PlacesAmount placesAmount : PlacesAmount.values()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", placesAmount.ordinal());
        jsonObject.addProperty("name", placesAmount.getNiceName());
        placesAmounts.add(jsonObject);
      }

      globalResponse.add("placesAmount", placesAmounts);

      context.json(globalResponse);
    });

  }

}
