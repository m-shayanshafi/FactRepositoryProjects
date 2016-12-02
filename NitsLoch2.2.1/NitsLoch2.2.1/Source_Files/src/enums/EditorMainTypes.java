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

/**
 * This enum keeps track of the main types that can be placed in
 * the editor.
 * @author Darren Watts
 * date 1/28/08
 */
public enum EditorMainTypes {
	ITEM	("Item" , 0),
	ENEMY	("Enemy", 1),
	NPC		("NPC", 2),
	PLAYER	("Player", 3);
	
	private String name;
	private int index;
	
	/**
	 * Private constructor that sets up the name of the type.
	 * @param str String : name of option that will appear on
	 * the editor's list of choices.
	 */
	private EditorMainTypes(String str, int num){
		name = str;
		index = num;
	}
	
	/**
	 * Gets the name of the option as it should appear in the
	 * combo box on the editor GUI.
	 * @return String : name of option
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the enum index of the type.
	 * @return int : index
	 */
	public int getIndex(){
		return index;
	}

}
