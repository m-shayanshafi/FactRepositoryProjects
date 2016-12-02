package tit07.morris.view.extra;

import java.awt.Graphics;
import tit07.morris.model.StoneColor;
import tit07.morris.model.ModelViewable;
import tit07.morris.exception.IllegalPositionException;


/**
 * Die Klasse AnimationEngine ist für die Animation auf dem Spielfeld zuständig.
 * Sie stellt Methoden zur Verfügung um Animationen zu initialisieren und den
 * animierten Stein zu zeichnen.
 */
public class AnimationEngine {

    /** Referenz auf das Model */
    private ModelViewable model;

    /** Referenz auf den View */
    private Animateable   view;

    /** Variable, ob die Animation momentan läuft */
    private boolean       isRunning;

    /** Zielposition des animatierten Steins */
    private int           destinationPosition;

    /** Aktuelle Koorinaten des animatierten Steins */
    private int           currentXPosition = 0;

    private int           currentYPosition = 0;


    /**
     * Erzeugt eine neue AnimationEngine zur Animation von Spielsteinen
     * 
     * @param view Referenz auf die Visualisierung des Spiels
     * @param model Referenz auf das Model des Spiels
     */
    public AnimationEngine( Animateable view, ModelViewable model ) {

        this.isRunning = false;
        this.view = view;
        this.model = model;
    }

    /**
     * Initialisiert die Animation: "Setze Stein aufs Spielbrett".
     * 
     * @param position Position auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initSetAnimation( int position )
                                                throws IllegalPositionException {

        if( this.isRunning ) {
            return;
        }
        this.isRunning = true;
        boolean isSuccessful = true;

        /* Überprüfe Zielposition */
        if( position < 1 || position > 24 ) {
            throw new IllegalPositionException( "Animation-Error: Falsche Position: " //$NON-NLS-1$
                    + position );
        }
        else {
            this.destinationPosition = position;
        }

        /* Berechne die X-Koordinate der Startposition außerhalb des Spielbretts */
        if( model.getStoneColor( position ) == StoneColor.WHITE ) {
            int[] coords = view
                               .position2Coords( Animateable.WHITE_SET_POSITION );
            this.currentXPosition = coords[0];
            this.currentYPosition = coords[1];
        }
        else if( model.getStoneColor( position ) == StoneColor.BLACK ) {
            int[] coords = view
                               .position2Coords( Animateable.BLACK_SET_POSITION );
            this.currentXPosition = coords[0];
            this.currentYPosition = coords[1];
        }
        else {
            isSuccessful = false;
        }

