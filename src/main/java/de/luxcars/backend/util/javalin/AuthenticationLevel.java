package de.luxcars.backend.util.javalin;

import io.javalin.security.RouteRole;

public enum AuthenticationLevel implements RouteRole {

  USER,
  ADMIN

}
