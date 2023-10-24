package de.luxcars.backend.services.image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;

public interface ImageService {

  void insertImage(int id, @NotNull InputStream inputStream);

  @Nullable File getImage(int id, boolean defaultImage);

  void deleteImage(int id);


}