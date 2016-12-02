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

import src.enums.Enemies;

public class EnemyWriter {

	public EnemyWriter(Document doc, Element rootElem) {
		Element enemyElem = doc.createElement("enemies");
		
		for(int i = 0; i < Enemies.values().length; i++) {
			Enemies enemy = Enemies.values()[i];
			if(enemy.getUsed()) {
				Element currentEnemy = doc.createElement("enemy");
				currentEnemy.setAttribute("type", String.valueOf(i));
				
				Element elem = doc.createElement("name");
				elem.setTextContent(enemy.getName());
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("leftImage");
				elem.setTextContent(enemy.getLeftImage().trim());
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("rightImage");
				elem.setTextContent(enemy.getRightImage().trim());
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("dungeonImage");
				elem.setTextContent(enemy.getDungeonImage().trim());
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("hitPoints");
				elem.setTextContent(String.valueOf(enemy.getMaxHitPoints()));
				currentEnemy.appendChild(elem);

				elem = doc.createElement("ability");
				elem.setTextContent(String.valueOf(enemy.getAbility()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("weaponType");
				elem.setTextContent(String.valueOf(enemy.getWeapon().getType()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("armorType");
				elem.setTextContent(String.valueOf(enemy.getArmor().getType()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("behavior");
				elem.setTextContent(String.valueOf(enemy.getBehavior().getType()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("minMoney");
				elem.setTextContent(String.valueOf(enemy.getMinMoney()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("maxMoney");
				elem.setTextContent(String.valueOf(enemy.getMaxMoney()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("leader");
				elem.setTextContent(String.valueOf(enemy.getIsLeader()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("shopkeeper");
				elem.setTextContent(String.valueOf(enemy.getIsShopkeeper()));
				currentEnemy.appendChild(elem);
				
				elem = doc.createElement("thief");
				elem.setTextContent(String.valueOf(enemy.getIsThief()));
				currentEnemy.appendChild(elem);

				elem = doc.createElement("properNoun");
				elem.setTextContent(String.valueOf(enemy.getIsProperNoun()));
				currentEnemy.appendChild(elem);
				
				enemyElem.appendChild(currentEnemy);
			}
		}
		
		rootElem.appendChild(enemyElem);
	}
}
