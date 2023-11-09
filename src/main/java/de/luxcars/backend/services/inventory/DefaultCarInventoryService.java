package de.luxcars.backend.services.inventory;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.inventory.object.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
  public @NotNull List<Car> getCars(int brand, int @NotNull [] carTypes, double maxPrice, double minPrice, int @NotNull [] doorsAmounts, int @NotNull [] placesAmounts, int minKilometres,
      int maxKilometres, int @NotNull [] fuelTypes, int @NotNull [] transmissionTypes) {

    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM `cars` WHERE 1=1");

    if (brand != -1) {
      query.append(" AND `brand` = ").append(brand);
    }

    if (carTypes.length > 0) {
      query.append(" AND `type` IN (");
      for (int carType : carTypes) {
        query.append(carType).append(",");
      }
      query.deleteCharAt(query.length() - 1);
      query.append(")");
    }

    if (maxPrice != 0) {
      query.append(" AND `price` <= ").append(maxPrice);
    }

    if (minPrice != 0) {
      query.append(" AND `price` >= ").append(minPrice);
    }

    if (doorsAmounts.length > 0) {
      query.append(" AND `doors` IN (");
      for (int doorsAmount : doorsAmounts) {
        query.append(doorsAmount).append(",");
      }
      query.deleteCharAt(query.length() - 1);
      query.append(")");
    }

    if (placesAmounts.length > 0) {
      query.append(" AND `places` IN (");
      for (int placesAmount : placesAmounts) {
        query.append(placesAmount).append(",");
      }
      query.deleteCharAt(query.length() - 1);
      query.append(")");
    }

    if (maxKilometres != 0) {
      query.append(" AND `kilometres` <= ").append(maxKilometres);
    }

    if (minKilometres != 0) {
      query.append(" AND `kilometres` >= ").append(minKilometres);
    }

    if (fuelTypes.length > 0) {
      query.append(" AND `fuel` IN (");
      for (int fuelType : fuelTypes) {
        query.append(fuelType).append(",");
      }
      query.deleteCharAt(query.length() - 1);
      query.append(")");
    }

    if (transmissionTypes.length > 0) {
      query.append(" AND `transmission` IN (");
      for (int transmissionType : transmissionTypes) {
        query.append(transmissionType).append(",");
      }
      query.deleteCharAt(query.length() - 1);
      query.append(")");
    }

    query.append(";");

    return this.databaseDriver.executeQuery(query.toString(), resultSet -> {
      List<Car> cars = new ArrayList<>();
      while (resultSet.next()) {
        cars.add(new Car(
            resultSet.getInt("carId"),
            resultSet.getInt("ownerId"),
            CarBrand.values()[resultSet.getInt("brand")],
            resultSet.getString("model"),
            CarType.values()[resultSet.getInt("type")],
            DoorsAmount.values()[resultSet.getInt("doors")],
            FuelType.values()[resultSet.getInt("fuel")],
            PlacesAmount.values()[resultSet.getInt("places")],
            TransmissionType.values()[resultSet.getInt("transmission")],
            resultSet.getDouble("price"),
            resultSet.getInt("kilometres")
        ));
      }
      return cars;
    });
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
              transmissionType,
              price, kilometres);
        });
  }

}
