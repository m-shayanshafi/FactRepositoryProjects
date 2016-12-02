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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import src.enums.ExplosionType;
import src.scenario.MiscScenarioData;

public class GeneralPanel {

	private static JTextField txtScenarioName;
	private static JTextField txtMapFile;
	private static JTextField txtRunHitPoint;
	private static JTextField txtRunChance;
	private static JTextField txtCitySpawn;
	private static JTextField txtDungeonSpawn;
	private static JTextField txtItemDrop;
	private static JTextField txtHealthScale;
	private static JTextField txtAbilityScale;
	private static JTextField txtDamageScale;
	private static JTextField txtExplSmall;
	private static JTextField txtExplMedium;
	private static JTextField txtExplLarge;

	private static JTextArea txtEndMessage;

	public static JPanel getGeneralPanel() {
		JPanel pnlGeneral = new JPanel();
		pnlGeneral.setLayout(new GridBagLayout());

		JLabel lblScenarioName = new JLabel();
		lblScenarioName.setText("Scenario Name:");
		JLabel lblMapFile = new JLabel();
		lblMapFile.setText("Map File:");
		JLabel lblRunHitPoint = new JLabel();
		lblRunHitPoint.setText("Hit-point run threshold:");
		JLabel lblRunChance = new JLabel();
		lblRunChance.setText("Run Chance:");
		JLabel lblCitySpawnChance = new JLabel();
		lblCitySpawnChance.setText("City Spawn Chance:");
		JLabel lblDungeonSpawnChance = new JLabel();
		lblDungeonSpawnChance.setText("Dungeon Spawn Chance:");
		JLabel lblItemDropChance = new JLabel();
		lblItemDropChance.setText("Item Drop Chance:");
		JLabel lblDifficultyScale = new JLabel();
		lblDifficultyScale.setText("Difficulty Scale");
		JLabel lblHealthScale = new JLabel();
		lblHealthScale.setText("Health:");
		JLabel lblAbilityScale = new JLabel();
		lblAbilityScale.setText("Ability:");
		JLabel lblDamageScale = new JLabel();
		lblDamageScale.setText("Damage:");
		JLabel lblExplosionDamage = new JLabel();
		lblExplosionDamage.setText("Explosion Damage");
		JLabel lblExplSmall = new JLabel();
		lblExplSmall.setText("Small:");
		JLabel lblExplMedium = new JLabel();
		lblExplMedium.setText("Medium:");
		JLabel lblExplLarge = new JLabel();
		lblExplLarge.setText("Large:");
		JLabel lblEndMessage = new JLabel();
		lblEndMessage.setText("End Message:");

		txtScenarioName = new JTextField();
		txtScenarioName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				MiscScenarioData.NAME = txtScenarioName.getText();
			}
		});
		txtMapFile = new JTextField();
		txtMapFile.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				MiscScenarioData.MAP_PATH = txtMapFile.getText();
			}
		});
		txtRunHitPoint = new JTextField();
		txtRunHitPoint.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.RUN_HP_AMOUNT =
						Integer.parseInt(txtRunHitPoint.getText());
				} catch (Exception ex) { }
			}
		});
		txtRunChance = new JTextField();
		txtRunChance.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.RUN_CHANCE =
						Double.parseDouble(txtRunChance.getText());
				} catch (Exception ex) { }
			}
		});
		txtCitySpawn = new JTextField();
		txtCitySpawn.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.SPAWN_CHANCE =
						Double.parseDouble(txtCitySpawn.getText());
				} catch (Exception ex) { }
			}
		});
		txtDungeonSpawn = new JTextField();
		txtDungeonSpawn.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.SPAWN_CHANCE_DUN =
						Double.parseDouble(txtDungeonSpawn.getText());
				} catch (Exception ex) { }
			}
		});
		txtItemDrop = new JTextField();
		txtItemDrop.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.SPAWN_CHANCE_ITEM =
						Double.parseDouble(txtItemDrop.getText());
				} catch (Exception ex) { }
			}
		});
		txtHealthScale = new JTextField();
		txtHealthScale.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.HEALTH_SCALE =
						Double.parseDouble(txtHealthScale.getText());
				} catch (Exception ex) { }
			}
		});
		txtAbilityScale = new JTextField();
		txtAbilityScale.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.ABILITY_SCALE =
						Double.parseDouble(txtAbilityScale.getText());
				} catch (Exception ex) { }
			}
		});
		txtDamageScale = new JTextField();
		txtDamageScale.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.DAMAGE_SCALE =
						Double.parseDouble(txtDamageScale.getText());
				} catch (Exception ex) { }
			}
		});
		txtExplSmall = new JTextField();
		txtExplSmall.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					ExplosionType.MINOR.setDamage(
						Integer.parseInt(txtExplSmall.getText()));
				} catch (Exception ex) { }
			}
		});
		txtExplMedium = new JTextField();
		txtExplMedium.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					ExplosionType.MEDIUM.setDamage(
						Integer.parseInt(txtExplMedium.getText()));
				} catch (Exception ex) { }
			}
		});
		txtExplLarge = new JTextField();
		txtExplLarge.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					ExplosionType.MAJOR.setDamage(
						Integer.parseInt(txtExplLarge.getText()));
				} catch (Exception ex) { }
			}
		});
		txtEndMessage = new JTextArea();
		txtEndMessage.setWrapStyleWord(true);
		txtEndMessage.setLineWrap(true);
		txtEndMessage.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					MiscScenarioData.ENDING_MESSAGE =
						txtEndMessage.getText();
				} catch (Exception ex) { }
			}
		});
		JScrollPane scrollMessage = new JScrollPane(txtEndMessage);

		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 3;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 21;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 1;
		lstObjectsC.fill = GridBagConstraints.BOTH;
		lstObjectsC.insets = new Insets(0, 40, 40, 40); 

		pnlGeneral.add(scrollMessage, lstObjectsC);

		// Add labels
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridwidth = 3;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.fill = GridBagConstraints.NONE;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 40, 0, 40); 
		pnlGeneral.add(lblScenarioName, lstObjectsC);
		lstObjectsC.gridy = 2;
		pnlGeneral.add(lblMapFile, lstObjectsC);
		lstObjectsC.gridy = 4;
		pnlGeneral.add(lblRunHitPoint, lstObjectsC);
		lstObjectsC.gridy = 6;
		pnlGeneral.add(lblRunChance, lstObjectsC);
		lstObjectsC.gridy = 8;
		pnlGeneral.add(lblCitySpawnChance, lstObjectsC);
		lstObjectsC.gridy = 10;
		pnlGeneral.add(lblDungeonSpawnChance, lstObjectsC);
		lstObjectsC.gridy = 12;
		pnlGeneral.add(lblItemDropChance, lstObjectsC);
		lstObjectsC.gridy = 14;
		lstObjectsC.insets = new Insets(10, 40, 0, 40); 
		pnlGeneral.add(lblDifficultyScale, lstObjectsC);
		lstObjectsC.gridy = 15;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.insets = new Insets(5, 40, 0, 40);
		pnlGeneral.add(lblHealthScale, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlGeneral.add(lblAbilityScale, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGeneral.add(lblDamageScale, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 17;
		lstObjectsC.gridwidth = 3;
		lstObjectsC.insets = new Insets(10, 40, 0, 40); 
		pnlGeneral.add(lblExplosionDamage, lstObjectsC);
		lstObjectsC.gridy = 18;
		lstObjectsC.gridwidth = 1;
		lstObjectsC.insets = new Insets(5, 40, 0, 40);
		pnlGeneral.add(lblExplSmall, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlGeneral.add(lblExplMedium, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGeneral.add(lblExplLarge, lstObjectsC);
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 20;
		lstObjectsC.gridwidth = 3;
		pnlGeneral.add(lblEndMessage, lstObjectsC);
		lstObjectsC.insets = new Insets(0, 40, 0, 40);

		// Add text fields
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 1;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.weightx = 1.0;
		lstObjectsC.weighty = 0.0;
		lstObjectsC.insets = new Insets(0, 40, 5, 40);
		pnlGeneral.add(txtScenarioName, lstObjectsC);
		lstObjectsC.gridy = 3;
		pnlGeneral.add(txtMapFile, lstObjectsC);
		lstObjectsC.gridy = 5;
		pnlGeneral.add(txtRunHitPoint, lstObjectsC);
		lstObjectsC.gridy = 7;
		pnlGeneral.add(txtRunChance, lstObjectsC);
		lstObjectsC.gridy = 9;
		pnlGeneral.add(txtCitySpawn, lstObjectsC);
		lstObjectsC.gridy = 11;
		pnlGeneral.add(txtDungeonSpawn, lstObjectsC);
		lstObjectsC.gridy = 13;
		pnlGeneral.add(txtItemDrop, lstObjectsC);
		lstObjectsC.gridy = 16;
		lstObjectsC.gridwidth = 1;
		pnlGeneral.add(txtHealthScale, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlGeneral.add(txtAbilityScale, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGeneral.add(txtDamageScale, lstObjectsC);
		lstObjectsC.gridy = 19;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridwidth = 1;
		pnlGeneral.add(txtExplSmall, lstObjectsC);
		lstObjectsC.gridx = 1;
		pnlGeneral.add(txtExplMedium, lstObjectsC);
		lstObjectsC.gridx = 2;
		pnlGeneral.add(txtExplLarge, lstObjectsC);

		fillInfo();

		return pnlGeneral;
	}

	private static void fillInfo() {
		txtScenarioName.setText(MiscScenarioData.NAME);
		txtMapFile.setText(MiscScenarioData.MAP_PATH);
		txtRunHitPoint.setText(String.valueOf(MiscScenarioData.RUN_HP_AMOUNT));
		txtRunChance.setText(String.valueOf(MiscScenarioData.RUN_CHANCE));
		txtCitySpawn.setText(String.valueOf(MiscScenarioData.SPAWN_CHANCE));
		txtDungeonSpawn.setText(String.valueOf(MiscScenarioData.SPAWN_CHANCE_DUN));
		txtItemDrop.setText(String.valueOf(MiscScenarioData.SPAWN_CHANCE_ITEM));
		txtHealthScale.setText(String.valueOf(MiscScenarioData.HEALTH_SCALE));
		txtAbilityScale.setText(String.valueOf(MiscScenarioData.ABILITY_SCALE));
		txtDamageScale.setText(String.valueOf(MiscScenarioData.DAMAGE_SCALE));
		txtExplSmall.setText(String.valueOf(ExplosionType.MINOR.getBaseDamage()));
		txtExplMedium.setText(String.valueOf(ExplosionType.MEDIUM.getBaseDamage()));
		txtExplLarge.setText(String.valueOf(ExplosionType.MAJOR.getBaseDamage()));
		txtEndMessage.setText(MiscScenarioData.ENDING_MESSAGE);
	}

}
