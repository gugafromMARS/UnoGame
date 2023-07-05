package UNO.specialcards;

import UNO.Game.UnoGame;
import server.Server;

public class Skip implements SpecialCardHandler{
    @Override
    public void execute(UnoGame game) {
        game.getNextPlayer();
    }
}
