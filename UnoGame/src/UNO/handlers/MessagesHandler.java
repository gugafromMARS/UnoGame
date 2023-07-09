package UNO.handlers;

import server.Server;

import java.util.List;

public class MessagesHandler {


    private List<Server.PlayerHandler> playerHandlers;

    public MessagesHandler(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
    }

    public void messageToAll(String message){
        playerHandlers.forEach(pH -> pH.sendMessageToPlayer(message));
    }

    public void broadcast(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pHandler -> !pHandler.equals(ph))
                .forEach(pHandler -> pHandler.sendMessageToPlayer(message));
    }

    public void messageToPlayer(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pH -> pH.equals(ph))
                .forEach(pH -> pH.sendMessageToPlayer(message));
    }
}
