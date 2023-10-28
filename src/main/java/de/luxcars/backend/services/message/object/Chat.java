package de.luxcars.backend.services.message.object;

import org.jetbrains.annotations.NotNull;

public class Chat {

  private final int userId;
  private final String name;
  private final String lastMessage;

  public Chat(int userId, String name, String lastMessage) {
    this.userId = userId;
    this.name = name;
    this.lastMessage = lastMessage;
  }

  public int getUserId() {
    return this.userId;
  }

  @NotNull
  public String getName() {
    return this.name;
  }

  @NotNull
  public String getLastMessage() {
    return this.lastMessage;
  }

}
