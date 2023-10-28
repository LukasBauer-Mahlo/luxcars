package de.luxcars.backend.services.chat;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.object.Account;
import de.luxcars.backend.services.chat.object.ChatRoom;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultChatRoomService implements ChatRoomService {

  private final DatabaseDriver databaseDriver;
  private final AccountService accountService;

  public DefaultChatRoomService(DatabaseDriver databaseDriver, AccountService accountService) {
    this.databaseDriver = databaseDriver;
    this.accountService = accountService;

    this.databaseDriver.executeUpdate("CREATE TABLE IF NOT EXISTS `chat_rooms` (`chatRoomId` INT NOT NULL PRIMARY KEY AUTO_INCREMENT);");
    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `chat_rooms_users` (`chatRoomId` INT NOT NULL, `userId` INT NOT NULL, PRIMARY KEY (chatRoomId, userId), FOREIGN KEY (`chatRoomId`) REFERENCES chat_rooms(`chatRoomId`));"
    );
  }

  @Override
  public int getOrCreateChatRoom(int firstUser, int secondUser) {
    return this.databaseDriver.executeQuery(
        "SELECT c.chatRoomId FROM chat_rooms c INNER JOIN chat_rooms_users cu1 ON c.chatRoomId = cu1.chatRoomId INNER JOIN chat_rooms_users cu2 ON c.chatRoomId = cu2.chatRoomId WHERE cu1.userId = ? AND cu2.userId = ?;",
        statement -> {
          statement.setInt(1, firstUser);
          statement.setInt(2, secondUser);
        }, resultSet -> {
          if (resultSet.next()) {
            return resultSet.getInt("chatRoomId");
          }

          return this.databaseDriver.executeUpdateWithKeys("INSERT INTO `chat_rooms`() VALUES ();", statement -> {
          }, creationResultSet -> {
            if (!creationResultSet.next()) {
              throw new RuntimeException("Unable to create new chatroom");
            }

            int chatRoomId = creationResultSet.getInt(1);
            this.addToChatRoom(chatRoomId, firstUser);
            this.addToChatRoom(chatRoomId, secondUser);

            return chatRoomId;
          });
        });
  }

  @Override
  public @NotNull List<ChatRoom> getChatRoomsByUser(int userId) {
    String sql = "SELECT cr.chatRoomId, cr.chatPartnerId, cr.lastMessage " +
        "FROM (SELECT cru1.chatRoomId, cru2.userId AS chatPartnerId, " +
        "      (SELECT cm.text FROM chat_rooms_messages cm " +
        "       WHERE cm.chatRoomId = cru1.chatRoomId " +
        "       ORDER BY cm.time DESC " +
        "       LIMIT 1) AS lastMessage " +
        "      FROM chat_rooms_users cru1 " +
        "      INNER JOIN chat_rooms_users cru2 " +
        "      ON cru1.chatRoomId = cru2.chatRoomId " +
        "      WHERE cru1.userId = ? AND cru2.userId != ?) AS cr";

    List<ChatRoom> chatRooms = new ArrayList<>();

    this.databaseDriver.executeQuery(sql, statement -> {
      statement.setInt(1, userId);
      statement.setInt(2, userId);
    }, resultSet -> {
      while (resultSet.next()) {
        int chatRoomId = resultSet.getInt("chatRoomId");
        int chatPartnerId = resultSet.getInt("chatPartnerId");
        String lastMessage = resultSet.getString("lastMessage");

        if (chatPartnerId != 0 && lastMessage != null) {
          this.accountService.getAccount(chatPartnerId).ifPresent(chatPartner -> chatRooms.add(new ChatRoom(chatRoomId, chatPartner.toString(), lastMessage)));
        }
      }

      return null;
    });

    return chatRooms;

  }

  private void addToChatRoom(int chatRoomId, int userId) {
    this.databaseDriver.executeUpdate("INSERT INTO `chat_rooms_users` (`chatRoomId`, `userId`) VALUES (?, ?);", statement -> {
      statement.setInt(1, chatRoomId);
      statement.setInt(2, userId);
    });
  }

}
