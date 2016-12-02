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

import src.game.Enemy;
import src.game.Item;
import src.game.NPC;

/**
 * An interface that has functions to make it easy for other parts of the
 * code to perform checks on and modify the land.  Outside, Shop, Street and
 * Wall are clases that implement the Land class.
 * @author Darren Watts
 * date 11/11/07
 */
public interface Land {
	
	/**
	 * Returns the type of the land object.  Must be implemented in
	 * subclasses.
	 * @return LandType : type of the land object.
	 */
	public int getLandType();
	
	/**
	 * This method is implemented in subclases and will return true if the
	 * land object allows for the player to enter it.
	 * @return Boolean indicating whether or not the player can enter the object.
	 */
	public boolean canEnter();
	
	/**
	 * The player attemps to enter this land object.  If possible, the
	 * player will enter successfully.  If not, an exception will be
	 * thrown.
	 * @param plr Player : the player trying to enter the land object.
	 */
	public void enter(src.game.Player plr) throws src.exceptions.PlayerEnteredException,
	src.exceptions.EnemyDiedException;
	
	/**
	 * An enemy attemps to enter this land object.  Will return true
	 * if the enemy successfully moved.
	 * @param enemy The enemy that's moving into the land object.
	 * @return boolean : successfully entered
	 */
	public boolean enter(src.game.Enemy enemy);
	
	/**
	 * Gets the image file name.
	 * @return Image file name as a String.
	 */
	public String getImage();
	
	/**
	 * Gets the image file name of the land object.  This will return
	 * the picture of the ground, instead of any object that is sitting
	 * on the land.
	 * @return String : image file name.
	 */
	public String getLandImage();
	
	/**
	 * This method reports if there is a player on this object.
	 * @return int : index of the player at this land object.  If there
	 * is not player, it will return -1.
	 */
	public int hasPlayer();
	
	/**
	 * Adds an enemy to this object.  Only valid if it's a street.
	 * @param enemy The enemy to add.
	 */
	public void addEnemy(Enemy enemy);
	
	/**
	 * Adds an item to this object.  Only valid if it's a street.
	 * @param item The item to add.
	 */
	public void addItem(Item item);
	
	/**
	 * Returns the enemy.  If no enemy exists, returns null.
	 * @return Enemy in this land object.
	 */
	public Enemy getEnemy();
	
	/**
	 * Returns the item.  If no item exists, returns null.
	 * @return Item in this land object.
	 */
	public Item getItem();
	
	/**
	 * Sets the enemy in this land object.
	 * @param enemy The enemy.
	 */
	public void setEnemy(Enemy enemy);
	
	/**
	 * Sets the NPC at this land object.
	 * @param npc NPC : the npc
	 */
	public void setNPC(NPC npc);
	
	/**
	 * Sets the player index at this land object.
	 * @param plr int : player index
	 */
	public void setPlayer(int plr);
	
	/**
	 * Accessor for the NPC at this land object.
	 * @return NPC : npc
	 */
	public NPC getNPC();
	
	/**
	 * Sets the item in this land object.
	 * @param item The item.
	 */
	public void setItem(Item item);
	
	/**
	 * Used to see if the land object can be entered by enemies.
	 * @return True if the land object can be entered by enemies.
	 */
	public boolean isEnemyAccessible();
	
	/**
	 * Used to see if the land object can be destroyed.
	 * @return boolean
	 */
	public boolean isDestroyable();
	
	/**
	 * Method to save this land object.
	 * @param oos ObjectOutputStream
	 */
	public void save(ObjectOutputStream oos);
	
	/**
	 * Method to load this land object.
	 * @param ois ObjectInputStream
	 */
	public void load(ObjectInputStream ois);
	
	/*
	 *  This method is needed only if you are reading from a serialized
	 *  map file.  As long as an actual map format exists, there won't
	 *  be any serialized map files.
	/**
	 * Method to load this land object from the map file.
	 * @param ois ObjectInputStream
	 *
	public void initLoad(ObjectInputStream ois);*/
	
	/**
	 * Keeps track of whether the land object has been explored or not.
	 * This is used for the overhead map.
	 * @return Boolean
	 */
	public boolean hasBeenExplored();
	
	/**
	 * Sets whether or not this land object has been explored.
	 * @param bool Boolean
	 */
	public void setHasBeenExplored(boolean bool);
	
	/**
	 * Checks whether or not this land object stops projectiles
	 * when hit.
	 * @return boolean : stops projectiles
	 */
	public boolean stopsProjectiles();
}
