package tit07.morris.controller.ai;

import java.util.ArrayList;
import tit07.morris.exception.IllegalColorException;
import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * Klasse zur Berechnung aller möglichen Spielzüge anhand einer gegebenen
 * Spielsituation.
 */
public class AIPossibleMoves {

    /** Art des nächsten Zuges (setzen, ziehen, springen, entfernen) */
    private State               nextAction     = null;

    /** Farbe des Spielers, welcher den nächsten Zug ausführt */
    private StoneColor          activeColor    = null;

    /** Alle möglichen Zugpositionen auf dem Spielbrett */
    private ArrayList <Integer> toMove         = null;

    /** Bei ziehen und springen alle möglichen Ausgangspositionen */
    private ArrayList <Integer> fromMove       = null;

    /** Speichert, ob bei einem Zug eine Mühle geschlossen wurde */
    private ArrayList <Boolean> isMorrisClosed = null;


    /**
     * Erzeugt eine neue Instanz und berechnet dafür alle möglichen Spielzüge
     * 
     * @param game Referenz auf das aktuelle Spiel
     */
    public AIPossibleMoves( ModelAIInterface game ) {

        /* Initialisierung der Variablen */
        this.fromMove = new ArrayList <Integer>();
        this.toMove = new ArrayList <Integer>();
        this.isMorrisClosed = new ArrayList <Boolean>();
        if( game == null ) {
            return;
        }

        this.activeColor = this.getActiveColor( game );
        try {
            if( this.activeColor instanceof StoneColor ) {
                this.nextAction = game.getState( this.activeColor );

                /* Alle möglichen Züge berechnen */
                this.calcPossibleMoves( game, this.nextAction );

                /* Zugsortierung */
                boolean isMoving = ( this.nextAction == State.MOVE || this.nextAction == State.JUMP );
                if( this.nextAction != State.REMOVE ) {
                    this.sortPossibleMoves( isMoving );
                }
            }
        }
        catch( IllegalColorException e ) {
        }
    }

    /**
     * Berechnet die Farbe des Spielers, welcher am Zug ist
     * 
     * @param game Referenz auf das aktuelle Spiel
     * @return Farbe des aktiven Spielers
     */
    private StoneColor getActiveColor( ModelAIInterface game ) {

        try {
            if( game.isItTurnOfColor( StoneColor.WHITE ) ) {
                return StoneColor.WHITE;
            }
            else if( game.isItTurnOfColor( StoneColor.BLACK ) ) {
                return StoneColor.BLACK;
            }
        }
        catch( IllegalColorException e ) {
        }
        return null;
    }

