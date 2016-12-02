/* AIDialog.java
   This class opens a dialog to set up the AI players

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

public class AIDialog extends JPanel implements ActionListener {

	private SoccerMaster m_client;
	private DialogManager m_dlgManager;
	JDialog m_dialog;
	private String command =
		"java -cp soccer.jar tos_teams.graviton.AIPlayers -l 10 -r 11";

	private JTextField commandField;
	private JComboBox commandBox = new JComboBox();
	private JTextField inputField;
	private JTextArea outputArea;

	// ai player runtime control
	private PrintStream ps; // process input stream
	private DataInputStream ds; // process output stream
	private ActiveCommand current = null; // current selected one  

	public AIDialog(DialogManager mgr, SoccerMaster soccerMaster) {
		m_client = soccerMaster;
		m_dlgManager = mgr;

		setupAIPanel();

	}

	private void setupAIPanel() {

		setBorder(BorderFactory.createLoweredBevelBorder());
		TitledBorder title;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// command line field
		JPanel p1 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Command Line:");
		p1.setBorder(title);
		commandField = new JTextField(40);
		commandField.setActionCommand("Command");
		commandField.setText(command);
		commandField.addActionListener(this);
		p1.add(commandField);
		add(p1);
		add(Box.createVerticalGlue());

		JPanel p2 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Current Active Command:");
		p2.setBorder(title);
		commandBox = new JComboBox();
		commandBox.setActionCommand("Active");
		commandBox.addActionListener(this);
		commandBox.setEditable(false);
		commandBox.setAlignmentX(LEFT_ALIGNMENT);
		p2.add(commandBox);
		add(p2);
		add(Box.createVerticalGlue());

		// set input field
		JPanel p3 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Input:");
		p3.setBorder(title);
		// input field
		inputField = new JTextField(40);
		inputField.setActionCommand("Input");
		inputField.addActionListener(this);
		p3.add(inputField);
		add(p3);
		add(Box.createVerticalGlue());

		// set output field
		JPanel p4 = new JPanel(new BorderLayout());
		title = BorderFactory.createTitledBorder("Output:");
		p4.setBorder(title);
		// output field
		outputArea = new JTextArea(6, 80);
		outputArea.setEditable(false);
		JScrollPane scrollPane =
			new JScrollPane(
				outputArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		p4.add(scrollPane);
		add(p4);
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
			if (SoccerMaster.activeCommands.size()
				< SoccerMaster.maxCommands) {
				command = commandField.getText();
				try {
					Process p = SoccerMaster.runtime.exec(command);
					PrintStream p_s = new PrintStream(p.getOutputStream());
					ds = new DataInputStream(p.getInputStream());
					OutputUpdater ou = new OutputUpdater(ds, 50);
					ou.start();
					if (ou.isAlive()) {
						ActiveCommand ac =
							new ActiveCommand(command, p, ou, p_s);
						commandBox.addItem(ac);
						SoccerMaster.activeCommands.addElement(ac);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
						m_client,
						ex,
						"Error",
						JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(
					m_client,
					"Max number has been reached. No more process.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (e.getActionCommand().equals("Off")) {
			ActiveCommand ac = (ActiveCommand) commandBox.getSelectedItem();
			if (ac != null) {
				try {
					ac.getProcess().destroy();
					ac.getOutputUpdater().setOK(false);
					commandBox.removeItem(ac);
					SoccerMaster.activeCommands.removeElement(ac);
				} catch (Exception ex) {
				}
			}
		} else if (e.getActionCommand().equals("Active")) {
			ActiveCommand ac = (ActiveCommand) commandBox.getSelectedItem();
			if (ac != null && !ac.equals(current)) {

				if (current != null)
					current.getOutputUpdater().setOutput(null);
				ps = ac.getPrintStream();
				outputArea.setText("");
				ac.getOutputUpdater().setOutput(outputArea);
				current = ac;
			}
		} else if (e.getActionCommand().equals("Input")) {
			if (ps != null) {
				ps.println(inputField.getText());
				ps.flush();
			}
		} else if (e.getActionCommand().equals("Close"))
			undisplay();
		;

	}

	public void display() {
		if (m_dialog == null) {
			m_dialog = new JDialog((Frame) null, "Set Up AI Players", true);
			m_dialog.getContentPane().setLayout(new BorderLayout());
			m_dialog.getContentPane().add(AIDialog.this, BorderLayout.CENTER);
			m_dialog.setSize(400, 300);
			m_dialog.setResizable(false);

		}

		m_dlgManager.showDialog(m_dialog);
	}

	public void undisplay() {
		m_dlgManager.hideDialog(m_dialog);
	}

}
