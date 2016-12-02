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

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import src.land.Exit;
import src.land.Land;
import src.scenario.Images;
import src.enums.Armor;
import src.enums.Bars;
import src.enums.Direction;
import src.enums.Drinks;
import src.enums.Enemies;
import src.enums.ExitType;
import src.enums.Facing;
import src.enums.GroundItems;
import src.enums.NPCs;
import src.enums.ObstructionLandType;
import src.enums.Sounds;
import src.enums.StoreItems;
import src.enums.Weapon;
import src.game.CitySpawns;
import src.game.DungeonSpawns;
import src.game.DungeonView;
import src.game.Enemy;
import src.game.GameWorld;
import src.game.Item;
import src.game.Messages;
import src.game.OutsideView;
import src.game.Player;
import src.game.ShopkeeperSpawns;
import src.game.TheGame;
import src.enums.AmmoPresets;
import src.enums.ArmorPresets;
import src.enums.GenericPresets;
import src.enums.MagicPresets;
import src.enums.WeaponPresets;
import src.enums.StreetType;
import src.exceptions.EnemyDiedException;
import src.exceptions.NoSuchCityException;
import src.exceptions.PlayerEnteredException;
import src.file.player.PlayerFile;
import src.file.scenario.ScenarioFile;
import java.util.ArrayList;

/**
 * Facade between the GUI and the domain layer.
 *
 * @author Darren Watts date 11/11/07
 *
 */
public class Controller {

	private GameFrame gf;

        private ArrayList<Boolean> startLocations;
        private ArrayList<Boolean> enemies;

        public Controller(){
            startLocations = new ArrayList<Boolean>();
            enemies = new ArrayList<Boolean>();

            for(int i = 0; i < getNumCities(); i++){
                startLocations.add(false);
                enemies.add(true);
            }
           // startLocations.set(0, true);
        }

        /**
         * Sets the start location boolean array.  Used to see which
         * level to start on.
         * @param starts ArrayList : start locations
         */
        public void setStartLocations(ArrayList<Boolean> starts){
            startLocations = starts;
        }

        /**
         * Sets the enemies array.  Used to clear enemies off of certain
         * levels when skipping levels.
         * @param enems ArrayList : enemies
         */
        public void setEnemies(ArrayList<Boolean> enems){
            enemies = enems;
        }

	/**
	 * Sets the game frame for this controller.
	 *
	 * @param frame
	 *            GameFrame
	 */
	public void setGameFrame(GameFrame frame) {
		gf = frame;
	}

	/**
	 * Checks to see whether there is a shopkeeper guarding the store at the
	 * given location. If there is, it will return the enemy object that is the
	 * shopkeeper. Otherwise, it will return null.
	 *
	 * @param row
	 *            int : row of the shop
	 * @param col
	 *            int : column of the shop
	 * @return Enemy : The shopkeeper. Null if there isn't one.
	 */
	public Enemy getShopkeeperAround(int row, int col) {
		for (int i = row - 3; i <= row + 3; i++) {
			for (int k = col - 3; k <= col + 3; k++) {
				try {
					if (GameWorld.getInstance().getLandAt(i, k).getEnemy() != null) {
						if (GameWorld.getInstance().getLandAt(i, k).getEnemy()
								.isShopkeeper())
							return GameWorld.getInstance().getLandAt(i, k)
									.getEnemy();
					}
				} catch (NullPointerException ex) {
				} // Do nothing
			}
		}
		return null;
	}

	/**
	 * Checks to see if the player successfully steals the item.
	 *
	 * @param player
	 *            Player : the player
	 * @return boolean : success
	 */
	public boolean successfullySteal(Player player) {
		return Math.random() * 100.0 * player.getLevel() < player
				.getThievingAbility()
				&& !player.getCaughtStealing();
	}

	/**
	 * Initializes parts of the game so that a new game can start fresh.
	 */
	public void newGame() {
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

		StreetType.clear();
		StoreItems.clear();
		
		GameWorld.getInstance().loadMapFromFile();
	}

