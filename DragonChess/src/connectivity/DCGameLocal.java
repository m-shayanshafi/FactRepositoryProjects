/*
 * Classname            : DCGameLocal
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:43:15
 * Last Updated         : Sun Dec 08 16:05:34 CET 2002 
 * Description          : 
 * GPL disclaimer       :
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

package connectivity;

/* package import */
import java.lang.*;
import main.*;
import backend.DCLocalGameEnv;

/**
 * This class provides a backend for a local game, that is without any network
 * connections. It sets up a DCLocalGameEnv backend and takes care of the
 * routing of messages between backend and frontend. This class does not
 * listen for network activity at all, so it is impossible for spectators to
 * connect.
 *
 * @author Christophe Hertigers
 * @author Davy Herben
 * @version 021208
 */
public class DCGameLocal extends DCGame {
									 
    /*
     * CONSTRUCTORS
     *
     */

	/**
	 * Class constructor. Creates a new backend and connects it to the frontend
	 * @param	gui	the frontend to send messages to
	 */
	public DCGameLocal(DCFrontEnd gui) {
        super(gui);
		
		//Create a DCLocalGameEnv
		backend = new DCLocalGameEnv(this);
	
		//create one local connection. This connection can handle both players
		DCLocalConnection newConn = new DCLocalConnection(this, gui,
				connections.size());
		connections.add(newConn);
		
		//DEBUG OUTPUT
		//System.out.println("****** DCGAMELOCAL INITIALISED ******");
	}
}

/* END OF FILE */

