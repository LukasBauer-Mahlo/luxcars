package de.luxcars.backend.services.chat.message.object;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;

public class Message {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

  private final int senderId;
  private final String text;

  private final String date;
  private final String time;

  public Message(int senderId, String text, long timestamp) {
    this.senderId = senderId;
    this.text = text;

    String[] splitedTime = SIMPLE_DATE_FORMAT.format(timestamp).split(" ");
    this.date = splitedTime[1];
    this.time = splitedTime[0];
  }

  public int getSenderId() {
    return this.senderId;
  }

  @NotNull
  public String getText() {
    return this.text;
  }

  @NotNull
  public String getDate() {
    return this.date;
  }

  @NotNull
  public String getTime() {
    return this.time;
  }

}
