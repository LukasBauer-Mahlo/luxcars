package de.luxcars.backend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface Constants {

  Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
  String ACCOUNT_ATTRIBUTE_KEY = "accountAttribute";

}
