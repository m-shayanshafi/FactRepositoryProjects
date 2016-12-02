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

import java.util.ArrayList;

/**
 * Preset configurations for armor shops.  There are up to 20 types
 * of armor shops, each one can have different armor available at
 * different prices.  This enum will keep track of those preset shop
 * configurations.
 * @author Darren Watts
 * date 1/12/08
 */
public enum ArmorPresets {
	P_000,
	P_001,
	P_002,
	P_003,
	P_004,
	P_005,
	P_006,
	P_007,
	P_008,
	P_009,
	P_010,
	P_011,
	P_012,
	P_013,
	P_014,
	P_015,
	P_016,
	P_017,
	P_018,
	P_019;
	
	private ArrayList<StoreItems> items;
	private ArrayList<Integer> prices;
	
	/**
	 * Private constructor.  Sets up lists for items and prices.
	 */
	private ArmorPresets(){
		items = new ArrayList<StoreItems>();
		prices = new ArrayList<Integer>();
	}
	
	/**
	 * Clears the Armor presets
	 */
	public static void clear(){
		for (ArmorPresets preset : ArmorPresets.values()){
			preset.items = new ArrayList<StoreItems>();
			preset.prices = new ArrayList<Integer>();
		}
	}
	
	/**
	 * Adds a type of armor to this preset.
	 * @param itemID int : type of armor
	 * @param price int : price of armor
	 */
	public void addArmor(int itemID, int price){
		if(items.contains(StoreItems.values()[itemID])) return;
		items.add(StoreItems.values()[itemID]);
		prices.add(price);
	}
	
	public void deleteArmor(int itemID){
		int itemIndex = -1;
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItemName().equals(StoreItems.values()[itemID].getItemName()))
				itemIndex = i;
		}
		if(itemIndex == -1) return;
		items.remove(itemIndex);
		prices.remove(itemIndex);
	}
	
	/**
	 * Gets the list of armor assigned to this preset.  It assigns
	 * the appropriate amount of money to the items that it's
	 * keeping track of, and then returns a list of the items.
	 * @return ArrayList<StoreItems> : Armor types.
	 */
	public ArrayList<StoreItems> getItems(){
		int index = 0;
		for(StoreItems item : items){
			item.setPrice(prices.get(index++));
		}
		return items;
	}
	
	public void setItems(ArrayList<StoreItems> newItems) {
		items = newItems;
		prices = new ArrayList<Integer>();
		for(StoreItems item : items) {
			prices.add(item.getPrice());
		}
	}
	
	public ArrayList<Integer> getPrices(){
		return prices;
	}
}
