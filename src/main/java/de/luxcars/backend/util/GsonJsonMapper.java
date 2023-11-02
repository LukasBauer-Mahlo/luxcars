package de.luxcars.backend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.json.JsonMapper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

public class GsonJsonMapper implements JsonMapper {

  private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  @NotNull
  @Override
  public String toJsonString(@NotNull Object obj, @NotNull Type type) {
    return this.gson.toJson(obj, type);
  }

  @NotNull
  @Override
  public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
    return this.gson.fromJson(json, targetType);
  }

  @NotNull
  @Override
  public <T> T fromJsonStream(@NotNull InputStream json, @NotNull Type targetType) {
    return this.gson.fromJson(new InputStreamReader(json, StandardCharsets.UTF_8), targetType);
  }
}
