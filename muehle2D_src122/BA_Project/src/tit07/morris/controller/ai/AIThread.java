package tit07.morris.controller.ai;

import tit07.morris.exception.IllegalColorException;
import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.StoneColor;
import tit07.morris.model.config.PlayerInput;
import tit07.morris.view.ViewControllable;


/**
 * Die Klasse AIThread implementiert einen Thread, welcher fortlaufend
 * überprüft, ob ein Spielzug der KI vom Spiel gefordert wird. Ist dies der
 * Fall, so wird die Aufgabe den entsprechenden Spielzug zu finden an eine
 * Klasse, welche Playable implementiert weitergeleitet.
 */
public class AIThread implements Runnable, AIControllable {

    /** Referenz auf das Model */
    private ModelAIInterface model;

    /** Referenz auf den View */
    private ViewControllable view;

    /** Referenz auf den Controller */
    private Playable         gameController;

    /** Referenz zur Verwaltung des Threads */
    private Thread           thread;

    /**
     * Referenz auf die Klasse, welche die Schnittstelle zur Zugberechnung
     * implementiert
     */
    private Calculateable    aiHandle;


    /**
     * Erzeugt eine neue Instanz des AI-Threads und setzt die entsprechenden
     * Referenzen
     * 
     * @param model Referenz auf das Model
     * @param view Referenz auf den View
     * @param controller Referenz auf den Controller zur Steuerung des
     *            Spielfeldes
     */
    public AIThread( ModelAIInterface model,
                     ViewControllable view,
                     Playable controller ) {

        this.model = model;
        this.view = view;
        this.gameController = controller;
        this.aiHandle = AIControll.getInstance();
    }

    /**
     * Abfrage, ob der AIThread aktiv ist.
     * 
     * @return True, wenn der AIThread läuft. Ansonsten wird false zurück
     *         gegeben.
     */
    public boolean isRunning() {

        return ( this.thread instanceof Thread );
    }

    /**
     * Beendet den AIThread falls er aktiv war.
     */
    public void stop() {

        thread = null;
    }

    /**
     * Startet den AIThread
     */
    public void start() {

        thread = new Thread( this,
                             "AI-Thread" ); //$NON-NLS-1$
        thread.setDaemon( true );
        thread.start();
    }

    /**
     * Versucht in einem bestimmten Zeitintervall einen Spielzug der KI
     * auszuführen
     */
    public void run() {

        while( Thread.currentThread() == thread ) {
            performAIMove();
            try {
                Thread.sleep( 500 );
            }
            catch( InterruptedException e ) {
            }
        }
    }

    /**
     * Versucht einen Spielzug der KI auszuführen
     */
    private void performAIMove() {

        /* Keinen Spielzug ausführen, wenn die Animation noch läuft */
        if( view.isAnimationRunning() || gameController.isMoving() ) {
            return;
        }

        try {

            /*
             * Keinen Spielzug ausführen, wenn ein menschlicher Spieler an der
             * Reihe ist
             */
            if( ( model.getConfig().getWhiteInput() == PlayerInput.HUMAN && model
                                                                                 .isItTurnOfColor( StoneColor.WHITE ) )
                    || ( model.getConfig().getBlackInput() == PlayerInput.HUMAN && model
                                                                                        .isItTurnOfColor( StoneColor.BLACK ) )
                    || ( !model.isItTurnOfColor( StoneColor.WHITE ) && !model
                                                                             .isItTurnOfColor( StoneColor.BLACK ) ) ) {
                return;
            }

            /* KI-Spielzug berechnen */
            view.setStatusLine( Messages.getString("AIThread.wait_ai_is_busy") ); //$NON-NLS-1$
            AIMove chosenMove = aiHandle.calculateNextMove( model.clone() );
            if( chosenMove == null ) {
                return;
            }

            /* Berechneten KI-Spielzug ausführen */
            switch( chosenMove.getAction() ) {
            case SET:
                gameController.performMove( chosenMove.getTo(),
                                            0,
                                            chosenMove.getActiveColor(),
                                            true );
                break;
            case MOVE:
                gameController.performMove( chosenMove.getTo(),
                                            chosenMove.getFrom(),
                                            chosenMove.getActiveColor(),
                                            true );
                break;
            case JUMP:
                gameController.performMove( chosenMove.getTo(),
                                            chosenMove.getFrom(),
                                            chosenMove.getActiveColor(),
                                            true );
                break;
            case REMOVE:
                gameController.performMove( chosenMove.getTo(),
                                            0,
                                            chosenMove.getActiveColor(),
                                            true );
                break;
            }
        }
        catch( IllegalColorException e ) {
        }
    }
}
