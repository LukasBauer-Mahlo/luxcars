package de.luxcars.backend.services.chat;

import de.luxcars.backend.services.chat.object.ChatRoom;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ChatRoomService {

  int getOrCreateChatRoom(int firstUser, int secondUser);

  int getChatPartnerId(int chatRoomId, int requesterId);

  @NotNull List<ChatRoom> getChatRoomsByUser(int userId);

}
