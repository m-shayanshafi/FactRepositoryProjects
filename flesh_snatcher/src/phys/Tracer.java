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
import jglcore.JGL_3DPlane;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DBsp;


/**
 * Class providing methods to trace collisions.
 * 
 * @author Nicolas Devere
 *
 */
public final class Tracer {
	

	public static short VOLUME_PRECISION 	= 0;
	public static short FACE_PRECISION		= 1;
	private static short s_precision;
	
	
	// trace method
	private static Trace s_impact;
	private static Trace s_impact_t;
	
	// triangleTrace method
	private static JGL_3DVector s_intersect;
	
	// nodeSphereIntersection method
	private static JGL_3DVector s_impactPoint_sav;
	
	
	static {
		s_precision = VOLUME_PRECISION;
		
		s_impact = new Trace();
		s_impact_t = new Trace();
		
		s_intersect = new JGL_3DVector();
		s_impactPoint_sav = new JGL_3DVector();
	}
	
	
	
	/**
	 * Sets the precision level of the collision detection.<br><br>
	 * <code>VOLUME_PRECISION</code> means that only the convex 
	 * volumes of the BSP will be checked. This method is very fast, 
	 * but only works on consistent BSP's (strictly closed shapes) 
	 * and doesn't support face overlapping.<br><br>
	 * FACE_PRECISION means that the triangles in the BSP will be checked. 
	 * This method is slower but allows all kind of polygon structures.
	 * 
	 * @param arg : <code>JGL_TracerBSP.VOLUME_PRECISION</code> or 
	 * 				<code>JGL_TracerBSP.FACE_PRECISION</code>
	 */
	public static void setTracePrecision(short arg) {
		if (arg==VOLUME_PRECISION || arg==FACE_PRECISION)
			s_precision = arg;
	}
	
	
	
	
	/**
	 * Tests the intersection between a collision shape and a BSP, 
	 * and stores the result in the specified trace.
	 * 
	 * @param bsp : the BSP
	 * @param trace : describes the shape movement and stores the impact result
	 * @return if an intersection occurs with the BSP
	 */
	public static boolean trace(JGL_3DBsp bsp, Trace trace) {
		
		s_impact.reset(trace.cshape, trace.start, trace.end);
		
		if (s_precision == VOLUME_PRECISION) {
			s_impact_t.reset(trace.cshape, trace.start, trace.end);
			solidTrace(bsp);
		}
		else
			triangleTrace(bsp);
		
		if (s_impact.dummy)
			trace.dummy = true;
		
		if (s_impact.isImpact())
			return trace.setNearerImpact(s_impact.correction, s_impact.fractionImpact, s_impact.fractionReal);
		return false;
	}
	
	