	/**
	 * Sets the explode row and column from the player's point of view, so the
	 * explosion can be drawn to the screen.
	 *
	 * @param row
	 *            int : row
	 * @param col
	 *            itn : column
	 */
	public void explode(int row, int col) {
		GameWorld.getInstance().setExplodeRow(row);
		GameWorld.getInstance().setExplodeCol(col);
	}

	/**
	 * Gets the outside view that will be drawn on the main game window.
	 * @return BufferedImage[][] : images to be drawn.
	 */
	public BufferedImage[][] getOutsideView() {
		int row = GameWorld.getInstance().getLocalPlayer().getRow();
		int col = GameWorld.getInstance().getLocalPlayer().getCol();
		return OutsideView.getView(GameWorld.getInstance().getGameWorld(row,
				col));
	}

	/**
	 * Gets the outside view that will be drawn to the main game window
	 * without any objects on the ground.
	 * @return BufferedImage[][] : images to be drawn.
	 */
	public BufferedImage[][] getOutsideViewNoObjects() {
		int row = GameWorld.getInstance().getLocalPlayer().getRow();
		int col = GameWorld.getInstance().getLocalPlayer().getCol();
		return OutsideView.getViewNoObjects(GameWorld.getInstance().getGameWorld(row,
				col));
	}

	/**
	 * Checks whether or not the local player is in a dungeon or not.
	 *
	 * @return boolean : in dungeon
	 */
	public boolean getIsInDungeon() {
		return TheGame.getInstance().getInDungeon();
	}

        /**
         * Returns the number of cities in the world.
         * @return int : number of cities
         */
        public int getNumCities(){
            return GameWorld.getInstance().getCities().size();
        }

        public void skipLevels(){
            int startLevel = 0;
            for(int i = 0; i < getNumCities(); i++){
                if(!enemies.get(i)){
                    GameWorld.getInstance().removeEnemies(i);
                }
                if(startLocations.get(i)) startLevel = i;
            }
            try {
                GameWorld.getInstance().setCurrentLevel(startLevel);
                GameWorld.getInstance().getLocalPlayer().setLocation(startLevel);

            } catch(NoSuchCityException noCity){

            }
        }

        /**
         * Returns the city name of the level index specified
         * @param level int : level index
         * @return String : city name
         */
        public String getCityName(int level){
            return GameWorld.getInstance().getCityName(level);
        }

	/**
	 * Sets whether or not the game is running.
	 *
	 * @param bool
	 *            boolean : running
	 */
	public void setGameRunning(boolean bool) {
		TheGame.getInstance().setRunning(bool);
	}

	/**
	 * Gets whether or not the game is running.
	 *
	 * @return boolean : running
	 */
	public boolean getGameRunning() {
		return TheGame.getInstance().getRunning();
	}

	public Land[] getVisibleDungeonArea() {
		return DungeonView.getVisibleArea();
	}

	/**
	 * Blanks the main window for dungeon mode.
	 */
	public void blankScreenDungeon() {
		gf.dungeonBlankScreen();
	}

