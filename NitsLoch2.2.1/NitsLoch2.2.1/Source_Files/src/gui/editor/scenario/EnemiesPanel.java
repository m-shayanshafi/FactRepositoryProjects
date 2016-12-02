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

import src.enums.Enemies;
import src.enums.EnemyBehavior;
import src.enums.Weapon;
import src.enums.Armor;

public class EnemiesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static JPanel pnlEnemy;
	private static JList lstEnemies;
	private static JTextField txtName;
	private static JTextField txtLeftImage;
	private static JTextField txtRightImage;
	private static JTextField txtDungeonImage;
	private static JTextField txtHitPoints;
	private static JTextField txtAbility;
	private static JTextField txtMinMoney;
	private static JTextField txtMaxMoney;

	private static JComboBox cmbWeaponType;
	private static JComboBox cmbArmorType;
	private static JComboBox cmbBehavior;

	private static JCheckBox chkProperNoun;
	private static JCheckBox chkLeader;
	private static JCheckBox chkShopkeeper;
	private static JCheckBox chkThief;

	private static JButton btnNew;
	private static JButton btnDelete;

	private static int currentEnemy = -1;

	public static JPanel getEnemyPanel() {
		pnlEnemy = new JPanel();
		pnlEnemy.setLayout(new GridBagLayout());

		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 22;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);

		// Weapons tab
		lstEnemies = new JList();
		lstEnemies.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstEnemies.getSelectedIndex() != -1)
					currentEnemy = lstEnemies.getSelectedIndex();
				else return;
				fillInfo(currentEnemy);
			}
		});
		JScrollPane scrollEnemies = new JScrollPane(lstEnemies);

		setEnemyNameList();

		JLabel lblName = new JLabel();
		lblName.setText("Enemy Name:");
		JLabel lblLeftImage = new JLabel();
		lblLeftImage.setText("Left Image:");
		JLabel lblRightImage = new JLabel();
		lblRightImage.setText("Right Image:");
		JLabel lblDungeonImage = new JLabel();
		lblDungeonImage.setText("Dungeon Image:");
		JLabel lblHitPoints = new JLabel();
		lblHitPoints.setText("Hit Points:");
		JLabel lblAbility = new JLabel();
		lblAbility.setText("Ability:");
		JLabel lblMinMoney = new JLabel();
		lblMinMoney.setText("Minimum Money:");
		JLabel lblMaxMoney = new JLabel();
		lblMaxMoney.setText("Maximum Money:");
		JLabel lblWeaponType = new JLabel();
		lblWeaponType.setText("Weapon Type:");
		JLabel lblArmorType = new JLabel();
		lblArmorType.setText("Armor Type:");
		JLabel lblBehavior = new JLabel();
		lblBehavior.setText("Behavior:");

		txtName = new JTextField();
		txtName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setName(
							txtName.getText());
				} catch(Exception ex) { }
			}
		});
		txtLeftImage = new JTextField();
		txtLeftImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setLeftImage(
							txtLeftImage.getText());
				} catch(Exception ex) { }
			}
		});
		txtRightImage = new JTextField();
		txtRightImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setRightImage(
							txtRightImage.getText());
				} catch(Exception ex) { }
			}
		});
		txtDungeonImage = new JTextField();
		txtDungeonImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setDungeonImage(
							txtDungeonImage.getText());
				} catch(Exception ex) { }
			}
		});
		txtHitPoints = new JTextField();
		txtHitPoints.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setHitPoints(
							Integer.parseInt(txtHitPoints.getText()));
				} catch(Exception ex) { }
			}
		});
		txtAbility = new JTextField();
		txtAbility.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setAbility(
							Integer.parseInt(txtAbility.getText()));
				} catch(Exception ex) { }
			}
		});
		txtMinMoney = new JTextField();
		txtMinMoney.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setMinMoney(
							Integer.parseInt(txtMinMoney.getText()));
				} catch(Exception ex) { }
			}
		});
		txtMaxMoney = new JTextField();
		txtMaxMoney.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Enemies.values()[currentEnemy].setMaxMoney(
							Integer.parseInt(txtMaxMoney.getText()));
				} catch(Exception ex) { }
			}
		});

		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addEnemy();
			}
		});

		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteEnemy();
			}
		});

		pnlEnemy.add(scrollEnemies, lstObjectsC);

		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		pnlEnemy.add(lblName, lstObjectsC);
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridy = 2;
		pnlEnemy.add(lblLeftImage, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlEnemy.add(lblRightImage, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlEnemy.add(lblDungeonImage, lstObjectsC);
		lstObjectsC.gridy = 8;
		lstObjectsC.gridwidth = 1;
		pnlEnemy.add(lblHitPoints, lstObjectsC);
		lstObjectsC.gridx = 3;
		pnlEnemy.add(lblAbility, lstObjectsC);
		lstObjectsC.gridy = 10;
		lstObjectsC.gridx = 2;
		pnlEnemy.add(lblMinMoney, lstObjectsC);
		lstObjectsC.gridx = 3;
		pnlEnemy.add(lblMaxMoney, lstObjectsC);
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 12;
		lstObjectsC.gridwidth = 2;
		pnlEnemy.add(lblWeaponType, lstObjectsC);
		lstObjectsC.gridy = 14;
		pnlEnemy.add(lblArmorType, lstObjectsC);
		lstObjectsC.gridy = 16;
		pnlEnemy.add(lblBehavior, lstObjectsC);

		// Add text fields
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 30, 10, 30);
		pnlEnemy.add(txtName, lstObjectsC);
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridy = 3;
		pnlEnemy.add(txtLeftImage, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlEnemy.add(txtRightImage, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlEnemy.add(txtDungeonImage, lstObjectsC);
		lstObjectsC.gridy = 9;
		lstObjectsC.gridwidth = 1;
		pnlEnemy.add(txtHitPoints, lstObjectsC);
		lstObjectsC.gridx = 3;
		pnlEnemy.add(txtAbility, lstObjectsC);
		lstObjectsC.gridy = 11;
		lstObjectsC.gridx = 2;
		pnlEnemy.add(txtMinMoney, lstObjectsC);
		lstObjectsC.gridx = 3;
		pnlEnemy.add(txtMaxMoney, lstObjectsC);

		// Add drop down boxes
		fillWeaponType();

		fillArmorType();

		cmbBehavior = new JComboBox();

		cmbBehavior.addItem("Innocent");
		cmbBehavior.addItem("Territorial");
		cmbBehavior.addItem("Mean");
		cmbBehavior.addItem("Restricted");
		cmbBehavior.addItem("Cowardly");
		cmbBehavior.addItem("Deceptive");
		cmbBehavior.addItem("Clingy");

		cmbBehavior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Enemies.values()[currentEnemy].setEnemyBehavior(
							EnemyBehavior.values()[cmbBehavior.getSelectedIndex()]);
				} catch (Exception ex) { }
			}
		});

		lstObjectsC.insets = new Insets(0, 30, 10, 30);
		lstObjectsC.gridy = 17;
		lstObjectsC.gridx = 2;
		lstObjectsC.gridwidth = 2;
		pnlEnemy.add(cmbBehavior, lstObjectsC);

		// Add checkboxes
		chkProperNoun = new JCheckBox();
		chkProperNoun.setText("Proper Noun");
		chkProperNoun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Enemies.values()[currentEnemy].setProperNoun(
						chkProperNoun.isSelected());
			}
		});
		
		chkLeader = new JCheckBox();
		chkLeader.setText("Leader");
		chkLeader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Enemies.values()[currentEnemy].setLeader(
						chkLeader.isSelected());
			}
		});

		chkShopkeeper = new JCheckBox();
		chkShopkeeper.setText("Shopkeeper");
		chkShopkeeper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Enemies.values()[currentEnemy].setShopkeeper(
						chkShopkeeper.isSelected());
			}
		});

		chkThief = new JCheckBox();
		chkThief.setText("Thief");
		chkThief.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Enemies.values()[currentEnemy].setThief(
						chkThief.isSelected());
			}
		});

		lstObjectsC.insets = new Insets(0, 30, 0, 0);
		lstObjectsC.gridwidth = 1;
		//lstObjectsC.fill = GridBagConstraints.EAST;
		lstObjectsC.gridx = 3;
		lstObjectsC.gridy = 1;
		pnlEnemy.add(chkProperNoun, lstObjectsC);
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 18;
		pnlEnemy.add(chkLeader, lstObjectsC);
		lstObjectsC.gridy = 19;
		pnlEnemy.add(chkShopkeeper, lstObjectsC);
		lstObjectsC.gridy = 20;
		pnlEnemy.add(chkThief, lstObjectsC);

		// Add buttons
		lstObjectsC.gridx = 3;
		lstObjectsC.insets = new Insets(0, 0, 0, 30);
		lstObjectsC.gridy = 18;
		pnlEnemy.add(btnNew, lstObjectsC);

		lstObjectsC.gridy = 20;
		pnlEnemy.add(btnDelete, lstObjectsC);

		return pnlEnemy;
	}

	protected static void fillWeaponType() {
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		if(cmbWeaponType != null) pnlEnemy.remove(cmbWeaponType);
		cmbWeaponType = new JComboBox();

		for(int i = 0; i < Weapon.values().length; i++) {
			if(!Weapon.values()[i].getItemName().equals("")) {
				cmbWeaponType.addItem(Weapon.values()[i].getItemName());
			}
		}
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridx = 2;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 30, 10, 30);
		lstObjectsC.gridy = 13;

		cmbWeaponType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Enemies.values()[currentEnemy].setWeaponType(
							Weapon.values()[cmbWeaponType.getSelectedIndex()]);
				} catch (Exception ex) { }
			}
		});

		pnlEnemy.add(cmbWeaponType, lstObjectsC);
	}

	protected static void fillArmorType() {
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		if(cmbArmorType != null) pnlEnemy.remove(cmbArmorType);
		cmbArmorType = new JComboBox();

		for(int i = 0; i < Armor.values().length; i++) {
			if(!Armor.values()[i].getItemName().equals("")) {
				cmbArmorType.addItem(Armor.values()[i].getItemName());
			}
		}

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridx = 2;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 30, 10, 30);
		lstObjectsC.gridy = 15;

		cmbArmorType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Enemies.values()[currentEnemy].setArmorType(
							Armor.values()[cmbArmorType.getSelectedIndex()]);
				} catch (Exception ex) { }
			}
		}); 

		pnlEnemy.add(cmbArmorType, lstObjectsC);
	}

	private static void setEnemyNameList() {
		ArrayList<String> enemies = new ArrayList<String>();
		for(Enemies e : Enemies.values()) {
			try {
				if(!e.getName().equals("")) {
					enemies.add(e.getName());
				}
			} catch (Exception ex) { }
		}
		lstEnemies.setListData(enemies.toArray());
	}

	private static void fillInfo(int index) {
		txtName.setText(Enemies.values()[index].getName());
		txtLeftImage.setText(Enemies.values()[index].getLeftImage());
		txtRightImage.setText(Enemies.values()[index].getRightImage());
		txtDungeonImage.setText(Enemies.values()[index].getDungeonImage());
		txtHitPoints.setText(String.valueOf(Enemies.values()[index].getMaxHitPoints()));
		txtAbility.setText(String.valueOf(Enemies.values()[index].getAbility()));
		txtMinMoney.setText(String.valueOf(Enemies.values()[index].getMinMoney()));
		txtMaxMoney.setText(String.valueOf(Enemies.values()[index].getMaxMoney()));

		chkLeader.setSelected(Enemies.values()[index].getIsLeader());
		chkShopkeeper.setSelected(Enemies.values()[index].getIsShopkeeper());
		chkThief.setSelected(Enemies.values()[index].getIsThief());
		chkProperNoun.setSelected(Enemies.values()[index].getIsProperNoun());

		//fillWeaponType();
		//fillArmorType();

		cmbWeaponType.setSelectedIndex(
				Enemies.values()[index].getWeapon().getType());
		cmbArmorType.setSelectedIndex(
				Enemies.values()[index].getArmor().getType());
		cmbBehavior.setSelectedIndex(
				Enemies.values()[index].getBehavior().getType());

		setEnemyNameList();
	}

	private static void addEnemy() {
		int newIndex = 0;
		for(int i = 0; i < Enemies.values().length; i++) {
			if(Enemies.values()[i].getUsed())
				newIndex = i+1;
			else break;
		}
		if(newIndex > Enemies.values().length) return;

		Enemies enemy = Enemies.values()[newIndex];
		enemy.setStats("New Enemy", 100, 50, 0, 0, 
				2, 0, 0, false, false, false, false, "enemyLeft.png", 
				"enemyRight.png", "enemyDungeon.png");

		setEnemyNameList();
	}

	public static void deleteEnemy() {
		if(currentEnemy == -1) return;
		int numEnemy = 0;
		for(int i = 0; i < Enemies.values().length; i++) {
			if(Enemies.values()[i].getUsed())
				numEnemy++;
		}
		if(numEnemy <= 1) return;

		for(int i = currentEnemy; i < Enemies.values().length-1; i++) {
			Enemies enemy1 = Enemies.values()[i];
			Enemies enemy2 = Enemies.values()[i+1];

			if(!enemy2.getUsed())
				enemy1.remove();
			else {
				String name;
				int hitPoints, ability, weaponIndex, armorIndex;
				int behaviorIndex, minMoney, maxMoney;
				boolean isLeader, isShopkeeper, isThief, isProperNoun;
				String leftImage, rightImage, dungeonImage;

				name = enemy2.getName();
				hitPoints = enemy2.getMaxHitPoints();
				ability = enemy2.getAbility();
				weaponIndex = enemy2.getWeapon().getType();
				armorIndex = enemy2.getArmor().getType();
				behaviorIndex = enemy2.getBehavior().getType();
				minMoney = enemy2.getMinMoney();
				maxMoney = enemy2.getMaxMoney();
				isLeader = enemy2.getIsLeader();
				isShopkeeper = enemy2.getIsShopkeeper();
				isThief = enemy2.getIsThief();
				isProperNoun = enemy2.getIsProperNoun();
				leftImage = enemy2.getLeftImage();
				rightImage = enemy2.getRightImage();
				dungeonImage = enemy2.getDungeonImage();

				enemy1.setStats(name, hitPoints, ability, weaponIndex,
						armorIndex, behaviorIndex, minMoney, maxMoney,
						isLeader, isShopkeeper, isThief, isProperNoun,
						leftImage, rightImage, dungeonImage);
			}
			enemy2.remove();
		}

		currentEnemy = -1;

		setEnemyNameList();
	}
}
