package tit07.morris.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.JFileChooser;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import tit07.morris.model.ModelControllable;
import tit07.morris.view.ActionCommand;
import tit07.morris.view.ViewControllable;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;


/**
 * Die Klasse MenuHandle wertet die Interaktion des Benutzes mit dem Menü aus
 * und führt entsprechende Operationen durch.
 */
public class MenuHandle implements ActionListener, MenuListener {

    /** Referenz auf das Model */
    private ModelControllable model;

    /** Referenz auf den View */
    private ViewControllable  view;

    /** Referenz auf die Threadverwaltung */
    private ThreadControll    threadControll;


    /**
     * Erzeugt einen neuen Controller für das Menü
     * 
     * @param model Referenz auf das Model
     * @param view Referenz auf den View
     * @param threadControll Referenz auf die Threadverwaltung
     */
    public MenuHandle( ModelControllable model,
                       ViewControllable view,
                       ThreadControll threadControll ) {

        this.model = model;
        this.view = view;
        this.threadControll = threadControll;
    }

    /**
     * Wird automatisch bei Interaktion mit dem Menü aufgerufen
     * 
     * @param e Ausgelöstes ActionEvent
     */
    public void actionPerformed( ActionEvent e ) {

        /* Neues Spiel */
        if( e.getActionCommand().equals( ActionCommand.NEWGAME ) ) {
            this.threadControll.reset();
            this.view.repaintAll();
        }

        /* Spiel fortsetzen/pausieren */
        if( e.getActionCommand().equals( ActionCommand.TOGGLEGAME ) ) {

            if( this.threadControll.isRunning() ) {
                view.setRunning( false );
                this.threadControll.pause();
            }
            else {
                view.setRunning( true );
                this.threadControll.resume( true );
            }
        }

        /* Spiel laden */
        else if( e.getActionCommand().equals( ActionCommand.LOADGAME ) ) {
            this.threadControll.interrupt();
            String filename = getAbsolutePath( e.getActionCommand() );

            try {
                if( !( filename.equals( "" ) ) ) { //$NON-NLS-1$
                    File inFile = new File( filename );
                    this.model.loadGame( inFile );
                }
            }
            catch( IOException e1 ) {
                this.view.showErrorMsg( Messages.getString("MenuHandle.loading_not_suc") //$NON-NLS-1$
                        + e1.getMessage() );
            }
            finally {
                this.threadControll.start();
            }
        }

        /* Spiel speichern */
        else if( e.getActionCommand().equals( ActionCommand.SAVEGAME ) ) {
            this.threadControll.interrupt();
            String filename = getAbsolutePath( e.getActionCommand() );
            try {
                if( !( filename.equals( "" ) ) ) { //$NON-NLS-1$
                    File outFile = new File( filename );
                    this.model.saveGame( outFile );
                }
            }
            catch( IOException e1 ) {
                this.view.showErrorMsg( Messages.getString("MenuHandle.save_not_suc") //$NON-NLS-1$
                        + e1.getMessage() );
            }
            finally {
                this.threadControll.start();
            }
        }

        /* Spiel beenden */
        else if( e.getActionCommand().equals( ActionCommand.QUITGAME ) ) {
            this.threadControll.interrupt();
            WindowHandle.saveWindowSize( model, view );
            System.exit( 0 );
        }

        /* Spieloptionen */
        else if( e.getActionCommand().equals( ActionCommand.GRAPHIC_OPTION )
                || e.getActionCommand().equals( ActionCommand.PLAYER_OPTION ) ) {

            this.threadControll.interrupt();
            this.view.openOptionMenu( e.getActionCommand() );
            this.threadControll.start();
        }

        /* Checkbox Sound betätigt */
        else if( e.getActionCommand().equals( ActionCommand.TOGGLE_SOUND ) ) {

            this.model.getConfig().setSoundOn( view.getSoundOption() );
            try {
                this.model.getConfig().saveToDisk();
            }
            catch( IOException e2 ) {
                String msg = Messages.getString("MenuHandle.config_not_saved"); //$NON-NLS-1$
                view.showErrorMsg( msg );
            }
        }

        /* Checkbox Animation betätigt */
        else if( e.getActionCommand().equals( ActionCommand.TOGGLE_ANIMATION ) ) {

            this.model.getConfig().setAnimationOn( view.getAnimationOption() );
            try {
                this.model.getConfig().saveToDisk();
            }
            catch( IOException e2 ) {
                String msg = Messages.getString("MenuHandle.config_not_saved"); //$NON-NLS-1$
                view.showErrorMsg( msg );
            }
        }

        /* Hilfe anzeigen */
        else if( e.getActionCommand().equals( ActionCommand.HELP ) ) {
            try {
                this.launchBrowser( model.getConfig().getLocalHelpURL() );
            }
            catch( MalformedURLException e1 ) {
                String msg = Messages.getString("MenuHandle.url_wrong"); //$NON-NLS-1$
                this.view.showErrorMsg( msg );
            }
        }

        /* Infobox zeigen */
        else if( e.getActionCommand().equals( ActionCommand.INFO ) ) {
            this.threadControll.interrupt();
            view.showInfoMsg( model.getConfig().getTitle()
                    + " - " //$NON-NLS-1$
                    + model.getConfig().getVersion()
                    + "\n" //$NON-NLS-1$
                    + Messages.getString("MenuHandle.authors") //$NON-NLS-1$
                    + "\n\n" //$NON-NLS-1$
                    + Messages.getString("MenuHandle.copyright") //$NON-NLS-1$
                    + "\n" //$NON-NLS-1$
                    + Messages.getString("MenuHandle.further_license") //$NON-NLS-1$
                    + "\n"); //$NON-NLS-1$
            this.threadControll.start();
        }
    }

