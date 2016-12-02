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
import src.enums.GroundItems;

/**
 * Keeps track of items in the world.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Item implements Serializable {
	
	private static final long serialVersionUID = Constants.serialVersionUID;

	private GroundItems type;
	private int data;
	
	/**
	 * Constructor for the item object on the ground.  Take a GroundItems
	 * type so it knows what kind of item it is and a data int for other
	 * information.  The data variable can mean different things depending
	 * on the item.  Data for money = amount of money, data for explosives
	 * indicates how many moves are left before it explodes.  For all other
	 * types, data is meaningless.
	 * @param type
	 */
	public Item(GroundItems type, int data){
		this.type = type;
		this.data = data;
	}
	
	/**
	 * Method to pick up item and add to inventory.
	 */
	public void pickupItem(Player plr){
		switch(type){
		case GRENADE:
		case DYNAMITE:
			break;
		case BANDAIDS:
			if(data > 0) {
				plr.addBandaid(data);
				Messages.getInstance().addMessage("You just found " + data + " bandaids.");
			}
			else {
				plr.addBandaid(1);
				Messages.getInstance().addMessage("You just found some bandaids.");
			}
			src.enums.Sounds.PICKUP_ITEM.playSound();
			break;
		case BULLETS:
			if(data > 0){
				plr.addBullets(data);
				Messages.getInstance().addMessage("You just found " + data + " bullets.");
			}
			else {
				plr.addBullets(10);
				Messages.getInstance().addMessage("You just found 10 bullets.");
			}
			src.enums.Sounds.PICKUP_ITEM.playSound();
			break;
		case ROCKETS:
			if(data > 0){
				plr.addRockets(data);
				Messages.getInstance().addMessage("You just found " + data + " rockets.");
			}
			else {
				plr.addRockets(10);
				Messages.getInstance().addMessage("You just found 10 rockets.");
			}
			src.enums.Sounds.PICKUP_ITEM.playSound();
			break;
		case MONEY:
			if(data == 0) data = (int)( (Math.random()) * 40) + 10;
			if(data == 1)
				Messages.getInstance().addMessage("You just found 1 dollar.");
			else Messages.getInstance().addMessage("You just found " + data + " dollars.");
			plr.addMoney(data);
			src.enums.Sounds.PICKUP_ITEM.playSound();
			break;
		case GRAVE:
			GameWorld.getInstance().getLandAt(plr.getRow(), plr.getCol()).setItem(null);
			break;
		}
	}
	
	/**
	 * Decreases the value in the data variable by one.  Used for timer
	 * countdown on explosives.  Returns the value of data after the
	 * decrement, so the calling method knows whether or not to cause
	 * an explosion.
	 * @return int : data
	 */
	public int decrementData(){
		if(type.isExplosive()){
			data--;
		}
		return data;
	}
	
	/**
	 * Returns the image location of the item.
	 * @return String : image location
	 */
	public String getImage(){
		return type.getImage();
	}
	
	/**
	 * Accessor for the type of item.
	 * @return GroundItems : type
	 */
	public GroundItems getType(){
		return type;
	}
	
	/**
	 * Accessor for the data value of this item.
	 * @return int : data
	 */
	public int getData(){
		return data;
	}
}
