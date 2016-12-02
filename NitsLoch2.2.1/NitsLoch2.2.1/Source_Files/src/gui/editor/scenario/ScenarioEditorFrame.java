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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import src.enums.AmmoPresets;
import src.enums.Armor;
import src.enums.ArmorPresets;
import src.enums.Bars;
import src.enums.DamageType;
import src.enums.Drinks;
import src.enums.Enemies;
import src.enums.EnemyBehavior;
import src.enums.ExitType;
import src.enums.ExplosionImages;
import src.enums.ExplosionType;
import src.enums.GenericPresets;
import src.enums.GroundItems;
import src.enums.HitImages;
import src.enums.InventoryLimits;
import src.enums.MagicPresets;
import src.enums.NPCs;
import src.enums.ObstructionLandType;
import src.enums.PlayerImages;
import src.enums.Shops;
import src.enums.Sounds;
import src.enums.StartingInventory;
import src.enums.StoreItems;
import src.enums.StreetType;
import src.enums.Weapon;
import src.enums.WeaponPresets;
import src.game.CitySpawns;
import src.game.DungeonSpawns;
import src.game.GameWorld;
import src.game.Messages;
import src.game.ShopkeeperSpawns;
import src.game.TheGame;
import src.scenario.Images;
import src.scenario.ItemNameToStoreID;
import src.scenario.MiscScenarioData;
import src.scenario.writer.ScenarioWriter;

public class ScenarioEditorFrame extends JFrame {
	private static final long serialVersionUID = src.Constants.serialVersionUID;
	
	// Menu
	JMenuBar menuBar = new JMenuBar();
	JMenu mnuFile = new JMenu();
	JMenuItem mnuFileNew = new JMenuItem();
	JMenuItem mnuFileOpen = new JMenuItem();
	JMenuItem mnuFileSave = new JMenuItem();
	JMenuItem mnuFileExit = new JMenuItem();
	
	// Tabs
	JTabbedPane mainTabbedPane = new JTabbedPane();
	
	// Objects
	JPanel pnlObjects = new JPanel();
	JTabbedPane objectsTabbedPane = new JTabbedPane();
	JPanel pnlWeapons = new JPanel();
	JPanel pnlArmor = new JPanel();
	JPanel pnlEnemies = new JPanel();
	JPanel pnlNPCs = new JPanel();
	
