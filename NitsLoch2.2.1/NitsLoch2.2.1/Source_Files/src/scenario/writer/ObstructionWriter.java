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

import src.enums.ObstructionLandType;

public class ObstructionWriter {

	public ObstructionWriter(Document doc, Element rootElem) {
		Element obstructionImagesElem = doc.createElement("obstructionImages");
		
		for(int i = 0; i < ObstructionLandType.values().length; i++) {
			ObstructionLandType obstruction = ObstructionLandType.values()[i];
			if(obstruction.getUsed()) {
				Element currentObstruction = doc.createElement("obstruction");
				currentObstruction.setAttribute("type", String.valueOf(i));

				Element elem = doc.createElement("image");
				elem.setTextContent(obstruction.getImage().trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("destroyable");
				elem.setTextContent(String.valueOf(obstruction.getIsPossiblyDestroyed()));
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("name");
				elem.setTextContent(obstruction.getName());
				currentObstruction.appendChild(elem);

				obstructionImagesElem.appendChild(currentObstruction);
			}

		}
		
		rootElem.appendChild(obstructionImagesElem);
	}
}
