package UNO.handlers;

import server.Server;

import java.util.List;

public class MessagesHandler {


    private List<Server.PlayerHandler> playerHandlers;

    public MessagesHandler(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
    }

    /**
     * All players receive the message
     * @param message message to send
     */
    public void messageToAll(String message){
        playerHandlers.forEach(pH -> pH.sendMessageToPlayer(message));
    }

    /**
     * All players receive the message but the current player don't.
     * @param message message to send
     * @param ph current player handler
     */

    public void broadcast(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pHandler -> !pHandler.equals(ph))
                .forEach(pHandler -> pHandler.sendMessageToPlayer(message));
    }

    /**
     * Send a message to a specific player
     * @param message message to send
     * @param ph current player handler
     */
    public void messageToPlayer(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pH -> pH.equals(ph))
                .forEach(pH -> pH.sendMessageToPlayer(message));
    }
}
