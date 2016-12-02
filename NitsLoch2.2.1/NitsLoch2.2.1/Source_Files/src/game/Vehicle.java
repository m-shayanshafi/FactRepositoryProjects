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

package src.game;

import src.enums.VehicleType;

/**
 * This class keeps track of the vehicle objects in the world.
 * @author Darren Watts
 * date 11/18/07
 */
public class Vehicle {
	private VehicleType type;
	
	/**
	 * Constructor for vehicle.  Needs to know the type of vehicle.
	 * @param type VehicleType : type of vehicle
	 */
	public Vehicle(VehicleType type){
		this.type = type;
	}
	
	/**
	 * Accessor for the type of this vehicle object.
	 * @return VehicleType : type
	 */
	public VehicleType getType(){
		return type;
	}
	
	/**
	 * Gets the image location.
	 * @return String : path to image
	 */
	public String getImage(){
		return type.getImage();
	}
}
