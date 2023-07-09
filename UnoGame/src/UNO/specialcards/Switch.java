package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;


public class Switch implements SpecialCardHandler {
    @Override
    public void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        nextAndPreviousPlayerHandler.setGameDirection(!nextAndPreviousPlayerHandler.isGameDirection());
    }
}