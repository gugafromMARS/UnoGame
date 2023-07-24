package UNO.handlers;

import messages.Messages;
import server.Server;

import java.util.List;

public class UserHandler {

    private List<Server.PlayerHandler> playerHandlers;


    public UserHandler(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
    }

    /**
     * Create a username
     */
    public void createUser(){
        for(Server.PlayerHandler ph : playerHandlers) {
            String user = ph.insertUsername();
            while(!UsernameIsValid(user, ph)){
                ph.sendMessageToPlayer(Messages.User_ALREADY_EXISTS);
                user = ph.insertUsername();
            }
            ph.setUsername(user);
        }
    }

    /**
     * Check if username chosen is valid
     * @param name username choice
     * @param ph player handler
     * @return return true if user chosen is valid
     */
    private boolean UsernameIsValid(String name, Server.PlayerHandler ph){
        for(Server.PlayerHandler pHandler : playerHandlers) {
            if (pHandler.getUsername() != null) {
                if ((pHandler.getUsername().equals(name))) {
                    return false;
                }
            }
        }
        return true;
    }

}
