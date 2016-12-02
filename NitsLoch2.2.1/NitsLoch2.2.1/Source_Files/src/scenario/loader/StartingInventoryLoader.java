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

import src.enums.StartingInventory;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the startingInventory
 * information in the scenario file.  It will read each startingInventory item type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public class StartingInventoryLoader {

	/**
	 * Constructor for the StartingInventoryLoader.  Takes the startingInventory node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the items section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : startingInventory node
	 * @throws BadScenarioFileException
	 */
	public StartingInventoryLoader(Node node) throws BadScenarioFileException {
		try {
			processItemAmount(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each inventory starting amount and assigns the values into the correct
	 * StartingInventory enum.
	 * @param node
	 * @throws BadScenarioFileException
	 */
	public void processItemAmount(Node node) throws BadScenarioFileException {
		try {

			int bandaidAmount;
			int grenadeAmount;
			int dynamiteAmount;
			int bulletAmount;
			int rocketAmount;
			int flameAmount;
			int ladderUpAmount;
			int ladderDownAmount;
			int mapViewerAmount;
			int exportAmount;

			Element currentElement = (Element)node;

			bandaidAmount = Integer.parseInt(currentElement.getElementsByTagName(
					"bandaids").item(0).getFirstChild().getNodeValue());

			grenadeAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"grenades").item(0).getFirstChild().getNodeValue());

			dynamiteAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"dynamite").item(0).getFirstChild().getNodeValue());

			bulletAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"bullets").item(0).getFirstChild().getNodeValue());

			rocketAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"rockets").item(0).getFirstChild().getNodeValue());

			flameAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"flamePacks").item(0).getFirstChild().getNodeValue());

			ladderUpAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"laddersUp").item(0).getFirstChild().getNodeValue());

			ladderDownAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"laddersDown").item(0).getFirstChild().getNodeValue());

			mapViewerAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"mapViewers").item(0).getFirstChild().getNodeValue());
			
			exportAmount = Integer.parseInt(currentElement.getElementsByTagName(
			"exports").item(0).getFirstChild().getNodeValue());

			StartingInventory.BANDAIDS.setAmount(bandaidAmount);
			StartingInventory.BULLETS.setAmount(bulletAmount);
			StartingInventory.DYNAMITE.setAmount(dynamiteAmount);
			StartingInventory.FLAME_PACKS.setAmount(flameAmount);
			StartingInventory.GRENADES.setAmount(grenadeAmount);
			StartingInventory.LADDER_DOWN.setAmount(ladderDownAmount);
			StartingInventory.LADDER_UP.setAmount(ladderUpAmount);
			StartingInventory.MAP_VIEWERS.setAmount(mapViewerAmount);
			StartingInventory.ROCKETS.setAmount(rocketAmount);
			StartingInventory.EXPORTS.setAmount(exportAmount);


		} catch(Exception e){
			System.err.println("Your starting inventory information is invalid.");
			throw new BadScenarioFileException();
		}
	}

}
