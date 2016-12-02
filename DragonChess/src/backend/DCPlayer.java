/*
 * Classname			: DCPlayer
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sun Oct 20 11:39:46 CEST 2002 
 * Last Updated			: Sun Dec 08 13:25:48 CET 2002 
 * Description			: Simple class that contains player info
 * GPL disclaimer		:
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package backend;

/* package import */
import java.lang.*;
import main.DCConstants;

/**
 * This class represents a player in the backend. It is only used to hold some
 * information about players, such as their name and address, and their
 * connection state.
 *
 * @author Davy Herben
 * @version 021208
 */ 
public class DCPlayer {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */ 
	private String name;
	private String address;
	private int state;
	
	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/**
	 * Class constructor. Creates a player in UNREGISTERED state
	 */
	public DCPlayer() {
		state = DCConstants.PL_UNREGISTERED;
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * gets the name of the player
	 * @return	String with name of player
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the player
	 * @param	name	String with name of the player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the address of the player
	 * @return	String with address of player
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * sets the address of the player
	 * @param	address	String with address of player
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * gets the player state
	 * @return	int with player state
	 */
	public int getState() {
		return state;
	}

	/**
	 * sets the player state
	 * @param	state	int with player state
	 */
	public void setState(int state) {
		this.state = state;
	}
} 

/* END OF FILE */
