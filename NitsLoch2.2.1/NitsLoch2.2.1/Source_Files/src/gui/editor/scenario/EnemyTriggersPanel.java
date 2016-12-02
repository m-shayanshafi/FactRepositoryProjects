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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
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

import src.enums.Enemies;
import src.enums.StreetType;

public class EnemyTriggersPanel {

	private static JList lstTriggers;
	private static JList lstEnemies;
	private static JTextField txtName;
	private static JTextField txtImage;
	private static JTextField txtChance;
	private static JComboBox cmbEnemies;
	private static JButton btnAddEnemy;
	private static JButton btnDeleteEnemy;
	private static JButton btnAddTrigger;
	private static JButton btnDeleteTrigger;
	private static int currentTrigger;
	private static int currentEnemy;

	public static Component getEnemyTriggersPanel() {
		JPanel pnlEnemyTriggers = new JPanel();
		pnlEnemyTriggers.setLayout(new GridBagLayout());

		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1.5;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);

		lstTriggers = new JList();
		lstTriggers.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstTriggers.getSelectedIndex() != -1)
					currentTrigger = lstTriggers.getSelectedIndex()+200;
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollTriggers = new JScrollPane(lstTriggers);

		lstEnemies = new JList();
		lstEnemies.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstEnemies.getSelectedIndex() != -1) {
					String selected = (String)lstEnemies.getSelectedValue();
					for(int i = 0; i < Enemies.values().length; i++) {
						if(selected.equals(Enemies.values()[i].getName())) {
							currentEnemy = i;
							break;
						}
					}
				}
				else return;
			}
		});
		JScrollPane scrollEnemies = new JScrollPane(lstEnemies);

		JLabel lblName = new JLabel();
		lblName.setText("Name:");
		JLabel lblImage = new JLabel();
		lblImage.setText("Image:");
		JLabel lblChance = new JLabel();
		lblChance.setText("Chance:");
		JLabel lblEnemy = new JLabel();
		lblEnemy.setText("Enemy:");
		JLabel lblEnemies = new JLabel();
		lblEnemies.setText("Enemies:");

		txtName = new JTextField();
		txtName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				StreetType.values()[currentTrigger].setName(txtName.getText());
			}
		});
		txtImage = new JTextField();
		txtImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				StreetType.values()[currentTrigger].setImage(txtImage.getText());
			}
		});
		txtChance = new JTextField();
		txtChance.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StreetType.values()[currentTrigger].setChance(
							Double.parseDouble(txtChance.getText()));
				} catch(Exception ex) { }
			}
		});

		cmbEnemies = new JComboBox();

		btnAddEnemy = new JButton();
		btnAddEnemy.setText("Add Enemy");
		btnAddEnemy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					StreetType.values()[currentTrigger].getEnemies().add(
							Enemies.values()[cmbEnemies.getSelectedIndex()]);
					fillEnemiesList();
				} catch(Exception ex) { }
			}
		});

		btnDeleteEnemy = new JButton();
		btnDeleteEnemy.setText("Delete Enemy");
		btnDeleteEnemy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(StreetType.values()[currentTrigger].getEnemies().size() <= 1) return;
					StreetType.values()[currentTrigger].deleteEnemy(currentEnemy);
					fillEnemiesList();
					currentEnemy = -1;
				} catch(Exception ex) { }
			}
		});

		btnAddTrigger = new JButton();
		btnAddTrigger.setText("Add Trigger");
		btnAddTrigger.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 200; i < StreetType.values().length; i++) {
					if(!StreetType.values()[i].getUsed()) {
						ArrayList<Enemies> enemies = new ArrayList<Enemies>();
						enemies.add(Enemies.E_000);
						StreetType.values()[i].setStats("New Trigger", "trigger.png", 10.0);
						StreetType.values()[i].setEnemies(enemies);
						fillTriggerList();
						break;
					}
				}
			}
		});

		btnDeleteTrigger = new JButton();
		btnDeleteTrigger.setText("Delete Trigger");
		btnDeleteTrigger.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(int i = 200; i < StreetType.values().length; i++) {
					if(StreetType.values()[i].getUsed())
						count++;
				}
				if(count <= 1) return;

				for(int i = currentTrigger; i < StreetType.values().length-1; i++) {
					StreetType trigger1 = StreetType.values()[i];
					StreetType trigger2 = StreetType.values()[i+1];

					if(!trigger2.getUsed())
						trigger1.remove();
					else {
						String name = trigger2.getName();
						String image = trigger2.getImage();
						double chance = trigger2.getChance();
						
						trigger1.setStats(name, image, chance);
						
						ArrayList<Enemies> enemies = new ArrayList<Enemies>();
						for(Enemies e : trigger2.getEnemies()) {
							enemies.add(e);
						}
						trigger1.setEnemies(enemies);
					}
					trigger2.remove();
				}
				currentTrigger = -1;

				fillTriggerList();
			}
		});

		// Add trigger list
		pnlEnemyTriggers.add(scrollTriggers, lstObjectsC);
		
