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
import src.enums.Shops;
import src.game.GameWorld;
import src.game.Item;
import src.game.Enemy;
import src.game.NPC;
import src.game.Player;
import src.gui.ShopWindow;


/**
 * Shop land object.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Shop implements Land{

	private boolean hasBeenExplored;
	private Shops type;
	private int permutation;

	/**
	 * Constructor sets up type of shop.
	 * @param type Shops : type
	 */
	public Shop(Shops type, int permutation){
		this.type = type;
		this.permutation = permutation;
	}
	
	/**
	 * Returns the land type "shop".
	 * @return LandType : shop.
	 */
	public int getLandType(){
		return LandType.SHOP.getTypeNum();
	}

	/**
	 * Accessor for the permutation number of the shop.
	 * @return int : permutation
	 */
	public int getPermutation(){
		return permutation;
	}

	/**
	 * Not possible for shops
	 * @param item Item : the item
	 */
	public void setItem(Item item){}

	/**
	 * Shops never have players.
	 * @return int : index of player (-1)
	 */
	public int hasPlayer(){
		return -1;
	}

	/**
	 * Gets whether or not this shop has been explored.
	 * @return boolean : explored
	 */
	public boolean hasBeenExplored(){
		return hasBeenExplored;
	}

	/**
	 * Sets whether or not this shop has been explored.
	 * @param bool boolean : explored
	 */
	public void setHasBeenExplored(boolean bool){
		hasBeenExplored = bool;
	}

	/**
	 * Returns null for shops.
	 * @return Item : item
	 */
	public Item getItem(){
		return null;
	}

	/**
	 * Does nothing for shops
	 * @param item Item : the item
	 */
	public void addItem(Item item){}

	/**
	 * Does nothing for shops.
	 * @param enemy Enemy : the enemy
	 */
	public void addEnemy(Enemy enemy){}

	/**
	 * Does nothing for shops.
	 * @param enemy Enemy : the enemy
	 */
	public void setEnemy(Enemy enemy){}

	/**
	 * Does nothing for Shop objects.
	 */
	public void setNPC(NPC npc) {}

	/**
	 * Will always return null for shops.
	 */
	public NPC getNPC(){
		return null;
	}

	/**
	 * Returns null for shops
	 * @return Enemy : the enemy
	 */
	public Enemy getEnemy(){
		return null;
	}

	/**
	 * Does nothing for shop land objects.
	 */
	public void setPlayer(int plr) { }

	/**
	 * Shops are destroyable
	 * @return boolean : destroyable
	 */
	public boolean isDestroyable(){
		return true;
	}

	/**
	 * Returns false for shops
	 * @return boolean : accessible to enemies
	 */
	public boolean isEnemyAccessible(){
		return false;
	}

	/**
	 * Gets the image location for this shop.
	 * @return String : image location
	 */
	public String getImage(){
		return type.getImage();
	}
	
	/**
	 * The same as getImage for shop land types.
	 * @return String : image location
	 */
	public String getLandImage(){
		return getImage();
	}

	/**
	 * Accessor for this shop's type.
	 * @return Shops : type of shop
	 */
	public Shops getType(){
		return type;
	}

	/**
	 * Returns false for shops.
	 * @return boolean : can enter
	 */
	public boolean canEnter(){
		return false;
	}

	/**
	 * Brings up a shop GUI window.
	 * @param plr Player : the player
	 */
	public void enter(Player plr) {
		int shopRow = 0;
		int shopCol = 0;
		if (type == Shops.BAR_L || type == Shops.BAR_R)
			ShopWindow.getInstance().createBarWindow(permutation);
		else if( type == Shops.HOSPITAL_L || type == Shops.HOSPITAL_R)
			ShopWindow.getInstance().createHospitalWindow();
		else {
			
			// Find location of this shop.
			for(int i = plr.getRow()-1; i <= plr.getRow()+1; i++){
				for(int k = plr.getCol()-1; k <= plr.getCol()+1; k++){
					if(GameWorld.getInstance().getLandAt(i, k) == this){
						shopRow = i;
						shopCol = k;
					}
				}
			}
			ShopWindow.getInstance().createShopWindow(type, permutation,
					shopRow, shopCol);
		}
	}

	/**
	 * Shops stop projectiles.
	 */
	public boolean stopsProjectiles(){
		return true;
	}

	/**
	 * Enemy tries to enter this land object.
	 * @return boolean : successfully entered
	 */
	public boolean enter(Enemy enemy){
		return false;
	}

	/**
	 * Saves the shop object to an output stream.
	 * @param oos ObjectOutputStream : output stream to save to.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(type);
			oos.writeObject(permutation);
			oos.writeObject(hasBeenExplored);
		} catch(Exception e) {}
	}

	/**
	 * Loads the shop object from an input stream.
	 * @param ois ObjectInputStream : input stream to read from.
	 */
	public void load(ObjectInputStream ois){
		try {
			type = (Shops) ois.readObject();
			permutation = (Integer) ois.readObject();
			hasBeenExplored = (Boolean) ois.readObject();
		} catch(Exception e) {}
	}
}
