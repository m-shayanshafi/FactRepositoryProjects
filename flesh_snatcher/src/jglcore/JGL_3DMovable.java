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


/**
 * Representation of a movable 3D object. 
 * A <code>JGL_3DMovable</code> object can stores a <code>JGL_3DStruct</code> object 
 * and provides methods to move it (translation, rotation, scale).
 * 
 * @author Nicolas Devere
 */
public final class JGL_3DMovable implements JGL_3DStruct {
	
	
	private JGL_3DStruct struct;
	
	private JGL_3DMatrix matPos;
	private JGL_3DMatrix matInv;
	private JGL_3DMatrix matTemp;
	private JGL_3DVector pos;
	
	private JGL_3DVector view;
	private JGL_3DVector[] viewCone;
	
	private float eye_square_distance;
	
	
	
	/**
	 * Constructs a <code>JGL_3DMovable</code> object given a <code>JGL_3DStruct</code> object.
	 * 
	 * @param _struct : the 3D object implementing the <code>JGL_3DStruct</code> interface
	 */
	public JGL_3DMovable(JGL_3DStruct _struct) {
		
		setStruct(_struct);
		
		matPos = new JGL_3DMatrix();
		matInv = new JGL_3DMatrix();
		matTemp = new JGL_3DMatrix();
		identity();
		
		pos = new JGL_3DVector();
		
		view = new JGL_3DVector();
		viewCone = new JGL_3DVector[4];
		for(int i = 0; i<4; i++)
			viewCone[i] = new JGL_3DVector();
		
		eye_square_distance = 0f;
	}
	
	
	

	
	/**
	 * Stores the specified <code>JGL_3DStruct</code> object.
	 * 
	 * @param _struct : the 3D object implementing the <code>JGL_3DStruct</code> interface
	 */
	public void setStruct(JGL_3DStruct _struct) {
		struct = _struct;
	}
	
	
	/**
	 * Returns the <code>JGL_3DStruct</code> object currently stored.
	 * 
	 * @return the <code>JGL_3DStruct</code> object currently stored
	 */
	public JGL_3DStruct getStruct() {
		return struct;
	}
	
	
	/**
	 * Returns the object's current position.
	 * 
	 * @return the object's current position
	 */
	public JGL_3DVector getPosition() {
		pos.assign(matPos.m14, matPos.m24, matPos.m34);
		return pos;
	}
	
	
	/**
	 * Resets the object's situation (sets its matrix to identity).
	 */
	public void identity() {
		matPos.identity();
		matTemp.identity();
	}
	
	
	
	/**
	 * Applies a translation to the object.
	 * 
	 * @param x : the X axis translation value
	 * @param y : the Y axis translation value
	 * @param z : the Z axis translation value
	 */
	public void translate(float x, float y, float z) {
		matPos.translate(x, y, z);
	}
	
	
	
	/**
	 * Applies a rotation (in degrees) to the object 
	 * around the specified axis.
	 * 
	 * @param angle : the rotation angle (in degrees)
	 * @param x : the rotation axis's X value
	 * @param y : the rotation axis's Y value
	 * @param z : the rotation axis's Z value
	 * @param normalizedAxis : sets if the axis is normalized (witch accelerates compute), or not
	 */
	public void rotate(float angle, float x, float y, float z, boolean normalizedAxis) {
		matPos.rotate(angle, x, y, z, normalizedAxis);
	}
	
	
	/**
	 * Applies a rotation to the object with the specified Euler angles, 
	 * in the chosen order (the angles order is given by constants from 
	 * the <code>JGL</code> class. 
	 * Ex : <code>JGL.ZXY</code> means that the matrix is rotated around Z, then X, then Y).
	 * 
	 * @param angleX : the rotation angle around the X axis
	 * @param angleY : the rotation angle around the Y axis
	 * @param angleZ : the rotation angle around the Z axis
	 * @param order : the 3 axis rotations order
	 */
	public void rotate(float angleX, float angleY, float angleZ, int order) {
		matPos.rotate(angleX, angleY, angleZ, order);
	}
	
	
	/**
	 * Applies a scale transformation on 3 axis to the object.
	 * 
	 * @param x : the X axis scale value
	 * @param y : the Y axis scale value
	 * @param z : the Z axis scale value
	 */
	public void scale(float x, float y, float z) {
		matPos.scale(x, y, z);
	}
	
	
	/**
	 * Computes the square of the distance between the object 
	 * and the specified eye position.
	 * 
	 * @param eye : the eye position
	 */
	public void setEyeSquareDistance(JGL_3DVector eye) {
		float dx = matPos.m14 - eye.x;
		float dy = matPos.m24 - eye.y;
		float dz = matPos.m34 - eye.z;
		eye_square_distance = (dx * dx) + (dy * dy) + (dz * dz);
	}
	
	
	/**
	 * Returns the last computed eye square distance.
	 * 
	 * @return the last computed eye square distance
	 */
	public float getEyeSquareDistance() {
		return eye_square_distance;
	}
	
	
	
