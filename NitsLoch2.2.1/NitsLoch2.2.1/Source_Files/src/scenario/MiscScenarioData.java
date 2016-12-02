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

package src.scenario;

/**
 * Class that has public variables used for various scenario data.
 * @author Darren Watts
 * date 11/14/07
 *
 */
public class MiscScenarioData {
	
	public static String NAME				= "Untitled Scenario";

	public static String MAP_PATH 			= "Map path not set"; // Gets set from the xml file.
	
	public static int NUM_CITIES			= 0;
	
	public static String ENDING_MESSAGE		= "Congratulations!!! You have restored law and order to all of the cities.";

	public static int NUM_PLAYER_IMAGES		= 0;
	public static double RUN_CHANCE			= .30; // Chance enemy will run when hit points drop below threshold.
	public static int RUN_HP_AMOUNT			= 20; // Hit point threshold that enemy will start to run.
	public static String SCENARIO_FILE		= "Scenario.nits";
	public static double SPAWN_CHANCE		= .007; // Chance that enemy will spawn when resting.
	public static double SPAWN_CHANCE_DUN	= .027; // Chance an enemy will spawn while walking in dungeon.
	public static double SPAWN_CHANCE_ITEM	= .03; // Percent chance an item will appear on enemy death.
	
	// Difficulties
	public static double HEALTH_SCALE		= 1;
	public static double ABILITY_SCALE		= 1;
	public static double DAMAGE_SCALE		= 1;
}
