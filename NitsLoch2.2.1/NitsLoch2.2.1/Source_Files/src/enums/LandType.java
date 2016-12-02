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

import java.io.Serializable;

/**
 * LandType enum.  Used for saving and loading games.
 * @author Darren Watts
 * date 1/26/08
 */
public enum LandType implements Serializable {
	EXIT			(0, "Exit"),
	OBSTRUCTION		(1, "Obstruction"),
	SHOP			(2, "Shop"),
	STREET			(3, "Street");
	//WATER			(4, "Water");
	
	private int typeNum;
	private String name;
	
	/**
	 * Private constructor that sets the type number of the
	 * land type along with the name that will appear in the
	 * list for the editor window.
	 * @param num int : type number
	 * @param str String : name
	 */
	private LandType(int num, String str){
		typeNum = num;
		name = str;
	}
	
	/**
	 * Gets the type number
	 * @return int : type number
	 */
	public int getTypeNum(){
		return typeNum;
	}
	
	/**
	 * Gets the name of the land type as it should appear in
	 * the Editor's GUI window.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
}
