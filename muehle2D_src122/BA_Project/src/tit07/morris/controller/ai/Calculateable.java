package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;


/**
 * Das Interface Playable definiert die Schnittstellen, welche die KI haben muss
 * um einen Spielzug zu berechnen.
 */
public interface Calculateable {

    /**
     * F�r eine �bergebene Spielsituation wird ein g�ltiger Spielzug berechnet
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return G�ltiger Spielzug
     */
    public AIMove calculateNextMove( ModelAIInterface gameHandle );
}
