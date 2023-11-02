package de.luxcars.backend.services.chat.read;

public interface ChatReadService {

  void readChat(int chatId, int userId);

  void handleNewMessage(int chatId, int userId);

  int getUnreadChats(int userId);

}