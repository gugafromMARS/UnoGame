package UNO.Game;

import UNO.Player.Player;
import UNO.UnoDeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UnoGame {
    private ServerSocket serverSocket;
    private UnoDeck deck;
    private List<PlayerHandler> players;

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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                acceptPlayers();
            }
        }
        System.out.println("We don't have more slots!");
    }




   public class PlayerHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

       public PlayerHandler(Socket socket) {
           this.socket = socket;
       }

       @Override
       public void run() {

       }
   }

}
