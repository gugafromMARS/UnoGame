package UNO;
public class UnoCard {

    private CardColor color;
    private CardValue value;
    

    public UnoCard(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
    }

    public CardColor getColor() {
        return color;
    }

    public CardValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "color=" + color +
                ", value=" + value;
    }
}
