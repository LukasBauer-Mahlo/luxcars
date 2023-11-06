package de.luxcars.backend.services.chat.message.read;

import de.luxcars.backend.database.DatabaseDriver;

public class DefaultMessageReadService implements MessageReadService {

  private final DatabaseDriver databaseDriver;

  public DefaultMessageReadService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `user_message_unread` (`userId` INT NOT NULL, `chatId` INT NOT NULL, `time` BIGINT NOT NULL, PRIMARY KEY (`userId`, `chatId`, `time`), FOREIGN KEY (`userId`) REFERENCES accounts(`id`), FOREIGN KEY (`chatId`) REFERENCES chat_rooms(`chatRoomId`));"
    );
  }

  @Override
  public void addUnreadMessage(int userId, int chatId, long messageTime) {
    this.databaseDriver.executeUpdate("INSERT INTO `user_message_unread` (`userId`, `chatId`, `time`) VALUES (?, ?, ?);", statement -> {
      statement.setInt(1, userId);
      statement.setInt(2, chatId);
      statement.setLong(3, messageTime);
    });
  }

  @Override
  public void clearUnreadMessages(int userId, int chatId) {
    this.databaseDriver.executeUpdate("DELETE FROM `user_message_unread` WHERE `userId` = ? AND `chatId` = ?;", statement -> {
      statement.setInt(1, userId);
      statement.setInt(2, chatId);
    });
  }

  @Override
  public int getUnreadMessages(int userId, int chatId) {
    return this.databaseDriver.executeQuery("SELECT COUNT(*) FROM `user_message_unread` WHERE `userId` = ? AND `chatId` = ?;", statement -> {
      statement.setInt(1, userId);
      statement.setInt(2, chatId);
    }, resultSet -> {
      if (!resultSet.next()) {
        return 0;
      }

      return resultSet.getInt(1);
    });
  }

}
