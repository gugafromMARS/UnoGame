package UNO.specialcards;

import UNO.Game.UnoGame;


public class Switch implements SpecialCardHandler {
    @Override
    public void execute(UnoGame game) {
        game.setGameDirection(!game.isGameDirection());
    }
}