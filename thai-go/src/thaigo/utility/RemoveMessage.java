package thaigo.utility;

import java.io.Serializable;

import thaigo.property.Position;

/**
 * Remove message sent when there is some <code>Pawn</code> object is removed.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class RemoveMessage extends Message implements Serializable {
	
	/** Position of removed <code>Pawn</code> object. */
	private Position removedPawn;
	
	/** Inverted position of removed <code>Pawn</code> object. */
	private Position inversedRemovedPawn;
	private String eater;
	
	/**
	 * Constructor initialize by position of removed <code>Pawn</code> object.
	 * @param removedPawn position of removed <code>Pawn</code> object.
	 */
	public RemoveMessage(Position removedPawn) {
		this.removedPawn = removedPawn;
		this.eater = PropertyManager.getProperty("player");
		this.inversedRemovedPawn = new Position(removedPawn.getX(), Integer.parseInt(PropertyManager.getProperty("table")) - 1 - removedPawn.getY());

	}
	
	/**
	 * Return position of removed <code>Pawn</code> object.
	 * @return position of removed <code>Pawn</code> object
	 */
	public Position getRemovedPosition() {
		return removedPawn;
	}
	
	/**
	 * Return inverted position of removed <code>Pawn</code> object.
	 * @return inverted position of removed <code>Pawn</code> object
	 */
	public Position getInversedRemovedPosition() {
		return inversedRemovedPawn;
	}
	
	/**
	 * Return string show which <code>Pawn</code> object is ate by whom.
	 * @return string show which <code>Pawn</code> object is ate by whom
	 */
	public String toString() {
		return eater + " ate at " + removedPawn.toString();
	}
	
	/**
	 * Return string show which <code>Pawn</code> object is ate by whom.
	 * @return string show which <code>Pawn</code> object is ate by whom
	 */
	public String inversedToString() {
		return eater + " ate at " + inversedRemovedPawn.toString();
	}
	
}