    /**
     * Öffnet das Dateiauswahlmenü
     * 
     * @param command Gibt ab, ob das Spiel geladen oder gespeichert werden soll
     * @return Ausgewählten absoluten Pfad
     */
    private String getAbsolutePath( String command ) {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter( Messages.getString("MenuHandle.savegames"), //$NON-NLS-1$
                                                                      model
                                                                           .getConfig()
                                                                           .getSavegameExtension() );
        chooser.setFileFilter( filter );
        if( command.equals( ActionCommand.SAVEGAME ) ) {
            int returnVal = chooser.showSaveDialog( (Component) view );
            if( returnVal == JFileChooser.APPROVE_OPTION ) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if( path.endsWith("." //$NON-NLS-1$
                        + model.getConfig().getSavegameExtension() ) ) {
                    return path;
                }
                return path + "." + model.getConfig().getSavegameExtension(); //$NON-NLS-1$
            }
        }
        else if( command.equals( ActionCommand.LOADGAME ) ) {
            int returnVal = chooser.showDialog( (Component) view, Messages.getString("MenuHandle.loading") ); //$NON-NLS-1$
            if( returnVal == JFileChooser.APPROVE_OPTION ) {
                return chooser.getSelectedFile().getAbsolutePath();
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Öffnet den Standardbrowser mit der übergebenen URL
     * 
     * @param url Zu öffnende URL
     */
    private void launchBrowser( String url ) {

        try {

            BrowserLauncher launcher = new BrowserLauncher();
            launcher.openURLinBrowser( url );
        }
        catch( BrowserLaunchingInitializingException e ) {
            String msg = Messages.getString("MenuHandle.brower_not_init"); //$NON-NLS-1$
            view.showErrorMsg( msg );
        }
        catch( UnsupportedOperatingSystemException e ) {
            String msg = Messages.getString("MenuHandle.browerstart_unsupported"); //$NON-NLS-1$
            view.showErrorMsg( msg );
        }
    }

    /**
     * Wird aufgerufen, wenn das Menü ausgewählt wurde
     * 
     * @param arg0 Ausgelöstes MenuEvent
     */
    @Override
    public void menuSelected( MenuEvent arg0 ) {

        this.threadControll.interrupt();
    }

    /**
     * Wird aufgerufen, wenn das Menü nicht mehr ausgewählt ist
     * 
     * @param arg0 Ausgelöstes MenuEvent
     */
    @Override
    public void menuDeselected( MenuEvent arg0 ) {

        this.threadControll.start();
    }

    /**
     * Wird bei Abbruch im Menü aufgerufen
     * 
     * @param arg0 Ausgelöstes MenuEvent
     */
    @Override
    public void menuCanceled( MenuEvent arg0 ) {

    }
}
