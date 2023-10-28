package de.luxcars.backend.services.socket;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultWebSocketService implements WebSocketService {

  private final List<ConnectedClient> connectedClients = new ArrayList<>();

  @Override
  public void registerClient(@NotNull Session session) {
    this.connectedClients.add(new ConnectedClient(session));
  }

  @Override
  public void terminateClient(@NotNull Session session) {
    this.connectedClients.removeIf(client -> client.getSession().equals(session));
  }

  @Override
  public @Nullable ConnectedClient getClient(@NotNull Session session) {
    for (ConnectedClient client : this.connectedClients) {
      if (client.getSession().equals(session)) {
        return client;
      }
    }

    return null;
  }
}
