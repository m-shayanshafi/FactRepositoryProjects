package tit07.morris.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import tit07.morris.controller.ai.AIThread;
import tit07.morris.controller.ai.Playable;
import tit07.morris.exception.IllegalColorException;
import tit07.morris.exception.IllegalMoveException;
import tit07.morris.exception.IllegalPositionException;
import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.ModelControllable;
import tit07.morris.model.StoneColor;
import tit07.morris.model.config.PlayerInput;
import tit07.morris.view.ViewControllable;


/**
 * Die Klasse GameHandle wertet die Interaktion des Benutzers mit dem Spielfeld
 * aus und führt entsprechende Operationen durch.
 */
public class GameHandle implements MouseListener, Playable {

    /** Referenz auf das Model */
    private ModelControllable model;

    /** Referenz auf den View */
    private ViewControllable  view;

    /** Referenz auf den KI-Thread */
    private AIThread          aiThread;

    /** Speicher für die Startposition beim Bewegen eines Steins */
    private int               fromPosition;

    /** Referenz auf die Threadverwaltung */
    private ThreadControll    threadControll;

    /** Gibt an, ob momentan ein Zug ausgeführt wird */
    private boolean           isMoving      = false;

    /** Gibt an, ob ein menschlicher Spieler momentan einen Zug ausführt */
    private boolean           isHumanMoving = false;


    /**
     * Erzeugt einen neuen GameHandle Controller
     * 
     * @param model Referenz auf das Modell
     * @param view Referenz auf den View
     * @param threadControll Referenz auf die Threadverwaltung
     */
    public GameHandle( ModelControllable model,
                       ViewControllable view,
                       ThreadControll threadControll ) {

        /* Referenzen setzen */
        this.model = model;
        this.view = view;
        this.threadControll = threadControll;
        this.aiThread = new AIThread( (ModelAIInterface) model,
                                      view,
                                      this );

        /* Ausgangsposition zurücksetzen */
        this.fromPosition = 0;
    }

    /**
     * Gibt die Referenz des KI-Threads zurück
     * 
     * @return Referenz des KI-Threads
     */
    public AIThread getAIThread() {

        return this.aiThread;
    }

    /**
     * Wird automatisch aufgerufen beim Mausklick auf das Spielfeld.
     * 
     * @param e Das ausgelöste MouseEvent
     */
    public void mouseClicked( MouseEvent e ) {

        this.isHumanMoving = true;
        // System.out.println( "Klick at: " + e.getX() + " " + e.getY() );

        /* Koordinaten in Position umrechnen */
        int position = this.view.coordiantes2Position( e.getX(), e.getY() );

        /* Klick ins Nirnvana oder bei laufender Animation ignorieren */
        if( position != 0 && !this.view.isAnimationRunning() && this.threadControll.isRunning() ) {
            this.prepareHumanMove( position );
        }
        this.isHumanMoving = false;
    }

    /**
     * Bereitet den Zug eines menschlichen Spielers vor
     */
    private void prepareHumanMove( int position ) {

        try {
            StoneColor stoneColor = StoneColor.NONE;

            /* Abfrage, ob weißer Spieler (Mensch) zugberechtigt ist */
            if( this.model.getConfig().getWhiteInput() == PlayerInput.HUMAN
                    && this.model.isItTurnOfColor( StoneColor.WHITE ) ) {

                stoneColor = StoneColor.WHITE;
            }
            /* Abfrage, ob schwarzer Spieler (Mensch) zugberechtigt ist */
            if( this.model.getConfig().getBlackInput() == PlayerInput.HUMAN
                    && this.model.isItTurnOfColor( StoneColor.BLACK ) ) {

                stoneColor = StoneColor.BLACK;
            }

            /* Führe Spielzug für Spieler aus */
            if( stoneColor == StoneColor.WHITE
                    || stoneColor == StoneColor.BLACK ) {
                this.performMove( position,
                                  this.fromPosition,
                                  stoneColor,
                                  false );

                /* KI antriggern falls sie noch inaktiv ist */
                if( !this.threadControll.isAIRunning() ) {
                    this.threadControll.interrupt();
                    this.threadControll.start( true );
                }
            }
        }
        catch( IllegalColorException e ) {
        }
    }

