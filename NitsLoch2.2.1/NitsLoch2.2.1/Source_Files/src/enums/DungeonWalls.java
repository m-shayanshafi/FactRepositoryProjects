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

package src.enums;

/**
 * Defines the walls that can appear in a dungeon and gives them a
 * value.  Used by the GUI.
 * @author Darren Watts
 * date 12/3/07
 */
public enum DungeonWalls {
	// Type numbers don't have to go from 0 to n-1, but must be unique.
	FRONT_WALL4		(0),
	FRONT_WALL3		(1),
	FRONT_WALL2		(2),
	FRONT_WALL		(3),
	LEFT_WALL4		(4),
	LEFT_WALL3		(5),
	LEFT_WALL2		(6),
	LEFT_WALL		(7),
	RIGHT_WALL4		(8),
	RIGHT_WALL3		(9),
	RIGHT_WALL2		(10),
	RIGHT_WALL		(11);
	
	private int type;
	
	/**
	 * Private constructor.  Sets up type number.
	 * @param num int : type number
	 */
	private DungeonWalls(int num){
		type = num;
	}
	
	/**
	 * Accessor for the type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}

}
