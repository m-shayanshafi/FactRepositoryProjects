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
import jglcore.JGL_Math;

/**
 * Collision shape implementation for bounding BSP trees.
 * 
 * @author Nicolas Devere
 *
 */
public class Shape_bsp implements Shape {
	
	private JGL_3DVector pos;
	private Bsp_tree bsp;
	private Trace impact;
	
	
	/**
	 * Constructs a BSP bounding shape with a position vector and a BSP tree.
	 * 
	 * @param position : the shape position
	 * @param bspTree : the BSP tree
	 */
	public Shape_bsp(JGL_3DVector position, Bsp_tree bspTree) {
		pos = position;
		bsp = bspTree;
		impact = new Trace();
	}
	

	@Override
	public float getOffset(JGL_3DVector planeNormal) {
		// TODO Auto-generated method stub
		return -1f;
	}

	@Override
	public JGL_3DVector getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}

	
	public boolean isIn(Shape shape) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPosition(JGL_3DVector position) {
		// TODO Auto-generated method stub
		pos.assign(position);
	}

	@Override
	public boolean trace(Trace trace) {
		// TODO Auto-generated method stub
		if (trace.dummy)
			return false;
		
		impact.reset(trace.cshape, trace.start, trace.end);
		JGL_Math.vector_subtract(impact.start, pos, impact.start);
		JGL_Math.vector_subtract(impact.end, pos, impact.end);
		
		if (bsp.trace(impact))
			return trace.setNearerImpact(impact.correction, impact.fractionImpact, impact.fractionReal);
		return false;
	}
	
	public Object clone() {
		return new Shape_bsp(new JGL_3DVector(pos.x, pos.y, pos.z), bsp);
	}
}
