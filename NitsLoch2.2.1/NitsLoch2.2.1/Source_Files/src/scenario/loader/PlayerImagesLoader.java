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

import src.enums.PlayerImages;
import src.exceptions.BadScenarioFileException;
import src.scenario.MiscScenarioData;

/**
 * This class will be passed the XML element for reading the player image
 * information in the scenario file.  It will read each player image type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/18/07
 *
 */
public class PlayerImagesLoader {

	/**
	 * Constructor for the PlayerImagesLoader.  Takes the playerImages node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the playerImages section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : playerImages node
	 * @throws BadScenarioFileException
	 */
	public PlayerImagesLoader(Node node) throws BadScenarioFileException {
		try {
			processPlayerImages(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each playerImage type and assigns the values into the correct
	 * PlayerImages enum.
	 * @param node Node : playerImages node
	 * @throws BadScenarioFileException
	 */
	public void processPlayerImages(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() != MiscScenarioData.NUM_PLAYER_IMAGES){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			String leftImage;
			String rightImage;
			for(int i = 0; i < filteredNodeList.size(); i++){
				Element currentElement = (Element)filteredNodeList.get(i);
				currentType = Integer.parseInt(currentElement.getAttribute("index"));

				leftImage = currentElement.getElementsByTagName(
				"leftImage").item(0).getFirstChild().getNodeValue();
				
				rightImage = currentElement.getElementsByTagName(
				"rightImage").item(0).getFirstChild().getNodeValue();
				
				PlayerImages.values()[currentType].setImages(leftImage, rightImage);
			}
		} catch(Exception e){
			System.err.println("Your player images information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