    /**
     * Führe entsprechenden Spielzug aus
     * 
     * @param toPosition Zielposition
     * @param fromPosition Ausgangsposition
     * @param stoneColor Farbe des aktiven Spielers
     * @param isAIMove True, wenn der Spielzug von der KI ausgeführt wird
     */
    @Override
    public synchronized void performMove( int toPosition,
                                          int fromPosition,
                                          StoneColor stoneColor,
                                          boolean isAIMove ) {

        this.isMoving = true;
        this.view.setStatusLine( " " ); //$NON-NLS-1$
        this.view.unhideAllStones();
        try {
            try {
                switch( model.getState( stoneColor ) ) {

                /* Stein setzen */
                case SET:
                    this.view.hidePosition( true, toPosition );
                    this.model.setStone( toPosition );
                    this.view.initSetAnimation( toPosition );
                    break;

                /* Stein bewegen (mindestens zwei Events nötig für Mensch) */
                case JUMP:
                case MOVE:
                    /* KI-Zug: bewegen */
                    if( isAIMove ) {
                        this.view.hidePosition( true, toPosition );
                        this.model.moveStone( fromPosition, toPosition );
                        this.view.initMoveAnimation( fromPosition, toPosition );
                        this.fromPosition = 0;
                    }
                    /* Ausgangsposition markieren */
                    else if( this.fromPosition == 0
                            && model.getStoneColor( toPosition ) == stoneColor ) {
                        this.fromPosition = toPosition;
                        this.view.markPosition( true, toPosition );
                    }
                    /* Markierung aufheben */
                    else if( this.fromPosition != 0
                            && this.fromPosition == toPosition ) {
                        this.view.markPosition( false, this.fromPosition );
                        this.fromPosition = 0;
                    }
                    /* Stein bewegen */
                    else if( this.fromPosition != 0 ) {
                        this.view.hidePosition( true, toPosition );
                        this.view.markPosition( false, this.fromPosition );
                        this.model.moveStone( this.fromPosition, toPosition );
                        this.view.initMoveAnimation( this.fromPosition,
                                                     toPosition );
                        this.fromPosition = 0;
                    }
                    break;

                /* Stein entfernen */
                case REMOVE:
                    StoneColor color = model.getStoneColor( toPosition );
                    this.view.hidePosition( true, toPosition );
                    this.model.removeStone( toPosition );
                    this.view.initRemoveAnimation( toPosition, color );
                    break;
                }
            }
            catch( IllegalMoveException illegalMove ) {
                this.fromPosition = 0;
                this.view.hidePosition( false, toPosition );
                this.view.setStatusLine( Messages.getString("GameHandle.forbidden_move") //$NON-NLS-1$
                        + illegalMove.getMessage() );
            }
        }
        catch( IllegalPositionException e ) {
            if( isAIMove ) {
                this.view.setStatusLine( Messages.getString("GameHandle.ai_forbidden_move") ); //$NON-NLS-1$
            }
        }
        catch( IllegalColorException e ) {
        }
        this.isMoving = false;
    }

    /**
     * Gibt an, ob Momentan ein Spielzug ausgeführt wird
     * 
     * @return True, wenn momentan ein Spielzug ausgeführt wird
     */
    @Override
    public boolean isMoving() {

        return ( this.isMoving || this.isHumanMoving );
    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param e Ausgelöstes MouseEvent
     */
    @Override
    public void mouseEntered( MouseEvent e ) {

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param e Ausgelöstes MouseEvent
     */
    @Override
    public void mouseExited( MouseEvent e ) {

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param e Ausgelöstes MouseEvent
     */
    @Override
    public void mousePressed( MouseEvent e ) {

    }

    /**
     * Wird bei Mausaktionen auf dem Spielfeld aufgerufen
     * 
     * @param e Ausgelöstes MouseEvent
     */
    @Override
    public void mouseReleased( MouseEvent e ) {

    }
}
