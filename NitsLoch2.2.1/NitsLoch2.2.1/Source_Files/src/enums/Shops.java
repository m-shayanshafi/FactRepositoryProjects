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

import java.awt.Color;
import java.util.ArrayList;

import src.scenario.Images;

/**
 * Enum to keep track of the different shops that can exist in the world.  Each
 * shop has a shop type integer associated with it to use with the map format.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum Shops {
	// Type numbers must go from 0 to n-1
	AMMO_L		(0, "Ammo Left"),
	AMMO_R		(1, "Ammo Right"),
	ARMOR_L		(2, "Armor Left"),
	ARMOR_R		(3, "Armor Right"),
	BAR_L		(4, "Bar Left"),
	BAR_R		(5, "Bar Right"),
	HOSPITAL_L	(6, "Hospital Left"),
	HOSPITAL_R	(7, "Hospital Right"),
	WEAPON_L	(8, "Weapon Left"),
	WEAPON_R	(9, "Weapon Right"),
	MAGIC_L		(10, "Magic Left"),
	MAGIC_R		(11, "Magic Right"),
	GENERIC_L	(12, "Generic Left"),
	GENERIC_R	(13, "Generic Right");

	private int type;
	private String name;
	private String imageLocation;

	/**
	 * Sets up the shop type.
	 * @param type int : type
	 */
	private Shops(int type, String str){
		this.type = type;
		name = str;
	}

	/**
	 * Returns the path to the image location (png).
	 * @return String : path to image location.
	 */
	public String getImage(){
		return imageLocation;
	}

	/**
	 * Sets the image location of this shop.  Can be used when reading
	 * in the physics for a scenario.
	 * @param str String : path to image
	 */
	public void setImageLocation(String str){
		imageLocation = str;

		Images.getInstance().add(str);
	}

	/**
	 * Accessor for this shop's type.  This number is used for the map format.
	 * @return int : type of shop
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Get the bar message based on the drink and permutatio of the shop.
	 * @param drink Drinks : the drink
	 * @param permutation int : shop number
	 * @return String : message
	 */
	public String getBarMessage(Drinks drink, int permutation){
		if(this == Shops.BAR_L || this == Shops.BAR_R){
			return Bars.getMessage(drink, permutation);
		}
		else return "";
	}

	/**
	 * Gets the color of the shop as it appears on the overhead map.
	 * @return Color : color
	 */
	public Color getColor(){
		switch(this){
		case AMMO_L:
		case AMMO_R:
			return Color.magenta;
		case ARMOR_L:
		case ARMOR_R:
			return Color.cyan;
		case WEAPON_L:
		case WEAPON_R:
			return Color.orange;
		case BAR_L:
		case BAR_R:
			return Color.blue;
		case HOSPITAL_L:
		case HOSPITAL_R:
			return Color.red;
		case MAGIC_L:
		case MAGIC_R:
			return Color.yellow;
		case GENERIC_L:
		case GENERIC_R:
			return Color.pink;
		}
		return Color.blue;
	}

	/**
	 * Gets the items that this shop sells.
	 * @return ArrayList<StoreItems> : items
	 */
	public ArrayList<StoreItems> getItems(int permutation){
		ArrayList<StoreItems> items = new ArrayList<StoreItems>();
		switch(this){
		case AMMO_L:
		case AMMO_R:
			return AmmoPresets.values()[permutation].getItems();
		case ARMOR_L:
		case ARMOR_R:
			return ArmorPresets.values()[permutation].getItems();
		case WEAPON_L:
		case WEAPON_R:
			return WeaponPresets.values()[permutation].getItems();
		case MAGIC_L:
		case MAGIC_R:
			return MagicPresets.values()[permutation].getItems();
		case GENERIC_L:
		case GENERIC_R:
			return GenericPresets.values()[permutation].getItems();
		}
		return items;
	}
	
	/**
	 * Get name as it will appear in the editor's GUI list.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
}
