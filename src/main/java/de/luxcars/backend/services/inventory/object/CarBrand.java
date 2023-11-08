package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum CarBrand {
  ASTON_MARTIN("Aston Martin"),
  AUDI("Audi"),
  BENTLEY("Bentley"),
  BMW("BMW"),
  BUGATTI("Bugatti"),
  FERRARI("Ferrari"),
  JAGUAR("Jaguar"),
  KTM("KTM"),
  LAMBORGHINI("Lamborghini"),
  MC_LAREN("McLaren"),
  MERCEDES("Mercedes"),
  PAGANI("Pagani"),
  PORSCHE("Porsche");

  private final String niceName;

  CarBrand(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}
