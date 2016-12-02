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

import src.enums.Armor;

public class ArmorWriter {

	public ArmorWriter(Document doc, Element rootElem) {
		Element defenseElem = doc.createElement("defense");
		
		for(int i = 0; i < Armor.values().length; i++) {
			Armor armor = Armor.values()[i];
			if(!armor.getItemName().equals("")) {
				Element currentArmor = doc.createElement("armor");
				currentArmor.setAttribute("type", String.valueOf(i));
				
				Element elem = doc.createElement("itemName");
				elem.setTextContent(armor.getItemName());
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("secondName");
				elem.setTextContent(armor.getSecondaryName());
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("melee");
				elem.setTextContent(String.valueOf(armor.getAbsorbMelee()));
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("martialArts");
				elem.setTextContent(String.valueOf(armor.getAbsorbMartialArts()));
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("marksman");
				elem.setTextContent(String.valueOf(armor.getAbsorbMarksman()));
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("flame");
				elem.setTextContent(String.valueOf(armor.getAbsorbFlame()));
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("other");
				elem.setTextContent(String.valueOf(armor.getAbsorbOther()));
				currentArmor.appendChild(elem);
				
				elem = doc.createElement("canBreak");
				elem.setTextContent(String.valueOf(armor.canBeDestroyed()));
				currentArmor.appendChild(elem);
				
				defenseElem.appendChild(currentArmor);
			}
		}
		
		rootElem.appendChild(defenseElem);
	}
}
