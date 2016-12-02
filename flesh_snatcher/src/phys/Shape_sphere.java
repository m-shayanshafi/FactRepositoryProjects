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
 * Collision shape implementation for bounding spheres.
 * 
 * @author Nicolas Devere
 *
 */
public final class Shape_sphere implements Shape {
	
	private JGL_3DVector pos;
	private float offset;
	
	
	/**
	 * Constructs a bounding sphere with a position vector and a sphere offset.
	 * 
	 * @param position : the sphere position vector
	 * @param offset : the sphere offset
	 */
	public Shape_sphere(JGL_3DVector position, float offset) {
		pos = position;
		this.offset = offset;
		
	}
	
	
	/**
	 * Assigns the specified position vector to the collision shape.
	 * 
	 * @param position : the new position vector of the collision shape
	 */
	public void setPosition(JGL_3DVector position) {
		pos.assign(position);
	}
	
	
	/**
	 * Returns the position vector of the collision shape.
	 * 
	 * @return the position vector of the collision shape
	 */
	public JGL_3DVector getPosition() {
		return pos;
	}
	
	
	
	public float getOffset(JGL_3DVector planeNormal) {
		return offset;
	}
	
	
	
	/**
	 * returns the sphere offset.
	 * 
	 * @return the sphere offset
	 */
	public float getOffset() {
		return offset;
	}
	
	
	public boolean isIn(Shape shape) {
		return false;
	}
	
	
	/**
	 * Searches and stores collision impact 
	 * from the specified shape against this object.
	 * 
	 * @param trace : describes the shape movement and stores the impact data.
	 * @return if a collision occurs
	 */
	public boolean trace(Trace trace) {
		return false;
	}
	
	
	public Object clone() {
		return  new Shape_sphere(new JGL_3DVector(pos.x, pos.y, pos.z), offset);
	}
	
}
