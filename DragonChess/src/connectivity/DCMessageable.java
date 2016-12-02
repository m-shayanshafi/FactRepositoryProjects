/*
 * Classname			: DCMessageable
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Fri Oct 18 17:12:31 CEST 2002 
 * Last Updated			: Sun Dec 08 16:23:08 CET 2002 
 * Description			: Interface
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

package connectivity;

/* package import */
import java.lang.*;

/**
 * Classes that implement this interface are able to accept DCMessages.
 *
 * @author Davy Herben
 * @version 021208
 */ 
public interface DCMessageable {

	/*
	 * METHODS
	 */
	
	/**
	 * Sends message to the class.
	 * @param message	the DCMessage to send
	 */
	public void sendMessage(DCMessage message);
} 

/* END OF FILE */
