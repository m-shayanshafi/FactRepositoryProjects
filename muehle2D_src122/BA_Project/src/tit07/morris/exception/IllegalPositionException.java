package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn eine ungültige Position übergeben wurde
 */
public class IllegalPositionException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht für den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalPositionException( String message ) {

        super( message );
    }
}
