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

package src.scenario;

import java.util.HashMap;
import java.util.Map;

import src.enums.StoreItems;

/**
 * Contains a map for store names to their ID numbers.  This makes it so you
 * don't have to modify scenario xml files if you change the indexes of some
 * of the items.
 * @author Darren Watts
 * date 2/19/08
 *
 */
public class ItemNameToStoreID {

	private Map<String, Integer> map;
	
	/**
	 * Constructor that sets up the map.
	 */
	public ItemNameToStoreID(){
		map = new HashMap<String, Integer>();
		
		map.put("armor1", 0);
		map.put("armor2", 1);
		map.put("armor3", 2);
		map.put("armor4", 3);
		map.put("armor5", 4);
		map.put("armor6", 5);
		map.put("armor7", 6);
		map.put("armor8", 7);
		map.put("armor9", 8);
		map.put("armor10", 9);
		map.put("armor11", 10);
		map.put("armor12", 11);
		map.put("armor13", 12);
		map.put("armor14", 13);
		map.put("armor15", 14);
		map.put("armor16", 15);
		map.put("armor17", 16);
		map.put("armor18", 17);
		map.put("armor19", 18);
		map.put("armor20", 19);
		map.put("armor21", 20);
		map.put("armor22", 21);
		map.put("armor23", 22);
		map.put("armor24", 23);
		
		map.put("grenade", 24);
		map.put("dynamite", 25);
		map.put("bullets", 26);
		map.put("flame packs", 27);
		map.put("rockets", 28);
		
		map.put("weapon1", 29);
		map.put("weapon2", 30);
		map.put("weapon3", 31);
		map.put("weapon4", 32);
		map.put("weapon5", 33);
		map.put("weapon6", 34);
		map.put("weapon7", 35);
		map.put("weapon8", 36);
		map.put("weapon9", 37);
		map.put("weapon10", 38);
		map.put("weapon11", 39);
		map.put("weapon12", 30);
		map.put("weapon13", 41);
		map.put("weapon14", 42);
		map.put("weapon15", 43);
		map.put("weapon16", 44);
		map.put("weapon17", 45);
		map.put("weapon18", 46);
		map.put("weapon19", 47);
		map.put("weapon20", 48);
		map.put("weapon21", 49);
		map.put("weapon22", 50);
		map.put("weapon23", 51);
		map.put("weapon24", 52);
		
		map.put("ladder up", 53);
		map.put("ladder down", 54);
		map.put("map viewer", 55);
		map.put("export", 56);
		map.put("bandaids", 57);
	}
	
	/**
	 * Gets the ID number of the item based on its name.
	 * @param itemName String : item name
	 * @return int : item ID
	 */
	public int getItemID(String itemName){
		return map.get(itemName);
	}
	
	public String getItemMnemonic(int num) {
		for(String key : map.keySet()) {
			if(map.get(key) == num) {
				return key;
			}
		}
		return "";
	}
	
	public int getShopItemNumber(String name) {
		for(int i = 0; i < StoreItems.values().length; i++) {
			if(StoreItems.values()[i].getItemName().equals(name))
				return i;
		}
		return -1;
	}
}
