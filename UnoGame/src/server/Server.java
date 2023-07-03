package server;
import UNO.Game.UnoGame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private static List<PlayerHandler> players;


    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(1010);
        server.acceptPlayers();
        Thread uno = new Thread(new UnoGame(players));
        uno.start();
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            players = new ArrayList<>();
        } catch (IOException e) {
            System.exit(1);
        }
        System.out.println("Server started!");
    }

    private void acceptPlayers() {

        if(players.size() <= 3) {
            System.out.println("Waiting for players to join...");
            try {
                Socket socket = serverSocket.accept();// blocking method!
                PlayerHandler player = new PlayerHandler(socket);
                players.add(player);
                new Thread(player).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                acceptPlayers();
            }
        }
        else{
            UnoGame uno =  new UnoGame(players);
            new Thread(uno).start();
            players = new ArrayList<>();
            acceptPlayers();
        }

    }



    private void broadcast(String message, PlayerHandler p) {
        players.stream()
                .filter(player -> !player.equals(p))
                .forEach(player -> player.sendMessageToPlayer( message));
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
            try {
                initializeBuffers();
                welcomeToPlayer();
//               while (gameIsOn) {
//
//               }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        private void initializeBuffers() throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        }

        private void welcomeToPlayer() throws IOException {
            System.out.println("New player joined!");
            sendMessageToPlayer("Welcome to Uno Game");
        }

        public void sendMessageToPlayer(String message) {
            out.println(message);
        }

        public String receiveMessageFromPlayer(){
            return null;
        }




    }

}
