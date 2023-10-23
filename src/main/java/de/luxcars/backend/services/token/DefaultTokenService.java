package de.luxcars.backend.services.token;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.util.StringUtils;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class DefaultTokenService implements TokenService {

  private final DatabaseDriver databaseDriver;

  public DefaultTokenService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;
    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `auth_tokens` (`userId` INT NOT NULL, `token` CHAR(128) NOT NULL PRIMARY KEY, FOREIGN KEY (`userId`) REFERENCES `accounts`(`id`) ON DELETE CASCADE);");
  }

  @Override
  public @NotNull String generateToken(int userId) {
    String token = StringUtils.randomString(128);
    this.databaseDriver.executeUpdate("INSERT INTO `auth_tokens` (`userId`, `token`) VALUES (?, ?);", statement -> {
      statement.setInt(1, userId);
      statement.setString(2, token);
    });

    return token;
  }

  @Override
  public @NotNull Optional<Integer> getUserIdByToken(@NotNull String token) {
    return this.databaseDriver.executeQuery("SELECT * FROM `auth_tokens` WHERE `token` = ?;", statement -> {
      statement.setString(1, token);
    }, resultSet -> {
      if (!resultSet.next()) {
        return Optional.empty();
      }

      return Optional.of(resultSet.getInt("userId"));
    });
  }

  @Override
  public void invalidateToken(@NotNull String token) {
    this.databaseDriver.executeUpdate("DELETE FROM `auth_tokens` WHERE `token` = ?;", statement -> statement.setString(1, token));
  }

  @Override
  public void invalidateAllTokens(int userId) {
    this.databaseDriver.executeUpdate("DELETE FROM `auth_tokens` WHERE `userId` = ?;", statement -> statement.setInt(1, userId));
  }

}