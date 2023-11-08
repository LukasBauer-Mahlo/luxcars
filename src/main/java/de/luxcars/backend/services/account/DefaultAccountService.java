package de.luxcars.backend.services.account;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.account.object.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

public class DefaultAccountService implements AccountService {

  private final DatabaseDriver databaseDriver;

  public DefaultAccountService(DatabaseDriver databaseDriver) {
    this.databaseDriver = databaseDriver;

    this.databaseDriver.executeUpdate(
        "CREATE TABLE IF NOT EXISTS `accounts` ("
            + "    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
            + "    mail VARCHAR(64) UNIQUE KEY NOT NULL,"
            + "    firstName VARCHAR(32) NOT NULL,"
            + "    lastName VARCHAR(32) NOT NULL,"
            + "    password VARCHAR(256) NOT NULL,"
            + "    lastOnline BIGINT NOT NULL,"
            + "    administrator BOOLEAN NOT NULL,"
            + "    disabled BOOLEAN DEFAULT false NOT NULL"
            + ");");
  }

  @Override
  public @NotNull Optional<Account> getAccount(@NotNull String mail) {
    return Optional.ofNullable(
        this.databaseDriver.executeQuery("SELECT * FROM `accounts` WHERE `mail` = ?;", statement -> {
          statement.setString(1, mail);
        }, resultSet -> {
          if (!resultSet.next()) {
            return null;
          }

          return this.fromResultSet(resultSet);
        })
    );
  }

  @Override
  public @NotNull Optional<Account> getAccount(int id) {
    return Optional.ofNullable(
        this.databaseDriver.executeQuery("SELECT * FROM `accounts` WHERE `id` = ?;", statement -> {
          statement.setInt(1, id);
        }, resultSet -> {
          if (!resultSet.next()) {
            return null;
          }

          return this.fromResultSet(resultSet);
        })
    );
  }

  @Override
  public @NotNull Account createAccount(@NotNull String mail, @NotNull String firstName, @NotNull String lastName, @NotNull String password, boolean administrator) {
    long lastOnline = System.currentTimeMillis();
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    int generatedId = this.databaseDriver.executeUpdateWithKeys("INSERT INTO `accounts` (`mail`, `firstName`, `lastName`, `password`, `administrator`, `lastOnline`) VALUES (?, ?, ?, ?, ?, ?);", statement -> {
      statement.setString(1, mail);
      statement.setString(2, firstName);
      statement.setString(3, lastName);
      statement.setString(4, hashedPassword);
      statement.setBoolean(5, administrator);
      statement.setLong(6, lastOnline);
    }, resultSet -> {
      if (!resultSet.next()) {
        throw new RuntimeException("Unable to generate accountId for account with name " + mail);
      }

      return resultSet.getInt(1);
    });

    return new Account(generatedId, mail, firstName, lastName, hashedPassword, lastOnline, false, administrator);
  }

  @Override
  public void updateAccount(@NotNull Account account) {
    this.databaseDriver.executeUpdate("UPDATE `accounts` SET `firstName` = ?, `lastName` = ?, `password` = ?, `lastOnline` = ?, `disabled` = ?, `administrator` = ? WHERE `id` = ?;", statement -> {
      statement.setString(1, account.getFirstName());
      statement.setString(2, account.getLastName());
      statement.setString(3, account.getPassword());
      statement.setLong(4, account.getLastOnline());
      statement.setBoolean(5, account.isDisabled());
      statement.setBoolean(6, account.isAdministrator());
      statement.setInt(7, account.getId());
    });
  }

  @Override
  public @NotNull List<Account> getAccounts() {
    return this.databaseDriver.executeQuery("SELECT * FROM `accounts`;", resultSet -> {
      List<Account> accounts = new ArrayList<>();
      while (resultSet.next()) {
        accounts.add(this.fromResultSet(resultSet));
      }

      return accounts;
    });
  }

  @NotNull
  private Account fromResultSet(@NotNull ResultSet resultSet) throws SQLException {
    return new Account(
        resultSet.getInt("id"),
        resultSet.getString("mail"),
        resultSet.getString("firstName"),
        resultSet.getString("lastName"),
        resultSet.getString("password"),
        resultSet.getLong("lastOnline"),
        resultSet.getBoolean("disabled"),
        resultSet.getBoolean("administrator")
    );
  }

}