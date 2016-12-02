package tit07.morris.view;

/**
 * Definiert Schnittstellen welche nötig sind, um das Spiel zu zeichnen.
 */
public interface Drawable {

    /**
     * Zeichnet die Spielfläche neu
     */
    public void repaintGameWindow();

    /**
     * Bewegt die Animation vorwärts
     * 
     * @return True, wenn die Animation noch läuft
     */
    public boolean moveAnimation();

    /**
     * Gibt die Zielposition der Animation zurück
     * 
     * @return Zielposition der Animation
     */
    public int getAnimationDestination();

    /**
     * Zeichnet das Statusfenster neu
     */
    public void repaintStatusWindow();

    /**
     * Zeichnet das Top-Menü neu
     */
    public void repaintTopMenu();

    /**
     * Gleicht die Daten des Statusfensters mit denen des Models ab
     * 
     * @param width Breite des Hauptfensters
     */
    public void updateStatusWindow( int width );

    /**
     * Gibt die Breite des Hautpfensters zurück
     * 
     * @return Breite des Hauptfensters
     */
    public int getWidth();

    /**
     * Räumt nach einer Animation auf und macht den Stein auf der Zielposition
     * sichtbar
     * 
     * @param position Zielposition der beendeten Animation
     */
    public void disposeAnimation( int position );
}
