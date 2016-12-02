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

import src.enums.Shops;

public class ShopWriter {

	public ShopWriter(Document doc, Element rootElem) {
		Element npcElem = doc.createElement("shops");
		
		for(int i = 0; i < Shops.values().length; i++) {
			Shops shop = Shops.values()[i];

			Element currentShop = doc.createElement("shop");
			currentShop.setAttribute("type", String.valueOf(i));

			Element elem = doc.createElement("image");
			elem.setTextContent(shop.getImage().trim());
			currentShop.appendChild(elem);

			npcElem.appendChild(currentShop);

		}
		
		rootElem.appendChild(npcElem);
	}
}
