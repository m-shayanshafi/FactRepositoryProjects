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

import src.enums.ExitType;
import src.enums.LandType;
import src.game.Enemy;
import src.game.GameWorld;
import src.game.Item;
import src.game.Messages;
import src.game.NPC;
import src.game.Player;
import src.game.TheGame;


/**
 * Exit land object.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Exit implements Land {

	private ExitType type;
	private int nextCity;
	private int destinationRow;
	private int destinationCol;
	private boolean isOpen;
	private boolean hasBeenExplored;

	/**
	 * Creates a new exit.  Needs to know the type of exit and the
	 * destination city and location.  Depending on the type of
	 * exit, the destination location may be ignored.  The exit
	 * also needs to know if it's open.
	 * @param type ExitType : type of exit.
	 * @param nextCity int : next city
	 * @param destinationRow int : destination row in city
	 * @param destinationCol int : destination column in city
	 * @param isOpen boolean : whether or not the exit is open for use.
	 */
	public Exit(
			ExitType type,
			int nextCity,
			int destinationRow,
			int destinationCol,
			boolean isOpen){
		this.type = type;
		this.nextCity = nextCity;
		this.destinationRow = destinationRow;
		this.destinationCol = destinationCol;
		this.isOpen = isOpen;

		hasBeenExplored = false;
	}

	/**
	 * Returns the land type "exit".
	 * @return LandType : Exit.
	 */
	public int getLandType(){
		return LandType.EXIT.getTypeNum();
	}

	/**
	 * Accessor for the type of exit.
	 * @return ExitType : type of exit.`
	 */
	public ExitType getType(){
		return type;
	}

	/**
	 * Accessor for whether or not this exit is open.
	 * @return boolean : open
	 */
	public boolean getIsOpen(){
		return isOpen;
	}

	/**
	 * Sets whether or not this exit is open.
	 * @param bool boolean : open
	 */
	public void setIsOpen(boolean bool){
		isOpen = bool;
	}

	/**
	 * Does nothing for the exit class.
	 */
	public void setItem(Item item){ return; }

	/**
	 * Player is never in an exit object.
	 */
	public int hasPlayer(){
		return -1;
	}

	/**
	 * Sets whether or not this Exit has been explored.
	 * @param bool Boolean
	 */
	public void setHasBeenExplored(boolean bool){
		hasBeenExplored = bool;
	}

	/**
	 * Accessor for whether or not this exit has been explored.
	 */
	public boolean hasBeenExplored(){
		return hasBeenExplored;
	}

	/**
	 * Returns null for exit objects.
	 */
	public Item getItem(){
		return null;
	}

	/**
	 * Does nothing for the exit class.
	 */
	public void addItem(Item i) { return; }

	/**
	 * Does nothing for the exit class.
	 */
	public void addEnemy(Enemy e){ return; }

	/**
	 * Does nothing for the exit class.
	 */
	public void setEnemy(Enemy enemy){ return; }

	/**
	 * Does nothing for exit class.
	 */
	public void setNPC(NPC npc){ return; }

	/**
	 * Always returns null for exit objects.
	 */
	public Enemy getEnemy(){ return null; }

	/**
	 * Returns null for exit objects.
	 */
	public NPC getNPC() { return null; }

	/**
	 * Exits cannot be destroyed.
	 */
	public boolean isDestroyable(){
		return false;
	}

	/**
	 * Exits are never accessible to enemies.
	 */
	public boolean isEnemyAccessible(){
		return false;
	}

	/**
	 * Returns the image location of this exit.
	 * @return String : image location
	 */
	public String getImage(){
		if(isOpen) return type.getOpenImage();
		else return type.getClosedImage();
	}

	/**
	 * Returns the same as getImage for exits, since there can
	 * be no object on this land object.
	 * @return String : image location
	 */
	public String getLandImage(){
		return getImage();
	}

	/**
	 * Player can enter if the door is open, otherwise he can't.  If the
	 * door is open, it will take the player to the next city.
	 */
	public boolean canEnter(){
		if(!isOpen) return false;
		else{
			return true;
		}
	}

	/**
	 * Does nothing for exit land objects.
	 */
	public void setPlayer(int plr) { }

	/**
	 * If the player can enter it will transport the player to the
	 * new location.  It will treat the different types of Exits
	 * differently.
	 * @param plr The player entering the exit.
	 */
	public void enter(Player plr){
		GameWorld world = GameWorld.getInstance();
		if(type == ExitType.CITY_GATE && canEnter()){
			try{
				if(world.getCities().get(nextCity) != null){
					if(world.getCurrentLevel() == nextCity){
						world.getLandAt(plr.getRow(), plr.getCol()).setPlayer(-1);
						plr.setRow(destinationRow);
						plr.setCol(destinationCol);
						world.getLandAt(plr.getRow(), plr.getCol()).setPlayer(plr.getIndex());
					}
					else {
						world.setCurrentLevel(nextCity);
						plr.setLocation(nextCity);
					}
					TheGame.getInstance().setHasBeenCleared(false);
				}
				else Messages.getInstance().addMessage("Next city does not exist.");
			} catch(Exception e){
				Messages.getInstance().addMessage("Next city does not exist.");
			}
		}
		else if(type == ExitType.DUNGEON && canEnter()){
			try{
				if(world.getCities().get(nextCity) != null){
					plr.addToDungeonPath(world.getCurrentLevel()); // Add new ladder up destination.
					
					world.setCurrentLevel(nextCity);
					plr.setLocation(nextCity);
					plr.setInDungeon(true);
					plr.setDungeonLevel(destinationRow);
					
					TheGame.getInstance().getController().blankScreenDungeon();
					TheGame.getInstance().setHasBeenCleared(false);
				}
				else Messages.getInstance().addMessage("Dungeon does not exist.");
			} catch(Exception e){
				Messages.getInstance().addMessage("Dungeon does not exist.");
			}
		}
		else if(type == ExitType.LADDER_UP){
			try{
				if(world.getCities().get(nextCity) != null){
					plr.getDungeonPathUp(); // Throw out last ladder up destination.
					
					world.setCurrentLevel(nextCity);
					plr.setLocation(nextCity);
					if(destinationRow == 0){
						plr.setInDungeon(false);
					}
					else plr.setDungeonLevel(destinationRow);
					TheGame.getInstance().setHasBeenCleared(false);
				}
				else Messages.getInstance().addMessage("Dungeon does not exist.");
			} catch(Exception e){
				Messages.getInstance().addMessage("Dungeon does not exist.");
			}
		}
		else if(type == ExitType.LADDER_DOWN){
			try{
				if(world.getCities().get(nextCity) != null){
					plr.addToDungeonPath(world.getCurrentLevel());
					
					world.setCurrentLevel(nextCity);
					plr.setLocation(nextCity);
					plr.setDungeonLevel(destinationRow);
					TheGame.getInstance().setHasBeenCleared(false);
				}
				else Messages.getInstance().addMessage("Dungeon does not exist.");
			} catch(Exception e){
				Messages.getInstance().addMessage("Dungeon does not exist.");
			}
		}
	}

	/**
	 * Accessor for the exit's next city index.
	 * @return int : next city index
	 */
	public int getNextCity(){
		return nextCity;
	}

	/**
	 * Accessor for destination row.
	 * @return int : destination row.
	 */
	public int getDestinationRow(){
		return destinationRow;
	}

	/**
	 * Accessor for destination column.
	 * @return int : destination column.
	 */
	public int getDestinationCol(){
		return destinationCol;
	}

	/**
	 * Enemy tries to enter this land object.
	 * @return boolean : successfully entered
	 */
	public boolean enter(Enemy enemy){
		return false;
	}

	/**
	 * Exits don't stop projectiles.
	 */
	public boolean stopsProjectiles(){
		return false;
	}

	/**
	 * Writes this land object to the output stream.
	 */
	public void save(ObjectOutputStream oos){
		try {
			oos.writeObject(type);
			oos.writeObject(nextCity);
			oos.writeObject(destinationRow);
			oos.writeObject(destinationCol);
			oos.writeObject(isOpen);
			oos.writeObject(hasBeenExplored);
		} catch(Exception e) {}
	}

	/**
	 * Loads this land object from the input stream.
	 */
	public void load(ObjectInputStream ois){
		try {
			type = (ExitType) ois.readObject();
			nextCity = (Integer) ois.readObject();
			destinationRow = (Integer) ois.readObject();
			destinationCol = (Integer) ois.readObject();
			isOpen = (Boolean) ois.readObject();
			hasBeenExplored = (Boolean) ois.readObject();
		} catch(Exception e) {}
	}

}
