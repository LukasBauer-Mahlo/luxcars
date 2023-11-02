package de.luxcars.backend.services.chat.read;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.web.chat.ChatWebSocket;

public class DefaultChatReadService implements ChatReadService {

  private final DatabaseDriver databaseDriver;
  private ChatWebSocket chatWebSocket;

  public DefaultChatReadService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate("CREATE TABLE IF NOT EXISTS `user_chat_unread` (`chatId` INT NOT NULL, `userId` INT NOT NULL, PRIMARY KEY (`chatId`, `userId`));");
  }

  @Override
  public void readChat(int chatId, int userId) {
    this.databaseDriver.executeUpdate("DELETE FROM `user_chat_unread` WHERE `chatId` = ? AND `userId` = ?;", statement -> {
      statement.setInt(1, chatId);
      statement.setInt(2, userId);
    });

    LuxCarsBackend.getInstance().getChatWebSocket().publishUnreadChatsUpdate(userId);
  }

  @Override
  public void handleNewMessage(int chatId, int userId) {
    this.databaseDriver.executeUpdate("INSERT IGNORE INTO `user_chat_unread` (`chatId`, `userId`) VALUES (?, ?);", statement -> {
      statement.setInt(1, chatId);
      statement.setInt(2, userId);
    });

    LuxCarsBackend.getInstance().getChatWebSocket().publishUnreadChatsUpdate(userId);
  }

  @Override
  public int getUnreadChats(int userId) {
    return this.databaseDriver.executeQuery("SELECT COUNT(*) FROM `user_chat_unread` WHERE `userId` = ?;", statement -> {
      statement.setInt(1, userId);
    }, resultSet -> {
      if (!resultSet.next()) {
        return 0;
      }

      return resultSet.getInt(1);
    });
  }

}
