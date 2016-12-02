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

import src.enums.EnemyBehavior;
import src.enums.GroundItems;
import src.enums.LandType;
import src.enums.StreetType;
import src.enums.PlayerImages;
import src.exceptions.EnemyDiedException;
import src.exceptions.NoPlayerException;
import src.exceptions.PlayerEnteredException;
import src.game.GameWorld;
import src.game.Item;
import src.game.Enemy;
import src.game.NPC;
import src.game.Player;

/**
 * Street land object.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Street implements Land {
	
	private StreetType type;
	private Item item;
	private Enemy enemy;
	private NPC npc;
	private int playerIndex;
	private boolean hasBeenExplored;
	
	/**
	 * Constructor for street.  Takes an int for the player index currently
	 * in this street object.  -1 player index means there is no player at this
	 * street object.  Also needs to know if there is a player or enemy. A street
	 * can only have one player, item, npc or enemy in it at a time.  If 2 or more
	 * are non-null, will choose only one of them based on this order:
	 * (player > enemy > npc > item).
	 * @param player int : player index (-1 if none).
	 * @param item Item : item
	 * @param enemy Enemy : enemy
	 */
	public Street(StreetType type, int player, Item item, Enemy enemy, NPC npc){
		if(player != -1){
			this.playerIndex = player;
			this.item = null;
			this.enemy = null;
			this.npc = null;
		}
		else if(enemy != null){
			this.playerIndex = -1;
			this.item = null;
			this.enemy = enemy;
			this.npc = null;
		}
		else if(npc != null){
			playerIndex = -1;
			this.item = null;
			this.enemy = null;
			this.npc = npc;
		}
		else { // Item != null
			playerIndex = -1;
			this.item = item;
			this.enemy = null;
			this.npc = null;
		}
		this.type = type;
		hasBeenExplored = false;
	}
	
	/**
	 * Returns the land type "street".
	 * @return LandType : street.
	 */
	public int getLandType(){
		return LandType.STREET.getTypeNum();
	}
	
	/**
	 * Accessor for the tyep of street.
	 * @return StreetType : type
	 */
	public StreetType getType(){
		return type;
	}
	
	/**
	 * Sets the item that is in the land object.
	 * @param item Kind of item.
	 */
	public void setItem(Item item){
		this.item = item;
	}
	
	/**
	 * Sets the NPC at this land object.
	 * @param npc NPC : the npc
	 */
	public void setNPC(NPC npc){
		this.npc = npc;
	}
	
	/**
	 * Sets the player to the specified player index at this street object.
	 * @param plr int : player index
	 */
	public void setPlayer(int plr){
		playerIndex = plr;
	}

	/**
	 * Gets the player index of the player at this street object.
	 * @return int : player index
	 */
	public int hasPlayer(){
		return playerIndex;
	}

	/**
	 * Accessor for whether or not this street object has been explored.
	 * @return boolean : has been explored
	 */
	public boolean hasBeenExplored(){
		return hasBeenExplored;
	}

	/**
	 * Sets whether or not this street object has been explored.
	 * @param bool boolean : explored
	 */
	public void setHasBeenExplored(boolean bool){
		hasBeenExplored = bool;
	}

	/**
	 * Accessor for the enemy. Will return null if there is none.
	 * @return Item : the item
	 */
	public Item getItem(){
		return item;
	}

	/**
	 * Adds the item to this street object.
	 * @param item Item : the item
	 */
	public void addItem(Item item){
		this.item = item;
	}

	/**
	 * Adds the enemy to this street object.
	 * @param enemy Enemy : the enemy
	 */
	public void addEnemy(Enemy enemy){
		this.enemy = enemy;
	}

	/**
	 * Sets the enemy that is in the land object.
	 * @param en Enemy : the enemy
	 */
	public void setEnemy(Enemy en){
		enemy = en;
	}

	/**
	 * Accessor for this street's enemy
	 * @return Enemy : enemy
	 */
	public Enemy getEnemy(){
		return enemy;
	}
	
	/**
	 * Accessor for this object's NPC.
	 * @return NPC : the npc
	 */
	public NPC getNPC(){
		return npc;
	}

	/**
	 * Street objects are never destroyable.
	 * @return boolean : destroyable
	 */
	public boolean isDestroyable(){
		return false;
	}
	
	/**
	 * Used to see if there is another object in the land object.
	 * @return boolean : True if there is no player, enemy or item.
	 */
	public boolean isEmpty(){
		if(item == null && enemy == null && playerIndex == -1
				&& npc == null) return true;
		else return false;
	}
	
	/**
	 * Checks to see whether or not an enemy can enter this street object.
	 * @return boolean : enemy accessible
	 */
	public boolean isEnemyAccessible(){
		if(isEmpty()) return true;
		else if(item != null && item.getType() == GroundItems.GRAVE){
			item = null;
			return true;
		}
		else return false;
	}

	/**
	 * Gets the image location for the street object.
	 * @return String : image location
	 */
	public String getImage(){
		String image = "";
		if(playerIndex > -1)
			try {
				image = GameWorld.getInstance().getPlayer(playerIndex).getImage();
			} catch(NoPlayerException plrEx){
				image = PlayerImages.I_00.getRightImage();
			}
		else if(enemy != null)
			image = enemy.getImage();
		else if(npc != null)
			image = npc.getImage();
		else if(item != null)
			image = item.getImage();
		else
			image = type.getImage();
		return image;
	}
	
	/**
	 * Gets the image of the street, ignoring any objects that
	 * are on it.
	 */
	public String getLandImage(){
		return type.getImage();
	}

	/**
	 * The player can enter the street only if there is no enemy already there.
	 * @return boolean : player can enter
	 */
	public boolean canEnter(){
		if(enemy == null) return true;
		else return false;
	}
	
	/**
	 * Player tries to enter this street object.  If the player can
	 * move, he will enter this street object.  If there is an enemy,
	 * it will cause the player to attack.  Will throw an exception for
	 * successfully entering the street, so the player knows to update
	 * his location.
	 * Todo:
	 * Initiate player vs player combat if networking is ever implemented.
	 * @param plr Player : the player
	 */
	public void enter(Player plr) throws PlayerEnteredException, EnemyDiedException {
		if(isEmpty()){
			playerIndex = plr.getIndex();
			if(type.getIsTrigger() && (Math.random() * 100) < type.getChance())
				GameWorld.getInstance().triggerEnemies(plr, type);
			throw new PlayerEnteredException();
		}
		else if(playerIndex > -1) return;
		else if(npc != null)
			npc.talk();
		else if(enemy == null){
			playerIndex = plr.getIndex();
			if(!item.getType().isExplosive()){
				item.pickupItem(plr);
				item = null;
			}
			if(type.getIsTrigger() && Math.random() < type.getChance())
				GameWorld.getInstance().triggerEnemies(plr, type);
			throw new PlayerEnteredException();
		}
		else { // Enemy in the street
			try {
				plr.attack(enemy);
			} catch(EnemyDiedException ex){
				throw new EnemyDiedException();
			}
		}
	}
	
	/**
	 * Enemy tries to enter this land object.
	 * @param enemy Enemy : the enemy
	 * @return boolean : successfully entered
	 */
	public boolean enter(Enemy enemy){
		if(isEmpty()){
			if(enemy.getType().getBehavior() == EnemyBehavior.RESTRICTED){
				if(enemy.getStartingLand() != type) return false;
			}
			this.enemy = enemy;
			return true;
		}
		else if(playerIndex > -1){
			try {
				if(enemy.getType().getBehavior() != EnemyBehavior.COWARDLY &&
						enemy.getType().getBehavior() != EnemyBehavior.CLINGY)
					enemy.attack(GameWorld.getInstance().getPlayer(playerIndex));
				return false;
			} catch(NoPlayerException plrE){
				return false;
			}
		}
		else { // Enemy or item in the street
			if(isEnemyAccessible()){
				// Remove item that does not block enemy's path
				item = null;
				this.enemy = enemy;
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Sets the player index at this street object.
	 * @param num int : player index
	 */
	public void setPlayerIndex(int num){
		playerIndex = num;
	}
	
	/**
	 * Accessor for the player index at this street object.
	 * @return int : player index
	 */
	public int getPlayerIndex(){
		return playerIndex;
	}
	
	/**
	 * Streets do not stop projectiles.
	 */
	public boolean stopsProjectiles(){
		return false;
	}

	/**
	 * Saves the street object to an output stream.
	 * @param oos ObjectOutputStream : output stream to save to.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(type);
			oos.writeObject(item);
			oos.writeObject(enemy);
			oos.writeObject(npc);
			oos.writeObject(playerIndex);
			oos.writeObject(hasBeenExplored);
		} catch(Exception e) {}
	}

	/**
	 * Loads the street object from an input stream.
	 * @param ois ObjectInputStream : input stream to read from.
	 */
	public void load(ObjectInputStream ois){
		try {
			type = (StreetType) ois.readObject();
			item = (Item) ois.readObject();
			enemy = (Enemy) ois.readObject();
			npc = (NPC) ois.readObject();
			playerIndex = (Integer) ois.readObject();
			hasBeenExplored = (Boolean) ois.readObject();
		} catch(Exception e) {}
	}
}
