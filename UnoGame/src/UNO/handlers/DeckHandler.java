package UNO.handlers;

import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;
import messages.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckHandler {
    private UnoDeck deck;
    private List<UnoCard> playedCards;
    private List<Player> players;
    private CardHandler cardHandler;
    private MessagesHandler messagesHandler;
    private NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler;

    public DeckHandler(UnoDeck deck, List<UnoCard> playedCards, List<Player> players, MessagesHandler messagesHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        this.deck = deck;
        this.playedCards = playedCards;
        this.messagesHandler = messagesHandler;
        this.cardHandler = new CardHandler(deck, playedCards, this, players, messagesHandler, nextAndPreviousPlayerHandler);
    }

    /**
     * check if deck have cards to draw, if not, shuffle deck and insert a new one on board.
     */

    public void checkDeck(){
        if(playedCards.size() == 0){
            messagesHandler.messageToAll(Messages.PLAYED_CARDS_IS_EMPTY);
            cardHandler.setCanDraw(false);
            return;
        }
        if(deck.getDeck().size() <= 1){
            Collections.shuffle(playedCards);
            this.deck = new UnoDeck(playedCards);
            playedCards = new ArrayList<>();
            messagesHandler.messageToAll(Messages.NEW_DECK);
        }
    }

    public CardHandler getCardHandler() {
        return cardHandler;
    }
}
