package de.luxcars.backend.services;

import de.luxcars.backend.database.DatabaseDriver;
import de.luxcars.backend.services.account.AccountService;
import de.luxcars.backend.services.account.DefaultAccountService;
import de.luxcars.backend.services.chat.ChatRoomService;
import de.luxcars.backend.services.chat.DefaultChatRoomService;
import de.luxcars.backend.services.chat.message.DefaultMessageService;
import de.luxcars.backend.services.chat.message.MessageService;
import de.luxcars.backend.services.chat.read.ChatReadService;
import de.luxcars.backend.services.chat.read.DefaultChatReadService;
import de.luxcars.backend.services.image.DefaultImageService;
import de.luxcars.backend.services.image.ImageService;
import de.luxcars.backend.services.location.DefaultLocationService;
import de.luxcars.backend.services.location.LocationService;
import de.luxcars.backend.services.socket.DefaultWebSocketService;
import de.luxcars.backend.services.socket.WebSocketService;
import de.luxcars.backend.services.token.DefaultTokenService;
import de.luxcars.backend.services.token.TokenService;
import org.jetbrains.annotations.NotNull;

public class ServiceRegistry {

  private final AccountService accountService;
  private final TokenService tokenService;
  private final LocationService locationService;
  private final ChatRoomService chatRoomService;
  private final MessageService messageService;
  private final ChatReadService chatReadService;

  private final ImageService imageService = new DefaultImageService();
  private final WebSocketService webSocketService = new DefaultWebSocketService();

  public ServiceRegistry(DatabaseDriver databaseDriver) {
    this.accountService = new DefaultAccountService(databaseDriver);
    this.tokenService = new DefaultTokenService(databaseDriver);
    this.locationService = new DefaultLocationService(databaseDriver);
    this.chatRoomService = new DefaultChatRoomService(databaseDriver, this.accountService);
    this.messageService = new DefaultMessageService(databaseDriver, this.chatRoomService);
    this.chatReadService = new DefaultChatReadService(databaseDriver);
  }

  @NotNull
  public AccountService getAccountService() {
    return this.accountService;
  }

  @NotNull
  public TokenService getTokenService() {
    return this.tokenService;
  }

  @NotNull
  public LocationService getLocationService() {
    return this.locationService;
  }

  @NotNull
  public ImageService getImageService() {
    return imageService;
  }

  @NotNull
  public WebSocketService getWebSocketService() {
    return this.webSocketService;
  }

  @NotNull
  public ChatRoomService getChatRoomService() {
    return this.chatRoomService;
  }

  @NotNull
  public MessageService getMessageService() {
    return this.messageService;
  }

  @NotNull
  public ChatReadService getChatReadService() {
    return this.chatReadService;
  }

}
