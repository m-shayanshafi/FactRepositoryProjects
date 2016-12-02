package tit07.morris.model;

import tit07.morris.exception.IllegalPositionException;


/**
 * Legt die Schnittstellen fest welche das Model implementieren muss, damit eine
 * KI darauf aufsetzen kann.
 */
public interface ModelAIInterface extends ModelControllable {

    /**
     * Kopiert das aktuelle Spiel
     * 
     * @return Tiefe kopie des aktuellen Spiels
     */
    public ModelAIInterface clone();

    /**
     * Setzt einen neuen Besitzer für einen Stein auf dem Spielbrett. Der neue
     * Besitzer wird nur bei einer übergebenen Instanz von StoneColor gesetzt.
     * 
     * @param position Position des Steins auf dem Spielbrett (1-24)
     * @param color Neue Spielsteinfarbe des Steins
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public void setStoneColor( int position, StoneColor color )
                                                               throws IllegalPositionException;

    /**
     * Abfrage, ob Stein in einer Mühle ist.
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return True, wenn sich der Stein in einer Mühle befindet
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public boolean isStoneInMill( int position )
                                                throws IllegalPositionException;

    /**
     * Abfrage, ob zwei Steine benachbart sind
     * 
     * @param position1 Position des ersten Steins (1-24)
     * @param position2 Position des zweiten Steins (1-24)
     * @return True wenn die beiden Steine benachbart sind
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public boolean isNeighbour( int position1, int position2 )
                                                              throws IllegalPositionException;

    /**
     * Gibt die Anzahl der Steine, die nicht in einer Mühle stehen
     * 
     * @param color Spielsteinfarbe, für welchen die freien Steine gezählt
     *            werden sollen
     * @return Anzahl der Steine des Spielers, welche nicht in einer Mühle
     *         stehen
     */
    public int getNumberOfFreeStones( StoneColor color );

    /**
     * Gibt die Anzahl der möglichen Bewegungen für einen Spieler
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der möglichen Bewegungen
     */
    public int getNumberOfPossibleMoves( StoneColor color );

    /**
     * Gibt die Anzahl der potentiellen Bewegungsmöglichkeiten zurück
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der potentiellen Bewegungen (wenn alle sonstige Felder
     *         frei wären)
     */
    public int getNumberOfPotentialMoves( StoneColor color );

    /**
     * Gibt die Anzahl der Spielsteine eines Spielers
     * 
     * @param color Spielsteinfarbe für welchen die Steine gezählt werden sollen
     * @return Die Anzahl der Steine des Spielers auf dem Spielbrett
     */
    public int getNumberOfStones( StoneColor color );
}
