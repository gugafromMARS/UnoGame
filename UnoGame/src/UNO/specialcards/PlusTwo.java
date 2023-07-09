package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;


public class PlusTwo implements SpecialCardHandler{
    @Override
    public void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
            nextAndPreviousPlayerHandler.getNextPlayer();
            cardHandler.drawNCards(2, nextAndPreviousPlayerHandler.getCurrentPlayer());
            nextAndPreviousPlayerHandler.previousPlayer();
    }
}
