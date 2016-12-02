package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn eine ung�ltie Spielsteinfarbe �bergeben
 * wurde
 */
public class IllegalColorException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht f�r den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalColorException( String message ) {

        super( message );
    }

}
