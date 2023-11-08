package de.luxcars.backend.services.inventory;

import de.luxcars.backend.services.inventory.object.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CarInventoryService {

  @NotNull List<Car> getCars(
      @NotNull CarBrand carBrand, @NotNull CarType[] carTypes,
      double maxPrice, double minPrice, @NotNull DoorsAmount[] doorsAmounts, @NotNull PlacesAmount[] placesAmounts,
      int minKilometres, int maxKilometres, @NotNull FuelType[] fuelTypes, @NotNull TransmissionType[] transmissionTypes
  );

  @NotNull Car createCar(int ownerId, @NotNull CarBrand carBrand, @NotNull String model, double price, @NotNull CarType carType, @NotNull DoorsAmount doorsAmount, @NotNull PlacesAmount placesAmount, int kilometres, @NotNull FuelType fuelType, @NotNull TransmissionType transmissionType);

}
