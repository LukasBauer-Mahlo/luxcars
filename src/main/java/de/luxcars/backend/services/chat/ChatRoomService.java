package de.luxcars.backend.services.chat;

import de.luxcars.backend.services.chat.object.ChatRoom;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ChatRoomService {

  int getOrCreateChatRoom(int firstUser, int secondUser);

  @NotNull List<ChatRoom> getChatRoomsByUser(int userId);

}
