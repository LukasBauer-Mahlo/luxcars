package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public enum TransmissionType {

  MANUELL("Manuell"),
  AUTOMATIK("Automatik"),
  SEQUENZIELL("Sequenziell");

  private final String niceName;

  TransmissionType(String niceName) {
    this.niceName = niceName;
  }

  @NotNull
  public String getNiceName() {
    return this.niceName;
  }

}