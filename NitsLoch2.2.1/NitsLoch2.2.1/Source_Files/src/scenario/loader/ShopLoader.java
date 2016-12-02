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

import src.enums.Shops;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the shop
 * information in the scenario file.  It will read each shop type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class ShopLoader {
	
	/**
	 * Constructor for the ShopLoader.  Takes the shops node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the shops section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : shops node
	 * @throws BadScenarioFileException
	 */
	public ShopLoader(Node node) throws BadScenarioFileException {
		try {
			processGroundItems(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}
	
	/**
	 * Reads each shop type and assigns the values into the correct
	 * Shop enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public void processGroundItems(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() != 14){
				throw new BadScenarioFileException();
			}

			int currentType;
			String image;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				image = currentElement.getElementsByTagName(
				"image").item(0).getFirstChild().getNodeValue();

				Shops.values()[currentType].setImageLocation(image);
			}
		} catch(Exception e){
			System.err.println("Your shop information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
