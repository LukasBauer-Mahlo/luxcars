package de.luxcars.backend.services.socket;

import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectedClient {

  private final Session session;
  private String selectedChat;

  public ConnectedClient(Session session) {
    this.session = session;
  }

  @NotNull
  public Session getSession() {
    return this.session;
  }

  @Nullable
  public String getSelectedChat() {
    return this.selectedChat;
  }

  public void setSelectedChat(@Nullable String selectedChat) {
    this.selectedChat = selectedChat;
  }

}
