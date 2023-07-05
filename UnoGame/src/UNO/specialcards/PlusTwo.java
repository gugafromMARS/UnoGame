package UNO.specialcards;

import UNO.Game.UnoGame;


public class PlusTwo implements SpecialCardHandler{
    @Override
    public void execute(UnoGame game) {
            game.getNextPlayer();
            game.drawNcards(2, game.getCurrentPlayer());
            game.previousPlayer();
    }
}
