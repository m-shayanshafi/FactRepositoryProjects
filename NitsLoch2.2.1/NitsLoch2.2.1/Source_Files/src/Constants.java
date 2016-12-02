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

package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Keeps track of constant numbers used for various things.  Also contains
 * functions that can be accessed statically.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Constants {
	public static final String version				= "2.0";
	public static final long serialVersionUID		= 1L;

	public static final int NUM_MESSAGES 			= 6; // Number of messages that can be displayed in messages window.
	public static final int WORLD_VIEW_SIZE			= 7; //Best if kept at 7.
	public static final int HIT_COUNTER				= 4; // Ticks that "pow" pictures will be displayed.
	public static final int TICKS_PER_SECOND		= 30; // Number of ticks per second.
	public static final double STEAL_PERCENT		= .18; // Chance enemy thieves will steal when player is hit.
	public static final double SHOPKEEPER_SPAWN		= .60; // Chance a shopkeeper will spawn if a shop has no shopkeeper.
	public static final double CHANCE_FIND_ADV_WEAPON = .05; // Chance to find a + weapon on an advanced enemy.
	public static final int HP_BONUS				= 8; // Number of hit points player will receive when an enemy is killed.
	public static final int HP_PER_DOLLAR			= 3; // Cost of purchasing health at hospitals.
	public static final int FLAME_HIT_PERCENT		= 70; // Hit percent when enemy or player uses a weapon with flame damage type.
	public static final int MAP_VIEW_SIZE			= 67; // View size for overhead map.
	public static final int MAX_FILE_SIZE			= 2048; // Max file size in the scenario file.
	public static String SCENARIO_XML			= "Scenario.xml";
	public static final String SCENARIO_INFO		= "ScenarioInfo.xml";
	public static final Color DUNGEON_WALL_COLOR	= Color.YELLOW;
	public static final int MESSAGE_LINE_LEN		= 62;
	public static final int CELL_SIZE				= 57; // Size of cell in editor window.
	public static final int NUM_DUNGEON_LEVELS		= 50;
	
	public static boolean EDITOR					= false; // Whether we are running editor or game.
	
	public static final boolean DEBUG				= false; // Debug flag.
	public static boolean SCENARIO_DEBUG			= false; // Debugging for scenario


	/**
	 * Returns the NitsLoch version number as a string.
	 * @return String : version number.
	 */
	public static String getVersion(){
		return version;
	}

	/**
	 * Centers the frame in the middle of the screen.
	 * @param frame The frame to center.
	 */
	public static void centerFrame(JFrame frame){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();

		int lx = (int) ((screen.getWidth() * .5) - ( .5 * frame.getWidth() ));
		int ly = (int) ((screen.getHeight() * .5) - ( .5 * frame.getHeight() ));
		frame.setLocation(lx, ly);
	}

	/**
	 * If the debug flag is on, this will print.
	 * @param str Object to print.
	 */
	public static void debugPrint(Object str){
		if(DEBUG)
			System.out.println(str);
	}
}
