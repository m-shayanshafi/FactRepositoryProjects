package tit07.morris.controller.ai;

import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Die Klasse AIMove implementiert einen ausgewählten Spielzug der KI.
 */
public class AIMove {

    /** Farbe des aktiven Spielers */
    private StoneColor activeColor;

    /** Art des Spielzugs des Spielers */
    private State      action;

    /** Bei ZIEHEN oder SPRINGEN die Ausgangsposition */
    private int        from;

    /** Position auf dem Spielfeld (bei ZIEHEN oder SPRINGEN die Zielposition) */
    private int        to;


    /**
     * Erzeugt einen neuen AIMove, welcher zunächst keine gültigen Zugdaten
     * enthält.
     */
    public AIMove() {

        this.activeColor = null;
        this.action = null;
        this.from = 0;
        this.to = 0;
    }

    /**
     * Gibt die Farbe des aktiven Spielers zurück
     * 
     * @return Farbe des aktiven Spielers
     */
    public StoneColor getActiveColor() {

        return this.activeColor;
    }

    /**
     * Setzt die Farbe des aktiven Spielers
     * 
     * @param activeColor Farbe des aktiven Spielers
     */
    public void setActiveColor( StoneColor activeColor ) {

        this.activeColor = activeColor;
    }

    /**
     * Gibt die Aktion des Spielzuges zurück
     * 
     * @return Aktion des Spielzuges
     */
    public State getAction() {

        return this.action;
    }

    /**
     * Setzt die Aktion des Spielzuges
     * 
     * @param action Aktion des Spielzuges
     */
    public void setAction( State action ) {

        this.action = action;
    }

    /**
     * Gibt die Ausgangsposition für die Spielzüge ZIEHEN und SPRINGEN zurück
     * 
     * @return Ausgangsposition auf dem Spielbrett
     */
    public int getFrom() {

        return this.from;
    }

    /**
     * Setzt die Ausgangsposition für die Spielzüge ZIEHEN und SPRINGEN
     * 
     * @param from Ausgangsposition auf dem Spielbrett
     */
    public void setFrom( int from ) {

        this.from = from;
    }

    /**
     * Gibt die Position (Zielposition für die Spielzüge ZIEHEN und SPRINGEN)
     * auf dem Spielfeld zurück
     * 
     * @return Position auf dem Spielfeld
     */
    public int getTo() {

        return this.to;
    }

    /**
     * Setzt die Position (Zielposition für die Spielzüge ZIEHEN und SPRINGEN)
     * auf dem Spielfeld.
     * 
     * @param to Position auf dem Spielfeld
     */
    public void setTo( int to ) {

        this.to = to;
    }
}
