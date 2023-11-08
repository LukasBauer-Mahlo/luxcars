package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum PlacesAmount {

  TWO_PLACES("2 Sitze"),
  FOUR_PLACES("4 Sitze"),
  FIVE_PLACES("5 Sitze");

  private final String niceName;

  PlacesAmount(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}
