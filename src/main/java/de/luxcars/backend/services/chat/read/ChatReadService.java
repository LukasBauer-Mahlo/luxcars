package de.luxcars.backend.services.chat.read;

import de.luxcars.backend.web.chat.ChatWebSocket;
import org.jetbrains.annotations.NotNull;

public interface ChatReadService {

  void readChat(int chatId, int userId);

  void handleNewMessage(int chatId, int userId);

  int getUnreadChats(int userId);

}