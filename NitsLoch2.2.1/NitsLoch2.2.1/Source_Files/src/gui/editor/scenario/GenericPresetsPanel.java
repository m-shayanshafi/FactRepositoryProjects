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

import src.enums.GenericPresets;
import src.enums.StoreItems;

public class GenericPresetsPanel {

	private static JList lstGenericPresets;
	private static JList lstGenericItems;
	private static JComboBox cmbGenericItem;
	private static JButton btnAddGeneric;
	private static JButton btnDeleteGeneric;
	private static JTextField txtGenericPrice;
	private static JButton btnAddGenericPreset;
	private static JButton btnDeleteGenericPreset;
	private static int currentGenericPreset;
	private static int currentGeneric;
	
	public static Component getGenericPresetsPanel() {
		JPanel pnlGenericPresets = new JPanel();
		pnlGenericPresets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstGenericPresets = new JList();
		lstGenericPresets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstGenericPresets.getSelectedIndex() != -1)
					currentGenericPreset = lstGenericPresets.getSelectedIndex();
				else return;
				fillGenericInfo();
			}
		});
		JScrollPane scrollGenericPresets = new JScrollPane(lstGenericPresets);
		
		lstGenericItems = new JList();
		lstGenericItems.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstGenericItems.getSelectedIndex() != -1) {
					String selected = (String)lstGenericItems.getSelectedValue();
					for(int i = 0; i < StoreItems.values().length; i++) {
						if(selected.equals(StoreItems.values()[i].getItemName())) {
							currentGeneric = i;
							break;
						}
					}
				}
				else return;
				fillGenericPrice();
			}
		});
		JScrollPane scrollGenericItems = new JScrollPane(lstGenericItems);
		
		JLabel lblGenericItems = new JLabel();
		lblGenericItems.setText("Items:");
		JLabel lblGenericItem = new JLabel();
		lblGenericItem.setText("Item:");
		JLabel lblGenericPrice = new JLabel();
		lblGenericPrice.setText("Price:");

		txtGenericPrice = new JTextField();
		txtGenericPrice.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StoreItems.values()[currentGeneric].setPrice(
							Integer.parseInt(txtGenericPrice.getText()));
					ArrayList<StoreItems> items = GenericPresets.values()[currentGenericPreset].getItems();
					for(int i = 0; i < items.size(); i++) {
						if(items.get(i).getItemName().equals(
								StoreItems.values()[currentGeneric].getItemName())) {
							GenericPresets.values()[currentGenericPreset].getPrices().set(i, 
									Integer.parseInt(txtGenericPrice.getText()));
						}
					}
				} catch(Exception ex) { }
			}
		});
		
		cmbGenericItem = new JComboBox();
		
		btnAddGeneric = new JButton();
		btnAddGeneric.setText("Add Generic");
		btnAddGeneric.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)cmbGenericItem.getSelectedItem();
				int itemID = 0;
				for(int i = 0; i < StoreItems.values().length; i++) {
					if(StoreItems.values()[i].getItemName().equals(selectedItem)) {
						itemID = i;
						break;
					}
				}
				GenericPresets.values()[currentGenericPreset].addGenericItem(itemID, 5);
				fillGenericPresetItems();
			}
		});
		
		btnDeleteGeneric = new JButton();
		btnDeleteGeneric.setText("Delete Generic");
		btnDeleteGeneric.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if(GenericPresets.values()[currentGenericPreset].getItems().size() <= 1) return;
				GenericPresets.values()[currentGenericPreset].deleteGenericItem(currentGeneric);
				fillGenericPresetItems();
			}
		});
		
		btnAddGenericPreset = new JButton();
		btnAddGenericPreset.setText("Add Preset");
		btnAddGenericPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < GenericPresets.values().length; i++) {
					if(GenericPresets.values()[i].getItems().size() == 0) {
						GenericPresets.values()[i].addGenericItem(0, 5);
						fillGenericPresetList();
						break;
					}
				}
			}
		});
		
		btnDeleteGenericPreset = new JButton();
		btnDeleteGenericPreset.setText("Delete Preset");
		btnDeleteGenericPreset.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				for(GenericPresets preset : GenericPresets.values()) {
					if(preset.getItems().size() > 0)
						count++;
				}
				if(count <= 1) return;
				
				for(int i = currentGenericPreset; i < GenericPresets.values().length-1; i++) {
					GenericPresets pre1 = GenericPresets.values()[i];
					GenericPresets pre2 = GenericPresets.values()[i+1];
					
					pre1.setItems(pre2.getItems());
					pre2.setItems(new ArrayList<StoreItems>());
					
					fillGenericPresetList();
				}
			}
		});
		
		// Add preset list
		pnlGenericPresets.add(scrollGenericPresets, lstObjectsC);

		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		// Add labels
		pnlGenericPresets.add(lblGenericItems, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlGenericPresets.add(lblGenericItem, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlGenericPresets.add(lblGenericPrice, lstObjectsC);
		
		// Add text fields
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 12;
		pnlGenericPresets.add(txtGenericPrice, lstObjectsC);
		
		// Add item list
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridheight = 7;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		
		pnlGenericPresets.add(scrollGenericItems, lstObjectsC);
		
		// Add combo box
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridy = 9;
		pnlGenericPresets.add(cmbGenericItem, lstObjectsC);
		
		// Add buttons
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 10;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		
		pnlGenericPresets.add(btnAddGeneric, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGenericPresets.add(btnDeleteGeneric, lstObjectsC);
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 13;
		pnlGenericPresets.add(btnAddGenericPreset, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGenericPresets.add(btnDeleteGenericPreset, lstObjectsC);

		fillGenericInfo();
		
		return pnlGenericPresets;
	}

	private static void fillGenericPresetItems() {
		String[] itemNames = new String[GenericPresets.values()[currentGenericPreset].getItems().size()];
		for(int i = 0; i < itemNames.length; i++) {
			itemNames[i] = GenericPresets.values()[currentGenericPreset].getItems().get(i).getItemName();
		}
		lstGenericItems.setListData(itemNames);
	}
	
	private static void fillGenericPresetList() {
		ArrayList<String> presetList = new ArrayList<String>();
		int count = 0;
		for(GenericPresets preset : GenericPresets.values()) {
			if(preset.getItems().size() > 0)
				presetList.add("Preset " + count++);
		}
		lstGenericPresets.setListData(presetList.toArray());
	}
	
	private static void fillGenericInfo() {
		try {
			fillGenericPresetList();
			fillGenericPresetItems();
			
			cmbGenericItem.removeAllItems();
			for(StoreItems item : StoreItems.values()) {
				if(!item.getItemName().equals(""))
					cmbGenericItem.addItem(item.getItemName());
			}
			fillGenericPrice();
		} catch(Exception ex) { }
	}
	
	private static void fillGenericPrice() {
		txtGenericPrice.setText(String.valueOf(StoreItems.values()[currentGeneric].getPrice()));
	}
}
