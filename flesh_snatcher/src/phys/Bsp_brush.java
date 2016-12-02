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

import jglcore.JGL_3DBsp;
import jglcore.JGL_3DMesh;


/**
 * Class representing a collision BSP brush.
 * 
 * @author Nicolas Devere
 *
 */
public final class Bsp_brush {
	
	
	public JGL_3DBsp bsp;
	public boolean collision_tested;
	
	
	/**
	 * Constructs a  brush with a convex mesh.
	 * 
	 * @param mesh : the mesh
	 */
	public Bsp_brush(JGL_3DMesh mesh) {
		
		bsp = new JGL_3DBsp(mesh);
		collision_tested = false;
	}
	
	
	/**
	 * Traces the brush.
	 * 
	 * @param trace : the impact trace
	 */
	public void trace(Trace trace) {
		
		if (!collision_tested) {
			Tracer.trace(bsp, trace);
			collision_tested = true;
		}
	}
	
}
