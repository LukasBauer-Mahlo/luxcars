package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public class OwnerInfo {

  private final String name;
  private final String location;

  public OwnerInfo(String name, String location) {
    this.name = name;
    this.location = location;
  }

  @NotNull
  public String getName() {
    return this.name;
  }

  public String getLocation() {
    return this.location;
  }

}
