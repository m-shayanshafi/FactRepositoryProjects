package tit07.morris.view;

/**
 * Definiert Schnittstellen welche n�tig sind, um das Spiel zu zeichnen.
 */
public interface Drawable {

    /**
     * Zeichnet die Spielfl�che neu
     */
    public void repaintGameWindow();

    /**
     * Bewegt die Animation vorw�rts
     * 
     * @return True, wenn die Animation noch l�uft
     */
    public boolean moveAnimation();

    /**
     * Gibt die Zielposition der Animation zur�ck
     * 
     * @return Zielposition der Animation
     */
    public int getAnimationDestination();

    /**
     * Zeichnet das Statusfenster neu
     */
    public void repaintStatusWindow();

    /**
     * Zeichnet das Top-Men� neu
     */
    public void repaintTopMenu();

    /**
     * Gleicht die Daten des Statusfensters mit denen des Models ab
     * 
     * @param width Breite des Hauptfensters
     */
    public void updateStatusWindow( int width );

    /**
     * Gibt die Breite des Hautpfensters zur�ck
     * 
     * @return Breite des Hauptfensters
     */
    public int getWidth();

    /**
     * R�umt nach einer Animation auf und macht den Stein auf der Zielposition
     * sichtbar
     * 
     * @param position Zielposition der beendeten Animation
     */
    public void disposeAnimation( int position );
}
