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
 * This class will hold the image locations of the explosions.  Explosions
 * are split into 9 squares, and there is an image for each square.
 * @author Darren Watts
 * date 12/2/07
 */
public enum ExplosionImages {
	// Type numbers must go from 0 to n-1
	CENTER		(0),
	NORTH		(1),
	NORTHEAST	(2),
	EAST		(3),
	SOUTHEAST	(4),
	SOUTH		(5),
	SOUTHWEST	(6),
	WEST		(7),
	NORTHWEST	(8);
	
	private int type;
	private String image;
	
	/**
	 * Private constructor that sets the type number of the explosion image.
	 * @param num int : type number
	 */
	private ExplosionImages(int num){
		type = num;
	}
	
	/**
	 * Accessor for the explosion's type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the image location of the explosions.
	 * @param imageLocation String : image location
	 */
	public void setImage(String imageLocation){
		image = imageLocation;
		
		Images.getInstance().add(image);
	}
	
	/**
	 * Accessor for the image location of the explosion.
	 * @return String : image location
	 */
	public String getImage(){
		return image;
	}

}
