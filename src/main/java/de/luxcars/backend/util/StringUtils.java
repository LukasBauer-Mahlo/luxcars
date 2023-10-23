package de.luxcars.backend.util;

import java.util.Random;

public class StringUtils {

  private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

  public static String randomString(int length) {
    char[] chars = new char[length];
    Random random = new Random();

    for (int i = 0; i < chars.length; i++) {
      chars[i] = ALPHABET[random.nextInt(ALPHABET.length)];
    }

    return String.valueOf(chars);
  }


}
