package UNO.Game;
import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;
import server.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UnoGame implements Runnable{

    private UnoDeck deck;
    private List<Server.PlayerHandler> playerHandlers;
    private List<Player> players;
    private Random random;
    private  boolean isGameOn;

    public UnoGame(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
        deck = new UnoDeck();
        random = new Random();
        isGameOn = true;
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
                ph.setUsername(user);
                break;
            }
        }
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
        for (int i = 0; i < 8; i++) {
            int randomNum = random.nextInt(0, getDeck().size());
            cardsToPlayer.add(getDeck().remove(randomNum));
        }
        return cardsToPlayer;
    }

    private void playRound() {
        while (isGameOn){
            for (Server.PlayerHandler ph : playerHandlers){
                messageToPlayer("It's your turn, write /special or /normal than the value!", ph);
                manageCard(ph.receiveMessageFromPlayer());
            }
        }
    }

    private void firstCard(){
        int num = random.nextInt(0, getDeck().size());
        UnoCard card = getDeck().remove(num);
        messageToAll("Uno starts with " + card.getValue() + " " + card.getColor());
    }

    private void manageCard(String card){
        //remover da mao do player, usar o command pattern para evitar switch!
        if (card.contains("/special")){

            return;
        }
        //

    }

    private void takeCardsFromPlayer(UnoCard card, Player player){
        player.getHandCards().remove(card);
    }
}
