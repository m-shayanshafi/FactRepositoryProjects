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

import src.enums.Enemies;
import src.exceptions.BadScenarioFileException;
import src.game.ShopkeeperSpawns;
import src.scenario.MiscScenarioData;

/**
 * This class will be passed the XML element for reading the shopkeeper spawn
 * information in the scenario file.  It will read each shopkeeper spawn per level
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 1/14/08
 *
 */
public class ShopkeeperSpawnLoader {
	
	/**
	 * Constructor for the ShopkeeperSpawnLoader.  Takes the 
	 * shopkeeperSpawns node in the scenario file as a parameter.
	 * Throws an exception if the information in the shopkeeperSpawns
	 * section of the scenario file isn't formatted 
	 * correctly/complete.
	 * @param node Node : shopkeeperSpawns node
	 * @throws BadScenarioFileException
	 */
	public ShopkeeperSpawnLoader(Node node) throws BadScenarioFileException {
		try {
			processShopkeeperSpawns(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each shopkeeper spawn type and assigns the values into the shopkeeper
	 * spawns object.
	 * @param node Node : shopkeeperSpawns node
	 * @throws BadScenarioFileException
	 */
	public void processShopkeeperSpawns(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
			
			if(filteredNodeList.size() != MiscScenarioData.NUM_CITIES){
				throw new BadScenarioFileException();
			}

			int currentLevel = 0;
			int shopkeeper;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentLevel = Integer.parseInt(currentElement.getAttribute("level"));

				shopkeeper = Integer.parseInt(currentElement.getElementsByTagName(
				"shopkeeper").item(0).getFirstChild().getNodeValue());
				
				ShopkeeperSpawns.getInstance().setShopkeeper(
						Enemies.values()[shopkeeper], currentLevel);
			}
		} catch(Exception e){
			System.err.println("Your shopkeeper spawn information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
