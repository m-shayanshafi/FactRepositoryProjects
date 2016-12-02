package tit07.morris.controller.ai;

import java.util.Random;
import tit07.morris.model.ModelAIInterface;


/**
 * Implementierung der Zufalls-KI welche von den m�glichen Spielz�gen einen
 * Spielzug zuf�llig ausw�hlt.
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
     * Gibt die Referenz der Instanz dieser Klasse zur�ck
     * 
     * @return Instanz der Klasse AIRandom
     */
    public static AIRandom getInstance() {

        return instance;
    }

    /**
     * Implementierung der Spielzugberechnung durch eine zuf�llige Auswahl
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Berechneter Spielzug der KI
     */
    @Override
    public AIMove calculateNextMove( ModelAIInterface gameHandle ) {

        /* Alle m�glichen Spielz�ge berechnen */
        AIPossibleMoves possibleMoves = new AIPossibleMoves( gameHandle );

        /* Instanz des ausgew�hlten Spielzuges */
        AIMove chosenMove = new AIMove();
        chosenMove.setAction( possibleMoves.getAction() );
        chosenMove.setActiveColor( possibleMoves.getActiveColor() );

        /* Anzahl der m�glichen Spielz�ge */
        int fromSize = possibleMoves.getFrom().size();
        int toSize = possibleMoves.getTo().size();

        /* Spielz�ge: ZIEHEN oder SPRINGEN, da zwei Positionen vorhanden */
        if( fromSize > 0 && toSize > 0 ) {
            int index = getRandomNumber( possibleMoves.getFrom().size() );
            chosenMove.setFrom( possibleMoves.getFrom().get( index ) );
            chosenMove.setTo( possibleMoves.getTo().get( index ) );
        }
        /* Spielz�ge: SETZEN oder ENTFERNEN, da eine Position vorhanden */
        else if( toSize > 0 ) {
            int index = getRandomNumber( possibleMoves.getTo().size() );
            chosenMove.setTo( possibleMoves.getTo().get( index ) );
            chosenMove.setFrom( 0 );
        }
        return chosenMove;
    }

    /**
     * Gibt eine Pseudo-Zufallszahl zwischen 0 (inklusive) und dem upperLimit
     * (exklusive) zur�ck.
     * 
     * @param upperLimit Obergrenze der Zufallszahl
     * @return Generierte Zufallszahl zwischen 0 und upperLimit
     */
    private int getRandomNumber( int upperLimit ) {

        return new Random().nextInt( upperLimit );
    }
}
