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
     * Setzt einen neuen Besitzer f�r einen Stein auf dem Spielbrett. Der neue
     * Besitzer wird nur bei einer �bergebenen Instanz von StoneColor gesetzt.
     * 
     * @param position Position des Steins auf dem Spielbrett (1-24)
     * @param color Neue Spielsteinfarbe des Steins
     * @throws IllegalPositionException Wird geworfen bei ung�ltiger Position
     */
    public void setStoneColor( int position, StoneColor color )
                                                               throws IllegalPositionException;

    /**
     * Abfrage, ob Stein in einer M�hle ist.
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return True, wenn sich der Stein in einer M�hle befindet
     * @throws IllegalPositionException Wird geworfen bei ung�ltiger Position
     */
    public boolean isStoneInMill( int position )
                                                throws IllegalPositionException;

    /**
     * Abfrage, ob zwei Steine benachbart sind
     * 
     * @param position1 Position des ersten Steins (1-24)
     * @param position2 Position des zweiten Steins (1-24)
     * @return True wenn die beiden Steine benachbart sind
     * @throws IllegalPositionException Wird geworfen bei ung�ltiger Position
     */
    public boolean isNeighbour( int position1, int position2 )
                                                              throws IllegalPositionException;

    /**
     * Gibt die Anzahl der Steine, die nicht in einer M�hle stehen
     * 
     * @param color Spielsteinfarbe, f�r welchen die freien Steine gez�hlt
     *            werden sollen
     * @return Anzahl der Steine des Spielers, welche nicht in einer M�hle
     *         stehen
     */
    public int getNumberOfFreeStones( StoneColor color );

    /**
     * Gibt die Anzahl der m�glichen Bewegungen f�r einen Spieler
     * 
     * @param color Spielsteinfarbe, f�r welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der m�glichen Bewegungen
     */
    public int getNumberOfPossibleMoves( StoneColor color );

    /**
     * Gibt die Anzahl der potentiellen Bewegungsm�glichkeiten zur�ck
     * 
     * @param color Spielsteinfarbe, f�r welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der potentiellen Bewegungen (wenn alle sonstige Felder
     *         frei w�ren)
     */
    public int getNumberOfPotentialMoves( StoneColor color );

    /**
     * Gibt die Anzahl der Spielsteine eines Spielers
     * 
     * @param color Spielsteinfarbe f�r welchen die Steine gez�hlt werden sollen
     * @return Die Anzahl der Steine des Spielers auf dem Spielbrett
     */
    public int getNumberOfStones( StoneColor color );
}
