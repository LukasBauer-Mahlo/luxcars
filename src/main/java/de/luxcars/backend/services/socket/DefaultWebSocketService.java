package de.luxcars.backend.services.socket;

import io.javalin.websocket.WsContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultWebSocketService implements WebSocketService {

  private final List<ConnectedClient> connectedClients = new ArrayList<>();

  public DefaultWebSocketService() {
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
      for (ConnectedClient client : connectedClients) {
        client.getContext().sendPing();
      }
    }, 0L, 1L, TimeUnit.HOURS);
  }

  @Override
  public void registerClient(@NotNull WsContext wsContext) {
    this.connectedClients.add(new ConnectedClient(wsContext));
  }

  @Override
  public void terminateClient(@NotNull WsContext wsContext) {
    this.connectedClients.removeIf(client -> client.getContext().equals(wsContext));
  }

  @Override
  public @Nullable ConnectedClient getClient(@NotNull WsContext wsContext) {
    for (ConnectedClient client : this.connectedClients) {
      if (client.getContext().equals(wsContext)) {
        return client;
      }
    }

    return null;
  }
}
