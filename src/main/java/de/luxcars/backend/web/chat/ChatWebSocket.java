package de.luxcars.backend.web.chat;

import de.luxcars.backend.services.socket.WebSocketService;
import de.luxcars.backend.util.javalin.AuthenticationLevel;
import io.javalin.Javalin;

import java.util.Scanner;

public class ChatWebSocket {

  public ChatWebSocket(Javalin javalin, WebSocketService webSocketService) {
    javalin.ws("/chat", webSocket -> {
      webSocket.onConnect(context -> {
        webSocketService.registerClient(context.session);

        System.out.println("Waiting...");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        context.send("updateChat:" + id);
        System.out.println("done");
      });

      webSocket.onClose(context -> {
        System.out.println("got disconnect");
        webSocketService.terminateClient(context.session);
      });

      webSocket.onMessage(wsMessageContext -> {
        System.out.println(wsMessageContext.message());
      });
    });
  }

}
