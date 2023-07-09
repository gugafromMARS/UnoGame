package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;

public class PlusFour implements SpecialCardHandler{
    @Override
    public void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        nextAndPreviousPlayerHandler.getNextPlayer();
        cardHandler.drawNCards(4, nextAndPreviousPlayerHandler.getCurrentPlayer());
        nextAndPreviousPlayerHandler.previousPlayer();
    }
}
