package UNO;

public enum CardColor{
    RED("\u001b[31;1m"),
    BLUE("\u001b[34;1m"),
    YELLOW("\u001b[33;1m"),
    GREEN("\u001b[32;1m"),
    WILD("\u001b[30;1m");

    private final String colorCode;

    CardColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
