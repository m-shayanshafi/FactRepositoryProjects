package tit07.morris.exception;

/**
 * Diese Exception wird geworfen, wenn eine ungültie Spielsteinfarbe übergeben
 * wurde
 */
public class IllegalColorException extends Exception {

    /**
     * Erzeugt eine neue Exception mit einer Nachricht für den Grund
     * 
     * @param message Grund der Exception
     */
    public IllegalColorException( String message ) {

        super( message );
    }

}
