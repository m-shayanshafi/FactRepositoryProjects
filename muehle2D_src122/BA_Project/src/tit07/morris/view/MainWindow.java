package tit07.morris.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import tit07.morris.exception.IllegalPositionException;
import tit07.morris.model.ModelViewable;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;
import tit07.morris.model.config.PlayerInput;
import tit07.morris.view.extra.SoundEngine;


/**
 * Die Klasse MainWindow stellt die Schnittstelle des View dar. Sie organisiert
 * die einzelnen Komponenten des Fensters und stellt Zugriffsmethoden f�r diese
 * zur Verf�gung.
 */
public class MainWindow extends JFrame implements Observer, ViewControllable,
                                      Drawable {

    /** Referenz auf das Model */
    private ModelViewable model;

    /** Men�leiste oben */
    private TopMenu       topMenu;

    /** Spielfenster */
    private GameWindow    gameWindow;

    /** Statusfenster */
    private StatusWindow  statusWindow;

    /** Optionsmen� */
    private OptionMenu    optionMenu;

    /** MessageBox zur Anzeige von Dialog-Nachrichten */
    private MessageBox    messageBox;

    /** Zeichenthread */
    private DrawThread    drawThread;

    /** Sound-Engine */
    private SoundEngine   soundEngine;


    /**
     * Erzeugt den View und Controller mit allen n�tigen Komponenten
     * 
     * @param model Referenz auf das Model
     * @param handle Listener f�r auftretende Events
     */
    public MainWindow( ModelViewable model, Reactable handle ) {

        /* Initialisierung des Fensters */
        super();
        this.frameInit();
        this.setMinimumSize( new Dimension( 670,
                                            400 ) );
        this.setLayout( new BorderLayout() );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setSize( model.getConfig().getXSize(), model.getConfig()
                                                         .getYSize() );
        this.setTitle( model.getConfig().getTitle()
                + " - " //$NON-NLS-1$
                + model.getConfig().getVersion() );

        this.model = model;

        /* Controller erzeugen */
        this.addComponentListener( handle );
        this.addWindowListener( handle );

        /* Men�leiste erzeugen */
        this.topMenu = new TopMenu( handle,
                                    model );
        this.setJMenuBar( topMenu );

        /* Spielfenster erzeugen */
        this.gameWindow = new GameWindow( model,
                                          handle );
        this.add( BorderLayout.CENTER, gameWindow );

        /* Statusfenster erzeugen */
        this.statusWindow = new StatusWindow( model );
        this.add( BorderLayout.SOUTH, statusWindow );
        this.statusWindow.update( this.getWidth() );

        /* Optionsmen� erzeugen */
        this.optionMenu = new OptionMenu( model,
                                          handle );

        /* MessageBox erzeugen */
        this.messageBox = new MessageBox( this );

        /* Sound-Engine erzeugen */
        this.soundEngine = new SoundEngine( model );

        /* Zeichenthread erzeugen */
        this.drawThread = new DrawThread( this );
    }

    /**
     * Schreibt eine Nachricht in die Statusleiste
     * 
     * @param message Nachricht in der Statusleiste
     */
    @Override
    public void setStatusLine( String message ) {

        this.statusWindow.setStatusLine( message );
    }

    /**
     * Zeigt eine Meldung in einem Error-Dialog an
     * 
     * @param msg Fehlermeldung
     */
    @Override
    public void showErrorMsg( String msg ) {

        this.messageBox.showErrorMessage( msg );
    }

    /**
     * Zeigt eine Meldung in einem Info-Dialog an
     * 
     * @param msg Infomeldung
     */
    @Override
    public void showInfoMsg( String msg ) {

        this.messageBox.showInfoMessage( msg );
    }

    /**
     * Wird automatisch aufgerufen bei Ver�nderungen im Model
     * 
     * @param o Das zu beobachtende Objekt (Model)
     * @param arg Argument, welches der Methode notifyObservers �bergeben wurde
     */
    @Override
    public void update( Observable o, Object arg ) {

        try {
            /* Menschlicher wei�er Spieler hat gewonnen */
            if( model.getState( StoneColor.WHITE ) == State.WINNER
                    && model.getConfig().getWhiteInput() == PlayerInput.HUMAN ) {

                this.soundEngine.playSound( SoundEngine.WINNER );
            }

            /* Menschlicher schwarzer Spieler hat gewonnen */
            else if( model.getState( StoneColor.BLACK ) == State.WINNER
                    && model.getConfig().getBlackInput() == PlayerInput.HUMAN ) {

                this.soundEngine.playSound( SoundEngine.WINNER );
            }

            /* Menschlicher wei�er Spieler hat gegen die KI verloren */
            else if( model.getState( StoneColor.WHITE ) == State.LOSER
                    && model.getConfig().getWhiteInput() == PlayerInput.HUMAN ) {

                this.soundEngine.playSound( SoundEngine.GAME_OVER );
            }

            /* Menschlicher schwarzer Spieler hat gegen die KI verloren */
            else if( model.getState( StoneColor.BLACK ) == State.LOSER
                    && model.getConfig().getBlackInput() == PlayerInput.HUMAN ) {

                this.soundEngine.playSound( SoundEngine.GAME_OVER );
            }

            /* Spielzug: SETZEN wurde ausgef�hrt */
            else if( arg.equals( "SET" ) ) { //$NON-NLS-1$
                this.soundEngine.playSound( SoundEngine.SET_SOUND );
            }

            /* Spielzug: ZIEHEN wurde ausgef�hrt */
            else if( arg.equals( "MOVE" ) ) { //$NON-NLS-1$
                this.soundEngine.playSound( SoundEngine.MOVE_SOUND );
            }

            /* Spielzug SPRINGEN wurde ausgef�hrt */
            else if( arg.equals( "JUMP" ) ) { //$NON-NLS-1$
                this.soundEngine.playSound( SoundEngine.JUMP_SOUND );
            }

            /* Spielzug ENTFERNEN wurde ausgef�hrt */
            else if( arg.equals( "REMOVE" ) ) { //$NON-NLS-1$
                this.soundEngine.playSound( SoundEngine.REMOVE_SOUND );
            }

            /* Statusleiste bei beendetem Spiel aktualisieren */
            if( model.getState( StoneColor.BLACK ) == State.WINNER
                    || model.getState( StoneColor.WHITE ) == State.WINNER ) {

                this.setStatusLine( Messages.getString("MainWindow.game_over") ); //$NON-NLS-1$
            }
            else if( model.getState( StoneColor.BLACK ) == State.DRAW ) {
                this.setStatusLine( Messages.getString("MainWindow.draw") ); //$NON-NLS-1$
            }

            /* Spiel geladen */
            if( arg.equals( "LOAD" ) ) { //$NON-NLS-1$
                int whiteSet = model.getStonesOut( StoneColor.WHITE );
                int blackSet = model.getStonesOut( StoneColor.BLACK );
                int whiteOut = 9
                        - whiteSet
                        - model.getStonesIn( StoneColor.WHITE );
                int blackOut = 9
                        - blackSet
                        - model.getStonesIn( StoneColor.BLACK );
                this.gameWindow.setStonesOutside( whiteSet,
                                                  whiteOut,
                                                  blackSet,
                                                  blackOut );
                drawThread.repaint( false );
            }
        }
        catch( Exception e ) {
        }
    }

    /**
     * Gibt an, ob die Animation momentan l�uft
     * 
     * @return True, wenn die Animation aktiv ist
     */
    @Override
    public boolean isAnimationRunning() {

        return gameWindow.getAnimationEngine().isRunning();
    }

    /**
     * Aktiviert/Deaktiviert den �bernehmen-Button
     * 
     * @param isEnabled True, um den �bernehmen-Button zu aktivieren
     */
    public void enableApplyButton( boolean isEnabled ) {

        this.optionMenu.getApplyButton().setEnabled( isEnabled );
    }

    /**
     * Aktiviert die Auswahl f�r die schwarze KI
     * 
     * @param isEnabled True, um die schwarze KI-Auswahl zu aktivieren
     */
    @Override
    public void enableBlackAI( boolean isEnabled ) {

        this.optionMenu.getChosenAIBlack().setEnabled( isEnabled );

    }

    /**
     * Aktiviert das Textfeld f�r den schwarzen Spielernamen
     * 
     * @param isEnabled True, um den schwarzen Spielernamen zu aktivieren
     */
    @Override
    public void enableBlackName( boolean isEnabled ) {

        this.optionMenu.getBlackName().setEnabled( isEnabled );

    }

    /**
     * Aktiviert die Auswahl f�r die wei�e KI
     * 
     * @param isEnabled True, um die wei�e KI-Auswahl zu aktivieren
     */
    @Override
    public void enableWhiteAI( boolean isEnabled ) {

        this.optionMenu.getChosenAIWhite().setEnabled( isEnabled );

    }

    /**
     * Aktiviert das Textfeld f�r den wei�en Spielernamen
     * 
     * @param isEnabled True, um den wei�en Spielernamen zu aktivieren
     */
    @Override
    public void enableWhiteName( boolean isEnabled ) {

        this.optionMenu.getWhiteName().setEnabled( isEnabled );

    }

    /**
     * Setzt den Eintrag in der Men�leiste auf pausieren/fortsetzen
     * 
     * @param isRunning True f�r pausieren, false f�r fortsetzen
     */
    @Override
    public void setRunning( boolean isRunning ) {

        this.topMenu.setRunning( isRunning );
    }

    /**
     * �ffnet das Optionsmen�
     * 
     * @param activeTab GRAPHIC_OPTION oder PLAYER_OPTION
     */
    @Override
    public void openOptionMenu( String activeTab ) {

        this.optionMenu.openOptionMenu( activeTab );

    }

    /**
     * Schlie�t das Optionsmen�
     */
    @Override
    public void closeOptionMenu() {

        this.optionMenu.closeOptionMenu();

    }

    /**
     * Gibt an, ob die Animation aktiviert oder deaktiviert ist
     * 
     * @return True, wenn die Animation aktiviert ist
     */
    @Override
    public boolean getAnimationOption() {

        return this.topMenu.getAnimationOption().getState();
    }

    /**
     * Gibt an, ob der Sound aktiviert oder deaktiviert ist
     * 
     * @return True, wenn der Sound aktiviert ist
     */
    @Override
    public boolean getSoundOption() {

        return this.topMenu.getSoundOption().getState();
    }

    /**
     * Zeichnet die das gesamte Hauptfenster neu
     */
    @Override
    public void repaintAll() {

        this.drawThread.repaint( false );
    }

    /**
     * Gibt an, ob das Optionsmen� vom Standard-Button aufgerufen wurde
     * 
     * @param isCallFromStandardButton True, beim Aufruf vom Standard-Button
     */
    @Override
    public void setIsCallFromStandardButton( boolean isCallFromStandardButton ) {

        this.optionMenu.setIsCallFromStandardButton( isCallFromStandardButton );
    }

    /**
     * Gibt den Namen des aktiven Tabs zur�ck
     * 
     * @return Name des aktiven Tabs
     */
    @Override
    public String getOptionName() {

        return this.optionMenu.getTabBar().getSelectedComponent().getName();
    }

    /**
     * Wandelt die �bergebenen 2D-Koordinaten in die entsprechende Position
     * (1-24) auf dem Spielfeld um. Gibt 0 zur�ck, wenn keine �bereinstimmung
     * vorliegt.
     * 
     * @param xCoord Position in X-Richtung
     * @param yCoord Position in Y-Richtung
     * @return Spielfeldposition der �bergebenen Koordinaten
     */
    @Override
    public int coordiantes2Position( int xCoord, int yCoord ) {

        return this.gameWindow.coordiantes2Position( xCoord, yCoord );
    }

    /**
     * Gibt den Namen der schwarzen KI zur�ck
     * 
     * @return Name der schwarzen KI
     */
    @Override
    public String getBlackAIName() {

        return (String) this.optionMenu.getChosenAIBlack().getSelectedItem();
    }

    /**
     * Gibt den Namen des schwarzen Spielers
     * 
     * @return Name des schwarzen Spielers
     */
    @Override
    public String getBlackName() {

        return this.optionMenu.getBlackName().getText();
    }

    /**
     * Gibt den Namen der wei�en KI zur�ck
     * 
     * @return Name der wei�en KI
     */
    @Override
    public String getWhiteAIName() {

        return (String) this.optionMenu.getChosenAIWhite().getSelectedItem();
    }

    /**
     * Gibt den Namen des wei�en Spielers zur�ck
     * 
     * @return Name des wei�en Spielers
     */
    @Override
    public String getWhiteName() {

        return this.optionMenu.getWhiteName().getText();
    }

    /**
     * Versteckt einen Stein auf der �bergebenen Position
     * 
     * @param isHidden True, wenn der Stein versteckt werden soll, false f�r
     *            Sichtbarkeit
     * @param position Position des Steins auf dem Spielfeld (1-24)
     * @throws IllegalPositionException Wird bei ung�ltiger Position geworfen
     */
    @Override
    public void hidePosition( boolean isHidden, int position )
                                                              throws IllegalPositionException {

        this.gameWindow.hidePosition( isHidden, position );
    }

    /**
     * Initialisiert die Animation: "Setze Stein aufs Spielbrett".
     * 
     * @param position Position auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    @Override
    public void initSetAnimation( int position )
                                                throws IllegalPositionException {

        this.gameWindow.getAnimationEngine().initSetAnimation( position );
    }

    /**
     * Initialisiert die Animation: "Bewege Stein auf dem Spielbrett".
     * 
     * @param fromPosition Ausgangsposition des Steins
     * @param toPosition Zielposition des Steins
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    @Override
    public void initMoveAnimation( int fromPosition, int toPosition )
                                                                     throws IllegalPositionException {

        this.gameWindow.getAnimationEngine().initMoveAnimation( fromPosition,
                                                                toPosition );

    }

    /**
     * Initialisiert die Animation: "Entferne Stein vom Spielbrett".
     * 
     * @param position Position, an welcher der Stein entfernt wurde
     * @param color Farbe des Steins, welcher entfernt wurde
     * @throws IllegalPositionException Wird geworfen bei falscher Position oder
     *             kein Stein auf Position
     */
    @Override
    public void initRemoveAnimation( int position, StoneColor color )
                                                                     throws IllegalPositionException {

        this.gameWindow.getAnimationEngine().initRemoveAnimation( position,
                                                                  color );

    }

    /**
     * Gibt an, ob die schwarze KI aktiviert ist
     * 
     * @return True, wenn die schwarze KI aktiviert ist
     */
    @Override
    public boolean isBlackAIEnabled() {

        return this.optionMenu.getChosenAIBlack().isEnabled();
    }

    /**
     * Gibt an, ob die wei�e KI aktiviert ist
     * 
     * @return True, wenn die wei�e KI aktiviert ist
     */
    @Override
    public boolean isWhiteAIEnabled() {

        return this.optionMenu.getChosenAIWhite().isEnabled();
    }

    /**
     * Markiert eine Position auf dem Spielfeld
     * 
     * @param isMarked Bei true wird die Position markiert, bei false aufgel�st.
     * @param position Die Position auf dem Spielbrett
     * @throws IllegalPositionException Wird bei ung�ltiger Position geworfen
     */
    @Override
    public void markPosition( boolean isMarked, int position )
                                                              throws IllegalPositionException {

        this.gameWindow.markPosition( isMarked, position );

    }

    /**
     * Schreibt die aktuellen Einstellungen vom Optionsmen� in die Config
     */
    @Override
    public void saveOptionParameters() throws Exception {

        this.optionMenu.saveParameters();
    }

    /**
     * Macht alle Steine wieder sichtbar
     */
    @Override
    public void unhideAllStones() {

        this.gameWindow.unhideAllStones();

    }

    /**
     * Gleicht das Statusmen� mit den aktuellen Werten des Spiels ab
     */
    @Override
    public void updateStatusWindow( int width ) {

        this.statusWindow.update( width );

    }

    /**
     * Gibt die Zielposition der Animation zur�ck
     * 
     * @return Zielposition der Animation
     */
    @Override
    public int getAnimationDestination() {

        return this.gameWindow.getAnimationEngine().getDestinationPosition();
    }

    /**
     * Bewegt die Animation auf die Zielposition zu. Wenn sie das Ziel erreicht
     * hat wird das Flag, ob die Animation aktiv ist auf false gesetzt.
     * 
     * @return True, wenn die Animation noch aktiv ist
     */
    @Override
    public boolean moveAnimation() {

        return this.gameWindow.getAnimationEngine().moveAnimation();

    }

    /**
     * Zeichnet die Spielfl�che neu
     */
    @Override
    public void repaintGameWindow() {

        this.gameWindow.repaint();

    }

    /**
     * Zeichnet das Statusfenster neu
     */
    @Override
    public void repaintStatusWindow() {

        this.statusWindow.repaint();

    }

    /**
     * Zeichnet das Hauptmen� neu
     */
    @Override
    public void repaintTopMenu() {

        this.topMenu.repaint();

    }

    /**
     * R�umt nach einer Animation auf, indem der Stein auf der Zielposition
     * wieder sichtbar gemacht wird
     * 
     * @param position Zielposition der Animation
     */
    @Override
    public void disposeAnimation( int position ) {

        this.gameWindow.disposeAnimation( position );

    }

    /**
     * Setzt den View in den Anfangszustand zur�ck
     */
    @Override
    public void resetView() {

        this.gameWindow.getAnimationEngine().reset();
        this.gameWindow.resetGame();
    }

    /**
     * Startet den Zeichenthread
     */
    @Override
    public void startDrawing() {

        this.drawThread.start();
    }

    /**
     * Stoppt den Zeichenthread
     */
    @Override
    public void stopDrawing() {

        this.drawThread.stop();
    }
    
    /**
     * Setzt den Haken bei der Soundoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setSound(boolean isOn) {
        this.topMenu.setSound( isOn );
    }
    
    /**
     * Setzt den Haken bei der Animationsoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setAnimation(boolean isOn) {
        this.topMenu.setAnimation( isOn );
    }
}
