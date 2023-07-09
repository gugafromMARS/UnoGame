package UNO.specialcards;

import UNO.handlers.CardHandler;
import UNO.handlers.NextAndPreviousPlayerHandler;

public interface SpecialCardHandler{

    void execute(CardHandler cardHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler);
}
