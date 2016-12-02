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
 * Representation of a vector in 3D space.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL_3DVector {
	
	/** The X value */
	public float x;
	
	/** The Y value */
	public float y;
	
	/** The Z value */
	public float z;
	
	
	/**
	 * Constructs a neutral vector.
	 */
	public JGL_3DVector() {
		x = 0f;
		y = 0f;
		z = 0f;
	}
	
	
	/**
	 * Constructs a vector with same values than the specified vector.
	 * 
	 * @param arg : the vector to assign
	 */
	public JGL_3DVector(JGL_3DVector arg) {
		assign(arg);
	}
	
	
	/**
	 * Constructs a vector given its 3 components.
	 * 
	 * @param xx : the X value
	 * @param yy : the Y value
	 * @param zz : the Z value
	 */
	public JGL_3DVector(float xx, float yy, float zz) {
		x = xx;
		y = yy;
		z = zz;
	}
	
	
	/**
	 * Assigns values of the specified vector to this vector.
	 * 
	 * @param v : the vector to assign
	 */
	public void assign(JGL_3DVector v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	
	/**
	 * Assigns the specified values to this vector.
	 * 
	 * @param xx : the X coordinate
	 * @param yy : the Y coordinate
	 * @param zz : the Z coordinate
	 */
	public void assign(float xx, float yy, float zz) {
		x = xx;
		y = yy;
		z = zz;
	}
	
	
	/**
	 * Returns if the specified vector's components are equals to this vector's.
	 * 
	 * @param v : the vector to compare
	 * @return true if equality, false otherwise
	 */
	public boolean eq(JGL_3DVector v)
	{
		return 	Math.abs(x - v.x) < JGL_Math.EPSILON &&
				Math.abs(y - v.y) < JGL_Math.EPSILON &&
				Math.abs(z - v.z) < JGL_Math.EPSILON;
	}
	
	
	/**
	 * Returns the vector's norm.
	 * 
	 * @return the vector's norm
	 */
	public float norm() {
		return (float)Math.sqrt( (x*x) + (y*y) + (z*z) );
	}
	
	
	/**
	 * Returns the square norm of the vector.
	 * 
	 * @return the vector's square norm
	 */
	public float norm2() {
		return (x*x) + (y*y) + (z*z);
	}
	
	
	/**
	 * Normalizes the vector.
	 */
	public void normalize() {
		float n = 1f / (float)Math.sqrt( (x*x) + (y*y) + (z*z) );
		x *= n;
		y *= n;
		z *= n;
	}
	
	
	/**
	 * Inverts the vector.
	 */
	public void invert() {
		x = -x;
		y = -y;
		z = -z;
	}
	
}