package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;


/**
 * Das Interface Decideable definiert die Schnittstellen, welche eine Heuristik
 * haben muss, um das Spiel zu beurteilen.
 */
public interface Decideable {

    /**
     * Evaluierungsfunktion zur Beurteilung des Spielfeldes (positiv: gut f�r
     * schwarz / negativ: gut f�r wei�)
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Beurteilung der Spielsituation
     */
    public int evaluateBorder( ModelAIInterface gameHandle );
}
