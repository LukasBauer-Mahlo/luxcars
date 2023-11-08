package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum FuelType {

  BENZIN("Benzin"),
  DIESEL("Diesel"),
  ELEKTRO("Elektro");

  private final String niceName;

  FuelType(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}
