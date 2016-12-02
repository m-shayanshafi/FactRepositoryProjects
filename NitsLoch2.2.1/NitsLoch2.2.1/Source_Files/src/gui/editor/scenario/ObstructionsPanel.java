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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.ObstructionLandType;

public class ObstructionsPanel {

	private static JList lstObstructions;
	private static JTextField txtName;
	private static JTextField txtImage;
	private static JCheckBox chkDestroyable;
	private static JButton btnNew;
	private static JButton btnDelete;
	private static int currentObstruction;
	
	public static JPanel getObstructionsPanel() {
		JPanel pnlObstructions = new JPanel();
		pnlObstructions.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);
		
		lstObstructions = new JList();
		lstObstructions.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstObstructions.getSelectedIndex() != -1)
					currentObstruction = lstObstructions.getSelectedIndex();
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollObstructions = new JScrollPane(lstObstructions);
		
		setObstructionNameList();
		
		JLabel lblName = new JLabel();
		lblName.setText("Name:");
		JLabel lblImage = new JLabel();
		lblImage.setText("Image:");
		
		txtName = new JTextField();
		txtName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ObstructionLandType.values()[currentObstruction].setName(
						txtName.getText());
			}
		});
		txtImage = new JTextField();
		txtImage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				ObstructionLandType.values()[currentObstruction].setImage(
						txtImage.getText());
			}
		});
		
		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addObstruction();
			}
		});
		
		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteObstruction();
			}
		});
		
		pnlObstructions.add(scrollObstructions, lstObjectsC);
		
		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		pnlObstructions.add(lblName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlObstructions.add(lblImage, lstObjectsC);
		
		// Add text fields
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 80, 10, 80);
		pnlObstructions.add(txtName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlObstructions.add(txtImage, lstObjectsC);
		
		// Add checkboxes
		chkDestroyable = new JCheckBox();
		chkDestroyable.setText("Destroyable");
		chkDestroyable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObstructionLandType.values()[currentObstruction].setDestroyable(
						chkDestroyable.isSelected());
			}
		});
		
		lstObjectsC.insets = new Insets(0, 80, 0, 0);
		//lstObjectsC.fill = GridBagConstraints.EAST;
		lstObjectsC.gridy = 4;
		pnlObstructions.add(chkDestroyable, lstObjectsC);
		
		// Add buttons
		lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridy = 5;
		pnlObstructions.add(btnNew, lstObjectsC);
		
		lstObjectsC.gridy = 6;
		pnlObstructions.add(btnDelete, lstObjectsC);
		
		return pnlObstructions;
	}
	
	public static void fillInfo() {
		txtName.setText(ObstructionLandType.values()[currentObstruction].getName());
		txtImage.setText(ObstructionLandType.values()[currentObstruction].getImage());
		chkDestroyable.setSelected(ObstructionLandType.values()[currentObstruction].getIsPossiblyDestroyed());
		
		setObstructionNameList();
	}
	
	private static void setObstructionNameList() {
		ArrayList<String> obs = new ArrayList<String>();
		for(ObstructionLandType o : ObstructionLandType.values()) {
			if(o.getUsed()) {
				obs.add(o.getName());
			}
		}
		lstObstructions.setListData(obs.toArray());
	}
	
	private static void addObstruction() {
		int newIndex = 0;
		for(int i = 0; i < ObstructionLandType.values().length; i++) {
			if(ObstructionLandType.values()[i].getUsed())
				newIndex = i+1;
			else break;
		}
		if(newIndex > ObstructionLandType.values().length-1) return;
		
		ObstructionLandType obs = ObstructionLandType.values()[newIndex];
		obs.setStats("newimage.png", true, "New Obstruction");
		
		setObstructionNameList();
	}
	
	public static void deleteObstruction() {
		if(currentObstruction == -1) return;
		int numObs = 0;
		for(int i = 0; i < ObstructionLandType.values().length; i++) {
			if(ObstructionLandType.values()[i].getUsed())
				numObs++;
		}
		if(numObs <= 2) return;
		
		for(int i = currentObstruction; i < ObstructionLandType.values().length-1; i++) {
			ObstructionLandType obs1 = ObstructionLandType.values()[i];
			ObstructionLandType obs2 = ObstructionLandType.values()[i+1];
			
			if(!obs2.getUsed())
				obs1.remove();
			else {
				String name, image;
				boolean destroyable;

				name = obs2.getName();
				image = obs2.getImage();
				destroyable = obs2.getIsPossiblyDestroyed();

				obs1.setStats(image, destroyable, name);
			}
			obs2.remove();
		}

		currentObstruction = -1;
		
		setObstructionNameList();
	}
}
