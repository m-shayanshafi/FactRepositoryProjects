/*
 * ViewDialog.java This class opens a view dialog to get required information
 * for viewing the game
 * 
 * Copyright (C) 2001 Yu Zhang
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *	Cosmetic modifications by Vadim Kyrylov 
							January 2006
 */

package soccer.client.dialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import soccer.client.action.*;
import soccer.client.Cviewer;
import soccer.client.SoccerMaster;
import soccer.common.*;

public class ViewDialog extends JPanel implements ActionListener 
{

	private SoccerMaster m_client;

	private DialogManager m_dlgManager;

	private JDialog m_dialog;

	private JTextField hostName;

	private JTextField portNum;

	private JCheckBox coachButton;

	public ViewDialog(DialogManager mgr, SoccerMaster soccerMaster) 
	{
		m_client = soccerMaster;
		m_dlgManager = mgr;

		setupViewPanel();
	}

	private SoccerMaster getClient() {
		return m_client;
	}

	private void setupViewPanel() {

		setBorder(BorderFactory.createLoweredBevelBorder());
		TitledBorder title;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// get coach ability
		JPanel coachP = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Coach Ability:");
		coachP.setBorder(title);
		coachButton = new JCheckBox("Server Controls", true);
		coachP.add(coachButton);
		add(coachP);
		add(Box.createVerticalGlue());

		// get server name
		JPanel nameP = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Server name:");
		nameP.setBorder(title);
		hostName = new JTextField(20);
		hostName.setText("localhost");
		hostName.setActionCommand("Host");
		hostName.addActionListener(this);
		nameP.add(hostName);
		add(nameP);
		add(Box.createVerticalGlue());

		// get server port
		JPanel portP = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Server port:");
		portP.setBorder(title);
		portNum = new JTextField(20);
		portNum.setText(String.valueOf(m_client.getPort()));
		portNum.setActionCommand("Port");
		portNum.addActionListener(this);
		portP.add(portNum);
		add(portP);
		add(Box.createVerticalGlue());

		// option buttons
		JButton OK = new JButton("OK");
		OK.setActionCommand("OK");
		OK.addActionListener(this);
		JButton Cancel = new JButton("Cancel");
		Cancel.setActionCommand("Cancel");
		Cancel.addActionListener(this);

		// option panel
		JPanel option = new JPanel();
		option.setLayout(new FlowLayout());
		option.add(OK);
		option.add(Cancel);
		add(option);
		add(Box.createVerticalGlue());

	}

	public void actionPerformed(ActionEvent e) {

		// get the server name and port
		if (e.getActionCommand().equals("OK")) {

			undisplay();
			;

			// link to the server and initialize the player
			init();

		} else if (e.getActionCommand().equals("Cancel"))
			undisplay();
		;
	}

	// initialize the client
	public boolean init() 
	{
		try {
			m_client.setPort(Integer.parseInt(portNum.getText()));
			m_client.setAddress(InetAddress.getByName(hostName.getText()));
			// Send the connect packet to server
			char side = ConnectData.ANYSIDE;
			if (coachButton.isSelected()) {
				side++;
			}
			ConnectData aConnectData 
						= new ConnectData(	ConnectData.VIEWER, 
											side);
			Packet connectPacket 
						= new Packet(	Packet.CONNECT, 
										aConnectData, 
										m_client.getAddress(), 
										m_client.getPort());
										
			m_client.getTransceiver().send(connectPacket);

			// wait for the connect message from server
			m_client.getTransceiver().setTimeout(1000);
			int limit = 0;
			Packet receivedPacket = null;
			while (limit < 60) {
				try {
					receivedPacket = m_client.getTransceiver().receive();
				} catch (Exception e) {
				}

				if (receivedPacket.packetType == Packet.INIT) {
					//System.gc();
					InitData aInitData = (InitData) receivedPacket.data;
					m_client.setHeartRate( aInitData.heartRate ); 
					//System.out.println("Set heartRate = " + aInitData.heartRate );
					m_client.viewer = new Cviewer( m_client );
					// ** this prioroty is higher than that of the players but 
					//    lower than that of the server.
					m_client.viewer.setPriority( Thread.NORM_PRIORITY + 1 );
					m_client.viewer.start();
					m_client.getAction((Class) PlayGameAction.class)
							.setEnabled(false);
					m_client.getAction((Class) ViewGameAction.class)
							.setEnabled(false);
					m_client.getAction((Class) LoadLogAction.class).setEnabled(
							false);
					m_client.getAction((Class) StopGameAction.class)
							.setEnabled(true);
					
					m_client.setGState( GState.CONNECTED );

					// enable the Forward button 
					JToolBar jtb = m_client.getJToolBar();
					JButton aJButton = (JButton)jtb
								.getComponentAtIndex( m_client.getFwdBtnIdx() ); 
					aJButton.setEnabled(true);
					
					break;
				}
				m_client.getTransceiver().send(connectPacket);
				limit++;
			}
			m_client.getTransceiver().setTimeout(0);
			if (receivedPacket == null) {
				JOptionPane.showMessageDialog(m_client,
						"Waiting time expired. Can not INIT.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			} else {
				return true;	
			}			
		} catch (Exception e) {
			undisplay();
			JOptionPane.showMessageDialog(m_client, e, 
					"Soccer Server may not be running.",
					JOptionPane.ERROR_MESSAGE );
			return false;
		}

	}

	public void display() {
		if (m_dialog == null) {
			m_dialog = new JDialog((Frame) null, "View TOS", true);
			m_dialog.getContentPane().setLayout(new BorderLayout());
			m_dialog.getContentPane().add(ViewDialog.this, BorderLayout.CENTER);
			m_dialog.setSize(250, 150);
			m_dialog.setResizable(false);

		}

		m_dlgManager.showDialog(m_dialog);
	}

	public void undisplay() {
		m_dlgManager.hideDialog(m_dialog);
	}

}