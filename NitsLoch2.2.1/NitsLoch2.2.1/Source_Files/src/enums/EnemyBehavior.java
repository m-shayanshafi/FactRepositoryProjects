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
 * This enum is for enemies' fighting behavior.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum EnemyBehavior {
	// Type numbers must go from 0 to n-1
	INNOCENT(0),
	TERRITORIAL(1),
	MEAN(2),
	RESTRICTED(3),
	COWARDLY(4),
	DECEPTIVE(5),
	CLINGY(6);
	
	private int type;
	
	/**
	 * Private constructor for enemy behavior
	 * @param type int : type number
	 */
	private EnemyBehavior(int type){
		this.type = type;
	}
	
	/**
	 * Accessor for type of enemy behavior.  Number used for input file.
	 * @return int
	 */
	public int getType(){
		return type;
	}
}
