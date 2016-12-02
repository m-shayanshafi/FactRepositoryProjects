/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer and Markus J. Luzius
 *
 * Version 0.10.1
 * Copyright (C) 2012-03-25
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jskat.util.rule;

import org.jskat.data.JSkatOptions;
import org.jskat.data.SkatGameData;
import org.jskat.data.SkatTableOptions.RamschSkatOwner;
import org.jskat.data.Trick;
import org.jskat.util.Player;

/**
 * Implementation of skat rules for Ramsch games
 */
public class RamschRules extends SuitGrandRamschRules {

	/**
	 * @see BasicSkatRules#calcGameResult(SkatGameData)
	 */
	@Override
	public int calcGameResult(SkatGameData gameData) {

		int highestPlayerPoints = getGetHighestPlayerPoints(gameData);

		return highestPlayerPoints * getMultiplier(gameData);
	}

	private int getGetHighestPlayerPoints(SkatGameData gameData) {

		int result = 0;

		int foreHandPoints = gameData.getPlayerPoints(Player.FOREHAND);
		int middleHandPoints = gameData.getPlayerPoints(Player.MIDDLEHAND);
		int rearHandPoints = gameData.getPlayerPoints(Player.REARHAND);
		int skatPoints = JSkatOptions.instance().getRamschSkatOwner()==RamschSkatOwner.LOSER?gameData.getSkat().getTotalValue():0;

		// FIXME (jan 18.11.2011) make this simpler
		// FIXME (markus 22.02.2012) consider skat points - seems to be missing
		if (foreHandPoints > middleHandPoints && foreHandPoints > rearHandPoints) {
			result = (foreHandPoints + skatPoints) * -1;
		} else if (middleHandPoints > foreHandPoints && middleHandPoints > rearHandPoints) {
			result = (middleHandPoints + skatPoints) * -1;
		} else if (rearHandPoints > foreHandPoints && rearHandPoints > middleHandPoints) {
			result = (rearHandPoints + skatPoints) * -1;
		} else if (middleHandPoints > foreHandPoints && middleHandPoints == rearHandPoints) {
			result = middleHandPoints * -1;
		} else if (foreHandPoints > middleHandPoints && foreHandPoints == rearHandPoints) {
			result = foreHandPoints * -1;
		} else if (foreHandPoints > rearHandPoints && foreHandPoints == middleHandPoints) {
			result = foreHandPoints * -1;
		} else {
			// all player have 40 points
			result = -40;
		}

		return result;
	}

	/**
	 * @see BasicSkatRules#calcGameWon(SkatGameData)
	 */
	@Override
	public boolean calcGameWon(SkatGameData gameData) {
		return false;
	}

	/**
	 * Checks whether a player did a durchmarsch (walkthrough) in a ramsch game<br>
	 * durchmarsch means one player made all tricks
	 * 
	 * @param player
	 *            player to be checked
	 * @param gameData
	 *            Game data
	 * @return TRUE if the player played a durchmarsch
	 */
	public final static boolean isDurchmarsch(Player player, SkatGameData gameData) {
		for (Trick t : gameData.getTricks()) {
			if (t.getTrickWinner() != player)
				return false;
		}
		return true;
	}

	/**
	 * Checks whether a player was jungfrau (virgin) in a ramsch game<br>
	 * jungfrau means one player made no trick<br>
	 * two players who played jungfrau means a durchmarsch for the third player
	 * 
	 * @param player
	 *            player to be checked
	 * @param gameData
	 *            Game data
	 * @return TRUE if the player was jungfrau
	 */
	public final static boolean isJungfrau(Player player, SkatGameData gameData) {
		for (Trick t : gameData.getTricks()) {
			if (t.getTrickWinner() == player)
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMultiplier(SkatGameData gameData) {
		int multiplier = 1;

		if (gameData.isJungfrau()) {
			multiplier = 2;
		}

		multiplier = (int) (multiplier * Math.pow(2, gameData.getGeschoben()));
		return multiplier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlayWithJacks(SkatGameData gameData) {
		return false;
	}
}
