package tit07.morris.model;

/**
 * Die Klasse Stone implementiert einen Spielstein. Der Stein hat eine Farbe vom
 * Typ StoneColor, sowie Referenzen auf Nachbarsteine und Steine, welche mit dem
 * Spielstein eine M�hle bilden. Dadurch kann abgefragt werden, ob sich der
 * Stein in einer M�hle befindet und ob es Bewegungsm�glichkeiten auf
 * Nachbarfelder f�r ihn gibt.
 */
public class Stone {

    /** Besitzer des Spielsteins */
    private StoneColor color;

    /** Nachbarstein oben */
    private Stone      northNeighbour;

    /** Nachbarstein unten */
    private Stone      southNeighbour;

    /** Nachbarstein rechts */
    private Stone      eastNeighbour;

    /** Nachbarstein links */
    private Stone      westNeighbour;

    /** Erster Stein in horizontaler M�hle */
    private Stone      morrisHorizontalStoneOne;

    /** Zweiter Stein in horizontaler M�hle */
    private Stone      morrisHorizontalStoneTwo;

    /** Erster Steine in vertikaler M�hle */
    private Stone      morrisVerticalStoneOne;

    /** Zweiter Steine in vertikaler M�hle */
    private Stone      morrisVerticalStoneTwo;


    /**
     * Erzeugt einen neuen Spielstein ohne Farbe und ohne Referenzen auf andere
     * Spielsteine.
     */
    public Stone() {

        /* Setze Besitzer */
        this.color = StoneColor.NONE;

        /* Setze Nachbarsteine */
        this.northNeighbour = null;
        this.southNeighbour = null;
        this.eastNeighbour = null;
        this.westNeighbour = null;

        /* Setze M�hlensteine */
        this.morrisHorizontalStoneOne = null;
        this.morrisHorizontalStoneTwo = null;
        this.morrisVerticalStoneOne = null;
        this.morrisVerticalStoneTwo = null;
    }

    /**
     * Abfrage, ob sich der Stein in einer M�hle befindet.
     * 
     * @return True, wenn sich der Spielstein in einer M�hle befindet.
     */
    public boolean isInMorris() {

        /* Abfrage, ob wei� oder schwarz ist (notwenige Bedingung) */
        if( color == StoneColor.WHITE || color == StoneColor.BLACK ) {

            /* �berpr�fe, ob Stein in horizontaler M�hle */
            if( morrisHorizontalStoneOne instanceof Stone
                    && morrisHorizontalStoneTwo instanceof Stone ) {
                if( color == morrisHorizontalStoneOne.getColor()
                        && color == morrisHorizontalStoneTwo.getColor() ) {
                    return true;
                }
            }

            /* �berpr�fe, ob Stein in vertikaler M�hle */
            if( morrisVerticalStoneOne instanceof Stone
                    && morrisVerticalStoneTwo instanceof Stone ) {
                if( color == morrisVerticalStoneOne.getColor()
                        && color == morrisVerticalStoneTwo.getColor() ) {
                    return true;
                }
            }
        }
        /* Stein befindet sich nicht in einer M�hle */
        return false;
    }

    /**
     * Abfrage, ob der Stein auf ein Nachbarfeld bewegt werden kann.
     * 
     * @return True, wenn der Spielstein auf ein Nachbarfeld bewegt werden kann.
     */
    public boolean isMovePossible() {

        /* Abfrage, ob wei� oder schwarz ist (notwenige Bedingung) */
        if( color == StoneColor.WHITE || color == StoneColor.BLACK ) {

            /* Abfrage, ob Bewegung nach oben m�glich ist */
            if( northNeighbour instanceof Stone
                    && northNeighbour.getColor() == StoneColor.NONE ) {
                return true;
            }

            /* Abfrage, ob Bewegung nach unten m�glich ist */
            if( southNeighbour instanceof Stone
                    && southNeighbour.getColor() == StoneColor.NONE ) {
                return true;
            }

            /* Abfrage, ob Bewegung nach rechts m�glich ist */
            if( eastNeighbour instanceof Stone
                    && eastNeighbour.getColor() == StoneColor.NONE ) {
                return true;
            }

            /* Abfrage, ob Bewegung nach links m�glich ist */
            if( westNeighbour instanceof Stone
                    && westNeighbour.getColor() == StoneColor.NONE ) {
                return true;
            }
        }
        /* Spielstein kann nicht auf ein Nachbarfeld bewegt werden */
        return false;
    }

    /**
     * Abfrage, ob der �bergebene Stein ein Nachbarstein ist
     * 
     * @param stone Der zu �berpr�fende Stein
     * @return True, wenn der Stein ein Nachbarstein ist
     */
    public boolean isNeighbour( Stone stone ) {

        if( stone == null ) {
            return false;
        }
        return ( stone == northNeighbour
                || stone == southNeighbour
                || stone == eastNeighbour || stone == westNeighbour );
    }

    /**
     * Gibt die Anzahl der Nachbarsteine zur�ck
     * 
     * @return Anzahl der Nachbarsteine
     */
    public int getNumberOfNeighbours() {

        int numberOfNeighbours = 0;

        if( northNeighbour instanceof Stone ) {
            numberOfNeighbours++;
        }
        if( southNeighbour instanceof Stone ) {
            numberOfNeighbours++;
        }
        if( eastNeighbour instanceof Stone ) {
            numberOfNeighbours++;
        }
        if( westNeighbour instanceof Stone ) {
            numberOfNeighbours++;
        }
        return numberOfNeighbours;
    }

