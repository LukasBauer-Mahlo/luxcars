package de.luxcars.backend;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.database.MySQLDatabaseDriver;
import de.luxcars.backend.services.ServiceRegistry;
import de.luxcars.backend.util.GsonJsonMapper;
import de.luxcars.backend.util.javalin.DefaultAccessManager;
import de.luxcars.backend.web.auth.AuthenticationRoutes;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jetbrains.annotations.NotNull;

public class LuxCarsBackend {

  private static final LuxCarsBackend LUX_CARS_BACKEND = new LuxCarsBackend();

  private DatabaseDriver databaseDriver;
  private ServiceRegistry serviceRegistry;

  public static void main(String[] args) {
    LUX_CARS_BACKEND.start();
  }

  private void start() {
    this.databaseDriver = new MySQLDatabaseDriver();
    this.serviceRegistry = new ServiceRegistry(this.databaseDriver);

    Javalin javalin = Javalin.create().updateConfig(config -> {
      config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
      config.showJavalinBanner = false;
      config.jsonMapper(new GsonJsonMapper());
      config.accessManager(new DefaultAccessManager(this.serviceRegistry.getTokenService(), this.serviceRegistry.getAccountService()));
    }).start(1888);
    //register web handlers

    new AuthenticationRoutes(
        javalin,
        this.serviceRegistry.getAccountService(),
        this.serviceRegistry.getImageService(),
        this.serviceRegistry.getTokenService()
    );
  }

  @NotNull
  public static LuxCarsBackend getInstance() {
    return LUX_CARS_BACKEND;
  }

  @NotNull
  public DatabaseDriver getDatabaseDriver() {
    return this.databaseDriver;
  }

  @NotNull
  public ServiceRegistry getServices() {
    return this.serviceRegistry;
  }

}
