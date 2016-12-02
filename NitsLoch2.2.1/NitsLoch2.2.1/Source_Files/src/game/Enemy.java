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

import src.Constants;
import src.enums.Armor;
import src.enums.Direction;
import src.enums.Enemies;
import src.enums.EnemyBehavior;
import src.enums.Facing;
import src.enums.HitImages;
import src.enums.StreetType;
import src.enums.Weapon;
import src.land.Street;
import src.scenario.MiscScenarioData;

/**
 * This is the class for enemy objects in the world.  Will keep track of the
 * current stats of the enemy and the methods that can be performed on the
 * enemy.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Enemy implements Serializable {

	private static final long serialVersionUID = Constants.serialVersionUID;

	private Enemies type;

	private int row, col;
	private int hitPoints;
	private int money;
	private Facing facing;
	private boolean hasBeenAttacked;
	private int advanced;

	private boolean isRunning;
	private boolean justHit;
	private boolean justRecoveredFromHit;
	private boolean stunned;
	private int hitCounter;
	private String hitImage;
	
	private StreetType startingLand;

	/**
	 * Constructor for the enemy.
	 * @param type Enemies : type of enemy
	 * @param row int : Row in the world
	 * @param col int : column in the world
	 * @param advanced int : enemy's level
	 */
	public Enemy(Enemies type, int row, int col, int advanced){
		this.row = row;
		this.col = col;
		this.type = type;

		justHit = false;
		justRecoveredFromHit = false;
		hitCounter = 0;
		hitImage = "";

		money = type.getRandomMoney();
		hitPoints = type.getMaxHitPoints();

		facing = Facing.RIGHT;

		if(type.getBehavior() == EnemyBehavior.COWARDLY ||
				type.getBehavior() == EnemyBehavior.DECEPTIVE)
			isRunning = true;
		else isRunning = false;
		
		this.advanced = advanced;

		//hitPoints *= advanced;
		hitPoints = hitPoints + (int)(hitPoints * 
				(MiscScenarioData.HEALTH_SCALE * (advanced - 1)));
		
		money *= advanced;

		hasBeenAttacked = false;
		stunned = false;
		
		if(GameWorld.getInstance().getLandAt(row, col) instanceof Street)
			startingLand = ((Street)GameWorld.getInstance().getLandAt(row, col)).getType();
		else startingLand = null;

	}

	/**
	 * Accessor for the type of enemy.
	 * @return Enemies : type of enemy.
	 */
	public Enemies getType(){
		return type;
	}

	/**
	 * Activates the enemy.  The enemy will then move or attack.  This is
	 * called on all enemies in range of a player.
	 */
	public void activate(){
		EnemyMovement.activate(this);
	}
	
	/**
	 * Mutator for enemy stunned.
	 * @param bool boolean : stunned
	 */
	public void setStunned(boolean bool){
		stunned = bool;
	}
	
	/**
	 * Accessor for enemy stunned.
	 * @return boolean : stunned
	 */
	public boolean getStunned(){
		return stunned;
	}

	/**
	 * Calls the move method in enemy movement
	 * @param direction The direction to move
	 */
	public void move(Direction direction){
		EnemyMovement.move(this, direction);
	}

	/**
	 * Calls the moveDungeon method in enemy movement
	 * @param direction Direction : the direction to move.
	 */
	public void moveDungeon(Direction direction){
		EnemyMovement.moveDungeon(this, direction);
	}

	/**
	 * Starts a fight between this enemy and a player.
	 * @param plr Player : the player
	 */
	public void attack(Player plr){
		Battles.fight(this, plr);
	}

	/**
	 * Gets the image location of this enemy.
	 * @return String : path to image
	 */
	public String getImage(){
		if(justRecoveredFromHit){
			justRecoveredFromHit = false;
			return GameWorld.getInstance().getLandAt(getRow(), getCol()).getLandImage();
		}
		if(!justHit){
			if(TheGame.getInstance().getInDungeon())
				return type.getDungeonImage();
			else if(facing == Facing.LEFT)
				return type.getLeftImage();
			else return type.getRightImage();
		}
		else {
			return hitImage;
		}
	}

	/**
	 * Drains the life of the enemy.
	 * @param amount int : damage amount
	 * @return boolean : whether or not the enemy died.
	 */
	public boolean drainLife(int amount){
		hitPoints -= amount;

		if(hitPoints <= 0){ // Enemy dies
			src.enums.Sounds.ENEMY_DIES.playSound();
			return true;
		}
		else {
			// Change image to a just hit image if the player is not in a dungeon.
			// Otherwise, play a sound.
			if(GameWorld.getInstance().getClosestPlayer(row, col).getInDungeon()){
				src.enums.Sounds.ENEMY_HIT_DUN.playSound();
			}
			else {
				justHit = true;
				hitCounter = src.Constants.HIT_COUNTER;
				hitImage = HitImages.HIT1.getRandomHitImage();
				
				src.enums.Sounds.ENEMY_HIT.playSound();
			}
		}

		return false;
	}

	/**
	 * Accessor for the enemy's row.
	 * @return int : row
	 */
	public int getRow(){
		return row;
	}

	/**
	 * Accessor for the enemy's column.
	 * @return int : column
	 */
	public int getCol(){
		return col;
	}

	/**
	 * Accessor for the enemy's hit points.
	 * @return Number of hit points as an int.
	 */
	public int getHitPoints(){
		return hitPoints;
	}

	/**
	 * Accessor for the enemy's fighting ability.
	 * @return Ability as an int.
	 */
	public int getAbility(){
		int ability = type.getAbility();
		ability = ability + (int)(ability * (MiscScenarioData.ABILITY_SCALE * (advanced-1)));
		return ability;
	}

	/**
	 * Accessor for the enemy's money.
	 * @return Amount of money as an int.
	 */
	public int getMoney(){
		return money;
	}

	/**
	 * Accessor for whether the enemy was just hit.  Used to determine which
	 * picture should be displayed for the enemy.
	 * @return Whether the enemy was just hit as a boolean.
	 */
	public boolean getJustHit(){
		return justHit;
	}

	/**
	 * Accessor for the enemy's facing direction.
	 * @return Character based on which direction the enemy is facing.
	 * Can be either 'L' or 'R'.
	 */
	public Facing getFacing(){
		return facing;
	}

	/**
	 * Accessor for whether the enemy has been attacked.  Needed for enemies
	 * that will not attack on sight.
	 * @return Whether enemy has been attacked as a boolean.
	 */
	public boolean getHasBeenAttacked(){
		return hasBeenAttacked;
	}

	/**
	 * Accessor for the enemy's weapon.
	 * @return Weapon
	 */
	public Weapon getWeapon(){
		return type.getWeapon();
	}

	/**
	 * Accessor for the enemy's armor.
	 * @return Armor
	 */
	public Armor getArmor(){
		return type.getArmor();
	}

	/**
	 * Accessor for the enemy's name.
	 * @return Enemy's name as a string.
	 */
	public String getName(){
		return type.getName();
	}

	/**
	 * Accessor for whether or not this enemy is a thief.
	 * @return boolean : is thief
	 */
	public boolean isThief(){
		return type.getIsThief();
	}

	/**
	 * Accessor for whether or not this enemy is a
	 * shopkeeper.
	 * @return boolean : is shopkeeper
	 */
	public boolean isShopkeeper(){
		return type.getIsShopkeeper();
	}
	
	/**
	 * Accessor for whether or not this enemy is a leader.
	 * @return boolean : is leader
	 */
	public boolean isLeader(){
		return type.getIsLeader();
	}

	/**
	 * Accessor for the hitCounter.
	 * @return hitCounter as an int.
	 */
	public int getHitCounter(){
		return hitCounter;
	}

	/**
	 * Accessor for whether the enemy is running.
	 * @return Running as a boolean.
	 */
	public boolean getIsRunning(){
		return isRunning;
	}

	/**
	 * Accessor for how advanced the enemy is.
	 * @return int : advanced level of the enemy.
	 */
	public int getAdvanced(){
		return advanced;
	}

	/**
	 * Sets the advanced level of the enemy.
	 * @param level int : level
	 */
	public void setLevel(int level){
		advanced = level;
	}

	/**
	 * Mutator for the enemy's row.
	 * @param row Row as an int.
	 */
	public void setRow(int row){
		this.row = row;
	}

	/**
	 * Mutator for enemy's column.
	 * @param col Column as an int.
	 */
	public void setCol(int col){
		this.col = col;
	}

	/**
	 * Mutator for hit points.
	 * @param hp Hit points as an int.
	 */
	public void setHitPoints(int hp){
		this.hitPoints = hp;
	}

	/**
	 * Mutator for money
	 * @param money Amount of money as an int.
	 */
	public void setMoney(int money){
		this.money = money;
	}

	/**
	 * Mutator for whether the enemy was just hit.
	 * @param jh Whether enemy was just hit as a boolean.
	 */
	public void setJustHit(boolean jh){
		this.justHit = jh;
	}

	/**
	 * Mutator for enemy's facing direction.
	 * @param fac Facing as a char.  Will be either 'L' or 'R'.
	 */
	public void setFacing(Facing fac){
		this.facing = fac;
	}

	/**
	 * Mutator for has been attacked.
	 * @param att Whether enemy has been attacked as an int.
	 */
	public void setHasBeenAttacked(boolean att){
		this.hasBeenAttacked = att;
	}

	/**
	 * Mutator for whether the enemy is running.
	 * @param bool Running as a boolean.
	 */
	public void setIsRunning(boolean bool){
		this.isRunning = bool;
	}
	
	/**
	 * Get the type of street this enemy started in.  Null if it was
	 * not a street
	 * @return StreetType : starting street type
	 */
	public StreetType getStartingLand() {
		return startingLand;
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
	 * Writes the enemy to the output stream.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(row);
			oos.writeObject(col);
			oos.writeObject(type);
			oos.writeObject(hitPoints);
			oos.writeObject(money);
			oos.writeObject(facing);
			oos.writeObject(hasBeenAttacked);
			oos.writeObject(advanced);
			oos.writeObject(isRunning);
		} catch(Exception e) {}
	}

	/**
	 * Loads the enemy from the input stream.
	 */
	public void load(ObjectInputStream ois){
		try{
			row = (Integer) ois.readObject();
			col = (Integer) ois.readObject();
			type = (Enemies) ois.readObject();
			hitPoints = (Integer) ois.readObject();
			money = (Integer) ois.readObject();
			facing = ((Facing) ois.readObject());
			hasBeenAttacked = (Boolean) ois.readObject();
			advanced = (Integer) ois.readObject();
			isRunning = (Boolean) ois.readObject();

		} catch(Exception e) {}
	}

}
