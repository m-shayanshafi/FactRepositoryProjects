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

package src.land;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import src.enums.LandType;
import src.enums.ObstructionLandType;
import src.game.Item;
import src.game.Enemy;
import src.game.NPC;
import src.game.Player;

/**
 * Obstruction land object.  This is a land object that no player or enemy
 * can pass through.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Obstruction implements Land{
	
	private ObstructionLandType type;
	private boolean canBeDestroyed;
	private boolean hasBeenExplored;
	
	/**
	 * Constructor for the Obstruction.  You can specify what type of obstruction
	 * it is and whether or not it can be destroyed.
	 * @param type ObstructionLandType : type of obstruction.
	 * @param destroy Boolean. True means the wall can be destroyed.
	 */
	public Obstruction(ObstructionLandType type, boolean destroy){
		this.type = type;
		this.canBeDestroyed = destroy;
		hasBeenExplored = false;
	}
	
	/**
	 * Returns the land type "obstruction".
	 * @return LandType : obstruction.
	 */
	public int getLandType(){
		return LandType.OBSTRUCTION.getTypeNum();
	}
	
	/**
	 * Accessor for the type of obstruction.
	 * @return ObstructionLandType
	 */
	public ObstructionLandType getType(){
		return type;
	}
	
	/**
	 * The player cannot enter obstruction objects.
	 */
	public boolean canEnter(){
		return false;
	}
	
	/**
	 * Does nothing for Obstruction land objects.
	 */
	public void setPlayer(int plr) { }
	
	/**
	 * Todo:
	 * Throw a CannotMoveHere exception.
	 */
	public void enter(Player plr) {}
	
	/**
	 * Enemy tries to enter this land object.
	 * @return boolean : successfully entered
	 */
	public boolean enter(Enemy enemy){
		return false;
	}
	
	/**
	 * Used to get the image for the obstruction land object.
	 * @return String : image location
	 */
	public String getImage(){
		return type.getImage();
	}
	
	/**
	 * The same as getImage for obstruction land types.
	 * @return String : image location
	 */
	public String getLandImage(){
		return getImage();
	}

	/**
	 * Returns false since the player can never be on an obstruction.
	 */
	public int hasPlayer(){
		return -1;
	}
	
	/**
	 * Meaningless for obstruction land objects.
	 */
	public void addEnemy(Enemy enemy) {}
	
	/**
	 * Meaningless for obstruction land objects.
	 */
	public void addItem(Item item) {}
	
	/**
	 * Will always return null for obstructions.
	 */
	public Enemy getEnemy(){
		return null;
	}
	
	/**
	 * Will always return null for obstructions.
	 */
	public NPC getNPC(){
		return null;
	}
	
	/**
	 * Always returns null for obstructions.
	 */
	public Item getItem(){
		return null;
	}
	
	/**
	 * Does nothing since enemies can't exist in obstructions.
	 */
	public void setEnemy(Enemy enemy) {}
	
	/**
	 * Does nothing for obstruction objects.
	 */
	public void setNPC(NPC npc) {}
	
	/**
	 * Does nothing, since items can't exist in obstructions.
	 */
	public void setItem(Item item) {}
	
	/**
	 * obstructions will never be accessible to enemies.
	 */
	public boolean isEnemyAccessible(){
		return false;
	}
	
	/**
	 * Accessor whether or not the obstruction can be destroyed.
	 * @return True means the obstruction can be destroyed.
	 */
	public boolean isDestroyable(){
		return canBeDestroyed && type.getIsPossiblyDestroyed();
	}
	
	/**
	 * Sets whether this obstruction is destroyable or not.
	 * @param bool boolean : destroyable
	 */
	public void setIsDestroyable(boolean bool){
		canBeDestroyed = bool;
	}
	
	/**
	 * Sets whether or not this object has been explored.
	 * @param bool Boolean
	 */
	public void setHasBeenExplored(boolean bool){
		hasBeenExplored = bool;
	}
	
	/**
	 * Accessor for whether or not this object has been explored.
	 */
	public boolean hasBeenExplored(){
		return hasBeenExplored;
	}
	
	/**
	 * Obstructions stop projectiles.
	 */
	public boolean stopsProjectiles(){
		return true;
	}
	
	/**
	 * Writes this land object to the output stream.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(type);
			oos.writeObject(canBeDestroyed);
			oos.writeObject(hasBeenExplored);
		} catch(Exception e) {}
	}
	
	/**
	 * Loads this land object from the input stream.
	 */
	public void load(ObjectInputStream ois){
		try{
			type = (ObstructionLandType) ois.readObject();
			canBeDestroyed = (Boolean) ois.readObject();
			hasBeenExplored = (Boolean) ois.readObject();
		} catch(Exception e) {}
	}
}
