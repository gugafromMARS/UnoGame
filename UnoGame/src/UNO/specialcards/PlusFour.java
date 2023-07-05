package UNO.specialcards;

import UNO.Game.UnoGame;
import server.Server;

public class PlusFour implements SpecialCardHandler{
    @Override
    public void execute(UnoGame game) {
        game.getNextPlayer();
        game.drawNcards(4, game.getCurrentPlayer());
        game.previousPlayer();
    }
}
