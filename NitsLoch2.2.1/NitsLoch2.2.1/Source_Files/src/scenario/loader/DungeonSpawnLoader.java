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

package src.scenario.loader;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import src.enums.Enemies;
import src.exceptions.BadScenarioFileException;
import src.game.DungeonSpawns;

/**
 * This class will be passed the XML element for reading the dungeon spawn
 * information in the scenario file.  It will read each dungeon spawn per level
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class DungeonSpawnLoader {

	/**
	 * Constructor for the DungeonSpawnLoader.  Takes the dungeonSpawns node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the dungeonSpawns section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : dungeonSpawns node
	 * @throws BadScenarioFileException
	 */
	public DungeonSpawnLoader(Node node) throws BadScenarioFileException {
		try {
			processDungeonSpawns(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each dungeon spawn type and assigns the values into the dungeon
	 * spawns object.
	 * @param node Node : dungeonSpawns node
	 * @throws BadScenarioFileException
	 */
	public void processDungeonSpawns(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
			
			if(filteredNodeList.size() > src.Constants.NUM_DUNGEON_LEVELS){
				throw new BadScenarioFileException();
			}

			int currentLevel = 0;
			ArrayList<Enemies> enemyList = new ArrayList<Enemies>();
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				enemyList.clear();
				Element currentElement = (Element)filteredNodeList.get(i);
				currentLevel = Integer.parseInt(currentElement.getAttribute("level"));

				NodeList enemyNodeList = currentElement.getElementsByTagName("enemy");
				
				for(int k = 0; k < enemyNodeList.getLength(); k++){
					enemyList.add(Enemies.values()[Integer.parseInt(
							enemyNodeList.item(k).getFirstChild().getNodeValue())]);
				}
				
				DungeonSpawns.getInstance().setEnemies(enemyList, currentLevel);
			}
		} catch(Exception e){
			System.err.println("Your dungeon spawn information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
