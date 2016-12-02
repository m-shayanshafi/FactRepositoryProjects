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

import jglcore.JGL_3DPlane;


/**
 * Class representing a collision BSP node.
 * 
 * @author Nicolas Devere
 *
 */
public final class Bsp_node {
	
	
	/** Node instance */
	public static short NODE = 0;
	
	/** Solid leaf instance */
	public static short SOLID_LEAF = -1;
	
	/** Empty leaf instance */
	public static short EMPTY_LEAF = 1;
	
	/** Instance's type (LEAF / NODE) */
	public short type;
	
	/** Node's split plane */
	public JGL_3DPlane plane;
	
	/** Front child */
	public Bsp_node front;
	
	/** Rear child */
	public Bsp_node rear;
	
	/** Brushes in the leaf */
	public Bsp_brush[] brushes;
	
	
	
	/**
	 * Constructs an empty BSP (empty leaf).
	 */
	public Bsp_node() {
		this(EMPTY_LEAF);
	}
	
	
	/**
	 * Constructs a BSP given its type and its parent node 
	 * (or null if it's a root).
	 * 
	 * @param _type : the BSP type (SOLID_LEAF / NODE / EMPTY_LEAF)
	 */
	public Bsp_node(short _type) {
		type = _type;
		plane = null;
		rear = front = null;
		brushes = new Bsp_brush[0];
	}
	
	
	
	/**
	 * Tests the intersection with the BSP.
	 * 
	 * @param trace
	 * @param p1f
	 * @param p2f
	 * @param p1
	 * @param p2
	 */
	public void traceThroughTree( Trace trace, float p1f, float p2f, float p1x, float p1y, float p1z, float p2x, float p2y, float p2z) {
		
		float		t1, t2, offset, offset2;
		float		frac, frac2;
		float		idist;
		float 		midx, midy, midz;
		Bsp_node	node1, node2;
		float		midf;

		if (trace.fractionImpact <= p1f)
			return;		// already hit something nearer
		
		// if < 0, we are in a leaf node
		if (type != NODE) {
			for (int i=0; i<brushes.length; i++)
				brushes[i].trace(trace);
			return;
		}
		
		float nx = plane.normal.x;
		float ny = plane.normal.y;
		float nz = plane.normal.z;
		float nc = plane.constant;
		t1 = (nx * p1x) + (ny * p1y) + (nz * p1z) + nc;
		t2 = (nx * p2x) + (ny * p2y) + (nz * p2z) + nc;
		
		offset = trace.cshape.getOffset(plane.normal);

		// see which sides we need to consider
		if ( t1 >= offset + 1  && t2 >= offset +1 ) {
			front.traceThroughTree( trace, p1f, p2f, p1x, p1y, p1z, p2x, p2y, p2z );
			return;
		}
		
		plane.normal.invert();
		offset2 = trace.cshape.getOffset(plane.normal);
		plane.normal.invert();
		
		if ( t1 < -offset2 - 1  && t2 < -offset2 -1 ) {
			rear.traceThroughTree( trace, p1f, p2f, p1x, p1y, p1z, p2x, p2y, p2z );
			return;
		}

		// put the crosspoint SURFACE_CLIP_EPSILON pixels on the near side
		if ( t1 < t2 ) {
			idist = 1f/(t1-t2);
			node1 = rear;
			node2 = front;
			frac2 = (t1 + offset2 + Util4Phys.MIN_DISTANCE)*idist;
			frac = (t1 - offset2 + Util4Phys.MIN_DISTANCE)*idist;
		} else if (t1 > t2) {
			idist = 1f/(t1-t2);
			node1 = front;
			node2 = rear;
			frac2 = (t1 - offset - Util4Phys.MIN_DISTANCE)*idist;
			frac = (t1 + offset + Util4Phys.MIN_DISTANCE)*idist;
		} else {
			node1 = rear;
			node2 = front;
			frac = 1;
			frac2 = 0;
		}

		// move up to the node
		if ( frac < 0 ) {
			frac = 0;
		}
		if ( frac > 1 ) {
			frac = 1;
		}
			
		midf = p1f + (p2f - p1f)*frac;

		midx = p1x + frac*(p2x - p1x);
		midy = p1y + frac*(p2y - p1y);
		midz = p1z + frac*(p2z - p1z);

		node1.traceThroughTree( trace, p1f, midf, p1x, p1y, p1z, midx, midy, midz );


		// go past the node
		if ( frac2 < 0 ) {
			frac2 = 0;
		}
		if ( frac2 > 1 ) {
			frac2 = 1;
		}
			
		midf = p1f + (p2f - p1f)*frac2;

		midx = p1x + frac2*(p2x - p1x);
		midy = p1y + frac2*(p2y - p1y);
		midz = p1z + frac2*(p2z - p1z);

		node2.traceThroughTree( trace, midf, p2f, midx, midy, midz, p2x, p2y, p2z );
	}
	
	
}
