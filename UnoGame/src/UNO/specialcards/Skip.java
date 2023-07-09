package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;

public class Skip implements SpecialCardHandler{
    @Override
    public void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        nextAndPreviousPlayerHandler.getNextPlayer();
    }
}
