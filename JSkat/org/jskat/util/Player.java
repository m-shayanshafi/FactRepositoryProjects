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

/**
 * Contains all player positions in a trick
 */
public enum Player {
	/**
	 * First player
	 */
	FOREHAND {
		@Override
		public int getOrder() { return 0; }
		@Override
		public Player getRightNeighbor() { return REARHAND; }
		@Override
		public Player getLeftNeighbor() { return MIDDLEHAND; }
	},
	/**
	 * Second player
	 */
	MIDDLEHAND { 
		@Override
		public int getOrder() { return 1; } 
		@Override
		public Player getRightNeighbor() { return FOREHAND; }
		@Override
		public Player getLeftNeighbor() { return REARHAND; }
	},
	/**
	 * Third player
	 */
	REARHAND { 
		@Override
		public int getOrder() { return 2; } 
		@Override
		public Player getRightNeighbor() { return MIDDLEHAND; }
		@Override
		public Player getLeftNeighbor() { return FOREHAND; }
	};
	
	/**
	 * Gets order of a player
	 * 
	 * @return Order of the player
	 */
	public abstract int getOrder();
	
	/**
	 * Gets the player right from the player
	 * 
	 * @return Player right from the player
	 */
	public abstract Player getRightNeighbor();
	
	/**
	 * Gets the player left from the player
	 * 
	 * @return Player left from the player
	 */
	public abstract Player getLeftNeighbor();
}
