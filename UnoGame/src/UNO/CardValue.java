package UNO;

public enum CardValue{
    ONE("one"),
    TWO("two"),
    THREE("three"),
    FOUR("four"),
    FIVE("five"),
    SIX("six"),
    SEVEN("seven"),
    EIGHT("eight"),
    NINE("nine"),
    PLUS_TWO("plus two"),
    PLUS_FOUR("plus four"),
    SWITCH("switch"),
    SKIP("skip"),
    NO_VALUE("no value");
    //public static final CardValue[] values = CardValue.values();
    private final String description;

    CardValue(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
