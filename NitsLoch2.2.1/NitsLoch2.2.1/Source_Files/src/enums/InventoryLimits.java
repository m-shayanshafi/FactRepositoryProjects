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
 * Keeps track of the limits assigned to each inventory item.
 * The limit number is the maxium amount of that item a
 * player can carry.
 * @author Darren Watts
 * date 2/9/08
 *
 */
public enum InventoryLimits {
	MONEY,
	BANDAIDS,
	GRENADES,
	DYNAMITE,
	BULLETS,
	ROCKETS,
	FLAME_PACKS,
	LADDER_UP,
	LADDER_DOWN,
	MAP_VIEWERS,
	EXPORTS;
	
	private int limit;
	
	/**
	 * Sets the limit of this item.
	 * @param num int : limit
	 */
	public void setLimit(int num){
		limit = num;
	}
	
	/**
	 * Gets the limit of this item. 
	 * @return int : limit
	 */
	public int getLimit(){
		return limit;
	}
	
	public static void clearAll() {
		for(InventoryLimits i : InventoryLimits.values()) {
			i.limit = 0;
		}
	}
}
