package de.luxcars.backend.services.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultImageService implements ImageService {

  private static final String IMAGE_DIRECTORY = "profileImages";
  private byte[] defaultImage = null;

  public DefaultImageService() {
    try {
      this.defaultImage = Files.readAllBytes(Path.of(IMAGE_DIRECTORY, "DEFAULT_IMAGE"));

      File imageDirectory = new File(IMAGE_DIRECTORY);
      if (!imageDirectory.exists()) {
        imageDirectory.mkdir();
      }

    } catch (IOException exception) {
      exception.printStackTrace();
    }

  }

  @Override
  public void insertImage(int id, @NotNull InputStream inputStream) {
    try {
      Files.copy(inputStream, Path.of(IMAGE_DIRECTORY, Integer.toString(id)), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public byte @Nullable [] getImage(int id, boolean defaultImage) {
    try {
      return Files.readAllBytes(Path.of(IMAGE_DIRECTORY, Integer.toString(id)));
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    if (defaultImage) {
      return this.defaultImage;
    }

    return null;
  }

  @Override
  public void deleteImage(int id) {
    File file = new File(IMAGE_DIRECTORY, Integer.toString(id));
    if (file.exists()) {
      file.delete();
    }
  }

}
