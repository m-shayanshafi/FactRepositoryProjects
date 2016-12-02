package tit07.morris.controller.ai;

import java.util.Random;
import tit07.morris.model.ModelAIInterface;


/**
 * Implementierung der Zufalls-KI welche von den möglichen Spielzügen einen
 * Spielzug zufällig auswählt.
 */
public class AIRandom implements Calculateable {

    /** Referenz auf die einzige Instanz der Klasse AIRandom */
    private static AIRandom instance = new AIRandom();


    /**
     * Privater Konstruktor, da nicht beliebig viele Objekte erzeugt werden
     * sollen.
     */
    private AIRandom() {

    }

    /**
     * Gibt die Referenz der Instanz dieser Klasse zurück
     * 
     * @return Instanz der Klasse AIRandom
     */
    public static AIRandom getInstance() {

        return instance;
    }

    /**
     * Implementierung der Spielzugberechnung durch eine zufällige Auswahl
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Berechneter Spielzug der KI
     */
    @Override
    public AIMove calculateNextMove( ModelAIInterface gameHandle ) {

        /* Alle möglichen Spielzüge berechnen */
        AIPossibleMoves possibleMoves = new AIPossibleMoves( gameHandle );

        /* Instanz des ausgewählten Spielzuges */
        AIMove chosenMove = new AIMove();
        chosenMove.setAction( possibleMoves.getAction() );
        chosenMove.setActiveColor( possibleMoves.getActiveColor() );

        /* Anzahl der möglichen Spielzüge */
        int fromSize = possibleMoves.getFrom().size();
        int toSize = possibleMoves.getTo().size();

        /* Spielzüge: ZIEHEN oder SPRINGEN, da zwei Positionen vorhanden */
        if( fromSize > 0 && toSize > 0 ) {
            int index = getRandomNumber( possibleMoves.getFrom().size() );
            chosenMove.setFrom( possibleMoves.getFrom().get( index ) );
            chosenMove.setTo( possibleMoves.getTo().get( index ) );
        }
        /* Spielzüge: SETZEN oder ENTFERNEN, da eine Position vorhanden */
        else if( toSize > 0 ) {
            int index = getRandomNumber( possibleMoves.getTo().size() );
            chosenMove.setTo( possibleMoves.getTo().get( index ) );
            chosenMove.setFrom( 0 );
        }
        return chosenMove;
    }

    /**
     * Gibt eine Pseudo-Zufallszahl zwischen 0 (inklusive) und dem upperLimit
     * (exklusive) zurück.
     * 
     * @param upperLimit Obergrenze der Zufallszahl
     * @return Generierte Zufallszahl zwischen 0 und upperLimit
     */
    private int getRandomNumber( int upperLimit ) {

        return new Random().nextInt( upperLimit );
    }
}
