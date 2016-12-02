/* PlayDialog.java
   This class opens a play dialog to get required information for play
   
   Copyright (C) 2001  Yu Zhang

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

package soccer.client.dialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import soccer.client.Cplayer;
import soccer.client.SoccerMaster;
import soccer.client.action.*;
import soccer.common.*;

public class PlayDialog extends JPanel implements ActionListener {

	private SoccerMaster m_client;
	private DialogManager m_dlgManager;
	JDialog m_dialog;

	private char side = 'l';

	private JTextField hostName;
	private JTextField portNum;
	private JCheckBox coachButton;

	public PlayDialog(DialogManager mgr, SoccerMaster soccerMaster) {
		m_client = soccerMaster;
		m_dlgManager = mgr;

		setupPlayPanel();
	}

	private void setupPlayPanel() {

		setBorder(BorderFactory.createLoweredBevelBorder());
		TitledBorder title;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create the radio buttons.
		JRadioButton leftButton = new JRadioButton("Left", true);
		leftButton.setActionCommand("Left");
		JRadioButton anyButton = new JRadioButton("Anyside", false);
		anyButton.setActionCommand("Anyside");
		JRadioButton rightButton = new JRadioButton("Right", false);
		rightButton.setActionCommand("Right");

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(leftButton);
		group.add(anyButton);
		group.add(rightButton);

		// Register a listener for the radio buttons.
		leftButton.addActionListener(this);
		anyButton.addActionListener(this);
		rightButton.addActionListener(this);

		// Put the radio buttons in a panel
		JPanel sideOption = new JPanel();
		sideOption.setLayout(new FlowLayout());
		sideOption.add(leftButton);
		sideOption.add(anyButton);
		sideOption.add(rightButton);
		//sideOption.setAlignmentX(LEFT_ALIGNMENT);

		title = BorderFactory.createTitledBorder("Side to join:");
		sideOption.setBorder(title);
		add(sideOption);
		add(Box.createVerticalGlue());

		// get coach ability
		JPanel coachP = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Coach Ability:");
		coachP.setBorder(title);
		coachButton = new JCheckBox("Server Controls");
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
		//option.setAlignmentX(LEFT_ALIGNMENT);
		add(option);
		add(Box.createVerticalGlue());

	}

	private SoccerMaster getClient() {
		return m_client;
	}

	public void actionPerformed(ActionEvent e) {
		// set the side form radio buttons      
		if (e.getActionCommand().equals("Left"))
			side = 'l';
		else if (e.getActionCommand().equals("Right"))
			side = 'r';
		else if (e.getActionCommand().equals("Anyside"))
			side = 'a';
		// if OK, then connect to server
		else if (e.getActionCommand().equals("OK")) {

			undisplay();
			// link to the server and initialize the player
			init();

		} else if (e.getActionCommand().equals("Cancel"))
			undisplay();
		;

	}

	// initialize the player 
	private void init() {
		try {
			m_client.setPort(Integer.parseInt(portNum.getText()));
			m_client.setAddress(InetAddress.getByName(hostName.getText()));
			// Send the connect packet to server
			if(coachButton.isSelected()) 
			{
				side++;
			} 
			ConnectData connect = new ConnectData(ConnectData.PLAYER, side);
			Packet initPacket =
				new Packet(
					Packet.CONNECT,
					connect,
					m_client.getAddress(),
					m_client.getPort());
			m_client.getTransceiver().send(initPacket);

			// wait for the connect message from server
			m_client.getTransceiver().setTimeout(1000);
			int limit = 0;
			Packet packet = null;
			while (limit < 60) {
				try {
					packet = m_client.getTransceiver().receive();
				} catch (Exception e) {
				}
				
				if (packet.packetType == Packet.INIT) 
				{
					//System.gc();
					InitData init = (InitData) packet.data;
					m_client.player = new Cplayer(init, m_client);
					m_client.player.start();
					m_client.getAction((Class) PlayGameAction.class).setEnabled(
						false);
					m_client.getAction((Class) ViewGameAction.class).setEnabled(
						false);
					m_client.getAction((Class) LoadLogAction.class).setEnabled(
						false);
					m_client.getAction((Class) StopGameAction.class).setEnabled(
						true);
					break;
				}				
				m_client.getTransceiver().send(initPacket);
				limit++;
			}

			m_client.getTransceiver().setTimeout(0);
			if (packet == null) {
				JOptionPane.showMessageDialog(
					m_client,
					"Waiting time expired. Can not INIT.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}

 
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
				m_client,
				e,
				"Error",
				JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	public void display() {
		if (m_dialog == null) {
			m_dialog = new JDialog((Frame) null, "Play TOS", true);
			m_dialog.getContentPane().setLayout(new BorderLayout());
			m_dialog.getContentPane().add(PlayDialog.this, BorderLayout.CENTER);
			m_dialog.setSize(250, 200);
			m_dialog.setResizable(false);

		}

		m_dlgManager.showDialog(m_dialog);
	}

	public void undisplay() {
		m_dlgManager.hideDialog(m_dialog);
	}

}
