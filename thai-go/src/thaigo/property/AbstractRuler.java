package thaigo.property;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import thaigo.network.client.Client;
import thaigo.network.server.ClientHandler;
import thaigo.object.Board;
import thaigo.object.Pawn;
import thaigo.utility.PropertyManager;
import thaigo.utility.RemoveMessage;
import thaigo.view.GameUI;

/**
 * This abstract class is prototype of the ruler.
 * We can make other style of game by adding class ruler
 * and then inherit this abstract class.
 * 
 * @author Rungroj Maipradit 5510546654
 * @version 9/5/2013
 */
public abstract class AbstractRuler {
	/** Number of block in one line.*/
	protected static int TABLE;
	/**	All of yourPawn.*/
	protected static List<Pawn> yourPawn;
	/**	All of foePawn.*/
	protected static List<Pawn> foePawn;
	/**	Board that use to play in this game.*/
	protected static Board board;
	/** Present pawn position.*/
	protected static Position pawnPos;
	/**	 */
	protected static Position gopenelPos;
	/** Client of this game.*/
	private static Client client;
	/** Server of this game.*/
	private static ClientHandler server;
	/** UI of this game.**/
	protected static GameUI gameUI;
	/** Ruler for handling rule of the game.*/
	protected static AbstractRuler ruler;
	/**Array that check what is block have.*/
	protected int[][] checkarr;
	/**Check that position can walk or not.*/
	protected boolean rightPosition = false;
	
	protected Timer timer;
	
	/**
	 * Constructor of this class.
	 */
	protected AbstractRuler() {
		this.TABLE = Integer.parseInt(PropertyManager.getProperty("table"));
		checkarr = new int[TABLE][TABLE];
		yourPawn = new ArrayList<Pawn>();
		foePawn = new ArrayList<Pawn>();
		this.client = Client.getInstance();
		this.server = ClientHandler.getInstance();
	}
	/**
	 * Get Array of block.
	 * @return Array of block.
	 */
	public int[][] getCheckArr() {
		return checkarr;
	}

	/** Stops the timer. */
	public void stopTimer(){
		timer.cancel();
	}
	
	/**
	 * Set Client.
	 * @param client reference of client.
	 */
	public void setClient(Client client) {
		this.client = client;
		this.gameUI = client.getGameUI();
	}

	/**
	 * Get client.
	 * @return client.
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Set Server.
	 * @param server reference of server.
	 */
	public void setClientHandler(ClientHandler server) {
		this.server = server;
		this.gameUI = server.getGameUI();
	}

	/**
	 * Get server.
	 * @return server.
	 */
	public ClientHandler getClientHandler() {
		return this.server;
	}

	/**
	 * Initialize pawn into board.
	 * @param board that contain pawn.
	 */
	public abstract void initPawn(Board board);
	
	/**
	 * Rule of move pawn.
	 * @param e mouse release.
	 */
	public abstract void walking(MouseEvent e);
	
	/**
	 * Rule of remove pawn.
	 */
	public abstract void eating();
	
	/**
	 * Rule that show which block can move.
	 */
	public abstract void showing();
	
	/**
	 * Check which person win, draw or lose.
	 */
	public abstract void checkWinDrawLose();

	/**
	 * Set position of pawn.
	 * @param pawnPos present pawn that click.
	 */
	public void setPawnPosition(Position pawnPos) {
		board.resetColor();
		this.pawnPos = pawnPos;
	}

	/**
	 * 
	 * @param position
	 */
	public void setGOPanelPosition(Position position) {
		this.gopenelPos = position;
	}

	/**
	 * Check position can move or not.
	 * @return can move true can't false.
	 */
	public boolean isRightPosition() {
		return rightPosition;
	}

	/**
	 * Set position can move or not.
	 * @param rightPosition reference of rightPosition.
	 */
	public void setRightPosition(boolean rightPosition) {
		this.rightPosition = rightPosition;
	}

	/**
	 * Reset all position.
	 */
	public void resetAllPosition() {
		for (int i = 0; i < yourPawn.size(); i++) {
			if (yourPawn.get(i).getPosition().equals(pawnPos))
				yourPawn.get(i).setPosition(gopenelPos);
		}
		this.pawnPos = null;
		this.gopenelPos = null;
	}

	/**
	 * Check pawn position is empty or not.
	 * @return emtry true not false.
	 */
	public boolean isPawnPosNull() {
		return (pawnPos == null);
	}

	/**
	 * All of your pawn.
	 * @return your pawn.
	 */
	public List<Pawn> getYourPawn(){
		return yourPawn;
	}

	/**
	 * All of foe pawn.
	 * @return foe pawn.
	 */
	public List<Pawn> getFoePawn(){
		return foePawn;
	}

	/**
	 * Position of present pawn.
	 * @return present pawn position.
	 */
	public static Position getPawnPos() {
		return pawnPos;
	}

	/**
	 * 
	 * @return
	 */
	public static Position getGopenelPos() {
		return gopenelPos;
	}

	/**
	 * Update pawn to board.
	 */
	public void updateBoard() {
		board.render(yourPawn, foePawn);
	}

	/**
	 * Send Position of pawn to foe.
	 */
	public void sendPawnPositionsToFoe() {
		if (isRightPosition()) {
			if (PropertyManager.getProperty("mode").equals("server")) {
				server.sendPawnPositionsToFoe();
			}
			else {
				client.sendPawnPositionsToFoe();
			}
		}
	}

	/**
	 * Send position that move to foe.
	 * @param oldPos before move position.
	 * @param newPos after move position.
	 */
	public void setFoeWalkingPosition(Position oldPos, Position newPos) {
		for (int i = 0; i < foePawn.size(); i++) {
			if (foePawn.get(i).getPosition().equals(oldPos))
				foePawn.get(i).setPosition(newPos);
		}
		updateBoard();
	}

	/**
	 * Send which position remove to foe.
	 * @param removedPos position that removed.
	 */
	public void sendRemoveCommandToFoe(Position removedPos) {

		if (PropertyManager.getProperty("mode").equals("server")) {
			server.sendRemoveCommandToFoe(new RemoveMessage(removedPos));
		}
		else {
			client.sendRemoveCommandToFoe(new RemoveMessage(removedPos));
		}
	}

	/**
	 * Remove pawn in that position.
	 * @param position that removed.
	 */
	public void removedAtPosition(Position position) {
		for(int i = 0 ;i<yourPawn.size();i++){
			if (yourPawn.get(i).getPosition().equals(position))
				yourPawn.remove(yourPawn.get(i));
		}
		updateBoard();
		checkWinDrawLose();
	}
	/**
	 * Ask for created new game.
	 */
	public abstract void newGame();
}