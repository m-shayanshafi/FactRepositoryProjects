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

import jglcore.JGL_3DBsp;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DVector;
import phys.Shape_sphere;
import phys.Shape_aabb;
import phys.Trace;
import phys.Tracer;
import phys.Util4Phys;


/**
 * Land providing height-map collision management.
 * 
 * @author Nicolas Devere
 *
 */
public final class CollisionHeightMap implements CollisionNode {
	
	
	
	/**
	 * Represents a heightmap cell. It's a (x, z) square made by 2 triangles. 
	 * It also stores a BSP storing the 2 triangles made for collision tracing.
	 * 
	 * @author Nicolas Devere
	 *
	 */
	final class HeightMapCell {
		
		public boolean dummy;
		public JGL_3DBsp bsp;
		
		/**
		 * Constructs a cell with 2 triangles.
		 * 
		 * @param _t1 : first triangle
		 * @param _t2 : second triangle
		 */
		public HeightMapCell(JGL_3DTriangle _t1, JGL_3DTriangle _t2) {
			
			dummy = false;
			
			if (_t1.point1.y==Float.NEGATIVE_INFINITY) dummy = true;
			if (_t1.point2.y==Float.NEGATIVE_INFINITY) dummy = true;
			if (_t1.point3.y==Float.NEGATIVE_INFINITY) dummy = true;
			if (_t2.point1.y==Float.NEGATIVE_INFINITY) dummy = true;
			if (_t2.point2.y==Float.NEGATIVE_INFINITY) dummy = true;
			if (_t2.point3.y==Float.NEGATIVE_INFINITY) dummy = true;
			
			if (!dummy) {
				bsp = new JGL_3DBsp();
				bsp.addFace(_t1);
				bsp.addFace(_t2);
			}
		}
	}
	
	
	
	private static Shape_sphere pPoint = new Shape_sphere(new JGL_3DVector(), 0f);
	private static Trace testTrace = new Trace();
	
	private String name;
	private float xp, yp, zp;
	private int wi;
	private int de;
	private float ga;
	private float ga_inv;
	
	private HeightMapCell[][] map;
	