        /* Kein Stein auf der Position vorhanden */
        if( !isSuccessful ) {
            throw new IllegalPositionException( "Animation-Error: Kein Stein auf der Position " //$NON-NLS-1$
                    + position );
        }
    }

    /**
     * Initialisiert die Animation: "Bewege Stein auf dem Spielbrett".
     * 
     * @param fromPosition Ausgangsposition des Steins
     * @param toPosition Zielposition des Steins
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initMoveAnimation( int fromPosition, int toPosition )
                                                                     throws IllegalPositionException {

        if( this.isRunning ) {
            return;
        }
        this.isRunning = true;

        /* Überprüfe Zielposition */
        if( toPosition < 1 || toPosition > 24 ) {
            throw new IllegalPositionException( "Animation-Error: Falsche Position: " //$NON-NLS-1$
                    + fromPosition );
        }
        else {
            this.destinationPosition = toPosition;
        }

        /* Wandle die Ausgangsposition in Koordinaten um */
        int[] coords = view.position2Coords( fromPosition );
        this.currentXPosition = coords[0];
        this.currentYPosition = coords[1];
    }

    /**
     * Initialisiert die Animation: "Entferne Stein vom Spielbrett".
     * 
     * @param position Position, an welcher der Stein entfernt wurde
     * @param color Farbe des Steins, welcher entfernt wurde
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initRemoveAnimation( int position, StoneColor color )
                                                                     throws IllegalPositionException {

        if( this.isRunning ) {
            return;
        }
        this.isRunning = true;

        /* Abfrage, welcher Stein entfernt wurde */
        if( color == StoneColor.WHITE ) {
            this.destinationPosition = Animateable.WHITE_REMOVE_POSITION;
        }
        else if( color == StoneColor.BLACK ) {
            this.destinationPosition = Animateable.BLACK_REMOVE_POSITION;
        }
        else {
            throw new IllegalPositionException( "Animation-Error: Kein Stein aus Position " //$NON-NLS-1$
                    + position );
        }

        /* Wandle die Ausgangsposition in Koordinaten um */
        int[] coords = view.position2Coords( position );
        this.currentXPosition = coords[0];
        this.currentYPosition = coords[1];
    }

    /**
     * Zeichnet den animierten Stein
     * 
     * @param g Grafikkontext zum zeichnen
     * @param boardSize Größe des Spielfeldes
     */
    public void animate( Graphics g, int boardSize ) {

        try {
            /*
             * Führe die Animation nur durch wenn sie aktiviert und
             * initialisiert ist
             */
            if( !model.getConfig().isAnimationOn() || !isRunning ) {
                return;
            }

            /* Steinfarbe der Zielposition abfragen */
            if( destinationPosition == Animateable.BLACK_REMOVE_POSITION ) {
                g.setColor( model.getConfig().getGameStyle().getBlackColor() );
            }
            else if( destinationPosition == Animateable.WHITE_REMOVE_POSITION ) {
                g.setColor( model.getConfig().getGameStyle().getWhiteColor() );
            }
            else if( model.getStoneColor( destinationPosition ) == StoneColor.WHITE ) {
                g.setColor( model.getConfig().getGameStyle().getWhiteColor() );
            }
            else if( model.getStoneColor( destinationPosition ) == StoneColor.BLACK ) {
                g.setColor( model.getConfig().getGameStyle().getBlackColor() );
            }

            /* Durchmesser des Steins abfragen */
            int stoneDiameter = view.getDiameter();

            /* Animierten Stein zeichnen */
            g.fillOval( currentXPosition - stoneDiameter / 2, currentYPosition
                    - stoneDiameter
                    / 2, stoneDiameter, stoneDiameter );

        }
        catch( IllegalPositionException e ) {
        }
    }

    /**
     * Bewegt die Animation auf die Zielposition zu. Wenn sie das Ziel erreicht
     * hat wird das Flag, ob die Animation aktiv ist auf false gesetzt.
     * 
     * @return True, wenn die Animation noch aktiv ist
     */
    public boolean moveAnimation() {

        boolean isAnimationOver = this.isRunning;
        try {
            /*
             * Führe die Animation nur durch wenn sie aktiviert und
             * initialisiert ist
             */
            if( !model.getConfig().isAnimationOn() ) {
                isRunning = false;
            }

            /* Zielposition in Koordinaten umwandeln */
            int[] coords = view.position2Coords( destinationPosition );
            int xDestination = coords[0];
            int yDestination = coords[1];

            /* Aktuelle Position auf Zielposition zubewegen */
            for( int i = 0; i < model.getConfig().getAnimationSpeed(); i++ ) {
                if( currentXPosition < xDestination ) {
                    currentXPosition++;
                }
                else if( currentXPosition > xDestination ) {
                    currentXPosition--;
                }
                if( currentYPosition < yDestination ) {
                    currentYPosition++;
                }
                else if( currentYPosition > yDestination ) {
                    currentYPosition--;
                }
            }

            /* Animation beenden, wenn Ziel erreicht */
            if( currentXPosition == xDestination
                    && currentYPosition == yDestination ) {
                this.isRunning = false;
            }

            isAnimationOver = ( isAnimationOver && !isRunning );
        }
        catch( IllegalPositionException e ) {
        }
        return isAnimationOver;
    }

    /**
     * Gibt die Zielposition der Animation zurück
     * 
     * @return Zielposition der Animation
     */
    public int getDestinationPosition() {

        return this.destinationPosition;
    }

    /**
     * Abfrage, ob die Animation momentan aktiv ist
     * 
     * @return True, wenn die Animation läuft, ansonsten false.
     */
    public boolean isRunning() {

        return isRunning;
    }

    /**
     * Setzt die Animation zurück
     */
    public void reset() {

        this.isRunning = false;
    }
}
