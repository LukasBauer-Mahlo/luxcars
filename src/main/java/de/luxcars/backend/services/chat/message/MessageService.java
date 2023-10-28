package de.luxcars.backend.services.chat.message;

import de.luxcars.backend.services.chat.message.object.Message;
import de.luxcars.backend.web.chat.ChatWebSocket;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface MessageService {

  void createMessage(int senderId, int receiverId, @NotNull String text);

  @NotNull List<Message> getMessages(int chatRoomId);

  @NotNull List<Message> getMessages(int firstUser, int secondUser);

  void setChatWebSocket(@NotNull ChatWebSocket chatWebSocket);

}
