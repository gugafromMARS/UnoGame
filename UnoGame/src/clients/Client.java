package clients;

import server.Server;

import java.io.*;
import java.net.Socket;

public class Client extends Server {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.handleServer();
    }

    private void handleServer() {
        connectServer();
        startListeningServer();
        communicateWithServer();
    }

    private void connectServer() {
        String hostName = "localhost";
        int port = 1010;
        try {
            socket = new Socket(hostName, port);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Maybe server dead!");
            connectServer();
        }
    }

    private void startListeningServer() {
        try {
            new Thread(new ServerListener(socket.getInputStream())).start();
        } catch (IOException e) {
            handleServer();
        }
    }

    private void communicateWithServer() {
        try {
            sendMessage();
            communicateWithServer();
        } catch (IOException e) {
            System.out.println("maybe server is dead");
            handleServer();
        }
    }

    private void sendMessage() throws IOException {
        String message = in.readLine();
        out.println(message);
    }

    private class ServerListener implements Runnable {

        private BufferedReader in;

        public ServerListener(InputStream inputStream) {
            this.in = new BufferedReader(new InputStreamReader(inputStream));
        }

        @Override
        public void run() {
            try {
                readMessage();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private void readMessage() throws IOException {
            String message = in.readLine();
            if(message == null) {
                System.out.println("Disconnected");
                System.exit(1);
            }
            System.out.println(message);
            readMessage();
        }
    }
}
