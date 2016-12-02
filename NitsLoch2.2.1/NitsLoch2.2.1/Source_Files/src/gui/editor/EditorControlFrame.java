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

package src.gui.editor;

import java.awt.*;

import javax.swing.*;

import src.file.map.MapFile;
import src.game.Enemy;
import src.game.GameWorld;
import src.game.Item;
import src.game.NPC;
import src.game.TheEditor;
import src.land.Exit;
import src.land.Land;
import src.land.Obstruction;
import src.land.Shop;
import src.land.Street;

import src.enums.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.FileWriter;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This frame keeps track of what action will take place when
 * the user clicks on the Editor Window.  It also has the menu
 * options dealing with the level and switching between levels.
 * @author Darren Watts
 * date 1/28/09
 */
public class EditorControlFrame extends JFrame {

	private enum Bounds { INSIDE, BORDER, OUTSIDE; };

	private final String PREFS_FILE = "EditorPrefs";

	private static final long serialVersionUID = src.Constants.serialVersionUID;

	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu mnuFile = new JMenu();
	JMenuItem mnuFileOpen = new JMenuItem();
	JMenuItem mnuFileSaveAs = new JMenuItem();
	JMenuItem mnuFileSave = new JMenuItem();
	JMenuItem mnuFileExit = new JMenuItem();
	JMenu mnuEdit = new JMenu();
	JMenuItem mnuEditRenameLevel = new JMenuItem();
	JMenuItem mnuEditAddLevel = new JMenuItem();
	JMenuItem mnuEditInsertLevels = new JMenuItem();
	JMenuItem mnuEditResizeLevel = new JMenuItem();
	JMenuItem mnuEditSetAllEnemyLevels = new JMenuItem();
	JMenuItem mnuEditNukeObjects = new JMenuItem();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JComboBox cmbMainType = new JComboBox();
	JComboBox cmbLandSubmenu = new JComboBox();
	JComboBox cmbLandSubtype = new JComboBox();
	JCheckBox chkCanBeDestroyed = new JCheckBox();
	JLabel lblNextCity = new JLabel();
	JTextField txtNextCity = new JTextField();
	JLabel lblDestRow = new JLabel();
	JTextField txtDestRow = new JTextField();
	JLabel lblDestCol = new JLabel();
	JTextField txtDestCol = new JTextField();
	JLabel lblLand = new JLabel();
	JCheckBox chkIsOpen = new JCheckBox();
	JLabel lblShopNumber = new JLabel();
	JTextField txtShopNumber = new JTextField();
	JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
	JLabel lblObject = new JLabel();
	JComboBox cmbObjectSubtype = new JComboBox();
	JLabel lblItemData = new JLabel();
	JTextField txtItemData = new JTextField();
	JLabel lblEnemyLevel = new JLabel();
	JTextField txtEnemyLevel = new JTextField();
	JComboBox cmbLevelSelect = new JComboBox();

	private EditorWindow editWindow;
	private String saveLocation = "";

