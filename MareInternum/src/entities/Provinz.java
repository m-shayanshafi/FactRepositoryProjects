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
 * This class represents a province
 * @author johannes
 *
 */
public class Provinz {
	/**
	 * id
	 */
	public int oid;
	
	/**
	 * which player owns this province
	 */
	public int spielernummer;
	
	/**
	 * count of fortresses in this province
	 */
	public int z_lager;
	
	/**
	 * id of trade good from this province
	 */
	public int ware;
	
	/**
	 * is this province selected
	 */
	public boolean isAusgewaehlt;
	
	/**
	 * the name of this province
	 */
	public String name;
	
	/**
	 *the x value of the trading good icon 
	 */
	public int x_wMarker;
	
	/**
	 *the y value of the trading good icon 
	 */
	public int y_wMarker;
	
	/**
	 *the x value of the province flag 
	 */
	public int x_pMarker;
	
	/**
	 *the y value of the province flag 
	 */
	public int y_pMarker;
	
	/**
	 *has this province a city 
	 */
	public boolean isStadt;
	
	/**
	 *the x value of the city icon 
	 */
	public int x_Stadt;
	
	/**
	 *the y value of the city icon 
	 */
	public int y_Stadt;
	
	
	public boolean isStadt() {
		return isStadt;
	}
	public int getX_Stadt() {
		return x_Stadt;
	}
	public int getY_Stadt() {
		return y_Stadt;
	}
	public int getWare() {
		return ware;
	}
	public void setWare(int ware) {
		this.ware = ware;
	}
	public boolean isAusgewaehlt() {
		return isAusgewaehlt;
	}
	public void setAusgewaehlt(boolean isAusgwaehlt) {
		this.isAusgewaehlt = isAusgwaehlt;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOid() {
		return oid;
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
	public int getX_pMarker() {
		return x_pMarker;
	}
	public void setX_pMarker(int marker) {
		x_pMarker = marker;
	}
	public int getX_wMarker() {
		return x_wMarker;
	}
	public void setX_wMarker(int marker) {
		x_wMarker = marker;
	}
	public int getY_pMarker() {
		return y_pMarker;
	}
	public void setY_pMarker(int marker) {
		y_pMarker = marker;
	}
	public int getY_wMarker() {
		return y_wMarker;
	}
	public void setY_wMarker(int marker) {
		y_wMarker = marker;
	}
	public int getZ_lager() {
		return z_lager;
	}
	public void setZ_lager(int z_lager) {
		this.z_lager = z_lager;
	}
	
}
