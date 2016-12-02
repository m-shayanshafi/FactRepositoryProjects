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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.StreetType;

public class StreetsPanel {

	private static JList lstStreets;
	private static JTextField txtName;
	private static JTextField txtImage;
	private static JButton btnNew;
	private static JButton btnDelete;
	private static int currentStreet;
	
	public static JPanel getStreetsPanel() {
		JPanel pnlStreets = new JPanel();
		pnlStreets.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstStreets = new JList();
		lstStreets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstStreets.getSelectedIndex() != -1)
					currentStreet = lstStreets.getSelectedIndex();
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollStreets = new JScrollPane(lstStreets);
		
		setStreetsNameList();
		
		JLabel lblName = new JLabel();
		lblName.setText("Name:");
		JLabel lblImage = new JLabel();
		lblImage.setText("Image:");
		
		txtName = new JTextField();
		txtName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				StreetType.values()[currentStreet].setName(
						txtName.getText());
			}
		});
		txtImage = new JTextField();
		txtImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				StreetType.values()[currentStreet].setImage(
						txtImage.getText());
			}
		});
		
		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addStreet();
			}
		});
		
		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteStreet();
			}
		});
		
		pnlStreets.add(scrollStreets, lstObjectsC);
		
		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		pnlStreets.add(lblName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlStreets.add(lblImage, lstObjectsC);
		
		// Add text fields
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 80, 10, 80);
		pnlStreets.add(txtName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlStreets.add(txtImage, lstObjectsC);
		
		// Add buttons
		lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridy = 4;
		pnlStreets.add(btnNew, lstObjectsC);
		
		lstObjectsC.gridy = 5;
		pnlStreets.add(btnDelete, lstObjectsC);
		
		return pnlStreets;
	}
	
	public static void fillInfo() {
		txtName.setText(StreetType.values()[currentStreet].getName());
		txtImage.setText(StreetType.values()[currentStreet].getImage());
		
		setStreetsNameList();
	}
	
	private static void setStreetsNameList() {
		ArrayList<String> streets = new ArrayList<String>();
		for(StreetType s : StreetType.values()) {
			if(s.getUsed() && !s.getIsTrigger()) {
				streets.add(s.getName());
			}
		}
		lstStreets.setListData(streets.toArray());
	}
	
	private static void addStreet() {
		int newIndex = 0;
		for(int i = 0; i < 200; i++) {
			if(StreetType.values()[i].getUsed())
				newIndex = i+1;
			else break;
		}
		if(newIndex > 199) return;
		
		StreetType street = StreetType.values()[newIndex];
		street.setStats("New Street", "newimage.png", 0);
		
		setStreetsNameList();
	}
	
	public static void deleteStreet() {
		if(currentStreet == -1) return;
		int numStreets = 0;
		for(int i = 0; i < StreetType.values().length; i++) {
			if(StreetType.values()[i].getUsed())
				numStreets++;
		}
		if(numStreets <= 1) return;
		
		for(int i = currentStreet; i < 199; i++) {
			StreetType street1 = StreetType.values()[i];
			StreetType street2 = StreetType.values()[i+1];
			
			if(!street2.getUsed())
				street1.remove();
			else {
				String name, image;

				name = street2.getName();
				image = street2.getImage();

				street1.setStats(name, image, 0);
			}
			street2.remove();
		}

		currentStreet = -1;
		
		setStreetsNameList();
	}
}
