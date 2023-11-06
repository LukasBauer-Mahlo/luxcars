package de.luxcars.backend.services.chat.message.read;

public interface MessageReadService {

  void addUnreadMessage(int userId, int chatId, long messageTime);

  void clearUnreadMessages(int userId, int chatId);

  int getUnreadMessages(int userId, int chatId);

}
