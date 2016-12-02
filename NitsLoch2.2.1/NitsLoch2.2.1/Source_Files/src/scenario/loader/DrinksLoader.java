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

import src.enums.Drinks;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the drinks
 * information in the scenario file.  It will read each element under drinks
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 1/10/08
 *
 */
public class DrinksLoader {
	
	/**
	 * Constructor for the DrinksLoader.  Takes the drinks node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the drinks section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public DrinksLoader(Node node) throws BadScenarioFileException {
		try {
			processDrink(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each drink type and assigns it the correct price in the
	 * Drinks enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	private void processDrink(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
			
			if(filteredNodeList.size() != 6){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			int price;
			
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);

				currentType = Integer.parseInt(currentElement.getAttribute("type"));

				price = Integer.parseInt(currentElement.getElementsByTagName(
				"price").item(0).getFirstChild().getNodeValue());
				
				Drinks.values()[currentType].setCost(price);

			}
		} catch(Exception e){
			System.err.println("Your drinks information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
