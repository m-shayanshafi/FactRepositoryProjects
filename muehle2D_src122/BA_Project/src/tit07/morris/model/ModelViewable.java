package tit07.morris.model;

import tit07.morris.exception.IllegalColorException;
import tit07.morris.exception.IllegalPositionException;


/**
 * Das Interface Viewable legt die Schnittstellen für das Model fest, welche
 * nötig sind um das Spiel zu visualisieren.
 */
public interface ModelViewable extends Configurateable {

    /**
     * Anzahl der Steine einer Farbe aus dem Spielbrett
     * 
     * @param color Farbe der abzufragenden Spielsteine
     * @return Anzahl der Steine auf dem Spielfeld
     * @throws IllegalColorException Wird bei einer ungültigen Steinfarbe
     *             geworfen
     */
    public int getStonesIn( StoneColor color ) throws IllegalColorException;

    /**
     * Anzahl der Steine einer Farbe, die noch zu setzen sind
     * 
     * @param color Farbe der abzufragenden Spielsteine
     * @return Anzahl der Steine die noch zu setzen sind
     * @throws IllegalColorException Wird bei einer ungültigen Steinfarbe
     *             geworfen
     */
    public int getStonesOut( StoneColor color ) throws IllegalColorException;

    /**
     * Status eines Spielers abfragen
     * 
     * @param color Steinfarbe des abzufragenden Spielers
     * @return Status des Spielers
     * @throws IllegalColorException Wird bei einer ungültigen Steinfarbe
     *             geworfen
     */
    public State getState( StoneColor color ) throws IllegalColorException;

    /**
     * Steinfarbe auf einer Position abfragen
     * 
     * @param position Position auf dem Spielfeld
     * @return Steinfarbe auf der übergebenen Position
     * @throws IllegalPositionException Wird bei einer ungültigen Position
     *             geworfen
     */
    public StoneColor getStoneColor( int position )
                                                   throws IllegalPositionException;

    /**
     * Abfrage, ob Spieler am Zug ist
     * 
     * @param color Steinfarbe des abzufragenden Spielers
     * @return True, wenn der Spieler am Zug ist
     * @throws IllegalColorException Wird bei einer ungültigen Steinfarbe
     *             geworfen
     */
    public boolean isItTurnOfColor( StoneColor color )
                                                      throws IllegalColorException;
}
