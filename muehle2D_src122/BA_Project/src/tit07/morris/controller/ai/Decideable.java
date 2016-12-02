package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;


/**
 * Das Interface Decideable definiert die Schnittstellen, welche eine Heuristik
 * haben muss, um das Spiel zu beurteilen.
 */
public interface Decideable {

    /**
     * Evaluierungsfunktion zur Beurteilung des Spielfeldes (positiv: gut für
     * schwarz / negativ: gut für weiß)
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Beurteilung der Spielsituation
     */
    public int evaluateBorder( ModelAIInterface gameHandle );
}
