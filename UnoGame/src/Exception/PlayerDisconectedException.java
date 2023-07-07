package Exception;

public class PlayerDisconectedException extends Exception{

    public PlayerDisconectedException(String message) {
        super("Player disconnected...");
    }
}
