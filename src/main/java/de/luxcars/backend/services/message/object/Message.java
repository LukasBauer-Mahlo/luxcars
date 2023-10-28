package de.luxcars.backend.services.message.object;

import org.jetbrains.annotations.NotNull;

public class Message {

  private final int senderId;
  private final int receiverId;
  private final String date;
  private final String time;
  private final String text;

  public Message(int senderId, int receiverId, String date, String time, String text) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.date = date;
    this.time = time;
    this.text = text;
  }

  public int getSenderId() {
    return this.senderId;
  }

  public int getReceiverId() {
    return this.receiverId;
  }

  @NotNull
  public String getDate() {
    return this.date;
  }

  @NotNull
  public String getTime() {
    return this.time;
  }

  @NotNull
  public String getText() {
    return this.text;
  }

}
