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

import jglcore.JGL_3DVector;
import phys.Shape_aabb;
import phys.Shape_sphere;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;



public class DisplayNode {
	
	private static Shape_sphere eyeSphere = new Shape_sphere(new JGL_3DVector(), 0f);
	
	private String name;
	private Node node;
	private Shape_aabb[] viewShapes;
	
	
	/**
	 * Constructs a BSP area.
	 * 
	 * @param _meshes : the 3D meshes
	 * @param _pos : the area position
	 * @param _aabb : the area AABB
	 * @param map : the height-map land which contains the BSP area
	 */
	public DisplayNode(String _name, Node _node, Vector _viewShapes) {
		
		int i;
		
		name = _name;
		node = _node;
		
		viewShapes = new Shape_aabb[_viewShapes.size()];
		for (i=0; i<_viewShapes.size(); i++)
			viewShapes[i] = (Shape_aabb)_viewShapes.get(i);
	}
	
	
	public String getName() {
		return name;
	}
	
	public Node getNode() {
		return node;
	}
	
	
	public Shape_aabb[] getViewShapes() {
		return viewShapes;
	}
	
	
	
	public boolean isIn(JGL_3DVector arg) {
		if (viewShapes.length==0)
			return true;
		
		eyeSphere.getPosition().assign(arg);
		for (int i=0; i<viewShapes.length; i++)
			if (viewShapes[i].isIn(eyeSphere))
				return true;
		
		return false;
	}
	
	
	
	/**
	 * 
	 * @param tpf
	 */
	public void render(JGL_3DVector eye) {
		if (isIn(eye))
			DisplaySystem.getDisplaySystem().getRenderer().draw(node);
	}
	
	
	
}
