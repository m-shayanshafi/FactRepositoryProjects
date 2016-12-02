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

import src.enums.GroundItems;

public class GroundItemsWriter {

	public GroundItemsWriter(Document doc, Element rootElem) {
		Element itemsElem = doc.createElement("items");
		
		for(int i = 0; i < GroundItems.values().length; i++) {
			GroundItems item = GroundItems.values()[i];

			Element currentItem = doc.createElement("item");
			currentItem.setAttribute("type", String.valueOf(i));

			Element elem = doc.createElement("image");
			elem.setTextContent(item.getImage());
			currentItem.appendChild(elem);

			itemsElem.appendChild(currentItem);

		}
		
		rootElem.appendChild(itemsElem);
	}
}
