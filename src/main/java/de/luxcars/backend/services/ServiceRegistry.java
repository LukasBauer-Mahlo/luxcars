package de.luxcars.backend.services;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.DefaultAccountService;
import de.luxcars.backend.services.location.DefaultLocationService;
import de.luxcars.backend.services.location.LocationService;
import de.luxcars.backend.services.token.DefaultTokenService;
import de.luxcars.backend.services.token.TokenService;
import org.jetbrains.annotations.NotNull;

public class ServiceRegistry {

  private final AccountService accountService;
  private final TokenService tokenService;
  private final LocationService locationService;

  public ServiceRegistry(DatabaseDriver databaseDriver) {
    this.accountService = new DefaultAccountService(databaseDriver);
    this.tokenService = new DefaultTokenService(databaseDriver);

    this.locationService = new DefaultLocationService(databaseDriver);
  }

  @NotNull
  public AccountService getAccountService() {
    return this.accountService;
  }

  @NotNull
  public TokenService getTokenService() {
    return this.tokenService;
  }

  @NotNull
  public LocationService getLocationService() {
    return this.locationService;
  }

}
