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
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.enums.Bars;
import src.enums.Drinks;

public class BarMessagesPanel {

	private static JList lstBars;
	private static JTextArea txtTonicWater;
	private static JTextArea txtSoda;
	private static JTextArea txtGin;
	private static JTextArea txtRum;
	private static JTextArea txtScotch;
	private static JTextArea txtRedEye;
	private static JButton btnNew;
	private static JButton btnDelete;

	private static int currentBar = -1;

	public static JPanel getBarMessagesPanel() {
		JPanel pnlBarMessages = new JPanel();
		pnlBarMessages.setLayout(new GridBagLayout());

		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 20;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 0;
		lstObjectsC.weighty = 1.0;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 0, 0, 0);

		lstBars = new JList();
		lstBars.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(lstBars.getSelectedIndex() != -1)
					currentBar = lstBars.getSelectedIndex();
				else return;
				fillInfo();
			}
		});
		JScrollPane scrollBars = new JScrollPane(lstBars);

		setBarNameList();

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

		txtTonicWater = new JTextArea();
		txtTonicWater.setWrapStyleWord(true);
		txtTonicWater.setLineWrap(true);
		txtTonicWater.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setTonicWater(txtTonicWater.getText());
				} catch (Exception ex) { }
			}
		});
		txtSoda = new JTextArea();
		txtSoda.setWrapStyleWord(true);
		txtSoda.setLineWrap(true);
		txtSoda.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setSoda(txtSoda.getText());
				} catch (Exception ex) { }
			}
		});
		txtGin = new JTextArea();
		txtGin.setWrapStyleWord(true);
		txtGin.setLineWrap(true);
		txtGin.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setGin(txtGin.getText());
				} catch (Exception ex) { }
			}
		});
		txtRum = new JTextArea();
		txtRum.setWrapStyleWord(true);
		txtRum.setLineWrap(true);
		txtRum.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setRum(txtRum.getText());
				} catch (Exception ex) { }
			}
		});
		txtScotch = new JTextArea();
		txtScotch.setWrapStyleWord(true);
		txtScotch.setLineWrap(true);
		txtScotch.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setScotch(txtScotch.getText());
				} catch (Exception ex) { }
			}
		});
		txtRedEye = new JTextArea();
		txtRedEye.setWrapStyleWord(true);
		txtRedEye.setLineWrap(true);
		txtRedEye.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Bars.values()[currentBar].setRedeye(txtRedEye.getText());
				} catch (Exception ex) { }
			}
		});

		btnNew = new JButton();
		btnNew.setText("New");
		btnNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				addBar();
			}
		});

		btnDelete = new JButton();
		btnDelete.setText("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				deleteBar();
			}
		});

		pnlBarMessages.add(scrollBars, lstObjectsC);

		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 0.5;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 0, 10, 0);
		pnlBarMessages.add(lblTonicWater, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlBarMessages.add(lblSoda, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlBarMessages.add(lblGin, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlBarMessages.add(lblRum, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlBarMessages.add(lblScotch, lstObjectsC);
		lstObjectsC.gridy = 15;
		pnlBarMessages.add(lblRedEye, lstObjectsC);

		// Add text fields
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 0;
		lstObjectsC.gridheight = 3;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 1.0;
		JScrollPane scrollTonic = new JScrollPane(txtTonicWater);
		pnlBarMessages.add(scrollTonic, lstObjectsC);
		lstObjectsC.gridy = 3;
		JScrollPane scrollSoda = new JScrollPane(txtSoda);
		pnlBarMessages.add(scrollSoda, lstObjectsC);
		lstObjectsC.gridy = 6;
		JScrollPane scrollGin = new JScrollPane(txtGin);
		pnlBarMessages.add(scrollGin, lstObjectsC);
		lstObjectsC.gridy = 9;
		JScrollPane scrollRum = new JScrollPane(txtRum);
		pnlBarMessages.add(scrollRum, lstObjectsC);
		lstObjectsC.gridy = 12;
		JScrollPane scrollScotch = new JScrollPane(txtScotch);
		pnlBarMessages.add(scrollScotch, lstObjectsC);
		lstObjectsC.gridy = 15;
		JScrollPane scrollRedEye = new JScrollPane(txtRedEye);
		pnlBarMessages.add(scrollRedEye, lstObjectsC);

		// Add buttons
		//lstObjectsC.insets = new Insets(10, 150, 10, 150);
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 2;
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 18;
		lstObjectsC.insets = new Insets(0, 100, 10, 100);
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		pnlBarMessages.add(btnNew, lstObjectsC);

		lstObjectsC.gridy = 19;
		pnlBarMessages.add(btnDelete, lstObjectsC);

		return pnlBarMessages;
	}

	private static void setBarNameList() {
		ArrayList<String> bars = new ArrayList<String>();
		int count = 0;
		for(Bars b : Bars.values()) {
			if(b.getUsed()) {
				bars.add("Bar " + count++);
			}
			else break;
		}
		lstBars.setListData(bars.toArray());
	}

	private static void fillInfo() {
		txtTonicWater.setText(Bars.getMessage(Drinks.TONIC_WATER, currentBar));
		txtSoda.setText(Bars.getMessage(Drinks.SODA, currentBar));
		txtGin.setText(Bars.getMessage(Drinks.GIN, currentBar));
		txtRum.setText(Bars.getMessage(Drinks.RUM, currentBar));
		txtScotch.setText(Bars.getMessage(Drinks.SCOTCH, currentBar));
		txtRedEye.setText(Bars.getMessage(Drinks.REDEYE, currentBar));

		setBarNameList();
	}

	private static void addBar() {
		int newIndex = 0;
		for(int i = 0; i < Bars.values().length; i++) {
			if(Bars.values()[i].getUsed())
				newIndex = i+1;
			else break;
		}
		if(newIndex > Bars.values().length-1) return;

		Bars bar = Bars.values()[newIndex];
		bar.setStats("Hi!", "Hi!", "Hi!", "Hi!", "Hi!", "Why don't we sell Woodford? :C");

		setBarNameList();
	}

	public static void deleteBar() {
		if(currentBar == -1) return;
		int numBars = 0;
		for(int i = 0; i < Bars.values().length; i++) {
			if(Bars.values()[i].getUsed())
				numBars++;
		}
		if(numBars <= 1) return;

		for(int i = currentBar; i < Bars.values().length-1; i++) {
			Bars bar1 = Bars.values()[i];
			Bars bar2 = Bars.values()[i+1];

			if(!bar2.getUsed())
				bar1.remove();
			else {

				String tonic, soda, gin, rum, scotch, redeye;

				tonic = Bars.getMessage(Drinks.TONIC_WATER, bar2.getType());
				soda = Bars.getMessage(Drinks.SODA, bar2.getType());
				gin = Bars.getMessage(Drinks.GIN, bar2.getType());
				rum = Bars.getMessage(Drinks.RUM, bar2.getType());
				scotch = Bars.getMessage(Drinks.SCOTCH, bar2.getType());
				redeye = Bars.getMessage(Drinks.REDEYE, bar2.getType());

				bar1.setStats(tonic, soda, gin, rum, scotch, redeye);
			}
			bar2.remove();
		}

		currentBar = -1;

		setBarNameList();
	}
}
