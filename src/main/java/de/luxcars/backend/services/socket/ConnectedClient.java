package de.luxcars.backend.services.socket;

import io.javalin.websocket.WsContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectedClient {

  private final WsContext context;
  private String selectedChat;

  public ConnectedClient(WsContext context) {
    this.context = context;
  }

  @NotNull
  public WsContext getContext() {
    return this.context;
  }

  @Nullable
  public String getSelectedChat() {
    return this.selectedChat;
  }

  public void setSelectedChat(@Nullable String selectedChat) {
    this.selectedChat = selectedChat;
  }

}
