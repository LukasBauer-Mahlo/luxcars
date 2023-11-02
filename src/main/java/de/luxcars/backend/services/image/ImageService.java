package de.luxcars.backend.services.image;

import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ImageService {

  void insertImage(int id, @NotNull InputStream inputStream);

  byte @Nullable [] getImage(int id, boolean defaultImage);

  void deleteImage(int id);


}