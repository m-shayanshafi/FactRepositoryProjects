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

package world;

import java.util.Vector;
import phys.Bsp_tree;
import phys.Shape_aabb;
import phys.Shape_sphere;
import phys.Trace;
import jglcore.JGL_Math;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DVector;


/**
 * BSP area that can be insert in a height-map land.
 * 
 * @author Nicolas Devere
 *
 */
public final class CollisionBSP implements CollisionNode {
	
	private static Shape_sphere eyeSphere = new Shape_sphere(new JGL_3DVector(), 0.05f);
	private static Trace testTrace = new Trace();
	
	private String name;
	private Bsp_tree bsp_phys;
	private JGL_3DVector min;
	private JGL_3DVector max;
	private Shape_aabb aabb;
	private Shape_aabb[] viewShapes;
	
	
	/**
	 * Constructs a BSP area.
	 * 
	 * @param _meshes : the 3D meshes
	 * @param _pos : the area position
	 * @param _aabb : the area AABB
	 * @param map : the height-map land which contains the BSP area
	 */
	public CollisionBSP(String id, Vector _meshes, Vector _viewShapes, JGL_3DVector _pos, Shape_aabb _aabb) {
		
		name = id;
		
		int i, j;
		
		viewShapes = new Shape_aabb[_viewShapes.size()];
		for (i=0; i<_viewShapes.size(); i++)
			viewShapes[i] = (Shape_aabb)_viewShapes.get(i);
		
		aabb = _aabb;
		aabb.setPosition(_pos);
		
		JGL_3DMesh m;
		JGL_3DVector v;
		JGL_3DMesh[] brushes = new JGL_3DMesh[_meshes.size()];
		
		for (i=0; i<brushes.length; i++) {
			m = (JGL_3DMesh)_meshes.get(i);
			for (j=0; j<m.getPoints().size(); j++) {
				v = (JGL_3DVector)m.getPoints().get(j);
				JGL_Math.vector_add(v, _pos, v);
			}
			
			brushes[i] = m;
		}
		bsp_phys = new Bsp_tree(brushes);
		
		min = new JGL_3DVector();
		max = new JGL_3DVector();
		min.assign(aabb.getMinX()+_pos.x, aabb.getMinY()+_pos.y, aabb.getMinZ()+_pos.z);
		max.assign(aabb.getMaxX()+_pos.x, aabb.getMaxY()+_pos.y, aabb.getMaxZ()+_pos.z);
	}
	
	
	public String getName() {
		return name;
	}
	
	
	/**
	 * Returns the BSP in this area.
	 * 
	 * @return the BSP in this area
	 */
	public Bsp_tree getBsp() {
		return bsp_phys;
	}
	
	
	/**
	 * Returns the bounding box of this area.
	 * 
	 * @return the bounding box of this area
	 */
	public Shape_aabb getAABB() {
		return aabb;
	}
	
	
	
	public Shape_aabb[] getViewShapes() {
		return viewShapes;
	}
	
	
	/**
	 * Traces the shape across the BSP area.
	 * 
	 * @param trace : the trace result
	 * @return if an impact occurs nearer than the trace's stored one
	 */
	public boolean collide(Trace trace) {
		
		//if (trace.dummy)
		//	return false;
		
		boolean test = false;
		
		if (viewShapes.length==0) {
			testTrace.reset(trace.cshape, trace.start, trace.end);
			test = aabb.isIn(testTrace.cshape);
			test |= aabb.trace(testTrace);
		}
		else {
			for (int i=0; i<viewShapes.length && !test; i++) {
				testTrace.reset(trace.cshape, trace.start, trace.end);
				test = viewShapes[i].isIn(testTrace.cshape);
				test |= viewShapes[i].trace(testTrace);
			}
		}
		
		if (test)
			return bsp_phys.trace(trace);
		return false;
	}
	
	
	/**
	 * Returns if the specified segment  
	 * intersects the map (height-map + BSP's).
	 * 
	 * @param p1 : segment start point
	 * @param p2 : segment end point
	 * @return if the segment intersects the map
	 */
	public boolean intersect(JGL_3DVector p1, JGL_3DVector p2) {
		/*boolean test = false;
		
		if (viewShapes.length==0) {
			testTrace.reset(eyeSphere, p1, p2);
			test = aabb.isIn(testTrace.cshape);
			test |= aabb.trace(testTrace);
		}
		else {
			for (int i=0; i<viewShapes.length && !test; i++) {
				testTrace.reset(eyeSphere, p1, p2);
				test = viewShapes[i].isIn(testTrace.cshape);
				test |= viewShapes[i].trace(testTrace);
			}
		}*/
		
		//if (test) {
			testTrace.reset(eyeSphere, p1, p2);
			bsp_phys.trace(testTrace);
			return testTrace.isImpact();
		//}
		//return false;
	}
	
	
	
}
