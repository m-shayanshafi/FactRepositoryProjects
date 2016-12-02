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

import src.enums.Bars;
import src.enums.Drinks;

public class BarWriter {

	public BarWriter(Document doc, Element rootElem) {
		Element barsElem = doc.createElement("bars");
		
		for(int i = 0; i < Bars.values().length; i++) {
			Bars bar = Bars.values()[i];
			if(bar.getUsed()) {
				Element currentObstruction = doc.createElement("bar");
				currentObstruction.setAttribute("type", String.valueOf(i));

				Element elem = doc.createElement("tonicwater");
				elem.setTextContent(Bars.getMessage(Drinks.TONIC_WATER, bar.getType()).trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("soda");
				elem.setTextContent(Bars.getMessage(Drinks.SODA, bar.getType()).trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("gin");
				elem.setTextContent(Bars.getMessage(Drinks.GIN, bar.getType()).trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("rum");
				elem.setTextContent(Bars.getMessage(Drinks.RUM, bar.getType()).trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("scotch");
				elem.setTextContent(Bars.getMessage(Drinks.SCOTCH, bar.getType()).trim());
				currentObstruction.appendChild(elem);
				
				elem = doc.createElement("redeye");
				elem.setTextContent(Bars.getMessage(Drinks.REDEYE, bar.getType()).trim());
				currentObstruction.appendChild(elem);

				barsElem.appendChild(currentObstruction);
			}

		}
		
		rootElem.appendChild(barsElem);
	}
}
