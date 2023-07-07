package UNO.Exception;

import messages.Messages;

public class DontHaveCardException extends Exception{

    public DontHaveCardException() {
        super(Messages.PLAYER_DONT_HAVE_CARD);
    }
}
