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

import jglcore.JGL_3DMesh;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DVector;
import jglcore.JGL_Math;


/**
 * Collision shape implementation for axis-oriented bounding boxes.
 * 
 * @author Nicolas Devere
 *
 */
public final class Shape_aabb implements Shape {
	
	private JGL_3DVector pos;
	private JGL_3DVector min;
	private JGL_3DVector max;
	private Bsp_tree bsp;
	private Trace impact;
	
	
	/**
	 * Constructs a AABB with a position vector, 
	 * a relative min corner and a relative max corner.
	 * 
	 * @param position : the AABB position vector
	 * @param minCorner : the AABB min corner
	 * @param maxCorner : the AABB max corner
	 */
	public Shape_aabb(JGL_3DVector position, JGL_3DVector minCorner, JGL_3DVector maxCorner) {
		pos = position;
		min = minCorner;
		max = maxCorner;
		
		JGL_3DMesh mesh = new JGL_3DMesh();
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(min.x, min.y, max.z), new JGL_3DVector(max.x, min.y, max.z), new JGL_3DVector(min.x, max.y, max.z)));
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(max.x, min.y, max.z), new JGL_3DVector(max.x, min.y, min.z), new JGL_3DVector(max.x, max.y, max.z)));
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(max.x, min.y, min.z), new JGL_3DVector(min.x, min.y, min.z), new JGL_3DVector(max.x, max.y, min.z)));
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(min.x, min.y, min.z), new JGL_3DVector(min.x, min.y, max.z), new JGL_3DVector(min.x, max.y, min.z)));
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(min.x, max.y, max.z), new JGL_3DVector(max.x, max.y, max.z), new JGL_3DVector(min.x, max.y, min.z)));
		mesh.addFace(new JGL_3DTriangle(new JGL_3DVector(max.x, min.y, max.z), new JGL_3DVector(min.x, min.y, max.z), new JGL_3DVector(max.x, min.y, min.z)));
		JGL_3DMesh[] submesh = new JGL_3DMesh[1];
		submesh[0] = mesh;
		
		bsp = new Bsp_tree(submesh);
		impact = new Trace();
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
	
	
	public float getMinX() { return min.x; }
	public float getMaxX() { return max.x; }
	public float getMinY() { return min.y; }
	public float getMaxY() { return max.y; }
	public float getMinZ() { return min.z; }
	public float getMaxZ() { return max.z; }
	
	
	
	public float getOffset(JGL_3DVector planeNormal) {
		
		float xc, yc, zc;
		
		float xn = planeNormal.x;
		float yn = planeNormal.y;
		float zn = planeNormal.z;
		
		if (xn>0f) xc = min.x;
		else xc = max.x;
		
		if (yn>0f) yc = min.y;
		else yc = max.y;
		
		if (zn>0f) zc = min.z;
		else zc = max.z;
		
		return Math.abs((xc * xn) + (yc * yn) + (zc * zn));
	}
	
	
	/**
	 * Searches and stores collision impact 
	 * from the specified shape against this object.
	 * 
	 * @param trace : describes the shape movement and stores the impact data.
	 * @return if a collision occurs
	 */
	public boolean trace(Trace trace) {
		
		/*float offset;
		float d1, d2, dDiff;
		float fImpact = 1f;
		float frac = 1f;
		float fTest;
		JGL_3DVector normal = null;
		
		JGL_3DVector p1 = trace.start;
		JGL_3DVector p2 = trace.end;
		JGL_3DVector pos = getPosition();
		
		// Left side
		offset = trace.cshape.getOffset(Util4Phys.left.normal);
		d1 = (pos.x + min.x) - (p1.x + offset);
		d2 = (pos.x + min.x) - (p2.x + offset);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.left.normal;
			}
		}
		
		// Right side
		offset = trace.cshape.getOffset(Util4Phys.right.normal);
		d1 = (p1.x - offset) - (pos.x + max.x);
		d2 = (p2.x - offset) - (pos.x + max.x);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.right.normal;
			}
		}
		
		// Front side
		offset = trace.cshape.getOffset(Util4Phys.near.normal);
		d1 = (p1.z - offset) - (pos.z + max.z);
		d2 = (p2.z - offset) - (pos.z + max.z);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.near.normal;
			}
		}
		
		// Back side
		offset = trace.cshape.getOffset(Util4Phys.far.normal);
		d1 = (pos.z + min.z) - (p1.z + offset);
		d2 = (pos.z + min.z) - (p2.z + offset);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.far.normal;
			}
		}
		
		// Top side
		offset = trace.cshape.getOffset(Util4Phys.up.normal);
		d1 = (p1.y - offset) - (pos.y + max.y);
		d2 = (p2.y - offset) - (pos.y + max.y);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.up.normal;
			}
		}
		
		// Bottom side
		offset = trace.cshape.getOffset(Util4Phys.down.normal);
		d1 = (pos.y + min.y) - (p1.y + offset);
		d2 = (pos.y + min.y) - (p2.y + offset);
		if(d1>0f && d2>0f)
			return false;
		if (d1>0f && d2<=0f) {
			
			dDiff = 1f / (d1 - d2);
			fTest = d1 * dDiff;
			if (fTest<frac) {
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				frac = fTest;
				normal = Util4Phys.down.normal;
			}
		}
		
		if (frac<1f)
			return trace.setNearerImpact(normal, fImpact, frac);
		return false;*/
		
		if (trace.dummy)
			return false;
		
		impact.reset(trace.cshape, trace.start, trace.end);
		JGL_Math.vector_subtract(impact.start, pos, impact.start);
		JGL_Math.vector_subtract(impact.end, pos, impact.end);
		
		bsp.trace(impact);
		
		if (impact.dummy) {
			trace.dummy = true;
			return false;
		}
		
		if (impact.isImpact())
			return trace.setNearerImpact(impact.correction, impact.fractionImpact, impact.fractionReal);
		return false;
	}
	
	
	
	public boolean isIn(Shape shape) {
		
		JGL_3DVector spos = shape.getPosition();
		
		if (spos.x - pos.x < min.x - shape.getOffset(Util4Phys.left.normal) || 
			spos.x - pos.x > max.x + shape.getOffset(Util4Phys.right.normal)|| 
			spos.y - pos.y < min.y - shape.getOffset(Util4Phys.down.normal) || 
			spos.y - pos.y > max.y + shape.getOffset(Util4Phys.up.normal) 	|| 
			spos.z - pos.z < min.z - shape.getOffset(Util4Phys.far.normal) 	|| 
			spos.z - pos.z > max.z + shape.getOffset(Util4Phys.near.normal))
			return false;
		
		return true;
	}
	
	
	
	public Object clone() {
		return  new Shape_aabb(	new JGL_3DVector(pos.x, pos.y, pos.z), 
								new JGL_3DVector(getMinX(), getMinY(), getMinZ()), 
								new JGL_3DVector(getMaxX(), getMaxY(), getMaxZ()) );
	}

}
