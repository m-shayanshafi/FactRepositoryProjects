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

import src.enums.AmmoPresets;
import src.enums.StoreItems;

public class AmmoPresetsPanel {

	private static JList lstAmmoPresets;
	private static JList lstAmmoItems;
	private static JComboBox cmbAmmoItem;
	private static JButton btnAddAmmo;
	private static JButton btnDeleteAmmo;
	private static JTextField txtAmmoPrice;
	private static JButton btnAddAmmoPreset;
	private static JButton btnDeleteAmmoPreset;
	private static int currentAmmoPreset;
	private static int currentAmmo;
	
	public static Component getAmmoPresetsPanel() {
		JPanel pnlAmmoPresets = new JPanel();
		pnlAmmoPresets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstAmmoPresets = new JList();
		lstAmmoPresets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstAmmoPresets.getSelectedIndex() != -1)
					currentAmmoPreset = lstAmmoPresets.getSelectedIndex();
				else return;
				fillAmmoPresetItems();
			}
		});
		JScrollPane scrollAmmoPresets = new JScrollPane(lstAmmoPresets);
		
		lstAmmoItems = new JList();
		lstAmmoItems.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstAmmoItems.getSelectedIndex() != -1) {
					String selected = (String)lstAmmoItems.getSelectedValue();
					for(int i = 0; i < StoreItems.values().length; i++) {
						if(selected.equals(StoreItems.values()[i].getItemName())) {
							currentAmmo = i;
							break;
						}
					}
				}
				else return;
				fillAmmoPrice();
			}
		});
		JScrollPane scrollAmmoItems = new JScrollPane(lstAmmoItems);
		
		JLabel lblAmmoItems = new JLabel();
		lblAmmoItems.setText("Items:");
		JLabel lblAmmoItem = new JLabel();
		lblAmmoItem.setText("Item:");
		JLabel lblAmmoPrice = new JLabel();
		lblAmmoPrice.setText("Price:");

		txtAmmoPrice = new JTextField();
		txtAmmoPrice.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StoreItems.values()[currentAmmo].setPrice(
							Integer.parseInt(txtAmmoPrice.getText()));
					ArrayList<StoreItems> items = AmmoPresets.values()[currentAmmoPreset].getItems();
					for(int i = 0; i < items.size(); i++) {
						if(items.get(i).getItemName().equals(
								StoreItems.values()[currentAmmo].getItemName())) {
							AmmoPresets.values()[currentAmmoPreset].getPrices().set(i, 
									Integer.parseInt(txtAmmoPrice.getText()));
						}
					}
				} catch(Exception ex) { }
			}
		});
		
		cmbAmmoItem = new JComboBox();
		
		btnAddAmmo = new JButton();
		btnAddAmmo.setText("Add Ammo");
		btnAddAmmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)cmbAmmoItem.getSelectedItem();
				int itemID = 0;
				for(int i = 0; i < StoreItems.values().length; i++) {
					if(StoreItems.values()[i].getItemName().equals(selectedItem)) {
						itemID = i;
						break;
					}
				}
				AmmoPresets.values()[currentAmmoPreset].addAmmo(itemID, 5);
				fillAmmoPresetItems();
			}
		});
		
		btnDeleteAmmo = new JButton();
		btnDeleteAmmo.setText("Delete Ammo");
		btnDeleteAmmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(AmmoPresets.values()[currentAmmoPreset].getItems().size() <= 1) return;
				AmmoPresets.values()[currentAmmoPreset].deleteAmmo(currentAmmo);
				fillAmmoPresetItems();
			}
		});
		
		btnAddAmmoPreset = new JButton();
		btnAddAmmoPreset.setText("Add Preset");
		btnAddAmmoPreset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < AmmoPresets.values().length; i++) {
					if(AmmoPresets.values()[i].getItems().size() == 0) {
						AmmoPresets.values()[i].addAmmo(0, 5);
						fillAmmoPresetList();
						break;
					}
				}
			}
		});
		
		btnDeleteAmmoPreset = new JButton();
		btnDeleteAmmoPreset.setText("Delete Preset");
		btnDeleteAmmoPreset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(AmmoPresets preset : AmmoPresets.values()) {
					if(preset.getItems().size() > 0)
						count++;
				}
				if(count <= 1) return;
				
				for(int i = currentAmmoPreset; i < AmmoPresets.values().length-1; i++) {
					AmmoPresets pre1 = AmmoPresets.values()[i];
					AmmoPresets pre2 = AmmoPresets.values()[i+1];
					
					pre1.setItems(pre2.getItems());
					pre2.setItems(new ArrayList<StoreItems>());
					
					fillAmmoPresetList();
				}
			}
		});
		
		// Add preset list
		pnlAmmoPresets.add(scrollAmmoPresets, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlAmmoPresets.add(lblAmmoItems, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlAmmoPresets.add(lblAmmoItem, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlAmmoPresets.add(lblAmmoPrice, lstObjectsC);
		
		// Add text fields
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 12;
		pnlAmmoPresets.add(txtAmmoPrice, lstObjectsC);
		
		// Add item list
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 7;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		
		pnlAmmoPresets.add(scrollAmmoItems, lstObjectsC);
		
		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 9;
		pnlAmmoPresets.add(cmbAmmoItem, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlAmmoPresets.add(btnAddAmmo, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlAmmoPresets.add(btnDeleteAmmo, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlAmmoPresets.add(btnAddAmmoPreset, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlAmmoPresets.add(btnDeleteAmmoPreset, lstObjectsC);

		fillAmmoInfo();
		
		return pnlAmmoPresets;
	}

	private static void fillAmmoPresetItems() {
		String[] itemNames = new String[AmmoPresets.values()[currentAmmoPreset].getItems().size()];
		for(int i = 0; i < itemNames.length; i++) {
			itemNames[i] = AmmoPresets.values()[currentAmmoPreset].getItems().get(i).getItemName();
		}
		lstAmmoItems.setListData(itemNames);
	}
	
	private static void fillAmmoPresetList() {
		ArrayList<String> presetList = new ArrayList<String>();
		int count = 0;
		for(AmmoPresets preset : AmmoPresets.values()) {
			if(preset.getItems().size() > 0)
				presetList.add("Preset " + count++);
		}
		lstAmmoPresets.setListData(presetList.toArray());
	}
	
	private static void fillAmmoInfo() {
		try {
			fillAmmoPresetList();
			fillAmmoPresetItems();
			
			cmbAmmoItem.removeAllItems();
			for(StoreItems item : StoreItems.values()) {
				if(!item.getItemName().equals(""))
					cmbAmmoItem.addItem(item.getItemName());
			}
			fillAmmoPrice();
		} catch(Exception ex) { }
	}
	
	private static void fillAmmoPrice() {
		txtAmmoPrice.setText(String.valueOf(StoreItems.values()[currentAmmo].getPrice()));
	}
}
