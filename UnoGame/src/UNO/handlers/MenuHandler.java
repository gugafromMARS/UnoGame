package UNO.handlers;

import UNO.Player.Player;
import messages.Messages;

public class MenuHandler {

    private DeckHandler deckHandler;
    private NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler;
    private boolean playerIsPlaying = true;

    public MenuHandler(DeckHandler deckHandler, NextAndPreviousPlayerHandler nextAndPreviousPlayerHandler) {
        this.deckHandler = deckHandler;
        this.nextAndPreviousPlayerHandler = nextAndPreviousPlayerHandler;
    }

    public void playerMenu(Player p) {
        String option = p.getPh().receiveMessageFromPlayer();
        switch (option.trim()){
            case "/draw":
                deckHandler.getCardHandler().drawCard(p);
                playerMenu(p);
                break;
            case "/multiple":
                p.getPh().sendMessageToPlayer(Messages.MULTIPLE_CARDS_RULE);
                String[] nCards = p.getPh().receiveMessageFromPlayer().split(",");
                deckHandler.getCardHandler().getMultipleCardsFromPlayer(nCards, p);
                playerIsPlaying = false;
                break;
            default:
                deckHandler.getCardHandler().dealWithCard(option, nextAndPreviousPlayerHandler.getCurrentPlayer());
                playerIsPlaying = false;
                break;
        }
    }

    public boolean isPlayerIsPlaying() {
        return playerIsPlaying;
    }

    public void setPlayerIsPlaying(boolean playerIsPlaying) {
        this.playerIsPlaying = playerIsPlaying;
    }
}
