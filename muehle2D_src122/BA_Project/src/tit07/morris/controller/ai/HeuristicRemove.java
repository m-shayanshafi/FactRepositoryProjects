package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Implementierung der Bewertungsfunktion um Spielsteine zu entfernen
 */
public class HeuristicRemove extends Heuristic {

    /** Referenz auf die einzige Instanz der Klasse HeuristicRemove */
    private static HeuristicRemove instance = new HeuristicRemove();


    /**
     * Privater Konstruktor, da nicht beliebig viele Objekte erzeugt werden
     * sollen.
     */
    private HeuristicRemove() {

    }

    /**
     * Gibt die Referenz der Instanz dieser Klasse zurück
     * 
     * @return Instanz der Klasse HeuristicNormal
     */
    public static HeuristicRemove getInstance() {

        return instance;
    }

    /**
     * Implementierung der Bewertungsfunktion: BLACK <+++0---> WHITE Je
     * positiver der Wert, desto mehr ist schwarz im Vorteil. Je negativer der
     * Wert, desto mehr ist weiß im Vorteil.
     * 
     * @return Bewertung der Spielsituation
     */
    @Override
    public int evaluateBorder( ModelAIInterface gameHandle ) {

        int whiteValue = 0;
        int blackValue = 0;

        int numberOfOpenMorrisBlack = getNumberOfOpenMills( gameHandle,
                                                            StoneColor.BLACK );
        int numberOfOpenMorrisWhite = getNumberOfOpenMills( gameHandle,
                                                            StoneColor.WHITE );
        int numberOfMovesBlack = gameHandle
                                           .getNumberOfPossibleMoves( StoneColor.BLACK );
        int numberOfMovesWhite = gameHandle
                                           .getNumberOfPossibleMoves( StoneColor.WHITE );

        int factorOpenBlack = 100;
        int factorOpenWhite = 100;
        try {
            /* Schwarz hat eine Mühle geschlossen */
            if( gameHandle.getState( StoneColor.BLACK ) == State.REMOVE ) {
                blackValue += 100000;
            }
            /* Weiß hat eine Mühle geschlossen */
            else if( gameHandle.getState( StoneColor.WHITE ) == State.REMOVE ) {
                whiteValue += 100000;
            }
            /* Schwarz hat einen Stein entfernt */
            if( gameHandle.isItTurnOfColor( StoneColor.WHITE ) ) {
                factorOpenWhite = 500;
            }
            /* Weiß hat einen Stein entfernt */
            else if( gameHandle.isItTurnOfColor( StoneColor.BLACK ) ) {
                factorOpenBlack = 500;
            }
            /* Schwarz hat gewonnen */
            if( gameHandle.getState( StoneColor.BLACK ) == State.WINNER ) {
                blackValue += 1000000;
            }
            /* Weiß hat gewonnen */
            else if( gameHandle.getState( StoneColor.WHITE ) == State.WINNER ) {
                whiteValue += 1000000;
            }
        }
        catch( Exception e ) {
        }

        blackValue += ( ( numberOfOpenMorrisBlack * factorOpenBlack ) + ( numberOfMovesBlack * 25 ) );
        whiteValue += ( ( numberOfOpenMorrisWhite * factorOpenWhite ) + ( numberOfMovesWhite * 25 ) );
        return ( blackValue - whiteValue );
    }
}
