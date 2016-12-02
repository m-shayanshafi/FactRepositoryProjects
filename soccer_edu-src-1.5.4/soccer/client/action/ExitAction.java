/* ExitAction.java
   
   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import soccer.client.SoccerMaster;

public class ExitAction extends AbstractClientAction {
	public ExitAction() {
		super();
		putValue(NAME, "Exit");
		URL imgURL = SoccerMaster.class.getResource("/imag/exit.gif");
		ImageIcon icon = new ImageIcon(imgURL);
		putValue(SMALL_ICON, icon);
		setAccelerator(KeyEvent.VK_Q, Event.CTRL_MASK);
		setEnabled(true);
	}
	public void actionPerformed(ActionEvent e) {
		int option =
			JOptionPane.showConfirmDialog(
				getSoccerMaster(),
				"Do you really want to quit?",
				SoccerMaster.APP_NAME,
				JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			getSoccerMaster().quit();
		}
	}
}
