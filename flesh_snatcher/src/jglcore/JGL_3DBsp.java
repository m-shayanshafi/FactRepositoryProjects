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
 * Dynamic BSP tree. Provides, besides display functionalities, the possibility to add 
 * <code>JGL_3DStruct</code> objects (these ones are removed after each BSP display).<br>
 * <code>JGL_3DBsp</code> class also provides a mechanism to modify its content 
 * by cleaning/adding faces (useful to emulate a z-buffer or to manage animated meshes). 
 * 
 * @author Nicolas Devere
 */
public final class JGL_3DBsp implements JGL_3DStruct {
	
	/** Node instance */
	public static short NODE = 0;
	
	/** Solid leaf instance */
	public static short SOLID_LEAF = -1;
	
	/** Empty leaf instance */
	public static short EMPTY_LEAF = 1;
	
	/** Instance's type (SOLID_LEAF / NODE / EMPTY_LEAF) */
	public short type;
	
	/** Node's split plane */
	public JGL_3DPlane plane;
	
	/** Front child */
	public JGL_3DBsp front;
	
	/** Rear child */
	public JGL_3DBsp rear;
	
	
	/**
	 * Node's faces list.<br>
	 * WARNING : its size is given by the <code>facesSize</code> attribute, 
	 * not by the <code>faces.size()</code> method !!
	 */
	public Vector faces;
	
	/** Faces number in the node */
	public int facesSize;
	
	/**
	 * Movable objects's sorted list.<br>
	 * WARNING : movable objects are removed after each display. 
	 * An insertion over each frame is needed to keep a constant render.
	 */
	public Vector structs;
	
	
	//Optimization attributes
	private JGL_3DTriangle[] split;
	private int realSize;
	
	
	/**
	 * Constructs an empty BSP (empty leaf).
	 */
	public JGL_3DBsp() {
		this(EMPTY_LEAF);
	}
	
	
	/**
	 * Constructs a BSP containing the specified mesh.
	 * 
	 * @param mesh : the mesh to put in the BSP
	 */
	public JGL_3DBsp(JGL_3DMesh mesh) {
		this(EMPTY_LEAF);
		addMesh(mesh);
	}
	
	
	/**
	 * Constructs a BSP given its type and its parent node 
	 * (or null if it's a root).
	 * 
	 * @param _type : the BSP type (SOLID_LEAF / NODE / EMPTY_LEAF)
	 */
	private JGL_3DBsp(short _type) {
		type = _type;
		faces = null;
		structs = new Vector();
		facesSize = realSize = 0;
		plane = null;
		rear = front = null;
		split = null;
	}
	
	
	
	/**
	 * Adds a mesh in the BSP.
	 * 
	 * @param mesh : the mesh to add
	 */
	public void addMesh(JGL_3DMesh mesh) {
		Vector v = mesh.getFaces();
		for (int i=0; i<v.size(); i++)
			addFace((JGL_3DTriangle)v.get(i));
	}
	
	
	
