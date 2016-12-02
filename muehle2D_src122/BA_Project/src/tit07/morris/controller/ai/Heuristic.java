package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.StoneColor;


/**
 * Implementierung der Grundfunktionen zur Bewertung eines Spiels, auf welche
 * eine Heuristik aufsetzen kann.
 */
public abstract class Heuristic implements Decideable {

    /**
     * Gibt die Anzahl der geschlossenen Mühlen zurück
     * 
     * @param border Aktuelles Spielbrett
     * @param color Farbe der zu zählenden Mühlen
     * @return Anzahl der geschlossenen Mühlen
     */
    protected int getNumberOfClosedMills( ModelAIInterface border,
                                          StoneColor color ) {

        int numberOfMorris = 0;

        for( int i = 1; i <= 16; i++ ) {
            StoneColor[] morris = getMill( border, i );
            if( morris[0] == color && morris[1] == color && morris[2] == color ) {
                numberOfMorris++;
            }
        }
        return numberOfMorris;
    }

    /**
     * Gibt die Anzahl der offenen Mühlen zurück
     * 
     * @param border Aktuelles Spielbrett
     * @param color Farbe der zu zählenden Mühlen
     * @return Anzahl der geschlossenen Mühlen
     */
    protected int getNumberOfOpenMills( ModelAIInterface border,
                                        StoneColor color ) {

        int numberOfMorris = 0;

        for( int i = 1; i <= 16; i++ ) {
            if( isOpenMill( color, getMill( border, i ) ) ) {
                numberOfMorris++;
            }
        }
        return numberOfMorris;
    }

    /**
     * Stellt fest, ob die übergebene Mühle eine offene Mühle ist
     * 
     * @param color Farbe der zu überprüfender Mühle
     * @param morris Mühle, bestehend aus drei Steinen
     * @return True, wenn die Mühle eine offene Mühle ist, ansonsten false.
     */
    protected boolean isOpenMill( StoneColor color, StoneColor[] morris ) {

        int numberOfColor = 0;
        int numberOfEmpty = 0;

        for( int i = 0; i < morris.length; i++ ) {
            if( morris[i] == color ) {
                numberOfColor++;
            }
            if( morris[i] == StoneColor.NONE ) {
                numberOfEmpty++;
            }
        }
        return ( numberOfColor == 2 && numberOfEmpty == 1 );
    }

    /**
     * Gibt die entsprechende Mühle mit dem übergebenen Index zurück
     * 
     * @param border Aktuelles Spielbrett
     * @param index Index der Mühle (1-16)
     * @return Mühle bestehend aus drei Steinen
     */
    protected StoneColor[] getMill( ModelAIInterface border, int index ) {

        StoneColor[] morris = new StoneColor[ 3 ];
        for( int i = 0; i < morris.length; i++ ) {
            morris[i] = StoneColor.NONE;
        }

        try {
            /* Horizontale Mühle */
            if( index >= 1 && index <= 8 ) {
                int start = index + ( index * 2 - 2 );
                for( int i = 0; i < 3; i++ ) {
                    morris[i] = border.getStoneColor( start + i );
                }
            }
            /* Vertikale Mühle */
            else if( index >= 9 && index <= 16 ) {
                switch( index ) {
                case 9:
                    morris[0] = border.getStoneColor( 1 );
                    morris[1] = border.getStoneColor( 10 );
                    morris[2] = border.getStoneColor( 22 );
                    break;
                case 10:
                    morris[0] = border.getStoneColor( 4 );
                    morris[1] = border.getStoneColor( 11 );
                    morris[2] = border.getStoneColor( 19 );
                    break;
                case 11:
                    morris[0] = border.getStoneColor( 7 );
                    morris[1] = border.getStoneColor( 12 );
                    morris[2] = border.getStoneColor( 16 );
                    break;
                case 12:
                    morris[0] = border.getStoneColor( 2 );
                    morris[1] = border.getStoneColor( 5 );
                    morris[2] = border.getStoneColor( 8 );
                    break;
                case 13:
                    morris[0] = border.getStoneColor( 17 );
                    morris[1] = border.getStoneColor( 20 );
                    morris[2] = border.getStoneColor( 23 );
                    break;
                case 14:
                    morris[0] = border.getStoneColor( 9 );
                    morris[1] = border.getStoneColor( 13 );
                    morris[2] = border.getStoneColor( 18 );
                    break;
                case 15:
                    morris[0] = border.getStoneColor( 6 );
                    morris[1] = border.getStoneColor( 14 );
                    morris[2] = border.getStoneColor( 21 );
                    break;
                case 16:
                    morris[0] = border.getStoneColor( 3 );
                    morris[1] = border.getStoneColor( 15 );
                    morris[2] = border.getStoneColor( 24 );
                    break;
                }
            }
            return morris;
        }
        catch( Exception e ) {
        }
        return null;
    }
}
