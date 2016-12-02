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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import src.exceptions.BadScenarioFileException;
import src.scenario.MiscScenarioData;

/**
 * This class will be passed the XML element for reading the startingInventory
 * information in the scenario file.  It will read each startingInventory item type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class DifficultyLoader {

	/**
	 * Constructor for the DifficultyLoader.  Takes the difficulty node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the items section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : difficulty node
	 * @throws BadScenarioFileException
	 */
	public DifficultyLoader(Node node) throws BadScenarioFileException {
		try {
			processDifficulty(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each difficulty type and assigns the values into the scenario data.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public void processDifficulty(Node node) throws BadScenarioFileException {
		try {

			double healthScale;
			double abilityScale;
			double damageScale;

			Element currentElement = (Element)node;

			healthScale = Double.parseDouble(currentElement.getElementsByTagName(
			"health").item(0).getFirstChild().getNodeValue());

			abilityScale = Double.parseDouble(currentElement.getElementsByTagName(
			"ability").item(0).getFirstChild().getNodeValue());

			damageScale = Double.parseDouble(currentElement.getElementsByTagName(
			"damage").item(0).getFirstChild().getNodeValue());

			MiscScenarioData.HEALTH_SCALE = healthScale;
			MiscScenarioData.ABILITY_SCALE = abilityScale;
			MiscScenarioData.DAMAGE_SCALE = damageScale;

		} catch(Exception e){
			System.err.println("Your difficulty information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
