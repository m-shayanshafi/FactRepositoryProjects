/*
 * Classname            : DCGameSpectator
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:43:15
 * Last Updated         : Sun Dec 08 16:14:15 CET 2002 
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
import main.DCFrontEnd;

/**
 * This class serves as a client to connect spectators to an existing game.
 * As this behaviour is very much like that of the {@link DCGameClient}, it is
 * likely that this class will not be implemented.
 *
 * <p>This class is not yet implemented.
 *
 * @author Christophe Hertigers
 * @author Davy Herben
 * @version 021208
 */
public class DCGameSpectator extends DCGame {
									 
	/*
     * VARIABLES
     *
     */

    /* CLASS VARIABLES */

    /* INSTANCE VARIABLES */
									 
    /* 
     * INNER CLASSES
     *
     */

    /*
     * CONSTRUCTORS
     *
     */

	/**
	 * Class constructor. Does not do anuthing yet.
	 */
	public DCGameSpectator(DCFrontEnd gui) {
        super(gui);
		
		//DEBUG OUTPUT
		System.out.println("****** DCCGAMESPECTATOR INITIALISED ******");
	}
}

/* END OF FILE */

