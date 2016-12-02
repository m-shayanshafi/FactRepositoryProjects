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

import src.game.Item;
import src.game.Enemy;
import src.game.NPC;
import src.game.Player;

/**
 * A water land object.  Not used for now.  Maybe the next release
 * will support water types.
 * @author Darren Watts
 * date 11/14/07
 *
 */
public class Water implements Land {
	private int playerIndex; // Index of the player in this land object (-1 if none)
	private boolean hasBeenExplored;
	private Enemy enemy;
	private NPC npc;
	
	/**
	 * Creates a new water object.  Sets there to be no player and unexplored.
	 */
	public Water(){
		playerIndex = -1;
		hasBeenExplored = false;
	}
	
	/**
	 * Returns the land type "water".
	 * @return LandType : water.
	 */
	public int getLandType(){
		//return LandType.WATER.getTypeNum();
		return -1;
	}
	
	/**
	 * Used to get the image for the Water land object.
	 */
	public String getImage(){
		return null;
	}
	
	/**
	 * Used to get the image of the water, ignoring any objects
	 * that may be on it.
	 * @return String : image location
	 */
	public String getLandImage(){
		return null;
	}
	
	/**
	 * Todo:
	 * The player can only enter water with an appropriate vehicle.
	 */
	public boolean canEnter(){
		return false;
	}
	
	/**
	 * Sets the player to the specified player index at this water object.
	 * @param plr int : player index
	 */
	public void setPlayer(int plr){
		playerIndex = plr;
	}
	
	/**
	 * Todo:
	 * Player can enter, or throw exception.
	 */
	public void enter(Player plr){
		
	}
	
	/**
	 * Enemy tries to enter this land object.
	 * @return boolean : successfully entered
	 */
	public boolean enter(Enemy enemy){
		return false;
	}
	
	/**
	 * Meaningless for water land objects.
	 */
	public void setItem(Item item) {}

	/**
	 * Returns whether or not the player is in this water object.
	 * @return boolean True if the player is here, false if not.
	 */
	public int hasPlayer(){
		return playerIndex;
	}
	
	/**
	 * Adds an enemy to this water object.
	 */
	public void addEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	
	/**
	 * Meaningless for water land objects.
	 */
	public void addItem(Item item) {}
	
	/**
	 * Return the enemy at this water object.
	 * @return Enemy : the enemy.
	 */
	public Enemy getEnemy(){
		return enemy;
	}
	
	/**
	 * Always returns null for water objects.
	 */
	public Item getItem(){
		return null;
	}
	
	/**
	 * Sets an enemy to this water object.
	 */
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	
	/**
	 * Sets the npc of this water object.
	 */
	public void setNPC(NPC npc){
		this.npc = npc;
	}
	
	/**
	 * Accessor for the npc at this water object.
	 * @return NPC : npc
	 */
	public NPC getNPC(){
		return npc;
	}
	
	/**
	 * Todo:
	 * Determine if the enemy can enter based on type of enemy.
	 */
	public boolean isEnemyAccessible(){
		return false;
	}
	
	/**
	 * Water cannot be destroyed, so it will always return false.
	 * @return false
	 */
	public boolean isDestroyable(){
		return false;
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
	 * Water does not stop projectiles.
	 */
	public boolean stopsProjectiles(){
		return false;
	}
	
	/**
	 * Writes this land object to the output stream.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(hasBeenExplored);
		} catch(Exception e) {}
	}
	
	/**
	 * Loads this land object from the input stream.
	 */
	public void load(ObjectInputStream ois){
		try{
			hasBeenExplored = (Boolean) ois.readObject();
		} catch(Exception e) {}
	}
}
