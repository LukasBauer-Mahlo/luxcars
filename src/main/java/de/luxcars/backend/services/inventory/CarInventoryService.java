package de.luxcars.backend.services.inventory;

import de.luxcars.backend.services.inventory.object.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CarInventoryService {

  @NotNull List<Car> getCars(
      int brand, int @NotNull [] carTypes,
      double maxPrice, double minPrice, int @NotNull [] doorsAmounts, int @NotNull [] placesAmounts,
      int minKilometres, int maxKilometres, int @NotNull [] fuelTypes, int @NotNull [] transmissionTypes
  );

  @NotNull Car createCar(int ownerId, @NotNull CarBrand carBrand, @NotNull String model, @NotNull String description, double price, @NotNull CarType carType, @NotNull DoorsAmount doorsAmount, @NotNull PlacesAmount placesAmount,
      int kilometres, @NotNull FuelType fuelType, @NotNull TransmissionType transmissionType);

  @Nullable Car getCar(int carId);

}
