package ChessGameKenai;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.text.SimpleAttributeSet;

/**
 * The Packet class is a serialized object that we use to communicate with the
 * client and the server we simply set needed instance variables and then we
 * extract them on the other side
 * 
 * @author Dimitri Pankov
 * @see Serializable
 * @version 1.5
 */
public class Packet implements Serializable {

	private String message;
	private String guestName;
	private String playerIconPath;
	private String restartGame;
	private String imgPath;
	private SimpleAttributeSet smpSet;
	private Color color;
	private String confirmRestart;

	/**
	 * The method getConfirmRestart is used to confirm if the user wants to
	 * restart the game or not when the user clicks restart game the
	 * confirmation will be sent to the other user if he really wants to restart
	 * the game or not
	 * 
	 * @return confirmRestart as a String
	 */
	public String getConfirmRestart() {
		return confirmRestart;
	}

	/**
	 * The method setConfirmRestart sets the confirmation for restart game
	 * 
	 * @param confirmRestart
	 *            as a String
	 */
	public void setConfirmRestart(final String confirmRestart) {
		this.confirmRestart = confirmRestart;
	}

	/**
	 * The method getRestartGame simply returns the null or not null if not null
	 * the game is restarted if null not restarted
	 * 
	 * @return as a String
	 */
	public String getRestartGame() {
		return restartGame;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(final String imgPath) {
		this.imgPath = imgPath;
	}

	/**
	 * The method getSmpSet simple returns the SimpleAttributeSet to the caller
	 * this object is used by the TextPane to insert strings with different
	 * color and attributes
	 * 
	 * @return smpSet as a SimpleAttributeSet
	 */
	public SimpleAttributeSet getSmpSet() {
		return smpSet;
	}

	/**
	 * The method setSmpSet simply sets the SimpleAttributeSet of the current
	 * string
	 * 
	 * @param smpSet
	 *            as a SimpleAttributeSet
	 */
	public void setSmpSet(final SimpleAttributeSet smpSet) {
		this.smpSet = smpSet;
	}

	/**
	 * The method setRestartGame simply sets the value to not null when the game
	 * needs to be restarted
	 * 
	 * @param restartGame
	 *            as a String
	 */
	public void setRestartGame(final String restartGame) {
		this.restartGame = restartGame;
	}

	public String getPlayerIconPath() {
		return playerIconPath;
	}

	public void setPlayerIconPath(final String playerIconPath) {
		this.playerIconPath = playerIconPath;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(final String guestName) {
		this.guestName = guestName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
