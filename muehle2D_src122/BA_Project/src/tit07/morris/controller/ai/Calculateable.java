package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;


/**
 * Das Interface Playable definiert die Schnittstellen, welche die KI haben muss
 * um einen Spielzug zu berechnen.
 */
public interface Calculateable {

    /**
     * Für eine übergebene Spielsituation wird ein gültiger Spielzug berechnet
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Gültiger Spielzug
     */
    public AIMove calculateNextMove( ModelAIInterface gameHandle );
}
