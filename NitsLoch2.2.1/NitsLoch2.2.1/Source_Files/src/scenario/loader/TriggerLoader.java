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
import src.enums.StreetType;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the enemy trigger
 * information in the scenario file.  It will read each trigger type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/23/07
 */
public class TriggerLoader {

	/**
	 * Constructor for the TriggerLoader.  Takes the triggers node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the triggers section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : triggers node
	 * @throws BadScenarioFileException
	 */
	public TriggerLoader(Node node) throws BadScenarioFileException {
		try {
			processTrigger(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each trigger type and assigns the values into the correct
	 * street enum.
	 * @param node Node : triggers node
	 * @throws BadScenarioFileException
	 */
	public void processTrigger(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			int currentIndex = 0;
			String name;
			String image;
			double chance;
			ArrayList<Enemies> enemyList = new ArrayList<Enemies>();
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentIndex = Integer.parseInt(currentElement.getAttribute("index"));

				name = currentElement.getElementsByTagName(
				"name").item(0).getFirstChild().getNodeValue();
				
				image = currentElement.getElementsByTagName(
				"image").item(0).getFirstChild().getNodeValue();

				chance = Double.parseDouble(currentElement.getElementsByTagName(
				"chance").item(0).getFirstChild().getNodeValue());
				
				NodeList enemyNodeList = currentElement.getElementsByTagName("enemy");
				
				enemyList = new ArrayList<Enemies>();
				for(int k = 0; k < enemyNodeList.getLength(); k++){
					enemyList.add(Enemies.values()[Integer.parseInt(
							enemyNodeList.item(k).getFirstChild().getNodeValue())]);
				}
				
				StreetType currentType = null;
				for(StreetType t : StreetType.values()){
					if(t.getType() == currentIndex){
						currentType = t;
					}
				}
				if(currentType != null){
					currentType.setEnemies(enemyList);
					currentType.setStats(name, image, chance);
				}
				else {
					throw new BadScenarioFileException();
				}
			}
		} catch(Exception e){
			System.err.println("Your enemy trigger information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
