//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package phys;

import jglcore.JGL_3DVector;


/**
 * General class for collidable bounding shapes. 
 * 
 * @author Nicolas Devere
 */
public interface Shape extends Tracable {
	
	
	/**
	 * Assigns the specified position vector to the collision shape.
	 * 
	 * @param position : the new position vector of the collision shape
	 */
	public void setPosition(JGL_3DVector position);
	
	
	/**
	 * Returns the position vector of the collision shape.
	 * 
	 * @return the position vector of the collision shape
	 */
	public JGL_3DVector getPosition();
	
	
	/**
	 * Returns the shape offset according to the specified plane.
	 * 
	 * @param plane : the plane to compute the offset
	 * @return the offset
	 */
	public float getOffset(JGL_3DVector planeNormal);
	
	
	/**
	 * Returns if the specified shape is intersecting this shape.
	 * 
	 * @param shape : the shape to test
	 * @return  if the specified shape is intersecting this shape
	 */
	public boolean isIn(Shape shape);
	
	
	public Object clone();
	
}
