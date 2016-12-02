package tit07.morris.controller;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import tit07.morris.model.ModelControllable;
import tit07.morris.view.ViewControllable;


/**
 * Die Klasse WindowHandle reagiert auf Fensterereignisse des Hauptfensters.
 */
public class WindowHandle implements ComponentListener, WindowListener {

    /** Referenz auf den View */
    private ViewControllable  view;

    /** Referenz auf das Model */
    private ModelControllable model;


    /**
     * Erzeugt eine neue Instanz des WindowHandlers
     * 
     * @param model Referenz auf das Model
     * @param view Referenz auf den View
     */
    public WindowHandle( ModelControllable model, ViewControllable view ) {

        this.model = model;
        this.view = view;
    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes ComponentEvent
     */
    @Override
    public void componentHidden( ComponentEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes ComponentEvent
     */
    @Override
    public void componentMoved( ComponentEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes ComponentEvent
     */
    @Override
    public void componentResized( ComponentEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes ComponentEvent
     */
    @Override
    public void componentShown( ComponentEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowActivated( WindowEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowClosed( WindowEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowClosing( WindowEvent arg0 ) {

        saveWindowSize( this.model, this.view );
    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowDeactivated( WindowEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowDeiconified( WindowEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowIconified( WindowEvent arg0 ) {

    }

    /**
     * Wird bei Veränderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgelöstes WindowEvent
     */
    @Override
    public void windowOpened( WindowEvent arg0 ) {

    }

    /**
     * Speichert die Fenstergröße, bevor das Programm beendet wird.
     * 
     * @param model Referenz auf das Model
     * @param view Referenz auf den View
     */
    public static void saveWindowSize( ModelControllable model,
                                       ViewControllable view ) {

        model.getConfig().setXSize( view.getWidth() );
        model.getConfig().setYSize( view.getHeight() );
        try {
            model.getConfig().saveToDisk();
        }
        catch( IOException e ) {
        }
    }
}
