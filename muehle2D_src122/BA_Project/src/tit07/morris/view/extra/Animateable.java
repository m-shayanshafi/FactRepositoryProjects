package tit07.morris.view.extra;

import tit07.morris.exception.IllegalPositionException;


/**
 * Das Interface Animatable legt die Schnittstellen f�r die Spielvisualisierung
 * fest, welche n�tig sind um Animationen auf dem Spielfeld durchzuf�hren.
 */
public interface Animateable {

    /**
     * Position der wei�en zu setzenden Steine
     */
    public final static int WHITE_SET_POSITION    = 25;

    /**
     * Position der schwarzen zu setzenden Steine
     */
    public final static int BLACK_SET_POSITION    = WHITE_SET_POSITION + 1;

    /**
     * Position der wei�en geschlagenen Steine
     */
    public final static int WHITE_REMOVE_POSITION = BLACK_SET_POSITION + 1;

    /**
     * Position der schwarzen geschlagenen Steine
     */
    public final static int BLACK_REMOVE_POSITION = WHITE_REMOVE_POSITION + 1;


    /**
     * Zeichnet das Spielfeld anhand der hinterlegten Daten neu
     */
    public void repaint();

    /**
     * Versteckt einen Stein auf der �bergebenen Position
     * 
     * @param isHidden True, wenn der Stein versteckt werden soll, false f�r
     *            Sichtbarkeit
     * @param position Position des Steins auf dem Spielfeld (1-24)
     * @throws IllegalPositionException Wird bei ung�ltiger Position geworfen
     */
    public void hidePosition( boolean isHidden, int position )
                                                              throws IllegalPositionException;

    /**
     * Gibt den Durchmesser des Steins auf dem Spielfeld zur�ck
     * 
     * @return Durchmesser des Spielsteins in Pixel
     */
    public int getDiameter();

    /**
     * Gibt f�r eine Position auf dem Spielfeld die zugeh�rigen Koordinaten
     * zur�ck
     * 
     * @param position Position des Steins auf dem Spielfeld
     * @return Array mit den Koodinaten (Index 0 = X-Koordinate, Index 1 =
     *         Y-Koordinate)
     * @throws IllegalPositionException Wird bei ung�ltiger Position geworfen
     */
    public int[] position2Coords( int position )
                                                throws IllegalPositionException;
}
