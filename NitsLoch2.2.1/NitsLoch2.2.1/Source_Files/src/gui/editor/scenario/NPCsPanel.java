/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.gui.editor.scenario;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.NPCs;

public class NPCsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JList lstNPCs;
	private static JTextField txtName;
	private static JTextField txtImage;
	private static JTextField txtSound;
	private static JTextArea txtMessage;
	private static JButton btnNew;
	private static JButton btnDelete;
	
	private static int currentNPC;
	
	public static JPanel getNPCsPanel() {
		JPanel pnlNPCs = new JPanel();
		pnlNPCs.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = .4;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0); 
		
		// Weapons tab
		lstNPCs = new JList();
		lstNPCs.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstNPCs.getSelectedIndex() != -1)
					currentNPC = lstNPCs.getSelectedIndex();
				else return;
				fillInfo(currentNPC);
			}
		});
		JScrollPane scrollNPCs = new JScrollPane(lstNPCs);
		
		//String[] weapons = new String[Weapon.values().length];
		setNPCNameList();
		
		JLabel lblName = new JLabel();
		lblName.setText("Name:");
		JLabel lblImage = new JLabel();
		lblImage.setText("Image:");
		JLabel lblSound = new JLabel();
		lblSound.setText("Sound (Optional):");
		JLabel lblMessage = new JLabel();
		lblMessage.setText("Message:");
		
		txtName = new JTextField();
		txtName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				NPCs.values()[currentNPC].setName(
						txtName.getText());
			}
		});
		txtImage = new JTextField();
		txtImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				NPCs.values()[currentNPC].setImage(
						txtImage.getText());
			}
		});
		txtSound = new JTextField();
		txtSound.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				NPCs.values()[currentNPC].setSoundPath(
						txtSound.getText());
			}
		});
		txtMessage = new JTextArea();
		txtMessage.setWrapStyleWord(true);
		txtMessage.setLineWrap(true);
		txtMessage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				NPCs.values()[currentNPC].setMessage(
						txtMessage.getText());
			}
		});
		
		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addNPC();
			}
		});
		
		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteNPC();
			}
		});
		
		JScrollPane scrollMessage = new JScrollPane(txtMessage);
		
		pnlNPCs.add(scrollNPCs, lstObjectsC);
		
		lstObjectsC.gridheight = 10;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 7;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.insets = new Insets(0, 40, 130, 40);
		pnlNPCs.add(scrollMessage, lstObjectsC);
		
		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 0, 0, 0); 
		pnlNPCs.add(lblName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlNPCs.add(lblImage, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlNPCs.add(lblSound, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlNPCs.add(lblMessage, lstObjectsC);
		
		// Add text fields
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 40, 10, 40);
		pnlNPCs.add(txtName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlNPCs.add(txtImage, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlNPCs.add(txtSound, lstObjectsC);
		
		// Add buttons
		lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridy = 17;
		pnlNPCs.add(btnNew, lstObjectsC);
		
		lstObjectsC.gridy = 18;
		pnlNPCs.add(btnDelete, lstObjectsC);
		
		return pnlNPCs;
	}
	
	private static void setNPCNameList() {
		ArrayList<String> npcs = new ArrayList<String>();
		for(NPCs n : NPCs.values()) {
			try {
				if(!n.getName().equals("")) {
					npcs.add(n.getName());
				}
			} catch (Exception ex) { }
		}
		lstNPCs.setListData(npcs.toArray());
	}
	
	private static void fillInfo(int index) {
		txtName.setText(NPCs.values()[index].getName());
		txtImage.setText(NPCs.values()[index].getImage());
		txtSound.setText(NPCs.values()[index].getSoundPath());
		txtMessage.setText(NPCs.values()[index].getMessage());
		
		setNPCNameList();
	}
	
	private static void addNPC() {
		int newIndex = 0;
		for(int i = 0; i < NPCs.values().length; i++) {
			if(NPCs.values()[i].getUsed())
				newIndex = i+1;
			else break;
		}
		if(newIndex > 199) return;
		
		NPCs npc = NPCs.values()[newIndex];
		npc.setStats("New NPC", "Hi", "newNpc.png", "");
		
		setNPCNameList();
	}
	
	public static void deleteNPC() {
		if(currentNPC == -1) return;
		int numNPC = 0;
		for(int i = 0; i < 199; i++) {
			if(NPCs.values()[i].getUsed())
				numNPC++;
		}
		if(numNPC <= 1) return;
		
		for(int i = currentNPC; i < 199; i++) {
			NPCs npc1 = NPCs.values()[i];
			NPCs npc2 = NPCs.values()[i+1];

			if(!npc2.getUsed())
				npc1.remove();
			else {
				String name, image, sound, message;

				name = npc2.getName();
				image = npc2.getImage();
				sound = npc2.getSoundPath();
				message = npc2.getMessage();

				npc1.setStats(name, message, image, sound);
			}
			npc2.remove();
		}
		
		currentNPC = -1;
		
		setNPCNameList();
	}
}
