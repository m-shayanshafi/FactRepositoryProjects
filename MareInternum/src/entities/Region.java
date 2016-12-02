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

import java.util.Vector;

import tools.DB;

/**
 * This class stores all informations about a specific region
 * @author johannes
 *
 */
public class Region {
	
	/**
	 * id of region
	 */
	public int oid;
	
	/**
	 * name of region
	 */
	public String name;
	
	/**
	 * is this region type of sea or land 
	 */
	public String topologie;
	
	/**
	 * how much money do troops cost for his region
	 */
	public int kosten;
	
	/**
	 * a collection for all troop icons in this region
	 */
	private Vector<Einheit> v_Einheiten;
	
	public Region(int ss_id){
		oid=ss_id;
	}
	public Region(){}
	
	
	public void init(DB dbBroker){
		
		v_Einheiten=dbBroker.getEinheitenByRegion(oid);
		
	}
	
	/**
	 * returns count of all troops for a specific player
	 * @param spielernummer
	 * @return
	 */
	public int getEinheitenS(int spielernummer){
		Einheit aktEinheiten;
		int z_einheiten=0;
		for(int i=0;i<v_Einheiten.size();i++){
			aktEinheiten=(Einheit)v_Einheiten.elementAt(spielernummer);
			if(aktEinheiten.getSpielernummer()==spielernummer){
				z_einheiten=aktEinheiten.getZ_einheiten();
			}
		}
		return z_einheiten;
	}
	
	/**
	 * adds a number of troops for a specific player
	 * @param spielernummer
	 * @param ss_einheiten
	 */
	public void setEinheitenSDazu(int spielernummer,int ss_einheiten){
		Einheit aktEinheiten;
		for(int i=0;i<v_Einheiten.size();i++){
			aktEinheiten=(Einheit)v_Einheiten.elementAt(i);
			if(aktEinheiten.getSpielernummer()==spielernummer){
				aktEinheiten.setZ_einheiten(aktEinheiten.getZ_einheiten()+ss_einheiten);
			}
		}
	}
	
	/**
	 * sets the count of troops for a specific player id absolutely
	 * @param spielernummer
	 * @param ss_einheiten
	 */
	public void setEinheitenS(int spielernummer,int ss_einheiten){
		Einheit aktEinheiten;
		for(int i=0;i<v_Einheiten.size();i++){
			aktEinheiten=(Einheit)v_Einheiten.elementAt(spielernummer);
			if(aktEinheiten.getSpielernummer()==spielernummer){
				aktEinheiten.setZ_einheiten(ss_einheiten);
			}
		}
	}

	public int getKosten() {
		return kosten;
	}
	public void setKosten(int kosten) {
		this.kosten = kosten;
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
	
	public String getTopologie() {
		return topologie;
	}
	public void setTopologie(String topologie) {
		this.topologie = topologie;
	}
	
	public Vector<Einheit> getV_Einheiten() {
		return v_Einheiten;
	}

}