	private Shape_aabb aabb;
	
	
	/**
	 * Constructs a height-map.
	 * 
	 * @param width
	 * @param depth
	 * @param gap
	 * @param heights
	 * @param h1
	 * @param color1
	 * @param h2
	 * @param color2
	 * @param light
	 * @param shininess
	 */
	public CollisionHeightMap(String id, float x, float y, float z, int width, int depth, float gap, float[][] heights) {
		
		name = id;
		
		xp = x;
		yp = y;
		zp = z;
		wi = width - 1;
		de = depth - 1;
		ga = gap;
		ga_inv = 1f / ga;
		
		map = new HeightMapCell[wi][de];
		
		JGL_3DMesh mesh = new JGL_3DMesh();
		
		JGL_3DTriangle t1, t2;
		
		for (int j=de-1; j>=0; j--) {
			for (int i=0; i<wi; i++) {
				t1 = new JGL_3DTriangle(new JGL_3DVector((i*ga)+xp, heights[i][j+1]+yp, (-(j+1)*ga)+zp), 
										new JGL_3DVector((i*ga)+xp, heights[i][j]+yp, (-j*ga)+zp), 
										new JGL_3DVector(((i+1)*ga)+xp, heights[i+1][j]+yp, (-j*ga)+zp));
				
				t2 = new JGL_3DTriangle(new JGL_3DVector(((i+1)*ga)+xp, heights[i+1][j]+yp, (-j*ga)+zp), 
										new JGL_3DVector(((i+1)*ga)+xp, heights[i+1][j+1]+yp, (-(j+1)*ga)+zp), 
										new JGL_3DVector((i*ga)+xp, heights[i][j+1]+yp, (-(j+1)*ga)+zp));
				
				map[i][j] = new HeightMapCell(t1, t2);
				
				if (!map[i][j].dummy) {
					mesh.addFace(t1);
					mesh.addFace(t2);
				}
			}
		}
		
		if (mesh.getFaces().size()>0)
			aabb = Util4Phys.getAABB(mesh, 2f);
		else
			aabb = new Shape_aabb(new JGL_3DVector(), new JGL_3DVector(), new JGL_3DVector());
		
		mesh.clear();
		mesh = null;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public int getWidth() { return wi; }
	public int getDepth() { return de; }
	public float getGap() { return ga; }
	
	
	/**
	 * Stores the specified BSP area in the map.
	 * @param area
	 */
	/*public void addBsp(CollisionBSP bsp) {
		
		int l = bsps.length;
		CollisionBSP[] tmp = new CollisionBSP[l+1];
		
		for (int i=0; i<l; i++)
			tmp[i] = bsps[i];
		
		tmp[l] = bsp;
		bsps = tmp;
	}*/
	
	
	/**
	 * Searches and stores collision on the height-map.
	 * 
	 * @param trace : the trace to collide
	 * @return if a collision occurs
	 */
	public boolean collide(Trace trace) {
		
		if (trace.dummy)
			return false;
		
		testTrace.reset(trace.cshape, trace.start, trace.end);
		if ( (!aabb.isIn(trace.cshape)) && (!aabb.trace(testTrace)) )
			return false;
		
		boolean result = false;
		float xMin, xMax;
		float zMin, zMax;
		float xf, zf;
		int xi, zi;
		float offset = trace.cshape.getOffset(Util4Phys.up.normal);
		
		testTrace.reset(pPoint, trace.start, trace.end);
		
		JGL_3DVector pos1 = testTrace.start;
		JGL_3DVector pos2 = testTrace.end;
		pos1.y -= offset;
		pos2.y -= offset;
		
		if (pos1.x<=pos2.x) {
			xMin = pos1.x - trace.cshape.getOffset(Util4Phys.right.normal);
			xMax = pos2.x + trace.cshape.getOffset(Util4Phys.left.normal);
		}
		else {
			xMin = pos2.x - trace.cshape.getOffset(Util4Phys.right.normal);
			xMax = pos1.x + trace.cshape.getOffset(Util4Phys.left.normal);
		}
		
		if (pos1.z>=pos2.z) {
			zMin = pos1.z + trace.cshape.getOffset(Util4Phys.near.normal);
			zMax = pos2.z - trace.cshape.getOffset(Util4Phys.far.normal);
		}
		else {
			zMin = pos2.z + trace.cshape.getOffset(Util4Phys.near.normal);
			zMax = pos1.z - trace.cshape.getOffset(Util4Phys.far.normal);
		}
		
		int xi1 = (int)Math.floor((xMin - xp) * ga_inv); if (xi1<0) xi1 = 0;
		int zi1 = -(int)Math.ceil((zMin - zp) * ga_inv); if (zi1<0) zi1 = 0;
		
		int xi2 = (int)Math.floor((xMax - xp) * ga_inv); if (xi2>=wi) xi2 = wi - 1;
		int zi2 = -(int)Math.ceil((zMax - zp) * ga_inv); if (zi2>=de) zi2 = de - 1;
		
		for (xi=xi1; xi<=xi2; xi++)
			for (zi=zi1; zi<=zi2; zi++)
				if (!map[xi][zi].dummy)
					if (Tracer.trace(map[xi][zi].bsp, testTrace)) {
						xf = (testTrace.start.x + (testTrace.segment.x * testTrace.fractionReal));
						zf = (testTrace.start.z + (testTrace.segment.z * testTrace.fractionReal));
						if ( (xf>=xMin && xf<xMax && zf<=zMin && zf>zMax) )
							result = true;
					}
		
		if (result)
			trace.setNearerImpact(testTrace.correction, testTrace.fractionImpact, testTrace.fractionReal);
		else {
			int xv = (int)Math.floor((testTrace.end.x - xp) * ga_inv); if (xv<0) return result; if (xv>=wi) return result;
			int zv = -(int)Math.ceil((testTrace.end.z - zp) * ga_inv); if (zv<0) return result; if (zv>=de) return result;
			if (!map[xv][zv].dummy)
				if (!isAbove(map[xv][zv].bsp, testTrace.end)) {
					trace.setImpact(Util4Phys.up, 0f, 0f);
					result = true;
				}
		}
		
		return result;
	}
	
	
	/**
	 * Returns if the specified segment  
	 * intersects the height-map.
	 * 
	 * @param p1 : segment start point
	 * @param p2 : segment end point
	 * @return if the segment intersects the map
	 */
	public boolean intersect(JGL_3DVector p1, JGL_3DVector p2) {
		
		testTrace.reset(pPoint, p1, p2);
		aabb.trace(testTrace);
		if (!testTrace.isImpact() && !testTrace.dummy)
			return false;
		
		int xMin, xMax;
		int zMin, zMax;
		int xi, zi;
		
		if (p1.x<=p2.x) {
			xMin = (int)Math.floor((p1.x - xp) * ga_inv);
			xMax = (int)Math.floor((p2.x - xp) * ga_inv);
		}
		else {
			xMin = (int)Math.floor((p2.x - xp) * ga_inv);
			xMax = (int)Math.floor((p1.x - xp) * ga_inv);
		}
		
		if (p1.z>=p2.z) {
			zMin = -(int)Math.ceil((p1.z - zp) * ga_inv);
			zMax = -(int)Math.ceil((p2.z - zp) * ga_inv);
		}
		else {
			zMin = -(int)Math.ceil((p2.z - zp) * ga_inv);
			zMax = -(int)Math.ceil((p1.z - zp) * ga_inv);
		}
		
		if (xMin<0) xMin = 0; if (xMax>=wi) xMax = wi - 1;
		if (zMin<0) zMin = 0; if (zMax>=de) zMax = de - 1;
		
		for (xi=xMin; xi<=xMax; xi++)
			for (zi=zMin; zi<=zMax; zi++) {
				testTrace.reset(pPoint, p1, p2);
				if (!map[xi][zi].dummy)
					if (Tracer.trace(map[xi][zi].bsp, testTrace)) {
						if ((int)Math.floor( ((testTrace.start.x + (testTrace.segment.x * testTrace.fractionImpact)) - xp) * ga_inv )==xi && 
							-(int)Math.ceil( ((testTrace.start.z + (testTrace.segment.z * testTrace.fractionImpact)) - zp) * ga_inv )==zi ) {
							return true;
						}
					}
			}
		
		return false;
	}
	
	
	private static boolean isAbove(JGL_3DBsp cell, JGL_3DVector point) {
		if (cell.type==JGL_3DBsp.EMPTY_LEAF)
			return true;
		if (cell.type==JGL_3DBsp.SOLID_LEAF)
			return false;
		
		if (cell.plane.distance(point)<=0)
			return isAbove(cell.rear, point);
		else
			return isAbove(cell.front, point);
	}
	
}
