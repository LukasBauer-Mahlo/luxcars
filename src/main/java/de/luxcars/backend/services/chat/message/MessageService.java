package de.luxcars.backend.services.chat.message;

import de.luxcars.backend.services.chat.message.object.Message;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface MessageService {

  void createMessage(int senderId, int receiverId, @NotNull String text);

  @NotNull List<Message> getMessages(int chatRoomId);

  @NotNull List<Message> getMessages(int firstUser, int secondUser);

}
