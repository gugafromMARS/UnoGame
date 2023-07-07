package UNO;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnoDeck {

    public List<UnoCard> deck;
    
    public UnoDeck() {
        deck = new ArrayList<>();
        generateDeck();
    }

    public UnoDeck(List<UnoCard> playedCards) {

        deck = new ArrayList<>(playedCards);
        //deck = (ArrayList)playedCards;
    }


    public void generateDeck(){
        //generate normal cards
        for(CardValue value:CardValue.values()){
            for(CardColor color:CardColor.values()){
                if(!color.equals(CardColor.WILD)){
                    if(value.equals(CardValue.NO_VALUE) || value.equals(CardValue.PLUS_FOUR)){
                        continue;
                    }
                    deck.add(new UnoCard(color,value));
                }
            }
        }

        //generate wild fours
        for(int i=0;i<4;i++){
            deck.add(new UnoCard(CardColor.WILD,CardValue.PLUS_FOUR));
        }
        //generate wild nulls
        for(int i=0;i<4;i++){
            deck.add(new UnoCard(CardColor.WILD,CardValue.NO_VALUE));
        }
    }

    public UnoCard drawCard(){
        UnoCard card = deck.get(new Random().nextInt( (getDeck().size() - 0) + 1) + 0);
        deck.remove(card);
        return card;
    }

    public List<UnoCard> getDeck() {
        return deck;
    }
}

