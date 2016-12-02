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

import src.enums.NPCs;

public class NPCWriter {
	
	public NPCWriter(Document doc, Element rootElem) {
		Element npcElem = doc.createElement("NPCs");
		
		for(int i = 0; i < NPCs.values().length; i++) {
			NPCs npc = NPCs.values()[i];
			if(npc.getUsed()) {
				Element currentNPC = doc.createElement("npc");
				currentNPC.setAttribute("type", String.valueOf(i));
				
				Element elem = doc.createElement("name");
				elem.setTextContent(npc.getName());
				currentNPC.appendChild(elem);
				
				elem = doc.createElement("message");
				elem.setTextContent(npc.getMessage().trim());
				currentNPC.appendChild(elem);
				
				elem = doc.createElement("image");
				elem.setTextContent(npc.getImage().trim());
				currentNPC.appendChild(elem);

				if(!npc.getSoundPath().equals("")) {
					elem = doc.createElement("sound");
					elem.setTextContent(npc.getSoundPath().trim());
					currentNPC.appendChild(elem);
				}
				
				npcElem.appendChild(currentNPC);
			}
		}
		
		rootElem.appendChild(npcElem);
	}

}
