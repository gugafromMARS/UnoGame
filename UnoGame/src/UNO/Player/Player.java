package UNO.Player;

import java.util.ArrayList;
import UNO.UnoCard;
import server.Server;

public class Player {

    private ArrayList<UnoCard> handCards;
    private Server.PlayerHandler ph;
    public Player(Server.PlayerHandler ph) {
        this.ph = ph;
    }
    public void setHandCards(ArrayList<UnoCard> handCards) {
        this.handCards = handCards;
    }
    public Server.PlayerHandler getPh() {
        return ph;
    }
    public ArrayList<UnoCard> getHandCards() {
        return handCards;
    }


}
