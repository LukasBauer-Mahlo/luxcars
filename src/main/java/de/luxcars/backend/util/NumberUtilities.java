package de.luxcars.backend.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumberUtilities {

  @Nullable
  public static Integer getInteger(@NotNull String input) {
    try {
      return Integer.valueOf(input);

    } catch (NumberFormatException ignored) {
      return null;
    }

  }

  public static Double getDouble(@NotNull String input) {
    try {
      return Double.valueOf(input);
    } catch (NumberFormatException ignored) {
      return null;
    }
  }

}
