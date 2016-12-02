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
import src.enums.ObstructionLandType;

import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the obstructionImages
 * information in the scenario file.  It will read each obstruction type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 12/02/07
 *
 */
public class ObstructionLoader {

	/**
	 * Constructor for the ObstructionImage.  Takes the obstructionImages node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the obstructionImages section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : obstructionImages node
	 * @throws BadScenarioFileException
	 */
	public ObstructionLoader(Node node) throws BadScenarioFileException {
		try {
			processObstructions(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each obstruction type and assigns the values into the correct
	 * ObstructionLandType enum.
	 * @param node Node : streets node
	 * @throws BadScenarioFileException
	 */
	public void processObstructions(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() < 2 || filteredNodeList.size() > 200){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			boolean destroyable;
			String name;
			String image;
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("type"));
				
				image = currentElement.getElementsByTagName(
				"image").item(0).getFirstChild().getNodeValue();
				
				destroyable = Boolean.parseBoolean(currentElement.getElementsByTagName(
				"destroyable").item(0).getFirstChild().getNodeValue());
				
				name = currentElement.getElementsByTagName(
				"name").item(0).getFirstChild().getNodeValue();

				ObstructionLandType.values()[currentType].setStats(image, destroyable, name);
			}
		} catch(Exception e){
			e.printStackTrace();
			System.err.println("Your obstruction information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
