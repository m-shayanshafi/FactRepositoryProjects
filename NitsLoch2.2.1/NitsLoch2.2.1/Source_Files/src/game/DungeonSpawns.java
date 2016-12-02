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

import src.enums.Enemies;
import src.exceptions.EnemySpawnNotDefinedException;

/**
 * A Singleton class that keeps track of what kind of monsters can spawn at
 * different levels of dungeons.  It will start with default values, but
 * the information can be read in from a file.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class DungeonSpawns {

	public static final int MAX_NUM_DUNGEONS = 50;
	private static DungeonSpawns instance = null;
	private Object[] allEnemies;
	//private ArrayList<ArrayList<Enemies> allEnemies;
	
	/**
	 * Constructor for DungeonSpawns.  Sets up the list of enemies.
	 */
	private DungeonSpawns(){
		allEnemies = new Object[MAX_NUM_DUNGEONS];
		for(int i = 0; i < allEnemies.length; i++){
			allEnemies[i] = new ArrayList<Enemies>();
		}
	}
	
	/**
	 * Returns the instance of this class.
	 * @return DungeonSpawns : instance
	 */
	public static DungeonSpawns getInstance(){
		if(instance == null)
			instance = new DungeonSpawns();
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
	 * level in the dungeons.
	 * @param enemies ArrayList<Enemies> : The enemies that can spawn.
	 * @param level int : level in the dungeon.
	 */
	public void setEnemies(ArrayList<Enemies> enemies, int level){
		allEnemies[level-1] = enemies.clone();
	}
	
	public void removeEnemies(int level) {
		allEnemies[level-1] = new ArrayList<Enemies>();
	}
	
	/**
	 * Returns a list of enemies at the specified level.
	 * @param level int : level in the dungeon.
	 * @return List of enemies
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Enemies> getEnemies(int level) throws EnemySpawnNotDefinedException {
		try {
			return (ArrayList<Enemies>)allEnemies[level-1];
		} catch(Exception ex){
			throw new EnemySpawnNotDefinedException();
		}
	}
	
	/*
	 * Only used for the scenario editor
	 */

	public Object[] getEnemyArray() {
		return allEnemies;
	}
}
