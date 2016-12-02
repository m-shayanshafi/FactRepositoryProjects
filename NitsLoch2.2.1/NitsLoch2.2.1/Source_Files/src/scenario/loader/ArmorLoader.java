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
import src.enums.Armor;

import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the armor
 * information in the scenario file.  It will read each armor type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class ArmorLoader {

	/**
	 * Constructor for the ArmorLoader.  Takes the defense node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the defense section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : armor node
	 * @throws BadScenarioFileException
	 */
	public ArmorLoader(Node node) throws BadScenarioFileException {
		try {
			processArmor(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each armor type and assigns the values into the correct
	 * armor enum.
	 * @param node Node : defense node
	 * @throws BadScenarioFileException
	 */
	public void processArmor(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() > 25){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			String itemName;
			String secondName;
			int melee = 0;
			int martialArts = 0;
			int marksman = 0;
			int flame = 0;
			int other = 0;
			boolean canBreak;
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));
				
				itemName = currentElement.getElementsByTagName(
				"itemName").item(0).getFirstChild().getNodeValue();
				
				secondName = currentElement.getElementsByTagName(
				"secondName").item(0).getFirstChild().getNodeValue();

				melee = Integer.parseInt(currentElement.getElementsByTagName(
				"melee").item(0).getFirstChild().getNodeValue());

				martialArts = Integer.parseInt(currentElement.getElementsByTagName(
				"martialArts").item(0).getFirstChild().getNodeValue());

				marksman = Integer.parseInt(currentElement.getElementsByTagName(
				"marksman").item(0).getFirstChild().getNodeValue());

				flame = Integer.parseInt(currentElement.getElementsByTagName(
				"flame").item(0).getFirstChild().getNodeValue());

				other = Integer.parseInt(currentElement.getElementsByTagName(
				"other").item(0).getFirstChild().getNodeValue());
				
				canBreak = Boolean.parseBoolean(currentElement.getElementsByTagName(
				"canBreak").item(0).getFirstChild().getNodeValue());
				
				Armor.values()[currentType].setStats(itemName, secondName, melee, martialArts,
						marksman, flame, other, canBreak);
			}
		} catch(Exception e){
			System.err.println("Your armor information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
