package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum DoorsAmount {

  TWO_DOORS("2 Türen"),
  THREE_DOORS("3 Türen"),
  FIVE_DOORS("5 Türen");

  private final String niceName;

  DoorsAmount(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}
