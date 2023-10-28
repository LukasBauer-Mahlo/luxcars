package de.luxcars.backend.services.message;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.message.object.Chat;
import de.luxcars.backend.services.message.object.Message;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultMessageService implements MessageService {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

  private final DatabaseDriver databaseDriver;

  public DefaultMessageService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate("CREATE TABLE IF NOT EXISTS messages (\n"
        + "    firstUser INT NOT NULL,\n"
        + "    secondUser INT NOT NULL,\n"
        + "    senderId INT NOT NULL,\n"
        + "    receiverId INT NOT NULL,\n"
        + "    time BIGINT NOT NULL,\n"
        + "    text TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,\n"
        + "    PRIMARY KEY (firstUser, secondUser, time),\n"
        + "    FOREIGN KEY (firstUser) REFERENCES accounts(id) ON DELETE CASCADE,\n"
        + "    FOREIGN KEY (secondUser) REFERENCES accounts(id) ON DELETE CASCADE,\n"
        + "    FOREIGN KEY (senderId) REFERENCES accounts(id) ON DELETE CASCADE,\n"
        + "    FOREIGN KEY (receiverId) REFERENCES accounts(id) ON DELETE CASCADE\n"
        + ");");
  }

  @Override
  public @NotNull Message createMessage(int senderUserId, int receiverId, @NotNull String text) {
    long time = System.currentTimeMillis();
    this.databaseDriver.executeUpdate("INSERT INTO `messages` (`firstUser`, `secondUser`, `senderId`, `receiverId`, `time`, `text`) VALUES (?, ?, ?, ?, ?, ?);", statement -> {
      statement.setInt(1, senderUserId);
      statement.setInt(2, receiverId);
      statement.setInt(3, senderUserId);
      statement.setInt(4, receiverId);
      statement.setLong(5, time);
      statement.setString(6, text);
    });

    String formattedTime = SIMPLE_DATE_FORMAT.format(time);
    String[] splitedTime = formattedTime.split(" ");
    return new Message(senderUserId, receiverId, splitedTime[1], splitedTime[0], text);
  }

  @Override
  public @NotNull List<Message> getMessages(int firstUser, int secondUser) {
    return this.databaseDriver.executeQuery("SELECT * FROM `messages` WHERE (`firstUser` = ? AND `secondUser` = ?) OR (`secondUser` = ? AND `firstUser` = ?) ORDER BY `time` ASC;", statement -> {
      statement.setInt(1, firstUser);
      statement.setInt(2, secondUser);
      statement.setInt(3, firstUser);
      statement.setInt(4, secondUser);
    }, resultSet -> {
      List<Message> messages = new ArrayList<>();
      while (resultSet.next()) {
        messages.add(this.fromResultSet(resultSet));
      }

      return messages;
    });
  }

  @Override
  public @NotNull List<Chat> getChats(int userId) {
    return this.databaseDriver.executeQuery("SELECT CASE WHEN firstUser = ? THEN secondUser ELSE firstUser END AS chatPartnerId, MAX(time) AS lastMessageTime, MAX(CASE WHEN firstUser = ? THEN secondUser ELSE firstUser END) AS otherUserId, MAX(text) AS lastMessage FROM messages WHERE firstUser = ? OR secondUser = ? ORDER BY lastMessageTime ASC;", statement -> {
      statement.setInt(1, userId);
      statement.setInt(2, userId);
      statement.setInt(3, userId);
      statement.setInt(4, userId);
    }, resultSet -> {
      List<Chat> chats = new ArrayList<>();
      while (resultSet.next()) {
        String lastMessage = resultSet.getString("lastMessage");
        LuxCarsBackend.getInstance().getServices().getAccountService().getAccount(resultSet.getInt("chatPartnerId")).ifPresent(account -> {
          chats.add(new Chat(
              account.getId(),
              account.toString(),
              lastMessage
          ));
        });
      }

      return chats;
    });
  }

  @NotNull
  private Message fromResultSet(@NotNull ResultSet resultSet) throws SQLException {
    String[] splitedTime = SIMPLE_DATE_FORMAT.format(resultSet.getLong("time")).split(" ");

    return new Message(
        resultSet.getInt("senderId"),
        resultSet.getInt("receiverId"),
        splitedTime[1],
        splitedTime[0],
        resultSet.getString("text")
    );
  }

}
