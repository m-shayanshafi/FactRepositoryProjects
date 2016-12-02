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

package src.game;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;

import src.Constants;
import src.enums.Armor;
import src.enums.Direction;
import src.enums.Facing;
import src.enums.InventoryLimits;
import src.enums.StartingInventory;
import src.enums.VehicleType;
import src.enums.Weapon;
import src.enums.HitImages;
import src.enums.PlayerImages;
import src.exceptions.EnemyDiedException;
import src.exceptions.NoSuchCityException;
import src.exceptions.PlayerEnteredException;

/**
 * Class to keep track of a player in the world.  Will record location
 * and current stats along with the methods that can be performed
 * on the player.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Player implements Serializable{
	private static final long serialVersionUID = src.Constants.serialVersionUID;

	private int col;
	private int row;
	private int index;

	/* Attributes */
	private String playerName;
	private int hitPoints;
	private int maxHitPoints;
	private int fightingAbility;
	private int marksmanAbility;
	private int martialArtsAbility;
	private int thievingAbility;

	/* Armor */
	private Armor armor[];
	private boolean availableArmor[]; // Which armor is in inventory.
	private int readiedArmorIndex;  // Which armor is being used.

	/* Weapons */
	private Weapon weapons[];
	private int readiedWeaponIndex; // Which weapon is being used.
	private int weaponLevels[]; // The level of the weapons.

	/* Inventory */
	private int money;
	private int numBandaids;
	private int numGrenades;
	private int numDynamite;
	private int numBullets;
	private int numRockets;
	private int numFlamePacks;
	private int numLaddersUp;
	private int numLaddersDown;
	private int numMapViewers;
	private int numExports;

	/* Used for graphic of the player.  He can face left or right */
	private Facing facing;

	// Dungeon state information
	private boolean inDungeon;
	private int dungeonLevel;
	private Stack<Integer> dungeonPath;

	// Used to draw the "bam", "pow" pictures.
	private boolean justHit;
	private boolean justRecoveredFromHit;
	private int hitCounter;
	private String hitImage;

	private boolean isDead;
	private boolean isShooting;
	private boolean caughtStealing;

	private VehicleType vehicle;

	/**
	 * Creates a player object.
	 * @param name String : name of the player
	 * @param hp int : hit points
	 * @param fightAbil int : fighting ability
	 * @param marksAbil int : marksmanship ability
	 * @param martsAbil int : martial arts ability
	 * @param thievAbil int : thieving ability
	 * @param mon int : money
	 */
	public Player(String name, int hp, int fightAbil, int marksAbil, int martsAbil,
			int thievAbil, int mon) throws NoSuchCityException {
		
		playerName = name;
		hitPoints = hp;
		maxHitPoints = hp;
		fightingAbility = fightAbil;
		marksmanAbility = marksAbil;
		martialArtsAbility = martsAbil;
		thievingAbility = thievAbil;
		money = mon;

		facing = Facing.RIGHT;
		justHit = false;
		justRecoveredFromHit = false;
		hitCounter = 0;

		isDead = false;
		isShooting = false;
		caughtStealing = false;
		inDungeon = false;

		numBandaids = StartingInventory.BANDAIDS.getAmount();
		numBullets = StartingInventory.BULLETS.getAmount();
		numDynamite = StartingInventory.DYNAMITE.getAmount();
		numFlamePacks = StartingInventory.FLAME_PACKS.getAmount();
		numGrenades = StartingInventory.GRENADES.getAmount();
		numLaddersDown = StartingInventory.LADDER_DOWN.getAmount();
		numLaddersUp = StartingInventory.LADDER_UP.getAmount();
		numMapViewers = StartingInventory.MAP_VIEWERS.getAmount();
		numRockets = StartingInventory.ROCKETS.getAmount();
		numExports = StartingInventory.EXPORTS.getAmount();

		vehicle = VehicleType.NONE;

		try {
			setLocation(0);
		} catch(NoSuchCityException noCity){
			throw new NoSuchCityException();
		}

		initializeArmor();
		initializeWeapons();
		
		dungeonPath = new Stack<Integer>();
	}

	/**
	 * Sets the location of the player in the specified city.  Used for
	 * setting the start location, or the location when the player enters
	 * a new city.
	 * @param level int : city number
	 */
	public void setLocation(int level) throws NoSuchCityException {
		try {
		int[] start = GameWorld.getInstance().getPlayerLocation(level);
		setRow(start[0]);
		setCol(start[1]);
		} catch(NoSuchCityException noCity){
			throw new NoSuchCityException();
		}
	}
	
	/**
	 * Adds a city index to the dungeon path.
	 * @param cityIndex
	 */
	public void addToDungeonPath(int cityIndex){
		dungeonPath.add(cityIndex);
	}
	
	/**
	 * Checks to see whether there are any more levels left in the
	 * dungeon path.
	 * @return boolean : dungeon path has more levels
	 */
	public boolean dungeonPathHasEntries(){
		return dungeonPath.size() > 0;
	}
	
	/**
	 * Pops a level index from the dungeon path stack and 
	 * returns its value.
	 * @return int : up spell destination
	 */
	public int getDungeonPathUp(){
		try {
			return dungeonPath.pop();
		} catch(Exception ex) { return -1; }
	}

	/**
	 * Creates an array with all possible armor choices and gives the player the
	 * starting armor.
	 *
	 */
	private void initializeArmor(){
		armor = new Armor[Armor.values().length];
		for(int i = 0; i < Armor.values().length; i++){
			armor[i] = Armor.values()[i];
		}

		availableArmor = new boolean[Armor.values().length];

		// Debug
		/*for(int i = 0; i < Armor.values().length; i++){
			availableArmor[i] = true;
		}*/

		availableArmor[0] = true;
		setReadiedArmor(Armor.A_00);
	}

	/**
	 * Creates an array with all possible weapon choices and gives the player the
	 * starting weapon.
	 *
	 */
	private void initializeWeapons(){
		weapons     = new Weapon[Weapon.values().length];
		weaponLevels = new int[Weapon.values().length];
		for(int i = 0; i < Weapon.values().length; i++){
			weapons[i] = Weapon.values()[i];
			weaponLevels[i] = 0;
		}

		//Debug
		/*for(int i = 0; i < Constants.MAX_NUMBER_WEAPONS; i++){
			weaponLevels[i] = 1;
		}*/

		weaponLevels[0] = 1;
		setReadiedWeapon(Weapon.W_00);
	}

	/**
	 * Player shoots in the specified direction.
	 * @param direction Direction : shooting direction
	 */
	private void shoot(Direction direction) throws EnemyDiedException {
		if(getReadiedWeapon().getDamageType().isMarksman()){
			try {
				Battles.shoot(this, direction);
			} catch(EnemyDiedException ex){
				GameWorld.getInstance().triggerWorldUpdate();
				throw new EnemyDiedException();
			}
			GameWorld.getInstance().triggerWorldUpdate();
		}
		else if(getReadiedWeapon().usesRockets()){
			Battles.shootRocket(this, direction);
			
			// Because shootRocket sets the explode row and then
			// trigger world update removes it, you have to record
			// what it is, and reset it after the update.
			int explodeRow = GameWorld.getInstance().getExplodeRow();
			int explodeCol = GameWorld.getInstance().getExplodeCol();
			
			GameWorld.getInstance().triggerWorldUpdate();
			TheGame.getInstance().getController().explode(explodeRow, explodeCol);
		}
	}

	/**
	 * If the player can move, it will call the enter method of the land,
	 * update the player facing direction, and trigger a world update.
	 * @param direction Direction to move as a String.
	 */
	public void move(Direction direction) throws PlayerEnteredException, EnemyDiedException {
		// Can't move if dead or viewing map.
		if(isDead || TheGame.getInstance().getViewingMap()) return;

		if(isShooting) {
			try {
				shoot(direction);
			} catch(EnemyDiedException e) {
				throw new EnemyDiedException();
			}
			return;
		}

		// Can't move diagonally in dungeon.
		if(inDungeon){
			if(direction == Direction.NORTHEAST || direction == Direction.NORTHWEST ||
					direction == Direction.SOUTHEAST || direction == Direction.SOUTHWEST)
				return;
		}

		updateFacing(direction);

		try {
			GameWorld.getInstance().enter(this, direction);
			GameWorld.getInstance().triggerWorldUpdate();
		} catch(PlayerEnteredException e){
			GameWorld.getInstance().triggerWorldUpdate();
			throw new PlayerEnteredException();
		}

	}

	/**
	 * Updates the player's facing direction based on the movement direction
	 * given.  It will act differently depending on if the palyer is in
	 * a dungeon or not.
	 * @param direction Direction : movement direction
	 */
	public void updateFacing(Direction direction){
		if(!inDungeon){
			if(direction == Direction.EAST || direction == Direction.NORTHEAST ||
					direction == Direction.SOUTHEAST)
				facing = Facing.RIGHT;
			else if(direction == Direction.WEST || direction == Direction.SOUTHWEST ||
					direction == Direction.NORTHWEST){
				facing = Facing.LEFT;
			}
		}
		else {
			if(direction == Direction.NORTH) facing = Facing.UP;
			else if(direction == Direction.EAST) facing = Facing.RIGHT;
			else if(direction == Direction.SOUTH) facing = Facing.DOWN;
			else if(direction == Direction.WEST) facing = Facing.LEFT;
		}
	}

	/**
	 * Called when the player decides to take a turn without moving.
	 * This will increase the player's minimum health by 2 hitpoints
	 * and trigger a world upate.  There is also a chance that
	 * a new enemy will spawn.
	 *
	 */
	public void pass(){
		// Heal the player.
		heal(2);

		GameWorld.getInstance().triggerWorldUpdate();

		if(hitPoints == maxHitPoints) return; // Don't spawn new enemies if didn't heal.

		// If in dungeon, it will be twice as likely that something will spawn
		// because chance to spawn was also called in the world update.  This is
		// fine, cause resting in a dungeon should be a little less safe.
		EnemyMovement.chanceToSpawnEnemy(getRow(), getCol(), getLevel(), getInDungeon());
	}

	/**
	 * Player will attack the enemy specified.
	 * @param enemy The enemy to attack.
	 */
	public void attack(Enemy enemy) throws EnemyDiedException {
		try {
			isShooting = false;
			Battles.fight(this, enemy);
		} catch(EnemyDiedException e){
			throw new EnemyDiedException();
		}
	}

	/**
	 * Drains the life of the player.  If the player dies, end the game.
	 * @param num The number of hitpoints the player's health should drop by.
	 */
	public void drainLife(int num){
		hitPoints -= num;
		
		// Check to see if auto bandaid should take effect.
		if(hitPoints <= 0 && TheGame.getInstance().getController().getAutoBandaids()){
			if(numBandaids > 0){
				numBandaids--;
				hitPoints = maxHitPoints - num;
				Messages.getInstance().addMessage("You use a bandaid.");
			}
		}

		if(hitPoints <= 0){ // Player died
			isDead = true;
			Messages.getInstance().addMessage("You have died.  Better luck next time.");
			TheGame.getInstance().setRunning(false);
			src.enums.Sounds.PLAYER_DIES.playSound();
		}
		else {
			// Change image to a just hit image if not in a dungeon.
			// Otherwise, play a sound.
			if(inDungeon){
				src.enums.Sounds.PLAYER_HIT_DUN.playSound();
			}
			else {
				justHit = true;
				hitCounter = Constants.HIT_COUNTER;
				hitImage = HitImages.HIT1.getRandomHitImage();
				
				src.enums.Sounds.PLAYER_HIT.playSound();
			}
		}
	}

	/**
	 * Heals the player the desired amount as long as the minimum
	 * hit points are below the maximum.
	 * @param amount Number of hit points to heal the player by.
	 */
	public void heal(int amount){
		if(hitPoints + amount > maxHitPoints)
			hitPoints = maxHitPoints;
		else hitPoints += amount;
	}

	/**
	 * Adds the desired number of points the the ability the player
	 * used to kill the enemy.
	 * @param num The number of ability points to add.
	 */
	public void addAbilityPoints(int num){
		getReadiedWeapon().getDamageType().addAbilityPoints(this, num);
	}

	/**
	 * Adds the specified number of fighting ability points to the player.
	 * @param num int : number of ability points to add.
	 */
	public void addFightingAbility(int num){
		fightingAbility += num;
	}

	/**
	 * Adds the specified number of martial arts ability points to the player.
	 * @param num int : number of ability points to add.
	 */
	public void addMartialArtsAbility(int num){
		martialArtsAbility += num;
	}

	/**
	 * Adds the specified number of marksmanship ability points to the player.
	 * @param num int : number of ability points to add.
	 */
	public void addMarksmanAbility(int num){
		marksmanAbility += num;
	}

	/**
	 * Adds to the player's money.
	 * @param money The amount of money to add.
	 */
	public void addMoney(int money){
		if(this.money >= InventoryLimits.MONEY.getLimit()) return;
		this.money += money;
		if(this.money > InventoryLimits.MONEY.getLimit())
			this.money = InventoryLimits.MONEY.getLimit();
	}

	/**
	 * Removes from the player's money if the player has enough money.
	 * It will return based on whether or not the player has enough money.
	 * This method is used for purchasing items at shops.
	 * @param mo The amount of money to remove.
	 * @return boolean : whether or not the money could be removed.
	 */
	public boolean removeMoney(int mo){
		if(money - mo >= 0){
			money -= mo;
			return true;
		} else return false;
	}

	/**
	 * Removes all money from the player's inventory.
	 */
	public void removeMoney(){
		money = 0;
	}
	
	/**
	 * Removes the specified number of ladders up from the player's
	 * inventory.
	 * @param num int : number to remove
	 */
	public void removeLadderUp(int num){
		numLaddersUp -= num;
	}
	
	/**
	 * Removes the specified number of ladders down from the player's
	 * inventory.
	 * @param num int : number to remove
	 */
	public void removeLadderDown(int num){
		numLaddersDown -= num;
	}

	/**
	 * Adds the armor to the player's inventory.
	 * @param armor Armor
	 */
	public void addArmor(Armor armor){
		try {
			availableArmor[armor.getType()] = true;
		} catch(Exception ex) {}
	}

	/**
	 * Removes the armor from the player's inventory.
	 * @param armor Armor
	 */
	public void removeArmor(Armor armor){
		try {
			availableArmor[armor.getType()] = false;
		} catch(Exception ex) {}
	}

	/**
	 * Adds the weapon to the player's inventory.
	 * @param weapon Weapon
	 */
	public void addWeapon(Weapon weapon, int level){
		if(level > getLevel()) level = getLevel();

		try {
			if(weaponLevels[weapon.getType()] < level)
				weaponLevels[weapon.getType()] = level;
		} catch(Exception ex){
			//ex.printStackTrace();
		}
	}

	/**
	 * Removes the weapon from the player's inventory.
	 * @param weapon Weapon
	 */
	public void removeWeapon(Weapon weapon){
		try {
			weaponLevels[weapon.getType()] = 0;
		} catch(Exception ex) {}
	}

	/**
	 * Returns the level of weapon the player has of the specified
	 * type of weapon.
	 * @return Weapon level as an int.
	 */
	public int getWeaponLevel(Weapon weapon){
		try {
			return weaponLevels[weapon.getType()];
		} catch(Exception ex){
			return 0;
		}
	}

	/**
	 * Returns the image location of the player.
	 * @return Image location as a string.
	 */
	public String getImage(){
		if(isDead){
			return PlayerImages.DEAD.getLeftImage();
		}
		if(justRecoveredFromHit){
			justRecoveredFromHit = false;
			return GameWorld.getInstance().getLandAt(getRow(), getCol()).getLandImage();
		}
		if(!justHit){
			if(facing == Facing.LEFT)
				return PlayerImages.values()[getReadiedWeapon().getType()].getLeftImage();
			else return PlayerImages.values()[getReadiedWeapon().getType()].getRightImage();
		}
		else {
			return hitImage;
		}
	}

	/**
	 * Calculates the player's level based on health and returns the value.
	 * @return Player level as an int.
	 */
	public int getLevel(){
		int level = maxHitPoints / 1000;
		if(level < 1) level = 1;
		return level;
	}

	/**
	 * Accessor for the isDead variable.
	 * @return Whether or not the player is dead as a boolean.
	 */
	public boolean getIsDead(){
		return isDead;
	}

	/* Attribute Accessors */

	/**
	 * Accessor for the name of the player.
	 * @return Name of the player as a string.
	 */
	public String getName(){
		return playerName;
	}

	/**
	 * Accessor for the hit points of the player.
	 * @return Hit points as an int.
	 */
	public int getHitPoints(){
		return hitPoints;
	}

	/**
	 * Accessor for maximum hit points.
	 * @return Max hit points as an int.
	 */
	public int getMaxHitPoints(){
		return maxHitPoints;
	}

	/**
	 * Accessor for fighting ability.
	 * @return Fighting ability as an int.
	 */
	public int getFightingAbility(){
		return fightingAbility;
	}

	/**
	 * Accessor for marksmanship ability.
	 * @return Marksmanship ability as an int.
	 */
	public int getMarksmanAbility(){
		return marksmanAbility;
	}

	/**
	 * Accessor for martial arts ability.
	 * @return Martial Arts ability as an int.
	 */
	public int getMartialArtsAbility(){
		return martialArtsAbility;
	}

	/**
	 * Accessor for thieving ability.
	 * @return Thieving ability as an int.
	 */
	public int getThievingAbility(){
		return thievingAbility;
	}

	/* Inventory Accessors */

	/**
	 * Accessor for money.
	 * @return Amount of money as an int.
	 */
	public int getMoney(){
		return money;
	}

	/**
	 * Accessor for bandaids.
	 * @return Number of bandaids as an int.
	 */
	public int getNumBandaids(){
		return numBandaids;
	}

	/**
	 * Accessor for grenades.
	 * @return Number of bandaids as an int.
	 */
	public int getNumGrenades(){
		return numGrenades;
	}

	/**
	 * Accessor for dynamite.
	 * @return Amount of dynamite as an int.
	 */
	public int getNumDynamite(){
		return numDynamite;
	}

	/**
	 * Accessor for bullets.
	 * @return Number of bullets as an int.
	 */
	public int getNumBullets(){
		return numBullets;
	}

	/**
	 * Accessor for rockets.
	 * @return Number of rockets as an int.
	 */
	public int getNumRockets(){
		return numRockets;
	}

	/**
	 * Accessor for flame packs.
	 * @return Number of flame packs as an int.
	 */
	public int getNumFlamePacks(){
		return numFlamePacks;
	}

	/**
	 * Accessor for whether or not the player is in a dungeon.
	 * @return boolean
	 */
	public boolean getInDungeon(){
		return inDungeon;
	}

	/**
	 * Sets whether or not the player is in a dungeon.
	 * @param bool boolean : in dungeon
	 */
	public void setInDungeon(boolean bool){
		inDungeon = bool;
	}

	/**
	 * Accessor for this player's dungeon level.
	 * @return int : dungeon level
	 */
	public int getDungeonLevel(){
		return dungeonLevel;
	}

	/**
	 * Sets the dungeon level of this player.
	 * @param level int : dungeon level
	 */
	public void setDungeonLevel(int level){
		dungeonLevel = level;
	}

	/**
	 * Accessor for the hitCounter.
	 * @return hitCounter as an int.
	 */
	public int getHitCounter(){
		return hitCounter;
	}

	/**
	 * If the hit counter is currently active, it will decrease it
	 * by one.  If after the decrease, the hit counter is 0, it will
	 * set the justHit variable to false.
	 *
	 */
	public void decrementHitCounter(){
		if(hitCounter > 0){
			hitCounter--;
			if(hitCounter <= 0){
				justHit = false;
				justRecoveredFromHit = true;
			}
		}
	}

	/**
	 * Accessor for the available armor.
	 * @return Available armor as a boolean array.
	 */
	public boolean[] getAvailableArmor(){
		return availableArmor;
	}

	/**
	 * Mutator method for readied weapon.
	 * @param w The weapon to be readied.
	 */
	public void setReadiedWeapon(Weapon w){
		readiedWeaponIndex = w.getType();
	}

	/**
	 * Mutator method for readied armor.
	 * @param a The armor to be readied.
	 */
	public void setReadiedArmor(Armor a){
		readiedArmorIndex = a.getType();
	}

	/**
	 * Accessor for readied weapon.
	 * @return The readied weapon.
	 */
	public Weapon getReadiedWeapon(){
		return weapons[readiedWeaponIndex];
	}

	/**
	 * Gets the weapon levels.
	 * @return weaponLevels as an int array.
	 */
	public int[] getWeaponLevels(){
		return weaponLevels;
	}

	/**
	 * Accessor for name of readied weapon.  Used to identify which player picture
	 * to use for the player.
	 * @return Name of the weapon part of the image file the the player should
	 * currently be using.
	 */
	/*public String getReadiedWeaponName(){
		return Weapon.values()[readiedWeaponIndex].getName();
	}*/

	/**
	 * Returns the player's ability with the weapon the player is currently
	 * using.
	 * @return Ability as an int.
	 */
	public int getCurrentAbility(){
		return getReadiedWeapon().getDamageType().getCurrentAbility(this);
	}

	/**
	 * Resets the weapon levels to 1.
	 */
	/*public void resetWeaponLevels(){
		for(int i = 0; i < weaponLevels.length; i++){
			weaponLevels[i] = 0;
		}
	}*/

	/**
	 * Accessor for readied armor.
	 * @return Index of readied armor.
	 */
	public Armor getReadiedArmor(){
		return armor[readiedArmorIndex];
	}

	/**
	 * Accessor for the direction the player is facing.
	 * @return Facing : facing direction
	 */
	public Facing getFacing(){
		return facing;
	}

	/**
	 * Accessor for this player's vehicle.
	 * @return VehicleType : type of vehicle
	 */
	public VehicleType getVehicle(){
		return vehicle;
	}

	/**
	 * Accessor for the player's row.
	 * @return Row as an int.
	 */
	public int getRow(){
		return row;
	}

	/**
	 * Accessor for the player's column.
	 * @return Column as an int.
	 */
	public int getCol(){
		return col;
	}

	/**
	 * Mutator for the player's row.
	 * @param row Row in the world as an int.
	 */
	public void setRow(int row){
		this.row = row;
	}

	/**
	 * Mutator for the player's column.
	 * @param col Column in the world as an int.
	 */
	public void setCol(int col){
		this.col = col;
	}

	/**
	 * Accessor for whether the player is currently caught stealing.
	 * @return boolean
	 */
	public boolean getCaughtStealing(){
		return caughtStealing;
	}

	/**
	 * Mutator for whether the player is currently caught stealing.
	 * @param bool Whether or not the player is stealing as a boolean.
	 */
	public void setCaughtStealing(boolean bool){
		caughtStealing = bool;
	}

	/**
	 * Mutator for the justHit variable.
	 * @param bool Whether the player was just hit as a boolean.
	 */
	public void setJustHit(boolean bool){
		justHit = bool;
	}

	/**
	 * Adds the specified number of bullets to the player's inventory.
	 *
	 */
	public boolean addBullets(int num){
		if(this.numBullets >= InventoryLimits.BULLETS.getLimit()) return false;
		numBullets += num;
		if(numBullets > InventoryLimits.BULLETS.getLimit())
			numBullets = InventoryLimits.BULLETS.getLimit();
		return true;
	}

	/**
	 * Adds the specified number of rockets to the player's inventory.
	 *
	 */
	public boolean addRockets(int num){
		if(this.numRockets >= InventoryLimits.ROCKETS.getLimit()) return false;
		numRockets += num;
		if(numRockets > InventoryLimits.ROCKETS.getLimit())
			numRockets = InventoryLimits.ROCKETS.getLimit();
		return true;
	}

	/**
	 * Adds 5 flame packs to the player's inventory.
	 *
	 */
	public boolean addFlamePacks(int num){
		if(this.numFlamePacks >= InventoryLimits.FLAME_PACKS.getLimit()) return false;
		numFlamePacks += num;
		if(numFlamePacks > InventoryLimits.FLAME_PACKS.getLimit())
			numFlamePacks = InventoryLimits.FLAME_PACKS.getLimit();
		return true;
	}

	/**
	 * Adds one bandaid to the player's inventory.
	 *
	 */
	public boolean addBandaid(int num){
		if(this.numBandaids >= InventoryLimits.BANDAIDS.getLimit()) return false;
		numBandaids += num;
		if(numBandaids > InventoryLimits.BANDAIDS.getLimit())
			numBandaids = InventoryLimits.BANDAIDS.getLimit();
		return true;
	}

	/**
	 * Adds the specified number of grenades to the player's inventory.
	 * @param num int : number of grenades to add
	 * @return boolean : whether or not the grenades were added
	 */
	public boolean addGrenade(int num){
		if(this.numGrenades >= InventoryLimits.GRENADES.getLimit()) return false;
		numGrenades += num;
		if(numGrenades > InventoryLimits.GRENADES.getLimit())
			numGrenades = InventoryLimits.GRENADES.getLimit();
		return false;
	}

	/**
	 * Adds the specified number of dynamite sticks to the player's inventory.
	 * @param num int : number of sticks of dynamite
	 * @return boolean : whether or not the dynamite was added
	 */
	public boolean addDynamite(int num) {
		if(this.numDynamite >= InventoryLimits.DYNAMITE.getLimit()) return false;
		numDynamite += num;
		if(numDynamite > InventoryLimits.DYNAMITE.getLimit())
			numDynamite = InventoryLimits.DYNAMITE.getLimit();
		return true;
	}

	/**
	 * Adds the specified number of ladders up to the player's inventory.
	 * @param num int : number of ladders up
	 */
	public boolean addLadderUp(int num){
		if(this.numLaddersUp >= InventoryLimits.LADDER_UP.getLimit()) return false;
		numLaddersUp += num;
		if(numLaddersUp > InventoryLimits.LADDER_UP.getLimit())
			numLaddersUp = InventoryLimits.LADDER_UP.getLimit();
		return true;
	}

	/**
	 * Accessor for number of ladders up in the player's inventory.
	 * @return int : number of ladders up
	 */
	public int getNumLaddersUp(){
		return numLaddersUp;
	}

	/**
	 * Adds the specified number of ladders down to the player's inventory.
	 * @param num int : number of ladders down
	 */
	public boolean addLadderDown(int num){
		if(this.numLaddersDown >= InventoryLimits.LADDER_DOWN.getLimit()) return false;
		numLaddersDown += num;
		if(numLaddersDown > InventoryLimits.LADDER_DOWN.getLimit())
			numLaddersDown = InventoryLimits.LADDER_DOWN.getLimit();
		return true;
	}

	/**
	 * Accessor for number of ladders down in the player's inventory.
	 * @return int : number of ladders down
	 */
	public int getNumLaddersDown(){
		return numLaddersDown;
	}
	
	/**
	 * Adds the specified number of map viewers to the player's inventory
	 * as long as it does not go over the limit the player can carry.
	 * @param num int : amount to add
	 */
	public void addMapViewer(int num){
		if(this.numMapViewers >= InventoryLimits.MAP_VIEWERS.getLimit()) return;
		numMapViewers += num;
		if(numMapViewers > InventoryLimits.MAP_VIEWERS.getLimit())
			numMapViewers = InventoryLimits.MAP_VIEWERS.getLimit();
	}
	
	/**
	 * Adds the specified number of exports to the player's inventory
	 * as long as it does not go over thel imit the player can carry.
	 * @param num int : amount to add
	 */
	public void addExports(int num){
		if(this.numExports >= InventoryLimits.EXPORTS.getLimit()) return;
		numExports += num;
		if(numExports > InventoryLimits.EXPORTS.getLimit())
			numExports = InventoryLimits.EXPORTS.getLimit();
	}
	
	/**
	 * Removes the specified number of map viewers from the player's inventory.
	 * @param num int : number to remove
	 */
	public void removeMapViewer(int num){
		numMapViewers -= num;
	}
	
	/**
	 * Gets the number of map viewers in the player's inventory.
	 * @return int : number of map viewers
	 */
	public int getNumMapViewers(){
		return numMapViewers;
	}
	
	/**
	 * Removes the specified number of exports from the player's inventory.
	 * @param num int : number to remove
	 */
	public void removeExport(int num){
		numExports -= num;
		if(numExports < 0) numExports = 0;
	}
	
	/**
	 * Gets the number of exports in the player's inventory.
	 * @return int : number of exports
	 */
	public int getNumExports(){
		return numExports;
	}

	/**
	 * Adds desired number of maximum hitpoints to the player.
	 * @param num Amount of hit points as an int.
	 */
	public void addMaxHP(int num){
		maxHitPoints += num;
	}

	/**
	 * Adds one or two thieving ability points and prints a message to the GUI.
	 *
	 */
	public void addThievingPoints(){
		int num;
		if(thievingAbility > 100 * getLevel()) return;
		if(Math.random() < .5){
			thievingAbility+=4;
			num = 4;
		}
		else {
			thievingAbility += 3;
			num = 3;
		}
		Messages.getInstance().addMessage(
				"You have gained " + num + " thieving ability points.");
		//Messages.addMessage("You have gained " + num + " thieving ability points.");
	}

	/**
	 * Accessor for whether the player is shooting.
	 * @return Shooting as a boolean.
	 */
	public boolean getIsShooting(){
		return isShooting;
	}

	/**
	 * Sets whether the player is shooting.
	 * @param shoot Whether the player is shooting as a boolean.
	 */
	public void setIsShooting(boolean shoot){
		isShooting = shoot;
	}

	/**
	 * Removes the desired number of bandaids from the player's inventory.
	 * @param num The number of bandaids to remove.
	 */
	public void removeBandaid(int num){
		numBandaids -= num;
	}

	/**
	 * Removes the desired number of grenades from the player's inventory.
	 * @param num The number of grenades to remove.
	 */
	public void removeGrenade(int num){
		numGrenades -= num;
	}

	/**
	 * Removes the desired number of sticks of dynamite from the player's inventory.
	 * @param num The number of sticks of dynamite to remove.
	 */
	public void removeDynamite(int num){
		numDynamite -= num;
	}

	/**
	 * Removes the specified number of rockets.
	 * @param num Number of rockets to remove as an int.
	 */
	public void removeRockets(int num){
		numRockets -= num;
	}

	/**
	 * Removes the specified number of bullets.
	 * @param num Number of bullets to remove as an int.
	 */
	public void removeBullets(int num){
		numBullets -= num;
	}

	/**
	 * Removes the specified number of flame packs.
	 * @param num int : number to remove
	 */
	public void removeFlamePacks(int num){
		numFlamePacks -= num;
	}

	/**
	 * Sets the facing direction of the player.
	 * @param f Facing : The direction
	 */
	public void setFacing(Facing f){
		facing = f;
	}

	/**
	 * Sets the maximum hit points of the player.
	 * @param num Number of hit points as an int.
	 */
	public void setMaxHP(int num){
		maxHitPoints = num;
	}

	/**
	 * Sets the number of actual hit points of the player.
	 * @param num Number of hit points as an int.
	 */
	public void setHP(int num){
		hitPoints = num;
	}

	/**
	 * Sets the fighting ability of the player.
	 * @param num Fighting ability as an int.
	 */
	public void setFightingAbil(int num){
		fightingAbility = num;
	}

	/**
	 * Sets the marksmanship ability of the player.
	 * @param num Marksmanship ability as an int.
	 */
	public void setMarksmanshipAbil(int num){
		marksmanAbility = num;
	}

	/**
	 * Sets the martial arts ability of the player.
	 * @param num Martial arts ability as an int.
	 */
	public void setMartialArtsAbil(int num){
		martialArtsAbility = num;
	}

	/**
	 * Sets the thieving ability of the player.
	 * @param num Thieving ability as an int.
	 */
	public void setThievingAbil(int num){
		thievingAbility = num;
	}

	/**
	 * Sets the money of the player.
	 * @param num Money as an int.
	 */
	public void setMoney(int num){
		money = num;
	}

	/**
	 * Sets the players name.
	 * @param name Player name as a string.
	 */
	public void setName(String name){
		this.playerName = name;
	}

	/**
	 * Accessor for this player's index.
	 * @return int : index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets this player's index.
	 * @param index int : index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Saves the player to the output stream.
	 * @param oos ObjectOutputStream
	 */
	public void save(ObjectOutputStream oos){
		try{
			// Player Attributes
			oos.writeObject(row);
			oos.writeObject(col);
			oos.writeObject(index);
			oos.writeObject(playerName);
			oos.writeObject(hitPoints);
			oos.writeObject(maxHitPoints);
			oos.writeObject(fightingAbility);
			oos.writeObject(marksmanAbility);
			oos.writeObject(martialArtsAbility);
			oos.writeObject(thievingAbility);
			oos.writeObject(money);
			oos.writeObject(facing);

			// Player Inventory
			oos.writeObject(availableArmor);
			oos.writeObject(weaponLevels);

			oos.writeObject(numBandaids);
			oos.writeObject(numGrenades);
			oos.writeObject(numDynamite);
			oos.writeObject(numBullets);
			oos.writeObject(numRockets);
			oos.writeObject(numFlamePacks);
			oos.writeObject(numLaddersUp);
			oos.writeObject(numLaddersDown);
			oos.writeObject(numMapViewers);
			oos.writeObject(numExports);

			oos.writeObject(readiedWeaponIndex);
			oos.writeObject(readiedArmorIndex);

			oos.writeObject(caughtStealing);
			oos.writeObject(isDead);
			oos.writeObject(inDungeon);
			oos.writeObject(dungeonLevel);
			oos.writeObject(dungeonPath);
			
			oos.writeObject(vehicle); // Not used yet

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads the player from the input stream.
	 * @param ois ObjectInputStream : Input stream
	 */
	@SuppressWarnings("unchecked")
	public void load(ObjectInputStream ois){
		try {
			// Player Attributes
			row = (Integer) ois.readObject();
			col = (Integer) ois.readObject();
			index = (Integer) ois.readObject();
			playerName = (String) ois.readObject();
			hitPoints = (Integer) ois.readObject();
			maxHitPoints = (Integer) ois.readObject();
			fightingAbility = (Integer) ois.readObject();
			marksmanAbility = (Integer) ois.readObject();
			martialArtsAbility = (Integer) ois.readObject();
			thievingAbility = (Integer) ois.readObject();
			money = (Integer) ois.readObject();
			facing = ((Facing) ois.readObject());

			// Player Inventory
			availableArmor = (boolean[]) ois.readObject();
			weaponLevels = (int[]) ois.readObject();

			numBandaids = (Integer) ois.readObject();
			numGrenades = (Integer) ois.readObject();
			numDynamite = (Integer) ois.readObject();
			numBullets = (Integer) ois.readObject();
			numRockets = (Integer) ois.readObject();
			numFlamePacks = (Integer) ois.readObject();
			numLaddersUp = (Integer) ois.readObject();
			numLaddersDown = (Integer) ois.readObject();
			numMapViewers = (Integer) ois.readObject();
			numExports = (Integer) ois.readObject();

			readiedWeaponIndex = (Integer) ois.readObject();
			readiedArmorIndex = (Integer) ois.readObject();

			caughtStealing = (Boolean) ois.readObject();
			isDead = (Boolean) ois.readObject();
			inDungeon = (Boolean) ois.readObject();
			dungeonLevel = (Integer) ois.readObject();
			dungeonPath = (Stack<Integer>) ois.readObject();
			
			vehicle = (VehicleType) ois.readObject();
			
		} catch(Exception e) {}
	}

}
