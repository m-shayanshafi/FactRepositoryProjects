package tit07.morris.view;

import tit07.morris.exception.IllegalPositionException;
import tit07.morris.model.StoneColor;


/**
 * Das Interface ViewControllable legt alle Schnittstellen fest, welche vom
 * Controller ben�tigt werden, um den View zu steuern.
 */
public interface ViewControllable {

    /**
     * Gibt an, ob die Animation momentan l�uft
     * 
     * @return True, wenn die Animation aktiv ist
     */
    public boolean isAnimationRunning();

    /**
     * Setzt die Statusleiste
     * 
     * @param message Nachricht der Statusleiste
     */
    public void setStatusLine( String message );

    /**
     * Aktiviert / Deaktiviert den �bernehmen-Button
     * 
     * @param isEnabled True, um den Button zu aktivieren
     */
    public void enableApplyButton( boolean isEnabled );

    /**
     * Aktiviert das Textfeld f�r den wei�en Spieler
     * 
     * @param isEnabled True, um das Texteld zu aktivieren
     */
    public void enableWhiteName( boolean isEnabled );

    /**
     * Aktiviert das Textfeld f�r den schwarzen Spieler
     * 
     * @param isEnabled True, um das Texteld zu aktivieren
     */
    public void enableBlackName( boolean isEnabled );

    /**
     * Aktiviert die Auswahl f�r die wei�e KI
     * 
     * @param isEnabled True, um die Auswahl zu aktivieren
     */
    public void enableWhiteAI( boolean isEnabled );

    /**
     * Aktiviert die Auswahl f�r die schwarze KI
     * 
     * @param isEnabled True, um die Auswahl zu aktivieren
     */
    public void enableBlackAI( boolean isEnabled );

    /**
     * Setzt den Men�eintrag auf pausieren/fortsetzen je nach Spielzustand
     * 
     * @param isRunning True, wenn das Spiel aktiv ist.
     */
    public void setRunning( boolean isRunning );

    /**
     * Zeigt eine Meldung in einem Error-Dialog an
     * 
     * @param msg Fehlermeldung
     */
    public void showErrorMsg( String msg );

    /**
     * Gibt die Breite des Fensters zur�ck
     * 
     * @return Breite des Fensters in Pixel
     */
    public int getWidth();

    /**
     * Gibt die H�he des Fensters zur�ck
     * 
     * @return H�he des Fensters in Pixel
     */
    public int getHeight();

    /**
     * �ffnet das Optionsmen� und aktiviert den �bergebenen Tab
     * 
     * @param activeTab GRAPHIC_OPTION oder PLAYER_OPTION
     */
    public void openOptionMenu( String activeTab );

    /**
     * Schlie�t das Optionsmen�
     */
    public void closeOptionMenu();

    /**
     * Gibt an, ob der Sound aktiviert oder deaktiviert ist
     * 
     * @return True, wenn der Sound aktiviert ist
     */
    public boolean getSoundOption();

    /**
     * Gibt an, ob die Animation aktiviert oder deaktiviert ist
     * 
     * @return True, wenn die Animation aktiviert ist
     */
    public boolean getAnimationOption();

    /**
     * Zeigt eine Meldung in einem Info-Dialog an
     * 
     * @param msg Infomeldung
     */
    public void showInfoMsg( String msg );

    /**
     * Wandelt die �bergebenen 2D-Koordinaten in die entsprechende Position
     * (1-24) auf dem Spielfeld um. Gibt 0 zur�ck, wenn keine �bereinstimmung
     * vorliegt.
     * 
     * @param xCoord Position in X-Richtung
     * @param yCoord Position in Y-Richtung
     * @return Spielfeldposition der �bergebenen Koordinaten
     */
    public int coordiantes2Position( int xCoord, int yCoord );

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
     * Macht alle Steine wieder sichtbar
     */
    public void unhideAllStones();

    /**
     * Markiert eine Position auf dem Spielfeld
     * 
     * @param isMarked Bei true wird die Position markiert, bei false aufgel�st.
     * @param position Die Position auf dem Spielbrett
     * @throws IllegalPositionException Wird bei ung�ltiger Position geworfen
     */
    public void markPosition( boolean isMarked, int position )
                                                              throws IllegalPositionException;

    /**
     * Initialisiert die Animation: "Setze Stein aufs Spielbrett".
     * 
     * @param position Position auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initSetAnimation( int position )
                                                throws IllegalPositionException;

    /**
     * Initialisiert die Animation: "Bewege Stein auf dem Spielbrett".
     * 
     * @param fromPosition Ausgangsposition des Steins
     * @param toPosition Zielposition des Steins
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initMoveAnimation( int fromPosition, int toPosition )
                                                                     throws IllegalPositionException;

    /**
     * Initialisiert die Animation: "Entferne Stein vom Spielbrett".
     * 
     * @param position Position, an welcher der Stein entfernt wurde
     * @param color Farbe des Steins, welcher entfernt wurde
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    public void initRemoveAnimation( int position, StoneColor color )
                                                                     throws IllegalPositionException;

    /**
     * Gleicht die Daten des Statusfenster mit denen des Models ab
     * 
     * @param width Breite des Hauptfensters
     */
    public void updateStatusWindow( int width );

    /**
     * Schreibt die aktuelle Konfiguration in die Config-Datei
     * 
     * @throws Exception Wird geworfen, wenn beim Schreibvorhang ein Problem
     *             auftritt.
     */
    public void saveOptionParameters() throws Exception;

    /**
     * Zeichnet das komplette Hauptfenster neu
     */
    public void repaintAll();

    /**
     * Gibt an, ob das Optionsmen� vom Standard-Button aufgerufen wurde
     * 
     * @param isCallFromStandardButton True, wenn es vom Standard-Button
     *            aufgerufen wurde
     */
    public void setIsCallFromStandardButton( boolean isCallFromStandardButton );

    /**
     * Gibt den Namen des aktiven Tabs im Optionsmen� zur�ck
     * 
     * @return Name des aktivierten Tab im Optionsmen�
     */
    public String getOptionName();

    /**
     * Gibt an, ob die wei�e KI aktiviert ist
     * 
     * @return True, wenn die wei�e KI aktiviert ist
     */
    public boolean isWhiteAIEnabled();

    /**
     * Gibt an, ob die schwarze KI aktiviert ist
     * 
     * @return True, wenn die schwarze KI aktiviert ist
     */
    public boolean isBlackAIEnabled();

    /**
     * Gibt den Namen der wei�en KI zur�ck
     * 
     * @return Name der wei�en KI
     */
    public String getWhiteAIName();

    /**
     * Gibt den Namen der schwarzen KI zur�ck
     * 
     * @return Name der schwarzen KI
     */
    public String getBlackAIName();

    /**
     * Gibt den Namen des schwarzen Spielers zur�ck
     * 
     * @return Name des schwarzen Spielers
     */
    public String getBlackName();

    /**
     * Gibt den Namen des wei�en Spielers zur�ck
     * 
     * @return Name des wei�en Spielers
     */
    public String getWhiteName();

    /**
     * Setzt den View zur�ck
     */
    public void resetView();

    /**
     * Startet den Zeichenthread
     */
    public void startDrawing();

    /**
     * Stoppt den Zeichenthread
     */
    public void stopDrawing();
    
    /**
     * Setzt den Haken bei der Soundoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setSound(boolean isOn);
    
    /**
     * Setzt den Haken bei der Animationsoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setAnimation(boolean isOn);
}
