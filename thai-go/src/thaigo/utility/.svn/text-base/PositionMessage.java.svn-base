package thaigo.utility;

import java.io.Serializable;

import thaigo.property.Position;

/**
 * Position message sent when there is some <code>Pawn</code> object is changed position.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class PositionMessage extends Message implements Serializable {

	/** Name of another player. */
	private String player;
	
	/** Old position which is changed. */
	private Position oldPos;
	/** New position. */
	private Position newPos;
	
	/** Inverted old position used in another game. */
	private Position invertedoldPos;
	/** Inverted new position used in another game. */
	private Position invertednewPos;

	/**
	 * Constructor initialize by getting old and new position.
	 * @param oldPos the old position
	 * @param newPos the new position
	 */
	public PositionMessage(Position oldPos, Position newPos) {
		this.oldPos = oldPos;
		this.newPos = newPos;
		this.invertedoldPos = new Position(oldPos.getX(), Integer.parseInt(PropertyManager.getProperty("table")) - 1 - oldPos.getY());
		this.invertednewPos = new Position(newPos.getX(), Integer.parseInt(PropertyManager.getProperty("table")) - 1 - newPos.getY());
		this.player = PropertyManager.getProperty("player");
	}

	/**
	 * Return old position.
	 * @return the old position
	 */
	public Position getOldPos() {
		return oldPos;
	}

	/**
	 * Return new position.
	 * @return the new position
	 */
	public Position getNewPos() {
		return newPos;
	}

	/**
	 * Return inverted old position.
	 * @return the inverted old position
	 */
	public Position getInvertedOldPos() {
		return invertedoldPos;
	}

	/**
	 * Return inverted new position.
	 * @return the inverted new position
	 */
	public Position getInvertedNewPos() {
		return invertednewPos;
	}

	/**
	 * Return string that identify which position is changed to what position.
	 * @return string that identify which position is changed to what position
	 */
	public String toString() {
		return player + " : From " + oldPos + " To " + newPos;
	}
	
	/**
	 * Return string that identify which position is changed to what position inversely.
	 * @return string that identify which position is changed to what position inversely
	 */
	public String invertedToString() {
		return player + " : From " + invertedoldPos + " To " + invertednewPos;
	}
}