	/**
	 * Adds a face in the BSP.
	 * 
	 * @param face : the face to add
	 */
	public void addFace(JGL_3DTriangle face) {
		
		// Transformation d'une feuille en node
		if (type!=NODE) {
			
			type = NODE;
			
			// Nouvelle feuille
			if (plane == null) {
				plane = new JGL_3DPlane(face.point1, face.point2, face.point3);
				faces = new Vector();
				faces.add(new JGL_3DTriangle(	new JGL_3DVector(face.point1.x, face.point1.y, face.point1.z), 
												new JGL_3DVector(face.point2.x, face.point2.y, face.point2.z), 
												new JGL_3DVector(face.point3.x, face.point3.y, face.point3.z), 
												face.color));
				facesSize++;
				realSize++;
				
				split = new JGL_3DTriangle[3];
				for (int i=0; i<3; i++)
					split[i] = new JGL_3DTriangle(new JGL_3DVector(), new JGL_3DVector(), new JGL_3DVector());
				
				front = new JGL_3DBsp(EMPTY_LEAF);
				rear  = new JGL_3DBsp(SOLID_LEAF);
				
				return;
			}
			// Feuille deja existante
			plane.assign(face.point1, face.point2, face.point3);
			((JGL_3DTriangle)faces.get(0)).assign(face.point1, face.point2, face.point3);
			((JGL_3DTriangle)faces.get(0)).color = face.color;
			facesSize = 1;
			return;
		}
		
		int place = JGL_Math.plane_trianglePosition(plane, face);
		
		if (place == 10) {
			if (facesSize < realSize) {
				((JGL_3DTriangle)faces.get(facesSize)).assign(face.point1, face.point2, face.point3);
				((JGL_3DTriangle)faces.get(facesSize)).color = face.color;
				facesSize++;
				return;
			}
			faces.add(	new JGL_3DTriangle(	new JGL_3DVector(face.point1.x, face.point1.y, face.point1.z), 
						new JGL_3DVector(face.point2.x, face.point2.y, face.point2.z), 
						new JGL_3DVector(face.point3.x, face.point3.y, face.point3.z), 
						face.color));
			facesSize++;
			realSize++;
			return;
		}
		
		if (place == -1) {
			if (rear == null)
				rear = new JGL_3DBsp(SOLID_LEAF);
			rear.addFace(face);
			return;
		}
		
		if (place == 1 || place == 9) {
			if (front == null)
				front = new JGL_3DBsp(EMPTY_LEAF);
			front.addFace(face);
			return;
		}
		
		if (place == 0) {
			int split_place;
			int nb = JGL_Math.triangle_split(face, plane, split[0], split[1], split[2]);
			
			for (int i=0; i<nb; i++) {
				split_place = JGL_Math.plane_trianglePosition(plane, split[i]);
				if (split_place == -1) {
					if (rear == null)
						rear = new JGL_3DBsp(SOLID_LEAF);
					rear.addFace(split[i]);
				}
				else if (split_place == 1) {
					if (front == null)
						front = new JGL_3DBsp(EMPTY_LEAF);
					front.addFace(split[i]);
				}
			}
		}
	}
	
	
	
	/**
	 * Clears the BSP (resets all nodes as leaves).
	 */
	public void clear() {
		clear(EMPTY_LEAF);
	}
	private void clear(short _type) {
		if(rear != null)
			rear.clear(SOLID_LEAF);
		
		type = _type;
		facesSize = 0;
		
		if(front != null)
			front.clear(EMPTY_LEAF);
	}
	
	
	
	/**
	 * Displays the BSP according to the specified eye position.
	 * 
	 * @param eye : the eye position
	 */
	public void display(JGL_3DVector eye) {
		
		if (type!=NODE) {
			if(!structs.isEmpty()) {
				for(int i=0; i<structs.size(); i++)
					((JGL_3DStruct)structs.get(i)).display(eye);
				structs.clear();
			}
			return;
		}

		if(plane.distance(eye) > 0f) {
			if(rear != null)
				rear.display(eye);
			
			for(int i = 0; i<facesSize; i++)
				((JGL_3DTriangle)faces.get(i)).display();
			
			if(front != null)
				front.display(eye);
		}
		
		else {
			if(front != null)
				front.display(eye);
			
			for(int i = 0; i<facesSize; i++)
				((JGL_3DTriangle)faces.get(i)).display();
			
			if(rear != null)
				rear.display(eye);
		}
	}
	
	
	/**
	 * Displays the BSP according the specified vision cone 
	 * (an eye position and 4 points to make a 4 sides pyramid).
	 * 
	 * @param eye : the eye position (cone base)
	 * @param cone : the vision cone represented by 4 points
	 */
	public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
		
		if (type!=NODE) {
			if(!structs.isEmpty()) {
				for(int i=0; i<structs.size(); i++)
					((JGL_3DStruct)structs.get(i)).display(eye, cone);
				structs.clear();
			}
			return;
		}
		
		if(plane.before(eye)) {
			
			if(	plane.behind(cone[0]) ||
				plane.behind(cone[1]) ||
				plane.behind(cone[2]) ||
				plane.behind(cone[3])) {
				if(rear != null) rear.display(eye, cone);
				
				for(int i = 0; i<facesSize; i++)
					((JGL_3DTriangle)faces.get(i)).display();
				
				if(front != null) front.display(eye, cone);
			}
			else {
				if(front != null) front.display(eye, cone);
			}
			
		}
		else {
			
			if(	plane.before(cone[0]) ||
				plane.before(cone[1]) ||
				plane.before(cone[2]) ||
				plane.before(cone[3])) {
				if(front != null) front.display(eye, cone);
				if(rear != null) rear.display(eye, cone);
			}
			else {
				if(rear != null) rear.display(eye, cone);
			}
		}
	}
	
	
	
	public void getFaces(Vector result) {
		
		if (type==NODE)
			result.addAll(faces);
		
		if(rear != null) rear.getFaces(result);
		if(front != null) front.getFaces(result);
	}
	
	
	
}
