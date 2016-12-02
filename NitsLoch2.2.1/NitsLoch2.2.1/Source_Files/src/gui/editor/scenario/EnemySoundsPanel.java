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

import src.enums.Sounds;

public class EnemySoundsPanel {

	private static JTextField txtMelee;
	private static JTextField txtMartArts;
	private static JTextField txtMarksman;
	private static JTextField txtFlame;
	private static JTextField txtOther;
	private static JTextField txtDeath;

	public static JPanel getEnemySoundsPanel() {
		JPanel pnlEnemySounds = new JPanel();
		pnlEnemySounds.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblMelee = new JLabel();
		lblMelee.setText("Melee Attacks:");
		JLabel lblMartArts = new JLabel();
		lblMartArts.setText("Martial Arts Attacks:");
		JLabel lblMarksman = new JLabel();
		lblMarksman.setText("Marksman Attacks:");
		JLabel lblFlame = new JLabel();
		lblFlame.setText("Flame Attacks:");
		JLabel lblOther = new JLabel();
		lblOther.setText("Other Attacks:");
		JLabel lblDeath = new JLabel();
		lblDeath.setText("Death:");

		txtMelee = new JTextField();
		txtMelee.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_MELEE_ATTACK.setSound(txtMelee.getText());
				} catch (Exception ex) { }
			}
		});
		txtMartArts = new JTextField();
		txtMartArts.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_MART_ARTS_ATTACK.setSound(txtMartArts.getText());
				} catch (Exception ex) { }
			}
		});
		txtMarksman = new JTextField();
		txtMarksman.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_MARKSMAN_ATTACK.setSound(txtMarksman.getText());
				} catch (Exception ex) { }
			}
		});
		txtFlame = new JTextField();
		txtFlame.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_FLAME_ATTACK.setSound(txtFlame.getText());
				} catch (Exception ex) { }
			}
		});
		txtOther = new JTextField();
		txtOther.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_OTHER_ATTACK.setSound(txtOther.getText());
				} catch (Exception ex) { }
			}
		});
		txtDeath = new JTextField();
		txtDeath.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.ENEMY_DIES.setSound(txtDeath.getText());
				} catch (Exception ex) { }
			}
		});
		
		// Add labels
		pnlEnemySounds.add(lblMelee, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlEnemySounds.add(lblMartArts, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlEnemySounds.add(lblMarksman, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlEnemySounds.add(lblFlame, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlEnemySounds.add(lblOther, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlEnemySounds.add(lblDeath, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlEnemySounds.add(txtMelee, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlEnemySounds.add(txtMartArts, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlEnemySounds.add(txtMarksman, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlEnemySounds.add(txtFlame, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlEnemySounds.add(txtOther, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlEnemySounds.add(txtDeath, lstObjectsC);
		
		fillInfo();
		
		return pnlEnemySounds;
	}
	
	private static void fillInfo() {
		txtMelee.setText(Sounds.ENEMY_MELEE_ATTACK.getPath());
		txtMartArts.setText(Sounds.ENEMY_MART_ARTS_ATTACK.getPath());
		txtMarksman.setText(Sounds.ENEMY_MARKSMAN_ATTACK.getPath());
		txtFlame.setText(Sounds.ENEMY_FLAME_ATTACK.getPath());
		txtOther.setText(Sounds.ENEMY_OTHER_ATTACK.getPath());
		txtDeath.setText(Sounds.ENEMY_DIES.getPath());
	}
}
