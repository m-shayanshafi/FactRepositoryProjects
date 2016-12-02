package tit07.morris.view;

import java.awt.Graphics;
import javax.swing.JComponent;
import tit07.morris.exception.IllegalPositionException;
import tit07.morris.model.ModelViewable;
import tit07.morris.model.StoneColor;
import tit07.morris.view.extra.Animateable;
import tit07.morris.view.extra.AnimationEngine;


/**
 * Die Klasse GameWindow stellt das Spielfeld grafisch dar.
 */
public class GameWindow extends JComponent implements Animateable {

    /** Referenz auf das Model */
    private ModelViewable   morris;

    /** Referenz für die Animation */
    private AnimationEngine animationEngine;

    /** Abstand zwischen Fenster und Spielbrett */
    private int             offsetX       = 100;

    private int             offsetY       = 20;

    /** Geometrische Spielfeldpositionen */
    private int             position[][]  = new int[ 24 ][ 8 ];

    /**
     * Steine außerhalb des Spielfeldes: 1. Dimension: Position neben dem
     * Spielbrett (oben/unten - rechts/links) 2. Dimension: X-Position des 3x3
     * Blocks 3. Dimension: Y-Position des 3x3 Blocks 4. Dimension: Attribute
     * wie Sichtbarkeit, geometrische Positionen...
     */
    private int[][][][]     stonesOutside = new int[ 4 ][ 3 ][ 3 ][ 8 ];

    /** Index: Ist der Stein versteckt */
    private final int       HIDE          = 0;

    /** Index: Absolute X-Position des Steins */
    private final int       ABS_X         = HIDE + 1;

    /** Index: Absolute Y-Position des Steins */
    private final int       ABS_Y         = ABS_X + 1;

    /** Index: Minimale X-Position des Steins */
    private final int       MIN_X         = ABS_Y + 1;

    /** Index: Maximale X-Position des Steins */
    private final int       MAX_X         = MIN_X + 1;

    /** Index: Minimale Y-Position des Steins */
    private final int       MIN_Y         = MAX_X + 1;

    /** Index: Maximale Y-Position des Steins */
    private final int       MAX_Y         = MIN_Y + 1;

    /** Index: Ist der Stein markiert */
    private final int       MARK          = MAX_Y + 1;


    /**
     * Erzeugt eine neue Visualisierung des Spielfeldes
     * 
     * @param morris Referenz auf das Model
     * @param input Referenz auf den Controller
     */
    public GameWindow( ModelViewable morris, Reactable input ) {

        super();
        this.setDoubleBuffered( true );
        this.addMouseListener( input );
        this.morris = morris;
        this.animationEngine = new AnimationEngine( this,
                                                    morris );
        this.resetGame();
    }

    /**
     * Gibt die Referenz auf die Animationsklasse zurück
     * 
     * @return Referenz auf die Animationsklasse
     */
    public AnimationEngine getAnimationEngine() {

        return animationEngine;
    }

