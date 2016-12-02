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

import src.enums.StreetType;

public class TriggerWriter {

	public TriggerWriter(Document doc, Element rootElem) {
		Element triggersElem = doc.createElement("triggers");
		
		for(int i = 200; i < 210; i++) {
			StreetType street = StreetType.values()[i];
			if(street.getUsed()) {
				Element currentTrigger = doc.createElement("trigger");
				currentTrigger.setAttribute("index", String.valueOf(i));
				
				Element elem = doc.createElement("name");
				elem.setTextContent(street.getName());
				currentTrigger.appendChild(elem);
				
				elem = doc.createElement("image");
				elem.setTextContent(street.getImage());
				currentTrigger.appendChild(elem);
				
				elem = doc.createElement("chance");
				elem.setTextContent(String.valueOf(street.getChance()));
				currentTrigger.appendChild(elem);
				
				for(int k = 0; k < street.getEnemies().size(); k++) {
					elem = doc.createElement("enemy");
					elem.setTextContent(String.valueOf(street.getEnemies().get(k).getType()));
					currentTrigger.appendChild(elem);
				}
				
				triggersElem.appendChild(currentTrigger);
			}
		}
		
		rootElem.appendChild(triggersElem);
	}
}
