package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn eine ung�ltige Position �bergeben wurde
 */
public class IllegalPositionException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht f�r den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalPositionException( String message ) {

        super( message );
    }
}
