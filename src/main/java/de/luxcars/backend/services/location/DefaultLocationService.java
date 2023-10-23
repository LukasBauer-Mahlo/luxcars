package de.luxcars.backend.services.location;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.location.object.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultLocationService implements LocationService {

  private final DatabaseDriver databaseDriver;

  public DefaultLocationService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS locations"
            + "("
            + "  id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
            + "  postalCode  VARCHAR(10)                    NOT NULL,"
            + "  city        VARCHAR(64)                    NOT NULL,"
            + "  street      VARCHAR(128)                   NOT NULL,"
            + "  houseNumber VARCHAR(10)                    NOT NULL"
            + ");"
    );
  }

  @Override
  public @Nullable Location getLocation(int id) {
    return this.databaseDriver.executeQuery("SELECT * FROM `locations` WHERE `id` = ?;", statement -> {
      statement.setInt(1, id);
    }, resultSet -> {
      if (!resultSet.next()) {
        return null;
      }

      return new Location(
          resultSet.getInt("id"),
          resultSet.getString("postalCode"),
          resultSet.getString("city"),
          resultSet.getString("street"),
          resultSet.getString("houseNumber")
      );
    });
  }

  @Override
  public @NotNull Location createLocation(@NotNull String postalCode, @NotNull String city, @NotNull String street, @NotNull String houseNumber) {
    int id = this.databaseDriver.executeUpdateWithKeys("INSERT INTO `locations` (`postalCode`, `city`, `street`, `houseNumber`) VALUES (?, ?, ?, ?);", statement -> {
      statement.setString(1, postalCode);
      statement.setString(2, city);
      statement.setString(3, street);
      statement.setString(4, houseNumber);
    }, resultSet -> {
      if (!resultSet.next()) {
        throw new RuntimeException("Unable to generate id for location");
      }

      return resultSet.getInt(1);
    });

    return new Location(
        id,
        postalCode,
        city,
        street,
        houseNumber
    );
  }
}
