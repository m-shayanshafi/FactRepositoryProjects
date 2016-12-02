/* ServerDialog.java
   This class opens a dialog to set up the local server

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

import soccer.client.SoccerMaster;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ServerDialog extends JPanel implements ActionListener {

	private SoccerMaster m_client;
	private DialogManager m_dlgManager;
	JDialog m_dialog;

	private String command = "java -cp soccer.jar soccer.server.SoccerServer";
	private String property = "";
	private String port = "";
	private String others = "";

	private JTextField commandField;
	private JTextField portField;
	private JTextField propertyField;
	private JTextField othersField;

	private JTextField inputField;
	private JTextArea outputArea;

	// server runtime control
	private PrintStream ps; // process input stream
	private DataInputStream ds; // process output stream
	private OutputUpdater serverOU;

	public ServerDialog(DialogManager mgr, SoccerMaster soccerMaster) {
		m_client = soccerMaster;
		m_dlgManager = mgr;

		setupServerPanel();

	}

	private void setupServerPanel() {

		setBorder(BorderFactory.createLoweredBevelBorder());
		TitledBorder title;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel p1 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Command:");
		p1.setBorder(title);
		commandField = new JTextField(40);
		commandField.setText(command);
		commandField.setActionCommand("Command");
		commandField.addActionListener(this);
		p1.add(commandField);
		add(p1);
		add(Box.createVerticalGlue());

		// set port number
		JPanel p2 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Port number:");
		p2.setBorder(title);
		portField = new JTextField(40);
		portField.setText(port);
		portField.setActionCommand("Port");
		portField.addActionListener(this);
		p2.add(portField);
		add(p2);
		add(Box.createVerticalGlue());

		// set property file
		JPanel p3 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Property file:");
		p3.setBorder(title);
		propertyField = new JTextField(40);
		propertyField.setText(property);
		propertyField.setActionCommand("Property");
		propertyField.addActionListener(this);
		p3.add(propertyField);
		add(p3);
		add(Box.createVerticalGlue());

		// set other command parameters
		JPanel p4 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Other parameters:");
		p4.setBorder(title);
		othersField = new JTextField(40);
		othersField.setText(others);
		othersField.setActionCommand("Other");
		othersField.addActionListener(this);
		p4.add(othersField);
		add(p4);
		add(Box.createVerticalGlue());
		
		// set input field
		JPanel p5 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Input:");
		p5.setBorder(title);
		// input field
		inputField = new JTextField(40);
		inputField.setActionCommand("Input");
		inputField.addActionListener(this);
		p5.add(inputField);
		add(p5);
		add(Box.createVerticalGlue());

		// set output field
		JPanel p6 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Output:");
		p6.setBorder(title);
		// output field
		outputArea = new JTextArea(6, 80);
		outputArea.setEditable(false);
		JScrollPane scrollPane =
			new JScrollPane(
				outputArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		p6.add(scrollPane);
		add(p6);
		add(Box.createVerticalGlue());

		// option buttons
		JButton on = new JButton("On");
		on.setActionCommand("On");
		on.addActionListener(this);
		JButton off = new JButton("Off");
		off.setActionCommand("Off");
		off.addActionListener(this);
		JButton close = new JButton("Close");
		close.setActionCommand("Close");
		close.addActionListener(this);

		// option panel
		JPanel option = new JPanel();
		option.setLayout(new FlowLayout());
		option.add(on);
		option.add(off);
		option.add(close);
		add(option);
		add(Box.createVerticalGlue());

	}

	private SoccerMaster getClient() {
		return m_client;
	}

	public void actionPerformed(ActionEvent e) {
		//   
		if (e.getActionCommand().equals("On")) {
			if (serverOU != null)
				if (!serverOU.isOK())
					SoccerMaster.serverP = null;
			if (SoccerMaster.serverP != null) {
				JOptionPane.showMessageDialog(
					m_client,
					"You need to turn off the current server first.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			command = commandField.getText();
			port = portField.getText();
			property = propertyField.getText();
			others = othersField.getText();
			try {
				SoccerMaster.serverP =
					SoccerMaster.runtime.exec(
						command + " " + port + " " + property + " " + others);
				ps = new PrintStream(SoccerMaster.serverP.getOutputStream());
				ds = new DataInputStream(SoccerMaster.serverP.getInputStream());
				serverOU = new OutputUpdater(outputArea, ds, 50);
				serverOU.start();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(
					m_client,
					ex,
					"Error",
					JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals("Off")) {
			if (SoccerMaster.serverP != null) {
				try {
					outputArea.setText("");
					SoccerMaster.serverP.destroy();
					SoccerMaster.serverP = null;
					serverOU.setOK(false);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
						m_client,
						ex,
						"Error",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getActionCommand().equals("Input")) {
			if (ps != null) {
				ps.println(inputField.getText());
				ps.flush();
			}
		} else if (e.getActionCommand().equals("Close"))
			undisplay();

	}

	public void display() {
		if (m_dialog == null) {
			m_dialog = new JDialog((Frame) null, "Set Up Local Server", true);
			m_dialog.getContentPane().setLayout(new BorderLayout());
			m_dialog.getContentPane().add(
				ServerDialog.this,
				BorderLayout.CENTER);
			m_dialog.setSize(400, 350);
			m_dialog.setResizable(false);

		}

		m_dlgManager.showDialog(m_dialog);
	}

	public void undisplay() {
		m_dlgManager.hideDialog(m_dialog);
	}

}
