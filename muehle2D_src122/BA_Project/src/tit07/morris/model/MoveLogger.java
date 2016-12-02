package tit07.morris.model;

/**
 * Die Klasse MoveLogger speichert eine bestimmte Anzahl von Zügen, wodurch die
 * feststellen kann ob Zugwiederholungen vorliegen. Diese können abgefragt
 * werden und das Spiel mit unentschieden beendet werden.
 */
public class MoveLogger {

    /** Anzahl der maximalen Zugwiederholungen pro Spieler */
    private final int REPEAT_MAX;

    /** Aktueller Index des weißen Spielers */
    private int       indexWhite        = 0;

    /** Aktueller Index des schwarzen Spielers */
    private int       indexBlack        = 0;

    /** Schwarze Ausgangspositionen */
    private int[]     positionBlackFrom = null;

    /** Schwarze Zielpositionen */
    private int[]     positionBlackTo   = null;

    /** Schwarze Spielaktionen */
    private State[]   actionBlack       = null;

    /** Weiße Ausgangspositionen */
    private int[]     positionWhiteFrom = null;

    /** Weiße Zielpositionen */
    private int[]     positionWhiteTo   = null;

    /** Weiße Spielaktionen */
    private State[]   actionWhite       = null;


    /**
     * Erzeugt einen neuen MoveLogger.
     * 
     * @param model Referenz auf das Model
     */
    public MoveLogger( Configurateable model ) {

        this.REPEAT_MAX = model.getConfig().getMaxRepeat();
        this.positionBlackFrom = new int[ REPEAT_MAX ];
        this.positionBlackTo = new int[ REPEAT_MAX ];
        this.positionWhiteFrom = new int[ REPEAT_MAX ];
        this.positionWhiteTo = new int[ REPEAT_MAX ];
        this.actionWhite = new State[ REPEAT_MAX ];
        this.actionBlack = new State[ REPEAT_MAX ];
    }

    /**
     * Loggt einen Spielzug.
     * 
     * @param positionFrom Ausgangsposition
     * @param positionTo Zielposition
     * @param action Aktion des Spielers
     * @param stoneColor Farbe des aktiven Spielers
     */
    public void logMove( int positionFrom,
                         int positionTo,
                         State action,
                         StoneColor stoneColor ) {

        if( stoneColor == StoneColor.BLACK || stoneColor == StoneColor.WHITE ) {

            if( stoneColor == StoneColor.BLACK ) {
                this.positionBlackFrom[this.indexBlack] = positionFrom;
                this.positionBlackTo[this.indexBlack] = positionTo;
                this.actionBlack[this.indexBlack] = action;
                this.indexBlack++;
                this.indexBlack = this.indexBlack % REPEAT_MAX;
            }
            else {
                this.positionWhiteFrom[this.indexWhite] = positionFrom;
                this.positionWhiteTo[this.indexWhite] = positionTo;
                this.actionWhite[this.indexBlack] = action;
                this.indexWhite++;
                this.indexWhite = this.indexWhite % REPEAT_MAX;
            }
        }
    }

    /**
     * Fragt ab, ob die maximimale Anzahl der Zugwiederholungen erreicht ist
     * 
     * @return True, wenn die maximale Anzahl der Zugwiederholungen erreicht ist
     */
    public boolean isDraw() {

        for( int i = 0; i < REPEAT_MAX - 2; i++ ) {
            if( this.positionWhiteFrom[i] != this.positionWhiteFrom[i + 2] ) {
                return false;
            }
            if( this.positionBlackFrom[i] != this.positionBlackFrom[i + 2] ) {
                return false;
            }
            if( this.positionWhiteTo[i] != this.positionWhiteTo[i + 2] ) {
                return false;
            }
            if( this.positionBlackTo[i] != this.positionBlackTo[i + 2] ) {
                return false;
            }
        }

        State compareState = this.actionBlack[0];
        for( int i = 1; i < this.REPEAT_MAX; i++ ) {
            if( compareState != this.actionBlack[i] ) {
                return false;
            }
        }
        compareState = this.actionWhite[0];
        for( int i = 1; i < this.REPEAT_MAX; i++ ) {
            if( compareState != this.actionWhite[i] ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Setzt den MoveLogger in den Ausgangszustand zurück
     */
    public void resetLogger() {

        for( int i = 0; i < REPEAT_MAX; i++ ) {
            this.positionWhiteFrom[i] = 0;
            this.positionBlackFrom[i] = 0;
            this.positionWhiteTo[i] = 0;
            this.positionBlackTo[i] = 0;
            this.actionWhite[i] = null;
            this.actionBlack[i] = null;
        }
    }
}
