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
 * Enum for weapons.  Number and names will be hardcoded.  Let user change damage and
 * weapon type.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum Weapon {
	// Type numbers must go from 0 to n-1
	W_00 	(0),
	W_01 	(1),
	W_02	(2),
	W_03	(3),
	W_04	(4),
	W_05	(5),
	W_06	(6),
	W_07	(7),
	W_08	(8),
	W_09	(9),
	W_10	(10),
	W_11	(11),
	W_12	(12),
	W_13	(13),
	W_14	(14),
	W_15	(15),
	W_16	(16),
	W_17	(17),
	W_18	(18),
	W_19	(19),
	W_20	(20),
	W_21	(21),
	W_22	(22),
	W_23	(23),
	W_24	(24);
	
	private int type;
	private int damage;
	private DamageType damageType;
	private String itemName;
	private String purchaseName;
	private String itemVerb;
	private boolean canBeBroken;
	private boolean shootsRockets;
	private boolean shootsFlamePacks;
	
	/**
	 * Private constructor that sets the type number of the weapon type.
	 * @param type int : type number
	 */
	private Weapon(int type){
		this.type = type;
		itemName = "";
	}
	
	/**
	 * Accessor for the type of weapon.  Used for reading in from scenario files.
	 * @return int : type
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the damage and the damage type of this item.  Used when reading in
	 * from scenario files.
	 * @param dam int : damage
	 * @param damType DamageType: Type of damage the weapon will have.
	 * @param name String : weapon name
	 * @param secondaryName String : name when purchasing item or find on an enemy.
	 * @param verb String : verb to use with weapon
	 * @param canBreak boolean : can be broken
	 * @param usesRockets boolean : shoots rockets
	 * @param usesFlamePacks boolean : uses flame packs
	 */
	public void setStats(String name, String secondaryName, String verb, int dam, DamageType damType,
			boolean canBreak, boolean usesRockets, boolean usesFlamePacks){
		itemName = name;
		purchaseName = secondaryName;
		itemVerb = verb;
		damage = dam;
		damageType = damType;
		canBeBroken = canBreak;
		shootsRockets = usesRockets;
		shootsFlamePacks = usesFlamePacks;
	}
	
	/**
	 * Accessor for the damage of the weapon.
	 * @return int : damage
	 */
	public int getDamage(){
		return damage;
	}
	
	/**
	 * Accessor for the damage type of the weapon.
	 * @return DamageType
	 */
	public DamageType getDamageType(){
		return damageType;
	}
	
	/**
	 * Name of the weapon
	 * @return String : name
	 */
	public String getItemName(){
		return itemName;
	}
	
	/**
	 * Gets the secondary name of the item.  This is used for purchasing
	 * items or picking up an item off an enemy.
	 * @return String : secondary name
	 */
	public String getSecondaryName(){
		return purchaseName;
	}
	
	/**
	 * Gets the verb to use with this weapon type.
	 * @return String : verb
	 */
	public String getVerb(){
		return itemVerb;
	}
	
	/**
	 * Checks to see whether or not the weapon can be destroyed.
	 * @return boolean : can be destroyed
	 */
	public boolean canBeBroken(){
		if(this != Weapon.W_00 && canBeBroken)
			return true;
		else return false;
	}
	
	/**
	 * Checks to see if this weapon type shoots rockets.
	 * @return boolean : uses rockets.
	 */
	public boolean usesRockets(){
		if(shootsRockets) return true;
		else return false;
	}
	
	/**
	 * Checks to see if this weapon type uses flame packs.
	 * @return boolean : uses flame packs
	 */
	public boolean usesFlamePacks(){
		if(shootsFlamePacks) return true;
		else return false;
	}
	
	public static void clearAll() {
		for(Weapon w : Weapon.values()) {
			w.itemName = "";
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setItemName(String str) {
		itemName = str;
	}
	
	public void setSecondaryName(String str) {
		purchaseName = str;
	}
	
	public void setVerb(String str) {
		itemVerb = str;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setDamageType(DamageType type) {
		damageType = type;
	}
	
	public void setCanBeBroken(boolean bool) {
		canBeBroken = bool;
	}
	
	public void setUsesRockets(boolean bool) {
		shootsRockets = bool;
	}
	
	public void setUsesFlamePacks(boolean bool) {
		shootsFlamePacks = bool;
	}
	
}
