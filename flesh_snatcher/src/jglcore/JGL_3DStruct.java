//	Copyright 2008 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package jglcore;


/**
 * Interface representing a 3D displayable structure. A <code>JGL_3DStruct</code> object can be 
 * contained by a <code>JGL_3DMovable</code> object, and can be insert in a <code>JGL_3DBsp</code> tree.
 * 
 * @author Nicolas Devere
 *
 */
public interface JGL_3DStruct {
	
	/**
	 * Displays the structure according to the specified eye position.
	 * 
	 * @param eye : the eye position
	 */
	public void display(JGL_3DVector eye);
	
	
	/**
	 * Displays the structure according to the specified vision cone 
	 * (an eye position and 4 points to make a 4 sides pyramid).
	 * 
	 * @param eye : the eye position (cone base)
	 * @param cone : the vision cone represented by 4 points
	 */
	public void display(JGL_3DVector eye, JGL_3DVector[] cone);
	
}
