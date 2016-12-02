package tit07.morris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MenuEvent;
import tit07.morris.controller.ai.AIControllable;
import tit07.morris.model.MorrisGame;
import tit07.morris.view.MainWindow;
import tit07.morris.view.Reactable;


/**
 * Die Klasse Controller erzeugt das Model und den View. Sie stellt au�erdem
 * Methoden zur Steuerung des Spielablaufs zur Verf�gung und implementiert die
 * Schnittstelle zur k�nstlichen Intelligenz
 */
public class Controller implements Reactable, AIControllable {

    /** Wertet Men�-Interaktionen aus */
    private MenuHandle     menuHandle;

    /** Wertet Spiel-Interaktionen aus */
    private GameHandle     gameHandle;

    /** Wertet das Interaktionen mit dem Optionsmen� aus */
    private OptionHandle   optionHandle;

    /** Wertet Ver�nderungen im Optionsmen� aus */
    private ChangeHandle   changeHandle;

    /** Wertet Fensterereignisse aus */
    private WindowHandle   windowHandle;

    /** Verwaltet die einzelnen Threads */
    private ThreadControll threadControll;


    /**
     * Erzeugt alle Objekte, welche f�r den Spielablauf n�tig sind und
     * initialisiert diese f�r ein neues Spiel.
     */
    public Controller() {

        /** Erzeugt das Model */
        MorrisGame model = new MorrisGame();

        /** Erzeugt den View */
        MainWindow view = new MainWindow( model,
                                          this );

        /** Meldet den View am Model an */
        model.addObserver( view );

        /** Erzeugt die Threadverwaltung */
        this.threadControll = new ThreadControll( view,
                                                  model,
                                                  this );

        /** Men�handle erzeugen */
        this.menuHandle = new MenuHandle( model,
                                          view,
                                          threadControll );

        /** GameHandle erzeugen */
        this.gameHandle = new GameHandle( model,
                                          view,
                                          threadControll );

        /** OptionHandle erzeugen */
        this.optionHandle = new OptionHandle( model,
                                              view );

        /** WindowHandle erzeugen */
        this.windowHandle = new WindowHandle( model,
                                              view );

        /** ChangeHandle erzeugen */
        this.changeHandle = new ChangeHandle( view );

        /** Zeichenthread starten */
        this.threadControll.resume( false );

        /** Hauptfenster sichtbar machen */
        view.setVisible( true );
    }

    /**
     * Startet den KI-Thread
     */
    @Override
    public void start() {

        this.gameHandle.getAIThread().start();
    }

    /**
     * Stoppt den KI-Thread
     */
    @Override
    public void stop() {

        this.gameHandle.getAIThread().stop();
    }

    /**
     * Unterbricht das Spiel, wenn das Men� selektiert wurde.
     * 
     * @param arg0 Ausgel�stes MenuEvent
     */
    @Override
    public void menuSelected( MenuEvent arg0 ) {

        this.menuHandle.menuSelected( arg0 );
    }

    /**
     * Setzt das Spiel fort, wenn das Men� deselektiert wurde
     * 
     * @param arg0 Ausgel�stes MenuEvent
     */
    @Override
    public void menuDeselected( MenuEvent arg0 ) {

        this.menuHandle.menuDeselected( arg0 );
    }

    /**
     * Wird bei Abbruch im Men� aufgerufen
     * 
     * @param arg0 Ausgel�stes MenuEvent
     */
    @Override
    public void menuCanceled( MenuEvent arg0 ) {

        this.menuHandle.menuCanceled( arg0 );
    }

    /**
     * Wird bei Ver�nderungen im Optionsmen� aufgerufen
     * 
     * @param arg0 Ausgel�stes ChangeEvent
     */
    @Override
    public void stateChanged( ChangeEvent arg0 ) {

        this.changeHandle.stateChanged( arg0 );
    }

    /**
     * Wird bei Ver�nderungen im Textfeld (Optionsmen�) aufgerufen
     * 
     * @param arg0 Ausgel�stes DocumentEvent
     */
    @Override
    public void changedUpdate( DocumentEvent arg0 ) {

        this.changeHandle.changedUpdate( arg0 );
    }

    /**
     * Wird bei Ver�nderungen im Textfeld (Optionsmen�) aufgerufen
     * 
     * @param arg0 Ausgel�stes DocumentEvent
     */
    @Override
    public void insertUpdate( DocumentEvent arg0 ) {

        this.changeHandle.insertUpdate( arg0 );
    }

    /**
     * Wird bei Ver�nderungen im Textfeld (Optionsmen�) aufgerufen
     * 
     * @param arg0 Ausgel�stes DocumentEvent
     */
    @Override
    public void removeUpdate( DocumentEvent arg0 ) {

        this.changeHandle.removeUpdate( arg0 );
    }

    /**
     * Verarbeitet Mausklicks auf dem Spielfeld
     * 
     * @param arg0 Ausgel�stes MouseEvent
     */
    @Override
    public void mouseClicked( MouseEvent arg0 ) {

        this.gameHandle.mouseClicked( arg0 );
    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param arg0 Ausgel�stes MouseEvent
     */
    @Override
    public void mouseEntered( MouseEvent arg0 ) {

        this.gameHandle.mouseEntered( arg0 );

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param arg0 Ausgel�stes MouseEvent
     */
    @Override
    public void mouseExited( MouseEvent arg0 ) {

        this.gameHandle.mouseExited( arg0 );

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param arg0 Ausgel�stes MouseEvent
     */
    @Override
    public void mousePressed( MouseEvent arg0 ) {

        this.gameHandle.mousePressed( arg0 );

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param arg0 Ausgel�stes MouseEvent
     */
    @Override
    public void mouseReleased( MouseEvent arg0 ) {

        this.gameHandle.mouseReleased( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes ComponentEvent
     */
    @Override
    public void componentHidden( ComponentEvent arg0 ) {

        this.windowHandle.componentHidden( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes ComponentEvent
     */
    @Override
    public void componentMoved( ComponentEvent arg0 ) {

        this.windowHandle.componentMoved( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes ComponentEvent
     */
    @Override
    public void componentResized( ComponentEvent arg0 ) {

        this.windowHandle.componentResized( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes ComponentEvent
     */
    @Override
    public void componentShown( ComponentEvent arg0 ) {

        this.windowHandle.componentShown( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowActivated( WindowEvent arg0 ) {

        this.windowHandle.windowActivated( arg0 );

    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowClosed( WindowEvent arg0 ) {

        this.windowHandle.windowClosed( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowClosing( WindowEvent arg0 ) {

        this.windowHandle.windowClosing( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowDeactivated( WindowEvent arg0 ) {

        this.windowHandle.windowDeactivated( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowDeiconified( WindowEvent arg0 ) {

        this.windowHandle.windowDeiconified( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowIconified( WindowEvent arg0 ) {

        this.windowHandle.windowIconified( arg0 );
    }

    /**
     * Wird bei Ver�nderungen des Hauptfensters aufgerufen
     * 
     * @param arg0 Ausgel�stes WindowEvent
     */
    @Override
    public void windowOpened( WindowEvent arg0 ) {

        this.windowHandle.windowOpened( arg0 );
    }

    /**
     * Wird bei Aktionen im Hauptmen� oder Optionsmen� aufgerufen
     * 
     * @param arg0 Ausgel�stes ActionEvent
     */
    @Override
    public void actionPerformed( ActionEvent arg0 ) {

        this.menuHandle.actionPerformed( arg0 );
        this.optionHandle.actionPerformed( arg0 );
        this.changeHandle.actionPerformed( arg0 );
    }
}
