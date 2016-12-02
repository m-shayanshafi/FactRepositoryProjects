package tit07.morris.controller.ai;

import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Die Klasse AIMove implementiert einen ausgew�hlten Spielzug der KI.
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
     * Erzeugt einen neuen AIMove, welcher zun�chst keine g�ltigen Zugdaten
     * enth�lt.
     */
    public AIMove() {

        this.activeColor = null;
        this.action = null;
        this.from = 0;
        this.to = 0;
    }

    /**
     * Gibt die Farbe des aktiven Spielers zur�ck
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
     * Gibt die Aktion des Spielzuges zur�ck
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
     * Gibt die Ausgangsposition f�r die Spielz�ge ZIEHEN und SPRINGEN zur�ck
     * 
     * @return Ausgangsposition auf dem Spielbrett
     */
    public int getFrom() {

        return this.from;
    }

    /**
     * Setzt die Ausgangsposition f�r die Spielz�ge ZIEHEN und SPRINGEN
     * 
     * @param from Ausgangsposition auf dem Spielbrett
     */
    public void setFrom( int from ) {

        this.from = from;
    }

    /**
     * Gibt die Position (Zielposition f�r die Spielz�ge ZIEHEN und SPRINGEN)
     * auf dem Spielfeld zur�ck
     * 
     * @return Position auf dem Spielfeld
     */
    public int getTo() {

        return this.to;
    }

    /**
     * Setzt die Position (Zielposition f�r die Spielz�ge ZIEHEN und SPRINGEN)
     * auf dem Spielfeld.
     * 
     * @param to Position auf dem Spielfeld
     */
    public void setTo( int to ) {

        this.to = to;
    }
}
