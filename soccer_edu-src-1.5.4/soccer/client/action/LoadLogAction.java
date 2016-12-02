/* LoadLogAction.java
   
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

import java.awt.event.ActionEvent;
import java.io.RandomAccessFile;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import soccer.client.Replayer;
import soccer.client.SoccerMaster;

public class LoadLogAction extends AbstractClientAction {
	public LoadLogAction() {
		super();
		putValue(NAME, "Load the log file...");
		URL imgURL = SoccerMaster.class.getResource("/imag/vopen.gif");
		ImageIcon icon = new ImageIcon(imgURL);
		putValue(SMALL_ICON, icon);
		//setAccelerator(KeyEvent.VK_Q, Event.CTRL_MASK);
		setEnabled(true);
	}
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showOpenDialog(getSoccerMaster());
		if (option == JFileChooser.APPROVE_OPTION)
			if (chooser.getSelectedFile() != null)
				try {
					getSoccerMaster().logFile =
						new RandomAccessFile(chooser.getSelectedFile(), "r");
					getSoccerMaster().replayer =
						new Replayer(getSoccerMaster());
					getSoccerMaster().replayer.start();
					getSoccerMaster().getAction(
						PlayGameAction.class).setEnabled(
						false);
					getSoccerMaster().getAction(
						ViewGameAction.class).setEnabled(
						false);
					setEnabled(false);
					getSoccerMaster().getAction(
						StopLogPlayAction.class).setEnabled(
						true);
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(
						getSoccerMaster(),
						"can not open the log file",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				}
	}
}