	/**
	 * Tests the intersection with the specified BSP.
	 * 
	 * @param bsp : the BSP
	 */
	private static void solidTrace(JGL_3DBsp bsp) {
		
		float d1, d2, dDiff;
		float fReal, fImpact;
		JGL_3DPlane vect_i;
		
		// empty leaf
		if (bsp.type == JGL_3DBsp.EMPTY_LEAF) {
			s_impact_t.clearImpact();
			return;
		}
		
		// solid leaf
		if (bsp.type == JGL_3DBsp.SOLID_LEAF) {
			if (s_impact_t.isImpact())
				s_impact.setNearerImpact(s_impact_t.correction, s_impact_t.fractionImpact, s_impact_t.fractionReal);
			else
				s_impact.dummy = true;
			s_impact_t.clearImpact();
			return;
		}
		
		// node
		
		float offset = s_impact_t.cshape.getOffset(bsp.plane.normal);
		
		d1 = bsp.plane.distance(s_impact.start);
		d2 = bsp.plane.distance(s_impact.end);
		
		// before the node
		if (d1>offset && d2>offset) {
			solidTrace(bsp.front);
			return;
		}
		
		bsp.plane.normal.invert();
		float offset2 = s_impact.cshape.getOffset(bsp.plane.normal);
		bsp.plane.normal.invert();
		
		// behind the node
		if (d1<=-offset2 && d2<=-offset2) {
			solidTrace(bsp.rear);
			return;
		}
		
		// Crosses the node
		
		if (d1>offset && d2<=offset) {
			d1 -= offset;
			d2 -= offset;
			dDiff = 1f / (d1 - d2);
			fReal = d1 * dDiff;
			if (fReal>=s_impact.fractionReal)
				return;
			
			if (fReal<s_impact_t.fractionReal) {
				vect_i = bsp.plane;
				fImpact = (d1 - Util4Phys.MIN_DISTANCE) * dDiff;
				s_impact_t.setImpact(vect_i, fImpact, fReal);
			}
			else {
				vect_i = s_impact_t.correction;
				fReal = s_impact_t.fractionReal;
				fImpact = s_impact_t.fractionImpact;
			}
		}
		else {
			vect_i = s_impact_t.correction;
			fReal = s_impact_t.fractionReal;
			fImpact = s_impact_t.fractionImpact;
		}
		
		solidTrace(bsp.rear);
		s_impact_t.setImpact(vect_i, fImpact, fReal);
		solidTrace(bsp.front);
	}

	
	/**
	 * Tests the intersection with the triangles from the specified BSP.
	 * 
	 * @param bsp : the BSP
	 */
	private static void triangleTrace(JGL_3DBsp bsp) {
		
		if(bsp.type != JGL_3DBsp.NODE)
			return;
		
		float offset = s_impact.cshape.getOffset(bsp.plane.normal);
		
		float d1 = bsp.plane.distance(s_impact.start);
		float d2 = bsp.plane.distance(s_impact.end);
		
		// before the node
		if (d1>offset && d2>offset)
			triangleTrace(bsp.front);
		
		// behind the node
		else if (d1<=offset && d2<=offset) {
			triangleTrace(bsp.rear);
			bsp.plane.normal.invert();
			float offset2 = s_impact.cshape.getOffset(bsp.plane.normal);
			bsp.plane.normal.invert();
			if (d1>=-offset2 && d2>=-offset2)
				triangleTrace(bsp.front);
		}
		
		// crosses the node
		else {
			
			float nx = bsp.plane.normal.x * offset;
			float ny = bsp.plane.normal.y * offset;
			float nz = bsp.plane.normal.z * offset;
			
			float ex = s_impact.start.x - nx;
			float ey = s_impact.start.y - ny;
			float ez = s_impact.start.z - nz;
			
			if (d1 > 0f) {
				d1 -= offset;
				d2 -= offset;
				float d_diff = d1 - d2;
				if(d_diff != 0f) {
					
					float frac = d1 / d_diff;
					s_intersect.x = ex + (((s_impact.end.x - nx) - ex) * frac);
					s_intersect.y = ey + (((s_impact.end.y - ny) - ey) * frac);
					s_intersect.z = ez + (((s_impact.end.z - nz) - ez) * frac);
					
					if (nodeSphereIntersection(bsp, s_intersect, offset))
						s_impact.setNearerImpact(bsp.plane, (d1 - Util4Phys.MIN_DISTANCE) / d_diff, frac);
				}
			}
			
			triangleTrace(bsp.front);
			if(!s_impact.isImpact())
				triangleTrace(bsp.rear);
		}
		
	}
	
	
	
	
	/**
	 * Returns if there is intersection between the BSP node triangles and the sphere.
	 * 
	 * @param node : the BSP node
	 * @param center : the sphere center
	 * @param offset : the sphere offset
	 * @return if there is intersection between the BSP node triangles and the sphere.
	 */
	private static boolean nodeSphereIntersection(JGL_3DBsp node, JGL_3DVector center, float offset) {
		
		float d;
		short pos;
		JGL_3DTriangle t;
		float sqrOffset = offset * offset;
		
		for (int i=0; i<node.facesSize; i++) {
			t = (JGL_3DTriangle)node.faces.get(i);
			pos = Util4Phys.triangle_pointPosition(t, center);
			
			if (pos==0 && node.plane.distance(center)<offset)
				return true;
			
			else if (pos==1 || pos==2) {
				d = Util4Phys.segmentSphereIntersection(center, offset, t.point1, t.point2, s_impactPoint_sav);
				if (d < sqrOffset)
					return true;
			}
			
			else if (pos==3 || pos==4) {
				d = Util4Phys.segmentSphereIntersection(center, offset, t.point2, t.point3, s_impactPoint_sav);
				if (d < sqrOffset)
					return true;
			}
			
			else if (pos==5 || pos==6) {
				d = Util4Phys.segmentSphereIntersection(center, offset, t.point3, t.point1, s_impactPoint_sav);
				if (d < sqrOffset)
					return true;
			}
		}
		
		return false;
	}
	
}
