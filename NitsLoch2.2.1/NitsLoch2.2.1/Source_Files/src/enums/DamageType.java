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

import src.game.Messages;
import src.game.Player;

/**
 * Enums to keep track of damage types for weapons.  Damage types will determine
 * whether or not a weapon is capable of breaking weapons, destroying armor,
 * burning money, or destroying all items.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum DamageType {
	// Type numbers must go from 0 to n-1
	MELEE_NO_BREAK_NO_PIERCE			(0),
	MELEE_BREAK_NO_PIERCE				(1),
	MELEE_PIERCE_NO_BREAK				(2),
	MELEE_BREAK_AND_PIERCE				(3),
	MARTIAL_ARTS_NO_BREAK_NO_PIERCE		(4),
	MARTIAL_ARTS_BREAK_NO_PIERCE		(5),
	MARTIAL_ARTS_NO_BREAK				(6),
	MARTIAL_ARTS_BREAK_AND_PIERCE		(7),
	MARKSMAN_NO_BREAK_NO_PIERCE			(8),
	MARKSMAN_ARTS_BREAK_NO_PIERCE		(9),
	MARKSMAN_ARTS_NO_BREAK				(10),
	MARKSMAN_ARTS_BREAK_AND_PIERCE		(11),
	FLAME_DESTROY_MONEY					(12),
	FLAME_DESTROY_NOTHING				(13),
	OTHER_DESTROY_MONEY_AND_ITEMS		(14),
	OTHER_DESTROY_MONEY_ONLY			(15),
	OTHER_DESTROY_NOTHING				(16);
	
	private int type;
	
	/**
	 * Private constructor that sets the type number of the damage
	 * type.  The type number is used for scenario files.
	 * @param type int : type number
	 */
	private DamageType(int type){
		this.type = type;
	}
	
	/**
	 * Accessor for the type number of this damage type.  Used
	 * for scenario files.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}

	/**
	 * Find out which kidn of ability points to add to the player and
	 * calls the appropriate add method for the player.
	 * @param plr Player : the player
	 * @param num int : number of ability points to add.
	 */
	public void addAbilityPoints(Player plr, int num){
		if(isFighting()){
			plr.addFightingAbility(num);
			if(num == 1)
				Messages.getInstance().addMessage("You have gained " + 
					"1 fighting ability point.");
			else Messages.getInstance().addMessage("You have gained " + 
						num + " fighting ability points.");
		}
		else if(isMartialArts()){
			plr.addMartialArtsAbility(num);
			if(num == 1)
				Messages.getInstance().addMessage("You have gained " + 
					"1 martial arts ability point.");
			else Messages.getInstance().addMessage("You have gained " + 
						num + " martial arts ability points.");
		}
		else if(isMarksman()){
			plr.addMarksmanAbility(num);
			if(num == 1)
				Messages.getInstance().addMessage("You have gained " + 
					"1 marksmanship ability point.");
			else Messages.getInstance().addMessage("You have gained " + 
						num + " marksmanship ability points.");
		}
	}

	/**
	 * Returns the ability of the specified player based on what
	 * weapon he is currently using.
	 * @param plr Player : the player
	 * @return int : ability
	 */
	public int getCurrentAbility(Player plr){
		if(isFighting() || isOther())
			return plr.getFightingAbility();
		else if(isMartialArts())
			return plr.getMartialArtsAbility();
		else if(isMarksman())
			return plr.getMarksmanAbility();
		else return 0;
	}

	/**
	 * Checks to see whether or not this damage type is of a fighting type.
	 * @return boolean : is a melee/fighting type
	 */
	public boolean isFighting(){
		switch(this){
		case MELEE_NO_BREAK_NO_PIERCE:
		case MELEE_BREAK_NO_PIERCE:
		case MELEE_PIERCE_NO_BREAK:
		case MELEE_BREAK_AND_PIERCE:
			return true;
		}
		return false;
	}

	/**
	 * Checks to see whether or not this damage type is a marksman type.
	 * @return boolean : is marksman type
	 */
	public boolean isMarksman(){
		switch(this){
		case MARKSMAN_NO_BREAK_NO_PIERCE:
		case MARKSMAN_ARTS_BREAK_NO_PIERCE:
		case MARKSMAN_ARTS_NO_BREAK:
		case MARKSMAN_ARTS_BREAK_AND_PIERCE:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type is a martial arts type.
	 * @return boolean : is martial arts type
	 */
	public boolean isMartialArts(){
		switch(this){
		case MARTIAL_ARTS_NO_BREAK_NO_PIERCE:
		case MARTIAL_ARTS_BREAK_NO_PIERCE:
		case MARTIAL_ARTS_NO_BREAK:
		case MARTIAL_ARTS_BREAK_AND_PIERCE:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type is of type other.
	 * @return boolean : is of type other
	 */
	public boolean isOther(){
		switch(this){
		case OTHER_DESTROY_MONEY_AND_ITEMS:
		case OTHER_DESTROY_MONEY_ONLY:
		case OTHER_DESTROY_NOTHING:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type is a flame type.
	 * @return boolean : is flame type
	 */
	public boolean isFlame(){
		switch(this){
		case FLAME_DESTROY_MONEY:
		case FLAME_DESTROY_NOTHING:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type breaks weapons.
	 * @return boolean : breaks weapons
	 */
	public boolean breaksWeapons(){
		switch(this){
		case MELEE_BREAK_NO_PIERCE:
		case MELEE_BREAK_AND_PIERCE:
		case MARTIAL_ARTS_BREAK_NO_PIERCE:
		case MARTIAL_ARTS_BREAK_AND_PIERCE:
		case MARKSMAN_ARTS_BREAK_NO_PIERCE:
		case MARKSMAN_ARTS_BREAK_AND_PIERCE:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type destroys armor.
	 * @return boolean : destroys armor
	 */
	public boolean destroysArmor(){
		switch(this){
		case MELEE_PIERCE_NO_BREAK:
		case MELEE_BREAK_AND_PIERCE:
		case MARTIAL_ARTS_NO_BREAK:
		case MARTIAL_ARTS_BREAK_AND_PIERCE:
		case MARKSMAN_ARTS_NO_BREAK:
		case MARKSMAN_ARTS_BREAK_AND_PIERCE:
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see whether or not this damage type destroys money.
	 * @return boolean : destroys money
	 */
	public boolean destroysMoney(){
		switch(this){
		case FLAME_DESTROY_MONEY:
		case OTHER_DESTROY_MONEY_AND_ITEMS:
		case OTHER_DESTROY_MONEY_ONLY:
			return true;
		}
		return false;
	}
}