    /**
     * Wandelt die übergebenen 2D-Koordinaten in die entsprechende Position
     * (1-24) auf dem Spielfeld um. Gibt 0 zurück, wenn keine Übereinstimmung
     * vorliegt.
     * 
     * @param xCoord Position in X-Richtung
     * @param yCoord Position in Y-Richtung
     * @return Spielfeldposition der übergebenen Koordinaten
     */
    public int coordiantes2Position( int xCoord, int yCoord ) {

        for( int i = 0; i < position.length; i++ ) {
            if( xCoord >= position[i][MIN_X]
                    && xCoord <= position[i][MAX_X]
                    && yCoord >= position[i][MIN_Y]
                    && yCoord <= position[i][MAX_Y] ) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * Gibt für eine Spielfeldposition die zugehörigen Koordinaten. Die
     * Spielfeldposition beinhaltet auch die Position der Steine außerhalb des
     * Spielbretts.
     * 
     * @param position Position (1-28) auf dem Spielfeld
     * @return Koordinaten der Position
     * @throws IllegalPositionException Wird bei ungültiger Position geworfen
     */
    public int[] position2Coords( int position )
                                                throws IllegalPositionException {

        int[] coords = new int[ 2 ];
        if( position >= 1 && position <= 24 ) {
            coords[0] = this.position[position - 1][ABS_X];
            coords[1] = this.position[position - 1][ABS_Y];
            return coords;
        }
        /* Gibt den nächsten sichtbaren zu setzenden weißen Stein */
        else if( position == WHITE_SET_POSITION ) {
            for( int x = 2; x >= 0; x-- ) {
                for( int y = 2; y >= 0; y-- ) {
                    if( stonesOutside[0][x][y][HIDE] == 0 ) {
                        stonesOutside[0][x][y][HIDE] = 1;
                        coords[0] = stonesOutside[0][x][y][ABS_X];
                        coords[1] = stonesOutside[0][x][y][ABS_Y];
                        return coords;
                    }
                }
            }
        }
        /* Gibt den nächsten sichtbaren zu setzenden schwarzen Stein */
        else if( position == BLACK_SET_POSITION ) {
            for( int x = 0; x < 3; x++ ) {
                for( int y = 0; y < 3; y++ ) {
                    if( stonesOutside[3][x][y][HIDE] == 0 ) {
                        stonesOutside[3][x][y][HIDE] = 1;
                        coords[0] = stonesOutside[3][x][y][ABS_X];
                        coords[1] = stonesOutside[3][x][y][ABS_Y];
                        return coords;
                    }
                }
            }
        }
        /* Gibt den nächsten unsichtbaren geschlagenen weißen Stein */
        else if( position == WHITE_REMOVE_POSITION ) {
            for( int x = 2; x >= 0; x-- ) {
                for( int y = 0; y < 3; y++ ) {
                    if( stonesOutside[1][x][y][HIDE] == 1 ) {
                        coords[0] = stonesOutside[1][x][y][ABS_X];
                        coords[1] = stonesOutside[1][x][y][ABS_Y];
                        return coords;
                    }
                }
            }

        }
        /* Gibt den nächsten unsichtbaren geschlagenen schwarzen Stein */
        else if( position == BLACK_REMOVE_POSITION ) {
            for( int x = 0; x < 3; x++ ) {
                for( int y = 2; y >= 0; y-- ) {
                    if( stonesOutside[2][x][y][HIDE] == 1 ) {
                        coords[0] = stonesOutside[2][x][y][ABS_X];
                        coords[1] = stonesOutside[2][x][y][ABS_Y];
                        return coords;
                    }
                }
            }
        }
        throw new IllegalPositionException( "GameWindow: Keine Umwandlung möglich " //$NON-NLS-1$
                + position );
    }

    /**
     * Gibt den Durchmesser des Spielsteins in Pixel zurück
     * 
     * @return Durchmesser des Spielsteins in Pixel
     */
    public int getDiameter() {

        return calculateBoardSize() / 15;
    }

    /**
     * Versteckt einen Stein auf der übergebenen Position
     * 
     * @param isHidden True, wenn der Stein versteckt werden soll, false für
     *            Sichtbarkeit
     * @param position Position des Steins auf dem Spielfeld (1-24)
     * @throws IllegalPositionException Wird bei ungültiger Position geworfen
     */
    public void hidePosition( boolean isHidden, int position )
                                                              throws IllegalPositionException {

        if( position < 1 || position > 24 ) {
            throw new IllegalPositionException( "GameWindow: Falsche Position " //$NON-NLS-1$
                    + position );
        }
        if( isHidden ) {
            this.position[position - 1][HIDE] = 1;
        }
        else {
            this.position[position - 1][HIDE] = 0;
        }
    }

    /**
     * Macht alle Steine wieder sichtbar
     */
    public void unhideAllStones() {

        for( int i = 0; i < position.length; i++ ) {
            this.position[i][HIDE] = 0;
        }
    }

    /**
     * Markiert eine Position auf dem Spielfeld
     * 
     * @param isMarked Bei true wird die Position markiert, bei false aufgelöst.
     * @param position Die Position auf dem Spielbrett
     * @throws IllegalPositionException Wird bei ungültiger Position geworfen
     */
    public void markPosition( boolean isMarked, int position )
                                                              throws IllegalPositionException {

        if( position < 1 || position > 24 ) {
            throw new IllegalPositionException( "GameWindow: Falsche Position" ); //$NON-NLS-1$
        }
        if( isMarked ) {
            this.position[position - 1][MARK] = 1;
        }
        else {
            this.position[position - 1][MARK] = 0;
        }
    }

    /**
     * Zeichnet die Spielfläche neu
     */
    @Override
    public void repaint() {

        update( getGraphics() );
    }

    /**
     * Räumt nach einer Animation auf, indem der Stein auf der Zielposition
     * wieder sichtbar gemacht wird
     * 
     * @param position Zielposition der Animation
     */
    public void disposeAnimation( int position ) {

        boolean goOut = false;
        /* Stein auf dem Spielfeld wieder sichtbar machen */
        if( position >= 1 && position <= 24 ) {
            try {
                this.hidePosition( false, position );
            }
            catch( IllegalPositionException e ) {
            }
        }
        /* Geschlagenen schwarzen Stein sichtbar machen */
        else if( position == BLACK_REMOVE_POSITION ) {
            for( int x = 0; x < 3; x++ ) {
                for( int y = 2; y >= 0; y-- ) {
                    if( stonesOutside[2][x][y][HIDE] == 1 ) {
                        stonesOutside[2][x][y][HIDE] = 0;
                        goOut = true;
                        break;
                    }
                }
                if( goOut ) {
                    break;
                }
            }
        }
        /* Geschlagenen weißen Stein sichtbar machen */
        else if( position == WHITE_REMOVE_POSITION ) {
            for( int x = 2; x >= 0; x-- ) {
                for( int y = 0; y < 3; y++ ) {
                    if( stonesOutside[1][x][y][HIDE] == 1 ) {
                        stonesOutside[1][x][y][HIDE] = 0;
                        goOut = true;
                        break;
                    }
                }
                if( goOut ) {
                    break;
                }
            }
        }
    }

    /**
     * Zeichnet das Spielfenster neu
     * 
     * @param g Grafik-Kontext, in welchen gezeichent werden soll
     */
    @Override
    public/* synchronized */void paintComponent( Graphics g ) {

        /* Zeichne Hintergrund des Spiels */
        g.setColor( morris.getConfig().getGameStyle().getBackgroundColor() );
        g
         .fillRect( this.getX(), this.getY(), this.getWidth(), this.getHeight() );

        /* Zeichne Spielfläche */
        paintGame( g );
    }

    /**
     * Setzt die Felder der Spielsteine in ihren Anfangszustand zurück.
     */
    public void resetGame() {

        /* Steine innerhalb des Spielfeldes */
        for( int i = 0; i < position.length; i++ ) {
            for( int j = 0; j < position[i].length; j++ ) {
                position[i][j] = 0;
            }
        }
        this.setStonesOutside( morris.getConfig().INIT_STONES_WHITE,
                               0,
                               morris.getConfig().INIT_STONES_WHITE,
                               0 );
    }

    /**
     * Setzt die Anzahl der Steine außerhalb des Spielfeldes sichtbar
     * 
     * @param whiteSet Anzahl der weißen zu setzenden Steine
     * @param whiteOut Anzahl der weißen geschlagenen Steine
     * @param blackSet Anzahl der schwarzen zu setzenden Steine
     * @param blackOut Anzahl der schwarzen geschlagenen Steine
     */
    public void setStonesOutside( int whiteSet,
                                  int whiteOut,
                                  int blackSet,
                                  int blackOut ) {

        /* Alle Steine verstecken */
        for( int i = 0; i < stonesOutside.length; i++ ) {
            for( int x = 0; x < stonesOutside[i].length; x++ ) {
                for( int y = 0; y < stonesOutside[i][x].length; y++ ) {
                    stonesOutside[i][x][y][HIDE] = 1;
                }
            }
        }

        /* Bestimmte Steine anzeigen */
        for( int i = 0; i < stonesOutside.length; i++ ) {
            int numberOfSets = 0;
            for( int x = 0; x < stonesOutside[i].length; x++ ) {
                for( int y = 0; y < stonesOutside[i][x].length; y++ ) {
                    if( ( i == 0 && numberOfSets < whiteSet )
                            || ( i == 1 && numberOfSets < whiteOut )
                            || ( i == 2 && numberOfSets < blackOut )
                            || ( i == 3 && numberOfSets < blackSet ) ) {
                        stonesOutside[i][x][y][HIDE] = 0;
                        numberOfSets++;
                    }
                }
            }
        }
    }

    /**
     * Berechnet die Größe des Spielbretts anhand der Fenstergröße
     * 
     * @return Größe des Spielbretts in Pixel
     */
    private int calculateBoardSize() {

        int wholeSize = 0;
        if( this.getWidth() < this.getHeight() ) {
            wholeSize = this.getWidth();
        }
        else {
            wholeSize = this.getHeight();
        }
        int outsideSpace = 8 * ( wholeSize / 23 );
        int boardSize = wholeSize - outsideSpace;
        this.offsetX = 4 * ( boardSize / 15 );
        this.offsetY = 4 * ( boardSize / 15 );
        return boardSize;
    }

    /**
     * Berechnet die Größe des Spielfeldes und zeichnet es neu
     * 
     * @param g Grafik-Kontext, in welchen gezeichent werden soll
     */
    private void paintGame( Graphics g ) {

        /* Skalierungsfaktor des Spielfeldes */
        int boardSize = calculateBoardSize();
        int factor = boardSize / 8;

        /* Zeichne Spielbrett */
        g.setColor( morris.getConfig().getGameStyle().getBoardColor() );
        g.fillRect( offsetX, offsetY, boardSize, boardSize );

        /* Zeichne Ringe */
        g.setColor( morris.getConfig().getGameStyle().getLineColor() );
        g.drawRect( offsetX + 1 * factor,
                    offsetY + 1 * factor,
                    6 * factor,
                    6 * factor );
        g.drawRect( offsetX + 2 * factor,
                    offsetY + 2 * factor,
                    4 * factor,
                    4 * factor );
        g.drawRect( offsetX + 3 * factor,
                    offsetY + 3 * factor,
                    2 * factor,
                    2 * factor );

        /* Zeichne Linien */
        g.drawLine( offsetX + 1 * factor, offsetY + 4 * factor, offsetX
                + 3
                * factor, offsetY + 4 * factor );
        g.drawLine( offsetX + 5 * factor, offsetY + 4 * factor, offsetX
                + 7
                * factor, offsetY + 4 * factor );
        g.drawLine( offsetX + 4 * factor, offsetY + 1 * factor, offsetX
                + 4
                * factor, offsetY + 3 * factor );
        g.drawLine( offsetX + 4 * factor, offsetY + 5 * factor, offsetX
                + 4
                * factor, offsetY + 7 * factor );

        /* Berechne Spielsteinpositionen innerhalb des Spielbretts neu */
        this.updatePositions( boardSize );

        /* Berechne Spielsteinposition außerhalb des Spielbretts neu */
        this.updateOutsidePositions( boardSize );

        /* Zeichne Spielsteine */
        this.paintStones( g, boardSize );

        /* Zeichne Spielsteine außerhalb des Spielfeldes */
        this.paintStonesOutside( g, boardSize );

        /* Zeichne animierten Stein */
        this.animationEngine.animate( g, boardSize );
    }

    /**
     * Zeichnet die Spielsteine auf dem Spielbrett
     * 
     * @param g Grafik-Kontext, in welchen gezeichent werden soll
     * @param boardSize Größe des Spielbretts
     */
    private void paintStones( Graphics g, int boardSize ) {

        /* Durchmesser eines Spielsteins */
        int diameter = boardSize / 15;

        for( int i = 0; i < position.length; i++ ) {
            StoneColor stoneColor = StoneColor.NONE;
            try {
                stoneColor = morris.getStoneColor( i + 1 );
            }
            catch( IllegalPositionException e ) {
            }
            if( stoneColor != StoneColor.NONE && position[i][HIDE] == 0 ) {
                if( stoneColor == StoneColor.WHITE ) {
                    if( position[i][MARK] == 0 ) {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getWhiteColor() );
                    }
                    else {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getMarkWhiteColor() );
                    }
                }
                else if( stoneColor == StoneColor.BLACK ) {
                    if( position[i][MARK] == 0 ) {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getBlackColor() );
                    }
                    else {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getMarkBlackColor() );
                    }
                }
                /* Zeichne Spielstein */
                g.fillOval( position[i][ABS_X] - diameter / 2,
                            position[i][ABS_Y] - diameter / 2,
                            diameter,
                            diameter );
            }
        }
    }

    /**
     * Zeichnet die Steine außerhalb des Spielbretts
     * 
     * @param g Grafik-Kontext, in welchen gezeichent werden soll
     * @param boardSize Größe des Spielbretts
     */
    private void paintStonesOutside( Graphics g, int boardSize ) {

        int diameter = boardSize / 15;
        for( int i = 0; i < stonesOutside.length; i++ ) {
            for( int j = 0; j < stonesOutside[i].length; j++ ) {
                for( int k = 0; k < stonesOutside[i][j].length; k++ ) {
                    boolean isVisible = ( stonesOutside[i][j][k][HIDE] == 0 );
                    if( isVisible && ( i == 0 || i == 1 ) ) {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getWhiteColor() );
                    }
                    else if( isVisible ) {
                        g.setColor( morris.getConfig().getGameStyle()
                                          .getBlackColor() );
                    }
                    if( isVisible ) {
                        g.fillOval( stonesOutside[i][j][k][ABS_X]
                                - diameter
                                / 2, stonesOutside[i][j][k][ABS_Y]
                                - diameter
                                / 2, diameter, diameter );
                    }
                }
            }
        }
    }

    /**
     * Berechnet die Positionen der Steine außerhalb des Spielbretts neu
     * 
     * @param boardSize Größe des Spielbretts
     */
    private void updateOutsidePositions( int boardSize ) {

        int distance = boardSize / 14;
        int xStart = 0;
        int yStart = 0;

        for( int i = 0; i < stonesOutside.length; i++ ) {
            switch( i ) {
            case 0:
                xStart = 0;
                yStart = offsetY;
                break;
            case 1:
                xStart = boardSize + offsetX;
                yStart = offsetY;
                break;
            case 2:
                xStart = 0;
                yStart = boardSize;
                break;
            case 3:
                xStart = boardSize + offsetX;
                yStart = boardSize;
                break;
            }
            for( int x = 0; x < stonesOutside[i].length; x++ ) {
                for( int y = 0; y < stonesOutside[i][x].length; y++ ) {
                    stonesOutside[i][x][y][ABS_X] = xStart
                            + ( x + 1 )
                            * distance;
                    stonesOutside[i][x][y][ABS_Y] = yStart
                            + ( y + 1 )
                            * distance;
                }
            }
        }
    }

    /**
     * Berechnet die Position der Spielsteine auf dem Spielbrett neu.
     * 
     * @param boardSize Größe des Spielbretts
     */
    private void updatePositions( int boardSize ) {

        /* Skalierungsfaktor des Spielfeldes */
        int factor = boardSize / 8;

        /* Radius eines Steins */
        int radius = boardSize / 30;

        /* Erste horizontale Reihe */
        position[0][ABS_X] = offsetX + 1 * factor;
        position[0][ABS_Y] = offsetY + 1 * factor;
        position[1][ABS_X] = offsetX + 4 * factor;
        position[1][ABS_Y] = offsetY + 1 * factor;
        position[2][ABS_X] = offsetX + 7 * factor;
        position[2][ABS_Y] = offsetY + 1 * factor;

        /* Zweite horizontale Reihe */
        position[3][ABS_X] = offsetX + 2 * factor;
        position[3][ABS_Y] = offsetY + 2 * factor;
        position[4][ABS_X] = offsetX + 4 * factor;
        position[4][ABS_Y] = offsetY + 2 * factor;
        position[5][ABS_X] = offsetX + 6 * factor;
        position[5][ABS_Y] = offsetY + 2 * factor;

        /* Dritte horizontale Reihe */
        position[6][ABS_X] = offsetX + 3 * factor;
        position[6][ABS_Y] = offsetY + 3 * factor;
        position[7][ABS_X] = offsetX + 4 * factor;
        position[7][ABS_Y] = offsetY + 3 * factor;
        position[8][ABS_X] = offsetX + 5 * factor;
        position[8][ABS_Y] = offsetY + 3 * factor;

        /* Vierte horizontale Reihe */
        position[9][ABS_X] = offsetX + 1 * factor;
        position[9][ABS_Y] = offsetY + 4 * factor;
        position[10][ABS_X] = offsetX + 2 * factor;
        position[10][ABS_Y] = offsetY + 4 * factor;
        position[11][ABS_X] = offsetX + 3 * factor;
        position[11][ABS_Y] = offsetY + 4 * factor;
        position[12][ABS_X] = offsetX + 5 * factor;
        position[12][ABS_Y] = offsetY + 4 * factor;
        position[13][ABS_X] = offsetX + 6 * factor;
        position[13][ABS_Y] = offsetY + 4 * factor;
        position[14][ABS_X] = offsetX + 7 * factor;
        position[14][ABS_Y] = offsetY + 4 * factor;

        /* Fünfte horizontale Reihe */
        position[15][ABS_X] = offsetX + 3 * factor;
        position[15][ABS_Y] = offsetY + 5 * factor;
        position[16][ABS_X] = offsetX + 4 * factor;
        position[16][ABS_Y] = offsetY + 5 * factor;
        position[17][ABS_X] = offsetX + 5 * factor;
        position[17][ABS_Y] = offsetY + 5 * factor;

        /* Sechste horizontale Reihe */
        position[18][ABS_X] = offsetX + 2 * factor;
        position[18][ABS_Y] = offsetY + 6 * factor;
        position[19][ABS_X] = offsetX + 4 * factor;
        position[19][ABS_Y] = offsetY + 6 * factor;
        position[20][ABS_X] = offsetX + 6 * factor;
        position[20][ABS_Y] = offsetY + 6 * factor;

        /* Siebte horizontale Reihe */
        position[21][ABS_X] = offsetX + 1 * factor;
        position[21][ABS_Y] = offsetY + 7 * factor;
        position[22][ABS_X] = offsetX + 4 * factor;
        position[22][ABS_Y] = offsetY + 7 * factor;
        position[23][ABS_X] = offsetX + 7 * factor;
        position[23][ABS_Y] = offsetY + 7 * factor;

        /* Minimum und Maximum neu berechnen */
        for( int i = 0; i < position.length; i++ ) {
            position[i][MIN_X] = position[i][ABS_X] - radius;
            position[i][MAX_X] = position[i][ABS_X] + radius;
            position[i][MIN_Y] = position[i][ABS_Y] - radius;
            position[i][MAX_Y] = position[i][ABS_Y] + radius;
        }
    }
}
