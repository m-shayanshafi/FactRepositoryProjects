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

import src.enums.InventoryLimits;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the inventory limit
 * information in the scenario file.  It will read each inventoryLImit item type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class InventoryLimitsLoader {
	
	/**
	 * Constructor for the InventoryLimitsLoader.  Takes the inventoryLimits node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the items section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : inventoryLimits node
	 * @throws BadScenarioFileException
	 */
	public InventoryLimitsLoader(Node node) throws BadScenarioFileException {
		try {
			processLimit(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}
	
	/**
	 * Reads each limit and assigns the values into the correct
	 * InventoryLimits enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public void processLimit(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() != 11){
				throw new BadScenarioFileException();
			}

			int currentType;
			int limit;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				limit = Integer.parseInt(currentElement.getElementsByTagName(
				"limit").item(0).getFirstChild().getNodeValue());
				
				if(limit <= 0) limit = Integer.MAX_VALUE;

				InventoryLimits.values()[currentType].setLimit(limit);
			}
		} catch(Exception e){
			System.err.println("Your inventory limits information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
