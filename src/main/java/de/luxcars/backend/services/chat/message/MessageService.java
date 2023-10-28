package de.luxcars.backend.services.chat.message;

import de.luxcars.backend.services.chat.message.object.Message;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface MessageService {

  void createMessage(int senderId, int receiverId, @NotNull String text);

  @NotNull List<Message> getMessages(int firstUser, int secondUser);

}
