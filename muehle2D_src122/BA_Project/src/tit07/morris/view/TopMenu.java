package tit07.morris.view;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import tit07.morris.model.Configurateable;


/**
 * Diese Klasse implementiert die Menüleiste für das Mühlespiel.
 */
public class TopMenu extends JMenuBar {

    /** Relativer Pfad zu den Bilddateien */
    private final String      ICON_PATH = "data/icons/"; //$NON-NLS-1$

    /** Referenz auf CheckboxItem für den Sound */
    private JCheckBoxMenuItem soundOption;

    /** Referenz auf CheckboxItem für die Animation */
    private JCheckBoxMenuItem animationOption;

    /** Menüitem um das Spiel zu pausieren/fortzusetzen */
    private JMenuItem         toggleGame;


    /**
     * Erzeugt eine Menüleiste für das Mühlespiel
     * 
     * @param controller Referenz auf den Controller
     * @param model Referenz auf das Model
     */
    public TopMenu( Reactable controller, Configurateable model ) {

        this.setDoubleBuffered( true );

        /*
         * ----- Menüpunkt: Spiel -----
         */
        JMenu game = new JMenu( Messages.getString("TopMenu.game") ); //$NON-NLS-1$
        game.addMenuListener( controller );
        this.add( game );

        /* ----- Neues Spiel ----- */
        ImageIcon newGameIcon = new ImageIcon( ICON_PATH + "newGame.gif" ); //$NON-NLS-1$
        JMenuItem newGame = new JMenuItem( Messages.getString("TopMenu.new_game"), //$NON-NLS-1$
                                           newGameIcon );
        newGame.setActionCommand( ActionCommand.NEWGAME );
        newGame.addActionListener( controller );
        game.add( newGame );

        /* ----- Spiel pausieren/fortsetzen ----- */
        ImageIcon toggleIcon = new ImageIcon( ICON_PATH + "toggleGame.gif" ); //$NON-NLS-1$
        this.toggleGame = new JMenuItem( toggleIcon );
        setRunning( true );
        this.toggleGame.setActionCommand( ActionCommand.TOGGLEGAME );
        this.toggleGame.addActionListener( controller );
        game.add( this.toggleGame );

        /* ----- Spiel laden ----- */
        ImageIcon loadGameIcon = new ImageIcon( ICON_PATH + "loadGame.gif" ); //$NON-NLS-1$
        JMenuItem loadGame = new JMenuItem( Messages.getString("TopMenu.load_game"), //$NON-NLS-1$
                                            loadGameIcon );
        loadGame.setActionCommand( ActionCommand.LOADGAME );
        loadGame.addActionListener( controller );
        game.add( loadGame );

        /* ----- Spiel speichern ----- */
        ImageIcon saveGameIcon = new ImageIcon( ICON_PATH + "saveGame.gif" ); //$NON-NLS-1$
        JMenuItem saveGame = new JMenuItem( Messages.getString("TopMenu.save_game"), //$NON-NLS-1$
                                            saveGameIcon );
        saveGame.setActionCommand( ActionCommand.SAVEGAME );
        saveGame.addActionListener( controller );
        game.add( saveGame );

        /* ----- Spiel beenden ----- */
        game.addSeparator();
        ImageIcon quitGameIcon = new ImageIcon( ICON_PATH + "quitGame.gif" ); //$NON-NLS-1$
        JMenuItem quitGame = new JMenuItem( Messages.getString("TopMenu.quit_game"), //$NON-NLS-1$
                                            quitGameIcon );
        quitGame.setActionCommand( ActionCommand.QUITGAME );
        quitGame.addActionListener( controller );
        game.add( quitGame );

        /*
         * ----- Menüpunkt: Einstellungen -----
         */
        JMenu options = new JMenu( Messages.getString("TopMenu.options") ); //$NON-NLS-1$
        options.addMenuListener( controller );
        this.add( options );

        /* ----- Grafik ----- */
        ImageIcon graphicIcon = new ImageIcon( ICON_PATH + "graphic.gif" ); //$NON-NLS-1$
        JMenuItem graphicOption = new JMenuItem( Messages.getString("TopMenu.graphic"), //$NON-NLS-1$
                                                 graphicIcon );
        graphicOption.setActionCommand( ActionCommand.GRAPHIC_OPTION );
        graphicOption.addActionListener( controller );
        options.add( graphicOption );

        /* ----- Spieler ----- */
        ImageIcon playerGameIcon = new ImageIcon( ICON_PATH
                + "playerOption.gif" ); //$NON-NLS-1$
        JMenuItem playerOption = new JMenuItem( Messages.getString("TopMenu.player"), //$NON-NLS-1$
                                                playerGameIcon );
        playerOption.setActionCommand( ActionCommand.PLAYER_OPTION );
        playerOption.addActionListener( controller );
        options.add( playerOption );

        /* ----- Sound an/aus ----- */
        ImageIcon soundGameIcon = new ImageIcon( ICON_PATH + "soundOption.gif" ); //$NON-NLS-1$
        this.soundOption = new JCheckBoxMenuItem( Messages.getString("TopMenu.sound"), //$NON-NLS-1$
                                                  soundGameIcon,
                                                  true );
        this.soundOption.setActionCommand( ActionCommand.TOGGLE_SOUND );
        this.soundOption.addActionListener( controller );
        options.add( this.soundOption );
        if( model.getConfig().isSoundOn() ) {
            soundOption.setSelected( true );
        }
        else
            soundOption.setSelected( false );

        /* ----- Animation an/aus ----- */
        ImageIcon animationGameIcon = new ImageIcon( ICON_PATH
                + "animation.gif" ); //$NON-NLS-1$
        this.animationOption = new JCheckBoxMenuItem( Messages.getString("TopMenu.animation"), //$NON-NLS-1$
                                                      animationGameIcon,
                                                      true );
        this.animationOption.setActionCommand( ActionCommand.TOGGLE_ANIMATION );
        this.animationOption.addActionListener( controller );
        options.add( this.animationOption );
        if( model.getConfig().isAnimationOn() ) {
            animationOption.setSelected( true );
        }
        else
            animationOption.setSelected( false );

        /*
         * ----- Menüpunkt: Hilfe -----
         */
        JMenu help = new JMenu( Messages.getString("TopMenu.help") ); //$NON-NLS-1$
        help.addMenuListener( controller );
        this.add( help );

        /* ----- Hilfe ----- */
        ImageIcon helpItemIcon = new ImageIcon( ICON_PATH + "helpItem.gif" ); //$NON-NLS-1$
        JMenuItem helpItem = new JMenuItem( Messages.getString("TopMenu.help"), //$NON-NLS-1$
                                            helpItemIcon );
        helpItem.setActionCommand( ActionCommand.HELP );
        helpItem.addActionListener( controller );
        help.add( helpItem );

        /* ----- Info ----- */
        ImageIcon aboutItemIcon = new ImageIcon( ICON_PATH + "aboutItem.gif" ); //$NON-NLS-1$
        JMenuItem aboutItem = new JMenuItem( Messages.getString("TopMenu.about"), //$NON-NLS-1$
                                             aboutItemIcon );
        aboutItem.setActionCommand( ActionCommand.INFO );
        aboutItem.addActionListener( controller );
        help.add( aboutItem );
    }

