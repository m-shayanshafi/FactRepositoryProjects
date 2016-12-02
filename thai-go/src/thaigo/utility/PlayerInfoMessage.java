package thaigo.utility;

import java.io.Serializable;

/**
 * Player information sent when creating the game.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class PlayerInfoMessage extends Message implements Serializable {

	/** Name of player. */
	private String name;

	/**
	 * Constructor for initializing by getting player name from property.
	 */
	public PlayerInfoMessage() {
		this.name = PropertyManager.getProperty("player");
	}

	/**
	 * Return sent name from another player.
	 * @return name of another player
	 */
	public String getFoeName() {
		return name;
	}
}