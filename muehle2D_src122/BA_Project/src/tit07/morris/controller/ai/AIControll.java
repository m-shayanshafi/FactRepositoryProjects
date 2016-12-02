package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.StoneColor;
import tit07.morris.model.config.PlayerInput;


/**
 * Die Klasse AIControll verwaltet die einzelnen KI-Implementierungen und leitet
 * Anfragen von Zugberechnungn an die ausgewählte KI-Implementierung weiter.
 */
public class AIControll implements Calculateable {

    /** Referenz auf die einzige Instanz der Klasse AIControll */
    private static AIControll instance = new AIControll();


    /**
     * Privater Konstruktor, da nur ein Objekt erzeugt werden soll.
     */
    private AIControll() {

    }

    /**
     * Gibt die Referenz der Instanz dieser Klasse zurück
     * 
     * @return Instanz der Klasse AIControll
     */
    public static AIControll getInstance() {

        return instance;
    }

    /**
     * Leitet die Anfrage zu Berechnung eines Spielzuges an die entsprechende
     * KI-Implementierung weiter
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Berechneter Spielzug der KI
     */
    @Override
    public AIMove calculateNextMove( ModelAIInterface gameHandle ) {

        Calculateable aiEngine = getCurrentAIEngine( gameHandle );
        if( aiEngine != null ) {
            return aiEngine.calculateNextMove( gameHandle );
        }
        return null;
    }

    /**
     * Gibt die KI-Engine der aktuellen Konfiguration wieder
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Aktive KI-Engine für den zugberechtigten Spieler
     */
    private Calculateable getCurrentAIEngine( ModelAIInterface gameHandle ) {

        /* Inputquelle für den zugberechtigen Spieler abfragen */
        PlayerInput input = null;
        try {
            if( gameHandle.isItTurnOfColor( StoneColor.WHITE ) ) {
                input = gameHandle.getConfig().getWhiteInput();
            }
            else if( gameHandle.isItTurnOfColor( StoneColor.BLACK ) ) {
                input = gameHandle.getConfig().getBlackInput();
            }
            else {
                return null;
            }
        }
        catch( Exception e ) {
        }

        /* Entsprechende Instanz der KI-Implementierung zurückgeben */
        switch( input ) {
        case CPU_VERY_WEAK:
            return AIMinimax.getInstance( HeuristicWeak.getInstance(), 3 );
            // return AIMinimax.getInstance( HeuristicNormal.getInstance(), 3 );
            // return AIRandom.getInstance(); // Auskommentieren für die
            // Zufalls-KI
        case CPU_WEAK:
            //return AIMinimax.getInstance( HeuristicNormal.getInstance(), 3 );
            return AIMinimax.getInstance( HeuristicTest.getInstance(), 5 );
        case CPU_NORMAL:
            return AIMinimax.getInstance( HeuristicStrong.getInstance(), 5 );            
        }
        return null;
    }
}
