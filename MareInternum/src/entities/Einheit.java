/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package entities;

/**
 * This class stores all information about one troop of a player
 * in a specific region
 * @author johannes
 *
 */
public class Einheit {
	
	/**
	 * id of this troop
	 */
	public int oid;
	
	/**
	 * x value of troop icon
	 */
	public int x;
	
	/**
	 * y value of troop icon
	 */
	public int y;
	
	/**
	 * id of player where this troop belongs to
	 */
	public int spielernummer;
	
	/**
	 * number of soldiers in this troop
	 */
	public int z_einheiten;
	
	/**
	 * id of region where this troop is placed
	 */
	public int regionID;
	
	public int getOid() {
		return oid;
	}
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getSpielernummer() {
		return spielernummer;
	}
	public void setSpielernummer(int spielernummer) {
		this.spielernummer = spielernummer;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ_einheiten() {
		return z_einheiten;
	}
	public void setZ_einheiten(int z_einheiten) {
		this.z_einheiten = z_einheiten;
	}
}
