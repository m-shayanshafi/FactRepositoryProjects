package tit07.morris.model;

import tit07.morris.exception.IllegalPositionException;


/**
 * Diese Klasse repräsentiert das Spielbrett. Das Spielbrett verwaltet die
 * Spielsteine, welche von oben links nach unten rechts von 1 - 24
 * durchnummeriert sind
 * <p>
 * <img src="morris_number.jpg" alt="Spielbrett">
 * <p>
 * Das Spielbrett implementiert keine Spielregeln. Daher können die Steine
 * beliebig gesetzt werden.
 */
public class Border implements Cloneable {

    /** Spielsteine, aus welchem das Spielfeld besteht */
    private Stone[] stone = new Stone[ 24 ];


    /**
     * Erzeugt ein leeres Spielbrett
     */
    public Border() {

        /* Steine erzeugen */
        for( int i = 0; i < stone.length; i++ ) {
            this.stone[i] = new Stone();
        }

        /* Steine initialisieren */
        this.initStones();
    }

    /**
     * Setzt das Spielbrett in den Anfangszustand zurück
     */
    public void resetBoard() {

        for( int i = 0; i < stone.length; i++ ) {
            this.stone[i].setColor( StoneColor.NONE );
        }
    }

    /**
     * Gibt die Anzahl der Spielsteine eines Spielers
     * 
     * @param color Spielsteinfarbe für welchen die Steine gezählt werden sollen
     * @return Die Anzahl der Steine des Spielers auf dem Spielbrett
     */
    public int getNumberOfStones( StoneColor color ) {

        int numberOfStones = 0;
        for( int i = 0; i < stone.length; i++ ) {
            if( stone[i].getColor() == color ) {
                numberOfStones++;
            }
        }
        return numberOfStones;
    }

    /**
     * Setzt einen neuen Besitzer für einen Stein auf dem Spielbrett. Der neue
     * Besitzer wird nur bei einer übergebenen Instanz von StoneColor gesetzt.
     * 
     * @param position Position des Steins auf dem Spielbrett (1-24)
     * @param color Neue Spielsteinfarbe des Steins
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public void setStoneColor( int position, StoneColor color )
                                                               throws IllegalPositionException {

        /* Überprüfe übergebene Position */
        isPositionValid( position );

