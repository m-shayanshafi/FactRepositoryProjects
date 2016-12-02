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
 * Types of exits.
 * @author Darren Watts
 * date 11/16/07
 *
 */
public enum ExitType {
	// Type numbers must go from 0 to n-1
	CITY_GATE		(0, "City Gate"),
	DUNGEON			(1, "Dungeon"),
	LADDER_UP		(2, "Ladder Up"),
	LADDER_DOWN		(3, "Ladder Down");

	private int type;
	private String name;
	private String openImage = "";
	private String closedImage = "";

	/**
	 * Sets the type of the exit.
	 * @param num int : type
	 * @param str String : name
	 */
	private ExitType(int num, String str){
		type = num;
		name = str;
	}

	/**
	 * Sets the image location for this exit type.
	 * @param open String : image location for a open exit
	 * @param closed String : image location for a closed exit
	 */
	public void setImage(String open, String closed){
		openImage = open;
		closedImage = closed;

		Images.getInstance().add(openImage);
		Images.getInstance().add(closedImage);
	}

	/**
	 * Accessor for this exit type's open image location
	 * @return String : path to image
	 */
	public String getOpenImage(){
		return openImage;
	}
	
	/**
	 * Accessor for this exit's closed image location.
	 * @return String : path to image
	 */
	public String getClosedImage(){
		return closedImage;
	}

	/**
	 * Accessor for the type number of this exit.  Used for scenario files.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Gets the name of the ExitType as it should appear on
	 * the editor GUI.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setOpenImage(String str) {
		openImage = str;
	}
	
	public void setClosedImage(String str) {
		closedImage = str;
	}
}
