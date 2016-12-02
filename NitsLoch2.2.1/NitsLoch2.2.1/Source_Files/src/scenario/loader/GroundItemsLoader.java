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

import src.enums.GroundItems;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the ground item
 * information in the scenario file.  It will read each ground item type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class GroundItemsLoader {
	
	/**
	 * Constructor for the GroundItemsLoader.  Takes the items node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the items section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : items node
	 * @throws BadScenarioFileException
	 */
	public GroundItemsLoader(Node node) throws BadScenarioFileException {
		try {
			processGroundItems(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}
	
	/**
	 * Reads each ground item type and assigns the values into the correct
	 * GroundItems enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public void processGroundItems(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() != 7){
				throw new BadScenarioFileException();
			}

			int currentType;
			String image;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				image = currentElement.getElementsByTagName(
				"image").item(0).getFirstChild().getNodeValue();

				GroundItems.values()[currentType].setImageLocation(image);
			}
		} catch(Exception e){
			System.err.println("Your ground item information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
