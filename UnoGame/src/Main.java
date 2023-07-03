import UNO.UnoCard;
import UNO.UnoDeck;

public class Main {
    public static void main(String[] args) {

        UnoDeck deck = new UnoDeck();
        for (UnoCard card : deck.deck) {
            System.out.println(card);
        }
        deck.drawCard();
//        player.drawnCard(deck.drawCard());

    }
}