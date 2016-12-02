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
import src.enums.ArmorPresets;

public class ArmorPresetsPanel {

	private static JList lstArmorPresets;
	private static JList lstArmorItems;
	private static JComboBox cmbArmorItem;
	private static JButton btnAddArmor;
	private static JButton btnDeleteArmor;
	private static JTextField txtArmorPrice;
	private static JButton btnAddArmorPreset;
	private static JButton btnDeleteArmorPreset;
	private static int currentArmorPreset;
	private static int currentArmor;
	
	public static Component getArmorPresetsPanel() {
		JPanel pnlArmorPresets = new JPanel();
		pnlArmorPresets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstArmorPresets = new JList();
		lstArmorPresets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstArmorPresets.getSelectedIndex() != -1)
					currentArmorPreset = lstArmorPresets.getSelectedIndex();
				else return;
				fillArmorInfo();
			}
		});
		JScrollPane scrollArmorPresets = new JScrollPane(lstArmorPresets);
		
		lstArmorItems = new JList();
		lstArmorItems.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstArmorItems.getSelectedIndex() != -1) {
					String selected = (String)lstArmorItems.getSelectedValue();
					for(int i = 0; i < StoreItems.values().length; i++) {
						if(selected.equals(StoreItems.values()[i].getItemName())) {
							currentArmor = i;
							break;
						}
					}
				}
				else return;
				fillArmorPrice();
			}
		});
		JScrollPane scrollArmorItems = new JScrollPane(lstArmorItems);
		
		JLabel lblArmorItems = new JLabel();
		lblArmorItems.setText("Items:");
		JLabel lblArmorItem = new JLabel();
		lblArmorItem.setText("Item:");
		JLabel lblArmorPrice = new JLabel();
		lblArmorPrice.setText("Price:");

		txtArmorPrice = new JTextField();
		txtArmorPrice.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StoreItems.values()[currentArmor].setPrice(
							Integer.parseInt(txtArmorPrice.getText()));
					ArrayList<StoreItems> items = ArmorPresets.values()[currentArmorPreset].getItems();
					for(int i = 0; i < items.size(); i++) {
						if(items.get(i).getItemName().equals(
								StoreItems.values()[currentArmor].getItemName())) {
							ArmorPresets.values()[currentArmorPreset].getPrices().set(i, 
									Integer.parseInt(txtArmorPrice.getText()));
						}
					}
				} catch(Exception ex) { }
			}
		});
		
		cmbArmorItem = new JComboBox();
		
		btnAddArmor = new JButton();
		btnAddArmor.setText("Add Armor");
		btnAddArmor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)cmbArmorItem.getSelectedItem();
				int itemID = 0;
				for(int i = 0; i < StoreItems.values().length; i++) {
					if(StoreItems.values()[i].getItemName().equals(selectedItem)) {
						itemID = i;
						break;
					}
				}
				ArmorPresets.values()[currentArmorPreset].addArmor(itemID, 5);
				fillArmorPresetItems();
			}
		});
		
		btnDeleteArmor = new JButton();
		btnDeleteArmor.setText("Delete Armor");
		btnDeleteArmor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ArmorPresets.values()[currentArmorPreset].getItems().size() <= 1) return;
				ArmorPresets.values()[currentArmorPreset].deleteArmor(currentArmor);
				fillArmorPresetItems();
			}
		});
		
		btnAddArmorPreset = new JButton();
		btnAddArmorPreset.setText("Add Preset");
		btnAddArmorPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < ArmorPresets.values().length; i++) {
					if(ArmorPresets.values()[i].getItems().size() == 0) {
						ArmorPresets.values()[i].addArmor(0, 5);
						fillArmorPresetList();
						break;
					}
				}
			}
		});
		
		btnDeleteArmorPreset = new JButton();
		btnDeleteArmorPreset.setText("Delete Preset");
		btnDeleteArmorPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(ArmorPresets preset : ArmorPresets.values()) {
					if(preset.getItems().size() > 0)
						count++;
				}
				if(count <= 1) return;
				
				for(int i = currentArmorPreset; i < ArmorPresets.values().length-1; i++) {
					ArmorPresets pre1 = ArmorPresets.values()[i];
					ArmorPresets pre2 = ArmorPresets.values()[i+1];
					
					pre1.setItems(pre2.getItems());
					pre2.setItems(new ArrayList<StoreItems>());
					
					fillArmorPresetList();
				}
			}
		});
		
		// Add preset list
		pnlArmorPresets.add(scrollArmorPresets, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlArmorPresets.add(lblArmorItems, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlArmorPresets.add(lblArmorItem, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlArmorPresets.add(lblArmorPrice, lstObjectsC);
		
		// Add text fields
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 12;
		pnlArmorPresets.add(txtArmorPrice, lstObjectsC);
		
		// Add item list
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 7;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		
		pnlArmorPresets.add(scrollArmorItems, lstObjectsC);
		
		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 9;
		pnlArmorPresets.add(cmbArmorItem, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlArmorPresets.add(btnAddArmor, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlArmorPresets.add(btnDeleteArmor, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlArmorPresets.add(btnAddArmorPreset, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlArmorPresets.add(btnDeleteArmorPreset, lstObjectsC);

		fillArmorInfo();
		
		return pnlArmorPresets;
	}

	private static void fillArmorPresetItems() {
		String[] itemNames = new String[ArmorPresets.values()[currentArmorPreset].getItems().size()];
		for(int i = 0; i < itemNames.length; i++) {
			itemNames[i] = ArmorPresets.values()[currentArmorPreset].getItems().get(i).getItemName();
		}
		lstArmorItems.setListData(itemNames);
	}
	
	private static void fillArmorPresetList() {
		ArrayList<String> presetList = new ArrayList<String>();
		int count = 0;
		for(ArmorPresets preset : ArmorPresets.values()) {
			if(preset.getItems().size() > 0)
				presetList.add("Preset " + count++);
		}
		lstArmorPresets.setListData(presetList.toArray());
	}
	
	private static void fillArmorInfo() {
		try {
			fillArmorPresetList();
			fillArmorPresetItems();
			
			cmbArmorItem.removeAllItems();
			for(StoreItems item : StoreItems.values()) {
				if(!item.getItemName().equals(""))
					cmbArmorItem.addItem(item.getItemName());
			}
			fillArmorPrice();
		} catch(Exception ex) { }
	}
	
	private static void fillArmorPrice() {
		txtArmorPrice.setText(String.valueOf(StoreItems.values()[currentArmor].getPrice()));
	}
}