    /**
     * Berechnet alle möglichen Spielzüge
     * 
     * @param border Referenz auf das aktuelle Spielbrett
     * @param action Art des nächstes Spielzuges
     */
    private void calcPossibleMoves( ModelAIInterface border, State action ) {

        if( action != State.SET
                && action != State.MOVE
                && action != State.JUMP
                && action != State.REMOVE ) {
            return;
        }

        try {
            switch( action ) {
            /*
             * Spielzug: SETZEN Bedingung: freies Feld
             */
            case SET:
                for( int i = 1; i <= 24; i++ ) {
                    if( border.getStoneColor( i ) == StoneColor.NONE ) {
                        this.toMove.add( new Integer( i ) );
                        border.setStoneColor( i, this.activeColor );
                        this.isMorrisClosed.add( border.isStoneInMill( i ) );
                        border.setStoneColor( i, StoneColor.NONE );
                    }
                }
                break;
            /*
             * Spielzug: ZIEHEN Bedingung: Eigener Stein auf Ausgangsfeld und
             * freies Nachbarfeld
             */
            case MOVE:
                for( int from = 1; from <= 24; from++ ) {
                    if( border.getStoneColor( from ) != this.activeColor ) {
                        continue;
                    }
                    for( int to = 1; to <= 24; to++ ) {
                        if( border.isNeighbour( from, to )
                                && border.getStoneColor( to ) == StoneColor.NONE ) {

                            this.fromMove.add( from );
                            this.toMove.add( to );
                            border.setStoneColor( to, this.activeColor );
                            this.isMorrisClosed
                                               .add( border
                                                           .isStoneInMill( from ) );
                            border.setStoneColor( to, StoneColor.NONE );
                        }
                    }
                }
                break;
            /*
             * Spielzug: SPRINGEN Bedingung: Eigener Stein auf Ausgangsfeld und
             * freies Zielfeld
             */
            case JUMP:
                for( int i = 1; i <= 24; i++ ) {
                    if( border.getStoneColor( i ) != this.activeColor ) {
                        continue;
                    }
                    for( int j = 1; j <= 24; j++ ) {
                        if( border.getStoneColor( j ) == StoneColor.NONE ) {
                            this.fromMove.add( i );
                            this.toMove.add( j );
                            border.setStoneColor( j, this.activeColor );
                            this.isMorrisClosed.add( border.isStoneInMill( i ) );
                            border.setStoneColor( j, StoneColor.NONE );
                        }
                    }
                }
                break;
            /*
             * Spielzug: ENTFERNEN Bedingung: Gegnerischer Stein auf Position
             * und nicht in einer Mühle oder gegnerischer Stein in Mühle und
             * keine freien Steine
             */
            case REMOVE:
                StoneColor removeColor = StoneColor.NONE;
                removeColor = ( this.activeColor == StoneColor.WHITE ) ? StoneColor.BLACK
                                                                      : StoneColor.WHITE;
                for( int i = 1; i <= 24; i++ ) {
                    StoneColor currentColor = border.getStoneColor( i );
                    if( !border.isStoneInMill( i )
                            && currentColor == removeColor ) {
                        this.toMove.add( i );
                    }
                    else if( border.isStoneInMill( i )
                            && border.getNumberOfFreeStones( removeColor ) == 0
                            && currentColor == removeColor ) {

                        this.toMove.add( i );
                    }
                }
                break;
            }
        }
        catch( Exception e ) {
            return;
        }
    }

    /**
     * Sortiert die möglichen Spielzüge nach dem Schema, dass Züge welche zur
     * einer Mühle führen den kleinsten Index haben.
     * 
     * @param isMoving Gibt an, ob die Ausgangsposition mit sortiert werden
     */
    private void sortPossibleMoves( boolean isMoving ) {

        ArrayList <Integer> sortedTo = new ArrayList <Integer>();
        ArrayList <Integer> sortedFrom = new ArrayList <Integer>();

        /* Züge, welche zu einer Mühle führen */
        for( int i = 0; i < isMorrisClosed.size(); i++ ) {
            if( isMorrisClosed.get( i ) ) {
                sortedTo.add( toMove.get( i ) );
                if( isMoving ) {
                    sortedFrom.add( fromMove.get( i ) );
                }
            }
        }
        /* Züge, welche nicht zu einer Mühle führen */
        for( int i = 0; i < isMorrisClosed.size(); i++ ) {
            if( !isMorrisClosed.get( i ) ) {
                sortedTo.add( toMove.get( i ) );
                if( isMoving ) {
                    sortedFrom.add( fromMove.get( i ) );
                }
            }
        }
        /* Referenzen erneuern */
        this.toMove = sortedTo;
        this.fromMove = sortedFrom;
    }

    /**
     * Gibt die Anzahl der möglichen Spielzüge zurück
     * 
     * @return Anzahl der möglichen Spielzüge
     */
    public int getNumberOfMoves() {

        return this.toMove.size();
    }

    /**
     * Gibt die Farbe des aktiven Spielers zurück
     * 
     * @return Farbe des aktiven Spielers
     */
    public StoneColor getActiveColor() {

        return this.activeColor;
    }

    /**
     * Gibt die Aktion des aktiven Spielers zurück
     * 
     * @return Aktion des aktiven Spielers
     */
    public State getAction() {

        return this.nextAction;
    }

    /**
     * Gibt die Ausgangspositionen aller möglichen Spielzüge zurück
     * 
     * @return Ausgangspositionen aller möglichen Spielzüge
     */
    public ArrayList <Integer> getFrom() {

        return this.fromMove;
    }

    /**
     * Gibt die Positionen aller möglichen Spielzüge zurück
     * 
     * @return Positionen aller möglichen Spielzüge
     */
    public ArrayList <Integer> getTo() {

        return this.toMove;
    }
}