	/**
	 * Calls the draw method in the GUI. Used to update the view in the GUI.
	 *
	 */
	public void drawGameWindow() {
		try {
			if (TheGame.getInstance().getInDungeon()) {
				gf.drawDungeonMode();
				gf.updateArmorMenu(GameWorld.getInstance().getLocalPlayer()
						.getAvailableArmor());
				gf.updateWeaponMenu(GameWorld.getInstance().getLocalPlayer()
						.getWeaponLevels());
			} else {
				int plrRow = GameWorld.getInstance().getLocalPlayer().getRow();
				int plrCol = GameWorld.getInstance().getLocalPlayer().getCol();
				gf.drawGameWindow(GameWorld.getInstance().getGameWorld(plrRow,
						plrCol));
				/*gf.updateArmorMenu(GameWorld.getInstance().getLocalPlayer()
						.getAvailableArmor());
				gf.updateWeaponMenu(GameWorld.getInstance().getLocalPlayer()
						.getWeaponLevels());*/
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Updates the weapon menus on the GUI.
	 *
	 * @param levels
	 *            int[] : weapon levels
	 */
	public void updateWeaponMenu(int levels[]) {
		try {
			gf.updateWeaponMenu(levels);
		} catch (Exception ex) {
		}
	}

	/**
	 * Updates the armor menus on the GUI.
	 *
	 * @param available
	 *            boolean[] : available armor
	 */
	public void updateArmorMenu(boolean available[]) {
		try {
			gf.updateArmorMenu(available);
		} catch (Exception ex) {
		}
	}

	/**
	 * Calls the drawMap method in the GUI.
	 *
	 */
	public void drawMap() {
		gf.drawMap();
	}

	/**
	 * Sets the save game path.
	 *
	 * @param path
	 *            String : path to save game.
	 */
	public void setSavePath(String path) {
		TheGame.getInstance().setSavePath(path);
	}

	/**
	 * Gets the save path.
	 *
	 * @return String : save path
	 */
	public String getSavePath() {
		return TheGame.getInstance().getSavePath();
	}

	/**
	 * Imports a saved character from a file.
	 *
	 * @param inFile
	 *            String : path to input file.
	 */
	public void importCharacter(String inFile) {
		PlayerFile.getInstance().load(inFile);
	}

	/**
	 * Exports the current character to a file.
	 *
	 * @param outFile
	 *            String : path to exported file.
	 */
	public void exportCharacter(String outFile) {
		Player plr = GameWorld.getInstance().getLocalPlayer();
		if(plr.getNumExports() > 0){
			plr.removeExport(1);
			PlayerFile.getInstance().save(outFile);
			Messages.getInstance().addMessage("You exported your character to a file.");
		}
		else Messages.getInstance().addMessage("You do not have any export spells.");
	}

	/**
	 * Checks to see if the local player has at least one map viewer.
	 * @return boolean : has a map viewer
	 */
	public boolean playerHasAMapViewer(){
		return GameWorld.getInstance().getLocalPlayer().getNumMapViewers() > 0;
	}

	/**
	 * Removes a map viewer from the local player's inventory.
	 */
	public void removeMapViewer(){
		GameWorld.getInstance().getLocalPlayer().removeMapViewer(1);
	}

	/**
	 * Purchases a drink.
	 *
	 * @param drink
	 *            Drinks : the drink to purchase
	 * @param permutation
	 *            int : permutation for which bar to purchase from.
	 */
	public void purchaseDrink(Drinks drink, int permutation) {
		src.enums.Bars.purchaseItem(drink, permutation);
	}

	/**
	 * Activates the enemy AI.
	 */
	public void activateEnemyAI() {
		GameWorld.getInstance().activateEnemyAI();
	}

	/**
	 * Sets the readied weapon of the player.
	 *
	 * @param wpn
	 *            Weapon : the weapon
	 */
	public void setPlrReadiedWeapon(Weapon wpn) {
		GameWorld.getInstance().getLocalPlayer().setReadiedWeapon(wpn);
	}

	/**
	 * Gets the local player's readied weapon.
	 *
	 * @return Weapon : readied weapon
	 */
	public Weapon getPlrReadiedWeapon() {
		return GameWorld.getInstance().getLocalPlayer().getReadiedWeapon();
	}

	/**
	 * Sets the local player's readied armor
	 *
	 * @param arm
	 *            Armor : armor
	 */
	public void setPlrReadiedArmor(Armor arm) {
		GameWorld.getInstance().getLocalPlayer().setReadiedArmor(arm);
	}

	/**
	 * Gets the local player's readied armor.
	 *
	 * @return Armor : readied armor
	 */
	public Armor getPlrReadiedArmor() {
		return GameWorld.getInstance().getLocalPlayer().getReadiedArmor();
	}

	/**
	 * Player casts an up ladder spell.
	 */
	public void playerUpSpell(){
		Player plr = GameWorld.getInstance().getLocalPlayer();
		if(plr.getNumLaddersUp() > 0){
			try {
				int destLevel = plr.getDungeonPathUp();
				if(destLevel == -1) return;
				
				// Find the ladder that leads back to where you came from, and set the dungeon level
				Land[][] land = GameWorld.getInstance().getLand();
				for(int i = 0; i < land.length; i++){
					for(int k = 0; k < land[0].length; k++){
						if(land[i][k] instanceof Exit){
							if( ((Exit)land[i][k]).getType() == ExitType.LADDER_UP &&
									((Exit)land[i][k]).getNextCity() == destLevel){
								plr.setDungeonLevel(((Exit)land[i][k]).getDestinationRow());
							}
						}
					}
				}
				
				GameWorld.getInstance().setCurrentLevel(destLevel);
				plr.setLocation(destLevel);
				plr.removeLadderUp(1);
				if(!plr.dungeonPathHasEntries()) plr.setInDungeon(false);
				TheGame.getInstance().setHasBeenCleared(false);
				Messages.getInstance().addMessage("You cast an up ladder spell.");
			} catch(NoSuchCityException ex){
				Messages.getInstance().addMessage("Cannot use a ladder up spell at this time.");
			}
		}
	}

	/**
	 * Player casts a down ladder spell.
	 */
	public void playerDownSpell(){
		int nextCity, nextDungeonLevel;

		GameWorld gw = GameWorld.getInstance();

		Player plr = gw.getLocalPlayer();
		Land[][] land = gw.getLand();
		if(plr.getNumLaddersDown() > 0){
			for(int i = 0; i < land.length; i++){
				for(int k = 0; k < land[0].length; k++){
					if(land[i][k] instanceof Exit){
						if( ((Exit)land[i][k]).getType() == ExitType.LADDER_DOWN){
							nextCity = ((Exit)land[i][k]).getNextCity();
							nextDungeonLevel = ((Exit)land[i][k]).getDestinationRow();

							try{
								if(gw.getCities().get(nextCity) != null){
									plr.addToDungeonPath(gw.getCurrentLevel());

									gw.setCurrentLevel(nextCity);
									plr.setLocation(nextCity);
									plr.setDungeonLevel(nextDungeonLevel);
									TheGame.getInstance().setHasBeenCleared(false);
									plr.removeLadderDown(1);
									Messages.getInstance().addMessage("You cast a down ladder spell.");
									return;
								}
								else Messages.getInstance().addMessage("Dungeon does not exist.");
							} catch(Exception e){
								Messages.getInstance().addMessage("Dungeon does not exist.");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Causes the local player to pass.
	 */
	public void playerPass() {
		GameWorld.getInstance().getLocalPlayer().pass();
	}

	/**
	 * Sets whether or not the local player is shooting.
	 *
	 * @param bool
	 *            boolean : shooting
	 */
	public void setPlayerShooting(boolean bool) {
		if (GameWorld.getInstance().getLocalPlayer().getReadiedWeapon().getDamageType().isMarksman() ||
				GameWorld.getInstance().getLocalPlayer().getReadiedWeapon().usesRockets()) {
			GameWorld.getInstance().getLocalPlayer().setIsShooting(bool);
			return;
		}
	}

	/**
	 * Gets the local player's name.
	 *
	 * @return String : name
	 */
	public String getPlayerName() {
		return GameWorld.getInstance().getLocalPlayer().getName();
	}

	/**
	 * Gets the local player's hit points.
	 *
	 * @return int : hit points
	 */
	public int getPlayerHitPoints() {
		return GameWorld.getInstance().getLocalPlayer().getHitPoints();
	}

	/**
	 * Gets the local player's maximum hit points.
	 *
	 * @return int : max hit points
	 */
	public int getPlayerMaxHitPoints() {
		return GameWorld.getInstance().getLocalPlayer().getMaxHitPoints();
	}

	/**
	 * Gets the local player's fighting ability.
	 *
	 * @return int : fighting ability
	 */
	public int getPlayerFightingAbility() {
		return GameWorld.getInstance().getLocalPlayer().getFightingAbility();
	}

	/**
	 * Gets the local player's marksmanship ability.
	 *
	 * @return int : marksmanship ability
	 */
	public int getPlayerMarksmanAbility() {
		return GameWorld.getInstance().getLocalPlayer().getMarksmanAbility();
	}

	/**
	 * Gets the local player's martial arts ability.
	 *
	 * @return int : martial arts ability
	 */
	public int getPlayerMartialArtsAbility() {
		return GameWorld.getInstance().getLocalPlayer().getMartialArtsAbility();
	}

	/**
	 * Gets the local player's thieving ability.
	 *
	 * @return int : thieving ability
	 */
	public int getPlayerThievingAbility() {
		return GameWorld.getInstance().getLocalPlayer().getThievingAbility();
	}

	/**
	 * Gets the local player's amount of money.
	 *
	 * @return int : money
	 */
	public int getPlayerMoney() {
		return GameWorld.getInstance().getLocalPlayer().getMoney();
	}

	/**
	 * Gets the local player's amount of grenades.
	 *
	 * @return int : grenades
	 */
	public int getPlayerNumGrenades() {
		return GameWorld.getInstance().getLocalPlayer().getNumGrenades();
	}

	/**
	 * Gets the local player's amount of dynamite.
	 *
	 * @return int : dynamite
	 */
	public int getPlayerNumDynamite() {
		return GameWorld.getInstance().getLocalPlayer().getNumDynamite();
	}

	/**
	 * Gets the local player's amount of bandaids.
	 *
	 * @return int : bandaids
	 */
	public int getPlayerNumBandaids() {
		return GameWorld.getInstance().getLocalPlayer().getNumBandaids();
	}

	/**
	 * Gets the local player's amount of bullets.
	 *
	 * @return int : bullets
	 */
	public int getPlayerNumBullets() {
		return GameWorld.getInstance().getLocalPlayer().getNumBullets();
	}

	/**
	 * Gets the local player's amount of rockets.
	 *
	 * @return int : rockets
	 */
	public int getPlayerNumRockets() {
		return GameWorld.getInstance().getLocalPlayer().getNumRockets();
	}

	/**
	 * Gets the local player's amount of flame packs.
	 *
	 * @return int : flame packs
	 */
	public int getPlayerNumFlamePacks() {
		return GameWorld.getInstance().getLocalPlayer().getNumFlamePacks();
	}

	/**
	 * Gets the local player's amount of ladders up.
	 *
	 * @return int : ladders up
	 */
	public int getPlayerNumLaddersUp() {
		return GameWorld.getInstance().getLocalPlayer().getNumLaddersUp();
	}

	/**
	 * Gets the local player's amount of ladders down.
	 *
	 * @return int : ladders down
	 */
	public int getPlayerNumLaddersDown() {
		return GameWorld.getInstance().getLocalPlayer().getNumLaddersDown();
	}

	/**
	 * Gets the local player's amount of map viewers.
	 *
	 * @return int : map viewers
	 */
	public int getPlayerNumMapViewers() {
		return GameWorld.getInstance().getLocalPlayer().getNumMapViewers();
	}

	/**
	 * Gets the local player's amount of exports.
	 *
	 * @return int : exports
	 */
	public int getPlayerNumExports() {
		return GameWorld.getInstance().getLocalPlayer().getNumExports();
	}

	/**
	 * Halts the drawing of the GUI.
	 */
	public void haltDrawing() {
		TheGame.getInstance().haltDrawing();
	}

	/**
	 * Resumes drawing of the GUI.
	 */
	public void resumeDrawing() {
		TheGame.getInstance().resumeDrawing();
	}

	/**
	 * Turns the player left or right.
	 *
	 * @param fac
	 *            Facing : direction to turn.
	 */
	public void turnPlayer(Facing fac) {
		Player plr = GameWorld.getInstance().getLocalPlayer();
		if (fac == Facing.LEFT) {
			if (plr.getFacing() == Facing.LEFT)
				plr.setFacing(Facing.DOWN);
			else if (plr.getFacing() == Facing.DOWN)
				plr.setFacing(Facing.RIGHT);
			else if (plr.getFacing() == Facing.RIGHT)
				plr.setFacing(Facing.UP);
			else if (plr.getFacing() == Facing.UP)
				plr.setFacing(Facing.LEFT);
		} else {
			if (plr.getFacing() == Facing.LEFT)
				plr.setFacing(Facing.UP);
			else if (plr.getFacing() == Facing.DOWN)
				plr.setFacing(Facing.LEFT);
			else if (plr.getFacing() == Facing.RIGHT)
				plr.setFacing(Facing.DOWN);
			else if (plr.getFacing() == Facing.UP)
				plr.setFacing(Facing.RIGHT);
		}
	}

	/**
	 * Turns the player around.
	 */
	public void turnPlayerAround() {
		Player plr = GameWorld.getInstance().getLocalPlayer();
		if (plr.getFacing() == Facing.LEFT)
			plr.setFacing(Facing.RIGHT);
		else if (plr.getFacing() == Facing.DOWN)
			plr.setFacing(Facing.UP);
		else if (plr.getFacing() == Facing.RIGHT)
			plr.setFacing(Facing.LEFT);
		else if (plr.getFacing() == Facing.UP)
			plr.setFacing(Facing.DOWN);
	}

	/**
	 * Moves the player forward.
	 */
	public void movePlayerForward() {
		try {
			Player plr = GameWorld.getInstance().getLocalPlayer();
			if (plr.getFacing() == Facing.LEFT)
				plr.move(Direction.WEST);
			else if (plr.getFacing() == Facing.DOWN)
				plr.move(Direction.SOUTH);
			else if (plr.getFacing() == Facing.RIGHT)
				plr.move(Direction.EAST);
			else if (plr.getFacing() == Facing.UP)
				plr.move(Direction.NORTH);
		} catch(PlayerEnteredException ex) {
		} catch(EnemyDiedException died) { }

	}

	/**
	 * Gets whether or not the local player is viewing the overhead map.
	 *
	 * @return boolean : viewing map
	 */
	public boolean getPlayerViewingMap() {
		return TheGame.getInstance().getViewingMap();
	}

	/**
	 * Gets the local player's row.
	 *
	 * @return int : row
	 */
	public int getPlayerRow() {
		return GameWorld.getInstance().getLocalPlayer().getRow();
	}

	/**
	 * Gets the local player's column.
	 *
	 * @return int : column
	 */
	public int getPlayerCol() {
		return GameWorld.getInstance().getLocalPlayer().getCol();
	}

	/**
	 * Sets whether or not the local player is viewing the overhead map.
	 *
	 * @param bool
	 *            boolean : viewing map
	 */
	public void setPlayerViewingMap(boolean bool) {
		TheGame.getInstance().setViewingMap(bool);
	}

	/**
	 * Moves the local player in the specified direction.
	 *
	 * @param dir
	 *            Direction : direction
	 */
	public void movePlayer(Direction dir) {
		GameWorld gw = GameWorld.getInstance();
		Facing fac = gw.getLocalPlayer().getFacing();
		int level = gw.getCurrentLevel();
		int row = gw.getLocalPlayer().getRow();
		int col = gw.getLocalPlayer().getCol();
		try {
			gw.getLocalPlayer().move(dir);
		} catch(PlayerEnteredException e){
			gf.setJustMoved(true);
			return;
		} catch(EnemyDiedException died){
			GameWorld.getInstance().triggerWorldUpdate();
			gf.setJustMoved(true);
			return;
		}
		if(fac != gw.getLocalPlayer().getFacing()) gf.setJustMoved(true);

		// If went through a teleporter or city gate, make sure just moved is
		// set to true to clear out visual glitches.
		if(gw.getCurrentLevel() != level || Math.abs(row - gw.getLocalPlayer().getRow()) > 1
				|| Math.abs(col - gw.getLocalPlayer().getCol()) > 1) {
			gf.setJustMoved(true);
		}
	}

	/**
	 * Gets the weapon level of the local player's specified weapon type.
	 *
	 * @param wpn
	 *            Weapon : weapon type
	 * @return int : weapon level
	 */
	/*public int getWeaponLevel(Weapon wpn) {
		return GameWorld.getInstance().getLocalPlayer().getWeaponLevel(wpn);
	}*/

	/**
	 * Gets the facing direction of the local player.
	 *
	 * @return Facing : facing direction
	 */
	public Facing getPlayerFacing() {
		return GameWorld.getInstance().getLocalPlayer().getFacing();
	}

	/**
	 * Gets the number of rows in the current city.
	 *
	 * @return int : number of rows
	 */
	public int getLandNumRows() {
		int level = GameWorld.getInstance().getCurrentLevel();
		return GameWorld.getInstance().getCities().get(level).length;
	}

	/**
	 * Gets the number of columns in the current city.
	 *
	 * @return int : number of columns
	 */
	public int getLandNumCols() {
		int level = GameWorld.getInstance().getCurrentLevel();
		return GameWorld.getInstance().getCities().get(level)[0].length;
	}

	/**
	 * Gets the current city.
	 *
	 * @return Land[][] : current city
	 */
	public Land[][] getCurrentCity() {
		return GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel());
	}

	/**
	 * Gets the current city name.
	 * @return String : current city name
	 */
	public String getCurrentCityName(){
		return GameWorld.getInstance().getCityName(GameWorld.getInstance().getCurrentLevel());
	}

	/**
	 * Gets the title from the scenario info file in the .nits file.
	 * @return String : scenario title
	 */
	public String getScenarioTitle(){
		String title;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document dom;

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(ScenarioFile.getInstance().getStream(src.Constants.SCENARIO_INFO));

			title = dom.getDocumentElement().getElementsByTagName(
			"title").item(0).getFirstChild().getNodeValue();

			return title;

		}catch(Exception e) {
			return "";
		}
	}

	/**
	 * Gets the message about the scenario from the scenario info file
	 * in the .nits file.
	 * @return String : scenario message
	 */
	public String getScenarioMessage(){
		String message;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document dom;

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(ScenarioFile.getInstance().getStream(src.Constants.SCENARIO_INFO));

			message = dom.getDocumentElement().getElementsByTagName(
			"message").item(0).getFirstChild().getNodeValue();

			return message;

		}catch(Exception e) {
			return "";
		}
	}

	/**
	 * Sets a grenade at the specified row and column in the world.
	 *
	 * @param row
	 *            int : row
	 * @param col
	 *            int : column
	 */
	public void setGrenade(int row, int col) {
		GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel())[row][col]
				.setItem(new Item(GroundItems.GRENADE, 2));
	}

	/**
	 * Returns the local player to the GUI.
	 *
	 * @return Player : the local player
	 */
	public Player getLocalPlayer() {
		return GameWorld.getInstance().getLocalPlayer();
	}
	
	/**
	 * Returns whether the auto bandaids checkbox is
	 * selected.
	 * @return boolean : auto bandaids
	 */
	public boolean getAutoBandaids(){
		return gf.autoBandaids();
	}

	/**
	 * Sets a stick of dynamite at the specified row and column in the world.
	 *
	 * @param row
	 *            int : row
	 * @param col
	 *            int : column
	 */
	public void setDynamite(int row, int col) {
		GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel())[row][col]
				.setItem(new Item(GroundItems.DYNAMITE, 2));
	}

	/**
	 * Saves the game
	 *
	 * @param saveFile
	 *            String : path to save game file
	 */
	public void saveGame(String saveFile) {
		if (getLocalPlayer().getIsDead())
			return;
		TheGame.getInstance().haltDrawing();
		try {
			FileOutputStream fout = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			GameWorld.getInstance().save(oos);
			getLocalPlayer().save(oos);
			oos.close();
		} catch (Exception e) {
			System.out.println("Problem saving");
		}
		TheGame.getInstance().resumeDrawing();
	}

	/**
	 * Loads the game by reading in a serialized object.
	 *
	 * @param loadFile
	 *            File name of the file to load from.
	 */
	public void loadGame(String loadFile){
		TheGame.getInstance().haltDrawing();
		// GameWorld.getInstance();
		GameWorld.getInstance().setExplodeRow(-1);
		GameWorld.getInstance().setExplodeCol(-1);
    	try{
	    	FileInputStream fin = new FileInputStream(loadFile);
	        ObjectInputStream ois = new ObjectInputStream(fin);
	        GameWorld.getInstance().load(ois);
	        if(GameWorld.getInstance().getLocalPlayer() == null){
	        	GameWorld.getInstance().addPlayer(new Player(
	        			"Player",
	        			10, 10, 10, 10, 10, 10));
	        }
	        getLocalPlayer().load(ois);
	        ois.close();
    	} catch(Exception e){
    		e.printStackTrace();
    		System.out.println("Problem loading");
    	}
    	TheGame.getInstance().startGame();
		TheGame.getInstance().resumeDrawing();
	}
}
