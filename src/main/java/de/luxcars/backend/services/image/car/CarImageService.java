package de.luxcars.backend.services.image.car;

import java.io.InputStream;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CarImageService {

  int insertImage(int carId, @NotNull InputStream inputStream, @NotNull CarImageType carImageType);

  void deleteImages(int carId);

  @NotNull List<Integer> getImages(int carId);

}
