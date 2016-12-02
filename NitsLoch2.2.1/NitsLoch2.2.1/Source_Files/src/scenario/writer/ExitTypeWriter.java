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

import src.enums.ExitType;

public class ExitTypeWriter {

	public ExitTypeWriter(Document doc, Element rootElem) {
		Element exitsElem = doc.createElement("exits");
		
		for(int i = 0; i < ExitType.values().length; i++) {
			ExitType exit = ExitType.values()[i];
			Element currentExit = doc.createElement("exit");
			currentExit.setAttribute("type", String.valueOf(i));

			Element elem = doc.createElement("openImage");
			elem.setTextContent(exit.getOpenImage().trim());
			currentExit.appendChild(elem);

			elem = doc.createElement("closedImage");
			elem.setTextContent(exit.getClosedImage().trim());
			currentExit.appendChild(elem);

			exitsElem.appendChild(currentExit);
		}
		
		rootElem.appendChild(exitsElem);
	}
}