    /**
     * Setzt den Menüeintrag auf pausieren/fortsetzen je nach Spielzustand
     * 
     * @param isRunning True, wenn das Spiel aktiv ist.
     */
    public void setRunning( boolean isRunning ) {

        String text = ( isRunning ) ? Messages.getString("TopMenu.pause") : Messages.getString("TopMenu.cont"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        this.toggleGame.setText( text );
    }

    /**
     * Gibt die aktuelle Soundoption zurück
     * 
     * @return Aktuelle Soundoption
     */
    public JCheckBoxMenuItem getSoundOption() {

        return this.soundOption;
    }

    /**
     * Setzt die aktuelle Soundoption
     * 
     * @param soundOption Aktuelle Soundoption
     */
    public void setSoundOption( JCheckBoxMenuItem soundOption ) {

        this.soundOption = soundOption;
    }

    /**
     * Gibt die aktuelle Animationsoption zurück
     * 
     * @return Aktuelle Animationsoption
     */
    public JCheckBoxMenuItem getAnimationOption() {

        return this.animationOption;
    }

    /**
     * Setzt die aktuelle Animationsoption
     * 
     * @param animationOption Aktuelle Animationsoption
     */
    public void setAnimationOption( JCheckBoxMenuItem animationOption ) {

        this.animationOption = animationOption;
    }
    
    /**
     * Setzt den Haken bei der Soundoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setSound(boolean isOn) {
        this.soundOption.setSelected( isOn );
    }
    
    /**
     * Setzt den Haken bei der Animationsoption
     * @param isOn True wenn der Hakten aktiviert werden soll
     */
    public void setAnimation(boolean isOn) {
        this.animationOption.setSelected( isOn );
    }
}
