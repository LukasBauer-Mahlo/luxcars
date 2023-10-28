package de.luxcars.backend.services.message;

import de.luxcars.backend.services.message.object.Chat;
import de.luxcars.backend.services.message.object.Message;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface MessageService {

  @NotNull Message createMessage(int senderUserId, int receiverId, @NotNull String text);

  @NotNull List<Message> getMessages(int firstUser, int secondUser);

  @NotNull List<Chat> getChats(int userId);

}
