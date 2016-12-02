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
 * Different types of vehicles that can exist.
 * @author Darren Watts
 * date 11/18/07
 */
public enum VehicleType {
	// Type numbers must go from 0 to n-1
	NONE		(0, 0),
	BOAT		(1, 50),
	AIR_CAR		(2, 80);
	
	private int type;
	private int shotDamage;
	private String imageLocation;
	
	/**
	 * Sets the type and damage of the vehicle
	 * @param type int : type of vehicle
	 * @param shotDamage int : damage vehicle inflicts when firing.
	 */
	private VehicleType(int type, int damage){
		this.type = type;
		this.shotDamage = damage;
	}
	
	/**
	 * Sets the damage of this vehicle.
	 * @param damage int : damage when this vehicle fires.
	 * @param location String : path to image file
	 */
	public void setStats(int damage, String location){
		shotDamage = damage;
		imageLocation = location;
		
		Images.getInstance().add(imageLocation);
	}
	
	/**
	 * Accessor for the type of vehicle.
	 * @return int : type number.
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Accessor for the amount of damage this vehicle does when firing.
	 * @return int : damage
	 */
	public int getDamage(){
		return shotDamage;
	}
	
	/**
	 * Accessor for the path to the image.
	 * @return String : image location
	 */
	public String getImage(){
		return imageLocation;
	}
}
