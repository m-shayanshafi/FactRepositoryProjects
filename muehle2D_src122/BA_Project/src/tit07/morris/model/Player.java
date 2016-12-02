package tit07.morris.model;

/**
 * Die Klasse Player repräsentiert einen Spieler für das Mühlespiel
 */
public class Player implements Cloneable {

    /** Spielsteinfarbe des Spielers */
    private final StoneColor color;

    /** Status des Spielers */
    private State            state;

    /** Anzahl der Steine außerhalb des Spielfeldes (noch zu setzen) */
    private int              stonesOut;


    /**
     * Erzeugt einen neuen Spieler mit einer Spielsteinfarbe
     * 
     * @param color Die Spielsteinfarbe des Spielers
     */
    public Player( StoneColor color ) {

        this.color = color;
    }

    /**
     * Abfrage der Spielsteinfarbe
     * 
     * @return Spielsteinfarbe des Spielers
     */
    public StoneColor getColor() {

        return color;
    }

    /**
     * Abfrage des Status
     * 
     * @return Status des Spielers
     */
    public State getState() {

        return state;
    }

    /**
     * Setzen des Status
     * 
     * @param state Neuer Status des Spielers
     */
    public void setState( State state ) {

        this.state = state;
    }

    /**
     * Abfrage der Spielsteine außerhalb des Spielbretts
     * 
     * @return Anzahl der Spielsteine außerhalb des Spielbretts
     */
    public int getStonesOut() {

        return stonesOut;
    }

    /**
     * Setzen der Spielsteine außerhalb des Spielbretts
     * 
     * @param stonesOut Neue Anzahl der Spielsteine außerhalb des Spielbretts
     */
    public void setStonesOut( int stonesOut ) {

        this.stonesOut = stonesOut;
    }

    /**
     * Erniedrigt die Anzahl der Spielsteine außerhalb des Spielbretts um 1
     */
    public void decrementStonesOut() {

        if( this.stonesOut > 0 ) {
            this.stonesOut--;
        }
    }

    /**
     * Erstellt eine Kopie des Spielers
     * 
     * @return Kopie des Spielers
     */
    @Override
    public Player clone() {

        Player playerClone = new Player( this.getColor() );
        playerClone.setState( this.getState() );
        playerClone.setStonesOut( this.getStonesOut() );
        return playerClone;
    }

    /**
     * Berechnet den neuen Status des Spielers wenn er dran ist
     * 
     * @param stonesIn Anzahl der Steine auf dem Spielbrett
     * @param movesPlayer Anzahl der Bewegungsmöglichkeiten
     * @param movesOppositePlayer Anzahl der Bewegungsmöglichkeiten des
     *            gegnerischen Spielers
     * @param oppositePlayer Referenz auf den gegnerischen Spieler
     * @param isDraw True, wenn das Spiel unentschieden ist
     */
    public void updateState( int stonesIn,
                             int movesPlayer,
                             int movesOppositePlayer,
                             Player oppositePlayer,
                             boolean isDraw ) {

        /* Überprüfen des Spielstatus */
        if( this.stonesOut > 0 ) {
            this.state = State.SET;
        }
        else if( stonesIn == 3 ) {
            this.state = State.JUMP;
        }
        else if( stonesIn < 3 || ( movesPlayer == 0 && stonesIn > 3 ) ) {
            this.state = State.LOSER;
            oppositePlayer.setState( State.WINNER );
        }
        else {
            this.state = State.MOVE;
        }
        if( isDraw ) {
            this.state = State.DRAW;
            oppositePlayer.setState( State.DRAW );
        }
    }
}
