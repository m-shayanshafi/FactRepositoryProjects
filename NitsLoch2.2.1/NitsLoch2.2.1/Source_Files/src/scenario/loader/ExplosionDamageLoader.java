/*	
	This file is part of NitsLoch.

	Copyright (C) 2008 Darren Watts

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

import src.enums.ExplosionType;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the explosion
 * damage information in the scenario file.  It will read each explosion type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/28/08
 *
 */
public class ExplosionDamageLoader {

	/**
	 * Constructor for the ExplosionDamageLoader.  Takes the explosionDamage node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the exits section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : explosionDamage node
	 * @throws BadScenarioFileException
	 */
	public ExplosionDamageLoader(Node node) throws BadScenarioFileException {
		try {
			processExplosions(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each explosionDamage type and assigns the values into the correct
	 * ExplosionType enum.
	 * @param node Node : explosionDamage node
	 * @throws BadScenarioFileException
	 */
	public void processExplosions(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
			
			if(filteredNodeList.size() != 3){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			int damage;
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				damage = Integer.parseInt(currentElement.getElementsByTagName(
				"damage").item(0).getFirstChild().getNodeValue());
				
				ExplosionType.values()[currentType].setDamage(damage);
			}
		} catch(Exception e){
			System.err.println("Your explosionDamage information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
