package UNO.Game;
import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;
import UNO.handlers.*;
import messages.Messages;
import server.Server;

import java.io.IOException;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class UnoGame implements Runnable{

    private UnoDeck deck;
    private List<Server.PlayerHandler> playerHandlers;
    private List<Player> players;
    private List<UnoCard> playedCards;
    private UserHandler userHandler;
    private DeckHandler deckHandler;
    private MessagesHandler messagesHandler;
    private NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler;
    private MenuHandler menuHandler;
    private Random random;
    private boolean isGameOn;
    private final int numOfInitialCards = 2;

    public UnoGame(List<Server.PlayerHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
        deck = new UnoDeck();
        random = new Random();
        isGameOn = true;
        playedCards = new ArrayList<>();
    }

    private List<UnoCard> getDeck() {
        return deck.getDeck();
    }

    @Override
    public void run() {
        players = playerHandlers.stream().map(ph -> new Player(ph)).collect(Collectors.toList());
        startHandlers();

        startGame();
        deckHandler.getCardHandler().firstCard();
        nextAndPreviousPlayerHandler.setCurrentPlayer(players.get(nextAndPreviousPlayerHandler.getCurrentPlayerId()));
        while (isGameOn) {
            playRound();
            deckHandler.checkDeck();
        }
        finishGame();
    }

    private void startHandlers(){
        userHandler = new UserHandler(playerHandlers);
        messagesHandler = new MessagesHandler(playerHandlers);
        nextAndPreviousPlayerHandler = new NextAndPreviousPlayerHandler(players);
        deckHandler = new DeckHandler(deck, playedCards, players, messagesHandler, nextAndPreviousPlayerHandler);
        menuHandler = new MenuHandler(deckHandler, nextAndPreviousPlayerHandler);
        deckHandler.getCardHandler().setMenuHandler(menuHandler);
    }

    private void finishGame(){
        for(Player p : players){
            try {
                p.getPh().clientDisconnect();
                messagesHandler.broadcast(p.getPh().getUsername() + " disconnected.", p.getPh());
            } catch (IOException e) {
                System.out.println(Messages.FINISH_GAME_WRONG);;
            }
        }
    }
    private void startGame() {
        deck.generateDeck();
        greetingPlayers();
        createUsername();
        giveCardsToPlayer();
    }

    private void checkPlayerUno(Player p){
        if(p.getHandCards().size()==1){
            messagesHandler.messageToAll(p.getPh().getUsername() + " says UNO !!");
        }
    }
    private void checkPlayerWin(Player p){
        if(p.getHandCards().size()==0){
            isGameOn = false;
            messagesHandler.messageToAll(p.getPh().getUsername() + " WIN THE GAME !!");
        }
    }

    private void greetingPlayers(){
        messagesHandler.messageToAll(Messages.WELCOME);
    }

    private void createUsername(){
        userHandler.createUser();
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
        for (int i = 0; i < numOfInitialCards; i++) {
            int randomNum = random.nextInt(getDeck().size());
            cardsToPlayer.add(getDeck().remove(randomNum));
        }
        return cardsToPlayer;
    }

    private void playRound() {
        while (isGameOn){
                Server.PlayerHandler ph = nextAndPreviousPlayerHandler.getCurrentPlayer().getPh();
                roundMessages(ph);
                deckHandler.getCardHandler().infoPlayerCards(nextAndPreviousPlayerHandler.getCurrentPlayer());

                while (menuHandler.isPlayerIsPlaying()){
                    ph.sendMessageToPlayer(Messages.MENU_OPTIONS);
                    menuHandler.playerMenu(nextAndPreviousPlayerHandler.getCurrentPlayer());
                }
                checkPlayerUno(nextAndPreviousPlayerHandler.getCurrentPlayer());
                menuHandler.setPlayerIsPlaying(true);
                checkPlayerWin(nextAndPreviousPlayerHandler.getCurrentPlayer());
                nextAndPreviousPlayerHandler.getNextPlayer();
        }
    }

    private void roundMessages(Server.PlayerHandler ph){
        messagesHandler.messageToPlayer(ph.getUsername() + Messages.YOUR_TURN, ph);
        messagesHandler.broadcast(ph.getUsername() + Messages.WAIT_TURN, ph);
    }

}
