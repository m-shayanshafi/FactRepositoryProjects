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
 * Collision shape implementation for vertical cylinders.
 * 
 * @author Nicolas Devere
 *
 */
public final class Shape_cylinder implements Shape {
	
	private JGL_3DVector pos;
	private float fl;
	private float ce;
	private float rad;
	
	
	
	public Shape_cylinder(JGL_3DVector position, float floor, float ceil, float radius) {
		pos = position;
		fl = floor;
		ce = ceil;
		rad = radius;
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
		
		float yn = planeNormal.y;
		
		if (yn==0f) return rad;
		
		if (yn>0f) {
			if (yn==1f) return -fl;
			return Math.abs(fl * yn) + Math.abs( rad * (float)Math.sqrt((1f - (yn * yn))) );
		}
		
		if (yn==-1f) return ce;
		return Math.abs(ce * yn) + Math.abs( rad * (float)Math.sqrt((1f - (yn * yn))) );
	}
	
	
	public boolean isIn(Shape shape) {
		return false;
	}
	
	
	public boolean trace(Trace trace) {
		return false;
	}
	
	
	public Object clone() {
		return new Shape_cylinder(new JGL_3DVector(pos.x, pos.y, pos.z), fl, ce, rad);
	}
	
}
