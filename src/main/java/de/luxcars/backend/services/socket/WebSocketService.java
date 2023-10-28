package de.luxcars.backend.services.socket;

import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WebSocketService {

  void registerClient(@NotNull Session session);

  void terminateClient(@NotNull Session session);

  @Nullable ConnectedClient getClient(@NotNull Session session);

}
