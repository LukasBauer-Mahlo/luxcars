package de.luxcars.backend.services.inventory.object;

import org.jetbrains.annotations.NotNull;

public class Car {

  private final int id;
  private final int ownerId;
  private final CarBrand carBrand;
  private final String model;
  private final CarType carType;
  private final DoorsAmount doorsAmount;
  private final FuelType fuelType;
  private final PlacesAmount placesAmount;
  private final TransmissionType transmissionType;

  public Car(int id, int ownerId, CarBrand carBrand, String model, CarType carType, DoorsAmount doorsAmount, FuelType fuelType, PlacesAmount placesAmount, TransmissionType transmissionType) {
    this.id = id;
    this.ownerId = ownerId;
    this.carBrand = carBrand;
    this.model = model;
    this.carType = carType;
    this.doorsAmount = doorsAmount;
    this.fuelType = fuelType;
    this.placesAmount = placesAmount;
    this.transmissionType = transmissionType;
  }

  public int getId() {
    return this.id;
  }

  public int getOwnerId() {
    return this.ownerId;
  }

  @NotNull
  public CarBrand getCarBrand() {
    return this.carBrand;
  }

  @NotNull
  public String getModel() {
    return this.model;
  }

  @NotNull
  public CarType getCarType() {
    return this.carType;
  }

  @NotNull
  public DoorsAmount getDoorsAmount() {
    return this.doorsAmount;
  }

  @NotNull
  public FuelType getFuelType() {
    return this.fuelType;
  }

  @NotNull
  public PlacesAmount getPlacesAmount() {
    return this.placesAmount;
  }

  @NotNull
  public TransmissionType getTransmissionType() {
    return this.transmissionType;
  }

}
