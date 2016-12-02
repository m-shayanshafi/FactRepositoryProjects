package thaigo.property;

import java.io.Serializable;

import thaigo.utility.PropertyManager;

/**
 * One of property of <code>Pawn</code> object.
 * Contains name and mode of player.
 *
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class Owner implements Serializable {

	/** Name of player. */
	private String player;
	/** Mode of player. */
	private String mode;

	/**
	 * Constructor for creating <code>Owner</code>.
	 * This constructor assigns player name and mode by getting from property.
	 */
	public Owner() {
		this.player = PropertyManager.getProperty("player");
		this.mode = PropertyManager.getProperty("mode");
	}

	/**
	 * Constructor for creating <code>Owner</code>.
	 * This constructor assigns player name and mode by getting from parameters.
	 * @param player name of player
	 * @param mode mode of player
	 */
	public Owner(String player, String mode) {
		this.player = player;
		this.mode = mode;
	}

	/**
	 * Check is this <code>Owner</code> equal to another or not.
	 * @param another <code>Owner</code> object
	 * @return true if another has same name and mode with this, otherwise false.
	 */
	public boolean equals(Object obj) {
		if (obj == null && !(obj instanceof Owner))
			return false;
		return (((Owner)obj).player.equals(player) && ((Owner)obj).mode.equals(mode));
	}

	/**
	 * Return string identifying owner.
	 * @return string as player name and mode 
	 */
	public String toString() {
		return String.format("<%s> %s", mode.toUpperCase(), player);
	}
}
