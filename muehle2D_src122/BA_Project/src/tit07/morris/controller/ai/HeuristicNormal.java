package tit07.morris.controller.ai;

import tit07.morris.exception.IllegalColorException;
import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Implementierung der Bewertungsfunktion für das Spielfeld
 */
public class HeuristicNormal extends Heuristic {

    /** Referenz auf die einzige Instanz der Klasse HeuristicNormal */
    private static HeuristicNormal instance = new HeuristicNormal();


    /**
     * Privater Konstruktor, da nicht beliebig viele Objekte erzeugt werden
     * sollen.
     */
    private HeuristicNormal() {

    }

    /**
     * Gibt die Referenz der Instanz dieser Klasse zurück
     * 
     * @return Instanz der Klasse HeuristicNormal
     */
    public static HeuristicNormal getInstance() {

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
    public int evaluateBorder( ModelAIInterface game ) {

        /* Anzahl der schwarzen Bewegungsmöglichkeiten */
        int numberOfMovesBlack = game
                                     .getNumberOfPossibleMoves( StoneColor.BLACK );

        /* Anzahl der schwarzen Steine im Feld */
        int numberOfStonesBlack = game.getNumberOfStones( StoneColor.BLACK );

        /* Anzahl der schwarzen offenen Mühlen */
        int numberOfOpenMorrisBlack = getNumberOfOpenMills( game,
                                                            StoneColor.BLACK );

        /* Anzahl der schwarzen geschlossenen Mühlen */
        int morrisOfClosedMorrisBlack = getNumberOfClosedMills( game,
                                                                StoneColor.BLACK );

        /* Anzahl der weißen Bewegungsmöglichkeiten */
        int numberOfMovesWhite = game
                                     .getNumberOfPossibleMoves( StoneColor.WHITE );

        /* Anzahl der weißen Steine im Feld */
        int numberOfStonesWhite = game.getNumberOfStones( StoneColor.WHITE );

        /* Anzahl der weißen offenen Mühlen */
        int numberOfOpenMorrisWhite = getNumberOfOpenMills( game,
                                                            StoneColor.WHITE );

        /* Anzahl der weißen geschlossenen Mühlen */
        int morrisOfClosedMorrisWhite = getNumberOfClosedMills( game,
                                                                StoneColor.WHITE );

        /* Anzahl der schwarzen potentiellen Bewegungsmöglichkeiten */
        int numberOfPotentialMovesBlack = game
                                              .getNumberOfPotentialMoves( StoneColor.BLACK );

        /* Anzahl der weißen potentiellen Bewegungsmöglichkeiten */
        int numberOfPotentialMovesWhite = game
                                              .getNumberOfPotentialMoves( StoneColor.WHITE );

        /* Bewertungsfaktoren festlegen */
        int factorOpenMorrisBlack = 200;
        int factorOpenMorrisWhite = 200;
        int factorClosedMorrisBlack = 100;
        int factorClosedMorrisWhite = 100;
        int factorMovesBlack = 70; // 25 // 70org
        int factorMovesWhite = 70; // 25 // 70org
        int factorStonesBlack = 10000;
        int factorStonesWhite = 10000;
        int factorPotentialBlack = 30; // 0 // 30org
        int factorPotentialWhite = 30; // 0 // 30org
        int whiteAddition = 0;
        int blackAddition = 0;

        /* Bewertungsfaktoren modifizieren */
        try {
            /* Weiß war am Zug */
            if( game.isItTurnOfColor( StoneColor.BLACK ) ) {
                factorOpenMorrisBlack = 320;
                factorOpenMorrisWhite = 420;
                factorClosedMorrisBlack = 20;
                factorClosedMorrisWhite = 40;
            }
            /* Schwarz war am Zug */
            else if( game.isItTurnOfColor( StoneColor.WHITE ) ) {
                factorOpenMorrisBlack = 420;
                factorOpenMorrisWhite = 320;
                factorClosedMorrisBlack = 40;
                factorClosedMorrisWhite = 20;
            }
            /* Schwarz hat gewonnen */
            if( game.getState( StoneColor.BLACK ) == State.WINNER ) {
                blackAddition += 1000000;
            }
            /* Weiß hat gewonnen */
            else if( game.getState( StoneColor.WHITE ) == State.WINNER ) {
                whiteAddition += 1000000;
            }
        }
        catch( IllegalColorException e ) {
        }

        int blackValue = ( numberOfMovesBlack * factorMovesBlack )
                + ( numberOfStonesBlack * factorStonesBlack )
                + ( numberOfOpenMorrisBlack * factorOpenMorrisBlack )
                + ( morrisOfClosedMorrisBlack * factorClosedMorrisBlack )
                + ( numberOfPotentialMovesBlack * factorPotentialBlack );
        int whiteValue = ( numberOfMovesWhite * factorMovesWhite )
                + ( numberOfStonesWhite * factorStonesWhite )
                + ( numberOfOpenMorrisWhite * factorOpenMorrisWhite )
                + ( morrisOfClosedMorrisWhite * factorClosedMorrisWhite )
                + ( numberOfPotentialMovesWhite * factorPotentialWhite );
        return ( blackValue + blackAddition ) - ( whiteValue + whiteAddition );
    }
}
