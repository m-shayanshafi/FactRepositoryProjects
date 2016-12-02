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

package src.scenario.loader;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import src.enums.Sounds;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the sounds
 * information in the scenario file.  It will read each sound type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 2/22/08
 *
 */
public class SoundsLoader {

	/**
	 * Constructor for the SoundsLoader.  Takes the sounds node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the sounds section of the scenario file isn't formatted
	 * correctly.
	 * @param node Node : sounds node
	 * @throws BadScenarioFileException
	 */
	public SoundsLoader(Node node) throws BadScenarioFileException {
		try {
			processSound(node);
		} catch(BadScenarioFileException bad){
			throw new BadScenarioFileException();
		}
	}

	/**
	 * Reads each tag in the sounds element.
	 * @param node Node : sounds node
	 * @throws BadScenarioFileException
	 */
	public void processSound(Node node) throws BadScenarioFileException {

		String enemyHit, playerHit, enemyHitDun, playerHitDun, scenarioInfo,
		enemyMelee, enemyMartArts, enemyMarksman, enemyFlame, enemyOther,
		playerMelee, playerMartArts, playerMarksman, playerFlame,
		playerOther, explosion, playerDies, enemyDies, pickupItem;

		Element currentElement = (Element)node;

		try {
			enemyHit = currentElement.getElementsByTagName(
			"enemyHitOutside").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_HIT.setSound(enemyHit);
		} catch(Exception ex) { }

		try {
			playerHit = currentElement.getElementsByTagName(
			"playerHitOutside").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_HIT.setSound(playerHit);
		} catch(Exception ex) { }

		try {
			enemyHitDun = currentElement.getElementsByTagName(
			"enemyHitDungeon").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_HIT_DUN.setSound(enemyHitDun);
		} catch(Exception ex) { }

		try {
			playerHitDun = currentElement.getElementsByTagName(
			"playerHitDungeon").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_HIT_DUN.setSound(playerHitDun);
		} catch(Exception ex) { }

		try {
			scenarioInfo = currentElement.getElementsByTagName(
			"scenarioInfo").item(0).getFirstChild().getNodeValue();
			Sounds.SCENARIO_INFO.setSound(scenarioInfo);
		} catch(Exception ex) { }

		try {
			enemyMelee = currentElement.getElementsByTagName(
			"enemyMeleeAttack").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_MELEE_ATTACK.setSound(enemyMelee);
		} catch(Exception ex) { }
		
		try {
			enemyMartArts = currentElement.getElementsByTagName(
			"enemyMartialArtsAttack").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_MART_ARTS_ATTACK.setSound(enemyMartArts);
		} catch(Exception ex) { }

		try {
			enemyMarksman = currentElement.getElementsByTagName(
			"enemyMarksmanAttack").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_MARKSMAN_ATTACK.setSound(enemyMarksman);
		} catch(Exception ex) { }
		
		try {
			enemyFlame = currentElement.getElementsByTagName(
			"enemyFlameAttack").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_FLAME_ATTACK.setSound(enemyFlame);
		} catch(Exception ex) { }
		
		try {
			enemyOther = currentElement.getElementsByTagName(
			"enemyOtherAttack").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_OTHER_ATTACK.setSound(enemyOther);
		} catch(Exception ex) { }

		try {
			playerMelee = currentElement.getElementsByTagName(
			"playerMeleeAttack").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_MELEE_ATTACK.setSound(playerMelee);
		} catch(Exception ex) { }
		
		try {
			playerMartArts = currentElement.getElementsByTagName(
			"playerMartialArtsAttack").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_MART_ARTS_ATTACK.setSound(playerMartArts);
		} catch(Exception ex) { }

		try {
			playerMarksman = currentElement.getElementsByTagName(
			"playerMarksmanAttack").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_MARKSMAN_ATTACK.setSound(playerMarksman);
		} catch(Exception ex) { }
		
		try {
			playerFlame = currentElement.getElementsByTagName(
			"playerFlameAttack").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_FLAME_ATTACK.setSound(playerFlame);
		} catch(Exception ex) { }
		
		try {
			playerOther = currentElement.getElementsByTagName(
			"playerOtherAttack").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_OTHER_ATTACK.setSound(playerOther);
		} catch(Exception ex) { }

		try {
			explosion = currentElement.getElementsByTagName(
			"explosion").item(0).getFirstChild().getNodeValue();
			Sounds.EXPLOSION.setSound(explosion);
		} catch(Exception ex) { }
		
		try {
			playerDies = currentElement.getElementsByTagName(
			"playerDies").item(0).getFirstChild().getNodeValue();
			Sounds.PLAYER_DIES.setSound(playerDies);
		} catch(Exception ex) { }
		
		try {
			enemyDies = currentElement.getElementsByTagName(
			"enemyDies").item(0).getFirstChild().getNodeValue();
			Sounds.ENEMY_DIES.setSound(enemyDies);
		} catch(Exception ex) { }
		
		try {
			pickupItem = currentElement.getElementsByTagName(
			"pickupItem").item(0).getFirstChild().getNodeValue();
			Sounds.PICKUP_ITEM.setSound(pickupItem);
		} catch(Exception ex) { }
	}

}