	public void display(JGL_3DVector eye) {
		JGL.pushMatrix();
		
		JGL.multiplyRight(matPos);
		
		matInv.assign(matPos);
		matInv.invert();
		
		view.x = (matInv.m11*eye.x) + (matInv.m12*eye.y) + (matInv.m13*eye.z) + matInv.m14;
		view.y = (matInv.m21*eye.x) + (matInv.m22*eye.y) + (matInv.m23*eye.z) + matInv.m24;
		view.z = (matInv.m31*eye.x) + (matInv.m32*eye.y) + (matInv.m33*eye.z) + matInv.m34;
		
		struct.display(view);
		
		JGL.popMatrix();
	}
	
	
	
	public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
		JGL.pushMatrix();
		
		JGL.multiplyRight(matPos);
		
		matInv.assign(matPos);
		matInv.invert();
		
		view.x = (matInv.m11*eye.x) + (matInv.m12*eye.y) + (matInv.m13*eye.z) + matInv.m14;
		view.y = (matInv.m21*eye.x) + (matInv.m22*eye.y) + (matInv.m23*eye.z) + matInv.m24;
		view.z = (matInv.m31*eye.x) + (matInv.m32*eye.y) + (matInv.m33*eye.z) + matInv.m34;
		
		for(int i=0; i<4; i++) {
			viewCone[i].x = (matInv.m11*cone[i].x) + (matInv.m12*cone[i].y) + (matInv.m13*cone[i].z) + matInv.m14;
			viewCone[i].y = (matInv.m21*cone[i].x) + (matInv.m22*cone[i].y) + (matInv.m23*cone[i].z) + matInv.m24;
			viewCone[i].z = (matInv.m31*cone[i].x) + (matInv.m32*cone[i].y) + (matInv.m33*cone[i].z) + matInv.m34;
		}
		
		struct.display(view, viewCone);
		
		JGL.popMatrix();
	}
	
	
	/*public static void main(String[] args) {

		float f = 0.56897f;
		JGL_3DVector r = new JGL_3DVector();
		JGL_3DVector v1 = new JGL_3DVector(6558f, 5896f, -365f);
		JGL_3DVector v2 = new JGL_3DVector(-8956f, 45f, 658f);
		JGL_3DVector v3 = new JGL_3DVector(0f, 0f, 0f);
		JGL_3DPlane pl = new JGL_3DPlane(v1, v2, v3);
		JGL_3DVector p1 = new JGL_3DVector(0f, 6000f, 55f);
		JGL_3DVector p2 = new JGL_3DVector(10f, -600f, -55f);
		
		JGL_3DVector[] cone = new JGL_3DVector[4];
		cone[0] = new JGL_3DVector(-10f, 10f, -10f);
		cone[1] = new JGL_3DVector(10f, 10f, -10f);
		cone[2] = new JGL_3DVector(10f, -10f, -10f);
		cone[3] = new JGL_3DVector(-10f, -10f, -10f);
		
		
		JGL_3DMatrix m = new JGL_3DMatrix();
		m.rotate(214.985f, 5f, 8f, -3f, false);
		
		JGL_3DMovable mov = new JGL_3DMovable(new JGL_3DMesh());
		mov.rotate(214.985f, 5f, 8f, -3f, false);
		
		long start = System.currentTimeMillis();
		
		for (int i=0; i<10000000; i++) {
			mov.display(v3, cone);
		}
		
		
		long end = System.currentTimeMillis();
		System.out.println("TIME : " + (end - start));
		
	}*/
	
}
