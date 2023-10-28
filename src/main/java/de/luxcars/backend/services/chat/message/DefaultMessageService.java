package de.luxcars.backend.services.chat.message;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.chat.ChatRoomService;
import de.luxcars.backend.services.chat.message.object.Message;
import java.util.ArrayList;
import java.util.List;
import de.luxcars.backend.web.chat.ChatWebSocket;
import org.jetbrains.annotations.NotNull;

public class DefaultMessageService implements MessageService {

  private final DatabaseDriver databaseDriver;
  private final ChatRoomService chatRoomService;
  private ChatWebSocket chatWebSocket;

  public DefaultMessageService(DatabaseDriver databaseDriver, ChatRoomService chatRoomService) {
    this.databaseDriver = databaseDriver;
    this.chatRoomService = chatRoomService;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `chat_rooms_messages` (\n"
            + "    `chatRoomId` INT NOT NULL,\n"
            + "    `senderId` INT NOT NULL,\n"
            + "    `time` BIGINT NOT NULL,\n"
            + "    `text` TEXT NOT NULL,\n"
            + "    PRIMARY KEY (`chatRoomId`, `senderId`, `time`)\n"
            + ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n"
    );
  }

  @Override
  public void createMessage(int senderId, int receiverId, @NotNull String text) {
    long time = System.currentTimeMillis();
    int chatRoomId = this.chatRoomService.getOrCreateChatRoom(senderId, receiverId);
    this.databaseDriver.executeUpdate("INSERT INTO `chat_rooms_messages` (`chatRoomId`, `senderId`, `time`, `text`) VALUES (?, ?, ?, ?);", statement -> {
      statement.setInt(1, chatRoomId);
      statement.setInt(2, senderId);
      statement.setLong(3, time);
      statement.setString(4, text);
    });

    this.chatWebSocket.publishChatRoomUpdate(chatRoomId);
  }

  @Override
  public @NotNull List<Message> getMessages(int chatRoomId) {
    return this.databaseDriver.executeQuery("SELECT * FROM `chat_rooms_messages` WHERE chatRoomId = ? ORDER BY time ASC;", statement -> {
      statement.setInt(1, chatRoomId);
    }, resultSet -> {
      List<Message> messages = new ArrayList<>();
      while (resultSet.next()) {
        messages.add(new Message(
            resultSet.getInt("senderId"),
            resultSet.getString("text"),
            resultSet.getLong("time")
        ));
      }

      return messages;
    });
  }

  @Override
  public @NotNull List<Message> getMessages(int firstUser, int secondUser) {
    return this.getMessages(this.chatRoomService.getOrCreateChatRoom(firstUser, secondUser));
  }

  @Override
  public void setChatWebSocket(@NotNull ChatWebSocket chatWebSocket) {
    this.chatWebSocket = chatWebSocket;
  }

}
