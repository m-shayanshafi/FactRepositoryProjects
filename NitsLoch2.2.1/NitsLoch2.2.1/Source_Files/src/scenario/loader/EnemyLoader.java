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

/**
 * This class will be passed the XML element for reading the enemy
 * information in the scenario file.  It will read each enemy type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class EnemyLoader {

	/**
	 * Constructor for the EnemyLoader.  Takes the enemies node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the enemies section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public EnemyLoader(Node node) throws BadScenarioFileException {
		try {
			processEnemy(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each enemy type and assigns the values into the correct
	 * enemy enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	private void processEnemy(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			int currentType = 0;
			String name;
			String leftImage;
			String rightImage;
			String dungeonImage;
			int hitPts;
			int ability;
			int weaponType;
			int armorType;
			int behavior;
			int minMoney;
			int maxMoney;
			boolean leader = false;
			boolean shopkeeper = false;
			boolean thief = false;
			boolean properNoun = false;
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);

				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				name = currentElement.getElementsByTagName(
				"name").item(0).getFirstChild().getNodeValue();

				leftImage = currentElement.getElementsByTagName(
				"leftImage").item(0).getFirstChild().getNodeValue();

				rightImage = currentElement.getElementsByTagName(
				"rightImage").item(0).getFirstChild().getNodeValue();

				dungeonImage = currentElement.getElementsByTagName(
				"dungeonImage").item(0).getFirstChild().getNodeValue();

				hitPts = Integer.parseInt(currentElement.getElementsByTagName(
				"hitPoints").item(0).getFirstChild().getNodeValue());

				ability = Integer.parseInt(currentElement.getElementsByTagName(
				"ability").item(0).getFirstChild().getNodeValue());

				weaponType = Integer.parseInt(currentElement.getElementsByTagName(
				"weaponType").item(0).getFirstChild().getNodeValue());

				armorType = Integer.parseInt(currentElement.getElementsByTagName(
				"armorType").item(0).getFirstChild().getNodeValue());

				behavior = Integer.parseInt(currentElement.getElementsByTagName(
				"behavior").item(0).getFirstChild().getNodeValue());

				minMoney = Integer.parseInt(currentElement.getElementsByTagName(
				"minMoney").item(0).getFirstChild().getNodeValue());

				maxMoney = Integer.parseInt(currentElement.getElementsByTagName(
				"maxMoney").item(0).getFirstChild().getNodeValue());

				// Flags
				if(currentElement.getElementsByTagName("leader").item(0) != null){
					leader = Boolean.parseBoolean(currentElement.getElementsByTagName(
					"leader").item(0).getFirstChild().getNodeValue());
				}

				if(currentElement.getElementsByTagName("shopkeeper").item(0) != null){
					shopkeeper = Boolean.parseBoolean(currentElement.getElementsByTagName(
					"shopkeeper").item(0).getFirstChild().getNodeValue());
				}

				if(currentElement.getElementsByTagName("thief").item(0) != null){
					thief = Boolean.parseBoolean(currentElement.getElementsByTagName(
					"thief").item(0).getFirstChild().getNodeValue());
				}
				
				if(currentElement.getElementsByTagName("properNoun").item(0) != null){
					properNoun = Boolean.parseBoolean(currentElement.getElementsByTagName(
					"properNoun").item(0).getFirstChild().getNodeValue());
				}

				Enemies.values()[currentType].setStats(
						name,
						hitPts,
						ability,
						weaponType,
						armorType,
						behavior,
						minMoney,
						maxMoney,
						leader,
						shopkeeper,
						thief,
						properNoun,
						leftImage,
						rightImage,
						dungeonImage);
			}
		} catch(Exception e){
			System.err.println("Your enemy information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
