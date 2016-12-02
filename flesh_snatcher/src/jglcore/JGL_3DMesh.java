//	Copyright 2008 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package jglcore;

import java.util.Vector;


/**
 * Representation of a 3D basic mesh. 
 * It manages a list of faces and a list of points.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL_3DMesh implements JGL_3DStruct {
	
	private String name;
	private Vector points;
	private Vector faces;
	
	
	/**
	 * Constructs an empty mesh.
	 */
	public JGL_3DMesh() {
		name = "";
		points = new Vector();
		faces = new Vector();
	}
	
	
	/**
	 * Constructs an empty mesh.
	 */
	public JGL_3DMesh(String name) {
		this.name = name;
		points = new Vector();
		faces = new Vector();
	}
	
	
	
	/**
	 * Adds a face to the mesh.
	 * 
	 * @param face : the face to add
	 */
	public void addFace(JGL_3DTriangle face) {
		
		boolean eq;
		JGL_3DVector p;
		int i;
		
		if(!points.contains(face.point1)) {
			i = 0;
			eq = false;
			while (i<points.size() && !eq) {
				p = (JGL_3DVector)points.get(i);
				if (face.point1.eq(p)) {
					face.point1 = p;
					eq = true;
				}
				i++;
			}
			if (!eq)
				points.add(face.point1);
		}
		
		if(!points.contains(face.point2)) {
			i = 0;
			eq = false;
			while (i<points.size() && !eq) {
				p = (JGL_3DVector)points.get(i);
				if (face.point2.eq(p)) {
					face.point2 = p;
					eq = true;
				}
				i++;
			}
			if (!eq)
				points.add(face.point2);
		}
		
		if(!points.contains(face.point3)) {
			i = 0;
			eq = false;
			while (i<points.size() && !eq) {
				p = (JGL_3DVector)points.get(i);
				if (face.point3.eq(p)) {
					face.point3 = p;
					eq = true;
				}
				i++;
			}
			if (!eq)
				points.add(face.point3);
		}
		
		faces.add(face);
	}
	
	
	/**
	 * Sets the name of the mesh.
	 * 
	 * @param name : the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Returns the name of the mesh.
	 * 
	 * @return the name of the mesh
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Returns the faces list in the mesh.
	 * 
	 * @return Vector
	 */
	public Vector getFaces() {
		return faces;
	}
	
	
	/**
	 * Returns the points list in the mesh.
	 * 
	 * @return Vector
	 */
	public Vector getPoints() {
		return points;
	}
	
	
	/**
	 * Removes all the faces and points from the mesh.
	 */
	public void clear() {
		points.clear();
		faces.clear();
	}
	
	
	/**
	 * Displays the mesh.
	 */
	public void display() {
		int s = faces.size();
		for (int i=0; i<s; i++)
			((JGL_3DTriangle)faces.get(i)).display();
	}
	
	
	
	public void display(JGL_3DVector eye) {
		display();
	}
	
	
	
	public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
		display();
	}
	
}
