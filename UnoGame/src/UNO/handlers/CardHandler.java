package UNO.handlers;

import UNO.CardColor;
import UNO.CardValue;
import UNO.Exception.CantPlayCardException;
import UNO.Exception.DontHaveCardException;
import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;
import UNO.specialcards.SpecialCard;
import messages.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardHandler {
    private UnoDeck deck;
    private DeckHandler deckHandler;
    private List<UnoCard> playedCards;
    private UnoCard previousCard;
    private List<Player> players;
    private MessagesHandler messagesHandler;
    private MenuHandler menuHandler;
    private NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler;
    private boolean canDraw = true;
    private Random random;
    private final int numOfInitialCards = 2;


    public CardHandler(UnoDeck deck, List<UnoCard> playedCards, DeckHandler deckHandler, List<Player> players, MessagesHandler messagesHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        this.deck = deck;
        this.playedCards = playedCards;
        this.random = new Random();
        this.deckHandler = deckHandler;
        this.players = players;
        this.messagesHandler = messagesHandler;
        this.nextAndPreviousPlayerHandler = nextAndPreviousPlayerHandler;
        previousCard = null;
        menuHandler = null;
    }

    public void drawCard(Player p){
        if(canDraw) {
            UnoCard c = deck.getDeck().get(random.nextInt(deck.getDeck().size()));
            deck.getDeck().remove(c);
            p.getHandCards().add(c);
            p.getPh().sendMessageToPlayer("You got a " + c.getColor().getColorCode() + c.getValue() + " / " + c.getColor() +"\u001b[0;1m");
            deckHandler.checkDeck();
        }
    }


    public void drawNCards(int n, Player p){
        for(int i=0;i<n;i++){
            deckHandler.checkDeck();
            drawCard(p);
        }
    }

    public void infoPlayerCards(Player player) {
        ArrayList<UnoCard> playerHandCards = player.getHandCards();
        StringBuilder sb = new StringBuilder();
        sb.append("\u001b[0;1m" + "Your cards are :");
        sb.append(" ");

        for(UnoCard card : playerHandCards){
            sb.append(card.getColor().getColorCode() + card.getValue());
            sb.append(" / ");
            sb.append(card.getColor());
            sb.append("\u001b[0;1m" + " || ");
        }
        String cardsInfo = sb.toString();
        messagesHandler.messageToPlayer(cardsInfo, player.getPh());
    }

    public void firstCard(){
        int num = random.nextInt(deck.getDeck().size());
        UnoCard card = deck.getDeck().get(num);
        if(card.getColor().equals(CardColor.WILD)
                || card.getValue().equals(CardValue.PLUS_FOUR)
                || card.getValue().equals(CardValue.PLUS_TWO)
                || card.getValue().equals(CardValue.SKIP)
                || card.getValue().equals(CardValue.SWITCH)) {

            firstCard();
        }
        else{
            deck.getDeck().remove(card);
            previousCard = card;
            managePlayedCards(card);
            messagesHandler.messageToAll("\u001b[0;1m" + "Uno starts with " + card.getColor().getColorCode() + card.getValue() + " " + card.getColor());
        }

    }
    private void managePlayedCards(UnoCard card) {
        playedCards.add(card);
    }

    public void dealWithCard(String playerCardSuggestion, Player player){
        if(validateCardFormat(playerCardSuggestion, player)){
            manageCard(playerCardSuggestion, player);
            return;
        }
        player.getPh().sendMessageToPlayer(Messages.CARD_NOT_VALID);
        menuHandler.playerMenu(player);
    }

    private void manageCard(String playerCardSuggestion, Player player) {
        UnoCard playerCard = getCardFromPlayer(playerCardSuggestion, player);
        try {
            checkPlayerHaveCard(playerCard);
            if(validateCard(playerCard, player)) {
                executeSpecialCard(playerCard);
            }
            canDraw = true;
        } catch (DontHaveCardException e) {
            player.getPh().sendMessageToPlayer(e.getMessage());
            dealWithCard(player.getPh().receiveMessageFromPlayer(), player);
        }
    }

    private void checkPlayerHaveCard(UnoCard card) throws DontHaveCardException {
        if(card == null){
            throw new DontHaveCardException();
        }
    }

    private boolean validateCardFormat(String playerCardSuggestion, Player p){
        boolean valueValid = false;
        boolean colorValid = false;
        boolean cardValid = false;
        for(CardValue c : CardValue.values()){
            if(playerCardSuggestion.contains(c.toString().toLowerCase())){
                valueValid = true;
            }
        }
        for(CardColor c : CardColor.values()){
            if(playerCardSuggestion.contains(c.toString().toLowerCase())){
                colorValid = true;
            }
        }
        if(valueValid && colorValid){
            cardValid = true;
        }
        return cardValid;
    }

    private UnoCard getCardFromPlayer(String playerCardSuggestion, Player player) {
        for (UnoCard c : player.getHandCards()) {
            if (playerCardSuggestion.contains(c.getValue().toString().toLowerCase()) &&
                    playerCardSuggestion.contains(c.getColor().toString().toLowerCase())) {
                return c;
            }
        }
        return null;
    }

    public void getMultipleCardsFromPlayer(String[] cards, Player player) {
        for (String c : cards) {
            UnoCard card = getCardFromPlayer(c, player);
            validateMultipleCards(card, player);
            executeSpecialCard(card);
        }

    }

    private void validateMultipleCards(UnoCard card, Player player){
        if(card.getValue() == previousCard.getValue()) {
            playerSuggestionAccepted(card, player);
        }
    }

    private void executeSpecialCard(UnoCard card){
        if(card.getValue() == CardValue.SWITCH){
            SpecialCard.SWITCH.getSpecialCardHandler().execute(this, getNextAndPreviousPlayer());
            return;
        }
        if(card.getValue() == CardValue.SKIP){
            SpecialCard.SKIP.getSpecialCardHandler().execute(this,getNextAndPreviousPlayer());
            return;
        }
        if(card.getValue() == CardValue.PLUS_TWO){
            SpecialCard.PLUS_TWO.getSpecialCardHandler().execute(this, getNextAndPreviousPlayer());
            return;
        }
        if(card.getValue() == CardValue.PLUS_FOUR){
            SpecialCard.PLUS_FOUR.getSpecialCardHandler().execute(this, getNextAndPreviousPlayer());
            SpecialCard.NO_VALUE.getSpecialCardHandler().execute(this, getNextAndPreviousPlayer());
            return;
        }
        if(card.getValue() == CardValue.NO_VALUE){
            SpecialCard.NO_VALUE.getSpecialCardHandler().execute(this, getNextAndPreviousPlayer());
        }
    }

    private boolean validateCard(UnoCard playerCard, Player player)  {
        try {
            checkPlayerHaveCard(playerCard);
            if(playerCard.getColor().toString().toLowerCase().equals(previousCard.getColor().toString().toLowerCase())
                    || playerCard.getValue().toString().toLowerCase().equals(previousCard.getValue().toString().toLowerCase())
                    || playerCard.getValue() == CardValue.NO_VALUE
                    || playerCard.getValue() == CardValue.PLUS_FOUR) {
                playerSuggestionAccepted(playerCard, player);
                return true;
            }
            throw new CantPlayCardException();
        } catch (DontHaveCardException e) {
            player.getPh().sendMessageToPlayer(e.getMessage());
            dealWithCard(player.getPh().receiveMessageFromPlayer(), player);
        } catch (CantPlayCardException e) {
            messagesHandler.messageToPlayer(e.getMessage(), player.getPh());
            dealWithInvalidCard(player.getPh().receiveMessageFromPlayer(), player);
        }
        return false;
    }
    private void dealWithInvalidCard(String playerCardSuggestion, Player player){
        if(playerCardSuggestion.contains("/draw")) {
            drawCard(player);
            menuHandler.playerMenu(player);
            return;
        }
        dealWithCard(playerCardSuggestion, player);
    }

    private void playerSuggestionAccepted(UnoCard playerCard, Player player){
        takeCardsFromPlayer(playerCard, player);
        playedCards.add(playerCard);
        if(playerCard.getValue()!=CardValue.NO_VALUE) {
            previousCard = playerCard;
            messagesHandler.messageToAll("\u001b[0;1m" + "Card in table now is : "
                    + previousCard.getColor().getColorCode() + previousCard.getValue()
                    + " " + previousCard.getColor() + "\u001b[0;1m");
        }
    }
    private void takeCardsFromPlayer(UnoCard card, Player player){
        player.getHandCards().remove(card);
    }
    public void NoValueColor(){
        Player currentPlayer = nextAndPreviousPlayerHandler.getCurrentPlayer();
        currentPlayer.getPh().sendMessageToPlayer(Messages.CHOOSE_COLOR);
        String color = currentPlayer.getPh().receiveMessageFromPlayer();
        previousCard = new UnoCard(CardColor.valueOf(color.toUpperCase()), CardValue.NO_VALUE);
        messagesHandler.messageToAll("\u001b[0;1m" + "Chosen card is " + previousCard.getValue() + " and the color is " + previousCard.getColor().getColorCode() + previousCard.getColor());
    }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public UnoCard getPreviousCard() {
        return previousCard;
    }

    public void setMenuHandler(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    public NextAndPreviousPlayerHandler getNextAndPreviousPlayer() {
        return nextAndPreviousPlayerHandler;
    }
}