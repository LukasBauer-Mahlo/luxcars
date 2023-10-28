package de.luxcars.backend.services.socket;

import io.javalin.websocket.WsContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WebSocketService {

  void registerClient(@NotNull WsContext session);

  void terminateClient(@NotNull WsContext session);

  @Nullable ConnectedClient getClient(@NotNull WsContext session);

}
