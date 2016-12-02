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

import src.enums.Sounds;

public class SoundsWriter {

	public SoundsWriter(Document doc, Element rootElem) {
		Element soundsElem = doc.createElement("sounds");

		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyMeleeAttack");
			elem.setTextContent(Sounds.ENEMY_MELEE_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyMartialArtsAttack");
			elem.setTextContent(Sounds.ENEMY_MART_ARTS_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyMarksmanAttack");
			elem.setTextContent(Sounds.ENEMY_MARKSMAN_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyFlameAttack");
			elem.setTextContent(Sounds.ENEMY_FLAME_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyOtherAttack");
			elem.setTextContent(Sounds.ENEMY_OTHER_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerMeleeAttack");
			elem.setTextContent(Sounds.PLAYER_MELEE_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerMartialArtsAttack");
			elem.setTextContent(Sounds.PLAYER_MART_ARTS_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerMarksmanAttack");
			elem.setTextContent(Sounds.PLAYER_MARKSMAN_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerFlameAttack");
			elem.setTextContent(Sounds.PLAYER_FLAME_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerOtherAttack");
			elem.setTextContent(Sounds.PLAYER_OTHER_ATTACK.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("explosion");
			elem.setTextContent(Sounds.EXPLOSION.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("playerDies");
			elem.setTextContent(Sounds.PLAYER_DIES.getPath().trim());
			soundsElem.appendChild(elem);
		}
		
		if(Sounds.ENEMY_MELEE_ATTACK.getUsed()) {
			Element elem = doc.createElement("enemyDies");
			elem.setTextContent(Sounds.ENEMY_DIES.getPath().trim());
			soundsElem.appendChild(elem);
		}

		rootElem.appendChild(soundsElem);
	}
}
