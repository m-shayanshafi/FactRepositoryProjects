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
 * Enum for armor types.  Number and names will be hardcoded.  People can change
 * absorption amounts.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum Armor {
	// Type numbers must go from 0 to n-1
	A_00(0),
	A_01(1),
	A_02(2),
	A_03(3),
	A_04(4),
	A_05(5),
	A_06(6),
	A_07(7),
	A_08(8),
	A_09(9),
	A_10(10),
	A_11(11),
	A_12(12),
	A_13(13),
	A_14(14),
	A_15(15),
	A_16(16),
	A_17(17),
	A_18(18),
	A_19(19),
	A_20(20),
	A_21(21),
	A_22(22),
	A_23(23),
	A_24(24);
	/*STREET_CLOTHES		(0, 0, 0, 0, 0, 0, "street clothes"),
	LEATHER				(1, 20, 15, 5, 0, 5, "a leather vest"),
	METAL				(2, 50, 40, 8, 5, 8, "a metal vest"),
	BULLET_PROOF		(3, 15, 15, 60, 5, 15, "a bullet-proof vest"),
	FLAME_RETARDANT		(4, 10, 10, 5, 60, 15, "a flame retardant vest"),
	BULLET_FLAME		(5, 15, 15, 60, 60, 20, "a bullet-proof/flame retardant vest");*/
	
	private int type;
	private String itemName;
	private String purchaseName;
	private int absorbMelee;
	private int absorbMartialArts;
	private int absorbMarksman;
	private int absorbFlame;
	private int absorbOther;
	private boolean canBreak;
	
	/**
	 * Private constructor for the armor type
	 * @param type int : type number
	 */
	private Armor(int type){
		this.type = type;
		itemName = "";
	}
	
	/**
	 * Gets the type number of the armor to use with the map format.
	 * @return int : type number.
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Gets the item name of the armor.
	 * @return String : item name
	 */
	public String getItemName(){
		return itemName;
	}
	
	/**
	 * Gets the secondary name of the armor.
	 * @return String : secondary name
	 */
	public String getSecondaryName(){
		return purchaseName;
	}
	
	/**
	 * Sets the absorption stats of this armor.  Absorption stats are a percentage
	 * of damage absorbed if hit with the specified damage type.  For example, if
	 * absorbMelee is set to 20, 20% of melee damage will be absorbed by the armor.
	 * @param itemName String : item name
	 * @param secondName String : secondary name
	 * @param absorbMelee int : absorption melee percent
	 * @param absorbMartialArts int : absorption martial arts percent.
	 * @param absorbMarksman int : absorption marksman percent.
	 * @param absorbFlame int : absorption flame percent.
	 * @param absorbOther int : absorption other percent.
	 * @param canBreak boolean : armor can be destroyed
	 */
	public void setStats(
			String itemName,
			String secondName,
			int absorbMelee,
			int absorbMartialArts,
			int absorbMarksman,
			int absorbFlame,
			int absorbOther,
			boolean canBreak){
		this.itemName = itemName;
		this.purchaseName = secondName;
		this.absorbMelee = absorbMelee;
		this.absorbMartialArts = absorbMartialArts;
		this.absorbMarksman = absorbMarksman;
		this.absorbFlame = absorbFlame;
		this.absorbOther = absorbOther;
		this.canBreak = canBreak;
	}
	
	/**
	 * Checks whether this kind of armor can be destroyed.
	 * @return boolean : can be destroyed
	 */
	public boolean canBeDestroyed(){
		if(this != Armor.A_00 && canBreak)
			return true;
		else return false;
	}
	
	/**
	 * Returns the amount of damage absorbed based on the damage type
	 * the attacker is using.
	 * @param damType DamageType : damage type
	 * @return int : percent absorbed
	 */
	public int getAbsorb(DamageType damType){
		if(damType.isFighting())
			return getAbsorbMelee();
		else if(damType.isFlame())
			return getAbsorbFlame();
		else if(damType.isMarksman())
			return getAbsorbMarksman();
		else if(damType.isMartialArts())
			return getAbsorbMartialArts();
		else return getAbsorbOther();
	}
	
	/**
	 * Accessor for absorption melee percent.
	 * @return int
	 */
	public int getAbsorbMelee(){
		return absorbMelee;
	}
	
	/**
	 * Accessor for absorption martial arts percent.
	 * @return int
	 */
	public int getAbsorbMartialArts(){
		return absorbMartialArts;
	}
	
	/**
	 * Accessor for absorption marksman percent.
	 * @return int
	 */
	public int getAbsorbMarksman(){
		return absorbMarksman;
	}
	
	/**
	 * Accessor for absorption flame percent.
	 * @return int
	 */
	public int getAbsorbFlame(){
		return absorbFlame;
	}
	
	/**
	 * Accessor for absorption other percent.
	 * @return int
	 */
	public int getAbsorbOther(){
		return absorbOther;
	}
	
	public static void clearAll() {
		for(Armor a : Armor.values()) {
			a.setStats("", "", 0, 0, 0, 0, 0, true);
			a.itemName = "";
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
	
	public void setMelee(int melee) {
		absorbMelee = melee;
	}
	
	public void setMartialArts(int martialArts) {
		absorbMartialArts = martialArts;
	}
	
	public void setMarksman(int marksman) {
		absorbMarksman = marksman;
	}
	
	public void setFlame(int flame) {
		absorbFlame = flame;
	}
	
	public void setOther(int other) {
		absorbOther = other;
	}

	public void setCanBeBroken(boolean bool) {
		canBreak = bool;
	}

	public void remove() {
		itemName = "";
	}

}
