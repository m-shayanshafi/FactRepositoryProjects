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

import src.enums.GenericPresets;
import src.scenario.ItemNameToStoreID;

public class GenericPresetWriter {

	public GenericPresetWriter(Document doc, Element rootElem) {
		Element presetElem = doc.createElement("genericPresets");
		
		for(int i = 0; i < GenericPresets.values().length; i++) {
			GenericPresets preset = GenericPresets.values()[i];
			if(preset.getItems().size() > 0) {
				Element currentPreset = doc.createElement("preset");
				currentPreset.setAttribute("type", String.valueOf(i));
				
				ItemNameToStoreID map = new ItemNameToStoreID();

				for(int k = 0; k < preset.getItems().size(); k++) {
					Element currentItem = doc.createElement("shopItem");
					
					Element elem = doc.createElement("itemID");
					elem.setTextContent(map.getItemMnemonic(
							map.getShopItemNumber(preset.getItems().get(k).getItemName())));
					currentItem.appendChild(elem);
					
					elem = doc.createElement("price");
					elem.setTextContent(String.valueOf(preset.getItems().get(k).getPrice()));
					currentItem.appendChild(elem);
					
					currentPreset.appendChild(currentItem);
				}

				presetElem.appendChild(currentPreset);
			}

		}
		
		rootElem.appendChild(presetElem);
	}
}
