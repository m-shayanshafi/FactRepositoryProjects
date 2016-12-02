/* CoachForwardAction.java
   
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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import soccer.client.SoccerMaster;
import soccer.common.Packet;
import soccer.common.PeriodData;
import java.io.IOException;

public class CoachForwardAction extends AbstractClientAction {
	
	public CoachForwardAction() {
		super();
		putValue(NAME, "Forward the game");
		//URL imgURL = SoccerMaster.class.getResource("/imag/cfwd_disabled.gif");
		URL imgURL = SoccerMaster.class.getResource("/imag/cforward.gif");
		ImageIcon icon = new ImageIcon(imgURL);
		putValue(SMALL_ICON, icon);
		//setAccelerator(KeyEvent.VK_Q, Event.CTRL_MASK);
		setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		
		JToolBar jtb = getSoccerMaster().getJToolBar();
		JButton aJButton;  

		// disable the Load button 
		aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getLoadBtnIdx() ); 
		aJButton.setEnabled(false);

		PeriodData serverControl = new PeriodData(PeriodData.FORWARD);
		Packet command = new Packet(Packet.PERIOD, 
									serverControl, 
									getSoccerMaster().getAddress(), 
									getSoccerMaster().getPort());
		
		try{
			getSoccerMaster().getTransceiver().send(command);
		}
		catch(IOException ie)
		{
		}		
	}
}
