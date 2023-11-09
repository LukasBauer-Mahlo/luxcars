package de.luxcars.backend.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccountValidator {

  public static boolean isPasswordValid(@NotNull String password) {
    if (password.trim().isEmpty()) {
      return false;
    }

    return password.length() <= 256;
  }

  public static boolean isNameValid(@NotNull String... name) {
    for (String any : name) {
      if (any.trim().isEmpty() || any.length() > 32) {
        return false;
      }
    }

    return true;
  }

  public static boolean isLocationValid(@NotNull String location) {
    return location.length() <= 50;
  }

}
