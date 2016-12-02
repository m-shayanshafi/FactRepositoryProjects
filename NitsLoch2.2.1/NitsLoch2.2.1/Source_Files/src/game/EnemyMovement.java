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
import java.util.Random;

import src.enums.Direction;
import src.enums.Enemies;
import src.enums.EnemyBehavior;
import src.enums.ExplosionType;
import src.enums.Facing;
import src.exceptions.EnemySpawnNotDefinedException;
import src.exceptions.NoPlayerException;
import src.land.Land;
import src.land.Obstruction;
import src.scenario.MiscScenarioData;

/**
 * This class will perform movement on specified enemies.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class EnemyMovement {

	/**
	 * If the random number decides to spawn an enemy, it will find
	 * a spot around the given row and column to create a new enemy.
	 * @param plrRow int : player's row
	 * @param plrCol int : player's column
	 * @param plrLevel int: player's level
	 */
	public static void chanceToSpawnEnemy(int plrRow, int plrCol,
			int plrLevel, boolean inDungeon){

		if(!inDungeon && Math.random() > MiscScenarioData.SPAWN_CHANCE) return;
		else if(inDungeon && Math.random() > MiscScenarioData.SPAWN_CHANCE_DUN) return;

		Land[][] temp = GameWorld.getInstance().getGameWorld(plrRow, plrCol);

		/*
		 * Different for loops to randomize the spawn location of the enemy.
		 * Each for loop will start looking for valid places to spawn an enemy
		 * at different corners of the screen.  This should add some variety.
		 */
		double rand = Math.random();
		Enemies enemyThatWillSpawn;
		try {
			enemyThatWillSpawn = getRandomSpawnedEnemy(inDungeon);
		} catch(EnemySpawnNotDefinedException ex){
			return;
		}
		if(enemyThatWillSpawn.getName() == null) return;
		if(rand < .25){

			// Check land objects in the view of the player until
			// find one that can take a new enemy.
			for(int row = 0; row < temp.length; row++){
				for(int col = 0; col < temp[0].length; col++){

					// Create enemy and assign it to the world.
					if(temp[row][col].isEnemyAccessible()){
						Enemy newEnemy = new Enemy(enemyThatWillSpawn,
								plrRow + row - 3,
								plrCol + col - 3,
								plrLevel);
						GameWorld.getInstance().getLandAt(plrRow + row - 3,
								plrCol + col - 3).setEnemy(newEnemy);
						Messages.getInstance().addMessage("An angry " +
								enemyThatWillSpawn.getName() + " appears.");
						return;
					}
				}
			}
		}
		else if(rand < .5){
			for(int row = temp.length-1; row >= 0; row--){
				for(int col = 0; col < temp[0].length; col++){

					// Create enemy and assign it to the world.
					if(temp[row][col].isEnemyAccessible()){
						Enemy newEnemy = new Enemy(enemyThatWillSpawn,
								plrRow + row - 3,
								plrCol + col - 3,
								plrLevel);
						GameWorld.getInstance().getLandAt(plrRow + row - 3,
								plrCol + col - 3).setEnemy(newEnemy);
						Messages.getInstance().addMessage("An angry " +
								enemyThatWillSpawn.getName() + " appears.");
						return;
					}
				}
			}
		}
		else if(rand < .75){
			for(int row = temp.length-1; row >= 0; row--){
				for(int col = temp[0].length-1; col >= 0; col--){

					// Create enemy and assign it to the world.
					if(temp[row][col].isEnemyAccessible()){
						Enemy newEnemy = new Enemy(enemyThatWillSpawn,
								plrRow + row - 3,
								plrCol + col - 3,
								plrLevel);
						GameWorld.getInstance().getLandAt(plrRow + row - 3,
								plrCol + col - 3).setEnemy(newEnemy);
						Messages.getInstance().addMessage("An angry " +
								enemyThatWillSpawn.getName() + " appears.");
						return;
					}
				}
			}
		}
		else{
			for(int row = 0; row < temp.length; row++){
				for(int col = temp[0].length-1; col >= 0; col--){

					// Create enemy and assign it to the world.
					if(temp[row][col].isEnemyAccessible()){
						Enemy newEnemy = new Enemy(enemyThatWillSpawn,
								plrRow + row - 3,
								plrCol + col - 3,
								plrLevel);
						GameWorld.getInstance().getLandAt(plrRow + row - 3,
								plrCol + col - 3).setEnemy(newEnemy);
						Messages.getInstance().addMessage("An angry " +
								enemyThatWillSpawn.getName() + " appears.");
						return;
					}
				}
			}
		}
	}

	/**
	 * Spawns a shopkeeper near the shop at the specified location.
	 * @param row int : row of shop
	 * @param col int : column of shop
	 * @param plr Player : the player near the shop
	 */
	public static void spawnShopkeeper(int row, int col, Player plr){
		int keeperLevel = plr.getLevel()-1;
		if(keeperLevel < 1) keeperLevel = 1;

		// Try to find a spot to spawn the shopkeeper closer to the shop
		// first, then move out.
		for(int i = row-1; i <= row+1; i++){
			for(int k = col-1; k <= col+1; k++){
				if(spawnIfPossible(i, k)) return;
			}
		}
		for(int i = row-2; i <= row+2; i++){
			for(int k = col-2; k <= col+2; k++){
				if(spawnIfPossible(i, k)) return;
			}
		}
		for(int i = row-3; i <= row+3; i++){
			for(int k = col-3; k <= col+3; k++){
				if(spawnIfPossible(i, k)) return;
			}
		}
	}

	/**
	 * Checks to see if the specified location in the world can
	 * hold a new shopkeeper.  If it can, create a new shopkeeper
	 * at that location and return true.  Otherwise, return false.
	 * @param row int : row to try to spawn shopkeeper
	 * @param col int : column to try to spawn shopkeeper
	 * @return boolean : whether or not spawning was a success
	 */
	private static boolean spawnIfPossible(int row, int col){
		try {
			if(GameWorld.getInstance().getLandAt(row, col).isEnemyAccessible()){
				GameWorld.getInstance().getLandAt(row, col).setEnemy(
						new Enemy(
								ShopkeeperSpawns.getInstance().getShopkeeper(
										GameWorld.getInstance().getCurrentLevel()),
										row,
										col, 
										GameWorld.getInstance().getLocalPlayer().getLevel()));
				return true;
			}
		} catch(Exception ex) { return false; }
		return false;
	}

	/**
	 * Gets a random type of enemy that can be spawned at the current level.
	 * @return Enemies : enemy type that will spawn
	 */
	private static Enemies getRandomSpawnedEnemy(boolean inDungeon)
	throws EnemySpawnNotDefinedException {
		try {
			ArrayList<Enemies> list;
			if(inDungeon){
				try {
					list = DungeonSpawns.getInstance().getEnemies(
							GameWorld.getInstance().getPlayer(
									TheGame.getInstance().getLocalPlayerIndex()).getDungeonLevel());
				} catch(NoPlayerException plrEx){
					return null;
				}
			}
			else {
				list = CitySpawns.getInstance().getEnemies(
						GameWorld.getInstance().getCurrentLevel());
			}
			Random generator = new Random();
			int randNum = generator.nextInt(list.size());
			return list.get(randNum);
		} catch(EnemySpawnNotDefinedException ex){
			throw new EnemySpawnNotDefinedException();
		}
	}

	/**
	 * Checks to see if the enemy is in attacking range of the player.
	 * In a dungeon, the enemy cannot attack diagonally, but outside
	 * the enemy can.
	 * @param enemy Enemy : the attacking enemy
	 * @param plr Player : the player being attacked
	 * @return boolean : in range
	 */
	private static boolean enemyInAttackingRange(Enemy enemy, Player plr){
		if(plr.getInDungeon()){
			if(
					( Math.abs(enemy.getRow() - plr.getRow()) < 2 && enemy.getCol() == plr.getCol()) ||
					( Math.abs(enemy.getCol() - plr.getCol()) < 2 && enemy.getRow() == plr.getRow())
			)
				return true;
			// Check to see if the enemy can shoot at you from a distance.
			else if(enemy.getWeapon().getDamageType().isMarksman()){
				if(
						(( Math.abs(enemy.getRow() - plr.getRow()) < 4 && enemy.getCol() == plr.getCol()) ||
						( Math.abs(enemy.getCol() - plr.getCol()) < 4 && enemy.getRow() == plr.getRow())) &&
						!GameWorld.getInstance().getBulletBoundaryBetween(plr, enemy)
				)
					return true;
				else return false;
			}
			else return false;
		}
		else {
			// Set facing
			Direction dir = determineRunningDirection(enemy.getRow(), enemy.getCol(),
					plr.getRow(), plr.getCol());
			if(dir == Direction.EAST || dir == Direction.NORTHEAST || dir == Direction.SOUTHEAST)
				enemy.setFacing(Facing.LEFT);
			else if(dir == Direction.WEST || dir == Direction.NORTHWEST || dir == Direction.SOUTHWEST)
				enemy.setFacing(Facing.RIGHT);
			
			if( Math.abs(enemy.getRow() - plr.getRow()) < 2 &&
					Math.abs(enemy.getCol() - plr.getCol()) < 2)
				return true;
			// Check to see if the enemy can shoot at you from a distance.
			else if(enemy.getWeapon().getDamageType().isMarksman()){
				// If player is to the right, left, up or down from enemy.
				if(
						(( Math.abs(enemy.getRow() - plr.getRow()) < 4 && enemy.getCol() == plr.getCol()) ||
						( Math.abs(enemy.getCol() - plr.getCol()) < 4 && enemy.getRow() == plr.getRow())) &&
						!GameWorld.getInstance().getBulletBoundaryBetween(plr, enemy)
				)
					return true;
				// If player is diagonal to the enemy.
				if(
						Math.abs(enemy.getRow() - plr.getRow()) < 4 &&
						Math.abs(enemy.getCol() - plr.getCol()) < 4 &&
						( Math.abs(enemy.getRow() - plr.getRow()) == 
							Math.abs(enemy.getCol() - plr.getCol())) &&
						!GameWorld.getInstance().getBulletBoundaryBetween(plr, enemy)
				)
					return true;
				else return false;
			}
			else return false;
		}
	}

	/**
	 * Will activate the specified enemy.
	 * @param enemy The enemy to activate.
	 */
	public static void activate(Enemy enemy){
		Player plr = GameWorld.getInstance().getClosestPlayer(
				enemy.getRow(), enemy.getCol());

		if(!enemy.getHasBeenAttacked() &&
				enemy.getType().getBehavior() == EnemyBehavior.INNOCENT){
			return;
		}

		if(enemyInAttackingRange(enemy, plr) && !enemy.getIsRunning() && 
				enemy.getType().getBehavior() != EnemyBehavior.COWARDLY &&
				enemy.getType().getBehavior() != EnemyBehavior.CLINGY &&
				!enemy.getWeapon().usesRockets()){
			Battles.fight(enemy, plr);
		}

		else if(enemy.getType().getBehavior() == EnemyBehavior.TERRITORIAL) return;

		// If the enemy is running
		else if(enemy.getIsRunning()){
			if(!plr.getInDungeon())
				enemy.move(determineRunningDirection(enemy.getRow(), enemy.getCol(),
						plr.getRow(), plr.getCol()));
			else 
				enemy.moveDungeon(determineRunningDirection(enemy.getRow(), enemy.getCol(),
						plr.getRow(), plr.getCol()));
		}
		
		// Enemy is using rockets
		else if(enemy.getWeapon().usesRockets()){
			shoot(enemy, determineRunningDirection(plr.getRow(), plr.getCol(),
					enemy.getRow(), enemy.getCol()));
		}

		// Enemy should move in to attack the player
		// Treat the player as the "runner"
		else {
			enemy.move(determineRunningDirection(plr.getRow(), plr.getCol(),
					enemy.getRow(), enemy.getCol()));
		}

	}

	/**
	 * Calculates the direction the enemy should move.
	 * @param runnerRow int : row of the target enemy/player
	 * @param runnerCol int : column of the target enemy/player
	 * @param chaserRow int : row of the player/enemy chasing target
	 * @param chaserCol int : column of the player.enemy chasing target
	 * @return Direction : direction the enemy should move
	 */
	public static Direction determineRunningDirection(int runnerRow, int runnerCol,
			int chaserRow, int chaserCol){
		if(runnerRow > chaserRow && runnerCol > chaserCol)
			return Direction.SOUTHEAST;
		else if(runnerRow > chaserRow && runnerCol == chaserCol)
			return Direction.SOUTH;
		else if(runnerRow > chaserRow && runnerCol < chaserCol)
			return Direction.SOUTHWEST;
		else if(runnerRow == chaserRow && runnerCol > chaserCol)
			return Direction.EAST;
		else if(runnerRow == chaserRow && runnerCol < chaserCol)
			return Direction.WEST;
		else if(runnerRow < chaserRow && runnerCol > chaserCol)
			return Direction.NORTHEAST;
		else if(runnerRow < chaserRow && runnerCol == chaserCol)
			return Direction.NORTH;
		else return Direction.NORTHWEST;
	}
	
	/**
	 * Enemy has a 50% chance of shooting a rocket in the specified
	 * direction.
	 * @param enemy Enemy : the enemy that is shooting
	 * @param direction Direction : direction
	 */
	private static void shoot(Enemy enemy, Direction direction){
		if(direction == Direction.EAST || direction == Direction.NORTHEAST ||
				direction == Direction.SOUTHEAST)
			enemy.setFacing(Facing.RIGHT);
		else if(direction == Direction.WEST || direction == Direction.NORTHWEST ||
				direction == Direction.SOUTHEAST)
			enemy.setFacing(Facing.LEFT);
		
		if(Math.random() < .5) return; // Sometimes they don't shoot.
		
		int newRow1 = 0, newRow2 = 0, newRow3 = 0, newCol1 = 0, newCol2 = 0, newCol3 = 0;
		if(direction == Direction.NORTH){
			newRow1 = enemy.getRow()-1;
			newRow2 = enemy.getRow()-2;
			newRow3 = enemy.getRow()-3;
			newCol1 = newCol2 = newCol3 = enemy.getCol();
		}
		else if(direction == Direction.NORTHEAST){
			newRow1 = enemy.getRow()-1;
			newRow2 = enemy.getRow()-2;
			newRow3 = enemy.getRow()-3;
			newCol1 = enemy.getCol()+1;
			newCol2 = enemy.getCol()+2;
			newCol3 = enemy.getCol()+3;
		}
		else if(direction == Direction.EAST){
			newRow1 = newRow2 = newRow3 = enemy.getRow();
			newCol1 = enemy.getCol()+1;
			newCol2 = enemy.getCol()+2;
			newCol3 = enemy.getCol()+3;
		}
		else if(direction == Direction.SOUTHEAST){
			newRow1 = enemy.getRow()+1;
			newRow2 = enemy.getRow()+2;
			newRow3 = enemy.getRow()+3;
			newCol1 = enemy.getCol()+1;
			newCol2 = enemy.getCol()+2;
			newCol3 = enemy.getCol()+3;
		}
		else if(direction == Direction.SOUTH){
			newRow1 = enemy.getRow()+1;
			newRow2 = enemy.getRow()+2;
			newRow3 = enemy.getRow()+3;
			newCol1 = newCol2 = newCol3 = enemy.getCol();
		}
		else if(direction == Direction.SOUTHWEST){
			newRow1 = enemy.getRow()+1;
			newRow2 = enemy.getRow()+2;
			newRow3 = enemy.getRow()+3;
			newCol1 = enemy.getCol()-1;
			newCol2 = enemy.getCol()-2;
			newCol3 = enemy.getCol()-3;
		}
		else if(direction == Direction.WEST){
			newRow1 = newRow2 = newRow3 = enemy.getRow();
			newCol1 = enemy.getCol()-1;
			newCol2 = enemy.getCol()-2;
			newCol3 = enemy.getCol()-3;
		}
		else if(direction == Direction.NORTHWEST){
			newRow1 = enemy.getRow()-1;
			newRow2 = enemy.getRow()-2;
			newRow3 = enemy.getRow()-3;
			newCol1 = enemy.getCol()-1;
			newCol2 = enemy.getCol()-2;
			newCol3 = enemy.getCol()-3;
		}
		
		GameWorld gw = GameWorld.getInstance();
		if(gw.getLandAt(newRow1, newCol1) instanceof Obstruction)
			return;
		
		if(gw.getLandAt(newRow1, newCol1).hasPlayer() > -1){
			gw.worldExplode(newRow1, newCol1, ExplosionType.MAJOR);
			Messages.getInstance().addMessage("The " + enemy.getName() + 
			" fires a rocket at you.");
			return;
		}
		if(gw.getLandAt(newRow2, newCol2).hasPlayer() > -1 ||
				gw.getLandAt(newRow2, newCol2).isDestroyable() ||
				gw.getLandAt(newRow2, newCol2) instanceof Obstruction){
			gw.worldExplode(newRow2, newCol2, ExplosionType.MAJOR);
			Messages.getInstance().addMessage("The " + enemy.getName() + 
			" fires a rocket at you.");
			return;
		}
		if(gw.getLandAt(newRow3,newCol3).hasPlayer() > -1 ||
				gw.getLandAt(newRow3, newCol3).isDestroyable() ||
				gw.getLandAt(newRow3, newCol3) instanceof Obstruction){
			gw.worldExplode(newRow3, newCol3, ExplosionType.MAJOR);
			Messages.getInstance().addMessage("The " + enemy.getName() + 
			" fires a rocket at you.");
			return;
		}
	}

	/**
	 * Will attempt to move the enemy in the specified direction. If the
	 * enemy cannot move in the desired direction, he will continue to
	 * try other directions until he find one that succeeds, or he runs
	 * out of options.
	 * @param enemy Enemy : enemy that is moving
	 * @param direction Direction : desired direction
	 */
	public static void move(Enemy enemy, Direction direction){
		if(direction == Direction.NORTH){
			if(!tryMove(enemy, Direction.NORTH))
				if(!tryMove(enemy, Direction.NORTHEAST))
					if(!tryMove(enemy, Direction.NORTHWEST))
						if(!tryMove(enemy, Direction.WEST))
							if(!tryMove(enemy, Direction.EAST))
								if(!tryMove(enemy, Direction.SOUTHEAST))
									if(!tryMove(enemy, Direction.SOUTHWEST))
										tryMove(enemy, Direction.SOUTH);
		}
		else if(direction == Direction.NORTHEAST){
			if(!tryMove(enemy, Direction.NORTHEAST))
				if(!tryMove(enemy, Direction.NORTH))
					if(!tryMove(enemy, Direction.EAST))
						if(!tryMove(enemy, Direction.NORTHWEST))
							if(!tryMove(enemy, Direction.SOUTHEAST))
								if(!tryMove(enemy, Direction.WEST))
									if(!tryMove(enemy, Direction.SOUTH))
										tryMove(enemy, Direction.SOUTHWEST);
		}
		else if(direction == Direction.EAST){
			if(!tryMove(enemy, Direction.EAST))
				if(!tryMove(enemy, Direction.NORTHEAST))
					if(!tryMove(enemy, Direction.SOUTHEAST))
						if(!tryMove(enemy, Direction.NORTH))
							if(!tryMove(enemy, Direction.SOUTH))
								if(!tryMove(enemy, Direction.SOUTHWEST))
									if(!tryMove(enemy, Direction.NORTHWEST))
										tryMove(enemy, Direction.WEST);
		}
		else if(direction == Direction.SOUTHEAST){
			if(!tryMove(enemy, Direction.SOUTHEAST))
				if(!tryMove(enemy, Direction.SOUTH))
					if(!tryMove(enemy, Direction.EAST))
						if(!tryMove(enemy, Direction.NORTHEAST))
							if(!tryMove(enemy, Direction.SOUTHWEST))
								if(!tryMove(enemy, Direction.NORTH))
									if(!tryMove(enemy, Direction.WEST))
										tryMove(enemy, Direction.NORTHWEST);
		}
		else if(direction == Direction.SOUTH){
			if(!tryMove(enemy, Direction.SOUTH))
				if(!tryMove(enemy, Direction.SOUTHWEST))
					if(!tryMove(enemy, Direction.SOUTHEAST))
						if(!tryMove(enemy, Direction.WEST))
							if(!tryMove(enemy, Direction.EAST))
								if(!tryMove(enemy, Direction.NORTHWEST))
									if(!tryMove(enemy, Direction.NORTHEAST))
										tryMove(enemy, Direction.NORTH);
		}
		else if(direction == Direction.SOUTHWEST){
			if(!tryMove(enemy, Direction.SOUTHWEST))
				if(!tryMove(enemy, Direction.WEST))
					if(!tryMove(enemy, Direction.SOUTH))
						if(!tryMove(enemy, Direction.NORTHWEST))
							if(!tryMove(enemy, Direction.SOUTHEAST))
								if(!tryMove(enemy, Direction.NORTH))
									if(!tryMove(enemy, Direction.EAST))
										tryMove(enemy, Direction.NORTHEAST);
		}
		else if(direction == Direction.WEST){
			if(!tryMove(enemy, Direction.WEST))
				if(!tryMove(enemy, Direction.NORTHWEST))
					if(!tryMove(enemy, Direction.SOUTHWEST))
						if(!tryMove(enemy, Direction.NORTH))
							if(!tryMove(enemy, Direction.SOUTH))
								if(!tryMove(enemy, Direction.NORTHEAST))
									if(!tryMove(enemy, Direction.SOUTHEAST))
										tryMove(enemy, Direction.EAST);
		}
		else if(direction == Direction.NORTHWEST){
			if(!tryMove(enemy, Direction.NORTHWEST))
				if(!tryMove(enemy, Direction.NORTH))
					if(!tryMove(enemy, Direction.WEST))
						if(!tryMove(enemy, Direction.SOUTHWEST))
							if(!tryMove(enemy, Direction.NORTHEAST))
								if(!tryMove(enemy, Direction.SOUTH))
									if(!tryMove(enemy, Direction.EAST))
										tryMove(enemy, Direction.SOUTHEAST);
		}
	}

	/**
	 * Will attempt to move the enemy in the specified direction. If the
	 * enemy cannot move in the desired direction, he will continue to
	 * try other directions until he find one that succeeds, or he runs
	 * out of options.  In a dungeon, the enemy cannot move diagonally.
	 * @param enemy Enemy : enemy that is moving
	 * @param direction Direction : desired direction
	 */
	public static void moveDungeon(Enemy enemy, Direction direction){
		if(direction == Direction.NORTH){
			if(!tryMove(enemy, Direction.NORTH))
				if(!tryMove(enemy, Direction.WEST))
					if(!tryMove(enemy, Direction.EAST))
						tryMove(enemy, Direction.SOUTH);
		}
		else if(direction == Direction.NORTHEAST){
			if(!tryMove(enemy, Direction.NORTH))
				if(!tryMove(enemy, Direction.EAST))
					if(!tryMove(enemy, Direction.WEST))
						tryMove(enemy, Direction.SOUTH);
		}
		else if(direction == Direction.EAST){
			if(!tryMove(enemy, Direction.EAST))
				if(!tryMove(enemy, Direction.NORTH))
					if(!tryMove(enemy, Direction.SOUTH))
						tryMove(enemy, Direction.WEST);
		}
		else if(direction == Direction.SOUTHEAST){
			if(!tryMove(enemy, Direction.SOUTH))
				if(!tryMove(enemy, Direction.EAST))
					if(!tryMove(enemy, Direction.NORTH))
						tryMove(enemy, Direction.WEST);
		}
		else if(direction == Direction.SOUTH){
			if(!tryMove(enemy, Direction.SOUTH))
				if(!tryMove(enemy, Direction.WEST))
					if(!tryMove(enemy, Direction.EAST))
						tryMove(enemy, Direction.NORTH);
		}
		else if(direction == Direction.SOUTHWEST){
			if(!tryMove(enemy, Direction.WEST))
				if(!tryMove(enemy, Direction.SOUTH))
					if(!tryMove(enemy, Direction.NORTH))
						tryMove(enemy, Direction.EAST);
		}
		else if(direction == Direction.WEST){
			if(!tryMove(enemy, Direction.WEST))
				if(!tryMove(enemy, Direction.NORTH))
					if(!tryMove(enemy, Direction.SOUTH))
						tryMove(enemy, Direction.EAST);
		}
		else if(direction == Direction.NORTHWEST){
			if(!tryMove(enemy, Direction.NORTH))
				if(!tryMove(enemy, Direction.WEST))
					if(!tryMove(enemy, Direction.SOUTH))
						tryMove(enemy, Direction.EAST);
		}
	}

	/**
	 * Attempts to move the enemy in the specified direction.  Returns
	 * whether or not the move was successful.
	 * @param enemy The enemy moving
	 * @param direction Direction : the direction to move
	 * @return boolean : success
	 */
	private static boolean tryMove(Enemy enemy, Direction direction){
		int newRow = enemy.getRow(), newCol = enemy.getCol();
		if(direction == Direction.NORTH){
			newRow--;
		}
		else if(direction == Direction.NORTHEAST) {
			enemy.setFacing(Facing.RIGHT);
			newRow--;
			newCol++;
		}
		else if(direction == Direction.EAST){
			enemy.setFacing(Facing.RIGHT);
			newCol++;
		}
		else if(direction == Direction.SOUTHEAST){
			enemy.setFacing(Facing.RIGHT);
			newRow++;
			newCol++;
		}
		else if(direction == Direction.SOUTH){
			newRow++;
		}
		else if(direction == Direction.SOUTHWEST){
			enemy.setFacing(Facing.LEFT);
			newRow++;
			newCol--;
		}
		else if(direction == Direction.WEST){
			enemy.setFacing(Facing.LEFT);
			newCol--;
		}
		else {
			enemy.setFacing(Facing.LEFT);
			newRow--;
			newCol--;
		}
		boolean moved = GameWorld.getInstance().getLandAt(newRow, newCol).enter(enemy);
		if(moved){
			GameWorld.getInstance().getLandAt(enemy.getRow(), enemy.getCol()).setEnemy(null);
			enemy.setRow(newRow);
			enemy.setCol(newCol);
		}
		return moved;
	}

}
