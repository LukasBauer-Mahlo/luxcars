package de.luxcars.backend.services.chat.object;

import org.jetbrains.annotations.NotNull;

public class ChatRoom {

  private final int chatRoomId;
  private final int otherUserId;
  private final String otherDisplayName;
  private final String lastMessage;
  private final int unreadMessages;

  public ChatRoom(int chatRoomId, int otherUserId, String otherDisplayName, String lastMessage, int unreadMessages) {
    this.chatRoomId = chatRoomId;
    this.otherUserId = otherUserId;
    this.otherDisplayName = otherDisplayName;
    this.lastMessage = lastMessage;
    this.unreadMessages = unreadMessages;
  }

  public int getChatRoomId() {
    return this.chatRoomId;
  }

  public int getOtherUserId() {
    return this.otherUserId;
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
