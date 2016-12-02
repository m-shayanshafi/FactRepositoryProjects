package tit07.morris.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import tit07.morris.exception.IllegalColorException;
import tit07.morris.exception.IllegalMoveException;
import tit07.morris.exception.IllegalPositionException;
import tit07.morris.model.config.GameConfig;


/**
 * Die Klasse MorrisGame implementiert die Spiellogik und dient gleichzeitig als
 * Schnittstelle zur Abfrage und Steuerung des Spiels. Die Spiellogik überprüft,
 * ob die Spielregeln eingehalten werden und wirft bei Verstoß Exceptions.
 * Angemeldete Observer werden bei Veränderungen benachrichtigt.
 */
public class MorrisGame extends Observable implements ModelViewable,
                                          ModelAIInterface, Cloneable {

     //private tit07.morris.controller.Controller controller;

    /** Referenz auf die Spielkonfiguration */
    private GameConfig config      = null;

    /** Referenz auf das Spielbrett */
    private Border     border      = null;

    /** Referenz auf den weißen Spieler */
    private Player     whitePlayer = null;

    /** Referenz auf den schwarzen Spieler */
    private Player     blackPlayer = null;

    /** Referenz auf den MoveLogger */
    private MoveLogger moveLogger  = null;


    /**
     * Erzeugt ein neues Mühlespiel
     */
    public MorrisGame() {

        super();

        /* Erzeugt neue Konfiguration */
        this.config = new GameConfig();

        /* Erzeugt ein neues Spielbrett */
        this.border = new Border();

        /* Erzeugt die Spieler */
        this.whitePlayer = new Player( StoneColor.WHITE );
        this.blackPlayer = new Player( StoneColor.BLACK );

        /* Erzeugt einen neuen MoveLogger */
        this.moveLogger = new MoveLogger( this );

        /* Spieleinstellungen setzen */
        this.resetGame();
    }

    /**
     * Erzeugt ein neues Mühlespiel über vorhandene Parameter
     * 
     * @param config Referenz auf die Spieleconfiguration
     * @param border Referenz auf das Spielbrett
     * @param whitePlayer Referenz auf den weißen Spieler
     * @param blackPlayer Referenz auf den schwarzen Spieler
     */
    private MorrisGame( GameConfig config,
                        Border border,
                        Player whitePlayer,
                        Player blackPlayer ) {

        this.config = config;
        this.border = border;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.moveLogger = new MoveLogger( this );
    }

    /**
     * Gibt ein Handle auf die Spielkonfiguration
     * 
     * @return Referenz auf die Spielekonfiguration
     */
    @Override
    public GameConfig getConfig() {

        return config;
    }

    /*
     * ----- Methoden zur Spielverwaltung -----
     */

    /**
     * Speichert den Spielstand als Datei
     * 
     * @param outFile Zieldatei
     * @throws IOException Wird bei aufgetretenen Fehlern während des
     *             Speichervorgangs geworfen
     */
    @Override
    public void saveGame( File outFile ) throws IOException {

        StoneColor[] stones = new StoneColor[ 24 ];
        for( int i = 1; i <= 24; i++ ) {
            try {
                stones[i - 1] = getStoneColor( i );
            }
            catch( IllegalPositionException e ) {
            }
        }
        FileOutputStream fileOutputSteam = new FileOutputStream( outFile );
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputSteam );
        objectOutputStream.writeObject( whitePlayer.getState() );
        objectOutputStream.writeObject( blackPlayer.getState() );
        Integer stonesWhiteOut = new Integer( whitePlayer.getStonesOut() );
        objectOutputStream.writeObject( stonesWhiteOut );
        Integer stonesBlackOut = new Integer( blackPlayer.getStonesOut() );
        objectOutputStream.writeObject( stonesBlackOut );
        objectOutputStream.writeObject( stones );
        objectOutputStream.close();
        fileOutputSteam.close();
    }

    /**
     * Läd den Spielstand von einer Datei
     * 
     * @param inFile Quelldatei
     * @throws IOException Wird bei aufgetretenen Fehlern während des
     *             LAdevorgangs geworfen
     */
    @Override
    public void loadGame( File inFile ) throws IOException {

        StoneColor[] stones = new StoneColor[ 24 ];
        FileInputStream fileInputStream = new FileInputStream( inFile );
        ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );

        State stateWhitePlayer = null;
        State stateBlackPlayer = null;
        try {
            stateWhitePlayer = (State) objectInputStream.readObject();
            stateBlackPlayer = (State) objectInputStream.readObject();
            this.whitePlayer
                            .setStonesOut( (Integer) objectInputStream
                                                                      .readObject() );
            this.blackPlayer
                            .setStonesOut( (Integer) objectInputStream
                                                                      .readObject() );
            stones = (StoneColor[]) objectInputStream.readObject();
        }
        catch( ClassNotFoundException e ) {
            throw new IOException( Messages.getString("MorrisGame.load_not_suc1") + e.getMessage() ); //$NON-NLS-1$
        }
        objectInputStream.close();
        fileInputStream.close();

        for( int i = 1; i <= 24; i++ ) {
            try {
                this.border.setStoneColor( i, stones[i - 1] );
            }
            catch( IllegalPositionException e ) {
                throw new IOException( Messages.getString("MorrisGame.load_not_suc1") //$NON-NLS-1$
                        + e.getMessage() );
            }
        }
        this.whitePlayer.setState( stateWhitePlayer );
        this.blackPlayer.setState( stateBlackPlayer );
        this.moveLogger.resetLogger();

        /* Observer benachrichten */
        this.setChanged();
        this.notifyObservers( "LOAD" ); //$NON-NLS-1$
    }

    /*
     * ----- Methoden um Spielinformationen abzufragen -----
     */

    /**
     * Gibt die Anzahl der Steine einer Farbe auf dem Spielbrett
     * 
     * @param color Spielsteinfarbe, für welche die Steine gezählt werden sollen
     * @return Anzahl der Steine der Spielsteinfarbe auf dem Spielbrett
     */
    @Override
    public int getStonesIn( StoneColor color ) {

        return border.getNumberOfStones( color );
    }

    /**
     * Gibt die Anzahl der Steine einer Farbe außerhalb des Spielbretts
     * 
     * @param color Spielsteinfarbe, für welche die Steine gezählt werden sollen
     * @return Anzahl der Steine der Spielsteinfarbe außerhalb des Spielbretts
     */
    @Override
    public int getStonesOut( StoneColor color ) {

        return ( color == StoneColor.BLACK ) ? blackPlayer.getStonesOut()
                                            : whitePlayer.getStonesOut();
    }

    /**
     * Gibt den Spielstatus des Spielers mit der übergebenen Spielsteinfarbe
     * zurück
     * 
     * @param color Spielsteinfarbe, für welche der Status abgefragt werden soll
     * @return Status des Spielers mit der übergebenen Spielsteinfarbe
     * @throws IllegalColorException Wird geworfen, wenn color weder schwarz
     *             noch weiß ist
     */
    @Override
    public State getState( StoneColor color ) throws IllegalColorException {

        if( color == StoneColor.WHITE ) {
            return whitePlayer.getState();
        }
        else if( color == StoneColor.BLACK ) {
            return blackPlayer.getState();
        }
        throw new IllegalColorException( "IllegalPlayer: " + color.toString() ); //$NON-NLS-1$
    }

    /**
     * Abfrage der Spielsteinfarbe des Steins auf der übergebenen Position
     * 
     * @param position Position (1-24) des Steins auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    @Override
    public StoneColor getStoneColor( int position )
                                                   throws IllegalPositionException {

        return border.getStoneColor( position );
    }

    /**
     * Fragt ab, ob der übergebene Spieler am Zug ist
     * 
     * @param color Steinfarbe eines Spielers (schwarz oder weiß)
     * @return True, wenn der Spieler am Zug ist
     */
    @Override
    public boolean isItTurnOfColor( StoneColor color )
                                                      throws IllegalColorException {

        if( ( getState( color ) == State.SET
                || getState( color ) == State.MOVE
                || getState( color ) == State.JUMP || getState( color ) == State.REMOVE ) ) {
            return true;
        }
        return false;
    }

    /*
     * ----- Methoden um Spiel zu beeinflussen -----
     */

    /**
     * Setzt das Spiel in den Anfangszustand zurück
     */
    @Override
    public void resetGame() {

        /* Steine auf dem Brett zurücksetzen */
        this.border.resetBoard();

        /* Weiß am Zug */
        this.whitePlayer.setState( State.SET );
        this.blackPlayer.setState( State.WAIT );

        /* Steine setzen */
        this.whitePlayer.setStonesOut( config.INIT_STONES_WHITE );
        this.blackPlayer.setStonesOut( config.INIT_STONES_BLACK );
        this.moveLogger.resetLogger();

        /* Observer benachrichten */
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Setzt einen Stein auf das Spielfeld
     * 
     * @param position Position auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     * @throws IllegalMoveException Wird geworfen, wenn der Zug die Spielregeln
     *             verletzt
     */
    @Override
    public void setStone( int position ) throws IllegalPositionException,
                                        IllegalMoveException {

        /* Steinbesitzer auf der Position */
        StoneColor owner = this.border.getStoneColor( position );
        ;

        /* Abfrage, ob das Feld frei ist */
        if( owner != StoneColor.NONE ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.field_taken") ); //$NON-NLS-1$
        }

        /* Stein setzen */
        boolean whiteSet = setStone( whitePlayer, blackPlayer, position );
        boolean blackSet = false;
        if( !whiteSet ) {
            blackSet = setStone( blackPlayer, whitePlayer, position );
        }
        if( !whiteSet && !blackSet ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.set_not_alow") ); //$NON-NLS-1$
        }

        /* Observer benachrichtigen */
        this.setChanged();
        this.notifyObservers( "SET" ); //$NON-NLS-1$
    }

    /**
     * Führt den Spielzug aus: "Setze Stein auf Spielbrett"
     * 
     * @param activePlayer Aktiver Spieler
     * @param passivePlayer Passiver Spieler
     * @param position Position auf dem Spielfeld
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    private boolean setStone( Player activePlayer,
                              Player passivePlayer,
                              int position ) throws IllegalPositionException {

        /* Variable, ob setzen erfolgreich war */
        boolean successful = false;

        /* Abfrage des Status */
        if( activePlayer.getState() == State.SET ) {

            /* Setze den Stein auf das Spielfeld */
            this.border.setStoneColor( position, activePlayer.getColor() );
            moveLogger
                      .logMove( 0, position, State.SET, activePlayer.getColor() );

            activePlayer.decrementStonesOut();
            this.updateStates( activePlayer, passivePlayer, position );
            successful = true;
        }
        return successful;
    }

    /**
     * Bewegt einen Spielstein von der Startposition zur Zielposition
     * 
     * @param fromPosition Startposition des Spielsteins
     * @param toPosition Zielposition des Spielsteins
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     * @throws IllegalMoveException Wird geworfen, wenn der Zug die Spielregeln
     *             verletzt
     */
    @Override
    public void moveStone( int fromPosition, int toPosition )
                                                             throws IllegalPositionException,
                                                             IllegalMoveException {

        /* Abfrage der Steinbesitzer auf den übergebenen Positionen */
        StoneColor fromStone = border.getStoneColor( fromPosition );
        StoneColor toStone = border.getStoneColor( toPosition );

        /* Abfrage, ob Stein auf der Startposition ist */
        if( fromStone != StoneColor.WHITE && fromStone != StoneColor.BLACK ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.No_stone_startpos") ); //$NON-NLS-1$
        }

        /* Abfrage, ob Zielposition leer ist */
        if( toStone != StoneColor.NONE ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.dest_pos_taken") ); //$NON-NLS-1$
        }

        /* Bewege Stein */
        boolean whiteMove = moveStone( whitePlayer,
                                       fromPosition,
                                       blackPlayer,
                                       toPosition );
        boolean blackMove = false;
        if( !whiteMove ) {
            blackMove = moveStone( blackPlayer,
                                   fromPosition,
                                   whitePlayer,
                                   toPosition );
        }
        if( !whiteMove && !blackMove ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.no_neigh") ); //$NON-NLS-1$
        }

        /* Observer benachrichtigen */
        this.setChanged();
        this.notifyObservers( "MOVE" ); //$NON-NLS-1$
    }

    /**
     * Führe den Spielzug aus: "Bewege Stein von Position A nach Position B"
     * 
     * @param activePlayer Aktiver Spieler
     * @param passivePlayer Passiver Spieler
     * @param fromPosition Startposition des Spielsteins
     * @param toPosition Zielposition des Spielsteins
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    private boolean moveStone( Player activePlayer,
                               int fromPosition,
                               Player passivePlayer,
                               int toPosition ) throws IllegalPositionException {

        /* Variable, ob bewegen erfolgreich war */
        boolean successful = false;

        /* Abfrage der Steinbesitzer auf der Startposition */
        StoneColor fromStone = border.getStoneColor( fromPosition );
        if( fromStone == activePlayer.getColor()
                && ( activePlayer.getState() == State.MOVE || activePlayer
                                                                          .getState() == State.JUMP ) ) {

            /* Abfrage, ob benachbartes Feld */
            if( activePlayer.getState() == State.MOVE
                    && !border.isNeighbour( fromPosition, toPosition ) ) {
                return false;
            }

            /* Stein verschieben */
            border.setStoneColor( fromPosition, StoneColor.NONE );
            border.setStoneColor( toPosition, activePlayer.getColor() );
            moveLogger.logMove( fromPosition,
                                toPosition,
                                State.MOVE,
                                activePlayer.getColor() );

            this.updateStates( activePlayer, passivePlayer, toPosition );
            successful = true;
        }
        return successful;
    }

    /**
     * Entfernt den Stein auf der übergebenen Position vom Spielfeld
     * 
     * @param position Position des zu entfernenden Steins
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     * @throws IllegalMoveException Wird geworfen, wenn der Zug die Spielregeln
     *             verletzt
     */
    @Override
    public void removeStone( int position ) throws IllegalPositionException,
                                           IllegalMoveException {

        /* Abfrage des Steinbesitzers auf der übergebenen Position */
        StoneColor owner = border.getStoneColor( position );

        /* Abfrage, ob Stein auf der Position vorhanden ist */
        if( owner != StoneColor.BLACK && owner != StoneColor.WHITE ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.no_stone_on_pos") ); //$NON-NLS-1$
        }

        /* Entferne Stein vom Spielbrett */
        boolean whiteRemove = removeStone( whitePlayer, blackPlayer, position );
        boolean blackRemove = false;
        if( !whiteRemove ) {
            blackRemove = removeStone( blackPlayer, whitePlayer, position );
        }
        if( !whiteRemove && !blackRemove ) {
            throw new IllegalMoveException( Messages.getString("MorrisGame.stone_may_not_be_rem") ); //$NON-NLS-1$
        }

        /* Observer benachrichtigen */
        this.setChanged();
        this.notifyObservers( "REMOVE" ); //$NON-NLS-1$
    }

    /**
     * Führe den Spielzug aus: "Entferne Stein vom Spielbrett
     * 
     * @param activePlayer Aktiver Spieler
     * @param passivePlayer Passiver Spieler
     * @param position Position des zu entfernenden Steins
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    private boolean removeStone( Player activePlayer,
                                 Player passivePlayer,
                                 int position ) throws IllegalPositionException {

        /* Variable, ob entfernen erfolgreich war */
        boolean successful = false;

        /* Abfrage des Steinbesitzers auf der übergebenen Position */
        StoneColor owner = border.getStoneColor( position );

        /*
         * Vorraussetzungen zum entfernen des Spielsteins - auf der Position
         * muss ein Spielstein des Gegners liegen - der Status des Spielers muss
         * REMOVE sein - der Stein darf generell nicht in einer Mühle liegen -
         * der Stein darf in einer Mühle liegen, wenn sonst keine Steine frei
         * sind
         */
        if( ( passivePlayer.getColor() == owner && activePlayer.getState() == State.REMOVE )
                && ( !border.isStoneInMill( position ) || ( border
                                                                  .isStoneInMill( position ) && border
                                                                                                      .getNumberOfFreeStones( passivePlayer
                                                                                                                                           .getColor() ) == 0 ) ) ) {

            /* Entfernen des Spielsteins */
            border.setStoneColor( position, StoneColor.NONE );
            moveLogger.logMove( 0,
                                position,
                                State.REMOVE,
                                activePlayer.getColor() );

            this.updateStates( activePlayer, passivePlayer, position );
            successful = true;
        }
        return successful;
    }

    /**
     * Gibt eine Kopie des aktuellen Spiels zurück
     * 
     * @return Kopie des aktuellen Spiels
     */
    @Override
    public MorrisGame clone() {

        Border borderClone = border.clone();
        Player whitePlayerClone = whitePlayer.clone();
        Player blackPlayerClone = blackPlayer.clone();
        MorrisGame gameClone = new MorrisGame( this.getConfig(),
                                               borderClone,
                                               whitePlayerClone,
                                               blackPlayerClone );
        return gameClone;
    }

    /**
     * Gibt die Anzahl der Steine, die nicht in einer Mühle stehen
     * 
     * @param color Spielsteinfarbe, für welchen die freien Steine gezählt
     *            werden sollen
     * @return Anzahl der Steine des Spielers, welche nicht in einer Mühle
     *         stehen
     */
    @Override
    public int getNumberOfFreeStones( StoneColor color ) {

        return this.border.getNumberOfFreeStones( color );
    }

    /**
     * Gibt die Anzahl der möglichen Bewegungen für einen Spieler
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der möglichen Bewegungen
     */
    @Override
    public int getNumberOfPossibleMoves( StoneColor color ) {

        return this.border.getNumberOfPossibleMoves( color );
    }

    /**
     * Gibt die Anzahl der potentiellen Bewegungsmöglichkeiten zurück
     * 
     * @param color Spielsteinfarbe, für welche die Bewegungen abgefragt werden
     *            sollen
     * @return Anzahl der potentiellen Bewegungen (wenn alle sonstige Felder
     *         frei wären)
     */
    @Override
    public int getNumberOfPotentialMoves( StoneColor color ) {

        return this.border.getNumberOfPotentialMoves( color );
    }

    /**
     * Gibt die Anzahl der Spielsteine eines Spielers
     * 
     * @param color Spielsteinfarbe für welchen die Steine gezählt werden sollen
     * @return Die Anzahl der Steine des Spielers auf dem Spielbrett
     */
    @Override
    public int getNumberOfStones( StoneColor color ) {

        return this.border.getNumberOfStones( color );
    }

    /**
     * Abfrage, ob zwei Steine benachbart sind
     * 
     * @param position1 Position des ersten Steins (1-24)
     * @param position2 Position des zweiten Steins (1-24)
     * @return True wenn die beiden Steine benachbart sind
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    @Override
    public boolean isNeighbour( int position1, int position2 )
                                                              throws IllegalPositionException {

        return this.border.isNeighbour( position1, position2 );
    }

    /**
     * Abfrage, ob Stein in einer Mühle ist.
     * 
     * @param position Position der Steins auf dem Spielbrett (1-24)
     * @return True, wenn sich der Stein in einer Mühle befindet
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    @Override
    public boolean isStoneInMill( int position )
                                                throws IllegalPositionException {

        return this.border.isStoneInMill( position );
    }

    /**
     * Setzt einen neuen Besitzer für einen Stein auf dem Spielbrett. Der neue
     * Besitzer wird nur bei einer übergebenen Instanz von StoneColor gesetzt.
     * 
     * @param position Position des Steins auf dem Spielbrett (1-24)
     * @param color Neue Spielsteinfarbe des Steins
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     */
    @Override
    public void setStoneColor( int position, StoneColor color )
                                                               throws IllegalPositionException {

        this.border.setStoneColor( position, color );

    }

    /**
     * Berechnet den neuen Status der Spieler nach einem Zug
     * 
     * @param activePlayer Spieler, welcher einen Spielzug ausgeführt hat
     * @param passivePlayer Spieler, welcher keinen Spielzug ausgeführt hat
     * @param position Position, auf welcher der aktive Spieler einen Stein
     *            gesetzt hat
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    private void updateStates( Player activePlayer,
                               Player passivePlayer,
                               int position ) throws IllegalPositionException {

        /* Abfrage, ob Mühle gesetzt wurde */
        if( this.border.isStoneInMill( position ) ) {
            activePlayer.setState( State.REMOVE );
        }
        else {
            /* Zug von weiß setzen */
            activePlayer.setState( State.WAIT );
            /* Zug von schwarz neu berechnen */
            passivePlayer
                         .updateState( border
                                             .getNumberOfStones( passivePlayer
                                                                              .getColor() ),
                                       border
                                             .getNumberOfPossibleMoves( passivePlayer
                                                                                     .getColor() ),
                                       border
                                             .getNumberOfPossibleMoves( activePlayer
                                                                                    .getColor() ),
                                       activePlayer,
                                       moveLogger.isDraw() );
        }
    }
}