//		lstObjectsC.gridwidth = 1;
//		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 3;
//		lstObjectsC.fill = GridBagConstraints.BOTH;
		pnlEnemyTriggers.add(scrollEnemies, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);

		// Add labels
		pnlEnemyTriggers.add(lblName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlEnemyTriggers.add(lblImage, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlEnemyTriggers.add(lblChance, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlEnemyTriggers.add(lblEnemy, lstObjectsC);
		lstObjectsC.gridx = 3;
		lstObjectsC.gridy = 0;
		pnlEnemyTriggers.add(lblEnemies, lstObjectsC);

		// Add text fields
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 10, 5, 10);
		lstObjectsC.gridy = 1;
		pnlEnemyTriggers.add(txtName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlEnemyTriggers.add(txtImage, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlEnemyTriggers.add(txtChance, lstObjectsC);

		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 7;
		pnlEnemyTriggers.add(cmbEnemies, lstObjectsC);

		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;

		pnlEnemyTriggers.add(btnAddEnemy, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlEnemyTriggers.add(btnDeleteEnemy, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlEnemyTriggers.add(btnAddTrigger, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlEnemyTriggers.add(btnDeleteTrigger, lstObjectsC);

		fillInfo();

		return pnlEnemyTriggers;
	}
	
	private static void fillEnemiesList() {
		try {
			String[] enemyNames = new String[StreetType.values()[currentTrigger].getEnemies().size()];
			for(int i = 0; i < enemyNames.length; i++) {
				enemyNames[i] = StreetType.values()[currentTrigger].getEnemies().get(i).getName();
			}
			
			lstEnemies.setListData(enemyNames);
		} catch(Exception ex) { }
	}
	
	private static void fillTriggerInfo() {
		txtName.setText(StreetType.values()[currentTrigger].getName());
		txtImage.setText(StreetType.values()[currentTrigger].getImage());
		txtChance.setText(String.valueOf(StreetType.values()[currentTrigger].getChance()));
	}
	
	public static void fillEnemySelector() {
		cmbEnemies.removeAllItems();
		for(Enemies e : Enemies.values()) {
			if(e.getUsed()) {
				cmbEnemies.addItem(e.getName());
			}
		}
	}
	
	private static void fillTriggerList() {
		ArrayList<String> triggers = new ArrayList<String>();
		for(int i = 200; i < StreetType.values().length; i++) {
			if(StreetType.values()[i].getUsed()) {
				triggers.add(StreetType.values()[i].getName());
			}
		}
		lstTriggers.setListData(triggers.toArray());
	}
	
	private static void fillInfo() {
		fillTriggerList();
		fillEnemySelector();
		fillEnemiesList();
		fillTriggerInfo();
	}
}