	public EditorControlFrame(TheEditor editor) {
		try {
			editWindow = new EditorWindow(this);
			editor.setWindow(editWindow);
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(gridBagLayout1);
		this.setJMenuBar(jMenuBar1);
		mnuFile.setText("File");
		mnuFileOpen.setText("Open");
		mnuFileOpen.addActionListener(new
				EditorControlFrame_mnuFileOpen_actionAdapter(this));
		mnuFileSaveAs.setText("Save As...");
		mnuFileSaveAs.addActionListener(new
				EditorControlFrame_mnuFileSaveAs_actionAdapter(this));
		mnuFileSave.setText("Save");
		mnuFileSave.addActionListener(new
				EditorControlFrame_mnuFileSave_actionAdapter(this));
		mnuFileExit.setText("Exit");
		mnuFileExit.addActionListener(new
				EditorControlFrame_mnuFileExit_actionAdapter(this));
		mnuEdit.setText("Edit");
		mnuEditRenameLevel.setText("Rename Level");
		mnuEditRenameLevel.addActionListener(new
				EditorControlFrame_mnuEditRenameLevel_actionAdapter(this));
		mnuEditAddLevel.setText("Add Level");
		mnuEditAddLevel.addActionListener(new
				EditorControlFrame_mnuEditAddLevel_actionAdapter(this));
		mnuEditInsertLevels.setText("Insert Levels");
		mnuEditInsertLevels.addActionListener(new
				EditorControlFrame_mnuEditInsertLevels_actionAdapter(this));
		mnuEditResizeLevel.setText("Resize Level");
		mnuEditResizeLevel.addActionListener(new
				EditorControlFrame_mnuEditResizeLevel_actionAdapter(this));
		mnuEditSetAllEnemyLevels.setText("Set All Enemy Levels...");
		mnuEditSetAllEnemyLevels.addActionListener(new
				EditorControlFrame_mnuEditSetAllEnemyLevels_actionAdapter(this));
		mnuEditNukeObjects.setText("Nuke Objects");
		mnuEditNukeObjects.addActionListener(new
				EditorControlFrame_mnuEditNukeObjects_actionAdapter(this));
		chkCanBeDestroyed.setText("Can Be Destroyed");
		lblNextCity.setText("Next City:");
		txtNextCity.setText("");
		lblDestRow.setText("Destination Row:");
		txtDestRow.setText("1");
		lblDestCol.setText("Destination Column:");
		txtDestCol.setText("1");
		lblLand.setFont(new java.awt.Font("Dialog", Font.BOLD, 16));
		lblLand.setText("Land:");
		chkIsOpen.setText("Is Open");
		lblShopNumber.setText("Shop Number:");
		txtShopNumber.setText("0");
		lblObject.setFont(new java.awt.Font("Dialog", Font.BOLD, 16));
		lblObject.setText("Object:");
		lblItemData.setText("Quantity:");
		txtItemData.setText("0");
		lblEnemyLevel.setText("Enemy Level:");
		txtEnemyLevel.setText("1");
		jMenuBar1.add(mnuFile);
		jMenuBar1.add(mnuEdit);
		mnuFile.add(mnuFileOpen);
		mnuFile.add(mnuFileSaveAs);
		mnuFile.add(mnuFileSave);
		mnuFile.addSeparator();
		mnuFile.add(mnuFileExit);
		mnuEdit.add(mnuEditRenameLevel);
		mnuEdit.add(mnuEditAddLevel);
		mnuEdit.add(mnuEditInsertLevels);
		mnuEdit.add(mnuEditResizeLevel);
		mnuEdit.add(mnuEditSetAllEnemyLevels);
		mnuEdit.add(mnuEditNukeObjects);
		this.getContentPane().add(txtEnemyLevel,
				new GridBagConstraints(1, 16, 1, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblEnemyLevel,
				new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(txtItemData,
				new GridBagConstraints(1, 15, 1, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblItemData,
				new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(cmbObjectSubtype,
				new GridBagConstraints(0, 14, 2, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblObject,
				new GridBagConstraints(0, 11, 2, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(cmbMainType,
				new GridBagConstraints(0, 13, 3, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblDestCol,
				new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(txtShopNumber,
				new GridBagConstraints(1, 9, 2, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblShopNumber,
				new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(chkIsOpen,
				new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(lblDestRow,
				new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(txtDestRow,
				new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(txtNextCity,
				new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(lblNextCity,
				new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
						, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(cmbLandSubmenu,
				new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10, 10, 0, 10), 0, 0));
		this.getContentPane().add(cmbLandSubtype,
				new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(txtDestCol,
				new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 10), 0, 0));
		this.getContentPane().add(chkCanBeDestroyed,
				new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 0, 0));
		this.getContentPane().add(separator,
				new GridBagConstraints(0, 10, 2, 1, 1.0, 0.0
						, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(10, 0, 0, 0), 0, 0));
		this.getContentPane().add(lblLand,
				new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(15, 10, 0, 0), 0, 0));
		this.getContentPane().add(cmbLevelSelect,
				new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
						, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 10, 0, 10), 0, 0));

		cmbLandSubmenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateWindowBasedOnLandSelection();
			}
		});

		cmbLandSubtype.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateWindowBasedOnLandSubtype();
			}
		});

		cmbMainType.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateWindowBasedOnMainSelection();
			}
		});

		cmbLevelSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(cmbLevelSelect.getSelectedIndex() < 0) return;
				GameWorld.getInstance().setCurrentLevel(cmbLevelSelect.getSelectedIndex());
				editWindow.resizePanel();
				editWindow.updateScrollbars();
				editWindow.setWindowTitle("Roof Notifier - " + cmbLevelSelect.getSelectedItem() +
						" (" + cmbLevelSelect.getSelectedIndex() + ")");
			}
		});

		setUpInitialSelection();

		for(Component c : this.getContentPane().getComponents()){
			readyForInput(c);
		}
		for(Component c: editWindow.getContentPane().getComponents()){
			readyForInput(c);
		}
		readyForInput(editWindow);
		readyForInput(this);
		readyForInput(getContentPane());
		
		editWindow.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
				try {
		        editWindow.setDirty(true);
				} catch(Exception ex) { }
		    }
		});
		
		addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
				try {
		    	editWindow.setDirty(true);
				} catch(Exception ex) { }
		    }
		});

		int xLength = 230;
		setSize(xLength, 530);
		setTitle("Roof Action");
		int editX = editWindow.getLocation().x;
		int editY = editWindow.getLocation().y;
		this.setLocation(new Point(editX - xLength, editY));

		loadPrefFile();

		setVisible(true);

	}

	/**
	 * Tells the component to listen for keystrokes for movement.
	 * @param comp The component you want to add the listener to.
	 */
	public void readyForInput(Component comp){
		comp.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.isControlDown()){
					editWindow.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				if(e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()){
					save();
				}
				else if(e.getKeyCode() == KeyEvent.VK_1 && e.isControlDown()){
					GameWorld.getInstance().randomizeLevel();
				}
				else if(e.getKeyCode() == KeyEvent.VK_O && e.isControlDown()){
					load();
				}
				else if(e.getKeyCode() == KeyEvent.VK_N && e.isControlDown()){
					addLevel();
				}
				else if(e.getKeyCode() == KeyEvent.VK_DELETE){
					deleteLevel();
				}
				else if(e.getKeyCode() == KeyEvent.VK_Q && e.isAltDown()){
					System.exit(0);
				}
				else if(e.getKeyCode() == KeyEvent.VK_A ||
						e.getKeyCode() == KeyEvent.VK_DIVIDE){
					if(cmbLandSubmenu.getSelectedIndex() > 0)
						cmbLandSubmenu.setSelectedIndex(
								cmbLandSubmenu.getSelectedIndex()-1);
					else
						cmbLandSubmenu.setSelectedIndex(cmbLandSubmenu.getItemCount()-1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_Z ||
						e.getKeyCode() == KeyEvent.VK_MULTIPLY){
					if(cmbLandSubmenu.getSelectedIndex() < cmbLandSubmenu.getItemCount()-1)
						cmbLandSubmenu.setSelectedIndex(
								cmbLandSubmenu.getSelectedIndex()+1);
					else cmbLandSubmenu.setSelectedIndex(0);
				}
				else if(e.getKeyCode() == KeyEvent.VK_S ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD8){
					if(cmbLandSubtype.getSelectedIndex() > 0)
						cmbLandSubtype.setSelectedIndex(
								cmbLandSubtype.getSelectedIndex()-1);
					else cmbLandSubtype.setSelectedIndex(cmbLandSubtype.getItemCount()-1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_X ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD9){
					if(cmbLandSubtype.getSelectedIndex() < cmbLandSubtype.getItemCount()-1)
						cmbLandSubtype.setSelectedIndex(
								cmbLandSubtype.getSelectedIndex()+1);
					else cmbLandSubtype.setSelectedIndex(0);
				}
				else if(e.getKeyCode() == KeyEvent.VK_D ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD5){
					if(cmbMainType.getSelectedIndex() > 0)
						cmbMainType.setSelectedIndex(
								cmbMainType.getSelectedIndex()-1);
					else cmbMainType.setSelectedIndex(cmbMainType.getItemCount()-1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_C ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD6){
					if(cmbMainType.getSelectedIndex() < cmbMainType.getItemCount()-1)
						cmbMainType.setSelectedIndex(
								cmbMainType.getSelectedIndex()+1);
					else cmbMainType.setSelectedIndex(0);
				}
				else if(e.getKeyCode() == KeyEvent.VK_F ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD2){
					if(cmbObjectSubtype.getSelectedIndex() > 0)
						cmbObjectSubtype.setSelectedIndex(
								cmbObjectSubtype.getSelectedIndex()-1);
					else cmbObjectSubtype.setSelectedIndex(cmbObjectSubtype.getItemCount()-1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_V ||
						e.getKeyCode() == KeyEvent.VK_NUMPAD3){
					try {
						if(cmbObjectSubtype.getSelectedIndex() < cmbObjectSubtype.getItemCount()-1)
							cmbObjectSubtype.setSelectedIndex(
									cmbObjectSubtype.getSelectedIndex()+1);
						else cmbObjectSubtype.setSelectedIndex(0);
					} catch(Exception ex) { }
				}
			}
			public void keyReleased(KeyEvent e){
				if(!e.isControlDown()){
					editWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}

	/**
	 * Fills in the combo boxes with lists, and picks the first
	 * option in the main type combo box.  It will then update
	 * the rest of the display to match the current selection.
	 * This is called when the window is first created.
	 */
	private void setUpInitialSelection(){
		fillLevelSelectBox();

		// Land
		for(LandType l : LandType.values()){
			cmbLandSubmenu.addItem(l.getName());
		}

		cmbLandSubmenu.setSelectedIndex(0);

		updateWindowBasedOnLandSelection();

		// Objects
		for(EditorMainTypes t : EditorMainTypes.values()){
			cmbMainType.addItem(t.getName());
		}
		cmbMainType.setSelectedIndex(0);

		updateWindowBasedOnMainSelection();
	}

	/**
	 * Fills the level select box with the city names.
	 */
	public void fillLevelSelectBox(){
		cmbLevelSelect.removeAllItems();

		for(int i = 0; i < GameWorld.getInstance().getCities().size(); i++){
			cmbLevelSelect.addItem(GameWorld.getInstance().getCityName(i));
		}
	}

	/**
	 * Checks to see what type is selected in the main type
	 * selector and makes components visible/invisible based
	 * on that choice.  It will also fill in the combo boxes
	 * with the correct set of lists for that selection.
	 */
	private void updateWindowBasedOnMainSelection(){
		EditorMainTypes type = EditorMainTypes.values()[cmbMainType.getSelectedIndex()];

		cmbObjectSubtype.removeAllItems();

		if(type == EditorMainTypes.ENEMY){
			cmbObjectSubtype.setEnabled(true);
			int count = 0;
			for(Enemies e : Enemies.values()){
				if(e.getUsed()) cmbObjectSubtype.addItem(e.getName() + " (" + count++ + ")");
			}
			lblItemData.setEnabled(false);
			txtItemData.setEnabled(false);
			lblEnemyLevel.setEnabled(true);
			txtEnemyLevel.setEnabled(true);
		}
		else if(type == EditorMainTypes.ITEM){
			cmbObjectSubtype.setEnabled(true);
			cmbObjectSubtype.addItem(GroundItems.BANDAIDS.getName());
			cmbObjectSubtype.addItem(GroundItems.BULLETS.getName());
			cmbObjectSubtype.addItem(GroundItems.MONEY.getName());
			cmbObjectSubtype.addItem(GroundItems.ROCKETS.getName());

			lblItemData.setEnabled(true);
			txtItemData.setEnabled(true);
			lblEnemyLevel.setEnabled(false);
			txtEnemyLevel.setEnabled(false);
		}
		else if(type == EditorMainTypes.NPC){
			cmbObjectSubtype.setEnabled(true);
			int count = 0;
			for(NPCs n : NPCs.values()){
				if(n.getUsed()) cmbObjectSubtype.addItem(n.getName() + " (" + count++ + ")");
			}
			lblItemData.setEnabled(false);
			txtItemData.setEnabled(false);
			lblEnemyLevel.setEnabled(false);
			txtEnemyLevel.setEnabled(false);
		}
		else if(type == EditorMainTypes.PLAYER){
			cmbObjectSubtype.setEnabled(false);
			lblItemData.setEnabled(false);
			txtItemData.setEnabled(false);
			lblEnemyLevel.setEnabled(false);
			txtEnemyLevel.setEnabled(false);
		}
	}

	/**
	 * Updates the labels based on thel and subtype selection.  It
	 * only does something if Land type exit is chosen.  Based on the
	 * subtype, it will update the label for destination row of the
	 * exit to be more clear as to what it does.
	 */
	private void updateWindowBasedOnLandSubtype(){
		LandType landType = LandType.values()[cmbLandSubmenu.getSelectedIndex()];

		try {
			ExitType type = ExitType.values()[cmbLandSubtype.getSelectedIndex()];

			if(landType != LandType.EXIT) return;

			if(type == ExitType.CITY_GATE)
				lblDestRow.setText("Destination Row:");
			else lblDestRow.setText("Dungeon Level:");
		} catch(Exception ex) {}
	}

	/**
	 * Checks to see what type is selected in the land
	 * type submenu and sets components based on that
	 * choice.
	 */
	private void updateWindowBasedOnLandSelection(){
		LandType type = LandType.values()[cmbLandSubmenu.getSelectedIndex()];

		cmbLandSubtype.removeAllItems();

		if(type == LandType.EXIT){
			for(ExitType e : ExitType.values()){
				cmbLandSubtype.addItem(e.getName());
			}
			chkCanBeDestroyed.setEnabled(false);
			chkIsOpen.setEnabled(true);
			lblNextCity.setEnabled(true);
			txtNextCity.setEnabled(true);
			lblDestRow.setEnabled(true);
			txtDestRow.setEnabled(true);
			lblDestCol.setEnabled(true);
			txtDestCol.setEnabled(true);
			lblShopNumber.setEnabled(false);
			txtShopNumber.setEnabled(false);
		}
		else if(type == LandType.OBSTRUCTION){
			for(ObstructionLandType o : ObstructionLandType.values()){
				if(o.getUsed()) cmbLandSubtype.addItem(o.getName());
			}
			chkCanBeDestroyed.setEnabled(true);
			chkIsOpen.setEnabled(false);
			lblNextCity.setEnabled(false);
			txtNextCity.setEnabled(false);
			lblDestRow.setEnabled(false);
			txtDestRow.setEnabled(false);
			lblDestCol.setEnabled(false);
			txtDestCol.setEnabled(false);
			lblShopNumber.setEnabled(false);
			txtShopNumber.setEnabled(false);
		}
		else if(type == LandType.SHOP){
			for(Shops s : Shops.values()){
				cmbLandSubtype.addItem(s.getName());
			}
			chkCanBeDestroyed.setEnabled(false);
			chkIsOpen.setEnabled(false);
			lblNextCity.setEnabled(false);
			txtNextCity.setEnabled(false);
			lblDestRow.setEnabled(false);
			txtDestRow.setEnabled(false);
			lblDestCol.setEnabled(false);
			txtDestCol.setEnabled(false);
			lblShopNumber.setEnabled(true);
			txtShopNumber.setEnabled(true);
		}
		else if(type == LandType.STREET){
			for(StreetType s : StreetType.values()){
				if(s.getUsed()) cmbLandSubtype.addItem(s.getName());
			}
			chkCanBeDestroyed.setEnabled(false);
			chkIsOpen.setEnabled(false);
			lblNextCity.setEnabled(false);
			txtNextCity.setEnabled(false);
			lblDestRow.setEnabled(false);
			txtDestRow.setEnabled(false);
			lblDestCol.setEnabled(false);
			txtDestCol.setEnabled(false);
			lblShopNumber.setEnabled(false);
			txtShopNumber.setEnabled(false);
		}
	}

	/**
	 * Creates a pop up error message with the given text.
	 * @param str String : error message
	 */
	private void errorMessage(String str){
		JOptionPane.showMessageDialog(
				editWindow,
				str,
				"ROOF NOTIFICATION!", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Checks to see whether the specified row and column is on
	 * the border of the current city or outside of the city.
	 * It will return a Border enum specifying whether it is
	 * in the city, on the border of the city, or outside the
	 * city.
	 * @param row int : row
	 * @param col int : col
	 * @return Bounds : Bounds enum type
	 */
	private Bounds checkBounds(int row, int col){
		int rows = GameWorld.getInstance().getLand().length;
		int cols = GameWorld.getInstance().getLand()[0].length;
		if(row < 0 || row > rows-1 || col < 0 || col > cols-1) return Bounds.OUTSIDE;
		if(row == 0 || row == rows-1 || col == 0 || col == cols-1) return Bounds.BORDER;
		else return Bounds.INSIDE;
	}

	/**
	 * The user has left clicked on the main editor window.  This
	 * method will create the currently selected land object at
	 * this location.
	 * @param row int : row to place land
	 * @param col int : column to place land
	 */
	public void leftClick(int row, int col){
		LandType currentLandType = LandType.values()[cmbLandSubmenu.getSelectedIndex()];

		if(currentLandType == LandType.EXIT){
			ExitType currentExitType = ExitType.values()[cmbLandSubtype.getSelectedIndex()];
			boolean isOpen = chkIsOpen.isSelected();
			try {
				int nextCity = Integer.parseInt(txtNextCity.getText());
				int destRow = Integer.parseInt(txtDestRow.getText());
				int destCol = Integer.parseInt(txtDestCol.getText());

				GameWorld.getInstance().setLandAt(row, col, new Exit(
						currentExitType, nextCity, destRow, destCol, isOpen));
			} catch(NumberFormatException formatEx){
				errorMessage("The 'Next City' 'Destination Row' " + 
				"or 'Destination Column' is not filled in correctly.");
			}
		}
		else if(currentLandType == LandType.OBSTRUCTION){
			ObstructionLandType currentObsType = ObstructionLandType.values(
			)[cmbLandSubtype.getSelectedIndex()];

			boolean canBeDestroyed = chkCanBeDestroyed.isSelected();
			if(checkBounds(row, col) == Bounds.BORDER) canBeDestroyed = false;

			GameWorld.getInstance().setLandAt(row, col, new Obstruction(
					currentObsType, canBeDestroyed));
		}
		else if(currentLandType == LandType.SHOP){
			if(checkBounds(row, col) == Bounds.BORDER || 
					checkBounds(row,col) == Bounds.OUTSIDE) return;

			Shops currentShopType = Shops.values()[cmbLandSubtype.getSelectedIndex()];

			try{
				int shopNumber = Integer.parseInt(txtShopNumber.getText());

				GameWorld.getInstance().setLandAt(row, col, new Shop(
						currentShopType, shopNumber));
			} catch(NumberFormatException formatEx){
				errorMessage("The 'Shop Number' is not filled in correctly.");
			}
		}
		else if(currentLandType == LandType.STREET){
			if(checkBounds(row, col) == Bounds.BORDER || 
					checkBounds(row,col) == Bounds.OUTSIDE) return;

			StreetType currentStreetType = StreetType.getType(
					(String)cmbLandSubtype.getSelectedItem());

			GameWorld.getInstance().setLandAt(row, col, new Street(
					currentStreetType, -1, null, null, null));
		}
	} // end left click

	/**
	 * The user has middle clicked on the main editor window.  This
	 * method will change what is selected based on what object
	 * was clicked on.  This will allow you to view the information
	 * on something that is already placed on the map.
	 * @param row int : row of object
	 * @param col int : column of object
	 */
	public void middleClick(int row, int col){
		try {
			Land land = GameWorld.getInstance().getLandAt(row, col);

			// Set object info
			if(land.getEnemy() != null || land.getItem() != null ||
					land.getNPC() != null){

				setRowColDisplay(row, col);

				if(land.getEnemy() != null){
					cmbMainType.setSelectedIndex(EditorMainTypes.ENEMY.getIndex());
					cmbObjectSubtype.setSelectedIndex(land.getEnemy().getType().getType());
					txtEnemyLevel.setText(String.valueOf(land.getEnemy().getAdvanced()));
				}
				else if(land.getItem() != null){
					cmbMainType.setSelectedIndex(EditorMainTypes.ITEM.getIndex());
					cmbObjectSubtype.setSelectedItem(land.getItem().getType().getName());
					txtItemData.setText(String.valueOf(land.getItem().getData()));
				}
				else if(land.getNPC() != null){
					cmbMainType.setSelectedIndex(EditorMainTypes.NPC.getIndex());
					cmbObjectSubtype.setSelectedIndex(land.getNPC().getType().getType());
				}
			}

			// Set land info
			else {
				if(land instanceof Exit){
					cmbLandSubmenu.setSelectedIndex(LandType.EXIT.getTypeNum());
					cmbLandSubtype.setSelectedIndex( ((Exit)land).getType().getType());
					chkIsOpen.setSelected(((Exit)land).getIsOpen());
					txtNextCity.setText(String.valueOf( ((Exit)land).getNextCity()));
					txtDestRow.setText(String.valueOf( ((Exit)land).getDestinationRow()));
					txtDestCol.setText(String.valueOf( ((Exit)land).getDestinationCol()));
				}
				else if(land instanceof Obstruction){
					cmbLandSubmenu.setSelectedIndex(LandType.OBSTRUCTION.getTypeNum());
					cmbLandSubtype.setSelectedIndex( ((Obstruction)land).getType().getType());
					chkCanBeDestroyed.setSelected( ((Obstruction)land).isDestroyable());

					setRowColDisplay(row, col);
				}
				else if(land instanceof Shop){
					cmbLandSubmenu.setSelectedIndex(LandType.SHOP.getTypeNum());
					cmbLandSubtype.setSelectedIndex( ((Shop)land).getType().getType());
					txtShopNumber.setText(String.valueOf( ((Shop)land).getPermutation()));

					setRowColDisplay(row, col);
				}
				else if(land instanceof Street){
					cmbLandSubmenu.setSelectedIndex(LandType.STREET.getTypeNum());
					cmbLandSubtype.setSelectedItem( ((Street)land).getType().getName());

					setRowColDisplay(row, col);
				}
			}
		} catch(Exception ex){ }
	}

	/**
	 * Sets the row and column fields in the display to the specified
	 * row and column.  It will set the "Destination Row" and "Destination
	 * Column" text fields to show the information.  This is used when
	 * middle clicking on the land, to see which row and column that land
	 * object is in.
	 * @param row int : row
	 * @param col int : col
	 */
	private void setRowColDisplay(int row, int col){
		lblDestRow.setText("Destination Row:");
		txtDestRow.setText(row + "");
		txtDestCol.setText(col + "");
	}

	/**
	 * The user has right clicked on the main editor window.  This
	 * method will create the currently selected object at
	 * this location.
	 * @param row int : row to place object
	 * @param col int : column to place object
	 */
	public void rightClick(int row, int col){
		EditorMainTypes currentMainType = EditorMainTypes.values()[cmbMainType.getSelectedIndex()];

		if(currentMainType == EditorMainTypes.ENEMY){
			Enemies currentEnemyType = Enemies.values()[cmbObjectSubtype.getSelectedIndex()];

			try{
				int enemyLevel = Integer.parseInt(txtEnemyLevel.getText());

				if(enemyLevel <= 0) enemyLevel = 1;

				GameWorld.getInstance().getLandAt(row, col).setEnemy(new Enemy(
						currentEnemyType, row, col, enemyLevel));
			} catch(NumberFormatException formatEx){
				errorMessage("The 'Enemy Level' is not filled in correctly.");
			}
		}
		else if(currentMainType == EditorMainTypes.ITEM){
			GroundItems currentItemType = GroundItems.getType(
					(String)cmbObjectSubtype.getSelectedItem());

			try{
				int data = Integer.parseInt(txtItemData.getText());

				if(data < 0) data = 0;

				GameWorld.getInstance().getLandAt(row, col).setItem(new Item(
						currentItemType, data));
			} catch(NumberFormatException formatEx){
				errorMessage("The 'Item Data' is not filled in correctly.");
			}
		}
		else if(currentMainType == EditorMainTypes.NPC){
			NPCs currentNPCType = NPCs.values()[cmbObjectSubtype.getSelectedIndex()];

			GameWorld.getInstance().getLandAt(row, col).setNPC(new NPC(
					currentNPCType));
		}
		else if(currentMainType == EditorMainTypes.PLAYER){
			for(int i = 0; i < GameWorld.getInstance().getLand().length; i++){
				for(int k = 0; k < GameWorld.getInstance().getLand()[0].length; k++){
					GameWorld.getInstance().getLandAt(i, k).setPlayer(-1);
				}
			}
			GameWorld.getInstance().getLandAt(row, col).setPlayer(0);
		}
	} // end right click

	/**
	 * Brings up the add level frame.
	 */
	private void addLevel(){
		new AddLevelFrame(this);
	}

	/**
	 * Brings up a dialog asking if the level should be deleted.  If
	 * yes, delete the current level.
	 */
	private void deleteLevel(){
		// Can't delete if there's only one map in the file.
		if(GameWorld.getInstance().getCities().size() == 1) return;

		Object[] options = {"Yes", "Cancel"};
		int n = JOptionPane.showOptionDialog(new Frame(),
				"Are you sure you want to delete this level?",
				"Delete",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		if(n == 1) {
			return;
		}

		int currentLevel = GameWorld.getInstance().getCurrentLevel();
		GameWorld.getInstance().removeCity(currentLevel);

		fillLevelSelectBox();

		cmbLevelSelect.setSelectedIndex(GameWorld.getInstance().getCurrentLevel());
	}

	/**
	 * Happens at the end of adding a level.  This updates the GUI so
	 * the new level shows up properly.
	 */
	public void finishAddLevel(){
		fillLevelSelectBox();
		cmbLevelSelect.setSelectedIndex(cmbLevelSelect.getItemCount()-1);
		updateForSizeChange();
	}

	/**
	 * Updates the window when the size of the city changes.
	 */
	public void updateForSizeChange(){
		editWindow.resizePanel();
		editWindow.updateScrollbars();
	}

	/**
	 * Loads the information from the prefs file and sets the window
	 * size based on it.
	 */
	private void loadPrefFile(){
		try {
			Scanner scan = new Scanner(new File(PREFS_FILE));
			String pref = scan.nextLine();
			scan.close();

			StringTokenizer st = new StringTokenizer(pref, ",");

			int thisX, thisY, thisWidth, thisHeight, editX, editY, editWidth, editHeight;

			thisX = Integer.parseInt(st.nextToken());
			thisY = Integer.parseInt(st.nextToken());
			thisWidth = Integer.parseInt(st.nextToken());
			thisHeight = Integer.parseInt(st.nextToken());

			editX = Integer.parseInt(st.nextToken());
			editY = Integer.parseInt(st.nextToken());
			editWidth = Integer.parseInt(st.nextToken());
			editHeight = Integer.parseInt(st.nextToken());

			this.setLocation(thisX, thisY);
			this.setSize(thisWidth, thisHeight);

			editWindow.setLocation(editX, editY);
			editWindow.setSize(editWidth, editHeight);
			editWindow.resizePanel();
			editWindow.updateScrollbars();

		} catch(Exception ex) {}
	}

	/**
	 * Writes the window locations and sizes to a preferences file.
	 */
	private void writePrefFile(){
		try {
			FileWriter writer = new FileWriter(PREFS_FILE, false);
			BufferedWriter out = new BufferedWriter(writer);
			String pref = "";

			pref += this.getLocation().x + "," + this.getLocation().y + "," +
			this.getSize().width + "," + this.getSize().height + ",";

			pref += editWindow.getLocation().x + "," + editWindow.getLocation().y + "," +
			editWindow.getSize().width + "," + editWindow.getSize().height;

			out.write(pref);
			out.close();
		} catch(Exception ex) {}
	}
	
	/**
	 * Checks to see if every city has a player start location.  If one does
	 * not, it will give an error message to the user, and return false.
	 * Otherwise, it will return true.
	 * @return boolean : start locations on every city
	 */
	private boolean checkForStartLocations(){
		for(Land[][] city : GameWorld.getInstance().getCities()){
			boolean start = false;
			for(int i = 0; i < city.length; i++){
				for(int k = 0; k < city[0].length; k++){
					if(city[i][k].hasPlayer() > -1)
						start = true;
				}
			}
			if(!start){
				JOptionPane.showMessageDialog(
						editWindow,
						"One of your cities does not have a player start location.",
						"ROOF NOTIFICATION!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	/**
	 * If a save path has already been set up, just save to
	 * that file.  Otherwise, call the saveAs method.
	 */
	private void save(){
		if(saveLocation.equals("")){
			saveAs();
			return;
		}
		else {
			if(!checkForStartLocations()) return;
			
			try{
				writePrefFile();
				
				// Serialization
				/*FileOutputStream fout = new FileOutputStream(saveLocation);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				GameWorld.getInstance().save(oos);
				oos.close();*/
				
				// Map format
				MapFile.getInstance().save(saveLocation);
			} catch(Exception ex){
				System.out.println("Problem saving.");
			}
		}
	}

	/**
	 * Brings up a save dialog and saves the map file.
	 */
	private void saveAs(){
		if(!checkForStartLocations()) return;
		try{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/maps"));
			int retVal = fc.showSaveDialog(editWindow);
			if(retVal == JFileChooser.APPROVE_OPTION){
				saveLocation = fc.getSelectedFile().getAbsolutePath();
				writePrefFile();
				
				// Serialization
				/*FileOutputStream fout = new FileOutputStream(saveLocation);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				GameWorld.getInstance().save(oos);
				oos.close();*/
				
				// Map format
				MapFile.getInstance().save(saveLocation);
			}
		} catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Problem saving.");
		}
	}

	/**
	 * Brings up a file dialog and loads from the chosen file.
	 */
	private void load(){
		try{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/maps"));
			int retVal = fc.showOpenDialog(editWindow);
			if(retVal == JFileChooser.APPROVE_OPTION){
				saveLocation = fc.getSelectedFile().getAbsolutePath();
				
				// Serialization
				/*FileInputStream fin = new FileInputStream(fc.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fin);
				GameWorld.getInstance().load(ois);
				ois.close();*/
				
				// Map format
				MapFile.getInstance().load(saveLocation);
			}
		} catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Problem loading.");
		}
		fillLevelSelectBox();
		editWindow.resizePanel();
		editWindow.updateScrollbars();
	}

	/**
	 * File open menu.
	 * @param e ActionEvent
	 */
	public void mnuFileOpen_actionPerformed(ActionEvent e) {
		load();
	}

	/**
	 * File Save as menu
	 * @param e ActionEvent
	 */
	public void mnuFileSaveAs_actionPerformed(ActionEvent e) {
		saveAs();
	}

	/**
	 * File Save menu
	 * @param e ActionEvent
	 */
	public void mnuFileSave_actionPerformed(ActionEvent e) {
		save();
	}

	/**
	 * File exit menu
	 * @param e Action event
	 */
	public void mnuFileExit_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/**
	 * Edit add level menu.
	 * @param e ActionEvent
	 */
	public void mnuEditAddLevel_actionPerformed(ActionEvent e) {
		addLevel();
	}

	/**
	 * Edit insert levels menu.  It creates temporary instances of the gameworld
	 * and adds the levels from a specified file to the currently loaded
	 * gameworld.
	 * @param e ActionEvent
	 */
	public void mnuEditInsertLevels_actionPerformed(ActionEvent e) {

		try{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/maps"));
			int retVal = fc.showOpenDialog(editWindow);
			if(retVal == JFileChooser.APPROVE_OPTION){
				GameWorld currentGameWorld = GameWorld.getInstance().getTempInstance();
				currentGameWorld.setCities(GameWorld.getInstance().getCities());
				currentGameWorld.setCityNames(GameWorld.getInstance().getCityNames());

				MapFile.getInstance().load(fc.getSelectedFile().getAbsolutePath());

				GameWorld newGameWorld = GameWorld.getInstance().getTempInstance();
				newGameWorld.setCities(GameWorld.getInstance().getCities());
				newGameWorld.setCityNames(GameWorld.getInstance().getCityNames());

				// Reset the old instance to the current one.
				GameWorld.setInstance(currentGameWorld);

				// Add the levels from the new instance to the old one.
				for(int i = 0; i < newGameWorld.getCities().size(); i++){
					GameWorld.getInstance().getCities().add(newGameWorld.getCities().get(i));
					if(GameWorld.getInstance().getCityNames().contains(newGameWorld.getCityNames().get(i))){
						GameWorld.getInstance().getCityNames().add(newGameWorld.getCityNames().get(i) + 
								" new" + GameWorld.getInstance().getCities().size());
					}
					else GameWorld.getInstance().getCityNames().add(newGameWorld.getCityNames().get(i));
				}

				int currentLv = GameWorld.getInstance().getCurrentLevel();

				// Refresh the editor, so it's got the new levels loaded
				finishAddLevel();
				GameWorld.getInstance().setCurrentLevel(currentLv);
				cmbLevelSelect.setSelectedIndex(currentLv);

			}
		} catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Problem inserting levels.");
		}
	}

	/**
	 * Edit resize level menu
	 * @param e ActionEvent
	 */
	public void mnuEditResizeLevel_actionPerformed(ActionEvent e) {
		new ResizeFrame(this);
	}

	/**
	 * Edit set all enemy levels menu
	 * @param e ActionEvent
	 */
	public void mnuEditSetAllEnemyLevels_actionPerformed(ActionEvent e) {
		String levelStr =  JOptionPane.showInputDialog("Set enemies to level:");
		int level = 1;
		try{
			level = Integer.parseInt(levelStr);
		} catch(Exception ex){
			level = 1;
		}
		if(level < 1) level = 1;

		GameWorld.getInstance().setEnemiesToLevel(level);
	}

	/**
	 * Edit nuke objects menu.  Removes all objects from the current
	 * level.
	 * @param e ActionEvent
	 */
	public void mnuEditNukeObjects_actionPerformed(ActionEvent e) {
		GameWorld.getInstance().nukeObjects();
	}

	/**
	 * Edit rename level menu.
	 * @param e ActionEvent
	 */
	public void mnuEditRenameLevel_actionPerformed(ActionEvent e) {
		try {
			int curLevel = GameWorld.getInstance().getCurrentLevel();
			String newName = JOptionPane.showInputDialog("New City Name:");
			if(newName.equals("")) return;
			GameWorld.getInstance().setCurrentCityName(newName);
			fillLevelSelectBox();
			cmbLevelSelect.setSelectedIndex(curLevel);
		} catch(src.exceptions.CityNameExistsException nameExists){
			JOptionPane.showMessageDialog(
					editWindow,
					"You must enter a unique city name.",
					"ROOF NOTIFICATION!", JOptionPane.ERROR_MESSAGE);
		} catch(Exception ex) {}
	}
}


class EditorControlFrame_mnuEditNukeObjects_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditNukeObjects_actionAdapter(EditorControlFrame
			adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditNukeObjects_actionPerformed(e);
	}
}

class EditorControlFrame_mnuEditRenameLevel_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditRenameLevel_actionAdapter(EditorControlFrame
			adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditRenameLevel_actionPerformed(e);
	}
}


class EditorControlFrame_mnuEditSetAllEnemyLevels_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditSetAllEnemyLevels_actionAdapter(
			EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditSetAllEnemyLevels_actionPerformed(e);
	}
}


class EditorControlFrame_mnuEditResizeLevel_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditResizeLevel_actionAdapter(EditorControlFrame
			adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditResizeLevel_actionPerformed(e);
	}
}


class EditorControlFrame_mnuEditInsertLevels_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditInsertLevels_actionAdapter(EditorControlFrame
			adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditInsertLevels_actionPerformed(e);
	}
}


class EditorControlFrame_mnuEditAddLevel_actionAdapter implements
ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuEditAddLevel_actionAdapter(EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuEditAddLevel_actionPerformed(e);
	}
}


class EditorControlFrame_mnuFileExit_actionAdapter implements ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuFileExit_actionAdapter(EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuFileExit_actionPerformed(e);
	}
}


class EditorControlFrame_mnuFileSave_actionAdapter implements ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuFileSave_actionAdapter(EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuFileSave_actionPerformed(e);
	}
}


class EditorControlFrame_mnuFileSaveAs_actionAdapter implements ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuFileSaveAs_actionAdapter(EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuFileSaveAs_actionPerformed(e);
	}
}


class EditorControlFrame_mnuFileOpen_actionAdapter implements ActionListener {
	private EditorControlFrame adaptee;
	EditorControlFrame_mnuFileOpen_actionAdapter(EditorControlFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.mnuFileOpen_actionPerformed(e);
	}
}
