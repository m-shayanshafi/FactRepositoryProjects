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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.Enemies;
import src.game.DungeonSpawns;

public class DungeonSpawnsPanel {

	private static JList lstDungeonSpawns;
	private static JList lstEnemySpawns;
	private static JComboBox cmbEnemy;
	private static JButton btnAddEnemy;
	private static JButton btnDeleteEnemy;
	private static JButton btnAddDungeon;
	private static JButton btnDeleteDungeon;
	private static int currentDungeon;
	private static int currentEnemy;
	
	public static Component getDungeonSpawnsPanel() {
		JPanel pnlDungeonSpawns = new JPanel();
		pnlDungeonSpawns.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstDungeonSpawns = new JList();
		lstDungeonSpawns.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstDungeonSpawns.getSelectedIndex() != -1) {
					try {
						String dungeon = (String)lstDungeonSpawns.getSelectedValue();
						String[] components = dungeon.split("-");
						currentDungeon = Integer.parseInt(components[1]);
					} catch(Exception ex) { }
				}
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollDungeons = new JScrollPane(lstDungeonSpawns);
		
		lstEnemySpawns = new JList();
		lstEnemySpawns.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstEnemySpawns.getSelectedIndex() != -1) {
					String selected = (String)lstEnemySpawns.getSelectedValue();
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
		JScrollPane scrollEnemies = new JScrollPane(lstEnemySpawns);
		
		JLabel lblAddEnemy = new JLabel();
		lblAddEnemy.setText("Add Enemy:");
		JLabel lblEnemies = new JLabel();
		lblEnemies.setText("Enemies:");
		
		cmbEnemy = new JComboBox();
		
		btnAddEnemy = new JButton();
		btnAddEnemy.setText("Add Enemy");
		btnAddEnemy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					DungeonSpawns.getInstance().getEnemies(currentDungeon).add(
							Enemies.values()[cmbEnemy.getSelectedIndex()]);
					fillEnemyList();
				} catch (Exception ex) { }
			}
		});
		
		btnDeleteEnemy = new JButton();
		btnDeleteEnemy.setText("Delete Enemy");
		btnDeleteEnemy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(DungeonSpawns.getInstance().getEnemies(currentDungeon).size() <= 1) return;
					DungeonSpawns.getInstance().getEnemies(currentDungeon).remove(Enemies.values()[currentEnemy]);
					fillEnemyList();
				} catch(Exception ex) { }
			}
		});
		
		btnAddDungeon = new JButton();
		btnAddDungeon.setText("Add Dungeon");
		btnAddDungeon.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int levelIndex = -1;
				while(levelIndex < 0) {
					try {
						String inputValue = JOptionPane.showInputDialog("Level Index:");
						levelIndex = Integer.parseInt(inputValue);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "You must enter a valid integer.");
					}
				}
				try {
					if(DungeonSpawns.getInstance().getEnemies(levelIndex) != null &&
							DungeonSpawns.getInstance().getEnemies(levelIndex).size() > 0) {
						JOptionPane.showMessageDialog(null, "Spawns for that Dungeon already exist.");
						return;
					}
				} catch(Exception ex) { }
				
				// Add default enemy
				ArrayList<Enemies> enemies = new ArrayList<Enemies>();
				enemies.add(Enemies.E_000);
				DungeonSpawns.getInstance().setEnemies(enemies, levelIndex);
				
				fillInfo();
			}
		});
		
		btnDeleteDungeon = new JButton();
		btnDeleteDungeon.setText("Delete Dungeon");
		btnDeleteDungeon.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				DungeonSpawns.getInstance().removeEnemies(currentDungeon);
				
				fillInfo();
			}
		});
		
		// Add lists
		pnlDungeonSpawns.add(scrollDungeons, lstObjectsC);
		lstObjectsC.gridx = 4;
		lstObjectsC.gridy = 1;
		lstObjectsC.gridheight = 3;
		pnlDungeonSpawns.add(scrollEnemies, lstObjectsC);;

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlDungeonSpawns.add(lblAddEnemy, lstObjectsC);
		lstObjectsC.gridy = 0;
		lstObjectsC.gridx = 4;
		pnlDungeonSpawns.add(lblEnemies, lstObjectsC);
		
		// Add combo boxes
		lstObjectsC.insets = new Insets(0, 10, 10, 10);
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		pnlDungeonSpawns.add(cmbEnemy, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 2;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlDungeonSpawns.add(btnAddEnemy, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlDungeonSpawns.add(btnDeleteEnemy, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 3;
		pnlDungeonSpawns.add(btnAddDungeon, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlDungeonSpawns.add(btnDeleteDungeon, lstObjectsC);

		fillInfo();
		
		return pnlDungeonSpawns;
	}
	
	private static void fillInfo() {
		fillEnemySelectorList();
		fillDungeonList();
		fillEnemyList();
	}
	
	private static void fillEnemyList() {
		try {
			ArrayList<Enemies> enemies = DungeonSpawns.getInstance().getEnemies(currentDungeon);
			String[] enemyNames = new String[enemies.size()];
			for(int i = 0; i < enemyNames.length; i++) {
				enemyNames[i] = enemies.get(i).getName();
			}
			lstEnemySpawns.setListData(enemyNames);
		} catch(Exception ex) { }
	}
	
	@SuppressWarnings("unchecked")
	private static void fillDungeonList() {
		Object[] enemies = DungeonSpawns.getInstance().getEnemyArray();
		ArrayList<String> dungeons = new ArrayList<String>();
		for(int i = 0; i < enemies.length; i++) {
			if(((ArrayList<Enemies>)enemies[i]).size() > 0) {
				dungeons.add("Dungeon-" + (i+1));
			}
		}
		lstDungeonSpawns.setListData(dungeons.toArray());
	}
	
	public static void fillEnemySelectorList() {
		cmbEnemy.removeAllItems();
		for(Enemies e : Enemies.values()) {
			if(e.getUsed()) {
				cmbEnemy.addItem(e.getName());
			}
		}
	}
}
