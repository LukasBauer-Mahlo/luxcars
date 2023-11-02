package de.luxcars.backend.database;

import com.zaxxer.hikari.HikariDataSource;
import de.luxcars.backend.database.function.ThrowableConsumer;
import de.luxcars.backend.database.function.ThrowableFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseDriver {

  HikariDataSource getDataSource();

  @NotNull
  Connection getConnection() throws SQLException;

  int executeUpdate(@NotNull String sql);

  int executeUpdate(@NotNull String sql,
      @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier);

  <T> T executeUpdateWithKeys(@NotNull String sql,
      @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier,
      @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper);

  <T> T executeQuery(@NotNull String sql,
      @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper);

  <T> T executeQuery(@NotNull String sql,
      @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier,
      @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper);

  void close();

}
