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
import java.util.ArrayList;

import javax.swing.JOptionPane;

import src.Constants;
import src.enums.*;
import src.exceptions.CityNameExistsException;
import src.exceptions.EnemyDiedException;
import src.exceptions.NoPlayerException;
import src.exceptions.NoSuchCityException;
import src.exceptions.PlayerEnteredException;
import src.file.map.MapFile;
import src.file.scenario.ScenarioFile;
import src.land.Land;
import src.land.Exit;
import src.land.Obstruction;
import src.land.Shop;
import src.land.Street;
import src.scenario.MiscScenarioData;

/**
 * Singleton class.  Keeps track of the maps in the current game.  Contains a
 * collection of players.  This is in case networking is implemented later.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class GameWorld {
	private static GameWorld instance = null;
	private int currentLevel;
	private ArrayList<Land[][]> cities;
	private ArrayList<String> cityNames;
	private Land[][] landArray;

	private int explodeRow;
	private int explodeCol;

	// A collection of players for now, to make it easier later
	// if networking is ever implemented.
	ArrayList<Player> players;

	private boolean endingMessageDisplayed;

	private int checkExitsLevel;
	private ArrayList<Integer> checkExitsArray;

	private GameWorld(){
		players = new ArrayList<Player>();
		cities = new ArrayList<Land[][]>();
		cityNames = new ArrayList<String>();
		currentLevel = 0;

		explodeRow = -1;
		explodeCol = -1;

		endingMessageDisplayed = false;
		checkExitsLevel = -1;
		checkExitsArray = new ArrayList<Integer>();
	}

	/**
	 * Loads the map file specified in the scenario xml file.
	 */
	public void loadMapFromFile(){
		try{
			/*FileInputStream fin = new FileInputStream(MiscScenarioData.MAP_PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);
			load(ois);
			ois.close();*/
			if(src.Constants.SCENARIO_DEBUG || src.Constants.EDITOR)
				MapFile.getInstance().load(MiscScenarioData.MAP_PATH);
			else MapFile.getInstance().load(ScenarioFile.getInstance().getStream(MiscScenarioData.MAP_PATH));
			GameWorld.getInstance().setCurrentLevel(0);
		} catch (Exception ex) {
			System.out.println("Error reading map file.");
		}
	}

	/**
	 * Returns the instance of this class.
	 * @return GameWorld : instance
	 */
	public static GameWorld getInstance(){
		if(instance == null)
			instance = new GameWorld();
		return instance;
	}

	/**
	 * Returns a new instance of a GameWorld that is not a saved instance.
	 * @return GameWorld : new instance
	 */
	public GameWorld getTempInstance(){
		return new GameWorld();
	}

	/**
	 * Sets the GameWorld instance.
	 * @param newInstance GameWorld : instance
	 */
	public static void setInstance(GameWorld newInstance){
		instance = newInstance;
	}

	/**
	 * Gets the city name for the specified city index.
	 * @param num int : city index
	 * @return String : city name
	 */
	public String getCityName(int num){
		return cityNames.get(num);
	}

	/**
	 * Accessor for all of the city names
	 * @return ArrayList<String> city names.
	 */
	public ArrayList<String> getCityNames(){
		return cityNames;
	}

	/**
	 * Sets the city names.
	 * @param newNames ArrayList<String> : city names
	 */
	public void setCityNames(ArrayList<String> newNames){
		cityNames = newNames;
	}

	/**
	 * Sets the current city name to the new name.
	 * @param newName String : new city name
	 */
	public void setCurrentCityName(String newName) throws CityNameExistsException {
		if(cityNames.contains(newName)) throw new CityNameExistsException();
		cityNames.set(currentLevel, newName);
	}

	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}

	/**
	 * Adds a player to this world.
	 * @param plr Player : the player to add.
	 * @return int : player index
	 */
	public int addPlayer(Player plr){
		players.add(plr);
		return players.size()-1;
	}

	/**
	 * Accessor for the list of players in the world.
	 * @return ArrayList of players.
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}

	/**
	 * Returns the player at the given index.
	 * @param playerIndex int : player index
	 * @return The player
	 */
	public Player getPlayer(int playerIndex) throws NoPlayerException {
		try {
			return players.get(playerIndex);
		} catch(IndexOutOfBoundsException bounds){
			throw new NoPlayerException();
		}
	}

	/**
	 * Will return the player closest to the land object at row and column.
	 * For now, this will just be the only player, but if networking is implemented
	 * later, this method would actually do something.
	 * @param row : int
	 * @param col : int
	 * @return The player
	 */
	public Player getClosestPlayer(int row, int col){
		return players.get(0);
	}

        /**
         * Removes all enemies on the specified level.
         * @param level int : level index
         */
        public void removeEnemies(int level){
            Land[][] tempLand = cities.get(level);
            for(int i = 0; i < tempLand.length; i++){
                for(int k = 0; k < tempLand[0].length; k++){
                    tempLand[i][k].setEnemy(null);
                }
            }
        }

	/**
	 * Gets the gameworld relative player from the landArray.  It will return
	 * the land[][] within the player's view.  Automatically sets up the Outside
	 * objects (trees), so they do not need to be included in the array.
	 * @param row Row number as an int.
	 * @param col Column number as an int.
	 * @return The world as a Land[][].
	 */
	public Land[][] getGameWorld(int row, int col){
		landArray = cities.get(currentLevel);
		Land[][] world = new Land[Constants.WORLD_VIEW_SIZE][Constants.WORLD_VIEW_SIZE];
		int range = Constants.WORLD_VIEW_SIZE/2;
		int rowCount = 0, colCount = 0;
		for(int i = row-range; i <= row+range; i++){
			for(int k = col-range; k <= col+range; k++){
				if(i < 0 || k < 0)
					world[rowCount][colCount++] = new Obstruction(
							ObstructionLandType.OBS_000, false);
				else if(i >= landArray.length || k >= landArray[0].length){
					world[rowCount][colCount++] = new Obstruction(
							ObstructionLandType.OBS_000, false);
				}
				else{
					world[rowCount][colCount++] = landArray[i][k];
					landArray[i][k].setHasBeenExplored(true);
				}

				if(colCount >= Constants.WORLD_VIEW_SIZE){ colCount = 0; rowCount++; }
			}
		}
		return world;
	}

	/**
	 * Accessor for the local player
	 * @return Player : local player
	 */
	public Player getLocalPlayer(){
		try {
			return players.get(TheGame.getInstance().getLocalPlayerIndex());
		} catch(Exception ex){ return null; }
	}

	/**
	 * Erases all information from the cities.  Starts fresh.
	 */
	public void resetCities(){
		cities = new ArrayList<Land[][]>();
		cityNames = new ArrayList<String>();
		currentLevel = 0;
	}

	/**
	 * Adds a new level with the specified level name, rows and columns.
	 * This is used by the editor when starting a new level.  It will
	 * initialize the map so the outer edge of the map are undestroyable
	 * obstructions, and the rest of the map is made of streets.
	 * @param name String : level name
	 * @param rows int : rows
	 * @param cols int : columns
	 */
	public void newCity(String name, int rows, int cols) throws CityNameExistsException {
		if(cityNames.contains(name)) throw new CityNameExistsException();

		cityNames.add(name);
		Land[][] newLand = new Land[rows][cols];
		for(int i = 0; i < rows; i++){
			for(int k = 0; k < cols; k++){
				if(i == 0 || i == rows-1){
					newLand[i][k] = new Obstruction(ObstructionLandType.OBS_001, false);
				}
				else if(k == 0 || k == cols-1){
					newLand[i][k] = new Obstruction(ObstructionLandType.OBS_001, false);
				}
				else {
					newLand[i][k] = new Street(StreetType.ST_000, -1, null, null, null);
				}
			}
		}
		cities.add(newLand);
		currentLevel = cities.size()-1;
	}

	/**
	 * Removes the specified city from the world.
	 * @param level int : level index
	 */
	public void removeCity(int level){
		if(currentLevel == level && currentLevel-1 >= 0) currentLevel = level-1;
		else if(currentLevel == level) currentLevel = 0;
		cityNames.remove(level);
		cities.remove(level);
	}

	/**
	 * Uses the current level and randomizes it so it fits a dungeon
	 * pattern.  It will reset all of the obstructions to wall 1, and
	 * all streets to street 0.  All of the objects will be removed.
	 */
	public void randomizeLevel(){
		// 50 - 50 chance for a given tile to be an obstruction or street
		for(int i = 0; i < landArray.length; i++){
			for(int k = 0; k < landArray[0].length; k++){
				if(Math.random() < .5){
					landArray[i][k] = new Street(StreetType.ST_000,
							-1, null, null, null);
				}
				else {
					landArray[i][k] = new Obstruction(
							ObstructionLandType.OBS_001, false);
				}
			}
		}
		// Try to make paths out of the current grid
		for(int i = 0; i < landArray.length; i++){
			for(int k = 0; k < landArray[0].length; k++){
				randomFix(i, k);
			}
		}
		// Fix diagonals so there are no large open areas that
		// won't render correctly in a dungeon.
		for(int i = 0; i < landArray.length; i+=2){
			for(int k = 0; k < landArray[0].length; k+=2){
				diagonalFix(i, k);
			}
		}

		// Create walls around the edge
		for(int i = 0; i < landArray.length; i++){
			landArray[i][0] = new Obstruction(
					ObstructionLandType.OBS_001, false);
			landArray[i][landArray[0].length-1] = new Obstruction(
					ObstructionLandType.OBS_001, false);
		}
		for(int i = 0; i < landArray[0].length; i++){
			landArray[0][i] = new Obstruction(
					ObstructionLandType.OBS_001, false);
			landArray[landArray.length-1][i] = new Obstruction(
					ObstructionLandType.OBS_001, false);
		}
	}

	/**
	 * Tries to create paths from the existing set of streets.
	 * @param row int : row of tile to create path around
	 * @param col int : column of tile to create path around
	 */
	private void randomFix(int row, int col){
		try {
			int pathCount = 0;
			if(landArray[row-1][col] instanceof Street)
				pathCount++;
			if(landArray[row+1][col] instanceof Street)
				pathCount++;
			if(landArray[row][col-1] instanceof Street)
				pathCount++;
			if(landArray[row][col+1] instanceof Street)
				pathCount++;
			if(pathCount < 2){
				createRandomStreet(row, col, Math.random());
			}
			if(pathCount > 2){
				createRandomObstruction(row, col, Math.random());
				randomFix(row, col);
			}
		} catch(Exception ex) { }
	}

	/**
	 * Creates a street next to the tile at the given row and column.
	 * It will decide which direction to create the opening based on
	 * the random number given.
	 * @param row int : row of tile to create street next to.
	 * @param col int : column of tile to create street next to.
	 * @param rand double : random number used to determine which direction
	 * the street will be created from the given tile.
	 */
	private void createRandomStreet(int row, int col, double rand){
		if(rand < .25){
			if(!(landArray[row-1][col] instanceof Street))
				landArray[row-1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row+1][col] instanceof Street))
				landArray[row+1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col-1] instanceof Street))
				landArray[row][col-1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col+1] instanceof Street))
				landArray[row][col+1] = new Street(StreetType.ST_000,
						-1, null, null, null);
		}
		else if(rand < .5){
			if(!(landArray[row+1][col] instanceof Street))
				landArray[row+1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col-1] instanceof Street))
				landArray[row][col-1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col+1] instanceof Street))
				landArray[row][col+1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row-1][col] instanceof Street))
				landArray[row-1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
		}
		else if(rand < .75){
			if(!(landArray[row][col-1] instanceof Street))
				landArray[row][col-1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col+1] instanceof Street))
				landArray[row][col+1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row-1][col] instanceof Street))
				landArray[row-1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row+1][col] instanceof Street))
				landArray[row+1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
		}
		else {
			if(!(landArray[row][col+1] instanceof Street))
				landArray[row][col+1] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row-1][col] instanceof Street))
				landArray[row-1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row+1][col] instanceof Street))
				landArray[row+1][col] = new Street(StreetType.ST_000,
						-1, null, null, null);
			else if(!(landArray[row][col-1] instanceof Street))
				landArray[row][col-1] = new Street(StreetType.ST_000,
						-1, null, null, null);
		}
	}

	/**
	 * Creates an obstruction next to the tile at the given row and column.
	 * It will decide which direction to create the wall based on
	 * the random number given.
	 * @param row int : row of tile to create obstruction next to.
	 * @param col int : column of tile to create obstruction next to.
	 * @param rand double : random number used to determine which direction
	 * the obstruction will be created from the given tile.
	 */
	private void createRandomObstruction(int row, int col, double rand){
		if(rand < .25) {
			if(landArray[row-1][col] instanceof Street)
				landArray[row-1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row+1][col] instanceof Street)
				landArray[row+1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col-1] instanceof Street)
				landArray[row][col-1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col+1] instanceof Street)
				landArray[row][col+1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
		}
		else if(rand < .5){
			if(landArray[row+1][col] instanceof Street)
				landArray[row+1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col-1] instanceof Street)
				landArray[row][col-1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col+1] instanceof Street)
				landArray[row][col+1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row-1][col] instanceof Street)
				landArray[row-1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
		}
		else if(rand < .75){
			if(landArray[row][col-1] instanceof Street)
				landArray[row][col-1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col+1] instanceof Street)
				landArray[row][col+1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row-1][col] instanceof Street)
				landArray[row-1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row+1][col] instanceof Street)
				landArray[row+1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
		}
		else {
			if(landArray[row][col+1] instanceof Street)
				landArray[row][col+1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row-1][col] instanceof Street)
				landArray[row-1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row+1][col] instanceof Street)
				landArray[row+1][col] = new Obstruction(
						ObstructionLandType.OBS_001, true);
			else if(landArray[row][col-1] instanceof Street)
				landArray[row][col-1] = new Obstruction(
						ObstructionLandType.OBS_001, true);
		}
	}

	/**
	 * Checks the diagonals of the given tile to make sure at
	 * least one of them is a wall.  This will make sure that
	 * there are no large openings that the dungeon renderer
	 * cannot draw correctly.
	 * @param row int : row of the tile to check for diagonals at.
	 * @param col int : column of the tile to check for diagonals at.
	 */
	private void diagonalFix(int row, int col){
		boolean hasWall = false;
		for(int i = row -1; i <= row; i++){
			for(int k = col - 1; k <= col; k++){
				try {
					if(landArray[i][k] instanceof Obstruction) hasWall = true;
				} catch(Exception ex) { hasWall = true; }
			}
		}
		if(!hasWall){
			landArray[row-1][col-1] = new Obstruction(
					ObstructionLandType.OBS_001, true);
		}
		hasWall = false;
		for(int i = row; i <= row + 1; i++){
			for(int k = col - 1; k <= col; k++){
				try {
					if(landArray[i][k] instanceof Obstruction) hasWall = true;
				} catch(Exception ex) { hasWall = true; }
			}
		}
		if(!hasWall){
			landArray[row+1][col-1] = new Obstruction(
					ObstructionLandType.OBS_001, true);
		}
		hasWall = false;
		for(int i = row - 1; i <= row; i++){
			for(int k = col; k <= col + 1; k++){
				try {
					if(landArray[i][k] instanceof Obstruction) hasWall = true;
				} catch(Exception ex) { hasWall = true; }
			}
		}
		if(!hasWall){
			landArray[row-1][col+1] = new Obstruction(
					ObstructionLandType.OBS_001, true);
		}
		hasWall = false;
		for(int i = row; i <= row + 1; i++){
			for(int k = col; k <= col + 1; k++){
				try {
					if(landArray[i][k] instanceof Obstruction) hasWall = true;
				} catch(Exception ex) { hasWall = true; }
			}
		}
		if(!hasWall){
			landArray[row+1][col+1] = new Obstruction(
					ObstructionLandType.OBS_001, true);
		}
	}

	/**
	 * Resizes the level to fit the specified row and columns.  It will expand
	 * or compress different parts of the map depending on what the bottom and
	 * right variables are set to.  For example, if the bottom variable is set,
	 * it will expand or shrink the bottom of the existing map, preserving what
	 * exists for the map that is above that point.  If bottom is not set, it
	 * will expand or shrink from the top.  The right variable works the same way,
	 * except it works with the right and left sides of the map.
	 * @param rows int : new number of rows
	 * @param cols int : new number of columns
	 * @param bottom boolean : expand/shrink bottom of map
	 * @param right boolean : expand/shrink right of map
	 */
	public void resizeLevel(int rows, int cols, boolean bottom, boolean right){
		Land[][] newLand = new Land[rows][cols];
		if(bottom && right){
			for(int row = 0; row < rows; row++){
				for(int col = 0; col < cols; col++){
					try {
						if(row == landArray.length-1 && rows != landArray.length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else if(col == landArray[0].length-1 && cols != landArray[0].length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else newLand[row][col] = landArray[row][col];
					} catch(ArrayIndexOutOfBoundsException aex){
						newLand[row][col] = new Street(StreetType.ST_000,
								-1, null, null, null);
					} catch(Exception e) {}

					forceNonDestroyableEdge(newLand, row, col, rows, cols);
				}
			}
		}
		else if(bottom && !right){
			int count = 1;
			for(int row = 0; row < rows; row++){
				for(int col = cols-1; col >= 0; col--){
					try {
						if(row == landArray.length-1 && rows != landArray.length){
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						}
						else if(col == newLand[0].length - landArray[0].length && cols != landArray[0].length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else newLand[row][col] = landArray[row][landArray[0].length-count];
						count++;
					} catch(ArrayIndexOutOfBoundsException aex){
						newLand[row][col] = new Street(StreetType.ST_000,
								-1, null, null, null);
					} catch(Exception e) {}

					forceNonDestroyableEdge(newLand, row, col, rows, cols);
				}
				count = 1;
			}
		}
		else if(!bottom && right){
			int count = 1;
			for(int row = rows-1; row >= 0; row--){
				for(int col = 0; col < cols; col++){
					try {
						if(row == newLand.length - landArray.length && rows != landArray.length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else if(col == landArray[0].length-1 && cols != landArray[0].length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else newLand[row][col] = landArray[landArray.length-count][col];
					} catch(ArrayIndexOutOfBoundsException aex){
						newLand[row][col] = new Street(StreetType.ST_000,
								-1, null, null, null);
					} catch(Exception e) {}

					forceNonDestroyableEdge(newLand, row, col, rows, cols);
				}
				count++;
			}
		}
		else if(!bottom && !right){
			int rowCount = 1;
			int colCount = 1;
			for(int row = rows-1; row >= 0; row--){
				for(int col = cols-1; col >= 0; col--){
					try {
						if(row == newLand.length - landArray.length && rows != landArray.length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else if(col == newLand[0].length - landArray[0].length && cols != landArray[0].length)
							newLand[row][col] = new Street(StreetType.ST_000,
									-1, null, null, null);
						else newLand[row][col] = landArray[landArray.length-rowCount][landArray[0].length-colCount];
						colCount++;
					} catch(ArrayIndexOutOfBoundsException aex){
						newLand[row][col] = new Street(StreetType.ST_000,
								-1, null, null, null);
					} catch(Exception e) {}

					forceNonDestroyableEdge(newLand, row, col, rows, cols);
				}
				rowCount++;
				colCount = 1;
			}
		}

		cities.set(currentLevel, newLand);
	}

	/**
	 * Checks to see what the land object is at the specified row and
	 * column to see if it needs to be made non destroyable.  This is
	 * used when resizing the level, and you want to make sure that
	 * all the boundary walls are non destroyable.
	 * @param newLand Land[][] the land to check
	 * @param row int : row
	 * @param col int : column
	 * @param rows int : number of rows in the land
	 * @param cols int : number of columns in the land
	 */
	private void forceNonDestroyableEdge(Land[][] newLand, int row,
			int col, int rows, int cols){

		if(row == 0 || col == 0 || row == rows-1 || col == cols-1){
			if(newLand[row][col] instanceof Exit) return;
			else if(newLand[row][col] instanceof Street)
				newLand[row][col] = new Obstruction(
						ObstructionLandType.OBS_001, false);
			else if(newLand[row][col] instanceof Obstruction)
				((Obstruction)newLand[row][col]).setIsDestroyable(false);
			else
				newLand[row][col] = new Obstruction(
						ObstructionLandType.OBS_001, false);
		}
	}

	/**
	 * Remove all objects on the current level.
	 */
	public void nukeObjects(){
		for(int i = 0; i < landArray.length; i++){
			for(int k = 0; k < landArray[0].length; k++){
				landArray[i][k].setItem(null);
				landArray[i][k].setEnemy(null);
				landArray[i][k].setNPC(null);
			}
		}
	}

	/**
	 * Sets all of the enemies on the current level to the specified
	 * level.
	 * @param level int : enemy level
	 */
	public void setEnemiesToLevel(int level){
		for(int i = 0; i < landArray.length; i++){
			for(int k = 0; k < landArray[0].length; k++){
				if(landArray[i][k].getEnemy() != null){
					landArray[i][k].getEnemy().setLevel(level);
				}
			}
		}
	}

	/**
	 * Creates an explosion at this place in the world.
	 * @param row int : row
	 * @param col int: column
	 */
	public void worldExplode(int row, int col, ExplosionType expType){

		src.enums.Sounds.EXPLOSION.playSound();

		explodeRow = row;
		explodeCol = col;
		// Remove the grenade, dynamite.
		landArray[row][col].setItem(null);
		TheGame.getInstance().getController().explode(Math.abs(row - getLocalPlayer().getRow() + 3),
				Math.abs(col - getLocalPlayer().getCol()+ 3));

		// For each land object in the explosion radius.
		for(int i = row-1; i <= row+1; i++){
			for(int k = col-1; k <= col+1; k++){
				// If the land object is a street, check to see if there is an
				// enemy or player to damage, and destroy any existing items.
				try{

					int damageAmount = expType.getDamage();
					// Determine damage amount against the enemy based on his armor
					int enemyDamage, playerDamage;
					enemyDamage = playerDamage = 0;
					if(landArray[i][k].getEnemy() != null){
						enemyDamage = damageAmount - (int)(damageAmount *
								landArray[i][k].getEnemy().getArmor().getAbsorbOther() / 100.0);
					}
					// Determine damage amount against the player based on armor
					if(landArray[i][k].hasPlayer() > -1){
						playerDamage = damageAmount - (int)(damageAmount *
								players.get(landArray[i][k].hasPlayer(
								)).getReadiedArmor().getAbsorbOther() / 100.0);
					}

					if(landArray[i][k].getEnemy() != null){
						landArray[i][k].getEnemy().setHasBeenAttacked(true);
						Messages.getInstance().addMessage("The blast hit the " +
								landArray[i][k].getEnemy().getName() +
								" for " + enemyDamage + " hit points.");
						if(landArray[i][k].getEnemy().drainLife(enemyDamage)){
							Messages.getInstance().addMessage("The " +
									landArray[i][k].getEnemy().getName() +
							" is dead.");
							landArray[i][k].setEnemy(null);
						}
					}

					// Damage the player
					if(landArray[i][k].hasPlayer() >= 0){
						if(landArray[i][k].hasPlayer() == TheGame.getInstance().getLocalPlayerIndex()){
							Messages.getInstance().addMessage("You were caught in the explosion.");
						}
						else {
							Messages.getInstance().addMessage("Another player was caught in the explosino.");
						}
						players.get(landArray[i][k].hasPlayer()).drainLife(playerDamage);
					}

					// Destroy the item.
					landArray[i][k].setItem(null);

				} catch(ArrayIndexOutOfBoundsException aob) {
					
				} catch(Exception e) {e.printStackTrace();}

				// Create a new street if the land object can be destroyed.
				try{
					if(expType != ExplosionType.MINOR && landArray[i][k].isDestroyable()){
						setLandAt(i, k, new Street(
								StreetType.ST_000,
								-1, null, null, null));
					}
				} catch(Exception e) {}

			}
		}
	}

	/**
	 * Gets the Land object at a location in the array.
	 * @param row Row number as an int.
	 * @param col Column number as an int.
	 * @return The Land object at row, col.
	 */
	public Land getLandAt(int row, int col){
		try{
			landArray = cities.get(currentLevel);
			return landArray[row][col];
		} catch(Exception e ){ return null; }
	}

	/**
	 * Modifies the land object at the desired location.
	 * @param row The row on the map to modify.
	 * @param col The column on the map to modify.
	 * @param land The new land object.
	 */
	public void setLandAt(int row, int col, Land land){
		landArray = cities.get(currentLevel);
		landArray[row][col] = land;
	}

	/**
	 * Gets the start location.  The first element in the array
	 * will be the starting row, and the second element is the
	 * starting column.
	 *
	 * ToDo:
	 * Will need improvements if networking is ever implemented
	 * @return int[] : starting location
	 */
	public int[] getPlayerLocation(int level) throws NoSuchCityException {
		try {
			Land[][] firstCity = cities.get(level);
			int[] startLocation = new int[2];
			for(int row = 0; row < firstCity.length; row++){
				for(int col = 0; col < firstCity[0].length; col++){
					if(firstCity[row][col].hasPlayer() > -1){
						startLocation[0] = row;
						startLocation[1] = col;
					}
				}
			}
			return startLocation;
		} catch( IndexOutOfBoundsException aex){
			throw new NoSuchCityException();
		}
	}

	/**
	 * Gets the row next to the player in a specified direction.
	 * @param direction Direction from the player.
	 * @param row Row the player is currently at.
	 * @return int : Row adjacent to the player in the specified direction.
	 */
	public int getRowByDirection(Direction direction, int row){
		int newRow = 0;
		if(direction == Direction.NORTHWEST)
			newRow = row-1;
		else if(direction == Direction.NORTH)
			newRow = row-1;
		else if(direction == Direction.NORTHEAST)
			newRow = row-1;
		else if(direction == Direction.WEST)
			newRow = row;
		else if(direction == Direction.EAST)
			newRow = row;
		else if(direction == Direction.SOUTHEAST)
			newRow = row+1;
		else if(direction == Direction.SOUTH)
			newRow = row+1;
		else
			newRow = row+1;
		return newRow;
	}

	/**
	 * Gets the column next to the player in a specified direction.
	 * @param direction Direction from the player.
	 * @param col Column the player is currently at.
	 * @return int : Column adjacent to the player in the specified direction.
	 */
	public int getColByDirection(Direction direction, int col){
		int newCol = 0;
		if(direction == Direction.NORTHWEST)
			newCol = col-1;
		else if(direction == Direction.NORTH){
			newCol = col; }
		else if(direction == Direction.NORTHEAST){
			newCol = col+1; }
		else if(direction == Direction.WEST){
			newCol = col-1; }
		else if(direction == Direction.EAST){
			newCol = col+1; }
		else if(direction == Direction.SOUTHWEST){
			newCol = col-1; }
		else if(direction == Direction.SOUTH){
			newCol = col; }
		else{
			newCol = col+1; }
		return newCol;
	}

	/**
	 * Has the player enter the desired land object in the world.
	 * @param plr The player entering
	 * @param direction Direction player wants to move.
	 */
	public void enter(Player plr, Direction direction) throws PlayerEnteredException,
	EnemyDiedException {
		landArray = cities.get(currentLevel);
		int newRow = 0, newCol = 0;
		int oldRow = plr.getRow();
		int oldCol = plr.getCol();
		newRow = getRowByDirection(direction, plr.getRow());
		newCol = getColByDirection(direction, plr.getCol());

		try {
			landArray[newRow][newCol].enter(plr);
		} catch(src.exceptions.PlayerEnteredException entered){
			landArray[oldRow][oldCol].setPlayer(-1);
			plr.setRow(newRow);
			plr.setCol(newCol);
			throw new PlayerEnteredException();
		} catch(EnemyDiedException died){
			throw new EnemyDiedException();
		} catch(Exception ex){

		}
	}

	/**
	 * For every enemy in view of a player, this will tell the enemy to activate.
	 * Adds each enemy in view into an arraylist and activates them once they've
	 * all been accounted for.  This way enemies won't get activated more than once.
	 */
	public void activateEnemyAI(){
		int _row;
		int _col;
		ArrayList<Enemy> enemiesInRange = new ArrayList<Enemy>();
		for(Player p : players){
			_row = p.getRow();
			_col = p.getCol();
			for(int row = _row - 3; row <= _row + 3; row++){
				for(int col = _col - 3; col <= _col + 3; col++){
					try{
						if(getLandAt(row, col).getEnemy() != null)
							enemiesInRange.add(getLandAt(row, col).getEnemy());
					} catch(Exception e){}
				}
			}
		}
		for(int i = 0; i < enemiesInRange.size(); i++){
			enemiesInRange.get(i).activate();
		}
	}

	/**
	 * Checks to see whether or not the exits should be open or closed.  If
	 * the exits should be closed, the method returns false, true if they
	 * should be open.
	 * @return boolean : whether or not the gates of the exits should open.
	 */
	public boolean checkExits(){
		try{
			if(checkExitsLevel != currentLevel){
				fillCheckExitsArray();
				checkExitsLevel = currentLevel;
			}

			Land[][] checkLand;
			for(int i = 0; i < checkExitsArray.size(); i++){
				try {
					checkLand = cities.get(checkExitsArray.get(i));
				} catch(IndexOutOfBoundsException aex){
					continue;
				}

				for(int row = 0; row < checkLand.length; row++){
					for(int col = 0; col < checkLand[0].length; col++){
						if(checkLand[row][col].getEnemy() != null &&
								checkLand[row][col].getEnemy().getType().getIsLeader())
							return false;
					}
				}
			}

			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Finds the dungeons that are on the current level, and calls the
	 * checkDungeons method to find all of the levels connected to those
	 * dungeons.
	 */
	private void fillCheckExitsArray(){
		checkExitsArray = new ArrayList<Integer>();

		// Find all the dungeons, and add their indexes to array to search through.
		for(int row = 0; row < landArray.length; row++){
			for(int col = 0; col < landArray[0].length; col++){
				if(landArray[row][col] instanceof Exit &&
						((Exit)landArray[row][col]).getType() == ExitType.DUNGEON){
					int dunIndex = ((Exit)landArray[row][col]).getNextCity();
					if(!checkExitsArray.contains(dunIndex)) checkExitsArray.add(dunIndex);
				}
			}
		}

		checkDungeons();

		if(!checkExitsArray.contains(currentLevel)) checkExitsArray.add(currentLevel);
	}

	/**
	 * Recursively checks all levels listed in the checkExitsArray to fill up
	 * the checkExitsArray with all of the dungeons connected to the current
	 * dungeon system.
	 */
	private void checkDungeons(){
		Land[][] checkLand;
		for(int i = 0; i < checkExitsArray.size(); i++){
			try {
				checkLand = cities.get(checkExitsArray.get(i));
			} catch(IndexOutOfBoundsException aex){
				continue;
			}

			for(int row = 0; row < checkLand.length; row++){
				for(int col = 0; col < checkLand[0].length; col++){
					try {
						if(checkLand[row][col] instanceof Exit &&
								((Exit)checkLand[row][col]).getType() == ExitType.LADDER_UP ||
								((Exit)checkLand[row][col]).getType() == ExitType.LADDER_DOWN)
							if(!checkExitsArray.contains(((Exit)checkLand[row][col]).getNextCity())){
								checkExitsArray.add(((Exit)checkLand[row][col]).getNextCity());
								checkDungeons();
								return;
							}
					} catch(ClassCastException cex){
						continue;
					}
				}
			}
		}
	}

	/**
	 * Opens the gates in the game world if any can be open.  If none can
	 * be opened on the current level, check to see if the game should end.
	 * @return boolean : whether or not the game ended.
	 */
	public boolean openGates(){
		boolean opened = false;
		Land[][] land;
		for(int row = 0; row < landArray.length; row++){
			for(int col = 0; col < landArray[0].length; col++){

				if(landArray[row][col] instanceof Exit &&
						((Exit)landArray[row][col]).getIsOpen() == false){

					((Exit)landArray[row][col]).setIsOpen(true);
					Messages.getInstance().addMessage("A gate opens somewhere.");
					opened = true;
				}
			}
		}
		if(!opened){ 	// No new exits opened on this level, so check to see
			// if the game should end.  Search all of the cities
			// to see if there are any leaders left.
			boolean done = true;
			for(int i = 0; i < cities.size(); i++){
				land = cities.get(i);
				for(int row = 0; row < land.length; row++){
					for(int col = 0; col < land[0].length; col++){
						if(land[row][col].getEnemy() != null &&
								land[row][col].getEnemy().getType().getIsLeader()){
							done = false;
							break;
						}
					}
					if(!done) break;
				}
			}
			if(done && !endingMessageDisplayed){
				JOptionPane.showMessageDialog(
						null,
						MiscScenarioData.ENDING_MESSAGE + "\n\n\n" +
						"The game is over, but you can continue to play if you want to.",
						"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
				endingMessageDisplayed = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates everything in the world.
	 */
	public void triggerWorldUpdate(){
		// Reset explode row, col on the display.
		landArray = cities.get(currentLevel);
		TheGame.getInstance().getController().explode(-1, -1);

		for(Player p : players){

			// If in dungeon, chance to create new enemy
			if(p.getInDungeon()){
				EnemyMovement.chanceToSpawnEnemy(p.getRow(),
						p.getCol(), p.getLevel(), true);
			}

			ArrayList<Enemy> enemiesToActivate = new ArrayList<Enemy>();

			for(int row = p.getRow()-3; row <= p.getRow()+3; row++){
				for(int col = p.getCol()-3; col <= p.getCol()+3; col++){

					// For all enemies within range of this player, activate
					try {
						if(landArray[row][col].getEnemy() != null){
							enemiesToActivate.add(landArray[row][col].getEnemy());
						}

						// For all explosives within range, decrease timer
						if(landArray[row][col].getItem() != null &&
								landArray[row][col].getItem().getType().isExplosive()){
							if(landArray[row][col].getItem().decrementData() <= 0){
								worldExplode(row, col, landArray[row][col].getItem().getType().getExplosionType());
							}
						}
					} catch(ArrayIndexOutOfBoundsException aob){

					}
				}
			}

			for(Enemy e : enemiesToActivate){
				e.activate();
			}

		}
	}

	/**
	 * Spawns enemies around the player based on the type of
	 * trigger.
	 * @param plr Player : the player to spawn enemies around
	 * @param trig TriggerType : kind of trigger that was activated
	 */
	public void triggerEnemies(Player plr, StreetType trig){
		boolean spawned;
		// For all enemies to spawn
		for(Enemies enemy : trig.getEnemies()){
			spawned = false;

			for(int i = plr.getRow()-3; i <= plr.getRow()+3; i++){
				if(i < 0 || i >= landArray.length) continue;
				for(int k = plr.getCol()-3; k <= plr.getCol()+3; k++){
					if(k < 0 || k >= landArray[0].length) continue;

					if(landArray[i][k].isEnemyAccessible()){
						landArray[i][k].setEnemy(new Enemy(
								enemy, i, k, plr.getLevel()));
						spawned = true;
						break;
					}
				}
				if(spawned) break;
			}
		}
	}

	/**
	 * Checks to see if there is a boundary between the given player
	 * and enemy to see if there is a barrier between them that will
	 * stop projectils from passing through.  This method assumes that
	 * the enemy is within visual distance of the player and shares either
	 * a row or column with the player or that the enemy is perfectly
	 * diagonal to the player.
	 * @param plr Player : the player
	 * @param enemy Enemy : the enemy
	 * @return boolean : whether there is a boundary that stops bullets
	 */
	public boolean getBulletBoundaryBetween(Player plr, Enemy enemy){
		int plrRow, plrCol, enRow, enCol;
		plrRow = plr.getRow();
		plrCol = plr.getCol();
		enRow = enemy.getRow();
		enCol = enemy.getCol();

		Direction dir; // Direction enemy is relative to player

		if(plrRow > enRow && plrCol == enCol) 		dir = Direction.NORTH;
		else if(plrRow > enRow && plrCol < enCol) 	dir = Direction.NORTHEAST;
		else if(plrRow == enRow && plrCol < enCol) 	dir = Direction.EAST;
		else if(plrRow < enRow && plrCol < enCol) 	dir = Direction.SOUTHEAST;
		else if(plrRow < enRow && plrCol == enCol) 	dir = Direction.SOUTH;
		else if(plrRow < enRow && plrCol > enCol) 	dir = Direction.SOUTHWEST;
		else if(plrRow == enRow && plrCol > enCol) 	dir = Direction.WEST;
		else dir = Direction.NORTHWEST;

		int newRow1 = 0, newRow2 = 0, newRow3 = 0, newCol1 = 0, newCol2 = 0, newCol3 = 0;
		if(dir == Direction.NORTH){
			newRow1 = enRow+1;
			newRow2 = enRow+2;
			newRow3 = enRow+3;
			newCol1 = newCol2 = newCol3 = enCol;
		}
		else if(dir == Direction.NORTHEAST){
			newRow1 = enRow+1;
			newRow2 = enRow+2;
			newRow3 = enRow+3;
			newCol1 = enCol-1;
			newCol2 = enCol-2;
			newCol3 = enCol-3;
		}
		else if(dir == Direction.EAST){
			newRow1 = newRow2 = newRow3 = enRow;
			newCol1 = enCol-1;
			newCol2 = enCol-2;
			newCol3 = enCol-3;
		}
		else if(dir == Direction.SOUTHEAST){
			newRow1 = enRow-1;
			newRow2 = enRow-2;
			newRow3 = enRow-3;
			newCol1 = enCol-1;
			newCol2 = enCol-2;
			newCol3 = enCol-3;
		}
		else if(dir == Direction.SOUTH){
			newRow1 = enRow-1;
			newRow2 = enRow-2;
			newRow3 = enRow-3;
			newCol1 = newCol2 = newCol3 = enCol;
		}
		else if(dir == Direction.SOUTHWEST){
			newRow1 = enRow-1;
			newRow2 = enRow-2;
			newRow3 = enRow-3;
			newCol1 = enCol+1;
			newCol2 = enCol+2;
			newCol3 = enCol+3;
		}
		else if(dir == Direction.WEST){
			newRow1 = newRow2 = newRow3 = enRow;
			newCol1 = enCol+1;
			newCol2 = enCol+2;
			newCol3 = enCol+3;
		}
		else if(dir == Direction.NORTHWEST){
			newRow1 = enRow+1;
			newRow2 = enRow+2;
			newRow3 = enRow+3;
			newCol1 = enCol+1;
			newCol2 = enCol+2;
			newCol3 = enCol+3;
		}
		GameWorld gw = GameWorld.getInstance();
		if(gw.getLandAt(newRow1, newCol1).stopsProjectiles())
			return true;
		if(gw.getLandAt(newRow1, newCol1).hasPlayer() > -1){
			return false;
		}
		if(gw.getLandAt(newRow2, newCol2).stopsProjectiles())
			return true;
		if(gw.getLandAt(newRow2, newCol2).hasPlayer() > -1){
			return false;
		}
		if(gw.getLandAt(newRow3, newCol3).stopsProjectiles())
			return true;
		if(gw.getLandAt(newRow3, newCol3).hasPlayer() > -1){
			return false;
		}
		return true;
	}

	/**
	 * Accessor for the explode row for the display.
	 * @return int : explode row
	 */
	public int getExplodeRow(){
		return explodeRow;
	}

	/**
	 * Accessor for the explode column for the display.
	 * @return int : explode column
	 */
	public int getExplodeCol(){
		return explodeCol;
	}

	/**
	 * Mutator for explode row.
	 * @param row int : row
	 */
	public void setExplodeRow(int row){
		explodeRow = row;
	}

	/**
	 * Mutator for explode column.
	 * @param col int : column
	 */
	public void setExplodeCol(int col){
		explodeCol = col;
	}

	/**
	 * Accessor for the current city.
	 * @return Land[][] : Current city.
	 */
	public Land[][] getLand(){
		landArray = cities.get(currentLevel);
		return landArray;
	}

	/**
	 * Mutator for the current city.
	 * @param land Land[][] : Current city.
	 */
	public void setLand(Land[][] land){
		landArray = cities.get(currentLevel);
		landArray = land;
	}

	/**
	 * Accessor for the current level.
	 * @return int : Current level
	 */
	public int getCurrentLevel(){
		return currentLevel;
	}

	/**
	 * Mutator for the current level.
	 * @param level int : Current level
	 */
	public void setCurrentLevel(int level){
		currentLevel = level;
	}

	/**
	 * Accessor for the cities.
	 * @return ArrayList<Land[][]> : Cities.
	 */
	public ArrayList<Land[][]> getCities(){
		return cities;
	}

	/**
	 * Mutator for the cities.
	 * @param cities ArrayList<Land[][]> : Cities.
	 */
	public void setCities(ArrayList<Land[][]> cities){
		this.cities = cities;
	}

	/**
	 * Saves the land to an output stream.
	 * @param oos ObjectOutputStream
	 */
	public void save(ObjectOutputStream oos){
		try{
			oos.writeObject(cities.size());
			oos.writeObject(cityNames);
			if(!src.Constants.EDITOR)
				oos.writeObject(currentLevel);
			else oos.writeObject(0);
			for(int i = 0; i < cities.size(); i++){
				oos.writeObject(cities.get(i).length);
				oos.writeObject(cities.get(i)[0].length);
				for(int row = 0; row < cities.get(i).length; row++){
					for(int col = 0; col < cities.get(i)[0].length; col++){

						oos.writeObject(cities.get(i)[row][col].getLandType());

						cities.get(i)[row][col].save(oos);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads the land from an input stream.
	 * @param ois ObjectInputStream
	 */
	@SuppressWarnings("unchecked")
	public void load(ObjectInputStream ois){
		cities = new ArrayList<Land[][]>();
		Land[][] land;
		int type = 0;
		try {
			int numLevels = (Integer) ois.readObject();
			cityNames = (ArrayList<String>) ois.readObject();
			int curLevel = (Integer) ois.readObject();
			for(int i = 0; i < numLevels; i++){
				int rowLength, colLength;
				rowLength = (Integer) ois.readObject();
				colLength = (Integer) ois.readObject();
				land = new Land[rowLength][colLength];
				for(int row = 0; row < land.length; row++){
					for(int col = 0; col < land[0].length; col++){
						type = (Integer) ois.readObject();

						// Have to initialize the land[row][col] to avoid
						// null pointer on load.  You have to know which
						// type of land object to initialize it to so that
						// you can polymorphically call the right load method.
						if(type == LandType.EXIT.getTypeNum()){
							land[row][col] = new Exit(null, 0, 0, 0, false);
							land[row][col].load(ois);
						}
						else if(type == LandType.SHOP.getTypeNum()){
							land[row][col] = new Shop(null, 0);
							land[row][col].load(ois);
						}
						else if(type == LandType.STREET.getTypeNum()){
							land[row][col] = new Street(null, 0, null, null, null);
							land[row][col].load(ois);

							// Get fresh enemy with stats from XML file if debugging a scenario.
							if(src.Constants.SCENARIO_DEBUG){
								if(land[row][col].getEnemy() != null){
									land[row][col].setEnemy(new Enemy(land[row][col].getEnemy().getType(),
											row, col, land[row][col].getEnemy().getAdvanced()));
								}
							}

							/*if( ((Street)land[row][col]).getEnemy() != null)
								((Street)land[row][col]).getEnemy().setWorld(this);*/
						}
						else if(type == LandType.OBSTRUCTION.getTypeNum()){
							land[row][col] = new Obstruction(null, false);
							land[row][col].load(ois);
						}
						/*else if(type == LandType.WATER.getTypeNum()){
							land[row][col] = new Water();
							land[row][col].load(ois);
						}*/
					}
				}
				cities.add(land);
			}
			if(!src.Constants.EDITOR)
				currentLevel = curLevel;
			TheGame.getInstance().setHasBeenCleared(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
