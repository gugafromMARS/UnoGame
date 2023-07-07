package UNO.Exception;

import messages.Messages;

public class CantPlayCardException extends Exception{

    public CantPlayCardException() {
        super(Messages.CANT_PLAY_THIS_CARD);
    }
}
