package UNO.Game;
import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;
import messages.Messages;
import server.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UnoGame implements Runnable{

    private UnoDeck deck;
    private UnoCard previousCard;
    private List<Server.PlayerHandler> playerHandlers;
    private List<Player> players;
    private List<UnoCard> playedCards;
    private Random random;
    private  boolean isGameOn;

    public UnoGame(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
        deck = new UnoDeck();
        random = new Random();
        isGameOn = true;
        playedCards = new ArrayList<>();
        previousCard = null;
    }

    public List<Server.PlayerHandler> getPlayerHandlers() {
        return playerHandlers;
    }

    public boolean isReady(){
        return players.size() > 3;
    }

    private List<UnoCard> getDeck() {
        return deck.getDeck();
    }

    private void messageToAll(String message){
        playerHandlers.forEach(pH -> pH.sendMessageToPlayer(message));
    }

    private void broadcast(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pHandler -> !pHandler.equals(ph))
                .forEach(pHandler -> pHandler.sendMessageToPlayer(message));
    }

    private void messageToPlayer(String message, Server.PlayerHandler ph){
        playerHandlers.stream()
                .filter(pH -> pH.equals(ph))
                .forEach(pH -> pH.sendMessageToPlayer(message));
    }

    @Override
    public void run() {
        players = playerHandlers.stream().map(ph -> new Player(ph)).toList();

        startGame();
        firstCard();
        while (isGameOn) {
            playRound();
        }
//        finishGame();
    }

    private void startGame() {
        deck.generateDeck();
        greetingPlayers();
        createUsername();
        giveCardsToPlayer();
    }

    private void greetingPlayers(){
        messageToAll("Welcome to Uno!");
    }
    private void createUsername(){
        for(Server.PlayerHandler ph : playerHandlers) {
            String user = ph.insertUsername();
            for(Server.PlayerHandler pHandler : playerHandlers){
                if(pHandler.getUsername() != null) {
                    if((pHandler.getUsername().equals(user))){
                        messageToPlayer("Username already exists!", ph);
                        user = ph.insertUsername();
                        ph.setUsername(user);
                        break;
                    }
                }
            }
            ph.setUsername(user);
        }
    }

    private void validateUsername(Server.PlayerHandler ph){

    }

    private void giveCardsToPlayer() {
        ArrayList<UnoCard> iCards;
        for (Player p : players){
            iCards = initialCards();
            p.setHandCards(iCards);
        }
    }

    private ArrayList<UnoCard> initialCards() {
        ArrayList<UnoCard> cardsToPlayer = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int randomNum = random.nextInt(0, getDeck().size());
            cardsToPlayer.add(getDeck().remove(randomNum));
        }
        return cardsToPlayer;
    }

    private void playRound() {
        while (isGameOn){
            for (Player player : players){
                Server.PlayerHandler ph = player.getPh();
                roundMessages(ph);
                infoPlayerCards(player);
                dealWithCard(ph.receiveMessageFromPlayer(), player);
            }
        }
    }

    private void roundMessages(Server.PlayerHandler ph){
        messageToPlayer(ph.getUsername() + Messages.YOUR_TURN, ph);
        broadcast(ph.getUsername() + Messages.WAIT_TURN, ph);
    }

    private void infoPlayerCards(Player player) {
        ArrayList<UnoCard> playerHandCards = player.getHandCards();
        StringBuilder sb = new StringBuilder();
        sb.append("Your cards are :");
        sb.append(" ");

        for(UnoCard card : playerHandCards){
            sb.append(card.getValue());
            sb.append(" / ");
            sb.append(card.getColor());
            sb.append(" || ");
        }
        String cardsInfo = sb.toString();
        messageToPlayer(cardsInfo, player.getPh());
    }

    private void firstCard(){
        // nao deve a primeira carta random ser uma especial para nao prejudicar o primeiro player comparativamente aos restantes
        int num = random.nextInt(0, getDeck().size());
        UnoCard card = getDeck().remove(num);
        previousCard = card;
        managePlayedCards(card);
        messageToAll("Uno starts with " + card.getValue() + " " + card.getColor());
    }
    private void dealWithCard(String playerCardSuggestion, Player player){
        if(playerCardSuggestion.contains("/special")){
            manageSpecial(playerCardSuggestion, player);
            return;
        }
        manageNumeric(playerCardSuggestion, player);
    }

    private void manageSpecial(String playerCardSuggestion, Player player) {

    }

    private void manageNumeric(String playerCardSuggestion, Player player) {
        UnoCard playerCard = null;
        for (UnoCard c : player.getHandCards()){
            if(playerCardSuggestion.contains(c.getValue().getDescription())) {
                playerCard = c;
                break;
            }
        }
        validateCard(playerCard, player);
    }

    private void validateCard(UnoCard playerCard, Player player) {
        if(playerCard.getColor().equals(previousCard.getColor())){
            playerSuggestionAccepted(playerCard, player);
            System.out.println(player.getHandCards());
            return;
        }
        if(playerCard.getValue().equals(previousCard.getValue())){
            playerSuggestionAccepted(playerCard, player);
            System.out.println(player.getHandCards());
            return;
        }
        messageToPlayer("CAN'T PLAY THAT CARD, TRY ANOTHER ONE!!!", player.getPh());
        dealWithCard(player.getPh().receiveMessageFromPlayer(), player);
    }

    private void playerSuggestionAccepted(UnoCard playerCard, Player player){
        takeCardsFromPlayer(playerCard, player);
        playedCards.add(playerCard);
        previousCard = playerCard;
        messageToAll("Card in table now is : " + previousCard.getValue() + " " + previousCard.getColor());
    }


    private void takeCardsFromPlayer(UnoCard card, Player player){
        player.getHandCards().remove(card);
    }
    private void managePlayedCards(UnoCard card) {
        playedCards.add(card);
    }
}
