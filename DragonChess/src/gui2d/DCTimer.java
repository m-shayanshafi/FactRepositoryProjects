/*
 * Classname		: DCTimer
 * Author			: Christophe Hertigers <christophe.hertigers@pandora.be>
 * Creation Date	: 2002/03/25
 * Last Updated		: Thursday, October 17 2002, 23:42:26
 * Description		: 2-Dimensional user interface for DragonChess.
 * GPL disclaimer	:
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

package gui2d;

/* package import */
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Timer Object to store the time spent playing.
 *
 * @author		Christophe Hertigers
 * @version     Thursday, October 17 2002, 23:42:30
 */
public class DCTimer {

	/**
	 * Inner class. Handles the ActionEvents of the timer.
	 *
	 */
	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			s++;
			timerLabel.setText(getTime());
		}
	}

	/*
	 * Instance Variables
	 *
	 */
	private static final int	ONE_SECOND = 1000;
	 
	private long	s;
	private Timer	time;
	private JLabel	timerLabel;

	/**
	 * Class Constructor. Initializes the time to 0.
	 *
	 */
	public DCTimer(JLabel myTimerLabel) {
		this(0, myTimerLabel);
	}
	
	/**
	 * Class Constructor. Initializes the time to the number of seconds given.
	 *
	 * @param sInt		the number of seconds the timer starts with.
	 */
	public DCTimer(long sInt, JLabel myTimerLabel) {
		s = sInt;
		time = new Timer(ONE_SECOND, new ActionHandler());
		timerLabel = myTimerLabel;
	}

	/**
	 * Starts the timer.
	 *
	 */
	public void start() {
		time.start();
	}

	/**
	 * Stops the timer.
	 *
	 */
	public void stop() {
		time.stop();
	}

	/**
	 * Gets the time.
	 *
	 * @return		a String containing the time (HH:MM:SS)
	 */
	public String getTime() {
		return((s/3600) + ":" + ((s/60)%60) + ":" + (s%60));
	}
}
