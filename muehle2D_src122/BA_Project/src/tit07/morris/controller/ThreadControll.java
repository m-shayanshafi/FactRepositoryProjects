package tit07.morris.controller;

import tit07.morris.controller.ai.AIControllable;
import tit07.morris.model.ModelControllable;
import tit07.morris.view.ViewControllable;


/**
 * Die Klasse GameControll steuert den Ablauf des Spiels. Sie übernimmt die
 * Verwaltung der Threads und ermöglicht die Unterbrechung, Fortsetzung und
 * Zurücksetzung des Spiels.
 */
public class ThreadControll {

    /** Referenz auf das Model */
    private ModelControllable model;

    /** Referenz auf den View */
    private ViewControllable  view;

    /** Referenz auf den AI-Controller */
    private AIControllable    controller;

    /** Gibt an, ob vor der Unterbrechung die KI aktiviert war */
    private boolean           wasAIRunning = false;

    /** Gibt an, ob das Spiel läuft */
    private boolean           isRunning    = false;

    /** Gibt an, ob der AI-Thread läuft */
    private boolean           isAIRunning  = false;


    /**
     * Erzeugt eine neue Instanz der Spielverwaltung
     * 
     * @param view Referenz auf den View
     * @param model Referenz auf das Model
     * @param controller Referenz auf den Controller
     */
    public ThreadControll( ViewControllable view,
                           ModelControllable model,
                           AIControllable controller ) {

        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    /**
     * Gibt an, ob das Spiel läuft
     * 
     * @return True, wenn das Spiel läuft, ansonsten false.
     */
    public boolean isRunning() {

        return this.isRunning;
    }

    /**
     * Gibt an, ob der KI-Thread läuft
     * 
     * @return True, wenn er KI-Thread läuft, ansonsten false
     */
    public boolean isAIRunning() {

        return this.isAIRunning;
    }

    /**
     * Pausiert das Spiel. Das Spiel muss dann mit Resume fortgesetzt werden.
     */
    public void pause() {

        this.isRunning = false;
        this.view.setStatusLine( Messages.getString("ThreadControll.game_paused") ); //$NON-NLS-1$
        this.interrupt();
        this.view.repaintAll();
    }

    /**
     * Setzt ein pausiertes oder inaktives Spiel fort
     * 
     * @param startAI True, wenn die KI gestartet werden soll, ansonsten false.
     */
    public void resume( boolean startAI ) {

        this.isRunning = true;
        this.view.setStatusLine( "" ); //$NON-NLS-1$
        this.start( startAI );
    }

    /**
     * Startet ein das Spiel. Die KI wird dabei nur gestartet, wenn sie vor der
     * Unterbrechung aktiv war.
     */
    public void start() {

        this.start( this.wasAIRunning );
    }

    /**
     * Startet das Spiel. Das Spiel wird nur gestartet, wenn es nicht pausiert
     * ist.
     * 
     * @param startAI True, wenn die KI gestartet werden soll, ansonsten false.
     */
    public void start( boolean startAI ) {

        if( this.isRunning ) {
            this.wasAIRunning = startAI;
            this.view.startDrawing();
            if( startAI ) {
                isAIRunning = true;
                controller.start();
            }
        }
    }

    /**
     * Unterbricht das Spiel. Es kann mit start wieder fortgesetzt werden.
     */
    public void interrupt() {

        this.view.stopDrawing();
        isAIRunning = false;
        this.controller.stop();
    }

    /**
     * Setzt das Spiel zurück und startet ein neues Spiel mit aktivierter KI
     */
    public void reset() {

        this.interrupt();
        this.view.resetView();
        this.model.resetGame();
        try {
            Thread.sleep( 100 );
        }
        catch( Exception e ) {
        }
       
        this.start( true );
    }
}
