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

package input;

import java.util.Vector;
import jglcore.JGL_3DMesh;

/**
 * Structure containing 3D data loaded from a file :<b>
 * - a 3D mesh<b>
 * - an animated skeletons list<b>
 * - a mapping list to link the mesh vertexes to skeletons bones (if skeletons are found)<b>
 * 
 * @author Nicolas Devere
 *
 */
public class Data3D {
	
	public JGL_3DMesh mesh;
	public Vector subMeshes;
	public Vector map_points_bones;
	
	
	
	/**
	 * Constructs a new data (sets features to null)
	 *
	 */
	public Data3D() {
		init();
	}
	
	/**
	 * Resets the data (sets features to null)
	 *
	 */
	public void init() {
		mesh = null;
		subMeshes = null;
		map_points_bones = null;
	}
	
	
}