	public ScenarioEditorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add menu
		mnuFile.setText("File");
		mnuFileNew.setText("New");
		mnuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearData();
				fillInitData();
				setUpTabs();
			}
		});
		mnuFileOpen.setText("Open");
		mnuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
		    		final JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
					int retVal = fc.showOpenDialog(null);
					if(retVal == JFileChooser.APPROVE_OPTION){
						//Error checking
						File f = fc.getSelectedFile();
						if(f.exists()) {
							clearData();
							
							src.Constants.SCENARIO_XML = f.getAbsolutePath();
							new src.scenario.loader.ScenarioLoader(); // Read in scenario data
							setUpTabs();
						}
						else return;
					}
					else return;

		    	} catch(Exception ex){ return; }
			}
		});
		mnuFileSave.setText("Save");
		mnuFileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ScenarioWriter();
			}
		});
		mnuFileExit.setText("Exit");
		mnuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnuFile.add(mnuFileNew);
		mnuFile.add(mnuFileOpen);
		mnuFile.add(mnuFileSave);
		mnuFile.add(mnuFileExit);
		menuBar.add(mnuFile);
		
		fillInitData();
		setUpTabs();

		setJMenuBar(menuBar);
		
		setSize(600, 650);
		setTitle("Roof Damager");
		setVisible(true);
	}
	
	private void setUpTabs() {
		mainTabbedPane.removeAll();
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.add(GeneralPanel.getGeneralPanel(), "General");
		mainTabbedPane.add(InventoryPanel.getInventoryPanel(), "Inventory");
		mainTabbedPane.add(ImagesPanel.getImagesPanel(), "Images");
		mainTabbedPane.add(PresetsPanel.getPresetsPanel(), "Shop Presets");
		
		setUpObjectPanes();
		objectsTabbedPane = new JTabbedPane();
		objectsTabbedPane.add(pnlWeapons, "Weapons");
		objectsTabbedPane.add(pnlArmor, "Armor");
		objectsTabbedPane.add(pnlEnemies, "Enemies");
		objectsTabbedPane.add(pnlNPCs, "NPCs");
		mainTabbedPane.add(pnlObjects, "Objects");
		
		mainTabbedPane.add(LandPanel.getLandPanel(), "Land");
		mainTabbedPane.add(SpawnsPanel.getSpawnsPanel(), "Spawns");
		mainTabbedPane.add(BarsPanel.getBarsPanel(), "Bars");
		mainTabbedPane.add(SoundsPanel.getSoundsPanel(), "Sounds");
		
		getContentPane().removeAll();
		getContentPane().add(mainTabbedPane);
		pnlObjects.add(objectsTabbedPane);
	}
	
	private void setUpObjectPanes() {
		pnlObjects = new JPanel();
		pnlObjects.setLayout(new BoxLayout(pnlObjects, BoxLayout.Y_AXIS));
		pnlWeapons = WeaponsPanel.getWeaponsPanel();
		pnlArmor = ArmorPanel.getArmorPanel();
		pnlEnemies = EnemiesPanel.getEnemyPanel();
		pnlNPCs = NPCsPanel.getNPCsPanel();		
	}
	
	private static void clearData() {
		GameWorld.getInstance().clearInstance();
		TheGame.getInstance().clearInstance();
		CitySpawns.getInstance().clearInstance();
		DungeonSpawns.getInstance().clearInstance();
		Messages.getInstance().clearInstance();
		ShopkeeperSpawns.getInstance().clearInstance();
		Images.getInstance().clearInstance();
		
		Weapon.clearAll();
		Armor.clearAll();
		Enemies.clearAll();
		NPCs.clearAll();
		ObstructionLandType.clearAll();
		StreetType.clearAll();
		Bars.clearAll();
		Sounds.clearAll();
		
		AmmoPresets.clear();
		ArmorPresets.clear();
		GenericPresets.clear();
		MagicPresets.clear();
		WeaponPresets.clear();
		
		InventoryLimits.clearAll();
		StartingInventory.clearAll();
		PlayerImages.clearAll();
		HitImages.clearAll();

		StreetType.clear();
		StoreItems.clear();
	}
	
	private static void fillInitData() {
		MiscScenarioData.NAME = "Untitled Scenario";
		MiscScenarioData.MAP_PATH = "map";
		ExplosionType.MINOR.setDamage(50);
		ExplosionType.MEDIUM.setDamage(70);
		ExplosionType.MAJOR.setDamage(90);
		
		GroundItems.GRENADE.setImageLocation("grenade.png");
		GroundItems.DYNAMITE.setImageLocation("dynamite.png");
		GroundItems.BANDAIDS.setImageLocation("bandaids.png");
		GroundItems.BULLETS.setImageLocation("bullets.png");
		GroundItems.ROCKETS.setImageLocation("rockets.png");
		GroundItems.MONEY.setImageLocation("money.png");
		GroundItems.GRAVE.setImageLocation("grave.png");
		
		Shops.AMMO_L.setImageLocation("ammoL.png");
		Shops.AMMO_R.setImageLocation("ammoR.png");
		Shops.ARMOR_L.setImageLocation("armorL.png");
		Shops.ARMOR_R.setImageLocation("armorR.png");
		Shops.BAR_L.setImageLocation("barL.png");
		Shops.BAR_R.setImageLocation("barR.png");
		Shops.HOSPITAL_L.setImageLocation("hospitalL.png");
		Shops.HOSPITAL_R.setImageLocation("hospitalR.png");
		Shops.WEAPON_L.setImageLocation("weaponL.png");
		Shops.WEAPON_R.setImageLocation("weaponR.png");
		Shops.MAGIC_L.setImageLocation("magicL.png");
		Shops.MAGIC_R.setImageLocation("magicR.png");
		Shops.GENERIC_L.setImageLocation("genericL.png");
		Shops.GENERIC_R.setImageLocation("genericR.png");
		
		PlayerImages.DEAD.setImages("deadL.png", "deadR.png");
		HitImages.HIT1.setLocation("hit1.png");
		
		ExplosionImages.CENTER.setImage("ExplC.png");
		ExplosionImages.NORTH.setImage("ExplN.png");
		ExplosionImages.NORTHEAST.setImage("ExplNE.png");
		ExplosionImages.EAST.setImage("ExplE.png");
		ExplosionImages.SOUTHEAST.setImage("ExplSE.png");
		ExplosionImages.SOUTH.setImage("ExplS.png");
		ExplosionImages.SOUTHWEST.setImage("ExplSW.png");
		ExplosionImages.WEST.setImage("ExplW.png");
		ExplosionImages.NORTHWEST.setImage("ExplNW.png");
		
		ExitType.CITY_GATE.setImage("exitOpen.png", "exitClosed.png");
		ExitType.DUNGEON.setImage("dungeonOpen.png", "dungeonClosed.png");
		ExitType.LADDER_UP.setImage("LadderUp.png", "LadderUp.png");
		ExitType.LADDER_DOWN.setImage("LadderDown.png", "LadderDown.png");
		
		Weapon.W_00.setStats("None", "nothing", "hit", 5,
				DamageType.MELEE_NO_BREAK_NO_PIERCE, false, false, false);
		Weapon.W_01.setStats("Failstache", "a horrible failstache", "disgust", 15,
				DamageType.MELEE_NO_BREAK_NO_PIERCE, true, false, false);
		Armor.A_00.setStats("None", "nothing", 0, 0, 0, 0, 0, false);
		Armor.A_01.setStats("Leather", "a leather vest", 5, 5, 5, 0, 0, true);
		Enemies.E_000.setStats("Mean Dude", 100, 50, Weapon.W_00.getType(), Armor.A_00.getType(),
				EnemyBehavior.MEAN.getType(), 0, 5, false, false, false, false, "enemyL.png",
				"enemyR.png", "enemyD.png");
		NPCs.NPC_000.setStats("nez", "loch", "npc.png", "");
		
		
		ItemNameToStoreID map = new ItemNameToStoreID();
		WeaponPresets.P_000.addWeapon(map.getItemID("weapon1"), 5);
		ArmorPresets.P_000.addArmor(map.getItemID("armor1"), 5);
		AmmoPresets.P_000.addAmmo(map.getItemID("grenade"), 50);
		MagicPresets.P_000.addMagic(map.getItemID("ladder up"), 50);
		GenericPresets.P_000.addGenericItem(map.getItemID("bandaids"), 500);
		
		ObstructionLandType.OBS_000.setStats("tree.png", true, "Tree");
		ObstructionLandType.OBS_001.setStats("wall.png", true, "Wall");
		StreetType.ST_000.setStats("Street", "street.png", 0);
		StreetType.TRIG_000.setStats("Trigger", "trigger.png", 10);
		ArrayList<Enemies> enemies = new ArrayList<Enemies>();
		enemies.add(Enemies.E_000);
		StreetType.TRIG_000.setEnemies(enemies);
		
		ArrayList<Enemies> enemies2 = new ArrayList<Enemies>();
		enemies2.add(Enemies.E_000);
		CitySpawns.getInstance().setEnemies(enemies2, 0);
		ShopkeeperSpawns.getInstance().setShopkeeper(enemies.get(0), 0);
		ArrayList<Enemies> enemies3 = new ArrayList<Enemies>();
		enemies3.add(Enemies.E_000);
		DungeonSpawns.getInstance().setEnemies(enemies, 1);
		
		Bars.BAR_00.setStats("Hi!", "Hi!", "Hi!", "Hi!", "Hi!", "Why don't we sell Woodford? :C");
		Drinks.TONIC_WATER.setCost(5);
		Drinks.SODA.setCost(10);
		Drinks.GIN.setCost(15);
		Drinks.RUM.setCost(20);
		Drinks.SCOTCH.setCost(25);
		Drinks.REDEYE.setCost(30);
		
		PlayerImages.I_00.setImages("plrL.png", "plrR.png");
		PlayerImages.I_01.setImages("stacheL.png", "stacheR.png");
		PlayerImages.DEAD.setImages("dead.png", "dead.png");
	}

}
