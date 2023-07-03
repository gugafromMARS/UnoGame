package UNO.Game;

import UNO.Player.Player;
import UNO.UnoCard;
import UNO.UnoDeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnoGame {
    private ServerSocket serverSocket;
    private UnoDeck deck;
    private List<PlayerHandler> players;
    private Random random;

    public static void main(String[] args) {
        UnoGame uno = new UnoGame();
        uno.startGame(1010);
        uno.acceptPlayers();
    }

    private void startGame(int port) {
        try {
            serverSocket = new ServerSocket(port);
            this.deck = new UnoDeck();
            deck.generateDeck();
//            System.out.println(deck.getDeck());
            players = new ArrayList<>();
        } catch (IOException e) {
            System.exit(1);
        }
        System.out.println("Uno started!");
    }


    private void acceptPlayers() {
        if(players.size() <= 3) {
            System.out.println("Waiting for players to join...");
            try {
                Socket socket = serverSocket.accept();
                PlayerHandler player = new PlayerHandler(socket);
                players.add(player);
                new Thread(player).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                acceptPlayers();
            }
        }
        System.out.println("We don't have more slots!");
    }

    private List<UnoCard> giveCardsToPlayer() {
        int num;
        List<UnoCard> cards = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            num = random.nextInt(0, deck.getDeck().size());
            cards.add(deck.getDeck().get(num));
            deck.getDeck().remove(num);
        }
        return cards;
    }

    private UnoCard drawCard() {
        int randomNum = random.nextInt(0, deck.getDeck().size());
        return deck.getDeck().remove(randomNum);
    }

    // Broacast do uno, sera necessario broadcast para mais alguma situacao ?
    private void broadcast(PlayerHandler p) {
        players.stream()
                .filter(player -> !player.equals(p))
                .forEach(player -> player.sendMessageToPlayer(p.getNickname() + " says UNO!!!"));
    }


   public class PlayerHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

       public PlayerHandler(Socket socket) {
           this.socket = socket;
       }

       @Override
       public void run() {
           try {
               initializeBuffers();
               welcomeToPlayer();

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

       }

       private void initializeBuffers() throws IOException {
           in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream(), true);
       }

       private void welcomeToPlayer() {
           System.out.println("New player joined!");
           sendMessageToPlayer("Welcome to Uno Game");
           try {
               nickname = insertUsername();
           } catch (IOException e) {
               System.out.println("Invalid nickname!");
           }
       }

       private void sendMessageToPlayer(String message) {
           out.println(message);
       }
       private String insertUsername() throws IOException {
           sendMessageToPlayer("Insert your nickname: ");
           String nick = in.readLine();

           for(PlayerHandler player : players) {
               if(player.getNickname().equals(nick)){
                   sendMessageToPlayer("Nickname Already Exists!");
                   insertUsername();
               }
           }
           return nick;
       }

       public String getNickname() {
           return nickname;
       }
   }

}
