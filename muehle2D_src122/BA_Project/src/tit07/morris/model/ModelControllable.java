package tit07.morris.model;

import java.io.File;
import java.io.IOException;
import tit07.morris.exception.IllegalColorException;
import tit07.morris.exception.IllegalMoveException;
import tit07.morris.exception.IllegalPositionException;


/**
 * Das Interface Controllable legt die Schnittstellen für das Model fest, welche
 * nötig sind um das Spiel zu steuern.
 */
public interface ModelControllable extends Configurateable {

    /**
     * Methode, um das Spiel zurückzusetzen
     */
    public void resetGame();

    /**
     * Methode, um einen Stein auf das Spielfeld zu setzen
     * 
     * @param position Position auf dem Spielfeld
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     * @throws IllegalMoveException Wird geworfen bei ungültigem Spielzug
     */
    public void setStone( int position ) throws IllegalPositionException,
                                        IllegalMoveException;

    /**
     * Methode, um einen Stein auf dem Spielfeld zu bewegen
     * 
     * @param fromPosition Ausgangsposition des Steins
     * @param toPosition Zielposition des Steins
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     * @throws IllegalMoveException Wird geworfen bei ungültigem Spielzug
     */
    public void moveStone( int fromPosition, int toPosition )
                                                             throws IllegalPositionException,
                                                             IllegalMoveException;

    /**
     * Methode, um einen Stein vom Spielfeld zu entfernen
     * 
     * @param position Position auf dem Spielfeld
     * @throws IllegalPositionException Wird geworfen bei ungültiger Position
     * @throws IllegalMoveException Wird geworfen bei ungültigem Spielzug
     */
    public void removeStone( int position ) throws IllegalPositionException,
                                           IllegalMoveException;

    /**
     * Abfrage, ob die übergebene Spielsteinfarbe am Zug ist
     * 
     * @param color Abzufragende Farbe des Spielers
     * @return True, wenn Farbe zugberechtigt
     * @throws IllegalColorException Wird geworfen bei ungültiger Farbe
     */
    public boolean isItTurnOfColor( StoneColor color )
                                                      throws IllegalColorException;

    /**
     * Speichert den Spielstand als Datei
     * 
     * @param outFile Zieldatei
     * @throws IOException Wird bei aufgetretenen Fehlern während des
     *             Speichervorgangs geworfen
     */
    public void saveGame( File outFile ) throws IOException;

    /**
     * Läd den Spielstand von einer Datei
     * 
     * @param inFile Quelldatei
     * @throws IOException Wird bei aufgetretenen Fehlern während des
     *             LAdevorgangs geworfen
     */
    public void loadGame( File inFile ) throws IOException;

    /**
     * Status eines Spielers abfragen
     * 
     * @param color Steinfarbe des abzufragenden Spielers
     * @return Status des Spielers
     * @throws IllegalColorException Wird bei einer ungültigen Steinfarbe
     *             geworfen
     */
    public State getState( StoneColor color ) throws IllegalColorException;

    /**
     * Abfrage der Spielsteinfarbe des Steins auf der übergebenen Position
     * 
     * @param position Position (1-24) des Steins auf dem Spielbrett
     * @throws IllegalPositionException Wird geworfen, wenn eine falsche
     *             Position übergeben wurde
     */
    public StoneColor getStoneColor( int position )
                                                   throws IllegalPositionException;
}
