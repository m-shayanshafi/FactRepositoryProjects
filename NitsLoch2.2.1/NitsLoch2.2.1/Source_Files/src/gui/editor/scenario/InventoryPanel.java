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

import src.enums.InventoryLimits;
import src.enums.StartingInventory;

public class InventoryPanel {

	private static JTextField txtLimitMoney;
	private static JTextField txtLimitBandaids;
	private static JTextField txtLimitGrenades;
	private static JTextField txtLimitDynamite;
	private static JTextField txtLimitBullets;
	private static JTextField txtLimitRockets;
	private static JTextField txtLimitFlamePacks;
	private static JTextField txtLimitLadderUp;
	private static JTextField txtLimitLadderDown;
	private static JTextField txtLimitMapViewers;
	private static JTextField txtLimitExports;

	private static JTextField txtStartBandaids;
	private static JTextField txtStartGrenades;
	private static JTextField txtStartDynamite;
	private static JTextField txtStartBullets;
	private static JTextField txtStartRockets;
	private static JTextField txtStartFlamePacks;
	private static JTextField txtStartLadderUp;
	private static JTextField txtStartLadderDown;
	private static JTextField txtStartMapViewers;
	private static JTextField txtStartExports;

	public static JPanel getInventoryPanel() {
		JPanel pnlInventory = new JPanel();
		pnlInventory.setLayout(new GridBagLayout());

		JLabel lblLimits = new JLabel();
		lblLimits.setText("Inventory Limits:");
		JLabel lblStart = new JLabel();
		lblStart.setText("Starting Inventory:");
		JLabel lblMoney = new JLabel();
		lblMoney.setText("Money");
		JLabel lblBandaids = new JLabel();
		lblBandaids.setText("Bandaids");
		JLabel lblGrenades = new JLabel();
		lblGrenades.setText("Grenades");
		JLabel lblDynamite = new JLabel();
		lblDynamite.setText("Dynamite");
		JLabel lblBullets = new JLabel();
		lblBullets.setText("Bullets");
		JLabel lblRockets = new JLabel();
		lblRockets.setText("Rockets");
		JLabel lblFlamePacks = new JLabel();
		lblFlamePacks.setText("Flame Packs");
		JLabel lblLadderUp = new JLabel();
		lblLadderUp.setText("Ladder Up");
		JLabel lblLadderDown = new JLabel();
		lblLadderDown.setText("Ladder Down");
		JLabel lblMapViewers = new JLabel();
		lblMapViewers.setText("Map Viewers");
		JLabel lblExports = new JLabel();
		lblExports.setText("Exports");

		txtLimitMoney = new JTextField();
		txtLimitMoney.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitMoney.getText()) <= 0)
						InventoryLimits.MONEY.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.MONEY.setLimit(
								Integer.parseInt(txtLimitMoney.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitBandaids = new JTextField();
		txtLimitBandaids.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitBandaids.getText()) <= 0)
						InventoryLimits.BANDAIDS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.BANDAIDS.setLimit(
								Integer.parseInt(txtLimitBandaids.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitGrenades = new JTextField();
		txtLimitGrenades.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitGrenades.getText()) <= 0)
						InventoryLimits.GRENADES.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.GRENADES.setLimit(
								Integer.parseInt(txtLimitGrenades.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitDynamite = new JTextField();
		txtLimitDynamite.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitDynamite.getText()) <= 0)
						InventoryLimits.DYNAMITE.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.DYNAMITE.setLimit(
								Integer.parseInt(txtLimitDynamite.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitBullets = new JTextField();
		txtLimitBullets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitBullets.getText()) <= 0)
						InventoryLimits.BULLETS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.BULLETS.setLimit(
								Integer.parseInt(txtLimitBullets.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitRockets = new JTextField();
		txtLimitRockets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitRockets.getText()) <= 0)
						InventoryLimits.ROCKETS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.ROCKETS.setLimit(
								Integer.parseInt(txtLimitRockets.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitFlamePacks = new JTextField();
		txtLimitFlamePacks.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitFlamePacks.getText()) <= 0)
						InventoryLimits.FLAME_PACKS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.FLAME_PACKS.setLimit(
								Integer.parseInt(txtLimitFlamePacks.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitLadderUp = new JTextField();
		txtLimitLadderUp.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitLadderUp.getText()) <= 0)
						InventoryLimits.LADDER_UP.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.LADDER_UP.setLimit(
								Integer.parseInt(txtLimitLadderUp.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitLadderDown = new JTextField();
		txtLimitLadderDown.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitLadderDown.getText()) <= 0)
						InventoryLimits.LADDER_DOWN.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.LADDER_DOWN.setLimit(
								Integer.parseInt(txtLimitLadderDown.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitMapViewers = new JTextField();
		txtLimitMapViewers.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitMapViewers.getText()) <= 0)
						InventoryLimits.MAP_VIEWERS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.MAP_VIEWERS.setLimit(
								Integer.parseInt(txtLimitMapViewers.getText()));
				} catch(Exception ex) { }
			}
		});
		txtLimitExports = new JTextField();
		txtLimitExports.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					if(Integer.parseInt(txtLimitExports.getText()) <= 0)
						InventoryLimits.EXPORTS.setLimit(Integer.MAX_VALUE);
					else
						InventoryLimits.EXPORTS.setLimit(
								Integer.parseInt(txtLimitExports.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartBandaids = new JTextField();
		txtStartBandaids.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.BANDAIDS.setAmount(
							Integer.parseInt(txtStartBandaids.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartGrenades = new JTextField();
		txtStartGrenades.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.GRENADES.setAmount(
							Integer.parseInt(txtStartGrenades.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartDynamite = new JTextField();
		txtStartDynamite.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.DYNAMITE.setAmount(
							Integer.parseInt(txtStartDynamite.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartBullets = new JTextField();
		txtStartBullets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.BULLETS.setAmount(
							Integer.parseInt(txtStartBullets.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartRockets = new JTextField();
		txtStartRockets.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.ROCKETS.setAmount(
							Integer.parseInt(txtStartRockets.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartFlamePacks = new JTextField();
		txtStartFlamePacks.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.FLAME_PACKS.setAmount(
							Integer.parseInt(txtStartFlamePacks.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartLadderUp = new JTextField();
		txtStartLadderUp.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.LADDER_UP.setAmount(
							Integer.parseInt(txtStartLadderUp.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartLadderDown = new JTextField();
		txtStartLadderDown.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.LADDER_DOWN.setAmount(
							Integer.parseInt(txtStartLadderDown.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartMapViewers = new JTextField();
		txtStartMapViewers.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.MAP_VIEWERS.setAmount(
							Integer.parseInt(txtStartMapViewers.getText()));
				} catch(Exception ex) { }
			}
		});
		txtStartExports = new JTextField();
		txtStartExports.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					StartingInventory.EXPORTS.setAmount(
							Integer.parseInt(txtStartExports.getText()));
				} catch(Exception ex) { }
			}
		});

		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 0;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 0, 0, 0); 

		// Add labels
		lstObjectsC.insets = new Insets(5, 10, 5, 10); 
		lstObjectsC.gridx = 1;
		pnlInventory.add(lblLimits, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlInventory.add(lblStart, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 1;
		pnlInventory.add(lblMoney, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlInventory.add(lblBandaids, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlInventory.add(lblGrenades, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlInventory.add(lblDynamite, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlInventory.add(lblBullets, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlInventory.add(lblRockets, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlInventory.add(lblFlamePacks, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlInventory.add(lblLadderUp, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlInventory.add(lblLadderDown, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlInventory.add(lblMapViewers, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlInventory.add(lblExports, lstObjectsC);

		// Add text fields
		lstObjectsC.gridx = 1;
		lstObjectsC.gridy = 1;
		lstObjectsC.weightx = 0;
		lstObjectsC.insets = new Insets(5, 10, 5, 10); 
		pnlInventory.add(txtLimitMoney, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlInventory.add(txtLimitBandaids, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlInventory.add(txtLimitGrenades, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlInventory.add(txtLimitDynamite, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlInventory.add(txtLimitBullets, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlInventory.add(txtLimitRockets, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlInventory.add(txtLimitFlamePacks, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlInventory.add(txtLimitLadderUp, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlInventory.add(txtLimitLadderDown, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlInventory.add(txtLimitMapViewers, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlInventory.add(txtLimitExports, lstObjectsC);
		lstObjectsC.gridx = 2;
		lstObjectsC.gridy = 2;
		pnlInventory.add(txtStartBandaids, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlInventory.add(txtStartGrenades, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlInventory.add(txtStartDynamite, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlInventory.add(txtStartBullets, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlInventory.add(txtStartRockets, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlInventory.add(txtStartFlamePacks, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlInventory.add(txtStartLadderUp, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlInventory.add(txtStartLadderDown, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlInventory.add(txtStartMapViewers, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlInventory.add(txtStartExports, lstObjectsC);

		fillInfo();

		return pnlInventory;
	}

	private static void fillInfo() {
		if(!(InventoryLimits.MONEY.getLimit() == Integer.MAX_VALUE))
			txtLimitMoney.setText(String.valueOf(InventoryLimits.MONEY.getLimit()));
		else txtLimitMoney.setText("0");
		if(!(InventoryLimits.BANDAIDS.getLimit() == Integer.MAX_VALUE))
			txtLimitBandaids.setText(String.valueOf(InventoryLimits.BANDAIDS.getLimit()));
		else txtLimitBandaids.setText("0");
		if(!(InventoryLimits.GRENADES.getLimit() == Integer.MAX_VALUE))
			txtLimitGrenades.setText(String.valueOf(InventoryLimits.GRENADES.getLimit()));
		else txtLimitGrenades.setText("0");
		if(!(InventoryLimits.DYNAMITE.getLimit() == Integer.MAX_VALUE))
			txtLimitDynamite.setText(String.valueOf(InventoryLimits.DYNAMITE.getLimit()));
		else txtLimitDynamite.setText("0");
		if(!(InventoryLimits.BULLETS.getLimit() == Integer.MAX_VALUE))
			txtLimitBullets.setText(String.valueOf(InventoryLimits.BULLETS.getLimit()));
		else txtLimitBullets.setText("0");
		if(!(InventoryLimits.ROCKETS.getLimit() == Integer.MAX_VALUE))
			txtLimitRockets.setText(String.valueOf(InventoryLimits.ROCKETS.getLimit()));
		else txtLimitRockets.setText("0");
		if(!(InventoryLimits.FLAME_PACKS.getLimit() == Integer.MAX_VALUE))
			txtLimitFlamePacks.setText(String.valueOf(InventoryLimits.FLAME_PACKS.getLimit()));
		else txtLimitFlamePacks.setText("0");
		if(!(InventoryLimits.LADDER_UP.getLimit() == Integer.MAX_VALUE))
			txtLimitLadderUp.setText(String.valueOf(InventoryLimits.LADDER_UP.getLimit()));
		else txtLimitLadderUp.setText("0");
		if(!(InventoryLimits.LADDER_DOWN.getLimit() == Integer.MAX_VALUE))
			txtLimitLadderDown.setText(String.valueOf(InventoryLimits.LADDER_DOWN.getLimit()));
		else txtLimitLadderDown.setText("0");
		if(!(InventoryLimits.MAP_VIEWERS.getLimit() == Integer.MAX_VALUE))
			txtLimitMapViewers.setText(String.valueOf(InventoryLimits.MAP_VIEWERS.getLimit()));
		else txtLimitMapViewers.setText("0");
		if(!(InventoryLimits.EXPORTS.getLimit() == Integer.MAX_VALUE))
			txtLimitExports.setText(String.valueOf(InventoryLimits.EXPORTS.getLimit()));
		else txtLimitExports.setText("0");

		txtStartBandaids.setText(String.valueOf(StartingInventory.BANDAIDS.getAmount()));
		txtStartGrenades.setText(String.valueOf(StartingInventory.GRENADES.getAmount()));
		txtStartDynamite.setText(String.valueOf(StartingInventory.DYNAMITE.getAmount()));
		txtStartBullets.setText(String.valueOf(StartingInventory.BULLETS.getAmount()));
		txtStartRockets.setText(String.valueOf(StartingInventory.ROCKETS.getAmount()));
		txtStartFlamePacks.setText(String.valueOf(StartingInventory.FLAME_PACKS.getAmount()));
		txtStartLadderUp.setText(String.valueOf(StartingInventory.LADDER_UP.getAmount()));
		txtStartLadderDown.setText(String.valueOf(StartingInventory.LADDER_DOWN.getAmount()));
		txtStartMapViewers.setText(String.valueOf(StartingInventory.MAP_VIEWERS.getAmount()));
		txtStartExports.setText(String.valueOf(StartingInventory.EXPORTS.getAmount()));
	}

}
