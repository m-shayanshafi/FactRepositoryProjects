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
 * Keeps track of different explosion types that can happen in the world.
 * @author Darren Watts
 * 11/30/07
 */
public enum ExplosionType {
	MINOR	(0),
	MEDIUM	(1),
	MAJOR	(2);
	
	private ExplosionType(int type) {
		this.type = type;
	}
	
	private int type;
	private int damage;
	
	/**
	 * Sets the damage for this type of explosion.
	 * @param dmg
	 */
	public void setDamage(int dmg) {
		damage = dmg;
	}
	
	/**
	 * Gets the damage amount for this explosion type.
	 * @return int : damage
	 */
	public int getDamage(){
		/*switch(this){
		case MINOR:
			return (int)(Math.random() * 50) + damage;
		case MEDIUM:
			return (int)(Math.random() * 80) + damage;
		case MAJOR:
			return (int)(Math.random() * 100) + 200;
		}
		return 0;*/
		return (int) (Math.random() * (damage/10.0)) + damage;
	}
	
	/**
	 * Get the type number of this kind of explosion.
	 * @return int : type number
	 */
	public int getType() {
		return type;
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public int getBaseDamage() {
		return damage;
	}
}
