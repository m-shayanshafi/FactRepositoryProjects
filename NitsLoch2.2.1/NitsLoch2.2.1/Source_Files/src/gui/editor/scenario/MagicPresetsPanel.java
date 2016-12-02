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

import src.enums.MagicPresets;
import src.enums.StoreItems;

public class MagicPresetsPanel {

	private static JList lstMagicPresets;
	private static JList lstMagicItems;
	private static JComboBox cmbMagicItem;
	private static JButton btnAddMagic;
	private static JButton btnDeleteMagic;
	private static JTextField txtMagicPrice;
	private static JButton btnAddMagicPreset;
	private static JButton btnDeleteMagicPreset;
	private static int currentMagicPreset;
	private static int currentMagic;
	
	public static Component getMagicPresetsPanel() {
		JPanel pnlMagicPresets = new JPanel();
		pnlMagicPresets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstMagicPresets = new JList();
		lstMagicPresets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstMagicPresets.getSelectedIndex() != -1)
					currentMagicPreset = lstMagicPresets.getSelectedIndex();
				else return;
				fillMagicPresetItems();
			}
		});
		JScrollPane scrollMagicPresets = new JScrollPane(lstMagicPresets);
		
		lstMagicItems = new JList();
		lstMagicItems.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstMagicItems.getSelectedIndex() != -1) {
					String selected = (String)lstMagicItems.getSelectedValue();
					for(int i = 0; i < StoreItems.values().length; i++) {
						if(selected.equals(StoreItems.values()[i].getItemName())) {
							currentMagic = i;
							break;
						}
					}
				}
				else return;
				fillMagicPrice();
			}
		});
		JScrollPane scrollMagicItems = new JScrollPane(lstMagicItems);
		
		JLabel lblMagicItems = new JLabel();
		lblMagicItems.setText("Items:");
		JLabel lblMagicItem = new JLabel();
		lblMagicItem.setText("Item:");
		JLabel lblMagicPrice = new JLabel();
		lblMagicPrice.setText("Price:");

		txtMagicPrice = new JTextField();
		txtMagicPrice.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StoreItems.values()[currentMagic].setPrice(
							Integer.parseInt(txtMagicPrice.getText()));
					ArrayList<StoreItems> items = MagicPresets.values()[currentMagicPreset].getItems();
					for(int i = 0; i < items.size(); i++) {
						if(items.get(i).getItemName().equals(
								StoreItems.values()[currentMagic].getItemName())) {
							MagicPresets.values()[currentMagicPreset].getPrices().set(i, 
									Integer.parseInt(txtMagicPrice.getText()));
						}
					}
				} catch(Exception ex) { }
			}
		});
		
		cmbMagicItem = new JComboBox();
		
		btnAddMagic = new JButton();
		btnAddMagic.setText("Add Magic");
		btnAddMagic.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)cmbMagicItem.getSelectedItem();
				int itemID = 0;
				for(int i = 0; i < StoreItems.values().length; i++) {
					if(StoreItems.values()[i].getItemName().equals(selectedItem)) {
						itemID = i;
						break;
					}
				}
				MagicPresets.values()[currentMagicPreset].addMagic(itemID, 5);
				fillMagicPresetItems();
			}
		});
		
		btnDeleteMagic = new JButton();
		btnDeleteMagic.setText("Delete Magic");
		btnDeleteMagic.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if(MagicPresets.values()[currentMagicPreset].getItems().size() <= 1) return;
				MagicPresets.values()[currentMagicPreset].deleteMagic(currentMagic);
				fillMagicPresetItems();
			}
		});
		
		btnAddMagicPreset = new JButton();
		btnAddMagicPreset.setText("Add Preset");
		btnAddMagicPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < MagicPresets.values().length; i++) {
					if(MagicPresets.values()[i].getItems().size() == 0) {
						MagicPresets.values()[i].addMagic(0, 5);
						fillMagicPresetList();
						break;
					}
				}
			}
		});
		
		btnDeleteMagicPreset = new JButton();
		btnDeleteMagicPreset.setText("Delete Preset");
		btnDeleteMagicPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(MagicPresets preset : MagicPresets.values()) {
					if(preset.getItems().size() > 0)
						count++;
				}
				if(count <= 1) return;
				
				for(int i = currentMagicPreset; i < MagicPresets.values().length-1; i++) {
					MagicPresets pre1 = MagicPresets.values()[i];
					MagicPresets pre2 = MagicPresets.values()[i+1];
					
					pre1.setItems(pre2.getItems());
					pre2.setItems(new ArrayList<StoreItems>());
					
					fillMagicPresetList();
				}
			}
		});
		
		// Add preset list
		pnlMagicPresets.add(scrollMagicPresets, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlMagicPresets.add(lblMagicItems, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlMagicPresets.add(lblMagicItem, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlMagicPresets.add(lblMagicPrice, lstObjectsC);
		
		// Add text fields
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 12;
		pnlMagicPresets.add(txtMagicPrice, lstObjectsC);
		
		// Add item list
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 7;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		
		pnlMagicPresets.add(scrollMagicItems, lstObjectsC);
		
		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 9;
		pnlMagicPresets.add(cmbMagicItem, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlMagicPresets.add(btnAddMagic, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlMagicPresets.add(btnDeleteMagic, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlMagicPresets.add(btnAddMagicPreset, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlMagicPresets.add(btnDeleteMagicPreset, lstObjectsC);

		fillMagicInfo();
		
		return pnlMagicPresets;
	}

	private static void fillMagicPresetItems() {
		String[] itemNames = new String[MagicPresets.values()[currentMagicPreset].getItems().size()];
		for(int i = 0; i < itemNames.length; i++) {
			itemNames[i] = MagicPresets.values()[currentMagicPreset].getItems().get(i).getItemName();
		}
		lstMagicItems.setListData(itemNames);
	}
	
	private static void fillMagicPresetList() {
		ArrayList<String> presetList = new ArrayList<String>();
		int count = 0;
		for(MagicPresets preset : MagicPresets.values()) {
			if(preset.getItems().size() > 0)
				presetList.add("Preset " + count++);
		}
		lstMagicPresets.setListData(presetList.toArray());
	}
	
	private static void fillMagicInfo() {
		try {
			fillMagicPresetList();
			fillMagicPresetItems();
			
			cmbMagicItem.removeAllItems();
			for(StoreItems item : StoreItems.values()) {
				if(!item.getItemName().equals(""))
					cmbMagicItem.addItem(item.getItemName());
			}
			fillMagicPrice();
		} catch(Exception ex) { }
	}
	
	private static void fillMagicPrice() {
		txtMagicPrice.setText(String.valueOf(StoreItems.values()[currentMagic].getPrice()));
	}
}
