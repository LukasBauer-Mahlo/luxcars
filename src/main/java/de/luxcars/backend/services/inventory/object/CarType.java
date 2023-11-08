package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum CarType {

  SPORTWAGEN("Sportwagen"),
  SUV("SUV"),
  KOMBI("Kombi"),
  EXOTE("Exote"),
  RENNWAGEN("Rennwagen");

  private final String niceName;

  CarType(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}
