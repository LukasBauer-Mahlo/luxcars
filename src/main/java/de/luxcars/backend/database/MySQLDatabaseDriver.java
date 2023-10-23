package de.luxcars.backend.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.luxcars.backend.util.Constants;
import de.luxcars.backend.database.config.DatabaseConfig;
import de.luxcars.backend.database.function.ThrowableConsumer;
import de.luxcars.backend.database.function.ThrowableFunction;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MySQLDatabaseDriver implements DatabaseDriver {

  private static final Path CONFIG_FILE = Paths.get("databaseConfig.json");

  private HikariDataSource dataSource;


  public MySQLDatabaseDriver() {
    DatabaseConfig databaseConfig = new DatabaseConfig();

    try {
      if (Files.notExists(CONFIG_FILE)) {
        Files.write(CONFIG_FILE, Constants.GSON.toJson(databaseConfig).getBytes(StandardCharsets.UTF_8));
      } else {
        databaseConfig = Constants.GSON.fromJson(Files.readString(CONFIG_FILE, StandardCharsets.UTF_8), DatabaseConfig.class);
      }

    } catch (IOException exception) {
      exception.printStackTrace();
    }

    if (!databaseConfig.isEnabled()) {
      return;
    }

    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setJdbcUrl(databaseConfig.getJdbcUrl());
    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
    hikariConfig.setUsername(databaseConfig.getUser());
    hikariConfig.setPassword(databaseConfig.getPassword());

    hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
    hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
    hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
    hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
    hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
    hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
    hikariConfig.addDataSourceProperty("maintainTimeStats", "false");

    this.dataSource = new HikariDataSource(hikariConfig);
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  @Override
  public HikariDataSource getDataSource() {
    return this.dataSource;
  }

  @Override
  public @NotNull Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }

  @Override
  public int executeUpdate(@NotNull String sql) {
    return this.executeUpdate(sql, null);
  }

  @Override
  public int executeUpdate(
      @NotNull String sql, @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier) {

    try (Connection connection = this.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      if (modifier != null) {
        modifier.accept(statement);
      }

      return statement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return -1;
  }

  @Override
  public <T> T executeUpdateWithKeys(
      @NotNull String sql,
      @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier,
      @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper) {
    long begin = System.currentTimeMillis();

    try (Connection connection = this.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

      if (modifier != null) {
        modifier.accept(statement);
      }

      statement.executeUpdate();

      try (ResultSet resultSet = statement.getGeneratedKeys()) {
        return resultMapper.apply(resultSet);
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return null;
  }

  @Override
  public <T> T executeQuery(
      @NotNull String sql, @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper) {
    return this.executeQuery(sql, null, resultMapper);
  }

  @Override
  public <T> T executeQuery(
      @NotNull String sql,
      @Nullable ThrowableConsumer<PreparedStatement, SQLException> modifier,
      @NotNull ThrowableFunction<ResultSet, T, SQLException> resultMapper) {

    try (Connection connection = this.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      if (modifier != null) {
        modifier.accept(statement);
      }

      try (ResultSet resultSet = statement.executeQuery()) {
        return resultMapper.apply(resultSet);
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return null;
  }

  @Override
  public void close() {
    this.dataSource.close();
  }
}