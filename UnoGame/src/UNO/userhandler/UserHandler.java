package UNO.userhandler;

import messages.Messages;
import server.Server;

import java.util.List;

public class UserHandler {

    private List<Server.PlayerHandler> playerHandlers;


    public UserHandler(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
    }

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
