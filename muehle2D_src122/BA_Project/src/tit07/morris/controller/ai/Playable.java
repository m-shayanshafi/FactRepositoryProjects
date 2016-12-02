package tit07.morris.controller.ai;

import tit07.morris.model.StoneColor;


/**
 * Schnittstellen, welche der Controller bereitstellt, damit die KI einen
 * Spielzug ausf�hren kann.
 */
public interface Playable {

    /**
     * F�hre entsprechenden Spielzug aus
     * 
     * @param toPosition Zielposition
     * @param fromPosition Ausgangsposition
     * @param stoneColor Farbe des aktiven Spielers
     * @param isAI True, wenn der Spielzug von der KI ausgef�hrt wird
     */
    public void performMove( int toPosition,
                             int fromPosition,
                             StoneColor stoneColor,
                             boolean isAI );

    /**
     * Gibt an, ob bereits ein Spielzug ausgef�hrt wird
     * 
     * @return True, wenn bereits ein Spielzug ausgef�hrt wird
     */
    public boolean isMoving();
}
