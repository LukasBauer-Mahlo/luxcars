package de.luxcars.backend.services.inventory;

import de.luxcars.backend.database.DatabaseDriver;

public class DefaultCarInventoryService {

  private final DatabaseDriver databaseDriver;

  public DefaultCarInventoryService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS (`carId` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `name` VARCHAR(100), `price` INT NOT NULL, `brand` TINYINT NOT NULL, `type` TINYINT NOT NULL, `doors` TINYINT NOT NULL, `places` TINYINT NOT NULL, `kilometres` INT NOT NULL, `fuel` TINYINT NOT NULL, `transmission` TINYINT NOT NULL);"
    );
  }
}
