/* CoachPlayAction.java
   
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
								
	Modifications by Vadim Kyrylov 
							January 2006
*/

package soccer.client.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import soccer.client.SoccerMaster;
import soccer.client.dialog.DialogManager;
import soccer.client.dialog.ViewDialog;

import soccer.common.GState;
import soccer.common.Packet;
import soccer.common.PeriodData;
import java.io.IOException;

public class CoachPlayAction extends AbstractClientAction {
	
	private ImageIcon pauseIcon;
	
	
	public CoachPlayAction() {
		super();
		putValue(NAME, "Start/Continue the game");
		URL imgURL = SoccerMaster.class.getResource("/imag/cplay.gif");
		ImageIcon playIcon = new ImageIcon(imgURL);
		putValue(SMALL_ICON, playIcon);
		//setAccelerator(KeyEvent.VK_Q, Event.CTRL_MASK);
		setEnabled(true);
			
		imgURL = SoccerMaster.class.getResource("/imag/cpause.gif");
		pauseIcon = new ImageIcon(imgURL);
	}

	
	public void actionPerformed(ActionEvent e) {
		
		JToolBar jtb = getSoccerMaster().getJToolBar();
		JButton aJButton;  

		// disable the Load button 
		aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getLoadBtnIdx() ); 
		aJButton.setEnabled(false);
		
		// disable the Save button 
		aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getSaveBtnIdx() ); 
		aJButton.setEnabled(false);

		// disable the Forward button 
		aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getFwdBtnIdx() ); 
		aJButton.setEnabled(false);
		
		if ( getSoccerMaster().getGState() == GState.INIT ) {
			
			// initialize viewing/running game without showing the dialogue
			
			DialogManager aDialogManager = getSoccerMaster().getDialogManager();  
			ViewDialog aViewDialog = new ViewDialog( aDialogManager, getSoccerMaster() ); 	
			aViewDialog.display();
			aViewDialog.init();
			aViewDialog.undisplay();

		} else {
			// normal start game command

			if ( getSoccerMaster().getGState() != GState.CONNECTED ) {
				// enable the Forward button 
				aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getFwdBtnIdx() ); 
				aJButton.setEnabled(true);
			}


			// enable the Step button and change its icon
			aJButton = (JButton)jtb.getComponentAtIndex( getSoccerMaster().getStepBtnIdx() ); 
			aJButton.setEnabled(true);
			aJButton.setIcon( pauseIcon );

			PeriodData serverControl = new PeriodData(PeriodData.PLAY);
			Packet command = new Packet(Packet.PERIOD, 
										serverControl, getSoccerMaster().getAddress(), 
										getSoccerMaster().getPort());
			try{
				getSoccerMaster().getTransceiver().send(command);
			}
			catch(IOException ie)
			{
			}
		}
	}
}
