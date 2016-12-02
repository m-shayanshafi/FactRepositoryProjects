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
 * Lists the items that you can buy in a store.  Has a method for every store
 * returning a list of the items that store has.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum StoreItems {
	// Armor
	ARMOR1	("", ""), // 0
	ARMOR2	("", ""), // 1
	ARMOR3	("", ""), // 2
	ARMOR4	("", ""), // 3
	ARMOR5	("", ""), // 4
	ARMOR6	("", ""), // 5
	ARMOR7	("", ""), // 6
	ARMOR8	("", ""), // 7
	ARMOR9	("", ""), // 8
	ARMOR10	("", ""), // 9
	ARMOR11	("", ""), // 10
	ARMOR12	("", ""), // 11
	ARMOR13	("", ""), // 12
	ARMOR14	("", ""), // 13
	ARMOR15	("", ""), // 14
	ARMOR16	("", ""), // 15
	ARMOR17	("", ""), // 16
	ARMOR18	("", ""), // 17
	ARMOR19	("", ""), // 18
	ARMOR20	("", ""), // 19
	ARMOR21	("", ""), // 20
	ARMOR22	("", ""), // 21
	ARMOR23	("", ""), // 22
	ARMOR24	("", ""), // 23

	// Ammo
	GRENADE				("Grenade", "a grenade"), // 24
	DYNAMITE			("Dynamite", "a stick of dynamite"), // 25
	BULLETS				("Bullets", "a box of bullets"), // 26
	FLAME_PACKS			("Flame Packs", "some fuel for your flame thrower"), // 27
	ROCKETS				("Rockets", "some rockets"), // 28

	// Weapons
	WEAPON1		(Weapon.W_01.getItemName(), Weapon.W_01.getSecondaryName()), // 29
	WEAPON2		(Weapon.W_02.getItemName(), Weapon.W_02.getSecondaryName()), // 30
	WEAPON3		(Weapon.W_03.getItemName(), Weapon.W_03.getSecondaryName()), // 31
	WEAPON4		(Weapon.W_04.getItemName(), Weapon.W_04.getSecondaryName()), // 32
	WEAPON5		(Weapon.W_05.getItemName(), Weapon.W_05.getSecondaryName()), // 33
	WEAPON6		(Weapon.W_06.getItemName(), Weapon.W_06.getSecondaryName()), // 34
	WEAPON7		(Weapon.W_07.getItemName(), Weapon.W_07.getSecondaryName()), // 35
	WEAPON8		(Weapon.W_08.getItemName(), Weapon.W_08.getSecondaryName()), // 36
	WEAPON9		(Weapon.W_09.getItemName(), Weapon.W_09.getSecondaryName()), // 37
	WEAPON10	(Weapon.W_10.getItemName(), Weapon.W_10.getSecondaryName()), // 38
	WEAPON11	(Weapon.W_11.getItemName(), Weapon.W_11.getSecondaryName()), // 39
	WEAPON12	(Weapon.W_12.getItemName(), Weapon.W_12.getSecondaryName()), // 40
	WEAPON13	(Weapon.W_13.getItemName(), Weapon.W_13.getSecondaryName()), // 41
	WEAPON14	(Weapon.W_14.getItemName(), Weapon.W_14.getSecondaryName()), // 42
	WEAPON15	(Weapon.W_15.getItemName(), Weapon.W_15.getSecondaryName()), // 43
	WEAPON16	(Weapon.W_16.getItemName(), Weapon.W_16.getSecondaryName()), // 44
	WEAPON17	(Weapon.W_17.getItemName(), Weapon.W_17.getSecondaryName()), // 45
	WEAPON18	(Weapon.W_18.getItemName(), Weapon.W_18.getSecondaryName()), // 46
	WEAPON19	(Weapon.W_19.getItemName(), Weapon.W_19.getSecondaryName()), // 47
	WEAPON20	(Weapon.W_20.getItemName(), Weapon.W_20.getSecondaryName()), // 48
	WEAPON21	(Weapon.W_21.getItemName(), Weapon.W_21.getSecondaryName()), // 49
	WEAPON22	(Weapon.W_22.getItemName(), Weapon.W_22.getSecondaryName()), // 50
	WEAPON23	(Weapon.W_23.getItemName(), Weapon.W_23.getSecondaryName()), // 51
	WEAPON24	(Weapon.W_24.getItemName(), Weapon.W_24.getSecondaryName()), // 52

	// Magic
	LADDER_UP			("Ladder Up", "a ladder up spell"), // 53
	LADDER_DOWN			("Ladder Down", "a ladder down spell"), // 54
	MAP_VIEWER			("Map Viewer", "a map viewer"), // 55
	EXPORTS				("Export", "an export"), // 56

	// Other
	BANDAIDS			("Bandaids", "some bandaids"); // 57

	private int price;
	private String itemName;
	private String purchaseName;

	/**
	 * Private constructor that sets up the 2 kinds of names of
	 * the item and initializes the cost to 0.  The cost will
	 * later be assigned based on the scenario file.
	 * @param itemName String : name of the item as it appears
	 * 		in the GUI window when buying the item.
	 * @param purchaseName String : name of the item as it
	 * 		appears in the messages window when purchasing the item.
	 */
	private StoreItems(String itemName, String purchaseName){
		this.itemName = itemName;
		this.purchaseName = purchaseName;
		price = 0;
	}
	
	/**
	 * Sets the names of this store item.
	 * @param itemName String : item name
	 * @param purchaseName String : secondary name
	 */
	public void setStats(String itemName, String purchaseName){
		this.itemName = itemName;
		this.purchaseName = purchaseName;
	}

	/**
	 * Accessor for the name of the item as it appears
	 * in the GUI window when purchasing the item.
	 * @return String : item name
	 */
	public String getItemName(){
		//if( ARMOR1.itemName.equals("")){ // Just do it once
			
			ARMOR1.setStats(Armor.A_01.getItemName(), Armor.A_01.getSecondaryName());
			ARMOR2.setStats(Armor.A_02.getItemName(), Armor.A_02.getSecondaryName());
			ARMOR3.setStats(Armor.A_03.getItemName(), Armor.A_03.getSecondaryName());
			ARMOR4.setStats(Armor.A_04.getItemName(), Armor.A_04.getSecondaryName());
			ARMOR5.setStats(Armor.A_05.getItemName(), Armor.A_05.getSecondaryName());
			ARMOR6.setStats(Armor.A_06.getItemName(), Armor.A_06.getSecondaryName());
			ARMOR7.setStats(Armor.A_07.getItemName(), Armor.A_07.getSecondaryName());
			ARMOR8.setStats(Armor.A_08.getItemName(), Armor.A_08.getSecondaryName());
			ARMOR9.setStats(Armor.A_09.getItemName(), Armor.A_09.getSecondaryName());
			ARMOR10.setStats(Armor.A_10.getItemName(), Armor.A_10.getSecondaryName());
			ARMOR11.setStats(Armor.A_11.getItemName(), Armor.A_11.getSecondaryName());
			ARMOR12.setStats(Armor.A_12.getItemName(), Armor.A_12.getSecondaryName());
			ARMOR13.setStats(Armor.A_13.getItemName(), Armor.A_13.getSecondaryName());
			ARMOR14.setStats(Armor.A_14.getItemName(), Armor.A_14.getSecondaryName());
			ARMOR15.setStats(Armor.A_15.getItemName(), Armor.A_15.getSecondaryName());
			ARMOR16.setStats(Armor.A_16.getItemName(), Armor.A_16.getSecondaryName());
			ARMOR17.setStats(Armor.A_17.getItemName(), Armor.A_17.getSecondaryName());
			ARMOR18.setStats(Armor.A_18.getItemName(), Armor.A_18.getSecondaryName());
			ARMOR19.setStats(Armor.A_19.getItemName(), Armor.A_19.getSecondaryName());
			ARMOR20.setStats(Armor.A_20.getItemName(), Armor.A_20.getSecondaryName());
			ARMOR21.setStats(Armor.A_21.getItemName(), Armor.A_21.getSecondaryName());
			ARMOR22.setStats(Armor.A_22.getItemName(), Armor.A_22.getSecondaryName());
			ARMOR23.setStats(Armor.A_23.getItemName(), Armor.A_23.getSecondaryName());
			ARMOR24.setStats(Armor.A_24.getItemName(), Armor.A_24.getSecondaryName());

			WEAPON1.setStats(Weapon.W_01.getItemName(), Weapon.W_01.getSecondaryName());
			WEAPON2.setStats(Weapon.W_02.getItemName(), Weapon.W_02.getSecondaryName());
			WEAPON3.setStats(Weapon.W_03.getItemName(), Weapon.W_03.getSecondaryName());
			WEAPON4.setStats(Weapon.W_04.getItemName(), Weapon.W_04.getSecondaryName());
			WEAPON5.setStats(Weapon.W_05.getItemName(), Weapon.W_05.getSecondaryName());
			WEAPON6.setStats(Weapon.W_06.getItemName(), Weapon.W_06.getSecondaryName());
			WEAPON7.setStats(Weapon.W_07.getItemName(), Weapon.W_07.getSecondaryName());
			WEAPON8.setStats(Weapon.W_08.getItemName(), Weapon.W_08.getSecondaryName());
			WEAPON9.setStats(Weapon.W_09.getItemName(), Weapon.W_09.getSecondaryName());
			WEAPON10.setStats(Weapon.W_10.getItemName(), Weapon.W_10.getSecondaryName());
			WEAPON11.setStats(Weapon.W_11.getItemName(), Weapon.W_11.getSecondaryName());
			WEAPON12.setStats(Weapon.W_12.getItemName(), Weapon.W_12.getSecondaryName());
			WEAPON13.setStats(Weapon.W_13.getItemName(), Weapon.W_13.getSecondaryName());
			WEAPON14.setStats(Weapon.W_14.getItemName(), Weapon.W_14.getSecondaryName());
			WEAPON15.setStats(Weapon.W_15.getItemName(), Weapon.W_15.getSecondaryName());
			WEAPON16.setStats(Weapon.W_16.getItemName(), Weapon.W_16.getSecondaryName());
			WEAPON17.setStats(Weapon.W_17.getItemName(), Weapon.W_17.getSecondaryName());
			WEAPON18.setStats(Weapon.W_18.getItemName(), Weapon.W_18.getSecondaryName());
			WEAPON19.setStats(Weapon.W_19.getItemName(), Weapon.W_19.getSecondaryName());
			WEAPON20.setStats(Weapon.W_20.getItemName(), Weapon.W_20.getSecondaryName());
			WEAPON21.setStats(Weapon.W_21.getItemName(), Weapon.W_21.getSecondaryName());
			WEAPON22.setStats(Weapon.W_22.getItemName(), Weapon.W_22.getSecondaryName());
			WEAPON23.setStats(Weapon.W_23.getItemName(), Weapon.W_23.getSecondaryName());
			WEAPON24.setStats(Weapon.W_24.getItemName(), Weapon.W_24.getSecondaryName());
		//}

		return itemName;
	}
	
	public static void clear() {
		ARMOR1.itemName = "";
	}

	/**
	 * Sets the price of the item.  Can be used when reading in a physics
	 * file.
	 * @param cost int
	 */
	public void setPrice(int cost){
		price = cost;
	}

	/**
	 * Accessor for the price of the item.
	 * @return int : price
	 */
	public int getPrice(){
		return price;
	}
	
	/**
	 * Checks to see if this store item is a weapon type.
	 * @return boolean : is a weapon
	 */
	public boolean isWeapon(){
		switch(this){
		case WEAPON1:
		case WEAPON2:
		case WEAPON3:
		case WEAPON4:
		case WEAPON5:
		case WEAPON6:
		case WEAPON7:
		case WEAPON8:
		case WEAPON9:
		case WEAPON10:
		case WEAPON11:
		case WEAPON12:
		case WEAPON13:
		case WEAPON14:
		case WEAPON15:
		case WEAPON16:
		case WEAPON17:
		case WEAPON18:
		case WEAPON19:
		case WEAPON20:
		case WEAPON21:
		case WEAPON22:
		case WEAPON23:
		case WEAPON24:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Purchases the item if the player has enough money.  If the purchase is
	 * successful, remove the money, add the item to the player's inventory and
	 * add a message indicating the sale was made.  Otherwise, add a message
	 * indicating the sale was not made.
	 * @param plr The player purchasing the item.
	 */
	public void buyItem(Player plr){
		int totalPrice = price;
		int weaponLevel = 1;
		if(plr.getLevel() > 1 && this.isWeapon()){
			for(int i = plr.getLevel(); i > 1; i--){
				if(plr.getMoney() >= price * i){
					totalPrice = price * i;
					weaponLevel = i;
					break;
				}
			}
		}
		if(plr.removeMoney(totalPrice)){
			if(addItem(plr, weaponLevel))
				Messages.getInstance().addMessage("Congratulations, you just purchased " +
						purchaseName + ".");
			else {
				plr.addMoney(totalPrice); // Add the money back
				Messages.getInstance().addMessage(
						"You do not have enough room in your inventory to purchase this item.");
			}
		}
		else {
			Messages.getInstance().addMessage("You do not have enough money to purchase " +
					purchaseName + ".");
		}
	}

	/**
	 * Calls the add item method and prints a message saying the player
	 * stole the item.
	 * @param plr Player : the player that stole an item
	 */
	public void stealItem(Player plr){
		addItem(plr, 1);
		Messages.getInstance().addMessage("You just stole " +
				purchaseName + ".");
		/*if(successfullySteal(plr)){
			addItem(plr);
			Messages.getInstance().addMessage("You just stole " +
					purchaseName + ".");
		}
		else {
			plr.setCaughtStealing(true);
			Messages.getInstance().addMessage("You were caught trying to steal. " +
			"The shopkeeper is coming after you!");
		}*/
	}

	/**
	 * Adds the StoreItem to the player's inventory.
	 * @param plr Player : the player
	 * @return boolean : sucessfully added item
	 */
	private boolean addItem(Player plr, int weaponLevel){
		switch(this){
		case ARMOR1:
			plr.addArmor(Armor.A_01);
			return true;
		case ARMOR2:
			plr.addArmor(Armor.A_02);
			return true;
		case ARMOR3:
			plr.addArmor(Armor.A_03);
			return true;
		case ARMOR4:
			plr.addArmor(Armor.A_04);
			return true;
		case ARMOR5:
			plr.addArmor(Armor.A_05);
			return true;
		case ARMOR6:
			plr.addArmor(Armor.A_06);
			return true;
		case ARMOR7:
			plr.addArmor(Armor.A_07);
			return true;
		case ARMOR8:
			plr.addArmor(Armor.A_08);
			return true;
		case ARMOR9:
			plr.addArmor(Armor.A_09);
			return true;
		case ARMOR10:
			plr.addArmor(Armor.A_10);
			return true;
		case ARMOR11:
			plr.addArmor(Armor.A_11);
			return true;
		case ARMOR12:
			plr.addArmor(Armor.A_12);
			return true;
		case ARMOR13:
			plr.addArmor(Armor.A_13);
			return true;
		case ARMOR14:
			plr.addArmor(Armor.A_14);
			return true;
		case ARMOR15:
			plr.addArmor(Armor.A_15);
			return true;
		case ARMOR16:
			plr.addArmor(Armor.A_16);
			return true;
		case ARMOR17:
			plr.addArmor(Armor.A_17);
			return true;
		case ARMOR18:
			plr.addArmor(Armor.A_18);
			return true;
		case ARMOR19:
			plr.addArmor(Armor.A_19);
			return true;
		case ARMOR20:
			plr.addArmor(Armor.A_20);
			return true;
		case ARMOR21:
			plr.addArmor(Armor.A_21);
			return true;
		case ARMOR22:
			plr.addArmor(Armor.A_22);
			return true;
		case ARMOR23:
			plr.addArmor(Armor.A_23);
			return true;
		case ARMOR24:
			plr.addArmor(Armor.A_24);
			return true;
		case GRENADE:
			if(plr.getNumGrenades() >= 
				InventoryLimits.GRENADES.getLimit()) return false;
			plr.addGrenade(1);
			return true;
		case DYNAMITE:
			if(plr.getNumDynamite() >= 
				InventoryLimits.DYNAMITE.getLimit()) return false;
			plr.addDynamite(1);
			return true;
		case BULLETS:
			if(plr.getNumBullets() >= 
				InventoryLimits.BULLETS.getLimit()) return false;
			plr.addBullets(50);
			return true;
		case FLAME_PACKS:
			if(plr.getNumFlamePacks() >= 
				InventoryLimits.FLAME_PACKS.getLimit()) return false;
			plr.addFlamePacks(5);
			return true;
		case ROCKETS:
			if(plr.getNumRockets() >= 
				InventoryLimits.ROCKETS.getLimit()) return false;
			plr.addRockets(5);
			return true;
		case WEAPON1:
			plr.addWeapon(Weapon.W_01, weaponLevel);
			return true;
		case WEAPON2:
			plr.addWeapon(Weapon.W_02, weaponLevel);
			return true;
		case WEAPON3:
			plr.addWeapon(Weapon.W_03, weaponLevel);
			return true;
		case WEAPON4:
			plr.addWeapon(Weapon.W_04, weaponLevel);
			return true;
		case WEAPON5:
			plr.addWeapon(Weapon.W_05, weaponLevel);
			return true;
		case WEAPON6:
			plr.addWeapon(Weapon.W_06, weaponLevel);
			return true;
		case WEAPON7:
			plr.addWeapon(Weapon.W_07, weaponLevel);
			return true;
		case WEAPON8:
			plr.addWeapon(Weapon.W_08, weaponLevel);
			return true;
		case WEAPON9:
			plr.addWeapon(Weapon.W_09, weaponLevel);
			return true;
		case WEAPON10:
			plr.addWeapon(Weapon.W_10, weaponLevel);
			return true;
		case WEAPON11:
			plr.addWeapon(Weapon.W_11, weaponLevel);
			return true;
		case WEAPON12:
			plr.addWeapon(Weapon.W_12, weaponLevel);
			return true;
		case WEAPON13:
			plr.addWeapon(Weapon.W_13, weaponLevel);
			return true;
		case WEAPON14:
			plr.addWeapon(Weapon.W_14, weaponLevel);
			return true;
		case WEAPON15:
			plr.addWeapon(Weapon.W_15, weaponLevel);
			return true;
		case WEAPON16:
			plr.addWeapon(Weapon.W_16, weaponLevel);
			return true;
		case WEAPON17:
			plr.addWeapon(Weapon.W_17, weaponLevel);
			return true;
		case WEAPON18:
			plr.addWeapon(Weapon.W_18, weaponLevel);
			return true;
		case WEAPON19:
			plr.addWeapon(Weapon.W_19, weaponLevel);
			return true;
		case WEAPON20:
			plr.addWeapon(Weapon.W_20, weaponLevel);
			return true;
		case WEAPON21:
			plr.addWeapon(Weapon.W_21, weaponLevel);
			return true;
		case WEAPON22:
			plr.addWeapon(Weapon.W_22, weaponLevel);
			return true;
		case WEAPON23:
			plr.addWeapon(Weapon.W_23, weaponLevel);
			return true;
		case WEAPON24:
			plr.addWeapon(Weapon.W_24, weaponLevel);
			return true;
		case LADDER_UP:
			if(plr.getNumLaddersUp() >= 
				InventoryLimits.LADDER_UP.getLimit()) return false;
			plr.addLadderUp(1);
			return true;
		case LADDER_DOWN:
			if(plr.getNumLaddersDown() >= 
				InventoryLimits.LADDER_DOWN.getLimit()) return false;
			plr.addLadderDown(1);
			return true;
		case BANDAIDS:
			if(plr.getNumBandaids() >= 
				InventoryLimits.BANDAIDS.getLimit()) return false;
			plr.addBandaid(1);
			return true;
		case MAP_VIEWER:
			if(plr.getNumMapViewers() >= 
				InventoryLimits.MAP_VIEWERS.getLimit()) return false;
			plr.addMapViewer(1);
			return true;
		case EXPORTS:
			if(plr.getNumExports() >= 
				InventoryLimits.EXPORTS.getLimit()) return false;
			plr.addExports(1);
			return true;
		}
		return false;
	}
}
