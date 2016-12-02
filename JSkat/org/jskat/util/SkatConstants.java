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
package org.jskat.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Skat constants
 */
public final class SkatConstants {

	/**
	 * All possible bid values ordered from the lowest to the highest bid value
	 */
	public final static List<Integer> bidOrder = Arrays.asList(
			Integer.valueOf(18), Integer.valueOf(20), Integer.valueOf(22),
			Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(27),
			Integer.valueOf(30), Integer.valueOf(33), Integer.valueOf(35),
			Integer.valueOf(36), Integer.valueOf(40), Integer.valueOf(44),
			Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(48),
			Integer.valueOf(50), Integer.valueOf(54), Integer.valueOf(55),
			Integer.valueOf(59), Integer.valueOf(60), Integer.valueOf(63),
			Integer.valueOf(66), Integer.valueOf(70), Integer.valueOf(72),
			Integer.valueOf(77), Integer.valueOf(80), Integer.valueOf(81),
			Integer.valueOf(84), Integer.valueOf(88), Integer.valueOf(90),
			Integer.valueOf(96), Integer.valueOf(99), Integer.valueOf(100),
			Integer.valueOf(108), Integer.valueOf(110), Integer.valueOf(117),
			Integer.valueOf(120), Integer.valueOf(121), Integer.valueOf(126),
			Integer.valueOf(130), Integer.valueOf(132), Integer.valueOf(135),
			Integer.valueOf(140), Integer.valueOf(143), Integer.valueOf(144),
			Integer.valueOf(150), Integer.valueOf(153), Integer.valueOf(154),
			Integer.valueOf(156), Integer.valueOf(160), Integer.valueOf(162),
			Integer.valueOf(165), Integer.valueOf(168), Integer.valueOf(170),
			Integer.valueOf(176), Integer.valueOf(180), Integer.valueOf(187),
			Integer.valueOf(192), Integer.valueOf(198), Integer.valueOf(204),
			Integer.valueOf(216), Integer.valueOf(240), Integer.valueOf(264));

	/**
	 * Gets the next valid bid value
	 * 
	 * @param currBidValue
	 *            Current bid value
	 * @return Next valid bid value or the last possible bid value
	 */
	public final static int getNextBidValue(int currBidValue) {

		int result = -1;
		boolean nextBidValueFound = false;

		Iterator<Integer> iter = bidOrder.iterator();
		while (iter.hasNext() && !nextBidValueFound) {

			result = iter.next().intValue();

			if (result > currBidValue) {

				nextBidValueFound = true;
			}
		}

		return result;
	}

	/**
	 * Returns the multiplier for the game
	 * 
	 * @param gameType
	 *            Game type
	 * @param hand
	 *            TRUE if game is a hand game
	 * @param ouvert
	 *            TRUE if game is an ouvert game
	 * @return Multiplier
	 */
	public final static int getGameBaseValue(GameType gameType, boolean hand,
			boolean ouvert) {

		int multiplier = 0;

		switch (gameType) {
		case PASSED_IN:
			break;
		case CLUBS:
			multiplier = 12;
			break;
		case SPADES:
			multiplier = 11;
			break;
		case HEARTS:
			multiplier = 10;
			break;
		case DIAMONDS:
			multiplier = 9;
			break;
		case NULL:
			if (hand) {
				if (ouvert) {
					multiplier = 59;
				} else {
					multiplier = 35;
				}
			} else {
				if (ouvert) {
					multiplier = 46;
				} else {
					multiplier = 23;
				}
			}
			break;
		case GRAND:
			multiplier = 24;
			break;
		case RAMSCH:
			multiplier = 1;
			break;
		}

		return multiplier;
	}

	/**
	 * Returns the game value after the Seeger-Fabian system
	 * 
	 * @param declarer
	 *            TRUE, if calculation should be done for declarer of game
	 * @param gameValue
	 *            Game value
	 * @param numberOfPlayers
	 *            Number of players on the skat table
	 * 
	 * @return Tournament value
	 */
	public final static int getTournamentGameValue(boolean declarer,
			int gameValue, int numberOfPlayers) {

		int result = 0;

		if (declarer) {
			// calculation for declarer of the game
			if (gameValue > 0) {

				result = gameValue + 50;
			} else {

				result = gameValue - 50;
			}
		} else {
			// calculation for opponents
			if (gameValue < 0) {

				if (numberOfPlayers == 3) {

					result = 40;
				} else if (numberOfPlayers == 4) {

					result = 30;
				}
			}
		}

		return result;
	}
}
