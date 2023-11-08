package de.luxcars.backend.services.inventory;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.inventory.object.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultCarInventoryService implements CarInventoryService {

  private final DatabaseDriver databaseDriver;

  public DefaultCarInventoryService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `cars` (`carId` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `ownerId` INT NOT NULL, `brand` TINYINT NOT NULL, `model` VARCHAR(100), `price` DOUBLE NOT NULL, `type` TINYINT NOT NULL, `doors` TINYINT NOT NULL, `places` TINYINT NOT NULL, `kilometres` INT NOT NULL, `fuel` TINYINT NOT NULL, `transmission` TINYINT NOT NULL, FOREIGN KEY (`ownerId`) REFERENCES accounts(`id`));"
    );
  }

  @Override
  public @NotNull List<Car> getCars(@NotNull CarBrand carBrand, @NotNull CarType[] carTypes, double maxPrice, double minPrice, @NotNull DoorsAmount[] doorsAmounts,
      @NotNull PlacesAmount[] placesAmounts, int minKilometres, int maxKilometres, @NotNull FuelType[] fuelTypes, @NotNull TransmissionType[] transmissionTypes) {

    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM `cars` WHERE `carBrand` = ? AND");

  }

  @Override
  public @NotNull Car createCar(int ownerId, @NotNull CarBrand carBrand, @NotNull String model, double price, @NotNull CarType carType, @NotNull DoorsAmount doorsAmount,
      @NotNull PlacesAmount placesAmount, int kilometres, @NotNull FuelType fuelType,
      @NotNull TransmissionType transmissionType) {
    return this.databaseDriver.executeUpdateWithKeys(
        "INSERT INTO `cars` (`ownerId`, `brand`, `model`, `price`, `type`, `doors`, `places`, `kilometres`, `fuel`, `transmission`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", statement -> {
          statement.setInt(1, ownerId);
          statement.setInt(2, carBrand.ordinal());
          statement.setString(3, model);
          statement.setDouble(4, price);
          statement.setInt(5, carType.ordinal());
          statement.setInt(6, doorsAmount.ordinal());
          statement.setInt(7, placesAmount.ordinal());
          statement.setInt(8, kilometres);
          statement.setInt(9, fuelType.ordinal());
          statement.setInt(10, transmissionType.ordinal());
        }, resultSet -> {
          if (!resultSet.next()) {
            throw new RuntimeException("Unable to generate id for new car");
          }

          return new Car(
              resultSet.getInt(1),
              ownerId,
              carBrand,
              model,
              carType,
              doorsAmount,
              fuelType,
              placesAmount,
              transmissionType
          );
        });
  }

}
