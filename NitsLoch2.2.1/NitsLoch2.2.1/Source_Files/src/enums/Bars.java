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

import src.game.GameWorld;
import src.game.Messages;
import src.game.Player;

/**
 * Enum for bar types.  Keeps track of the different bar messages for the bars.
 * @author Darren Watts
 * date 1/9/08
 *
 */
public enum Bars {
	// Type numbers must go from 0 to n-1
	BAR_00(0),
	BAR_01(1),
	BAR_02(2),
	BAR_03(3),
	BAR_04(4),
	BAR_05(5),
	BAR_06(6),
	BAR_07(7),
	BAR_08(8),
	BAR_09(9),
	BAR_10(10),
	BAR_11(11),
	BAR_12(12),
	BAR_13(13),
	BAR_14(14),
	BAR_15(15),
	BAR_16(16),
	BAR_17(17),
	BAR_18(18),
	BAR_19(19),
	BAR_20(20),
	BAR_21(21),
	BAR_22(22),
	BAR_23(23),
	BAR_24(24),
	BAR_25(25),
	BAR_26(26),
	BAR_27(27),
	BAR_28(28),
	BAR_29(29),
	BAR_30(30),
	BAR_31(31),
	BAR_32(32),
	BAR_33(33),
	BAR_34(34),
	BAR_35(35),
	BAR_36(36),
	BAR_37(37),
	BAR_38(38),
	BAR_39(39),
	BAR_40(40),
	BAR_41(41),
	BAR_42(42),
	BAR_43(43),
	BAR_44(44),
	BAR_45(45),
	BAR_46(46),
	BAR_47(47),
	BAR_48(48),
	BAR_49(49);

	private int type;
	private String tonicWater;
	private String soda;
	private String gin;
	private String rum;
	private String scotch;
	private String redeye;
	private boolean isUsed;

	/**
	 * Sets up the type number for the bar.  Used in the scenario file.
	 * @param type int : type number
	 */
	private Bars(int type){
		this.type = type;
		isUsed = false;
	}

	/**
	 * Sets the messages for this bar type.
	 * @param tonicWater String : tonic water message
	 * @param soda String : soda message
	 * @param rum String : rum message
	 * @param scotch String : scotch message
	 * @param redeye String : red eye message
	 */
	public void setStats(
			String tonicWater,
			String soda,
			String gin,
			String rum,
			String scotch,
			String redeye){

		this.tonicWater = tonicWater;
		this.soda = soda;
		this.gin = gin;
		this.rum = rum;
		this.scotch = scotch;
		this.redeye = redeye;
		this.isUsed = true;
	}

	/**
	 * Returns the bar message based on the number of bar, and
	 * which drink was purchased.
	 * @param drink Drink : the drink that was purchased
	 * @param permutation int : number of bar
	 * @return String : message
	 */
	public static String getMessage(Drinks drink, int permutation){
		try {
			if(drink == Drinks.TONIC_WATER)
				return Bars.values()[permutation].tonicWater;
			else if(drink == Drinks.SODA)
				return Bars.values()[permutation].soda;
			else if(drink == Drinks.GIN)
				return Bars.values()[permutation].gin;
			else if(drink == Drinks.RUM)
				return Bars.values()[permutation].rum;
			else if(drink == Drinks.SCOTCH)
				return Bars.values()[permutation].scotch;
			else if(drink == Drinks.REDEYE)
				return Bars.values()[permutation].redeye;
			else return "";
		} catch(Exception ex){
			return "Sorry! This bar type does not match up with any in the scenario file.";
		}
	}

	/**
	 * Adds the bar message to the messages window if the player has
	 * enough money to buy the specified drink.  The money is then
	 * removed from the player's inventory.
	 * @param drink Drinks : drink that is purchased
	 * @param permutation int : bar number
	 */
	public static void purchaseItem(Drinks drink, int permutation){
		Player player = GameWorld.getInstance().getLocalPlayer();
		if(player.getMoney() >= drink.getCost()){
			Messages.getInstance().addMessage(Bars.getMessage(drink, permutation));
			player.removeMoney(drink.getCost());
		}
		else {
			Messages.getInstance().addMessage("You do not have enough money " +
			"to purchase this drink");
		}
	}

	/**
	 * Accessor for the type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	public boolean getUsed() {
		return isUsed;
	}
	
	public static void clearAll() {
		for(Bars b : Bars.values()) {
			b.isUsed = false;
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setTonicWater(String tonicWater) {
		this.tonicWater = tonicWater;
	}

	public void setSoda(String soda) {
		this.soda = soda;
	}

	public void setGin(String gin) {
		this.gin = gin;
	}

	public void setRum(String rum) {
		this.rum = rum;
	}

	public void setScotch(String scotch) {
		this.scotch = scotch;
	}

	public void setRedeye(String redeye) {
		this.redeye = redeye;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	public void remove() {
		tonicWater = soda = gin = rum = scotch = redeye = "";
		isUsed = false;
	}
}
