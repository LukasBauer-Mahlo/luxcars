package de.luxcars.backend.services.location.object;

import org.jetbrains.annotations.NotNull;

public class Location {

  private final int id;
  private final String postalCode;
  private final String city;
  private final String street;
  private final String houseNumber;

  public Location(int id, String postalCode, String city, String street, String houseNumber) {
    this.id = id;
    this.postalCode = postalCode;
    this.city = city;
    this.street = street;
    this.houseNumber = houseNumber;
  }

  public int getId() {
    return id;
  }

  @NotNull
  public String getPostalCode() {
    return postalCode;
  }

  @NotNull
  public String getCity() {
    return city;
  }

  @NotNull
  public String getStreet() {
    return street;
  }

  @NotNull
  public String getHouseNumber() {
    return houseNumber;
  }

}
