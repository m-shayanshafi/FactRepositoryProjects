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

import src.gui.editor.EditorWindow;
import src.gui.editor.InitEditorFrame;

/**
 * Runnable class that runs the editor.
 * @author Darren Watts
 * date 1/21/08
 */
public class TheEditor implements Runnable {
	private boolean running;
	private EditorWindow window;
	private boolean canDraw;

	/**
	 * Constructor.  Sets up the various variables.
	 */
	public TheEditor() {
		running = true;
		new InitEditorFrame(this);
		//window = startFrame.getWindow();
		canDraw = true;
	}
	
	/**
	 * Sets the EditorWindow of the editor.
	 * @param win EditorWidow : editor window.
	 */
	public void setWindow(EditorWindow win){
		window = win;
	}

	/**
	 * Run loop.  Draws the editor window if it can, and waits for the
	 * next tick.
	 */
	public void run(){
		running = true;
		long gameStartTime;
		long beforeTime, afterTime, timeDiff, sleepTime, overSleepTime = 0L,
		excess = 0L, period = (long)1000000000/60; // Number dividing by is ticks/sec.
		int noDelays = 0;

		gameStartTime = System.nanoTime();
		beforeTime = gameStartTime;

		while(running){
			if(window != null && window.isVisible() && canDraw)
				window.draw();

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
	}

	/**
	 * Accessor for whether or not the editor window can be drawn at
	 * this time.
	 * @return boolean : can draw.
	 */
	public boolean getCanDraw(){
		return canDraw;
	}

	/**
	 * Sets whether or not the editor window can be drawn at this time.
	 * @param bool boolean : can draw
	 */
	public void setCanDraw(boolean bool){
		canDraw = bool;
	}
}
