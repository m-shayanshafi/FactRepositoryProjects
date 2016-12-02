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

import src.enums.StoreItems;
import src.enums.Weapon;
import src.enums.DamageType;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the weapon
 * information in the scenario file.  It will read each weapon type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class WeaponLoader {

	/**
	 * Constructor for the WeaponLoader.  Takes the weapons node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the weapons section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : weapons node
	 * @throws BadScenarioFileException
	 */
	public WeaponLoader(Node node) throws BadScenarioFileException {
		try {
			processWeapons(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each weapon type and assigns the values into the correct
	 * StoreItems enum.
	 * @param node Node : weapons node
	 * @throws BadScenarioFileException
	 */
	public void processWeapons(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
			
			if(filteredNodeList.size() > 25){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			String itemName;
			String secondName;
			String verb;
			int damage;
			int damageType;
			boolean canBreak;
			boolean usesRockets;
			boolean usesFlamePacks;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));
				
				itemName = currentElement.getElementsByTagName(
				"itemName").item(0).getFirstChild().getNodeValue();
				
				secondName = currentElement.getElementsByTagName(
				"secondName").item(0).getFirstChild().getNodeValue();
				
				try {
					verb = currentElement.getElementsByTagName(
					"verb").item(0).getFirstChild().getNodeValue();
				} catch(Exception ex) {
					verb = "hit";
				}

				damage = Integer.parseInt(currentElement.getElementsByTagName(
				"damage").item(0).getFirstChild().getNodeValue());
				
				damageType = Integer.parseInt(currentElement.getElementsByTagName(
				"damageType").item(0).getFirstChild().getNodeValue());
				
				canBreak = Boolean.parseBoolean(currentElement.getElementsByTagName(
				"canBreak").item(0).getFirstChild().getNodeValue());
				
				usesRockets = Boolean.parseBoolean(currentElement.getElementsByTagName(
				"usesRockets").item(0).getFirstChild().getNodeValue());
				
				usesFlamePacks = Boolean.parseBoolean(currentElement.getElementsByTagName(
				"usesFlamePacks").item(0).getFirstChild().getNodeValue());
				
				Weapon.values()[currentType].setStats(itemName, secondName, verb, damage,
						DamageType.values()[damageType], canBreak, usesRockets, usesFlamePacks);
				
				StoreItems.WEAPON1.setStats(itemName, secondName);
			}
		} catch(Exception e){
			System.err.println("Your weapon information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
