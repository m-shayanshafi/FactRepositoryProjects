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

import src.scenario.Images;

/**
 * Enum to keep track of what items can be placed on the ground.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum GroundItems {
	// Type numbers must go from 0 to n-1
	GRENADE(0, "Grenade"),
	DYNAMITE(1, "Dynamite"),
	BANDAIDS(2, "Bandaids"),
	BULLETS(3, "Bullets"),
	ROCKETS(4, "Rockets"),
	MONEY(5, "Money"),
	GRAVE(6, "Grave");
	
	private int type;
	private String name;
	private String imageLocation;
	
	private GroundItems(int type, String str){
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
	 * Sets the image location of this item.  Can be used when reading
	 * in the physics for a scenario.
	 * @param str String : path to image
	 */
	public void setImageLocation(String str){
		imageLocation = str;
		
		Images.getInstance().add(str);
	}
	
	/**
	 * Accessor for this item's type.  This number is used for the map format.
	 * @return int : type of ground item
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Checks to see whether or not this type of ground item is an explosive.
	 * @return boolean : is explosive
	 */
	public boolean isExplosive(){
		switch(this){
		case GRENADE:
		case DYNAMITE:
			return true;
		}
		return false;
	}
	
	/**
	 * Get the explosion type of this ground item.  If it is not an explosive,
	 * it will return null.
	 * @return ExplosionType : type of explosion
	 */
	public ExplosionType getExplosionType(){
		switch(this){
		case GRENADE:
			return ExplosionType.MINOR;
		case DYNAMITE:
			return ExplosionType.MEDIUM;
		}
		return null;
	}
	
	/**
	 * Gets the name of the item for use in the editor
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Finds the item type with the given name and returns that
	 * type.  If it cannot find a type, it returns null.
	 * @param str String : name
	 * @return GroundItems : type
	 */
	public static GroundItems getType(String str){
		for(GroundItems l : GroundItems.values()){
			if(l.getName() == null) continue;
			if(l.getName().equals(str)) return l;
		}
		return null;
	}

}
