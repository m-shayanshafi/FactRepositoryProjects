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

package src.scenario.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import src.enums.StartingInventory;

public class StartingInventoryWriter {

	public StartingInventoryWriter(Document doc, Element rootElem) {
		Element startingInventoryElem = doc.createElement("startingInventory");

		Element elem = doc.createElement("bandaids");
		elem.setTextContent(String.valueOf(StartingInventory.BANDAIDS.getAmount()));
		startingInventoryElem.appendChild(elem);

		elem = doc.createElement("grenades");
		elem.setTextContent(String.valueOf(StartingInventory.GRENADES.getAmount()));
		startingInventoryElem.appendChild(elem);

		elem = doc.createElement("dynamite");
		elem.setTextContent(String.valueOf(StartingInventory.DYNAMITE.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("bullets");
		elem.setTextContent(String.valueOf(StartingInventory.BULLETS.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("rockets");
		elem.setTextContent(String.valueOf(StartingInventory.ROCKETS.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("flamePacks");
		elem.setTextContent(String.valueOf(StartingInventory.FLAME_PACKS.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("laddersUp");
		elem.setTextContent(String.valueOf(StartingInventory.LADDER_UP.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("laddersDown");
		elem.setTextContent(String.valueOf(StartingInventory.LADDER_DOWN.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("mapViewers");
		elem.setTextContent(String.valueOf(StartingInventory.MAP_VIEWERS.getAmount()));
		startingInventoryElem.appendChild(elem);
		
		elem = doc.createElement("exports");
		elem.setTextContent(String.valueOf(StartingInventory.EXPORTS.getAmount()));
		startingInventoryElem.appendChild(elem);

		rootElem.appendChild(startingInventoryElem);
	}
}
