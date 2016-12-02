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
 * This class represents a border between a region and a province
 * @author johannes
 *
 */
public class RPGrenze {
	
	/**
	 * id of border
	 */
	public int oid;
	
	/**
	 * id of region
	 */
	public int regionID;
	
	/**
	 * id of province
	 */
	public int provinzID;
	
	/**
	 * in which direction shows the arrow during attacks
	 */
	public String richtung;
	
	/**
	 * x value of arrow for attacks
	 */
	public int x;
	
	/**
	 * y value of arrow for attacks
	 */
	public int y;
	
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getProvinzID() {
		return provinzID;
	}
	public void setProvinzID(int provinzID) {
		this.provinzID = provinzID;
	}
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	public String getRichtung() {
		return richtung;
	}
	public void setRichtung(String richtung) {
		this.richtung = richtung;
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
}