        /* Setze neuen Besitzer */
        stone[position - 1].setColor( color );
    }

    /**
     * Abfrage des Besitzers eines Spielsteins auf dem Spielbrett.
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return Spielsteinfarbe des Spielsteins
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public StoneColor getStoneColor( int position )
                                                   throws IllegalPositionException {

        /* Überprüfe übergebene Position */
        isPositionValid( position );

        /* Gebe Spielsteinbesitzer zurück */
        return stone[position - 1].getColor();
    }

    /**
     * Abfrage, ob Stein in einer Mühle ist.
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return True, wenn sich der Stein in einer Mühle befindet
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public boolean isStoneInMill( int position )
                                                throws IllegalPositionException {

        /* Überprüfe übergebene Position */
        isPositionValid( position );

        /* Abfrage, ob Stein in einer Mühle ist */
        return stone[position - 1].isInMorris();
    }

    /**
     * Abfrage, ob man den Stein auf ein Nachbarfeld bewegen kann
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return True, wenn man den Stein auf ein Nachbarfeld bewegen kann
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public boolean isMovePossible( int position )
                                                 throws IllegalPositionException {

        /* Überprüfe übergebene Position */
        isPositionValid( position );

        /* Abfrage, ob man den Stein auf ein Nachbarfeld bewegen kann */
        return stone[position - 1].isMovePossible();
    }

    /**
     * Abfrage, ob zwei Steine benachbart sind
     * 
     * @param position1 Position des ersten Steins (1-24)
     * @param position2 Position des zweiten Steins (1-24)
     * @return True wenn die beiden Steine benachbart sind
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    public boolean isNeighbour( int position1, int position2 )
                                                              throws IllegalPositionException {

        /* Überprüfe übergebene Position */
        isPositionValid( position1 );
        isPositionValid( position2 );

        return ( stone[position1 - 1].isNeighbour( stone[position2 - 1] ) && stone[position2 - 1]
                                                                                                 .isNeighbour( stone[position1 - 1] ) );
    }

    /**
     * Gibt die Anzahl der möglichen Bewegungen für einen Spieler
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der möglichen Bewegungen
     */
    public int getNumberOfPossibleMoves( StoneColor color ) {

        int numberOfPossibleMoves = 0;

        /* Bewegungsmöglichkeiten werden nur für schwarz und weiß gezählt */
        if( color != StoneColor.WHITE && color != StoneColor.BLACK ) {
            return 0;
        }

        for( int i = 0; i < stone.length; i++ ) {
            if( stone[i].getColor() == color ) {
                if( stone[i].getNorthNeighbour() instanceof Stone
                        && stone[i].getNorthNeighbour().getColor() == StoneColor.NONE ) {
                    numberOfPossibleMoves++;
                }
                if( stone[i].getSouthNeighbour() instanceof Stone
                        && stone[i].getSouthNeighbour().getColor() == StoneColor.NONE ) {
                    numberOfPossibleMoves++;
                }
                if( stone[i].getEastNeighbour() instanceof Stone
                        && stone[i].getEastNeighbour().getColor() == StoneColor.NONE ) {
                    numberOfPossibleMoves++;
                }
                if( stone[i].getWestNeighbour() instanceof Stone
                        && stone[i].getWestNeighbour().getColor() == StoneColor.NONE ) {
                    numberOfPossibleMoves++;
                }
            }
        }
        return numberOfPossibleMoves;
    }

    /**
     * Gibt die Anzahl der potentiellen Bewegungsmöglichkeiten zurück
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der potentiellen Bewegungen (wenn alle sonstige Felder
     *         frei wären)
     */
    public int getNumberOfPotentialMoves( StoneColor color ) {

        int numberOfPotentialMoves = 0;

        /* Bewegungsmöglichkeiten werden nur für schwarz und weiß gezählt */
        if( color != StoneColor.WHITE && color != StoneColor.BLACK ) {
            return 0;
        }

        for( int i = 0; i < stone.length; i++ ) {
            if( stone[i].getColor() == color ) {
                numberOfPotentialMoves += stone[i].getNumberOfNeighbours();
            }
        }
        return numberOfPotentialMoves;
    }

    /**
     * Gibt die Anzahl der Steine, die nicht in einer Mühle stehen
     * 
     * @param color Spielsteinfarbe, für welchen die freien Steine gezählt
     *            werden sollen
     * @return Anzahl der Steine des Spielers, welche nicht in einer Mühle
     *         stehen
     */
    public int getNumberOfFreeStones( StoneColor color ) {

        int numberOfFreeStones = 0;

        if( color == StoneColor.WHITE || color == StoneColor.BLACK ) {
            for( int i = 1; i <= 24; i++ ) {
                try {
                    if( this.getStoneColor( i ) == color
                            && !this.isStoneInMill( i ) ) {
                        numberOfFreeStones++;
                    }
                }
                catch( IllegalPositionException e ) {
                }
            }
        }
        return numberOfFreeStones;
    }

    /**
     * Erstellt eine Kopie des Spielbretts
     * 
     * @return Kopie des Spielbretts
     */
    @Override
    public Border clone() {

        Border duplicate = new Border();
        for( int i = 1; i <= 24; i++ ) {
            try {
                duplicate.setStoneColor( i, this.getStoneColor( i ) );
            }
            catch( IllegalPositionException e ) {
            }
        }
        return duplicate;
    }

    /**
     * Überprüft, ob die übergebene Position gültig ist
     * 
     * @param position Zu prüfende Position
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    private static void isPositionValid( int position )
                                                       throws IllegalPositionException {

        if( position < 1 || position > 24 ) {
            throw new IllegalPositionException( "IllegalPosition: " + position ); //$NON-NLS-1$
        }
    }

    /**
     * Verknüpft die Steine gegenseitig durch Referenzen
     */
    private void initStones() {

        /* Stein 1 */
        stone[0].setNorthNeighbour( null );
        stone[0].setSouthNeighbour( stone[9] );
        stone[0].setEastNeighbour( stone[1] );
        stone[0].setWestNeighbour( null );
        stone[0].setMorrisOneStoneOne( stone[1] );
        stone[0].setMorrisOneStoneTwo( stone[2] );
        stone[0].setMorrisTwoStoneOne( stone[9] );
        stone[0].setMorrisTwoStoneTwo( stone[21] );

        /* Stein 2 */
        stone[1].setNorthNeighbour( null );
        stone[1].setSouthNeighbour( stone[4] );
        stone[1].setEastNeighbour( stone[2] );
        stone[1].setWestNeighbour( stone[0] );
        stone[1].setMorrisOneStoneOne( stone[0] );
        stone[1].setMorrisOneStoneTwo( stone[2] );
        stone[1].setMorrisTwoStoneOne( stone[4] );
        stone[1].setMorrisTwoStoneTwo( stone[7] );

        /* Stein 3 */
        stone[2].setNorthNeighbour( null );
        stone[2].setSouthNeighbour( stone[14] );
        stone[2].setEastNeighbour( null );
        stone[2].setWestNeighbour( stone[1] );
        stone[2].setMorrisOneStoneOne( stone[0] );
        stone[2].setMorrisOneStoneTwo( stone[1] );
        stone[2].setMorrisTwoStoneOne( stone[14] );
        stone[2].setMorrisTwoStoneTwo( stone[23] );

        /* Stein 4 */
        stone[3].setNorthNeighbour( null );
        stone[3].setSouthNeighbour( stone[10] );
        stone[3].setEastNeighbour( stone[4] );
        stone[3].setWestNeighbour( null );
        stone[3].setMorrisOneStoneOne( stone[4] );
        stone[3].setMorrisOneStoneTwo( stone[5] );
        stone[3].setMorrisTwoStoneOne( stone[10] );
        stone[3].setMorrisTwoStoneTwo( stone[18] );

        /* Stein 5 */
        stone[4].setNorthNeighbour( stone[1] );
        stone[4].setSouthNeighbour( stone[7] );
        stone[4].setEastNeighbour( stone[5] );
        stone[4].setWestNeighbour( stone[3] );
        stone[4].setMorrisOneStoneOne( stone[3] );
        stone[4].setMorrisOneStoneTwo( stone[5] );
        stone[4].setMorrisTwoStoneOne( stone[1] );
        stone[4].setMorrisTwoStoneTwo( stone[7] );

        /* Stein 6 */
        stone[5].setNorthNeighbour( null );
        stone[5].setSouthNeighbour( stone[13] );
        stone[5].setEastNeighbour( null );
        stone[5].setWestNeighbour( stone[4] );
        stone[5].setMorrisOneStoneOne( stone[3] );
        stone[5].setMorrisOneStoneTwo( stone[4] );
        stone[5].setMorrisTwoStoneOne( stone[13] );
        stone[5].setMorrisTwoStoneTwo( stone[20] );

        /* Stein 7 */
        stone[6].setNorthNeighbour( null );
        stone[6].setSouthNeighbour( stone[11] );
        stone[6].setEastNeighbour( stone[7] );
        stone[6].setWestNeighbour( null );
        stone[6].setMorrisOneStoneOne( stone[7] );
        stone[6].setMorrisOneStoneTwo( stone[8] );
        stone[6].setMorrisTwoStoneOne( stone[11] );
        stone[6].setMorrisTwoStoneTwo( stone[15] );

        /* Stein 8 */
        stone[7].setNorthNeighbour( stone[4] );
        stone[7].setSouthNeighbour( null );
        stone[7].setEastNeighbour( stone[8] );
        stone[7].setWestNeighbour( stone[6] );
        stone[7].setMorrisOneStoneOne( stone[1] );
        stone[7].setMorrisOneStoneTwo( stone[4] );
        stone[7].setMorrisTwoStoneOne( stone[6] );
        stone[7].setMorrisTwoStoneTwo( stone[8] );

        /* Stein 9 */
        stone[8].setNorthNeighbour( null );
        stone[8].setSouthNeighbour( stone[12] );
        stone[8].setEastNeighbour( null );
        stone[8].setWestNeighbour( stone[7] );
        stone[8].setMorrisOneStoneOne( stone[6] );
        stone[8].setMorrisOneStoneTwo( stone[7] );
        stone[8].setMorrisTwoStoneOne( stone[12] );
        stone[8].setMorrisTwoStoneTwo( stone[17] );

        /* Stein 10 */
        stone[9].setNorthNeighbour( stone[0] );
        stone[9].setSouthNeighbour( stone[21] );
        stone[9].setEastNeighbour( stone[10] );
        stone[9].setWestNeighbour( null );
        stone[9].setMorrisOneStoneOne( stone[10] );
        stone[9].setMorrisOneStoneTwo( stone[11] );
        stone[9].setMorrisTwoStoneOne( stone[0] );
        stone[9].setMorrisTwoStoneTwo( stone[21] );

        /* Stein 11 */
        stone[10].setNorthNeighbour( stone[3] );
        stone[10].setSouthNeighbour( stone[18] );
        stone[10].setEastNeighbour( stone[11] );
        stone[10].setWestNeighbour( stone[9] );
        stone[10].setMorrisOneStoneOne( stone[9] );
        stone[10].setMorrisOneStoneTwo( stone[11] );
        stone[10].setMorrisTwoStoneOne( stone[3] );
        stone[10].setMorrisTwoStoneTwo( stone[18] );

        /* Stein 12 */
        stone[11].setNorthNeighbour( stone[6] );
        stone[11].setSouthNeighbour( stone[15] );
        stone[11].setEastNeighbour( null );
        stone[11].setWestNeighbour( stone[10] );
        stone[11].setMorrisOneStoneOne( stone[9] );
        stone[11].setMorrisOneStoneTwo( stone[10] );
        stone[11].setMorrisTwoStoneOne( stone[6] );
        stone[11].setMorrisTwoStoneTwo( stone[15] );

        /* Stein 13 */
        stone[12].setNorthNeighbour( stone[8] );
        stone[12].setSouthNeighbour( stone[17] );
        stone[12].setEastNeighbour( stone[13] );
        stone[12].setWestNeighbour( null );
        stone[12].setMorrisOneStoneOne( stone[13] );
        stone[12].setMorrisOneStoneTwo( stone[14] );
        stone[12].setMorrisTwoStoneOne( stone[8] );
        stone[12].setMorrisTwoStoneTwo( stone[17] );

        /* Stein 14 */
        stone[13].setNorthNeighbour( stone[5] );
        stone[13].setSouthNeighbour( stone[20] );
        stone[13].setEastNeighbour( stone[14] );
        stone[13].setWestNeighbour( stone[12] );
        stone[13].setMorrisOneStoneOne( stone[12] );
        stone[13].setMorrisOneStoneTwo( stone[14] );
        stone[13].setMorrisTwoStoneOne( stone[5] );
        stone[13].setMorrisTwoStoneTwo( stone[20] );

        /* Stein 15 */
        stone[14].setNorthNeighbour( stone[2] );
        stone[14].setSouthNeighbour( stone[23] );
        stone[14].setEastNeighbour( null );
        stone[14].setWestNeighbour( stone[13] );
        stone[14].setMorrisOneStoneOne( stone[12] );
        stone[14].setMorrisOneStoneTwo( stone[13] );
        stone[14].setMorrisTwoStoneOne( stone[2] );
        stone[14].setMorrisTwoStoneTwo( stone[23] );

        /* Stein 16 */
        stone[15].setNorthNeighbour( stone[11] );
        stone[15].setSouthNeighbour( null );
        stone[15].setEastNeighbour( stone[16] );
        stone[15].setWestNeighbour( null );
        stone[15].setMorrisOneStoneOne( stone[16] );
        stone[15].setMorrisOneStoneTwo( stone[17] );
        stone[15].setMorrisTwoStoneOne( stone[6] );
        stone[15].setMorrisTwoStoneTwo( stone[11] );

        /* Stein 17 */
        stone[16].setNorthNeighbour( null );
        stone[16].setSouthNeighbour( stone[19] );
        stone[16].setEastNeighbour( stone[17] );
        stone[16].setWestNeighbour( stone[15] );
        stone[16].setMorrisOneStoneOne( stone[15] );
        stone[16].setMorrisOneStoneTwo( stone[17] );
        stone[16].setMorrisTwoStoneOne( stone[19] );
        stone[16].setMorrisTwoStoneTwo( stone[22] );

        /* Stein 18 */
        stone[17].setNorthNeighbour( stone[12] );
        stone[17].setSouthNeighbour( null );
        stone[17].setEastNeighbour( null );
        stone[17].setWestNeighbour( stone[16] );
        stone[17].setMorrisOneStoneOne( stone[15] );
        stone[17].setMorrisOneStoneTwo( stone[16] );
        stone[17].setMorrisTwoStoneOne( stone[8] );
        stone[17].setMorrisTwoStoneTwo( stone[12] );

        /* Stein 19 */
        stone[18].setNorthNeighbour( stone[10] );
        stone[18].setSouthNeighbour( null );
        stone[18].setEastNeighbour( stone[19] );
        stone[18].setWestNeighbour( null );
        stone[18].setMorrisOneStoneOne( stone[19] );
        stone[18].setMorrisOneStoneTwo( stone[20] );
        stone[18].setMorrisTwoStoneOne( stone[3] );
        stone[18].setMorrisTwoStoneTwo( stone[10] );

        /* Stein 20 */
        stone[19].setNorthNeighbour( stone[16] );
        stone[19].setSouthNeighbour( stone[22] );
        stone[19].setEastNeighbour( stone[20] );
        stone[19].setWestNeighbour( stone[18] );
        stone[19].setMorrisOneStoneOne( stone[18] );
        stone[19].setMorrisOneStoneTwo( stone[20] );
        stone[19].setMorrisTwoStoneOne( stone[16] );
        stone[19].setMorrisTwoStoneTwo( stone[22] );

        /* Stein 21 */
        stone[20].setNorthNeighbour( stone[13] );
        stone[20].setSouthNeighbour( null );
        stone[20].setEastNeighbour( null );
        stone[20].setWestNeighbour( stone[19] );
        stone[20].setMorrisOneStoneOne( stone[18] );
        stone[20].setMorrisOneStoneTwo( stone[19] );
        stone[20].setMorrisTwoStoneOne( stone[5] );
        stone[20].setMorrisTwoStoneTwo( stone[13] );

        /* Stein 22 */
        stone[21].setNorthNeighbour( stone[9] );
        stone[21].setSouthNeighbour( null );
        stone[21].setEastNeighbour( stone[22] );
        stone[21].setWestNeighbour( null );
        stone[21].setMorrisOneStoneOne( stone[22] );
        stone[21].setMorrisOneStoneTwo( stone[23] );
        stone[21].setMorrisTwoStoneOne( stone[0] );
        stone[21].setMorrisTwoStoneTwo( stone[9] );

        /* Stein 23 */
        stone[22].setNorthNeighbour( stone[19] );
        stone[22].setSouthNeighbour( null );
        stone[22].setEastNeighbour( stone[23] );
        stone[22].setWestNeighbour( stone[21] );
        stone[22].setMorrisOneStoneOne( stone[21] );
        stone[22].setMorrisOneStoneTwo( stone[23] );
        stone[22].setMorrisTwoStoneOne( stone[16] );
        stone[22].setMorrisTwoStoneTwo( stone[19] );

        /* Stein 24 */
        stone[23].setNorthNeighbour( stone[14] );
        stone[23].setSouthNeighbour( null );
        stone[23].setEastNeighbour( null );
        stone[23].setWestNeighbour( stone[22] );
        stone[23].setMorrisOneStoneOne( stone[21] );
        stone[23].setMorrisOneStoneTwo( stone[22] );
        stone[23].setMorrisTwoStoneOne( stone[2] );
        stone[23].setMorrisTwoStoneTwo( stone[14] );
    }
}
