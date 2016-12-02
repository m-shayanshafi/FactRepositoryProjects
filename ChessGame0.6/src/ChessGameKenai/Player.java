package ChessGameKenai;

import java.io.Serializable;

/**
 * The Player class implements Serializable Its the Player that is created each
 * time you create a game The two players will have default names which are
 * player1 and player2 respectively
 * 
 * @author Dimitri Pankov
 * @see Serializable
 * @version 1.01
 */
public class Player implements Serializable {

	private String name;
	private int numberOfWins = 0;
	private String imagePath;
	private boolean playSelf = false;

	/**
	 * OverLoaded Constructor for creating Player object
	 * 
	 * @param name
	 *            name of the Player object
	 */
	public Player(final String name) {
		this.name = name;
	}

	/**
	 * Overloaded constructor of the class receives name and image path to
	 * represent itself
	 * 
	 * @param name
	 *            as a String
	 * @param imagePath
	 *            as a String
	 */
	public Player(final String name, final String imagePath) {
		this.name = name;
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void setToPlaySelf(boolean play) {
		this.playSelf = play;
	}

	public boolean getPlaySelf() {
		return this.playSelf;
	}

	public void setNumberOfWins(final int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}

	public String getIconPath() {
		return imagePath;
	}

	public void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * The class needs to overwrite the method the toString this method is very
	 * useful for debugging
	 * 
	 * @return s as a String
	 */
	@Override
	public String toString() {
		String stringValue = "";
		stringValue += "Name: " + this.getName() + ", Wins: "
				+ this.getNumberOfWins() + ", Icon: " + this.getIconPath();
		return stringValue;
	}
}
