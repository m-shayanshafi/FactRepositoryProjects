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

import src.enums.StoreItems;
import src.enums.WeaponPresets;

public class WeaponPresetsPanel {
	
	private static JList lstWeaponPresets;
	private static JList lstWeaponItems;
	private static JComboBox cmbWeaponItem;
	private static JButton btnAddWeapon;
	private static JButton btnDeleteWeapon;
	private static JTextField txtWeaponPrice;
	private static JButton btnAddWeaponPreset;
	private static JButton btnDeleteWeaponPreset;
	private static int currentWeaponPreset;
	private static int currentWeapon;

	public static Component getWeaponPresetsPanel() {
		JPanel pnlWeaponPresets = new JPanel();
		pnlWeaponPresets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstWeaponPresets = new JList();
		lstWeaponPresets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstWeaponPresets.getSelectedIndex() != -1)
					currentWeaponPreset = lstWeaponPresets.getSelectedIndex();
				else return;
				fillWeaponInfo();
			}
		});
		JScrollPane scrollWeaponPresets = new JScrollPane(lstWeaponPresets);
		
		lstWeaponItems = new JList();
		lstWeaponItems.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstWeaponItems.getSelectedIndex() != -1) {
					String selected = (String)lstWeaponItems.getSelectedValue();
					for(int i = 0; i < StoreItems.values().length; i++) {
						if(selected.equals(StoreItems.values()[i].getItemName())) {
							currentWeapon = i;
							break;
						}
					}
				}
				else return;
				fillWeaponPrice();
			}
		});
		JScrollPane scrollWeaponItems = new JScrollPane(lstWeaponItems);
		
		JLabel lblWeaponItems = new JLabel();
		lblWeaponItems.setText("Items:");
		JLabel lblWeaponItem = new JLabel();
		lblWeaponItem.setText("Item:");
		JLabel lblWeaponPrice = new JLabel();
		lblWeaponPrice.setText("Price:");

		txtWeaponPrice = new JTextField();
		txtWeaponPrice.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StoreItems.values()[currentWeapon].setPrice(
							Integer.parseInt(txtWeaponPrice.getText()));
					ArrayList<StoreItems> items = WeaponPresets.values()[currentWeaponPreset].getItems();
					for(int i = 0; i < items.size(); i++) {
						if(items.get(i).getItemName().equals(
								StoreItems.values()[currentWeapon].getItemName())) {
							WeaponPresets.values()[currentWeaponPreset].getPrices().set(i, 
									Integer.parseInt(txtWeaponPrice.getText()));
						}
					}
				} catch(Exception ex) { }
			}
		});
		
		cmbWeaponItem = new JComboBox();
		
		btnAddWeapon = new JButton();
		btnAddWeapon.setText("Add Weapon");
		btnAddWeapon.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)cmbWeaponItem.getSelectedItem();
				int itemID = 0;
				for(int i = 0; i < StoreItems.values().length; i++) {
					if(StoreItems.values()[i].getItemName().equals(selectedItem)) {
						itemID = i;
						break;
					}
				}
				WeaponPresets.values()[currentWeaponPreset].addWeapon(itemID, 5);
				fillWeaponPresetItems();
			}
		});
		
		btnDeleteWeapon = new JButton();
		btnDeleteWeapon.setText("Delete Weapon");
		btnDeleteWeapon.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if(WeaponPresets.values()[currentWeaponPreset].getItems().size() <= 1) return;
				WeaponPresets.values()[currentWeaponPreset].deleteWeapon(currentWeapon);
				fillWeaponPresetItems();
			}
		});
		
		btnAddWeaponPreset = new JButton();
		btnAddWeaponPreset.setText("Add Preset");
		btnAddWeaponPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < WeaponPresets.values().length; i++) {
					if(WeaponPresets.values()[i].getItems().size() == 0) {
						WeaponPresets.values()[i].addWeapon(0, 5);
						fillWeaponPresetList();
						break;
					}
				}
			}
		});
		
		btnDeleteWeaponPreset = new JButton();
		btnDeleteWeaponPreset.setText("Delete Preset");
		btnDeleteWeaponPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(WeaponPresets preset : WeaponPresets.values()) {
					if(preset.getItems().size() > 0)
						count++;
				}
				if(count <= 1) return;
				
				for(int i = currentWeaponPreset; i < WeaponPresets.values().length-1; i++) {
					WeaponPresets pre1 = WeaponPresets.values()[i];
					WeaponPresets pre2 = WeaponPresets.values()[i+1];
					
					pre1.setItems(pre2.getItems());
					pre2.setItems(new ArrayList<StoreItems>());
					
					fillWeaponPresetList();
				}
			}
		});
		
		// Add preset list
		pnlWeaponPresets.add(scrollWeaponPresets, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlWeaponPresets.add(lblWeaponItems, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlWeaponPresets.add(lblWeaponItem, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlWeaponPresets.add(lblWeaponPrice, lstObjectsC);
		
		// Add text fields
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 12;
		pnlWeaponPresets.add(txtWeaponPrice, lstObjectsC);
		
		// Add item list
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 7;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		
		pnlWeaponPresets.add(scrollWeaponItems, lstObjectsC);
		
		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 9;
		pnlWeaponPresets.add(cmbWeaponItem, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlWeaponPresets.add(btnAddWeapon, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlWeaponPresets.add(btnDeleteWeapon, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlWeaponPresets.add(btnAddWeaponPreset, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlWeaponPresets.add(btnDeleteWeaponPreset, lstObjectsC);

		fillWeaponInfo();
		
		return pnlWeaponPresets;
	}

	private static void fillWeaponPresetItems() {
		String[] itemNames = new String[WeaponPresets.values()[currentWeaponPreset].getItems().size()];
		for(int i = 0; i < itemNames.length; i++) {
			itemNames[i] = WeaponPresets.values()[currentWeaponPreset].getItems().get(i).getItemName();
		}
		lstWeaponItems.setListData(itemNames);
	}
	
	private static void fillWeaponPresetList() {
		ArrayList<String> presetList = new ArrayList<String>();
		int count = 0;
		for(WeaponPresets preset : WeaponPresets.values()) {
			if(preset.getItems().size() > 0)
				presetList.add("Preset " + count++);
		}
		lstWeaponPresets.setListData(presetList.toArray());
	}
	
	private static void fillWeaponInfo() {
		try {
			fillWeaponPresetList();
			fillWeaponPresetItems();
			
			cmbWeaponItem.removeAllItems();
			for(StoreItems item : StoreItems.values()) {
				if(!item.getItemName().equals(""))
					cmbWeaponItem.addItem(item.getItemName());
			}
			fillWeaponPrice();
		} catch(Exception ex) { }
	}
	
	private static void fillWeaponPrice() {
		txtWeaponPrice.setText(String.valueOf(StoreItems.values()[currentWeapon].getPrice()));
	}
}
