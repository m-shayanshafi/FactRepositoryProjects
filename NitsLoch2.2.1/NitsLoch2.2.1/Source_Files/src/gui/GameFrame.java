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

package src.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import src.Constants;
import src.enums.Armor;
import src.enums.Direction;
import src.enums.DungeonWalls;
import src.enums.ExitType;
import src.enums.Facing;
import src.enums.Weapon;
import src.game.Messages;
import src.game.TheGame;
import src.land.Exit;
import src.land.Land;
import src.land.Obstruction;
import src.land.Shop;
import src.land.Street;
import src.scenario.Images;

/**
 * This class contains the components that make up the GUI.
 */
public class GameFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	private enum FrontWallArrangement {
		NO_FRONT_WALL,
		WALL_LEFT,
		WALL_RIGHT,
		WALL_BOTH,
		WALL_NONE,
		LADDER_UP_LEFT,
		LADDER_UP_RIGHT,
		LADDER_UP_BOTH,
		LADDER_UP_NONE,
		LADDER_DOWN_LEFT,
		LADDER_DOWN_RIGHT,
		LADDER_DOWN_BOTH,
		LADDER_DOWN_NONE;
	}

	private enum FrontWallType{
		LADDER_UP,
		LADDER_DOWN,
		WALL;
	}

	// Dimentions for frame. P = Width/height of actual window. G is game
	// window.
	private final int PWIDTH = 655;
	private final int PHEIGHT = 580;
	private final int GHEIGHT = 400;
	private final int GWIDTH = 400;
	private final int GX_MIN = 240;
	private final int GY_MIN = 160;

	// Polygons for click-moving.
	private final int[] W_X = { 245, 245, 410, 410 };
	private final int[] W_Y = { 245, 420, 370, 325 };
	private final Polygon W_POLY = new Polygon(W_X, W_Y, 4);

	private final int[] NW_X = { 245, 245, 350, 419, 410 };
	private final int[] NW_Y = { 244, 150, 150, 315, 324 };
	private final Polygon NW_POLY = new Polygon(NW_X, NW_Y, 5);

	private final int[] N_X = { 351, 530, 465, 420 };
	private final int[] N_Y = { 150, 150, 315, 315 };
	private final Polygon N_POLY = new Polygon(N_X, N_Y, 4);

	private final int[] NE_X = { 531, 640, 640, 475, 466 };
	private final int[] NE_Y = { 150, 150, 244, 324, 315 };
	private final Polygon NE_POLY = new Polygon(NE_X, NE_Y, 5);

	private final int[] E_X = { 640, 640, 475, 475 };
	private final int[] E_Y = { 245, 420, 370, 325 };
	private final Polygon E_POLY = new Polygon(E_X, E_Y, 4);

	private final int[] SE_X = { 640, 640, 531, 466, 475 };
	private final int[] SE_Y = { 421, 545, 545, 380, 370 };
	private final Polygon SE_POLY = new Polygon(SE_X, SE_Y, 5);

	private final int[] S_X = { 351, 530, 465, 420 };
	private final int[] S_Y = { 545, 545, 380, 380 };
	private final Polygon S_POLY = new Polygon(S_X, S_Y, 4);

	private final int[] SW_X = { 350, 245, 245, 410, 420 };
	private final int[] SW_Y = { 545, 545, 421, 371, 380 };
	private final Polygon SW_POLY = new Polygon(SW_X, SW_Y, 5);

	private Controller controller = null;
	private JPanel jContentPane = null;
	private JMenuBar jMenuBar = null;
	private JTextArea messages = null;

	//private boolean dirty = true;

	private boolean justMoved = true;

	/* Labels */
	private JLabel jLabelPlayerName = null;
	private JLabel jLabelPlayerHitPoints = null;
	private JLabel jLabelPlayerFightAbil = null;
	private JLabel jLabelPlayerMarksAbil = null;
	private JLabel jLabelPlayerMartArtsAbil = null;
	private JLabel jLabelPlayerThievingAbil = null;
	private JLabel jLabelPlayerInventory = null;
	private JLabel jLabelPlayerMoney = null;
	private JLabel jLabelPlayerGrenades = null;
	private JLabel jLabelPlayerDynamite = null;
	private JLabel jLabelPlayerBandaids = null;
	private JLabel jLabelPlayerBullets = null;
	private JLabel jLabelPlayerRockets = null;
	private JLabel jLabelPlayerFlamePacks = null;
	private JLabel jLabelPlayerLaddersUp = null;
	private JLabel jLabelPlayerLaddersDown = null;
	private JLabel jLabelPlayerMapViewers = null;
	private JLabel jLabelPlayerExports = null;
	
	/* Options */
	private JCheckBox chkAutoBandaid = null;

	/* Buttons */
	private JButton jButtonPass = null;
	private JButton jButtonUse = null;
	private JButton jButtonShoot = null;

	private JMenu jWeaponMenu = null;
	private JMenu jArmorMenu = null;

	/**
	 * Constructor for GameFrame. Takes no arguments and initializes the frame.
	 * 
	 */
	public GameFrame(Controller cont) {
		super("NitsLoch");
		controller = cont;
		ShopWindow.getInstance().setController(controller);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		readyForInput(jContentPane);
		readyForInput(messages);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.isShiftDown()) controller.setPlayerShooting(true);
				testPress(e.getX(), e.getY());
			}
		});
		
		jContentPane.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
				try {
		        justMoved = true;
		        if(controller.getLocalPlayer().getInDungeon())
		        	dungeonBlankScreen();
				} catch(Exception ex) { }
		    }
		});

	}

	/**
	 * Initializes the frame. This is first called when the frame is created.
	 * It's responsible for setting the size of the frame, showing the content
	 * pane and the menu bar.
	 * 
	 */
	private void initialize() {
		this.dispose();
		this.setSize(PWIDTH, PHEIGHT);
		src.Constants.centerFrame(this);
		this.setVisible(true);
		this.setContentPane(getPane());
		this.setJMenuBar(getMenu());
		TheGame.getInstance().startGame();
	}

	private void newGame() {
		this.dispose();
		controller.newGame();
		new src.scenario.loader.ScenarioLoader(); // Read in scenario data
		new src.gui.CreatePlayerFrame();
	}

	/**
	 * Sets focus on the jContentPane.
	 */
	public void setPanelFocus() {
		jContentPane.grabFocus();
	}

	/**
	 * Creates the menu bar for the game.
	 * 
	 * @return JMenuBar : the menu bar
	 */
	private JMenuBar getMenu() {
		if (jMenuBar == null) {
			jMenuBar = new JMenuBar();
			jMenuBar.add(getJFileMenu());
			jMenuBar.add(getJWeaponMenu());
			jMenuBar.add(getJArmorMenu());
			jMenuBar.add(getJHelpMenu());
		}
		return jMenuBar;
	}

	/**
	 * Creates the file menu.
	 * 
	 * @return JMenu : file menu
	 */
	private JMenu getJFileMenu() {
		JMenu jFileMenu = new JMenu();
		jFileMenu.setText("File");
		jFileMenu.add(getJMenuItemFileNew());
		jFileMenu.add(getJMenuItemFileLoad());
		jFileMenu.add(getJMenuItemFileSaveAs());
		jFileMenu.add(getJMenuItemFileSave());
		jFileMenu.add(getJMenuItemFileImport());
		jFileMenu.add(getJMenuItemFileExport());
		jFileMenu.add(getJMenuItemFileExit());
		return jFileMenu;
	}

	/**
	 * Creates the New Game choice for the file menu. Starts a new game.
	 * 
	 * @return JMenuItem : file -> new
	 */
	private JMenuItem getJMenuItemFileNew() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("New Game");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setGameRunning(false);
				newGame();
			}
		});
		return menuItem;
	}

	/**
	 * Creates the SaveAs choice for the file menu.
	 * 
	 * @return JMenuItem : file -> save as menu
	 */
	private JMenuItem getJMenuItemFileSaveAs() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Save As...");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")
						+ "/saves"));
				int retVal = fc.showSaveDialog(GameFrame.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					controller.saveGame(fc.getSelectedFile().getPath());
					controller.setSavePath(fc.getSelectedFile().getPath());
				} else
					return;
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Save choice for the file menu. If a file has been loaded or
	 * saved, it will use the current save file path. Otherwise, it will bring
	 * up a dialog.
	 * 
	 * @return JMenuItem : file -> save menu
	 */
	private JMenuItem getJMenuItemFileSave() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Save");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (controller.getSavePath() != null
						&& !controller.getSavePath().equals("")) {
					controller.saveGame(controller.getSavePath());
					controller.setSavePath(controller.getSavePath());
				} else {
					final JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File(System
							.getProperty("user.dir")
							+ "/saves"));
					int retVal = fc.showSaveDialog(GameFrame.this);
					if (retVal == JFileChooser.APPROVE_OPTION) {
						controller.saveGame(fc.getSelectedFile().getPath());
						controller.setSavePath(fc.getSelectedFile().getPath());
					} else
						return;
				}
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Open choice for the file menu.
	 * 
	 * @return JMenuItem : file -> load menu
	 */
	private JMenuItem getJMenuItemFileLoad() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Open");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")
						+ "/saves"));
				int retVal = fc.showOpenDialog(GameFrame.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					controller.loadGame(fc.getSelectedFile().getPath());
					controller.setSavePath(fc.getSelectedFile().getPath());
				} else
					return;
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Import choice for the file menu. Imports a character from a
	 * saved file.
	 * 
	 * @return JMenuItem The menu item.
	 */
	private JMenuItem getJMenuItemFileImport() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Import Character");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc
				.setCurrentDirectory(new File(System
						.getProperty("user.dir")));
				int retVal = fc.showOpenDialog(GameFrame.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					controller.importCharacter(fc.getSelectedFile().getPath());
					// PlayerFile pf = new
					// PlayerFile(fc.getSelectedFile().getPath());
					// pf.setStats(controller.getGame().getPlayer());
				} else
					return;
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Export choice for the file menu. Exports a character to a
	 * saved file.
	 * 
	 * @return JMenuItem The menu item.
	 */
	private JMenuItem getJMenuItemFileExport() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Export Character");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc
				.setCurrentDirectory(new File(System
						.getProperty("user.dir")));
				int retVal = fc.showSaveDialog(GameFrame.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					controller.exportCharacter(fc.getSelectedFile().getPath());
					// new PlayerFile(fc.getSelectedFile().getPath(),
					// controller.getGame().getPlayer());
				} else
					return;
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Quit choice for the file menu. This closes down the game.
	 * 
	 * @return JMenuItem : file -> exit menu
	 */
	private JMenuItem getJMenuItemFileExit() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Quit");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.exit(0);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the Weapon menu.
	 * 
	 * @return JMenu : weapon menu
	 */
	private JMenu getJWeaponMenu() {
		if (jWeaponMenu == null) {
			jWeaponMenu = new JMenu();
			jWeaponMenu.setText("Weapon");
			jWeaponMenu.add(getJMenuItemWeapon00());
			jWeaponMenu.add(getJMenuItemWeapon01());
			jWeaponMenu.add(getJMenuItemWeapon02());
			jWeaponMenu.add(getJMenuItemWeapon03());
			jWeaponMenu.add(getJMenuItemWeapon04());
			jWeaponMenu.add(getJMenuItemWeapon05());
			jWeaponMenu.add(getJMenuItemWeapon06());
			jWeaponMenu.add(getJMenuItemWeapon07());
			jWeaponMenu.add(getJMenuItemWeapon08());
			jWeaponMenu.add(getJMenuItemWeapon09());
			jWeaponMenu.add(getJMenuItemWeapon10());
			jWeaponMenu.add(getJMenuItemWeapon11());
			jWeaponMenu.add(getJMenuItemWeapon12());
			jWeaponMenu.add(getJMenuItemWeapon13());
			jWeaponMenu.add(getJMenuItemWeapon14());
			jWeaponMenu.add(getJMenuItemWeapon15());
			jWeaponMenu.add(getJMenuItemWeapon16());
			jWeaponMenu.add(getJMenuItemWeapon17());
			jWeaponMenu.add(getJMenuItemWeapon18());
			jWeaponMenu.add(getJMenuItemWeapon19());
			jWeaponMenu.add(getJMenuItemWeapon20());
			jWeaponMenu.add(getJMenuItemWeapon21());
			jWeaponMenu.add(getJMenuItemWeapon22());
			jWeaponMenu.add(getJMenuItemWeapon23());
			jWeaponMenu.add(getJMenuItemWeapon24());

			for(int i = 0; i < Weapon.values().length; i++){
				if(jWeaponMenu.getItem(i).getText().equals("")){
					for(int k = i; k < Weapon.values().length; k++){
						jWeaponMenu.remove(i);
					}
					return jWeaponMenu;
				}
			}
		}
		return jWeaponMenu;
	}

	/**
	 * Creates the first weapon choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 0
	 */
	private JMenuItem getJMenuItemWeapon00() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_00.getItemName());
		menuItem.setEnabled(true);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_00);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the second choice for the weapon menu.
	 * 
	 * @return JMenuItem : Weapon -> weapon 1
	 */
	private JMenuItem getJMenuItemWeapon01() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_01.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_01);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the third choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapons -> weapon 2
	 */
	private JMenuItem getJMenuItemWeapon02() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_02.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_02);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fourth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 3
	 */
	private JMenuItem getJMenuItemWeapon03() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_03.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_03);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fifth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 4
	 */
	private JMenuItem getJMenuItemWeapon04() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_04.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_04);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the sixth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 5
	 */
	private JMenuItem getJMenuItemWeapon05() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_05.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_05);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the seventh choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 6
	 */
	private JMenuItem getJMenuItemWeapon06() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_06.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_06);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eighth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 7
	 */
	private JMenuItem getJMenuItemWeapon07() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_07.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_07);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the ninth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 8
	 */
	private JMenuItem getJMenuItemWeapon08() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_08.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_08);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the tenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 9
	 */
	private JMenuItem getJMenuItemWeapon09() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_09.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_09);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eleventh choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 10
	 */
	private JMenuItem getJMenuItemWeapon10() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_10.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_10);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twelfth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 11
	 */
	private JMenuItem getJMenuItemWeapon11() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_11.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_11);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the thirteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 12
	 */
	private JMenuItem getJMenuItemWeapon12() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_12.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_12);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fourteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 13
	 */
	private JMenuItem getJMenuItemWeapon13() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_13.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_13);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fifteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 14
	 */
	private JMenuItem getJMenuItemWeapon14() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_14.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_14);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the sixteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 15
	 */
	private JMenuItem getJMenuItemWeapon15() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_15.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_15);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the seventeenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 16
	 */
	private JMenuItem getJMenuItemWeapon16() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_16.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_16);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eighteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 17
	 */
	private JMenuItem getJMenuItemWeapon17() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_17.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_17);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the nineteenth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 18
	 */
	private JMenuItem getJMenuItemWeapon18() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_18.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_18);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twentieth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 19
	 */
	private JMenuItem getJMenuItemWeapon19() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_19.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_19);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-first choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 20
	 */
	private JMenuItem getJMenuItemWeapon20() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_20.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_20);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-second choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 21
	 */
	private JMenuItem getJMenuItemWeapon21() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_21.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_21);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-third choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 22
	 */
	private JMenuItem getJMenuItemWeapon22() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_22.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_22);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-fourth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 23
	 */
	private JMenuItem getJMenuItemWeapon23() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_23.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_23);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-fifth choice for the weapon menu.
	 * 
	 * @return JMenuItem : weapon -> weapon 24
	 */
	private JMenuItem getJMenuItemWeapon24() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Weapon.W_24.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setPlrReadiedWeapon(Weapon.W_24);
			}
		});
		return menuItem;
	}

	/**
	 * Called every tick; sets the weapon in the menu to enabled or disabled
	 * depending on whether or not the player has the weapon.
	 * 
	 * @param available
	 *            The boolean array of available weapons.
	 */
	public void updateWeaponMenu(int[] available) {
		if(jWeaponMenu.getItemCount() >= 2){
			if(available[Weapon.W_01.getType()] > 1)
				jWeaponMenu.getItem(1).setText(
						Weapon.W_01.getItemName() + " +" + available[Weapon.W_01.getType()]);
			else
				jWeaponMenu.getItem(1).setText(Weapon.W_01.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 3){
			if(available[Weapon.W_02.getType()] > 1)
				jWeaponMenu.getItem(2).setText(
						Weapon.W_02.getItemName() + " +" + available[Weapon.W_02.getType()]);
			else
				jWeaponMenu.getItem(2).setText(Weapon.W_02.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 4){
			if(available[Weapon.W_03.getType()] > 1)
				jWeaponMenu.getItem(3).setText(
						Weapon.W_03.getItemName() + " +" + available[Weapon.W_03.getType()]);
			else
				jWeaponMenu.getItem(3).setText(Weapon.W_03.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 5){
			if(available[Weapon.W_04.getType()] > 1)
				jWeaponMenu.getItem(4).setText(
						Weapon.W_04.getItemName() + " +" + available[Weapon.W_04.getType()]);
			else
				jWeaponMenu.getItem(4).setText(Weapon.W_04.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 6){
			if(available[Weapon.W_05.getType()] > 1)
				jWeaponMenu.getItem(5).setText(
						Weapon.W_05.getItemName() + " +" + available[Weapon.W_05.getType()]);
			else
				jWeaponMenu.getItem(5).setText(Weapon.W_05.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 7){
			if(available[Weapon.W_06.getType()] > 1)
				jWeaponMenu.getItem(6).setText(
						Weapon.W_06.getItemName() + " +" + available[Weapon.W_06.getType()]);
			else
				jWeaponMenu.getItem(6).setText(Weapon.W_06.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 8){
			if(available[Weapon.W_07.getType()] > 1)
				jWeaponMenu.getItem(7).setText(
						Weapon.W_07.getItemName() + " +" + available[Weapon.W_07.getType()]);
			else
				jWeaponMenu.getItem(7).setText(Weapon.W_07.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 9){
			if(available[Weapon.W_08.getType()] > 1)
				jWeaponMenu.getItem(8).setText(
						Weapon.W_08.getItemName() + " +" + available[Weapon.W_08.getType()]);
			else
				jWeaponMenu.getItem(8).setText(Weapon.W_08.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 10){
			if(available[Weapon.W_09.getType()] > 1)
				jWeaponMenu.getItem(9).setText(
						Weapon.W_09.getItemName() + " +" + available[Weapon.W_09.getType()]);
			else
				jWeaponMenu.getItem(9).setText(Weapon.W_09.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 11){
			if(available[Weapon.W_10.getType()] > 1)
				jWeaponMenu.getItem(10).setText(
						Weapon.W_10.getItemName() + " +" + available[Weapon.W_10.getType()]);
			else
				jWeaponMenu.getItem(10).setText(Weapon.W_10.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 12){
			if(available[Weapon.W_11.getType()] > 1)
				jWeaponMenu.getItem(11).setText(
						Weapon.W_11.getItemName() + " +" + available[Weapon.W_11.getType()]);
			else
				jWeaponMenu.getItem(11).setText(Weapon.W_11.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 13){
			if(available[Weapon.W_12.getType()] > 1)
				jWeaponMenu.getItem(12).setText(
						Weapon.W_12.getItemName() + " +" + available[Weapon.W_12.getType()]);
			else
				jWeaponMenu.getItem(12).setText(Weapon.W_12.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 14){
			if(available[Weapon.W_13.getType()] > 1)
				jWeaponMenu.getItem(13).setText(
						Weapon.W_13.getItemName() + " +" + available[Weapon.W_13.getType()]);
			else
				jWeaponMenu.getItem(13).setText(Weapon.W_13.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 15){
			if(available[Weapon.W_14.getType()] > 1)
				jWeaponMenu.getItem(14).setText(
						Weapon.W_14.getItemName() + " +" + available[Weapon.W_14.getType()]);
			else
				jWeaponMenu.getItem(14).setText(Weapon.W_14.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 16){
			if(available[Weapon.W_15.getType()] > 1)
				jWeaponMenu.getItem(15).setText(
						Weapon.W_15.getItemName() + " +" + available[Weapon.W_15.getType()]);
			else
				jWeaponMenu.getItem(15).setText(Weapon.W_15.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 17){
			if(available[Weapon.W_16.getType()] > 1)
				jWeaponMenu.getItem(16).setText(
						Weapon.W_16.getItemName() + " +" + available[Weapon.W_16.getType()]);
			else
				jWeaponMenu.getItem(16).setText(Weapon.W_16.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 18){
			if(available[Weapon.W_17.getType()] > 1)
				jWeaponMenu.getItem(17).setText(
						Weapon.W_17.getItemName() + " +" + available[Weapon.W_17.getType()]);
			else
				jWeaponMenu.getItem(17).setText(Weapon.W_17.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 19){
			if(available[Weapon.W_18.getType()] > 1)
				jWeaponMenu.getItem(18).setText(
						Weapon.W_18.getItemName() + " +" + available[Weapon.W_18.getType()]);
			else
				jWeaponMenu.getItem(18).setText(Weapon.W_18.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 20){
			if(available[Weapon.W_19.getType()] > 1)
				jWeaponMenu.getItem(19).setText(
						Weapon.W_19.getItemName() + " +" + available[Weapon.W_19.getType()]);
			else
				jWeaponMenu.getItem(19).setText(Weapon.W_19.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 21){
			if(available[Weapon.W_20.getType()] > 1)
				jWeaponMenu.getItem(20).setText(
						Weapon.W_20.getItemName() + " +" + available[Weapon.W_20.getType()]);
			else
				jWeaponMenu.getItem(20).setText(Weapon.W_20.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 22){
			if(available[Weapon.W_21.getType()] > 1)
				jWeaponMenu.getItem(21).setText(
						Weapon.W_21.getItemName() + " +" + available[Weapon.W_21.getType()]);
			else
				jWeaponMenu.getItem(21).setText(Weapon.W_21.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 23){
			if(available[Weapon.W_22.getType()] > 1)
				jWeaponMenu.getItem(22).setText(
						Weapon.W_22.getItemName() + " +" + available[Weapon.W_22.getType()]);
			else
				jWeaponMenu.getItem(22).setText(Weapon.W_22.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 24){
			if(available[Weapon.W_23.getType()] > 1)
				jWeaponMenu.getItem(23).setText(
						Weapon.W_23.getItemName() + " +" + available[Weapon.W_23.getType()]);
			else
				jWeaponMenu.getItem(23).setText(Weapon.W_23.getItemName());
		}

		if(jWeaponMenu.getItemCount() >= 25){
			if(available[Weapon.W_24.getType()] > 1)
				jWeaponMenu.getItem(24).setText(
						Weapon.W_24.getItemName() + " +" + available[Weapon.W_24.getType()]);
			else
				jWeaponMenu.getItem(24).setText(Weapon.W_24.getItemName());
		}

		for (int i = 0; i < jWeaponMenu.getItemCount(); i++) {
			if (available[i] > 0) {
				if (jWeaponMenu != null)
					jWeaponMenu.getItem(i).setEnabled(true);
			} else
				jWeaponMenu.getItem(i).setEnabled(false);
		}
		int type = controller.getPlrReadiedWeapon().getType();
		for (int i = 0; i < jWeaponMenu.getItemCount(); i++) {
			jWeaponMenu.getItem(i).setForeground(Color.black);
		}
		jWeaponMenu.getItem(type).setForeground(Color.red);
	}

	/**
	 * Creates the Armor menu.
	 * 
	 * @return JMenu : armor menu
	 */
	private JMenu getJArmorMenu() {
		if (jArmorMenu == null) {
			jArmorMenu = new JMenu();
			jArmorMenu.setText("Armor");
			jArmorMenu.add(getJMenuItemArmor00());
			jArmorMenu.add(getJMenuItemArmor01());
			jArmorMenu.add(getJMenuItemArmor02());
			jArmorMenu.add(getJMenuItemArmor03());
			jArmorMenu.add(getJMenuItemArmor04());
			jArmorMenu.add(getJMenuItemArmor05());
			jArmorMenu.add(getJMenuItemArmor06());
			jArmorMenu.add(getJMenuItemArmor07());
			jArmorMenu.add(getJMenuItemArmor08());
			jArmorMenu.add(getJMenuItemArmor09());
			jArmorMenu.add(getJMenuItemArmor10());
			jArmorMenu.add(getJMenuItemArmor11());
			jArmorMenu.add(getJMenuItemArmor12());
			jArmorMenu.add(getJMenuItemArmor13());
			jArmorMenu.add(getJMenuItemArmor14());
			jArmorMenu.add(getJMenuItemArmor15());
			jArmorMenu.add(getJMenuItemArmor16());
			jArmorMenu.add(getJMenuItemArmor17());
			jArmorMenu.add(getJMenuItemArmor18());
			jArmorMenu.add(getJMenuItemArmor19());
			jArmorMenu.add(getJMenuItemArmor20());
			jArmorMenu.add(getJMenuItemArmor21());
			jArmorMenu.add(getJMenuItemArmor22());
			jArmorMenu.add(getJMenuItemArmor23());
			jArmorMenu.add(getJMenuItemArmor24());

			for(int i = 0; i < Armor.values().length; i++){
				if(jArmorMenu.getItem(i).getText().equals("")){
					for(int k = i; k < Armor.values().length; k++){
						jArmorMenu.remove(i);
					}
					return jArmorMenu;
				}
			}
		}
		return jArmorMenu;
	}

	/**
	 * Creates the first choice for the armor menu.
	 * 
	 * @return JMenuItem : Armor -> armor 0
	 */
	private JMenuItem getJMenuItemArmor00() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_00.getItemName());
		menuItem.setEnabled(true);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_00);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the second choice for the armor menu.
	 * 
	 * @return JMenuItem : Armor -> armor 1
	 */
	private JMenuItem getJMenuItemArmor01() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_01.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_01);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the third choice for the armor menu.
	 * 
	 * @return JMenuItem : Armor -> armor 2
	 */
	private JMenuItem getJMenuItemArmor02() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_02.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_02);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fourth choice for the armor menu.
	 * 
	 * @return JMenuItem : Armor -> armor 3
	 */
	private JMenuItem getJMenuItemArmor03() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_03.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_03);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fifth choice for the armor menu.
	 * 
	 * @return JMenuItem : Armor -> armor 4
	 */
	private JMenuItem getJMenuItemArmor04() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_04.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_04);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the sixth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 5
	 */
	private JMenuItem getJMenuItemArmor05() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_05.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_05);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the seventh choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 6
	 */
	private JMenuItem getJMenuItemArmor06() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_06.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_06);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eighth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 7
	 */
	private JMenuItem getJMenuItemArmor07() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_07.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_07);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the ninth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 8
	 */
	private JMenuItem getJMenuItemArmor08() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_08.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_08);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the tenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 9
	 */
	private JMenuItem getJMenuItemArmor09() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_09.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_09);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eleventh choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 10
	 */
	private JMenuItem getJMenuItemArmor10() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_10.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_10);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twelfth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 11
	 */
	private JMenuItem getJMenuItemArmor11() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_11.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_11);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the thirteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 12
	 */
	private JMenuItem getJMenuItemArmor12() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_12.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_12);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fourteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 13
	 */
	private JMenuItem getJMenuItemArmor13() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_13.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_13);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the fifteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 14
	 */
	private JMenuItem getJMenuItemArmor14() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_14.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_14);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the sixteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 15
	 */
	private JMenuItem getJMenuItemArmor15() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_15.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_15);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the seventeenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 16
	 */
	private JMenuItem getJMenuItemArmor16() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_16.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_16);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the eighteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 17
	 */
	private JMenuItem getJMenuItemArmor17() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_17.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_17);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the nineteenth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 18
	 */
	private JMenuItem getJMenuItemArmor18() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_18.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_18);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twentieth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 19
	 */
	private JMenuItem getJMenuItemArmor19() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_19.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_19);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-first choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 20
	 */
	private JMenuItem getJMenuItemArmor20() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_20.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_20);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-second choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 21
	 */
	private JMenuItem getJMenuItemArmor21() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_21.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_21);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-third choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 22
	 */
	private JMenuItem getJMenuItemArmor22() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_22.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_22);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-fourth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 23
	 */
	private JMenuItem getJMenuItemArmor23() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_23.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_23);
			}
		});
		return menuItem;
	}

	/**
	 * Creates the twenty-fifth choice for the armor menu.
	 * 
	 * @return JMenuItem : armor -> armor 24
	 */
	private JMenuItem getJMenuItemArmor24() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(Armor.A_24.getItemName());
		menuItem.setEnabled(false);
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlrReadiedArmor(Armor.A_24);
			}
		});
		return menuItem;
	}

	/**
	 * Called every tick; sets the armor in the menu to enabled or disabled
	 * depending on whether or not the player has the armor.
	 * 
	 * @param available
	 *            The boolean array of available armor.
	 */
	public void updateArmorMenu(boolean[] available) {
		for (int i = 0; i < jArmorMenu.getItemCount(); i++) {
			if (available[i])
				jArmorMenu.getItem(i).setEnabled(true);
			else
				jArmorMenu.getItem(i).setEnabled(false);
		}
		int type = controller.getPlrReadiedArmor().getType();
		for (int i = 0; i < jArmorMenu.getItemCount(); i++) {
			jArmorMenu.getItem(i).setForeground(Color.black);
		}
		jArmorMenu.getItem(type).setForeground(Color.red);
	}

	private JMenu getJHelpMenu() {
		JMenu jHelpMenu = new JMenu();
		jHelpMenu.setText("Help");
		jHelpMenu.add(getJMenuItemAbout());
		jHelpMenu.add(getJMenuItemAboutScenario());
		return jHelpMenu;
	}

	private JMenuItem getJMenuItemAbout() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("About NitsLoch");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new AboutFrame();
			}
		});
		return menuItem;
	}

	private JMenuItem getJMenuItemAboutScenario() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("About Scenario");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String title, message;
				title = controller.getScenarioTitle();
				message = controller.getScenarioMessage();

				if(title.equals("")) return;

				src.enums.Sounds.SCENARIO_INFO.playSound();

				JOptionPane.showMessageDialog(
						null,
						message,
						title, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		return menuItem;
	}

	/**
	 * Used to access this class from anonymous classes.
	 * 
	 * @return GameFrame : this pointer
	 */
	protected GameFrame getThis() {
		return this;
	}

	/**
	 * Sets up the content pane with a JPanel. This is where the GUI components
	 * are created.
	 * 
	 * @return JPanel : the panel
	 */
	private JPanel getPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBackground(Color.WHITE);

			/* Buttons */
			jContentPane.add(getJButtonUse());
			jContentPane.add(getJButtonShoot());
			jContentPane.add(getJButtonPass());

			/* Text area */
			jContentPane.add(getJTextArea());

			/* Labels */
			jContentPane.add(getJLabelPlayerName());
			jContentPane.add(getJLabelPlayerHitPoints());
			jContentPane.add(getJLabelPlayerFightAbil());
			jContentPane.add(getJLabelPlayerMarksAbil());
			jContentPane.add(getJLabelPlayerMartArtsAbil());
			jContentPane.add(getJLabelPlayerThievingAbil());
			jContentPane.add(getJLabelPlayerInventory());
			jContentPane.add(getJLabelPlayerMoney());
			jContentPane.add(getJLabelPlayerGrenades());
			jContentPane.add(getJLabelPlayerDynamite());
			jContentPane.add(getJLabelPlayerBandaids());
			jContentPane.add(getJLabelPlayerBullets());
			jContentPane.add(getJLabelPlayerRockets());
			jContentPane.add(getJLabelPlayerFlamePacks());
			jContentPane.add(getJLabelPlayerLaddersUp());
			jContentPane.add(getJLabelPlayerLaddersDown());
			jContentPane.add(getJLabelPlayerMapViewers());
			jContentPane.add(getJLabelPlayerExports());
			
			jContentPane.add(getCheckBoxAutoBandaid());

			super.addNotify();
		}
		return jContentPane;
	}

	/* ***************** Start Buttons ******************* */

	/**
	 * Creates the Pass button.
	 * 
	 * @return JButton : pass button
	 */
	private JButton getJButtonPass() {
		jButtonPass = new JButton();
		jButtonPass.setSize(60, 50);
		jButtonPass.setBounds(new Rectangle(5, 405, 150, 30));
		jButtonPass.setMargin(new Insets(0, 0, 0, 0));
		jButtonPass.setText("Pass");
		jButtonPass.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.playerPass();
				jContentPane.grabFocus();
			}
		});
		return jButtonPass;
	}

	/**
	 * Creates the South East button.
	 * 
	 * @return JButton : use button.
	 */
	private JButton getJButtonUse() {
		jButtonUse = new JButton();
		jButtonUse.setSize(60, 50);
		jButtonUse.setBounds(new Rectangle(5, 445, 150, 30));
		jButtonUse.setMargin(new Insets(0, 0, 0, 0));
		jButtonUse.setText("Use");
		jButtonUse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new UseFrame(controller.getLocalPlayer(), controller, getThis());
			}
		});
		return jButtonUse;
	}

	/**
	 * Creates the Shoot button.
	 * 
	 * @return JButton : shoot button
	 */
	private JButton getJButtonShoot() {
		jButtonShoot = new JButton();
		jButtonShoot.setSize(60, 50);
		jButtonShoot.setBounds(new Rectangle(5, 485, 150, 30));
		jButtonShoot.setMargin(new Insets(0, 0, 0, 0));
		jButtonShoot.setText("Shoot");
		jButtonShoot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				controller.setPlayerShooting(true);
				jContentPane.grabFocus();
			}
		});
		return jButtonShoot;
	}

	/* ********************* Text Area ******************* */

	/**
	 * Creates the text area for messages.
	 */
	private JTextArea getJTextArea() {
		messages = new JTextArea();
		messages.setEditable(false);
		messages.setBounds(new Rectangle(GX_MIN, 5, GWIDTH - 6, 96));
		messages.setBorder(BorderFactory.createLineBorder(Color.black));
		return messages;
	}

	/* ******************* Labels *********************** */

	/**
	 * Creates the label for the player name.
	 * 
	 * @return JLabel : player name label
	 */
	private JLabel getJLabelPlayerName() {
		jLabelPlayerName = new JLabel();
		jLabelPlayerName.setBounds(new Rectangle(5, 0, 200, 20));
		jLabelPlayerName.setText("Player Name: " + controller.getPlayerName());
		return jLabelPlayerName;
	}

	/**
	 * Creates the label for the player hit points.
	 * 
	 * @return JLabel : hit points label
	 */
	private JLabel getJLabelPlayerHitPoints() {
		jLabelPlayerHitPoints = new JLabel();
		jLabelPlayerHitPoints.setBounds(new Rectangle(5, 20, 200, 20));
		jLabelPlayerHitPoints.setText("Hit Points: "
				+ controller.getPlayerHitPoints() + " / "
				+ controller.getPlayerMaxHitPoints());
		return jLabelPlayerHitPoints;
	}

	/**
	 * Creates the label for the player fighting ability.
	 * 
	 * @return Jlabel : fighting ability label
	 */
	private JLabel getJLabelPlayerFightAbil() {
		jLabelPlayerFightAbil = new JLabel();
		jLabelPlayerFightAbil.setBounds(new Rectangle(5, 40, 200, 20));
		jLabelPlayerFightAbil.setText("Fighting Ability: "
				+ controller.getPlayerFightingAbility());
		return jLabelPlayerFightAbil;
	}

	/**
	 * Creates the label for the player marksmanship ability.
	 * 
	 * @return JLabel : marksmanship ability label
	 */
	private JLabel getJLabelPlayerMarksAbil() {
		jLabelPlayerMarksAbil = new JLabel();
		jLabelPlayerMarksAbil.setBounds(new Rectangle(5, 60, 200, 20));
		jLabelPlayerMarksAbil.setText("Marksmanship Ability: "
				+ controller.getPlayerMarksmanAbility());
		return jLabelPlayerMarksAbil;
	}

	/**
	 * Creates the label for the player martial arts ability.
	 * 
	 * @return JLabel : martial arts ability label
	 */
	private JLabel getJLabelPlayerMartArtsAbil() {
		jLabelPlayerMartArtsAbil = new JLabel();
		jLabelPlayerMartArtsAbil.setBounds(new Rectangle(5, 80, 200, 20));
		jLabelPlayerMartArtsAbil.setText("Martial Arts Ability: "
				+ controller.getPlayerMartialArtsAbility());
		return jLabelPlayerMartArtsAbil;
	}

	/**
	 * Creates the label for the player thieving ability.
	 * 
	 * @return JLabel : thieving ability label
	 */
	private JLabel getJLabelPlayerThievingAbil() {
		jLabelPlayerThievingAbil = new JLabel();
		jLabelPlayerThievingAbil.setBounds(new Rectangle(5, 100, 200, 20));
		jLabelPlayerThievingAbil.setText("Thieving Ability: "
				+ controller.getPlayerThievingAbility());
		return jLabelPlayerThievingAbil;
	}

	/* ******************* Inventory ****************** */

	/**
	 * Creates the label for the player inventory.
	 * 
	 * @return JLabel : inventory label
	 */
	private JLabel getJLabelPlayerInventory() {
		jLabelPlayerInventory = new JLabel();
		jLabelPlayerInventory.setBounds(new Rectangle(20, 120, 200, 20));
		jLabelPlayerInventory.setForeground(Color.BLUE);
		jLabelPlayerInventory.setText("-------Inventory-------");
		return jLabelPlayerInventory;
	}

	/**
	 * Creates the label for the player money.
	 * 
	 * @return JLabel : inventory, money label
	 */
	private JLabel getJLabelPlayerMoney() {
		jLabelPlayerMoney = new JLabel();
		jLabelPlayerMoney.setBounds(new Rectangle(5, 140, 120, 20));
		jLabelPlayerMoney.setText("Money: " + controller.getPlayerMoney());
		return jLabelPlayerMoney;
	}

	/**
	 * Creates the label for the player's grenades.
	 * 
	 * @return JLabel : inventory, grenades label
	 */
	private JLabel getJLabelPlayerGrenades() {
		jLabelPlayerGrenades = new JLabel();
		jLabelPlayerGrenades.setBounds(new Rectangle(5, 160, 100, 20));
		jLabelPlayerGrenades.setText("Grenades: "
				+ controller.getPlayerNumGrenades());
		return jLabelPlayerGrenades;
	}

	/**
	 * Creates the label for the player's Dynamite.
	 * 
	 * @return JLabel : inventory, dynamite label
	 */
	private JLabel getJLabelPlayerDynamite() {
		jLabelPlayerDynamite = new JLabel();
		jLabelPlayerDynamite.setBounds(new Rectangle(5, 180, 100, 20));
		jLabelPlayerDynamite.setText("Dynamite: "
				+ controller.getPlayerNumDynamite());
		return jLabelPlayerDynamite;
	}

	/**
	 * Creates the label for the player's bandaids.
	 * 
	 * @return JLabel : inventory, bandaids label
	 */
	private JLabel getJLabelPlayerBandaids() {
		jLabelPlayerBandaids = new JLabel();
		jLabelPlayerBandaids.setBounds(new Rectangle(5, 200, 100, 20));
		jLabelPlayerBandaids.setText("Bandaids: "
				+ controller.getPlayerNumBandaids());
		return jLabelPlayerBandaids;
	}

	/**
	 * Creates the label for the player's bullets.
	 * 
	 * @return JLabel : inventory, bullets label
	 */
	private JLabel getJLabelPlayerBullets() {
		jLabelPlayerBullets = new JLabel();
		jLabelPlayerBullets.setBounds(new Rectangle(5, 220, 100, 20));
		jLabelPlayerBullets.setText("Bullets: "
				+ controller.getPlayerNumBullets());
		return jLabelPlayerBullets;
	}

	/**
	 * Creates the label for the player's Rockets.
	 * 
	 * @return JLabel : inventory, rockets label
	 */
	private JLabel getJLabelPlayerRockets() {
		jLabelPlayerRockets = new JLabel();
		jLabelPlayerRockets.setBounds(new Rectangle(5, 240, 100, 20));
		jLabelPlayerRockets.setText("Rockets: "
				+ controller.getPlayerNumRockets());
		return jLabelPlayerRockets;
	}

	/**
	 * Creates the label for the player's flame packs.
	 * 
	 * @return JLabel : inventory, flame packs label
	 */
	private JLabel getJLabelPlayerFlamePacks() {
		jLabelPlayerFlamePacks = new JLabel();
		jLabelPlayerFlamePacks.setBounds(new Rectangle(5, 260, 120, 20));
		jLabelPlayerFlamePacks.setText("Flame Packs: "
				+ controller.getPlayerNumFlamePacks());
		return jLabelPlayerFlamePacks;
	}

	/**
	 * Creates the label for the player's ladders up.
	 * 
	 * @return JLabel : inventory, ladders up label
	 */
	private JLabel getJLabelPlayerLaddersUp() {
		jLabelPlayerLaddersUp = new JLabel();
		jLabelPlayerLaddersUp.setBounds(new Rectangle(5, 280, 150, 20));
		jLabelPlayerLaddersUp.setText("Up Ladder Spells: "
				+ controller.getPlayerNumLaddersUp());
		return jLabelPlayerLaddersUp;
	}

	/**
	 * Creates the label for the player's ladders down.
	 * 
	 * @return JLabel : inventory, ladders down label
	 */
	private JLabel getJLabelPlayerLaddersDown() {
		jLabelPlayerLaddersDown = new JLabel();
		jLabelPlayerLaddersDown.setBounds(new Rectangle(5, 300, 150, 20));
		jLabelPlayerLaddersDown.setText("Down Ladder Spells: "
				+ controller.getPlayerNumLaddersDown());
		return jLabelPlayerLaddersDown;
	}

	/**
	 * Creates the label for the player's map viewers.
	 * 
	 * @return JLabel : inventory, map viewers label
	 */
	private JLabel getJLabelPlayerMapViewers() {
		jLabelPlayerMapViewers = new JLabel();
		jLabelPlayerMapViewers.setBounds(new Rectangle(5, 320, 150, 20));
		jLabelPlayerMapViewers.setText("Map Viewers: "
				+ controller.getPlayerNumMapViewers());
		return jLabelPlayerMapViewers;
	}
	
	/**
	 * Creates the label for the player's exports.
	 * 
	 * @return JLabel : inventory, exports label
	 */
	private JLabel getJLabelPlayerExports() {
		jLabelPlayerExports = new JLabel();
		jLabelPlayerExports.setBounds(new Rectangle(5, 340, 150, 20));
		jLabelPlayerExports.setText("Exports: "
				+ controller.getPlayerNumExports());
		return jLabelPlayerExports;
	}
	
	/* ****** Options ****** */
	private JCheckBox getCheckBoxAutoBandaid() {
		chkAutoBandaid = new JCheckBox();
		chkAutoBandaid.setBounds(new Rectangle(5, 375, 150, 20));
		chkAutoBandaid.setText("Auto-Bandaid");
		chkAutoBandaid.setBackground(Color.white);
		chkAutoBandaid.setSelected(true);
		chkAutoBandaid.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jContentPane.grabFocus();
			}
		});
		return chkAutoBandaid;
	}

	/**
	 * Tells the component to listen for keystrokes for movement.
	 * 
	 * @param comp
	 *            The component you want to add the listener to.
	 */
	public void readyForInput(Component comp) {
		comp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// Make sure dungeon screen is blanked out when moving.
				if (controller.getIsInDungeon())
					dungeonBlankScreen();
				// Halt drawing when moving, or get weird graphic glitches.
				controller.haltDrawing();
				int keyCode = e.getKeyCode();
				if (e.getKeyCode() == KeyEvent.VK_Q && e.isAltDown())
					System.exit(0);

				// Attempt to force redraw, in case it gets stuck not drawing.
				else if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())
					controller.resumeDrawing();

				else if	(e.getKeyCode() == KeyEvent.VK_K && e.isControlDown())
					controller.playerUpSpell();

				else if	(e.getKeyCode() == KeyEvent.VK_D && e.isControlDown())
					controller.playerDownSpell();

				if (controller.getIsInDungeon()) {
					//dungeonBlankScreen();

					switch (keyCode) {
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_NUMPAD6:
						controller.turnPlayer(Facing.RIGHT);
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_NUMPAD4:
						controller.turnPlayer(Facing.LEFT);
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_NUMPAD2:
						controller.turnPlayerAround();
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_NUMPAD8:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						controller.movePlayerForward();
						break;
					case KeyEvent.VK_NUMPAD5:
					case KeyEvent.VK_SPACE:
						controller.playerPass();
						break;
					case KeyEvent.VK_M:
						dungeonBlankScreen();
						
						if (controller.getPlayerViewingMap())
							controller.setPlayerViewingMap(false);
						else{
							if(controller.playerHasAMapViewer()){
								controller.removeMapViewer();
								controller.setPlayerViewingMap(true);
							}
							else {
								Messages.getInstance().addMessage("You are out of map viewers.");
							}				
						}
						break;
					default:
						break;
					}

				} else { // Not in dungeon

					switch (keyCode) {
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_NUMPAD6:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.EAST);
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_NUMPAD4:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.WEST);
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_NUMPAD2:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.SOUTH);
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_NUMPAD8:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.NORTH);
						break;
					case KeyEvent.VK_NUMPAD9:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.NORTHEAST);
						break;
					case KeyEvent.VK_NUMPAD7:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.NORTHWEST);
						break;
					case KeyEvent.VK_NUMPAD3:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.SOUTHEAST);
						break;
					case KeyEvent.VK_NUMPAD1:
						if(e.isControlDown()) controller.setPlayerShooting(true);
						movePlayer(Direction.SOUTHWEST);
						break;
					case KeyEvent.VK_NUMPAD5:
					case KeyEvent.VK_SPACE:
						// justMoved = true;
						controller.playerPass();
						break;
					case KeyEvent.VK_M:
						blankScreen();
						if (controller.getPlayerViewingMap()){
							controller.setPlayerViewingMap(false);
							justMoved = true;
						}
						else{
							if(controller.playerHasAMapViewer()){
								controller.removeMapViewer();
								controller.setPlayerViewingMap(true);
							}
							else {
								Messages.getInstance().addMessage("You are out of map viewers.");
							}
						}
						justMoved = true;
						break;
					default:
						break;
					}

				}
				controller.resumeDrawing(); // Done moving, so draw again.
			}
		});
	}

	/**
	 * Moves the player and sets the justMoved variable.
	 * 
	 * @param dir
	 *            Direction : direction to move
	 */
	private void movePlayer(Direction dir) {
		controller.movePlayer(dir);
	}

	/**
	 * Sets the player's readied weapon.
	 * @param w Weapon : weapon to ready
	 */
	private void setPlrReadiedWeapon(Weapon w){
		justMoved = true; // Clear the background image of the player.
		controller.setPlrReadiedWeapon(w);
	}

	/**
	 * Checks to see if the player clicked on the game window to move the
	 * player. If the click location is insinde a click-movement polygon, the
	 * player will move in that direction. If the player is not told to move, he
	 * will pass.
	 * 
	 * @param x
	 *            The X coordinate of the mouse click.
	 * @param y
	 *            The Y coordinate of the mouse click.
	 */
	private void testPress(int x, int y) {
		if (controller.getGameRunning() && !controller.getIsInDungeon()) {
			controller.haltDrawing(); // Don't draw when moving.
			if (NW_POLY.contains(x, y))
				movePlayer(Direction.NORTHWEST);
			else if (N_POLY.contains(x, y))
				movePlayer(Direction.NORTH);
			else if (NE_POLY.contains(x, y))
				movePlayer(Direction.NORTHEAST);
			else if (E_POLY.contains(x, y))
				movePlayer(Direction.EAST);
			else if (SE_POLY.contains(x, y))
				movePlayer(Direction.SOUTHEAST);
			else if (S_POLY.contains(x, y))
				movePlayer(Direction.SOUTH);
			else if (SW_POLY.contains(x, y))
				movePlayer(Direction.SOUTHWEST);
			else if (W_POLY.contains(x, y))
				movePlayer(Direction.WEST);
			else {
				controller.playerPass();
				controller.setPlayerShooting(false);
			}
			controller.resumeDrawing();
		}
	}

	/**
	 * Updates the labels on the frame so they show updated information.
	 * 
	 */
	private void updateLabelInfo() {
		if(!this.getTitle().equals("NitsLoch - " + controller.getCurrentCityName()))
			this.setTitle("NitsLoch - " + controller.getCurrentCityName());

		jLabelPlayerName.setText(controller.getPlayerName());
		jLabelPlayerHitPoints.setText("Hit Points: "
				+ controller.getPlayerHitPoints() + " / "
				+ controller.getPlayerMaxHitPoints());
		jLabelPlayerFightAbil.setText("Fighting Ability: "
				+ controller.getPlayerFightingAbility());
		jLabelPlayerMarksAbil.setText("Marksmanship Ability: "
				+ controller.getPlayerMarksmanAbility());
		jLabelPlayerMartArtsAbil.setText("Martial Arts Ability: "
				+ controller.getPlayerMartialArtsAbility());
		jLabelPlayerThievingAbil.setText("Thieving Ability: "
				+ controller.getPlayerThievingAbility());
		jLabelPlayerMoney.setText("Money: " + controller.getPlayerMoney());
		jLabelPlayerGrenades.setText("Grenades: "
				+ controller.getPlayerNumGrenades());
		jLabelPlayerDynamite.setText("Dynamite: "
				+ controller.getPlayerNumDynamite());
		jLabelPlayerBandaids.setText("Bandaids: "
				+ controller.getPlayerNumBandaids());
		jLabelPlayerBullets.setText("Bullets: "
				+ controller.getPlayerNumBullets());
		jLabelPlayerRockets.setText("Rockets: "
				+ controller.getPlayerNumRockets());
		jLabelPlayerFlamePacks.setText("Flame Packs: "
				+ controller.getPlayerNumFlamePacks());
		jLabelPlayerLaddersUp.setText("Up Ladder Spells: "
				+ controller.getPlayerNumLaddersUp());
		jLabelPlayerLaddersDown.setText("Down Ladder Spells: "
				+ controller.getPlayerNumLaddersDown());
		jLabelPlayerMapViewers.setText("Map Viewers: "
				+ controller.getPlayerNumMapViewers());
		jLabelPlayerExports.setText("Exports: "
				+ controller.getPlayerNumExports());
	}

	/**
	 * Draws the message box window and sets the text.
	 * 
	 * @param g
	 *            Graphics : graphics of the frame.
	 */
	private void drawMessageBox(Graphics g) {
//		g.setColor(Color.black);
//		g.drawRect(GX_MIN, 47, GWIDTH, 100);

		messages.setText(Messages.getInstance().getMessages());
	}

	/**
	 * Sets the just moved variable.  If true, this will tell the
	 * game to draw only only the land and ignore the objects for
	 * the next tick.
	 * @param bool boolean : justMoved
	 */
	public void setJustMoved(boolean bool){
		justMoved = bool;
	}

	/**
	 * Draws the updated land without any objects. This will allow enemies with
	 * transparent backgrounds to have the correct image behind them.
	 * 
	 * @param worldView
	 *            Land[][] : the visible land
	 */
	private void drawNoObjects(Land[][] worldView) {
		Graphics g = this.getGraphics();

		if (g == null)
			return;

		BufferedImage img = null;
		// Get the images that should be drawn.
		BufferedImage[][] bufImages = controller.getOutsideViewNoObjects();

		/*
		 * Draws the image obtained from the outside view renderer in each cell
		 * on the GUI window.
		 */
		for (int row = 0; row < src.Constants.WORLD_VIEW_SIZE; row++) {
			for (int col = 0; col < src.Constants.WORLD_VIEW_SIZE; col++) {
				img = bufImages[row][col];
				if (Constants.WORLD_VIEW_SIZE == 7) {
					if (img != null) {
						g.drawImage(img, GX_MIN + col
								* (GWIDTH / Constants.WORLD_VIEW_SIZE), GY_MIN
								+ row * (GHEIGHT / Constants.WORLD_VIEW_SIZE),
								null);
						continue;
					}
				} else { // Not drawn to scale
					if (img != null) {
						g.drawImage(img, GX_MIN + col
								* (GWIDTH / Constants.WORLD_VIEW_SIZE), GY_MIN
								+ row * (GHEIGHT / Constants.WORLD_VIEW_SIZE),
								GWIDTH / Constants.WORLD_VIEW_SIZE - 2, GHEIGHT
								/ Constants.WORLD_VIEW_SIZE - 2, null);
						continue;
					}
				}
			}
		}
	}

	/**
	 * Draws the Game Window.
	 * 
	 */
	public synchronized void drawGameWindow(Land[][] worldView) {
		if (justMoved) {
			drawNoObjects(worldView);
			justMoved = false;
		}

		Graphics g = this.getGraphics();

		if (g == null)
			return;

		drawMessageBox(g);
		updateLabelInfo();

		BufferedImage img = null;
		// Get the images that should be drawn.
		BufferedImage[][] bufImages = controller.getOutsideView();

		/*
		 * Draws the image obtained from the outside view renderer in each cell
		 * on the GUI window.
		 */
		for (int row = 0; row < src.Constants.WORLD_VIEW_SIZE; row++) {
			for (int col = 0; col < src.Constants.WORLD_VIEW_SIZE; col++) {
				img = bufImages[row][col];
				if (Constants.WORLD_VIEW_SIZE == 7) {
					if (img != null) {
						g.drawImage(img, GX_MIN + col
								* (GWIDTH / Constants.WORLD_VIEW_SIZE), GY_MIN
								+ row * (GHEIGHT / Constants.WORLD_VIEW_SIZE),
								null);
						continue;
					}
				} else { // Not drawn to scale
					if (img != null) {
						g.drawImage(img, GX_MIN + col
								* (GWIDTH / Constants.WORLD_VIEW_SIZE), GY_MIN
								+ row * (GHEIGHT / Constants.WORLD_VIEW_SIZE),
								GWIDTH / Constants.WORLD_VIEW_SIZE - 2, GHEIGHT
								/ Constants.WORLD_VIEW_SIZE - 2, null);
						continue;
					}
				}
			}
		}
	}

	/**
	 * Draws the overhead map in the main window.
	 * 
	 */
	public synchronized void drawMap() {
		Graphics g = this.getGraphics();

		drawMessageBox(g);

		g.setColor(Color.black);
		int rowCount = 0;
		int colCount = 0;
		Land[][] land = controller.getCurrentCity();
		for (int row = controller.getPlayerRow() - Constants.MAP_VIEW_SIZE / 2; row < controller
		.getPlayerRow() + Constants.MAP_VIEW_SIZE / 2; row++) {
			for (int col = controller.getPlayerCol() - Constants.MAP_VIEW_SIZE
					/ 2; col < controller.getPlayerCol()
					+ Constants.MAP_VIEW_SIZE / 2; col++) {
				try {
					if (land[row][col] == null)
						g.setColor(Color.black);
					else if (land[row][col] instanceof Obstruction)
						g.setColor(Color.darkGray);
					else if (land[row][col] instanceof Shop) {
						g.setColor(((Shop) land[row][col]).getType().getColor());
					} else if (land[row][col] instanceof Exit
							&& ((Exit) land[row][col]).getIsOpen()){
						if(controller.getIsInDungeon()){
							if(((Exit) land[row][col]).getType() == ExitType.LADDER_UP)
								g.setColor(Color.blue);
							else g.setColor(Color.red);
						}
						else g.setColor(Color.gray);
					}
					else if (land[row][col] instanceof Street
							&& ((Street) land[row][col]).hasPlayer() > -1)
						g.setColor(Color.green);
					else if(land[row][col].getEnemy() != null &&
							(land[row][col].getEnemy().isLeader())){
						double rand = Math.random();
						if(rand < .2) g.setColor(Color.blue);
						else if(rand < .4) g.setColor(Color.cyan);
						else if(rand < .6) g.setColor(Color.green);
						else if(rand < .8) g.setColor(Color.orange);
						else g.setColor(Color.red);
					}
					else
						g.setColor(Color.white);
					if (land[row][col] != null
							&& !land[row][col].hasBeenExplored())
						g.setColor(Color.black);
				} catch (ArrayIndexOutOfBoundsException ae) {
					g.setColor(Color.black);
				}
				g.fillRect(
						GX_MIN
						+ (int) (colCount * GWIDTH
								/ Constants.MAP_VIEW_SIZE * 1.0),
								GY_MIN
								+ (int) (rowCount * GHEIGHT
										/ Constants.MAP_VIEW_SIZE * 1.0),
										(int) (GWIDTH / (Constants.MAP_VIEW_SIZE * 1.0) + 1),
										(int) (GHEIGHT / (Constants.MAP_VIEW_SIZE * 1.0) + 1));
				colCount++;
			}
			rowCount++;
			colCount = 0;
		}
	}

	/**
	 * Draws the screen for the dungeon mode.
	 */
	public synchronized void drawDungeonMode() { // This method really sucks.
		updateLabelInfo();

		/*if (dirty) {
			dungeonBlankScreen();
			dirty = false;
		}*/

		Land[] land = controller.getVisibleDungeonArea();

		// Draw the screen

		Graphics g = this.getGraphics();
		if (g == null)
			return;

		drawMessageBox(g);

		g.setColor(Constants.DUNGEON_WALL_COLOR);

		/**
		 * How far you can see in front of you. Will be 1 if there is a wall or
		 * enemy directly in front of you and 4 if there is nothing blocking
		 * your view. It can be any value in between depending on at what point
		 * your view is blocked.
		 */
		int depth = 1;
		if (land[DungeonWalls.FRONT_WALL.getType()] == null
				|| !(land[DungeonWalls.FRONT_WALL.getType()] instanceof Street))
			depth = 1;
		else if (land[DungeonWalls.FRONT_WALL2.getType()] == null
				|| !(land[DungeonWalls.FRONT_WALL2.getType()] instanceof Street))
			depth = 2;
		else if (land[DungeonWalls.FRONT_WALL3.getType()] == null
				|| !(land[DungeonWalls.FRONT_WALL3.getType()] instanceof Street))
			depth = 3;
		else
			depth = 4;

		BufferedImage img;
		String image = "";

		// Draw enemy
		if (depth >= 1
				&& (land[DungeonWalls.FRONT_WALL.getType()].getEnemy() != null ||
						land[DungeonWalls.FRONT_WALL.getType()].getNPC() != null )) {
			try {
				if(land[DungeonWalls.FRONT_WALL.getType()].getEnemy() != null)
					image = land[DungeonWalls.FRONT_WALL.getType()].getEnemy().getImage();
				else image = land[DungeonWalls.FRONT_WALL.getType()].getNPC().getImage();

				img = Images.getInstance().getImage(image);
				g.drawImage(img, ((GX_MIN + GWIDTH + GX_MIN) / 2) - 28 * 4,
						((GY_MIN + GHEIGHT + GY_MIN) / 2) - 28 * 4, 57 * 4,
						57 * 4, null);

				if(land[DungeonWalls.FRONT_WALL.getType()].getEnemy() != null){
					// Update the hitCounter of the enemy if one exists.
					if (((Street) land[DungeonWalls.FRONT_WALL.getType()])
							.getEnemy().getHitCounter() > 1)
						((Street) land[DungeonWalls.FRONT_WALL.getType()])
						.getEnemy().decrementHitCounter();
					else if (((Street) land[DungeonWalls.FRONT_WALL.getType()])
							.getEnemy().getHitCounter() == 1) {
						((Street) land[DungeonWalls.FRONT_WALL.getType()])
						.getEnemy().setJustHit(false);
						((Street) land[DungeonWalls.FRONT_WALL.getType()])
						.getEnemy().decrementHitCounter();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot find picture: " + image);
			}
			depth = 1;
		} else if (depth >= 2
				&& (land[DungeonWalls.FRONT_WALL2.getType()].getEnemy() != null ||
						land[DungeonWalls.FRONT_WALL2.getType()].getNPC() != null)) {
			try {
				if(land[DungeonWalls.FRONT_WALL2.getType()].getEnemy() != null)
					image = land[DungeonWalls.FRONT_WALL2.getType()].getEnemy().getImage();
				else image = land[DungeonWalls.FRONT_WALL2.getType()].getNPC().getImage();
				img = Images.getInstance().getImage(image);
				//img = ImageIO.read(new File(image));
				g.drawImage(img, ((GX_MIN + GWIDTH + GX_MIN) / 2)
						- (int) (28 * 3.2), ((GY_MIN + GHEIGHT + GY_MIN) / 2)
						- (int) (28 * 3.2), (int) (57 * 3.2), (int) (57 * 3.2),
						null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot find picture: " + image);
			}
			depth = 2;
		} else if (depth >= 3 &&
				((land[DungeonWalls.FRONT_WALL3.getType()].getEnemy() != null) ||
						land[DungeonWalls.FRONT_WALL3.getType()].getNPC() != null)){
			try {
				if(land[DungeonWalls.FRONT_WALL3.getType()].getEnemy() != null)
					image = land[DungeonWalls.FRONT_WALL3.getType()].getEnemy().getImage();
				else image = land[DungeonWalls.FRONT_WALL3.getType()].getNPC().getImage();
				img = Images.getInstance().getImage(image);
				//img = ImageIO.read(new File(image));
				g.drawImage(img, ((GX_MIN + GWIDTH + GX_MIN) / 2)
						- (int) (28 * 1.7), ((GY_MIN + GHEIGHT + GY_MIN) / 2)
						- (int) (28 * 1.7), (int) (57 * 1.7), (int) (57 * 1.7),
						null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot find picture: " + image);
			}
			depth = 3;
		} else if (depth >= 4 &&
				((land[DungeonWalls.FRONT_WALL4.getType()].getEnemy() != null) ||
						land[DungeonWalls.FRONT_WALL4.getType()].getNPC() != null)){
			try {
				if(land[DungeonWalls.FRONT_WALL4.getType()].getEnemy() != null)
					image = land[DungeonWalls.FRONT_WALL4.getType()].getEnemy().getImage();
				else image = land[DungeonWalls.FRONT_WALL4.getType()].getNPC().getImage();
				img = Images.getInstance().getImage(image);
				//img = ImageIO.read(new File(image));
				g.drawImage(img, ((GX_MIN + GWIDTH + GX_MIN) / 2) - 28,
						((GY_MIN + GHEIGHT + GY_MIN) / 2) - 28, 57, 57, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot find picture: " + image);
			}
			depth = 4;
		}

		// X and Y coords for walls
		int leftX = 0, rightX = 0, topY = 0, bottomY = 0;

		// X and Y coords for ladder openings.
		int oLeftUpX, oRightUpX, oTopY, oLeftDownX, oRightDownX, oBottomY;
		oLeftUpX = oRightUpX = oTopY = oLeftDownX = oRightDownX = oBottomY = 0;

		// X and Y coords for ladders themselves.
		int lLeftX, lRightX, lTopY, lBottomY, lMid1Y, lMid2Y, lMid3Y, lMid4Y;
		lLeftX = lRightX = lTopY = lBottomY = lMid1Y = lMid2Y = lMid3Y = lMid4Y = 0;

		FrontWallArrangement arrangementValue;

		// Draw the front wall.
		switch (depth) {
		case 4:
			leftX = GX_MIN + (GX_MIN / 2 + 45);
			rightX = GX_MIN + (GX_MIN / 2 + 45) + 65;
			topY = GY_MIN + (GY_MIN / 2 + 90);
			bottomY = GY_MIN + (GY_MIN / 2 + 90) + 70;

			// Top of opening hole
			oLeftUpX = GX_MIN + (GX_MIN / 2 + 58);
			oRightUpX = GX_MIN + (GX_MIN / 2 + 58) + 40;
			oTopY = GY_MIN + (GY_MIN / 2 + 90);

			// Bottom of opening hole
			oLeftDownX = GX_MIN + (GX_MIN / 2 + 63);
			oRightDownX = GX_MIN + (GX_MIN / 2 + 63) + 30;
			oBottomY = GY_MIN + (GY_MIN / 2 + 95);

			// Ladder
			lLeftX = GX_MIN + (GX_MIN / 2 + 70);
			lRightX = GX_MIN + (GX_MIN / 2 + 70) + 17;
			lTopY = GY_MIN + (GY_MIN / 2 + 90);
			lBottomY = GY_MIN + (GY_MIN / 2 + 90) + 70;
			lMid1Y = GY_MIN + (GY_MIN / 2 + 90) + 15;
			lMid2Y = GY_MIN + (GY_MIN / 2 + 90) + 30;
			lMid3Y = GY_MIN + (GY_MIN / 2 + 90) + 45;
			lMid4Y = GY_MIN + (GY_MIN / 2 + 90) + 60;

			break;
		case 3:
			leftX = GX_MIN + (GX_MIN / 2 + 23);
			rightX = GX_MIN + (GX_MIN / 2 + 23) + 110;
			topY = GY_MIN + (GY_MIN / 2 + 75);
			bottomY = GY_MIN + (GY_MIN / 2 + 75) + 100;

			// Top of opening hole
			oLeftUpX = GX_MIN + (GX_MIN / 2 + 40);
			oRightUpX = GX_MIN + (GX_MIN / 2 + 40) + 75;
			oTopY = GY_MIN + (GY_MIN / 2 + 75);

			// Bottom of opening hole
			oLeftDownX = GX_MIN + (GX_MIN / 2 + 50);
			oRightDownX = GX_MIN + (GX_MIN / 2 + 50) + 55;
			oBottomY = GY_MIN + (GY_MIN / 2 + 75) + 10;

			// Ladder
			lLeftX = GX_MIN + (GX_MIN / 2 + 64);
			lRightX = GX_MIN + (GX_MIN / 2 + 64) + 28;
			lTopY = GY_MIN + (GY_MIN / 2 + 75);
			lBottomY = GY_MIN + (GY_MIN / 2 + 75) + 100;
			lMid1Y = GY_MIN + (GY_MIN / 2 + 75) + 20;
			lMid2Y = GY_MIN + (GY_MIN / 2 + 75) + 40;
			lMid3Y = GY_MIN + (GY_MIN / 2 + 75) + 60;
			lMid4Y = GY_MIN + (GY_MIN / 2 + 75) + 80;

			break;
		case 2:
			leftX = GX_MIN + (GX_MIN / 2 - 20) - 1;
			rightX = GX_MIN + (GX_MIN / 2 - 20) + 197;
			topY = GY_MIN + (GY_MIN / 2 + 45);
			bottomY = GY_MIN + (GY_MIN / 2 + 45) + 160;

			// Top of opening hole
			oLeftUpX = GX_MIN + (GX_MIN / 2 + 15);
			oRightUpX = GX_MIN + (GX_MIN / 2 + 15) + 125;
			oTopY = GY_MIN + (GY_MIN / 2 + 45);

			// Bottom of opening hole
			oLeftDownX = GX_MIN + (GX_MIN / 2 + 35);
			oRightDownX = GX_MIN + (GX_MIN / 2 + 35) + 85;
			oBottomY = GY_MIN + (GY_MIN / 2 + 45) + 20;

			// Ladder
			lLeftX = GX_MIN + (GX_MIN / 2 + 55);
			lRightX = GX_MIN + (GX_MIN / 2 + 55) + 45;
			lTopY = GY_MIN + (GY_MIN / 2 + 45);
			lBottomY = GY_MIN + (GY_MIN / 2 + 45) + 160;
			lMid1Y = GY_MIN + (GY_MIN / 2 + 45) + 35;
			lMid2Y = GY_MIN + (GY_MIN / 2 + 45) + 70;
			lMid3Y = GY_MIN + (GY_MIN / 2 + 45) + 105;
			lMid4Y = GY_MIN + (GY_MIN / 2 + 45) + 140;

			break;
		case 1:
			leftX = GX_MIN + (GX_MIN / 2 - 90);
			rightX = GX_MIN + (GX_MIN / 2 - 90) + 335;
			topY = GY_MIN + (GY_MIN / 2 - 3);
			bottomY = GY_MIN + (GY_MIN / 2 - 3) + 255;

			// Top of opening hole
			oLeftUpX = GX_MIN + (GX_MIN / 2 - 30);
			oRightUpX = GX_MIN + (GX_MIN / 2 - 30) + 210;
			oTopY = GY_MIN + (GY_MIN / 2 - 3);

			// Bottom of opening hole
			oLeftDownX = GX_MIN + (GX_MIN / 2 + 5);
			oRightDownX = GX_MIN + (GX_MIN / 2 + 5) + 140;
			oBottomY = GY_MIN + (GY_MIN / 2 - 3) + 35;

			// Ladder
			lLeftX = GX_MIN + (GX_MIN / 2 + 40);
			lRightX = GX_MIN + (GX_MIN / 2 + 40) + 70;
			lTopY = GY_MIN + (GY_MIN / 2 - 3);
			lBottomY = GY_MIN + (GY_MIN / 2 - 3) + 255;
			lMid1Y = GY_MIN + (GY_MIN / 2 - 3) + 55;
			lMid2Y = GY_MIN + (GY_MIN / 2 - 3) + 110;
			lMid3Y = GY_MIN + (GY_MIN / 2 - 3) + 165;
			lMid4Y = GY_MIN + (GY_MIN / 2 - 3) + 220;

			break;
		}

		arrangementValue = checkFrontWall(depth, land);

		if (arrangementValue == FrontWallArrangement.WALL_BOTH) { // Left/Right is wall.
			if (depth == 4)
				g.drawRect(leftX, topY, 65, 70);
			else if (depth == 3)
				g.drawRect(leftX, topY, 110, 100);
			else if (depth == 2)
				g.drawRect(leftX, topY, 196, 160);
			else
				g.drawRect(leftX, topY, 335, 255);
		}

		else if (arrangementValue == FrontWallArrangement.WALL_LEFT) { // Left is wall.
			g.drawLine(leftX, topY, rightX, topY);
			g.drawLine(leftX, topY, leftX, bottomY);
			g.drawLine(leftX, bottomY, rightX, bottomY);
		}

		else if (arrangementValue == FrontWallArrangement.WALL_RIGHT) { // Right is wall.
			g.drawLine(leftX, topY, rightX, topY);
			g.drawLine(rightX, topY, rightX, bottomY);
			g.drawLine(leftX, bottomY, rightX, bottomY);
		}

		else if (arrangementValue == FrontWallArrangement.WALL_NONE) { // Left/right is not a wall.
			g.drawLine(leftX, topY, rightX, topY);
			g.drawLine(leftX, bottomY, rightX, bottomY);
		}
		else if (arrangementValue == FrontWallArrangement.LADDER_UP_BOTH
				|| arrangementValue == FrontWallArrangement.LADDER_UP_NONE
				|| arrangementValue == FrontWallArrangement.LADDER_UP_LEFT
				|| arrangementValue == FrontWallArrangement.LADDER_UP_RIGHT) {
			g.drawLine(leftX, topY, leftX, bottomY);
			g.drawLine(rightX, topY, rightX, bottomY);

			g.setColor(Color.gray);

			// Opening
			g.drawLine(oLeftUpX, oTopY, oRightUpX, oTopY);
			g.drawLine(oLeftDownX, oBottomY, oRightDownX, oBottomY);
			g.drawLine(oLeftUpX, oTopY, oLeftDownX, oBottomY);
			g.drawLine(oRightUpX, oTopY, oRightDownX, oBottomY);

			// Ladder
			g.setColor(new Color(128, 64, 0));
			g.drawLine(lLeftX, lTopY, lLeftX, lBottomY);
			g.drawLine(lRightX, lTopY, lRightX, lBottomY);
			g.drawLine(lLeftX, lMid1Y, lRightX, lMid1Y);
			g.drawLine(lLeftX, lMid2Y, lRightX, lMid2Y);
			g.drawLine(lLeftX, lMid3Y, lRightX, lMid3Y);
			g.drawLine(lLeftX, lMid4Y, lRightX, lMid4Y);

			g.setColor(Constants.DUNGEON_WALL_COLOR);
		}

		else if (arrangementValue == FrontWallArrangement.LADDER_DOWN_BOTH
				|| arrangementValue == FrontWallArrangement.LADDER_DOWN_NONE
				|| arrangementValue == FrontWallArrangement.LADDER_DOWN_LEFT
				|| arrangementValue == FrontWallArrangement.LADDER_DOWN_RIGHT) {
			g.drawLine(leftX, topY, leftX, bottomY);
			g.drawLine(rightX, topY, rightX, bottomY);

			g.setColor(Color.gray);

			// Opening

			oTopY = (GY_MIN + GHEIGHT) - (oTopY - GY_MIN);
			oBottomY = (GY_MIN + GHEIGHT) - (oBottomY - GY_MIN);

			g.drawLine(oLeftUpX, oTopY, oRightUpX, oTopY);
			g.drawLine(oLeftDownX, oBottomY, oRightDownX, oBottomY);
			g.drawLine(oLeftUpX, oTopY, oLeftDownX, oBottomY);
			g.drawLine(oRightUpX, oTopY, oRightDownX, oBottomY);

			// Ladder
			g.setColor(new Color(128, 64, 0));
			g.drawLine(lLeftX, lTopY, lLeftX, lBottomY);
			g.drawLine(lRightX, lTopY, lRightX, lBottomY);
			g.drawLine(lLeftX, lMid1Y, lRightX, lMid1Y);
			g.drawLine(lLeftX, lMid2Y, lRightX, lMid2Y);
			g.drawLine(lLeftX, lMid3Y, lRightX, lMid3Y);
			g.drawLine(lLeftX, lMid4Y, lRightX, lMid4Y);

			g.setColor(Constants.DUNGEON_WALL_COLOR);
		}

		/* *********************************************
		 * Walls
		 ***********************************************/

		// Draw the left walls.
		if (depth >= 4 && land[DungeonWalls.LEFT_WALL4.getType()] != null
				&& (land[DungeonWalls.LEFT_WALL4.getType()] instanceof Street
						|| land[DungeonWalls.LEFT_WALL4.getType()] instanceof Exit)) {
			g.drawLine(leftX - 22, topY, leftX, topY);
			g.drawLine(leftX - 22, bottomY, leftX, bottomY);
			g.drawLine(leftX - 22, topY - 15, leftX - 22, bottomY + 15);

			if (arrangementValue == FrontWallArrangement.NO_FRONT_WALL)
				g.drawLine(leftX, topY, leftX, bottomY);
		}

		else if (depth >= 4
				&& (land[DungeonWalls.LEFT_WALL4.getType()] == null || 
						land[DungeonWalls.LEFT_WALL4.getType()] instanceof Obstruction)) {
			g.drawLine(leftX - 22, topY - 15, leftX, topY);
			g.drawLine(leftX - 22, bottomY + 15, leftX, bottomY);
		}

		if (depth >= 3 && land[DungeonWalls.LEFT_WALL3.getType()] != null
				&& (land[DungeonWalls.LEFT_WALL3.getType()] instanceof Street
						|| land[DungeonWalls.LEFT_WALL3.getType()] instanceof Exit)) {
			g.drawLine(345, 300, 390, 300);
			g.drawLine(345, 400, 390, 400);
			g.drawLine(345, 270, 345, 430);

			if (depth >= 4)
				g.drawLine(390, 300, 390, 400);
		}

		else if (depth >= 3
				&& (land[DungeonWalls.LEFT_WALL3.getType()] == null || 
						land[DungeonWalls.LEFT_WALL3.getType()] instanceof Obstruction)) {
			g.drawLine(345, 270, 390, 300);
			g.drawLine(345, 430, 390, 400);
		}

		if (depth >= 2 && land[DungeonWalls.LEFT_WALL2.getType()] != null
				&& (land[DungeonWalls.LEFT_WALL2.getType()] instanceof Street
						|| land[DungeonWalls.LEFT_WALL2.getType()] instanceof Exit)) {
			g.drawLine(277, 270, 345, 270);
			g.drawLine(277, 430, 345, 430);
			g.drawLine(277, 222, 277, 477);

			if (depth >= 3)
				g.drawLine(345, 270, 345, 430);
		}

		else if (depth >= 2
				&& (land[DungeonWalls.LEFT_WALL2.getType()] == null || 
						land[DungeonWalls.LEFT_WALL2.getType()] instanceof Obstruction)) {
			g.drawLine(277, 222, 345, 270);
			g.drawLine(277, 477, 345, 430);
		}

		if (land[DungeonWalls.LEFT_WALL.getType()] != null
				&& (land[DungeonWalls.LEFT_WALL.getType()] instanceof Street
						|| land[DungeonWalls.LEFT_WALL.getType()] instanceof Exit)) {
			g.drawLine(GX_MIN, 222, 277, 222);
			g.drawLine(GX_MIN, 477, 277, 477);

			if (depth >= 2)
				g.drawLine(277, 222, 277, 477);
		}

		else if (land[DungeonWalls.LEFT_WALL.getType()] == null
				|| land[DungeonWalls.LEFT_WALL.getType()] instanceof Obstruction) {
			g.drawLine(GX_MIN, 200, 277, 222);
			g.drawLine(GX_MIN, 500, 277, 477);
		}

		// Draw the right doors.
		if (depth >= 4 && land[DungeonWalls.RIGHT_WALL4.getType()] != null
				&& (land[DungeonWalls.RIGHT_WALL4.getType()] instanceof Street
						|| land[DungeonWalls.RIGHT_WALL4.getType()] instanceof Exit)) {
			g.drawLine(rightX + 22, topY, rightX, topY);
			g.drawLine(rightX + 22, bottomY, rightX, bottomY);
			g.drawLine(rightX + 22, topY - 15, rightX + 22, bottomY + 15);

			if (arrangementValue == FrontWallArrangement.NO_FRONT_WALL)
				g.drawLine(rightX, topY, rightX, bottomY);
		}

		else if (depth >= 4
				&& (land[DungeonWalls.RIGHT_WALL4.getType()] == null || 
						land[DungeonWalls.RIGHT_WALL4.getType()] instanceof Obstruction)) {
			g.drawLine(rightX + 22, topY - 15, rightX, topY);
			g.drawLine(rightX + 22, bottomY + 15, rightX, bottomY);
		}

		if (depth >= 3 && land[DungeonWalls.RIGHT_WALL3.getType()] != null
				&& (land[DungeonWalls.RIGHT_WALL3.getType()] instanceof Street
						|| land[DungeonWalls.RIGHT_WALL3.getType()] instanceof Exit)) {
			g.drawLine(500, 300, 545, 300);
			g.drawLine(500, 400, 545, 400);
			g.drawLine(545, 270, 545, 430);

			if (depth >= 4)
				g.drawLine(500, 300, 500, 400);
		}

		else if (depth >= 3
				&& (land[DungeonWalls.RIGHT_WALL3.getType()] == null || 
						land[DungeonWalls.RIGHT_WALL3.getType()] instanceof Obstruction)) {
			g.drawLine(545, 270, 500, 300);
			g.drawLine(545, 430, 500, 400);
		}

		if (depth >= 2 && land[DungeonWalls.RIGHT_WALL2.getType()] != null
				&& (land[DungeonWalls.RIGHT_WALL2.getType()] instanceof Street
						|| land[DungeonWalls.RIGHT_WALL2.getType()] instanceof Exit)) {
			g.drawLine(545, 270, 613, 270);
			g.drawLine(545, 430, 613, 430);
			g.drawLine(613, 222, 613, 477);

			if (depth >= 3)
				g.drawLine(545, 270, 545, 430);
		}

		else if (depth >= 2
				&& (land[DungeonWalls.RIGHT_WALL2.getType()] == null || 
						land[DungeonWalls.RIGHT_WALL2.getType()] instanceof Obstruction)) {
			g.drawLine(545, 270, 613, 222);
			g.drawLine(545, 430, 613, 477);
		}

		if (land[DungeonWalls.RIGHT_WALL.getType()] != null
				&& (land[DungeonWalls.RIGHT_WALL.getType()] instanceof Street
						|| land[DungeonWalls.RIGHT_WALL.getType()] instanceof Exit)) {
			g.drawLine(645, 222, 613, 222);
			g.drawLine(645, 477, 613, 477);

			if (depth >= 2)
				g.drawLine(613, 222, 613, 477);
		}

		else if (land[DungeonWalls.RIGHT_WALL.getType()] == null
				|| land[DungeonWalls.RIGHT_WALL.getType()] instanceof Obstruction) {
			g.drawLine(645, 200, 613, 222);
			g.drawLine(645, 500, 613, 477);
		}
	}

	/**
	 * Checks to see what type of wall the specified land type is.  It will
	 * return either an up ladder, down ladder, or regular wall.
	 * @param frontWall Land : front wall
	 * @return FrontWallType : type of wall
	 */
	private FrontWallType getWallType(Land frontWall){
		if(frontWall != null && frontWall instanceof Exit){

			// If it's an up ladder
			if(((Exit)frontWall).getType() == ExitType.LADDER_UP)
				return FrontWallType.LADDER_UP;

			// If it's a down ladder
			else return FrontWallType.LADDER_DOWN;
		}
		else return FrontWallType.WALL;
	}

	/**
	 * Checks the front wall and surrounding walls to see what arrangement they
	 * are in.  It will check to see if there is a front wall, whether there are
	 * walls surrounding it on either side, or if it is a ladder.
	 * @param land Land[] : the land in view
	 * @param frontWall DungeonWalls : the front wall in the given land array.
	 * @param leftWall DungeonWalls : the left wall in the given land array.
	 * @param rightWall DungeonWalls : the right wall in the given land array.
	 * @return FrontWallArrangement : the arrangement the front wall is in.
	 */
	private FrontWallArrangement getFrontWallArrangement(Land[] land, DungeonWalls frontWall,
			DungeonWalls leftWall, DungeonWalls rightWall){
		// No front wall
		if (land[frontWall.getType()] != null
				&& land[frontWall.getType()] instanceof Street)
			return FrontWallArrangement.NO_FRONT_WALL;

		// No walls to the sides.
		else if (land[leftWall.getType()] != null
				&& (land[leftWall.getType()] instanceof Street
						|| land[leftWall.getType()] instanceof Exit)
						&& land[rightWall.getType()] != null
						&& (land[rightWall.getType()] instanceof Street
								|| land[rightWall.getType()] instanceof Exit)){

			if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_UP)
				return FrontWallArrangement.LADDER_UP_NONE;
			else if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_DOWN)
				return FrontWallArrangement.LADDER_DOWN_NONE;
			else return FrontWallArrangement.WALL_NONE;
		}

		// Wall on right side only.
		else if (land[leftWall.getType()] != null
				&& (land[leftWall.getType()] instanceof Street
						|| land[leftWall.getType()] instanceof Exit)
						&& (land[rightWall.getType()] == null
								|| !(land[rightWall.getType()] instanceof Street)) ){

			if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_UP)
				return FrontWallArrangement.LADDER_UP_RIGHT;
			else if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_DOWN)
				return FrontWallArrangement.LADDER_DOWN_RIGHT;
			else return FrontWallArrangement.WALL_RIGHT;
		}

		// Wall on left side only.
		else if (land[rightWall.getType()] != null
				&& (land[rightWall.getType()] instanceof Street
						|| land[rightWall.getType()] instanceof Exit)
						&& (land[leftWall.getType()] == null
								|| !(land[leftWall.getType()] instanceof Street))){

			if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_UP)
				return FrontWallArrangement.LADDER_UP_LEFT;
			else if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_DOWN)
				return FrontWallArrangement.LADDER_DOWN_LEFT;
			else return FrontWallArrangement.WALL_LEFT;
		}

		// Walls on both sides.
		else {
			if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_UP)
				return FrontWallArrangement.LADDER_UP_BOTH;
			else if(getWallType(land[frontWall.getType()]) == FrontWallType.LADDER_DOWN)
				return FrontWallArrangement.LADDER_DOWN_BOTH;
			else return FrontWallArrangement.WALL_BOTH;
		}
	}

	/**
	 * Based on the given depth, it will call the getFrontWallArrangement
	 * method with the correct set of walls.  It will return the front wall
	 * arrangement so that it can be drawn.
	 * 
	 * @param depth
	 *            The depth of vision as an int.
	 * @param land
	 *            The land array of the area potentially in view.
	 * @return FrontWallArrangement : The type of front wall to draw.
	 * 
	 */
	private FrontWallArrangement checkFrontWall(int depth, Land[] land) {
		switch (depth) {
		case 4:
			return getFrontWallArrangement(land, DungeonWalls.FRONT_WALL4,
					DungeonWalls.LEFT_WALL4, DungeonWalls.RIGHT_WALL4);
		case 3:
			return getFrontWallArrangement(land, DungeonWalls.FRONT_WALL3,
					DungeonWalls.LEFT_WALL3, DungeonWalls.RIGHT_WALL3);
		case 2:
			return getFrontWallArrangement(land, DungeonWalls.FRONT_WALL2,
					DungeonWalls.LEFT_WALL2, DungeonWalls.RIGHT_WALL2);
		case 1:
			return getFrontWallArrangement(land, DungeonWalls.FRONT_WALL,
					DungeonWalls.LEFT_WALL, DungeonWalls.RIGHT_WALL);
		}
		return FrontWallArrangement.NO_FRONT_WALL;
	}

	/**
	 * Makes the game window white.
	 * 
	 */
	public synchronized void blankScreen() {
		Graphics g = this.getGraphics();
		g.setColor(Color.white);
		g.fillRect(GX_MIN, GY_MIN, GWIDTH, GHEIGHT);
	}

	/**
	 * Makes the game window black.
	 */
	public synchronized void dungeonBlankScreen() {
		try {
			Graphics g = this.getGraphics();
			g.setColor(Color.black);
			g.fillRect(GX_MIN, GY_MIN, GWIDTH, GHEIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns whether the auto bandaid checkbox is selected
	 * or not.
	 * @return boolean : auto bandaids
	 */
	public boolean autoBandaids(){
		return chkAutoBandaid.isSelected();
	}
}
