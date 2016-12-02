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

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import src.enums.Enemies;
import src.game.CitySpawns;

public class CitySpawnWriter {

	public CitySpawnWriter(Document doc, Element rootElem) throws Exception {
		Element spawnElem = doc.createElement("citySpawns");
		for(int i = 0; i <= CitySpawns.getInstance().getHighestLevelIndex(); i++) {
			try {
				if(CitySpawns.getInstance().getEnemies(i).size() > 0) {
					ArrayList<Enemies> enemies = CitySpawns.getInstance().getEnemies(i);
					Element currentDungeon = doc.createElement("city");
					currentDungeon.setAttribute("level", String.valueOf(i));

					for(int k = 0; k < enemies.size(); k++) {
						Element elem = doc.createElement("enemy");
						elem.setTextContent(String.valueOf(enemies.get(k).getType()));
						currentDungeon.appendChild(elem);
					}
					spawnElem.appendChild(currentDungeon);
				}
			} catch(NullPointerException n) { }
		}

		rootElem.appendChild(spawnElem);
	}
}
