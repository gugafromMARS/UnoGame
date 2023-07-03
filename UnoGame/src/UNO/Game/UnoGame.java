package UNO.Game;
import UNO.Player.Player;
import UNO.UnoDeck;
import server.Server;

import java.util.List;


public class UnoGame  implements  Runnable{

    private UnoDeck deck;
    private List<Server.PlayerHandler> players;
    private List<Player> playersFinal;

    private  boolean gameOn = true;

    public UnoGame(List<Server.PlayerHandler> players) {
        this.players = players;
    }

    public List<Server.PlayerHandler> getPlayers() {
        return players;
    }


    public boolean isReady(){
        return players.size() > 3;
    }

    @Override
    public void run() {
        System.out.println(players);
        playersFinal = players.stream().map(ph -> new Player(ph)).toList();
//        startGame(); //ask for players name /set cards
//        welcomePlayers();
//        playRound(); //ciclo while (jogar carta ou draw card)
//        finsihGame();

        while (true){
            //play game
        }
        //create players from each playerHandler
    }
}
