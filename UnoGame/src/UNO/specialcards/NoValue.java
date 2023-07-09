package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;

public class NoValue implements SpecialCardHandler{
    @Override
    public void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        cardHandler.createNewCard();
    }
}
