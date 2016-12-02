package tit07.morris.controller.ai;

import tit07.morris.exception.IllegalColorException;
import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Implementierung der Bewertungsfunktion f�r das Spielfeld
 */
public class HeuristicWeak extends Heuristic {

    /** Referenz auf die einzige Instanz der Klasse HeuristicNormal */
    private static HeuristicWeak instance = new HeuristicWeak();


    /**
     * Privater Konstruktor, da nicht beliebig viele Objekte erzeugt werden
     * sollen.
     */
    private HeuristicWeak() {

    }

    /**
     * Gibt die Referenz der Instanz dieser Klasse zur�ck
     * 
     * @return Instanz der Klasse HeuristicWeak
     */
    public static HeuristicWeak getInstance() {

        return instance;
    }

    /**
     * Implementierung der Bewertungsfunktion: BLACK <+++0---> WHITE Je
     * positiver der Wert, desto mehr ist schwarz im Vorteil. Je negativer der
     * Wert, desto mehr ist wei� im Vorteil.
     * 
     * @return Bewertung der Spielsituation
     */
    @Override
    public int evaluateBorder( ModelAIInterface game ) {

        /* Anzahl der schwarzen Bewegungsm�glichkeiten */
        int numberOfMovesBlack = game
                                     .getNumberOfPossibleMoves( StoneColor.BLACK );

        /* Anzahl der schwarzen Steine im Feld */
        int numberOfStonesBlack = game.getNumberOfStones( StoneColor.BLACK );

        /* Anzahl der schwarzen offenen M�hlen */
        int numberOfOpenMorrisBlack = getNumberOfOpenMills( game,
                                                            StoneColor.BLACK );

        /* Anzahl der schwarzen geschlossenen M�hlen */
        int morrisOfClosedMorrisBlack = getNumberOfClosedMills( game,
                                                                StoneColor.BLACK );

        /* Anzahl der wei�en Bewegungsm�glichkeiten */
        int numberOfMovesWhite = game
                                     .getNumberOfPossibleMoves( StoneColor.WHITE );

        /* Anzahl der wei�en Steine im Feld */
        int numberOfStonesWhite = game.getNumberOfStones( StoneColor.WHITE );

        /* Anzahl der wei�en offenen M�hlen */
        int numberOfOpenMorrisWhite = getNumberOfOpenMills( game,
                                                            StoneColor.WHITE );

        /* Anzahl der wei�en geschlossenen M�hlen */
        int morrisOfClosedMorrisWhite = getNumberOfClosedMills( game,
                                                                StoneColor.WHITE );

        /* Bewertungsfaktoren festlegen */
        int factorOpenMorrisBlack = 200;
        int factorOpenMorrisWhite = 200;
        int factorClosedMorrisBlack = 100;
        int factorClosedMorrisWhite = 100;
        int factorMovesBlack = 25;
        int factorMovesWhite = 25;
        int factorStonesBlack = 10000;
        int factorStonesWhite = 10000;
        int whiteAddition = 0;
        int blackAddition = 0;

        /* Bewertungsfaktoren modifizieren */
        try {
            /* Schwarz hat eine M�hle geschlossen */
            if( game.getState( StoneColor.BLACK ) == State.REMOVE ) {
                blackAddition += 100000;
            }
            /* Wei� hat eine M�hle geschlossen */
            else if( game.getState( StoneColor.WHITE ) == State.REMOVE ) {
                whiteAddition += 100000;
            }
            /* Wei� war am Zug */
            if( game.isItTurnOfColor( StoneColor.BLACK ) ) {
                factorOpenMorrisBlack = 320;
                factorOpenMorrisWhite = 640;
                factorClosedMorrisBlack = 20;
                factorClosedMorrisWhite = 40;
            }
            /* Schwarz war am Zug */
            else if( game.isItTurnOfColor( StoneColor.WHITE ) ) {
                factorOpenMorrisBlack = 640;
                factorOpenMorrisWhite = 320;
                factorClosedMorrisBlack = 40;
                factorClosedMorrisWhite = 20;
            }
            /* Schwarz hat gewonnen */
            if( game.getState( StoneColor.BLACK ) == State.WINNER ) {
                blackAddition += 1000000;
            }
            /* Wei� hat gewonnen */
            else if( game.getState( StoneColor.WHITE ) == State.WINNER ) {
                whiteAddition += 1000000;
            }
        }
        catch( IllegalColorException e ) {
        }

        int blackValue = ( numberOfMovesBlack * factorMovesBlack )
                + ( numberOfStonesBlack * factorStonesBlack )
                + ( numberOfOpenMorrisBlack * factorOpenMorrisBlack )
                + ( morrisOfClosedMorrisBlack * factorClosedMorrisBlack );
        int whiteValue = ( numberOfMovesWhite * factorMovesWhite )
                + ( numberOfStonesWhite * factorStonesWhite )
                + ( numberOfOpenMorrisWhite * factorOpenMorrisWhite )
                + ( morrisOfClosedMorrisWhite * factorClosedMorrisWhite );
        return ( blackValue + blackAddition ) - ( whiteValue + whiteAddition );
    }
}
