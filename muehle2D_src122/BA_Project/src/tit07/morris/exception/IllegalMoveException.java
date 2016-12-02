package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn versucht wird ein Spielzug auszuf�hren,
 * welcher den Spielregeln widerspricht.
 */
public class IllegalMoveException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht f�r den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalMoveException( String message ) {

        super( message );
    }
}
