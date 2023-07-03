package UNO.specialcards;

import UNO.CardValue;

public enum SpecialCard {
    PLUS_TWO(CardValue.PLUS_TWO, new PlusTwo()),
    PLUS_FOUR(CardValue.FOUR, new PlusFour()),
    SWITCH(CardValue.SWITCH, new Switch()),
    SKIP(CardValue.SKIP, new Skip()),
    NO_VALUE(CardValue.NO_VALUE, new NoValue());

    private final CardValue value;
    private final SpecialCardHandler specialCardHandler;

    SpecialCard(CardValue value, SpecialCardHandler specialCardHandler) {
        this.value = value;
        this.specialCardHandler = specialCardHandler;
    }

    public CardValue getValue() {
        return value;
    }


}
