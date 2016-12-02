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
package org.jskat.ai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * All implemented player types
 */
public enum PlayerType {

	/**
	 * Algorithmic player
	 */
	ALGORITHMIC("org.jskat.ai.algorithmic.AlgorithmicAIPlayer"), //$NON-NLS-1$
	/**
	 * Random player
	 */
	RANDOM("org.jskat.ai.rnd.AIPlayerRND"), //$NON-NLS-1$
	/**
	 * Neural network player
	 */
	NEURAL_NETWORK("org.jskat.ai.nn.AIPlayerNN"), //$NON-NLS-1$
	/**
	 * Human player
	 */
	HUMAN(null);

	private static Log log = LogFactory.getLog(PlayerType.class);

	private final String implementingClass;

	private PlayerType(String implementingClass) {
		// class is not checked here -
		// otherwise a missing class would already result in an exception here,
		// even if it is not required
		this.implementingClass = implementingClass;
	}

	/**
	 * Creates a new instance of an ai player type<br>
	 * Note: Creation of an ai player fails with an exception only at this point
	 * if this ai player has actually been selected.
	 * 
	 * @param type
	 *            a player type which must not be HUMAN
	 * @return an instance of the ai player's main class
	 * @throws IllegalArgumentException
	 *             if PlayerType==HUMAN
	 */
	public static JSkatPlayer getPlayerInstance(PlayerType type) {
		if (type == HUMAN)
			throw new IllegalArgumentException(
					".getPlayerInstance(..) cannot be used for human players");
		JSkatPlayer player = null;

		try {
			player = (JSkatPlayer) Class.forName(type.implementingClass)
					.newInstance();
		} catch (Exception ex) {
			// handle exception case
			try {
				player = (JSkatPlayer) Class.forName(RANDOM.implementingClass)
						.newInstance();
			} catch (Exception e) {
				log.warn("Cannot get JSkatPlayer: " + e.getClass() + ": "
						+ e.getMessage());
			}
		}

		return player;
	}

	/**
	 * Gets a player type from a string
	 * 
	 * @param playerString
	 *            Player type as string
	 * @return Player type or NULL if the player type does not exists
	 */
	public static PlayerType getPlayerTypeFromString(String playerString) {

		PlayerType result = null;

		for (PlayerType currPlayerType : PlayerType.values()) {

			if (currPlayerType.toString().equals(playerString)) {

				result = currPlayerType;
			}
		}

		return result;
	}
}
