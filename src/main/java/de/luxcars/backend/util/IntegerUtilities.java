package de.luxcars.backend.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntegerUtilities {

  @Nullable
  public static Integer getFromString(@NotNull String input) {
    try {
      return Integer.valueOf(input);

    } catch (NumberFormatException ignored) {
      return null;
    }

  }

}
