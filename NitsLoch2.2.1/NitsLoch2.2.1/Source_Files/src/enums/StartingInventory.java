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
 * Keeps track of the starting quantity of each inventory item.
 * @author Darren Watts
 * date 2/10/08
 *
 */
public enum StartingInventory {
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
	
	private int amount;
	
	/**
	 * Sets the amount of this item.
	 * @param num int : amount
	 */
	public void setAmount(int num){
		amount = num;
	}
	
	/**
	 * Gets the amount of this item. 
	 * @return int : amount
	 */
	public int getAmount(){
		return amount;
	}
	
	public static void clearAll() {
		for(StartingInventory i : StartingInventory.values()) {
			i.amount = 0;
		}
	}
}
