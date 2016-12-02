package tit07.morris.model;

/**
 * Diese Enumeration repräsentiert die verschiedenen Status der Spieler.
 */
public enum State {
    /**
     * Auf Zug vom Gegner warten
     */
    WAIT,

    /**
     * Stein auf das Spielbrett setzen
     */
    SET,

    /**
     * Mit Stein im Spielfeld ziehen
     */
    MOVE,

    /**
     * Mit Stein im Spielfeld springen
     */
    JUMP,

    /**
     * Gegnerischen Stein vom Spielfeld entfernen
     */
    REMOVE,

    /**
     * Gewinner des Spiels
     */
    WINNER,

    /**
     * Verlierer des Spiels
     */
    LOSER,

    /**
     * Unentschieden
     */
    DRAW
}
