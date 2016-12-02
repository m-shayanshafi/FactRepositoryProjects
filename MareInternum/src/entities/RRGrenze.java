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
 * This class represents a border between two regions
 * @author johannes
 *
 */
public class RRGrenze {
	/**
	 * id of border
	 */
	public int oid;
	
	/**
	 * id of first region
	 */
	public int regionID1;
	
	/**
	 * id of second region
	 */
	public int regionID2;
}
