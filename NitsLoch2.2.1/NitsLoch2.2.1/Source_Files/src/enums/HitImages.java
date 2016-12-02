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
 * Enum that keeps track of the different "hit" images that appear
 * when a player or enemy gets hit.
 * @author Darren Watts
 * date 11/16/07
 */
public enum HitImages {
	// Type numbers must go from 0 to n-1
	HIT1(0),
	HIT2(1),
	HIT3(2),
	HIT4(3),
	HIT5(4);
	
	private String imageLocation;
	private int type;
	private boolean isUsed;
	
	/**
	 * Constructor for HitImages.  Sets the type number of the hit image.
	 * @param type int : type number
	 */
	private HitImages(int type){
		this.type = type;
		isUsed = false;
	}
	
	/**
	 * Accessor for this hit image's type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the image location of this hit picture.
	 * @param str String : path to image
	 */
	public void setLocation(String str){
		imageLocation = str;
		isUsed = true;
		
		Images.getInstance().add(imageLocation);
	}
	
	/**
	 * Accessor for the hit image's location
	 * @return String : path to hit image
	 */
	public String getImage(){
		return imageLocation;
	}
	
	/**
	 * Accessor for whether or not this hit image is used in this scenario.
	 * @return boolean : is used.
	 */
	public boolean getIsUsed(){
		return isUsed;
	}
	
	/**
	 * Returns a random hit image.
	 * @return String : hit image
	 */
	public String getRandomHitImage(){
		int numUsedHitImages = 0;
		for(HitImages h : HitImages.values()) {
			if(h.getIsUsed()) numUsedHitImages++;
		}
		
		int random = (int)(Math.random() * numUsedHitImages+1 * 1.0);
		return HitImages.values()[random-1].getImage();
	}
	
	public static void clearAll() {
		for(HitImages h : HitImages.values()) {
			h.imageLocation = "";
			h.isUsed = false;
		}
	}
}
