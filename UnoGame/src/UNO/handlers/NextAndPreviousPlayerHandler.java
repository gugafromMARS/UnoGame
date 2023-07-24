package UNO.handlers;

import UNO.Player.Player;

import java.util.List;

public class NextAndPreviousPlayerHandler {

    private int currentPlayerId = 0;
    private Player currentPlayer;
    private boolean gameDirection = true;
    private List<Player> players;

    public NextAndPreviousPlayerHandler(List<Player> players) {
        this.players = players;
    }

    /**
     * set the next player
     */
    private void nextPlayer(){
        if(gameDirection){
            currentPlayerId++;
            currentPlayerId = (currentPlayerId == players.size()) ? 0 : currentPlayerId;
        }
        else {
            currentPlayerId--;
            currentPlayerId = (currentPlayerId ==-1) ? players.size()-1 : currentPlayerId;
        }
        currentPlayer = players.get(currentPlayerId);
    }

    /**
     * set the previous player
     */
    public void previousPlayer(){
        if(gameDirection){
            currentPlayerId--;
            currentPlayerId = (currentPlayerId ==-1) ? players.size()-1 : currentPlayerId;
        }
        else {
            currentPlayerId++;
            currentPlayerId = (currentPlayerId == players.size()) ? 0 : currentPlayerId;
        }
        currentPlayer = players.get(currentPlayerId);
    }

    public void setGameDirection(boolean gameDirection) {
        this.gameDirection = gameDirection;
    }

    public boolean isGameDirection() {
        return gameDirection;
    }

    public void getNextPlayer(){
        nextPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }
}