    /**
     * Abfrage der Spielsteinfarbe.
     * 
     * @return Farbe des Spielsteins
     */
    public StoneColor getColor() {

        return color;
    }

    /**
     * Setzen der Spielsteinfarbe. Die neue Farbe wird nur bei g�ltiger Instanz
     * des Parameters gesetzt.
     * 
     * @param color Neuer Besitzer des Spielsteins
     */
    public void setColor( StoneColor color ) {

        if( color instanceof StoneColor ) {
            this.color = color;
        }
    }

    /**
     * Gibt die Referenz auf den oberen Nachbarstein zur�ck
     * 
     * @return Oberer Nachbarstein
     */
    public Stone getNorthNeighbour() {

        return northNeighbour;
    }

    /**
     * Setzt die Referenz auf den oberen Nachbarstein
     * 
     * @param northNeighbour Oberer Nachbarstein
     */
    public void setNorthNeighbour( Stone northNeighbour ) {

        this.northNeighbour = northNeighbour;
    }

    /**
     * Gibt die Referenz auf den unteren Nachbarstein zur�ck
     * 
     * @return Unterer Nachbarstein
     */
    public Stone getSouthNeighbour() {

        return southNeighbour;
    }

    /**
     * Setzt die Referenz auf den unteren Nachbarstein
     * 
     * @param southNeighbour Unterer Nachbarstein
     */
    public void setSouthNeighbour( Stone southNeighbour ) {

        this.southNeighbour = southNeighbour;
    }

    /**
     * Gibt die Referenz auf den rechten Nachbarstein zur�ck
     * 
     * @return Rechter Nachbarstein
     */
    public Stone getEastNeighbour() {

        return eastNeighbour;
    }

    /**
     * Setzt die Referenz auf den rechten Nachbarstein
     * 
     * @param eastNeighbour Rechter Nachbarstein
     */
    public void setEastNeighbour( Stone eastNeighbour ) {

        this.eastNeighbour = eastNeighbour;
    }

    /**
     * Gibt die Referenz auf den linken Nachbarstein zur�ck
     * 
     * @return Linker Nachbarstein
     */
    public Stone getWestNeighbour() {

        return westNeighbour;
    }

    /**
     * Setzt die Referenz auf den linken Nachbarstein
     * 
     * @param westNeighbour Linker Nachbarstein
     */
    public void setWestNeighbour( Stone westNeighbour ) {

        this.westNeighbour = westNeighbour;
    }

    /**
     * Gibt die Referenz auf den ersten Stein der horizontalen M�hle zur�ck
     * 
     * @return Ersten Stein der horizontalen M�hle
     */
    public Stone getMorrisOneStoneOne() {

        return morrisHorizontalStoneOne;
    }

    /**
     * Setzt die Referenz auf den ersten Stein der horizontablen M�hle
     * 
     * @param morrisOneStoneOne Erster Stein der horizontablen M�hle
     */
    public void setMorrisOneStoneOne( Stone morrisOneStoneOne ) {

        this.morrisHorizontalStoneOne = morrisOneStoneOne;
    }

    /**
     * Gibt die Referenz auf den zweiten Stein der horizontalen M�hle zur�ck
     * 
     * @return Zweiter Stein der horizontalen M�hle
     */
    public Stone getMorrisOneStoneTwo() {

        return morrisHorizontalStoneTwo;
    }

    /**
     * Setzt die Referenz auf den zweiten Stein der horizontablen M�hle
     * 
     * @param morrisOneStoneTwo Zweiter Stein der horizontablen M�hle
     */
    public void setMorrisOneStoneTwo( Stone morrisOneStoneTwo ) {

        this.morrisHorizontalStoneTwo = morrisOneStoneTwo;
    }

    /**
     * Gibt die Referenz auf den ersten Stein der vertikalen M�hle zur�ck
     * 
     * @return Ersten Stein der vertikalen M�hle
     */
    public Stone getMorrisTwoStoneOne() {

        return morrisVerticalStoneOne;
    }

    /**
     * Setzt die Referenz auf den ersten Stein der vertikalen M�hle
     * 
     * @param morrisTwoStoneOne Erster Stein der vertikalen M�hle
     */
    public void setMorrisTwoStoneOne( Stone morrisTwoStoneOne ) {

        this.morrisVerticalStoneOne = morrisTwoStoneOne;
    }

    /**
     * Gibt die Referenz auf den zweiten Stein der vertikalen M�hle zur�ck
     * 
     * @return Zweiter Stein der vertikalen M�hle
     */
    public Stone getMorrisTwoStoneTwo() {

        return morrisVerticalStoneTwo;
    }

    /**
     * Setzt die Referenz auf den zweiten Stein der vertikalen M�hle
     * 
     * @param morrisTwoStoneTwo Zweiter Stein der vertikalen M�hle
     */
    public void setMorrisTwoStoneTwo( Stone morrisTwoStoneTwo ) {

        this.morrisVerticalStoneTwo = morrisTwoStoneTwo;
    }
}
