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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.Armor;

public class ArmorPanel {
	
	private static JList lstArmor;
	private static JTextField txtItemName;
	private static JTextField txtSecondName;
	private static JTextField txtMelee;
	private static JTextField txtMartialArts;
	private static JTextField txtMarksman;
	private static JTextField txtFlame;
	private static JTextField txtOther;
	private static JCheckBox chkCanBreak;
	private static JButton btnNew;
	private static JButton btnDelete;
	
	private static int currentArmor = -1;

	public static JPanel getArmorPanel() {
		JPanel pnlArmor = new JPanel();
		pnlArmor.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 0;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Weapons tab
		lstArmor = new JList();
		lstArmor.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstArmor.getSelectedIndex() != -1)
					currentArmor = lstArmor.getSelectedIndex();
				else return;
				fillInfo(currentArmor);
			}
		});
		JScrollPane scrollArmor = new JScrollPane(lstArmor);
		
		setArmorNameList();
		
		JLabel lblItemName = new JLabel();
		lblItemName.setText("Item Name:");
		JLabel lblSecondName = new JLabel();
		lblSecondName.setText("Second Name:");
		JLabel lblMelee = new JLabel();
		lblMelee.setText("Melee:");
		JLabel lblMartialArts = new JLabel();
		lblMartialArts.setText("Martial Arts:");
		JLabel lblMarksman = new JLabel();
		lblMarksman.setText("Marksman:");
		JLabel lblFlame = new JLabel();
		lblFlame.setText("Flame:");
		JLabel lblOther = new JLabel();
		lblOther.setText("Other");
		
		txtItemName = new JTextField();
		txtItemName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Armor.values()[currentArmor].setItemName(
						txtItemName.getText());
				EnemiesPanel.fillArmorType();
			}
		});
		txtSecondName = new JTextField();
		txtSecondName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Armor.values()[currentArmor].setSecondaryName(
						txtSecondName.getText());
			}
		});
		txtMelee = new JTextField();
		txtMelee.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Armor.values()[currentArmor].setMelee(
							Integer.valueOf(txtMelee.getText()));
				} catch(Exception ex) { }
			}
		});
		txtMartialArts = new JTextField();
		txtMartialArts.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Armor.values()[currentArmor].setMartialArts(
							Integer.valueOf(txtMartialArts.getText()));
				} catch(Exception ex) { }
			}
		});
		txtMarksman = new JTextField();
		txtMarksman.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Armor.values()[currentArmor].setMarksman(
							Integer.valueOf(txtMarksman.getText()));
				} catch(Exception ex) { }
			}
		});
		txtFlame = new JTextField();
		txtFlame.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Armor.values()[currentArmor].setFlame(
							Integer.valueOf(txtFlame.getText()));
				} catch(Exception ex) { }
			}
		});
		txtOther = new JTextField();
		txtOther.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Armor.values()[currentArmor].setOther(
							Integer.valueOf(txtOther.getText()));
				} catch(Exception ex) { }
			}
		});
		
		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addArmor();
			}
		});
		
		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteArmor();
			}
		});
		
		pnlArmor.add(scrollArmor, lstObjectsC);
		
		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		pnlArmor.add(lblItemName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlArmor.add(lblSecondName, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlArmor.add(lblMelee, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlArmor.add(lblMartialArts, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlArmor.add(lblMarksman, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlArmor.add(lblFlame, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlArmor.add(lblOther, lstObjectsC);
		
		// Add text fields
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 80, 10, 80);
		pnlArmor.add(txtItemName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlArmor.add(txtSecondName, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlArmor.add(txtMelee, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlArmor.add(txtMartialArts, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlArmor.add(txtMarksman, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlArmor.add(txtFlame, lstObjectsC);
		lstObjectsC.gridy = 13;
		pnlArmor.add(txtOther, lstObjectsC);
		
		// Add checkboxes
		chkCanBreak = new JCheckBox();
		chkCanBreak.setText("Can break");
		chkCanBreak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Armor.values()[currentArmor].setCanBeBroken(
						chkCanBreak.isSelected());
			}
		});
		
		lstObjectsC.insets = new Insets(0, 80, 0, 0);
		//lstObjectsC.fill = GridBagConstraints.EAST;
		lstObjectsC.gridy = 14;
		pnlArmor.add(chkCanBreak, lstObjectsC);
		
		// Add buttons
		lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridy = 15;
		pnlArmor.add(btnNew, lstObjectsC);
		
		lstObjectsC.gridy = 16;
		pnlArmor.add(btnDelete, lstObjectsC);
		
		return pnlArmor;
	}
	
	private static void setArmorNameList() {
		ArrayList<String> armor = new ArrayList<String>();
		for(Armor a : Armor.values()) {
			if(!a.getItemName().equals("")) {
				armor.add(a.getItemName());
			}
		}
		lstArmor.setListData(armor.toArray());
	}
	
	private static void fillInfo(int index) {
		txtItemName.setText(Armor.values()[index].getItemName());
		txtSecondName.setText(Armor.values()[index].getSecondaryName());
		txtMelee.setText(String.valueOf(Armor.values()[index].getAbsorbMelee()));
		txtMartialArts.setText(String.valueOf(Armor.values()[index].getAbsorbMartialArts()));
		txtMarksman.setText(String.valueOf(Armor.values()[index].getAbsorbMarksman()));
		txtFlame.setText(String.valueOf(Armor.values()[index].getAbsorbFlame()));
		txtOther.setText(String.valueOf(Armor.values()[index].getAbsorbOther()));
		
		chkCanBreak.setSelected(Armor.values()[index].canBeDestroyed());
		
		setArmorNameList();
	}
	
	private static void addArmor() {
		int newIndex = 0;
		for(int i = 0; i < Armor.values().length; i++) {
			if(!Armor.values()[i].getItemName().equals(""))
				newIndex = i+1;
			else break;
		}
		if(newIndex > 24) return;
		
		Armor armor = Armor.values()[newIndex];
		armor.setStats("New Armor", "some armor", 5, 5, 5, 5, 5, true);
		
		setArmorNameList();
	}
	
	public static void deleteArmor() {
		if(currentArmor == -1) return;
		int numArmor = 0;
		for(int i = 0; i < 24; i++) {
			if(!Armor.values()[i].getItemName().equals(""))
				numArmor++;
		}
		if(numArmor <= 1) return;
		
		for(int i = currentArmor; i < 24; i++) {
			Armor armor1 = Armor.values()[i];
			Armor armor2 = Armor.values()[i+1];
			
			if(armor2.getItemName().equals(""))
				armor1.remove();
			else {

				String name, secondName;
				int absMelee, absMarts, absMarks, absFlame, absOther;
				boolean canBreak;

				name = armor2.getItemName();
				secondName = armor2.getSecondaryName();
				absMelee = armor2.getAbsorbMelee();
				absMarts = armor2.getAbsorbMartialArts();
				absMarks = armor2.getAbsorbMarksman();
				absFlame = armor2.getAbsorbFlame();
				absOther = armor2.getAbsorbOther();
				canBreak = armor2.canBeDestroyed();

				armor1.setStats(name, secondName, absMelee, absMarts, 
						absMarks, absFlame, absOther, canBreak);
			}
			armor2.remove();
		}

		currentArmor = -1;
		
		setArmorNameList();
	}
}
