package tit07.morris.controller.ai;

import tit07.morris.model.StoneColor;


/**
 * Schnittstellen, welche der Controller bereitstellt, damit die KI einen
 * Spielzug ausführen kann.
 */
public interface Playable {

    /**
     * Führe entsprechenden Spielzug aus
     * 
     * @param toPosition Zielposition
     * @param fromPosition Ausgangsposition
     * @param stoneColor Farbe des aktiven Spielers
     * @param isAI True, wenn der Spielzug von der KI ausgeführt wird
     */
    public void performMove( int toPosition,
                             int fromPosition,
                             StoneColor stoneColor,
                             boolean isAI );

    /**
     * Gibt an, ob bereits ein Spielzug ausgeführt wird
     * 
     * @return True, wenn bereits ein Spielzug ausgeführt wird
     */
    public boolean isMoving();
}
