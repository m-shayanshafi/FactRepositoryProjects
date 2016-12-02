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
package org.jskat.ai.nn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jskat.util.GameType;

/**
 * Holds the results of all game simulations
 */
public class SimulationResults {

	private Map<GameType, Double> wonRates = new HashMap<GameType, Double>();

	Double getWonRate(GameType gameType) {

		return wonRates.get(gameType);
	}

	void setWonRate(GameType gameType, Double wonRate) {

		wonRates.put(gameType, wonRate);
	}

	Collection<Double> getAllWonRates() {
		return wonRates.values();
	}
}
