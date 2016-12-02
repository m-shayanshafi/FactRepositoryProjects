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
import java.util.Map;

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
import src.game.CitySpawns;
import src.game.ShopkeeperSpawns;

public class CitySpawnsPanel {

	private static JList lstCitySpawns;
	private static JList lstEnemySpawns;
	private static JList lstShopkeeperSpawns;
	private static JComboBox cmbEnemy;
	private static JComboBox cmbShopkeeper;
	private static JButton btnAddEnemy;
	private static JButton btnDeleteEnemy;
	private static JButton btnSetShopkeeper;
	private static JButton btnAddCity;
	private static JButton btnDeleteCity;
	private static int currentCity;
	private static int currentEnemy;
	
	public static Component getCitySpawnsPanel() {
		JPanel pnlCitySpawns = new JPanel();
		pnlCitySpawns.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstCitySpawns = new JList();
		lstCitySpawns.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstCitySpawns.getSelectedIndex() != -1) {
					try {
						String city = (String)lstCitySpawns.getSelectedValue();
						String[] components = city.split("-");
						currentCity = Integer.parseInt(components[1]);
					} catch(Exception ex) { }
				}
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollCities = new JScrollPane(lstCitySpawns);
		
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
		
		lstShopkeeperSpawns = new JList();
		JScrollPane scrollShopkeepers = new JScrollPane(lstShopkeeperSpawns);
		
		JLabel lblAddEnemy = new JLabel();
		lblAddEnemy.setText("Enemy:");
		JLabel lblAddShopkeeper = new JLabel();
		lblAddShopkeeper.setText("Shopkeeper:");
		JLabel lblEnemies = new JLabel();
		lblEnemies.setText("Enemies:");
		JLabel lblShopkeepers = new JLabel();
		lblShopkeepers.setText("Shopkeepers:");
		
		cmbEnemy = new JComboBox();
		cmbShopkeeper = new JComboBox();
		
		btnAddEnemy = new JButton();
		btnAddEnemy.setText("Add Enemy");
		btnAddEnemy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					CitySpawns.getInstance().getEnemies(currentCity).add(
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
					if(CitySpawns.getInstance().getEnemies(currentCity).size() <= 1) return;
					CitySpawns.getInstance().getEnemies(currentCity).remove(Enemies.values()[currentEnemy]);
					fillEnemyList();
				} catch(Exception ex) { }
			}
		});
		
		btnSetShopkeeper = new JButton();
		btnSetShopkeeper.setText("Set Shopkeeper");
		btnSetShopkeeper.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					String shopkeeperName = (String)cmbShopkeeper.getSelectedItem();
					int index = 0;
					for(Enemies e : Enemies.values()) {
						if(e.getName().equals(shopkeeperName)) {
							index = e.getType();
							break;
						}
					}
					ShopkeeperSpawns.getInstance().setShopkeeper(
							Enemies.values()[index], currentCity);
					fillShopkeeperList();
				} catch (Exception ex) { }
			}
		});
		
		btnAddCity = new JButton();
		btnAddCity.setText("Add City");
		btnAddCity.addActionListener(new ActionListener() {
			
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
				if(CitySpawns.getInstance().getEnemies(levelIndex) != null &&
						CitySpawns.getInstance().getEnemies(levelIndex).size() > 0) {
					JOptionPane.showMessageDialog(null, "Spawns for that city already exist.");
					return;
				}
				
				// Add default enemy
				ArrayList<Enemies> enemies = new ArrayList<Enemies>();
				enemies.add(Enemies.E_000);
				CitySpawns.getInstance().setEnemies(enemies, levelIndex);
				
				// Add default shopkeeper
				int shopkeeperIndex = -1;
				for(Enemies enemy : Enemies.values()) {
					if(enemy.getIsShopkeeper()) {
						shopkeeperIndex = enemy.getType();
						break;
					}
				}
				if(shopkeeperIndex == -1) {
					shopkeeperIndex = Enemies.E_000.getType();
				}
				ShopkeeperSpawns.getInstance().setShopkeeper(
						Enemies.values()[shopkeeperIndex], levelIndex);
				
				fillInfo();
			}
		});
		
		btnDeleteCity = new JButton();
		btnDeleteCity.setText("Delete City");
		btnDeleteCity.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				CitySpawns.getInstance().removeEnemies(currentCity);
				ShopkeeperSpawns.getInstance().removeShopkeeper(currentCity);
				
				fillInfo();
			}
		});
		
		// Add lists
		pnlCitySpawns.add(scrollCities, lstObjectsC);
		lstObjectsC.gridx = 4;
		lstObjectsC.gridy = 1;
		lstObjectsC.gridheight = 3;
		pnlCitySpawns.add(scrollEnemies, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlCitySpawns.add(scrollShopkeepers, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlCitySpawns.add(lblAddEnemy, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlCitySpawns.add(lblAddShopkeeper, lstObjectsC);
		lstObjectsC.gridy = 0;
		lstObjectsC.gridx = 4;
		pnlCitySpawns.add(lblEnemies, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlCitySpawns.add(lblShopkeepers, lstObjectsC);
		
		// Add combo boxes
		lstObjectsC.insets = new Insets(0, 10, 10, 10);
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		pnlCitySpawns.add(cmbEnemy, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlCitySpawns.add(cmbShopkeeper, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 2;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlCitySpawns.add(btnAddEnemy, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlCitySpawns.add(btnDeleteEnemy, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridy = 6;
		pnlCitySpawns.add(btnSetShopkeeper, lstObjectsC);
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridy = 7;
		pnlCitySpawns.add(btnAddCity, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlCitySpawns.add(btnDeleteCity, lstObjectsC);

		fillInfo();
		
		return pnlCitySpawns;
	}
	
	private static void fillInfo() {
		fillEnemySelectorList();
		fillShopkeeperSelectorList();
		fillCityList();
		fillEnemyList();
		fillShopkeeperList();
	}
	
	private static void fillEnemyList() {
		try {
			ArrayList<Enemies> enemies = CitySpawns.getInstance().getEnemies(currentCity);
			String[] enemyNames = new String[enemies.size()];
			for(int i = 0; i < enemyNames.length; i++) {
				enemyNames[i] = enemies.get(i).getName();
			}
			lstEnemySpawns.setListData(enemyNames);
		} catch(Exception ex) { }
	}
	
	private static void fillShopkeeperList() {
		try {
			Enemies shopkeeper = ShopkeeperSpawns.getInstance().getShopkeeper(currentCity);
			String[] enemyNames = new String[1];
			enemyNames[0] = shopkeeper.getName();
			lstShopkeeperSpawns.setListData(enemyNames);
		} catch(Exception ex) { }
	}
	
	private static void fillCityList() {
		Map<Integer, ArrayList<Enemies>> enemyMap = CitySpawns.getInstance().getEnemyMap();
		String[] cities = new String[enemyMap.size()];
		int count = 0;
		for(Integer i : enemyMap.keySet()) {
			cities[count++] = "City-" + i;
		}
		lstCitySpawns.setListData(cities);
	}
	
	public static void fillEnemySelectorList() {
		cmbEnemy.removeAllItems();
		for(Enemies e : Enemies.values()) {
			if(e.getUsed()) {
				cmbEnemy.addItem(e.getName());
			}
		}
	}
	
	public static void fillShopkeeperSelectorList() {
		cmbShopkeeper.removeAllItems();
		for(Enemies e : Enemies.values()) {
			if(e.getUsed() && e.getIsShopkeeper()) {
				cmbShopkeeper.addItem(e.getName());
			}
		}
	}
}
