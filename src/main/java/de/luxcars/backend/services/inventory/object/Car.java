package de.luxcars.backend.services.inventory.object;

import com.google.gson.JsonObject;
import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Car {

  private final int id;
  private final int ownerId;
  private final CarBrand carBrand;
  private final String model;
  private final String description;
  private final CarType carType;
  private final DoorsAmount doorsAmount;
  private final FuelType fuelType;
  private final PlacesAmount placesAmount;
  private final TransmissionType transmissionType;
  private final double price;
  private final int kilometres;

  private transient OwnerInfo ownerInfo; // marks as transient to prevent object serialization
  private transient List<Integer> images; // marks as transient to prevent object serialization

  public Car(int id, int ownerId, CarBrand carBrand, String model, String description, CarType carType, DoorsAmount doorsAmount, FuelType fuelType, PlacesAmount placesAmount,
      TransmissionType transmissionType,
      double price, int kilometres) {
    this.id = id;
    this.ownerId = ownerId;
    this.carBrand = carBrand;
    this.model = model;
    this.description = description;
    this.carType = carType;
    this.doorsAmount = doorsAmount;
    this.fuelType = fuelType;
    this.placesAmount = placesAmount;
    this.transmissionType = transmissionType;
    this.price = price;
    this.kilometres = kilometres;
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
  public String getDescription() {
    return this.description;
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

  public double getPrice() {
    return this.price;
  }

  public int getKilometres() {
    return this.kilometres;
  }

  @NotNull
  public OwnerInfo getOwnerInfo() {
    if (this.ownerInfo == null) {
      Account account = LuxCarsBackend.getInstance().getServices().getAccountService().getAccount(this.ownerId).orElseThrow(); // not possible because car is linked to accounts table
      this.ownerInfo = new OwnerInfo(account.getFirstName() + " " + account.getLastName(), "Not implemented yet."); // TODO
    }

    return this.ownerInfo;
  }

  @NotNull
  public List<Integer> getImages() {
    if (this.images == null) {
      this.images = LuxCarsBackend.getInstance().getServices().getCarImageService().getImages(this.id);
    }

    return this.images;
  }

  public JsonObject toJson(boolean ownerInfo) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", this.id);
    jsonObject.addProperty("ownerId", this.ownerId);
    jsonObject.addProperty("brand", this.carBrand.getNiceName());
    jsonObject.addProperty("model", this.model);
    jsonObject.addProperty("description", this.description);
    jsonObject.addProperty("type", this.carType.getNiceName());
    jsonObject.addProperty("doors", this.doorsAmount.getNiceName());
    jsonObject.addProperty("fuel", this.fuelType.getNiceName());
    jsonObject.addProperty("places", this.placesAmount.getNiceName());
    jsonObject.addProperty("transmission", this.transmissionType.getNiceName());
    jsonObject.addProperty("price", this.price);
    jsonObject.addProperty("kilometres", this.kilometres);

    jsonObject.add("images", Constants.GSON.toJsonTree(this.getImages()));
    if (ownerInfo) {
      jsonObject.add("ownerInfo", Constants.GSON.toJsonTree(this.getOwnerInfo()));
    }

    return jsonObject;
  }

}
