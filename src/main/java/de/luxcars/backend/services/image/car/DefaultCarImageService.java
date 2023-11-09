package de.luxcars.backend.services.image.car;

import de.luxcars.backend.database.DatabaseDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import de.luxcars.backend.util.Constants;
import org.jetbrains.annotations.NotNull;

public class DefaultCarImageService implements CarImageService {

  private final DatabaseDriver databaseDriver;

  public DefaultCarImageService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;
    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `car_images` (`imageId` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `carId` INT NOT NULL, `imageType` TINYINT NOT NULL, FOREIGN KEY (`carId`) REFERENCES cars(`carId`));");

    File imageDirectory = new File(Constants.CAR_IMAGES);
    if (!imageDirectory.exists()) {
      imageDirectory.mkdir();
    }
  }

  @Override
  public int insertImage(int carId, @NotNull InputStream inputStream, @NotNull CarImageType imageType) {
    int generatedId = this.databaseDriver.executeUpdateWithKeys("INSERT INTO `car_images` (`carId`, `imageType`) VALUES (?, ?);", statement -> {
      statement.setInt(1, carId);
      statement.setInt(2, imageType.ordinal());
    }, resultSet -> {
      if (!resultSet.next()) {
        throw new RuntimeException("Unable to create database entry for car image. carId: " + carId);
      }

      return resultSet.getInt(1);
    });

    try {
      Files.copy(inputStream, Path.of(Constants.CAR_IMAGES, Integer.toString(generatedId)), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    return generatedId;
  }

  @Override
  public void deleteImages(int carId) {
    for (Integer image : this.getImages(carId)) {
      File file = new File(Constants.CAR_IMAGES, Integer.toString(image));
      if (file.exists()) {
        file.delete();
      }
    }

    this.databaseDriver.executeUpdate("DELETE FROM `car_images` WHERE `carId` = ?;", statement -> statement.setInt(1, carId));
  }

  @Override
  public @NotNull List<Integer> getImages(int carId) {
    return this.databaseDriver.executeQuery("SELECT * FROM `car_images` WHERE `carId` = ?;", statement -> {
      statement.setInt(1, carId);
    }, resultSet -> {
      List<Integer> images = new ArrayList<>();
      while (resultSet.next()) {
        images.add(resultSet.getInt("imageId"));
      }

      return images;
    });
  }

}
