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

import java.util.HashMap;
import java.util.Map;

import src.enums.Enemies;

/**
 * This class keeps track of the shopkeeper that will spawn in
 * each city.
 * @author Darren Watts
 * date 1/14/08
 */
public class ShopkeeperSpawns {
	
	private static ShopkeeperSpawns instance = null;
	private Map<Integer, Enemies> shopkeeperMap;
	
	/**
	 * Constructor for ShopkeeperSpawns.  Sets up the list of enemies.
	 */
	private ShopkeeperSpawns(){
		shopkeeperMap = new HashMap<Integer, Enemies>();
	}
	
	/**
	 * Returns the instance of this class.
	 * @return ShopkeeperSpawns : instance
	 */
	public static ShopkeeperSpawns getInstance(){
		if(instance == null)
			instance = new ShopkeeperSpawns();
		return instance;
	}
	
	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}
	
	/**
	 * Assigns the shopkeeper to the given level.
	 * @param enemy Enemies : The enemy that will spawn.
	 * @param level int : level number
	 */
	public void setShopkeeper(Enemies enemy, int level){
		shopkeeperMap.put(level, enemy);
	}
	
	public void removeShopkeeper(int level) {
		shopkeeperMap.remove(level);
	}
	
	/**
	 * Gets the shopkeeper for the given level.
	 * @param level int : level number
	 * @return Enemies : shopkeeper type
	 */
	public Enemies getShopkeeper(int level){
		return shopkeeperMap.get(level);
	}
}
