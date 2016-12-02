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

import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DBsp;
import jglcore.JGL_Math;
//import java.util.Vector;


/**
 * Class representing a brush-based collision BSP.
 * 
 * @author Nicolas Devere
 *
 */
public final class Bsp_tree {
	
	
	private static Trace s_impact = new Trace();
	
	private Bsp_brush[] brushes;
	private Bsp_node root;
	
	
	/**
	 * Constructs a collision BSP with an array of convex brushes.
	 * 
	 * @param _brushes : the array of convex brushes
	 */
	public Bsp_tree(JGL_3DMesh[] _meshes) {
		
		int i, j;
		
		Bsp_brush[] _brushes = new Bsp_brush[_meshes.length];
		for (i=0; i<_meshes.length; i++)
			_brushes[i] = new Bsp_brush(_meshes[i]);
		
		JGL_3DBsp bsp1 = new JGL_3DBsp();
		root = new Bsp_node();
		
		for (i=0; i<_meshes.length; i++)
			for (j=0; j<_meshes[i].getFaces().size(); j++)
				bsp1.addFace((JGL_3DTriangle)_meshes[i].getFaces().get(j));
		
		/*Vector bsp1Faces = new Vector();
		bsp1.getFaces(bsp1Faces);
		Util4Phys.clearTriangles(bsp1Faces, _brushes);
		JGL_3DBsp bsp2 = new JGL_3DBsp();
		for (i=0; i<bsp1Faces.size(); i++)
			bsp2.addFace((JGL_3DTriangle)bsp1Faces.get(i));
		buildNodes(bsp2, root);*/
		
		buildNodes(bsp1, root);
		
		for (i=0; i<_meshes.length; i++)
			addBrush(root, _brushes[i], _meshes[i]);
		
		brushes = _brushes;
	}
	
	private static void buildNodes(JGL_3DBsp bsp1, Bsp_node bsp2) {
		
		if (bsp1.rear != null) {
			if (bsp2.rear == null)
				bsp2.rear = new Bsp_node(Bsp_node.SOLID_LEAF);
			buildNodes(bsp1.rear, bsp2.rear);
		}
		
		if (bsp1.type == JGL_3DBsp.NODE) {
			bsp2.type = Bsp_node.NODE;
			bsp2.plane = bsp1.plane;
		}
		
		if (bsp1.front != null) {
			if (bsp2.front == null)
				bsp2.front = new Bsp_node(Bsp_node.EMPTY_LEAF);
			buildNodes(bsp1.front, bsp2.front);
		}
	}
	
	private static void addBrush(Bsp_node bsp, Bsp_brush brush, JGL_3DMesh mesh) {
		
		if (bsp.type!=Bsp_node.NODE) {
			Bsp_brush[] newBrushes = new Bsp_brush[bsp.brushes.length+1];
			for (int i=0; i<bsp.brushes.length; i++)
				newBrushes[i] = bsp.brushes[i];
			newBrushes[bsp.brushes.length] = brush;
			bsp.brushes = newBrushes;
			return;
		}
		
		boolean f = false;
		boolean r = false;
		int place;
		
		for (int i=0; i<mesh.getFaces().size(); i++) {
			place = JGL_Math.plane_trianglePosition(bsp.plane, (JGL_3DTriangle)mesh.getFaces().get(i));
			if (place==-1 || place==0) r = true;
			if (place== 1 || place==0) f = true;
			if (place== 9) f = true;
			if (place==10) r = true;
		}
		
		if (r) addBrush(bsp.rear, brush, mesh);
		if (f) addBrush(bsp.front, brush, mesh);
	}
	
	
	
	/**
	 * Tests the intersection between a collision shape and the BSP, 
	 * and stores the result in the specified trace.
	 * 
	 * @param trace : describes the shape movement and stores the impact result
	 * @return if an intersection occurs with the BSP
	 */
	public boolean trace(Trace trace) {
		
		s_impact.reset(trace.cshape, trace.start, trace.end);
		
		for (int i=0; i<brushes.length; i++)
			brushes[i].collision_tested = false;
		
		root.traceThroughTree(s_impact, 0f, 1f, 
				s_impact.start.x, s_impact.start.y, s_impact.start.z, 
				s_impact.end.x, s_impact.end.y, s_impact.end.z);
		
		if (s_impact.dummy)
			trace.dummy = true;
		
		if (s_impact.isImpact())
			return trace.setNearerImpact(s_impact.correction, s_impact.fractionImpact, s_impact.fractionReal);
		
		return false;
	}
	
}
