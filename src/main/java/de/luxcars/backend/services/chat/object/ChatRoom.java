package de.luxcars.backend.services.chat.object;

import org.jetbrains.annotations.NotNull;

public class ChatRoom {

  private final int id;
  private final String otherDisplayName;
  private final String lastMessage;

  public ChatRoom(int id, String otherDisplayName, String lastMessage) {
    this.id = id;
    this.otherDisplayName = otherDisplayName;
    this.lastMessage = lastMessage;
  }

  public int getId() {
    return this.id;
  }

  @NotNull
  public String getOtherDisplayName() {
    return this.otherDisplayName;
  }

  @NotNull
  public String getLastMessage() {
    return this.lastMessage;
  }

}
