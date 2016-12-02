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
 * This class represents the border of two provinces
 * @author johannes
 *
 */
public class PPGrenze {
	
	/**
	 * id of this border
	 */
	public int oid;
	
	/**
	 * id of first province
	 */
	public int provinzID1;
	
	/**
	 * id of second province
	 */
	public int provinzID2;
	
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getProvinzID1() {
		return provinzID1;
	}
	public void setProvinzID1(int provinzID1) {
		this.provinzID1 = provinzID1;
	}
	public int getProvinzID2() {
		return provinzID2;
	}
	public void setProvinzID2(int provinzID2) {
		this.provinzID2 = provinzID2;
	}
}
