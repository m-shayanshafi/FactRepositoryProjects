package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn versucht wird ein Spielzug auszuführen,
 * welcher den Spielregeln widerspricht.
 */
public class IllegalMoveException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht für den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalMoveException( String message ) {

        super( message );
    }
}
