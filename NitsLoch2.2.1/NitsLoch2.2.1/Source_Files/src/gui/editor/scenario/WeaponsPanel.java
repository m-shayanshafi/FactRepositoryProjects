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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.DamageType;
import src.enums.PlayerImages;
import src.enums.Weapon;

public class WeaponsPanel {
	private static JList lstWeapons;
	private static JTextField txtItemName;
	private static JTextField txtSecondName;
	private static JTextField txtVerb;
	private static JTextField txtDamage;
	private static JComboBox cmbDamageType;
	private static JCheckBox chkCanBreak;
	private static JCheckBox chkUsesRockets;
	private static JCheckBox chkUsesFlamePacks;
	private static JTextField txtPlayerLeftImage;
	private static JTextField txtPlayerRightImage;
	private static JButton btnNew;
	private static JButton btnDelete;
	
	private static int currentWeapon = -1;
	
	public static JPanel getWeaponsPanel() {
		JPanel pnlWeapons = new JPanel();
		pnlWeapons.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 2000.0;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Weapons tab
		lstWeapons = new JList();
		lstWeapons.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstWeapons.getSelectedIndex() != -1)
					currentWeapon = lstWeapons.getSelectedIndex();
				else return;
				fillInfo(currentWeapon);
			}
		});
		JScrollPane scrollWeapons = new JScrollPane(lstWeapons);
		
		//String[] weapons = new String[Weapon.values().length];
		setWeaponNameList();
		
		JLabel lblItemName = new JLabel();
		lblItemName.setText("Item Name:");
		JLabel lblSecondName = new JLabel();
		lblSecondName.setText("Second Name:");
		JLabel lblVerb = new JLabel();
		lblVerb.setText("Verb:");
		JLabel lblDamage = new JLabel();
		lblDamage.setText("Damage:");
		JLabel lblDamageType = new JLabel();
		lblDamageType.setText("Damage Type:");
		JLabel lblPlayerLeftImage = new JLabel();
		lblPlayerLeftImage.setText("Player Left Image:");
		JLabel lblPlayerRightImage = new JLabel();
		lblPlayerRightImage.setText("Player Right Image:");
		
		txtItemName = new JTextField();
		txtItemName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Weapon.values()[currentWeapon].setItemName(
						txtItemName.getText());
				EnemiesPanel.fillWeaponType();
			}
		});
		txtSecondName = new JTextField();
		txtSecondName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Weapon.values()[currentWeapon].setSecondaryName(
						txtSecondName.getText());
			}
		});
		txtVerb = new JTextField();
		txtVerb.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Weapon.values()[currentWeapon].setVerb(
						txtVerb.getText());
			}
		});
		txtDamage = new JTextField();
		txtDamage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Weapon.values()[currentWeapon].setDamage(
							Integer.parseInt(txtDamage.getText()));
				} catch(Exception ex) {}
			}
		});
		txtPlayerLeftImage = new JTextField();
		txtPlayerLeftImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				PlayerImages.values()[currentWeapon].setLeftImage(
						txtPlayerLeftImage.getText());
			}
		});
		txtPlayerRightImage = new JTextField();
		txtPlayerRightImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				PlayerImages.values()[currentWeapon].setRightImage(
						txtPlayerRightImage.getText());
			}
		});
		
		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addWeapon();
			}
		});
		
		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteWeapon();
			}
		});
		
		pnlWeapons.add(scrollWeapons, lstObjectsC);
		
		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		pnlWeapons.add(lblItemName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlWeapons.add(lblSecondName, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlWeapons.add(lblVerb, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlWeapons.add(lblDamage, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlWeapons.add(lblDamageType, lstObjectsC);
		lstObjectsC.gridy = 13;
		pnlWeapons.add(lblPlayerLeftImage, lstObjectsC);
		lstObjectsC.gridy = 15;
		pnlWeapons.add(lblPlayerRightImage, lstObjectsC);
		
		// Add text fields
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 80, 10, 80);
		pnlWeapons.add(txtItemName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlWeapons.add(txtSecondName, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlWeapons.add(txtVerb, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlWeapons.add(txtDamage, lstObjectsC);
		lstObjectsC.gridy = 14;
		pnlWeapons.add(txtPlayerLeftImage, lstObjectsC);
		lstObjectsC.gridy = 16;
		pnlWeapons.add(txtPlayerRightImage, lstObjectsC);
		
		// Add damage type drop down
		cmbDamageType = new JComboBox();
		
		cmbDamageType.addItem("Melee");
		cmbDamageType.addItem("Melee breaks weapons");
		cmbDamageType.addItem("Melee destroys armor");
		cmbDamageType.addItem("Melee breaks weapons and destroys armor");
		cmbDamageType.addItem("Martial arts");
		cmbDamageType.addItem("Martial arts breaks weapons");
		cmbDamageType.addItem("Martial arts destroys armor");
		cmbDamageType.addItem("Martial arts breaks weapons and destroys armor");
		cmbDamageType.addItem("Marksman");
		cmbDamageType.addItem("Marksman breaks weapons");
		cmbDamageType.addItem("Marksman destroys armor");
		cmbDamageType.addItem("Marksman breaks weapons and destroys armor");
		cmbDamageType.addItem("Flame destroys money");
		cmbDamageType.addItem("Flame destroys nothing");
		cmbDamageType.addItem("Other destroys money and items");
		cmbDamageType.addItem("Other destroys money");
		cmbDamageType.addItem("Other destroys nothing");
		
		cmbDamageType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Weapon.values()[currentWeapon].setDamageType(
						DamageType.values()[cmbDamageType.getSelectedIndex()]);
			}
		});
		lstObjectsC.insets = new Insets(0, 50, 10, 50);
		lstObjectsC.gridy = 9;
		pnlWeapons.add(cmbDamageType, lstObjectsC);
		
		// Add checkboxes
		chkCanBreak = new JCheckBox();
		chkCanBreak.setText("Can break");
		chkCanBreak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Weapon.values()[currentWeapon].setCanBeBroken(
						chkCanBreak.isSelected());
			}
		});
		chkUsesRockets = new JCheckBox();
		chkUsesRockets.setText("Uses rockets");
		chkUsesRockets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Weapon.values()[currentWeapon].setUsesRockets(
						chkUsesRockets.isSelected());
			}
		});
		chkUsesFlamePacks = new JCheckBox();
		chkUsesFlamePacks.setText("Uses flame packs");
		chkUsesFlamePacks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Weapon.values()[currentWeapon].setUsesFlamePacks(
						chkUsesFlamePacks.isSelected());
			}
		});
		
		lstObjectsC.insets = new Insets(0, 50, 0, 0);
		//lstObjectsC.fill = GridBagConstraints.EAST;
		lstObjectsC.gridy = 10;
		pnlWeapons.add(chkCanBreak, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlWeapons.add(chkUsesRockets, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlWeapons.add(chkUsesFlamePacks, lstObjectsC);
		
		// Add buttons
		lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridy = 17;
		pnlWeapons.add(btnNew, lstObjectsC);
		
		lstObjectsC.gridy = 18;
		pnlWeapons.add(btnDelete, lstObjectsC);
		
		return pnlWeapons;
	}

	private static void setWeaponNameList() {
		ArrayList<String> weapons = new ArrayList<String>();
		for(Weapon w : Weapon.values()) {
			if(!w.getItemName().equals("")) {
				weapons.add(w.getItemName());
			}
		}
		lstWeapons.setListData(weapons.toArray());
	}
	
	private static void fillInfo(int index) {
		txtItemName.setText(Weapon.values()[index].getItemName());
		txtSecondName.setText(Weapon.values()[index].getSecondaryName());
		txtVerb.setText(Weapon.values()[index].getVerb());
		txtDamage.setText(String.valueOf(Weapon.values()[index].getDamage()));
		txtPlayerLeftImage.setText(PlayerImages.values()[index].getLeftImage());
		txtPlayerRightImage.setText(PlayerImages.values()[index].getRightImage());
		
		cmbDamageType.setSelectedIndex(
				Weapon.values()[index].getDamageType().getType());
		
		chkCanBreak.setSelected(Weapon.values()[index].canBeBroken());
		chkUsesRockets.setSelected(Weapon.values()[index].usesRockets());
		chkUsesFlamePacks.setSelected(Weapon.values()[index].usesFlamePacks());
		
		setWeaponNameList();
	}
	
	private static void addWeapon() {
		int newIndex = 0;
		for(int i = 0; i < Weapon.values().length; i++) {
			if(!Weapon.values()[i].getItemName().equals(""))
				newIndex = i+1;
			else break;
		}
		if(newIndex > 24) return;
		
		Weapon wep = Weapon.values()[newIndex];
		wep.setStats("New Weapon", "a weapon", "hit", 5, 
				DamageType.MELEE_NO_BREAK_NO_PIERCE, true, false, false);
		PlayerImages.values()[newIndex].setImages("left.png", "right.png");
		
		setWeaponNameList();
	}
	
	public static void deleteWeapon() {
		if(currentWeapon == -1) return;
		int numWeapons = 0;
		for(int i = 0; i < 24; i++) {
			if(!Weapon.values()[i].getItemName().equals(""))
				numWeapons++;
		}
		if(numWeapons <= 1) return;
		
		for(int i = currentWeapon; i < 24; i++) {
			Weapon wep1 = Weapon.values()[i];
			Weapon wep2 = Weapon.values()[i+1];
			
			String name, secondName, verb;
			int damage;
			DamageType damType;
			boolean canBreak, usesRockets, usesFlamePacks;
			
			name = wep2.getItemName();
			secondName = wep2.getSecondaryName();
			verb = wep2.getVerb();
			damage = wep2.getDamage();
			damType = wep2.getDamageType();
			canBreak = wep2.canBeBroken();
			usesRockets = wep2.usesRockets();
			usesFlamePacks = wep2.usesFlamePacks();
			
			wep1.setStats(name, secondName, verb, damage, 
					damType, canBreak, usesRockets, usesFlamePacks);
			
			PlayerImages img1 = PlayerImages.values()[i];
			PlayerImages img2 = PlayerImages.values()[i+1];
			
			String left, right;
			left = img2.getLeftImage();
			right = img2.getRightImage();
			
			img1.setImages(left, right);
		}
		
		currentWeapon = -1;
		
		setWeaponNameList();
	}

}
