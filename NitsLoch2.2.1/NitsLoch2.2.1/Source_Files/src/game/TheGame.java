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

import src.Constants;
import src.exceptions.NoPlayerException;
import src.gui.Controller;
import src.land.Land;

/**
 * This is a singleton class that actually runs the game.  It implements Runnable.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class TheGame implements Runnable {

	private static TheGame instance = null;

	private Controller controller;
	private Thread animator = null;
	private boolean running = false;
	private long gameStartTime;
	private volatile boolean canDraw = true;
	private boolean hasBeenCleared = false;
	private boolean viewingMap = false;
	private int localPlayerIndex;
	private String savePath;

	/**
	 * Sets the controller for this game.
	 * @param cont The Controller
	 */
	public void setController(Controller cont){
		controller = cont;
	}

	/**
	 * Starts the thread that is in charge of running the game.
	 */
	public void startGame(){
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	/**
	 * Sets the local player index
	 * @param index int : player index
	 */
	public void setLocalPlayerIndex(int index){
		localPlayerIndex = index;
	}

	/**
	 * Accessor for the save path when the user chooses to save, but
	 * not save as...
	 * @return String : path to current saved game on disk
	 */
	public String getSavePath(){
		return savePath;
	}

	/**
	 * Mutator for the save path when the user chooses to save, but
	 * not save as...
	 * @param str String : path to save file
	 */
	public void setSavePath(String str){
		savePath = str;
	}

	/**
	 * Accessor for the local player index
	 * @return int : local player index
	 */
	public int getLocalPlayerIndex(){
		return localPlayerIndex;
	}

	/**
	 * The game loop.
	 */
	public void run(){
		running = true;
		long beforeTime, afterTime, timeDiff, sleepTime, overSleepTime = 0L,
		excess = 0L, period = (long)1000000000/Constants.TICKS_PER_SECOND; // Number dividing by is ticks/sec.
		int noDelays = 0;

		gameStartTime = System.nanoTime();
		beforeTime = gameStartTime;

		while(running){
			try {
				controller.updateArmorMenu(GameWorld.getInstance().getLocalPlayer().getAvailableArmor());
				controller.updateWeaponMenu(GameWorld.getInstance().getLocalPlayer().getWeaponLevels());
			} catch( Exception ex) {}

			if(canDraw && !viewingMap){
				controller.drawGameWindow();
			}
			else if(canDraw && viewingMap){
				controller.drawMap();
			}
			if(!hasBeenCleared && GameWorld.getInstance().checkExits() && canDraw){
				openGates(); // Open doors of exits.
			}

			updateHitCounters();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;  

			if (sleepTime > 0) {   // some time left in this cycle
				try {
					Thread.sleep(sleepTime/1000000L);  // nano -> ms
				}
				catch(InterruptedException ex){}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else {    // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime;  // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= 16) {
					Thread.yield();   // give another thread a chance to run
					noDelays = 0;
				}
			}
			beforeTime = System.nanoTime();
		}
		controller.drawGameWindow();
	}

	/**
	 * Updates the hit counters for the players and all of the
	 * enemies within range of the players.
	 */
	private void updateHitCounters(){
		if(getCanDraw()){
			for(Player p : GameWorld.getInstance().getPlayers()){
				p.decrementHitCounter();

				// Decrement hit counter for all enemies within this player's view.
				try {
					for(Land[] landRow : GameWorld.getInstance().getGameWorld(p.getRow(), p.getCol())){
						for(Land l : landRow){
							if(l.getEnemy() != null)
								l.getEnemy().decrementHitCounter();
						}
					}
				} catch(Exception ex) {}
			}
		}
	}

	/**
	 * Calls open gates on the world object.
	 */
	public void openGates(){
		hasBeenCleared = true;
		GameWorld.getInstance().openGates();
		/*if( GameWorld.getInstance().openGates() )
			running = false;*/
	}

	/**
	 * Returns whether the local player index is in the dungeon or not.
	 * @return boolean : Whether or not the local player index is in a dungeon.
	 */
	public boolean getInDungeon(){
		try {
			if(GameWorld.getInstance().getPlayers().size() <= 0) return false;
			return GameWorld.getInstance().getPlayer(localPlayerIndex).getInDungeon();
		} catch(NoPlayerException plrEx){
			return false;
		}
	}

	/**
	 * Private constructor.
	 */
	private TheGame(){
		localPlayerIndex = 0;
	}

	/**
	 * Returns the instance of the game.
	 * @return TheGame : instance
	 */
	public static TheGame getInstance(){
		if(instance == null)
			instance = new TheGame();
		return instance;
	}

	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}

	/**
	 * Accessor for the reference to the controller.
	 * @return Controller : controller
	 */
	public Controller getController(){
		return controller;
	}

	/**
	 * Sets whether or not the player is viewing the map.
	 * @param bool boolean : viewing map
	 */
	public void setViewingMap(boolean bool){
		viewingMap = bool;
	}

	/**
	 * Accessor for whether or not the player is viewing the map.
	 * @return boolean : viewing map
	 */
	public boolean getViewingMap(){
		return viewingMap;
	}

	/**
	 * Sets the game to the specified running boolean.
	 * @param bool boolean : whether or not the game is running.
	 */
	public void setRunning(boolean bool){
		running = bool;
	}

	/**
	 * Accessor for whether or not the game is running.
	 * @return boolean : running
	 */
	public boolean getRunning(){
		return running;
	}

	/**
	 * Sets the can draw variable to false.
	 */
	public void haltDrawing(){
		canDraw = false;
	}

	/**
	 * Sets the can draw variable to true.
	 */
	public void resumeDrawing(){
		canDraw = true;
	}

	/**
	 * Accessor for whether or not the window can
	 * be drawn
	 * @return boolea : can draw
	 */
	public boolean getCanDraw(){
		return canDraw;
	}

	/**
	 * Sets whether or not the current map has been cleared.  If the current
	 * city has not been cleared, it will check to see if it is clear every
	 * tick until it is clear.
	 * @param bool boolean : cleared
	 */
	public void setHasBeenCleared(boolean bool){
		hasBeenCleared = bool;
	}
}
