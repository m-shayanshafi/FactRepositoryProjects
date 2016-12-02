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
 * This class keeps track of the different types of drinks that
 * can be purchased in a bar.
 * @author Darren Watts
 * date 1/10/08
 */
public enum Drinks {
	TONIC_WATER,
	SODA,
	GIN,
	RUM,
	SCOTCH,
	REDEYE;
	
	private int cost;
	
	/**
	 * Sets the cost of the drink at the bar.
	 * @param cost int : cost
	 */
	public void setCost(int cost){
		this.cost = cost;
	}
	
	/**
	 * Gets the cost for the drink
	 * @return int : cost
	 */
	public int getCost(){
		return cost;
	}
}
