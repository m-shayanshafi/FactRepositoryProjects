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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import src.enums.Enemies;

/**
 * This class will keep track of the type of enemies that can
 * spawn at each city.
 * @author Darren Watts
 * date 11/11/07
 */
public class CitySpawns {
	private static CitySpawns instance = null;
	private Map<Integer, ArrayList<Enemies>> enemyMap;
	private static int highestIndex = 0;
	
	/**
	 * Constructor for CitySpawns.  Sets up the map of enemies.
	 */
	private CitySpawns(){
		enemyMap = new HashMap<Integer, ArrayList<Enemies>>();
	}
	
	/**
	 * Returns the instance of this class.
	 * @return CitySpawns : instance
	 */
	public static CitySpawns getInstance(){
		if(instance == null)
			instance = new CitySpawns();
		return instance;
	}
	
	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}
	
	/**
	 * Sets the enemies to the list of enemies given at the specified
	 * level.
	 * @param enemies ArrayList<Enemies> : The enemies that can spawn.
	 * @param level int : level in the list of cities.
	 */
	@SuppressWarnings("unchecked")
	public void setEnemies(ArrayList<Enemies> enemies, int level){
		if(level > highestIndex) highestIndex = level;
		ArrayList<Enemies> temp = (ArrayList<Enemies>)enemies.clone();
		enemyMap.put(level, temp);
	}
	
	/**
	 * Returns a list of enemies at the specified level.
	 * @param level int : level in the set of cities.
	 * @return List of enemies
	 */
	public ArrayList<Enemies> getEnemies(int level){
		return enemyMap.get(level);
	}
	
	public void removeEnemies(int level) {
		enemyMap.remove(level);
	}
	
	public int getHighestLevelIndex() {
		return highestIndex;
	}
	
	/*
	 * Only used for the scenario editor
	 */

	public Map<Integer, ArrayList<Enemies>> getEnemyMap() {
		return enemyMap;
	}
}
