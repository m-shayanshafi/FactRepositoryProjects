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
import org.w3c.dom.NodeList;

import src.enums.MagicPresets;
import src.exceptions.BadScenarioFileException;
import src.scenario.ItemNameToStoreID;

/**
 * This class will be passed the XML element for reading the magicPresets
 * information in the scenario file.  It will read each preset type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 1/12/08
 *
 */
public class MagicPresetLoader {
	/**
	 * Constructor for the MagicPresetLoader.  Takes the magicPresets node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the magicPresets section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : magicPresets node
	 * @throws BadScenarioFileException
	 */
	public MagicPresetLoader(Node node) throws BadScenarioFileException {
		try {
			processPreset(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each preset type and assigns the values into the correct
	 * magicPresets enum.
	 * @param node Node : magicPresets node
	 * @throws BadScenarioFileException
	 */
	public void processPreset(Node node) throws BadScenarioFileException {
		try {
			ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);

			if(filteredNodeList.size() > 20){
				throw new BadScenarioFileException();
			}

			int currentType = 0;
			int itemID = 0;
			int price = 0;
			
			ItemNameToStoreID id = new ItemNameToStoreID();

			for(Node list : filteredNodeList){
				NodeList items = ((Element)list).getElementsByTagName("shopItem");

				currentType = Integer.parseInt(((Element)list).getAttribute("type"));

				for(int i = 0; i < items.getLength(); i++){
					Element currentElement = ((Element)items.item(i));

					itemID = id.getItemID((currentElement.getElementsByTagName(
					"itemID").item(0).getFirstChild().getNodeValue()).toLowerCase());

					price = Integer.parseInt(currentElement.getElementsByTagName(
					"price").item(0).getFirstChild().getNodeValue());

					MagicPresets.values()[currentType].addMagic(itemID, price);
				}
			}
		} catch(Exception e){
			System.err.println("Your magic preset information is invalid.");
			throw new BadScenarioFileException();
		}
	}
}
