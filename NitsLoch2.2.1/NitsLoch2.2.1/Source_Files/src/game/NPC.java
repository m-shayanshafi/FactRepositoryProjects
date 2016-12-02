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

import java.io.Serializable;

import src.Constants;
import src.enums.NPCs;

/**
 * This will keep track of NPC objects in the world.
 * @author Darren Watts
 * date 11/15/07
 * 
 */
public class NPC implements Serializable {
	
	private static final long serialVersionUID = Constants.serialVersionUID;
	
	private NPCs type;
	
	/**
	 * Constructor that sets up the type of NPC.
	 * @param type NPCs : type
	 */
	public NPC(NPCs type){
		this.type = type;
	}
	
	/**
	 * Adds the NPC's message to the messages window.
	 */
	public void talk(){
		type.talk();
	}
	
	/**
	 * Gets the image location of the NPC.
	 * @return String : image location
	 */
	public String getImage(){
		return type.getImage();
	}
	
	/**
	 * Accessor for this NPC's type.
	 * @return NPCs : type
	 */
	public NPCs getType(){
		return type;
	}
}
