package de.luxcars.backend.services.account.object;

import org.jetbrains.annotations.NotNull;

public class Account {

  private final int id;
  private final String mail;
  private String firstName;
  private String lastName;
  private String password;
  private boolean disabled;
  private boolean administrator;

  public Account(int id, String mail, String firstName, String lastName, String password, boolean disabled, boolean administrator) {
    this.id = id;
    this.mail = mail;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.disabled = disabled;
    this.administrator = administrator;
  }

  public int getId() {
    return this.id;
  }

  @NotNull
  public String getMail() {
    return this.mail;
  }

  @NotNull
  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(@NotNull String firstName) {
    this.firstName = firstName;
  }

  @NotNull
  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(@NotNull String lastName) {
    this.lastName = lastName;
  }

  @NotNull
  public String getPassword() {
    return this.password;
  }

  public void setPassword(@NotNull String password) {
    this.password = password;
  }

  public boolean isDisabled() {
    return this.disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isAdministrator() {
    return this.administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  @Override
  public String toString() {
    return this.firstName + " " + this.lastName;
  }
}
