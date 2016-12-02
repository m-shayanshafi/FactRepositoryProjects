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

import src.enums.Weapon;

public class WeaponWriter {
	
	public WeaponWriter(Document doc, Element rootElem) {
		Element weaponElem = doc.createElement("weapons");
		
		for(int i = 0; i < Weapon.values().length; i++) {
			Weapon weapon = Weapon.values()[i];
			if(!weapon.getItemName().equals("")) {
				Element currentWeapon = doc.createElement("weapon");
				currentWeapon.setAttribute("type", String.valueOf(i));
				
				Element elem = doc.createElement("itemName");
				elem.setTextContent(weapon.getItemName());
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("secondName");
				elem.setTextContent(weapon.getSecondaryName());
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("damage");
				elem.setTextContent(String.valueOf(weapon.getDamage()));
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("damageType");
				elem.setTextContent(String.valueOf(weapon.getDamageType().getType()));
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("canBreak");
				elem.setTextContent(String.valueOf(weapon.canBeBroken()));
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("usesRockets");
				elem.setTextContent(String.valueOf(weapon.usesRockets()));
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("usesFlamePacks");
				elem.setTextContent(String.valueOf(weapon.usesFlamePacks()));
				currentWeapon.appendChild(elem);
				
				elem = doc.createElement("verb");
				elem.setTextContent(String.valueOf(weapon.getVerb()));
				currentWeapon.appendChild(elem);
				
				weaponElem.appendChild(currentWeapon);
			}
		}
		
		rootElem.appendChild(weaponElem);
	}
}
