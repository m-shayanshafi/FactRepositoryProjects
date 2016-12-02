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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import src.enums.Drinks;

public class BarDrinksPanel {
	
	private static JTextField txtTonicWater;
	private static JTextField txtSoda;
	private static JTextField txtGin;
	private static JTextField txtRum;
	private static JTextField txtScotch;
	private static JTextField txtRedEye;

	public static JPanel getBarDrinksPanel() {
		JPanel pnlDrinks = new JPanel();
		pnlDrinks.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblTonicWater = new JLabel();
		lblTonicWater.setText("Tonic Water:");
		JLabel lblSoda = new JLabel();
		lblSoda.setText("Soda:");
		JLabel lblGin = new JLabel();
		lblGin.setText("Gin:");
		JLabel lblRum = new JLabel();
		lblRum.setText("Rum:");
		JLabel lblScotch = new JLabel();
		lblScotch.setText("Scotch:");
		JLabel lblRedEye = new JLabel();
		lblRedEye.setText("Red Eye:");

		txtTonicWater = new JTextField();
		txtTonicWater.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.TONIC_WATER.setCost(Integer.parseInt(txtTonicWater.getText()));
				} catch (Exception ex) { }
			}
		});
		txtSoda = new JTextField();
		txtSoda.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.SODA.setCost(Integer.parseInt(txtSoda.getText()));
				} catch (Exception ex) { }
			}
		});
		txtGin = new JTextField();
		txtGin.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.GIN.setCost(Integer.parseInt(txtGin.getText()));
				} catch (Exception ex) { }
			}
		});
		txtRum = new JTextField();
		txtRum.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.RUM.setCost(Integer.parseInt(txtRum.getText()));
				} catch (Exception ex) { }
			}
		});
		txtScotch = new JTextField();
		txtScotch.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.SCOTCH.setCost(Integer.parseInt(txtScotch.getText()));
				} catch (Exception ex) { }
			}
		});
		txtRedEye = new JTextField();
		txtRedEye.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Drinks.REDEYE.setCost(Integer.parseInt(txtRedEye.getText()));
				} catch (Exception ex) { }
			}
		});
		
		// Add labels
		pnlDrinks.add(lblTonicWater, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlDrinks.add(lblSoda, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlDrinks.add(lblGin, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlDrinks.add(lblRum, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlDrinks.add(lblScotch, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlDrinks.add(lblRedEye, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlDrinks.add(txtTonicWater, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlDrinks.add(txtSoda, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlDrinks.add(txtGin, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlDrinks.add(txtRum, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlDrinks.add(txtScotch, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlDrinks.add(txtRedEye, lstObjectsC);
		
		fillInfo();
		
		return pnlDrinks;
	}
	
	private static void fillInfo() {
		txtTonicWater.setText(String.valueOf(Drinks.TONIC_WATER.getCost()));
		txtSoda.setText(String.valueOf(Drinks.SODA.getCost()));
		txtGin.setText(String.valueOf(Drinks.GIN.getCost()));
		txtRum.setText(String.valueOf(Drinks.RUM.getCost()));
		txtScotch.setText(String.valueOf(Drinks.SCOTCH.getCost()));
		txtRedEye.setText(String.valueOf(Drinks.REDEYE.getCost()));
	}
}
