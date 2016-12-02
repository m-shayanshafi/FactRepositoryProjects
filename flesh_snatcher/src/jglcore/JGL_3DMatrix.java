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
 * 4x3 Matrix used for 3D Transformations. 
 * Supports rotations (quaternions and Euler angles), translations, scales, 
 * left and right multiplication, inversion.
 * 
 * @author Nicolas Devere
 */
public final class JGL_3DMatrix {
	
	public float m11, m12, m13, m14;
	public float m21, m22, m23, m24;
	public float m31, m32, m33, m34;
	
	
	/**
	 * Constructs an identity matrix.
	 *
	 */
	public JGL_3DMatrix() {
		identity();
	}
	
	
	/**
	 *  Constructs a matrix with same values than the specified matrix.
	 *  
	 * @param m : the matrix to copy
	 */
	public JGL_3DMatrix(JGL_3DMatrix m) {
		assign(m);
	}
	
	
	/**
	 * Assigns values of the specified matrix to this matrix.
	 * 
	 * @param m : the matrix to assign
	 */
	public final void assign(JGL_3DMatrix m) {
		m11 = m.m11; m12 = m.m12; m13 = m.m13; m14 = m.m14;
		m21 = m.m21; m22 = m.m22; m23 = m.m23; m24 = m.m24;
		m31 = m.m31; m32 = m.m32; m33 = m.m33; m34 = m.m34;
	}
	
	
	/**
	 * Resets the matrix to identity.
	 */
	public final void identity() {
		m11 = m22 = m33 = 1f;
		m12 = m13 = m14 = m21 = m23 = 
		m24 = m31 = m32 = m34 = 0f;
	}
	
	
	/**
	 * Applies a translation to the matrix.
	 * 
	 * @param x : the X axis translation value
	 * @param y : the Y axis translation value
	 * @param z : the Z axis translation value
	 */
	public final void translate(float x, float y, float z) {
		m14 = (m11*x) + (m12*y) + (m13*z) + m14;
		m24 = (m21*x) + (m22*y) + (m23*z) + m24;
		m34 = (m31*x) + (m32*y) + (m33*z) + m34;
	}
	
	
	/**
	 * Applies a rotation (in degrees) to the matrix 
	 * around the specified axis.
	 * 
	 * @param angle : the rotation angle (in degrees)
	 * @param x : the rotation axis's X value
	 * @param y : the rotation axis's Y value
	 * @param z : the rotation axis's Z value
	 * @param normalizedAxis : sets if the axis is normalized (witch accelerates compute), or not
	 */
	public final void rotate(float angle, float x, float y, float z, boolean normalizedAxis) {
		
		if (!normalizedAxis) {
			float n = 1f / (float)Math.sqrt( (x*x) + (y*y) + (z*z) );
			x *= n;
			y *= n;
			z *= n;
		}
		
		//	Quaternion compute
		angle *= 0.5f;
		float q = JGL_Math.sin(angle);
		
		float a = x * q;
		float b = y * q;
		float c = z * q;
		float d = JGL_Math.cos(angle);
		
		//	Rotation matrix compute
		float e = 2 * b * b; float f = 2 * c * c; float g = 2 * a * b;
		float h = 2 * c * d; float i = 2 * a * c; float j = 2 * b * d;
		float k = 2 * a * a; float l = 2 * b * c; float m = 2 * a * d;
		
		float mr0 = 1 - e - f;
		float mr1 = g - h;
		float mr2 = i + j;
		
		float mr4 = g + h;
		float mr5 = 1 - k - f;
		float mr6 = l - m;
		
		float mr8 = i - j;
		float mr9 = l + m;
		float mr10 = 1 - k - e;
		
		//	( current matrix * rotation matrix ) multiplication
		float mt0 = (m11*mr0) + (m12*mr4) + (m13*mr8);
		float mt1 = (m11*mr1) + (m12*mr5) + (m13*mr9);
		float mt2 = (m11*mr2) + (m12*mr6) + (m13*mr10);
		
		float mt4 = (m21*mr0) + (m22*mr4) + (m23*mr8);
		float mt5 = (m21*mr1) + (m22*mr5) + (m23*mr9);
		float mt6 = (m21*mr2) + (m22*mr6) + (m23*mr10);

		float mt8 = (m31*mr0) + (m32*mr4) + (m33*mr8);
		float mt9 = (m31*mr1) + (m32*mr5) + (m33*mr9);
		float mt10 = (m31*mr2) + (m32*mr6) + (m33*mr10);
		
		m11 = mt0; m12 = mt1; m13 = mt2;
		m21 = mt4; m22 = mt5; m23 = mt6;
		m31 = mt8; m32 = mt9; m33 = mt10;
	}
	
	
	
	/**
	 * 
	 * Applies a rotation to the matrix with the specified Euler angles, 
	 * in the chosen order (the angles order is given by constants from 
	 * the <code>JGL</code> class. 
	 * Ex : <code>JGL.ZXY</code> means that the matrix is rotated around Z, then X, then Y).
	 * 
	 * @param angleX : the rotation angle around the X axis
	 * @param angleY : the rotation angle around the Y axis
	 * @param angleZ : the rotation angle around the Z axis
	 * @param order : the 3 axis rotations order
	 */
	public final void rotate(float angleX, float angleY, float angleZ, int order) {
		
		float mr0, mr1, mr2, mr4, mr5, mr6, mr8, mr9, mr10;
		
		float a = JGL_Math.cos(angleX);
		float b = JGL_Math.sin(angleX);
		float c = JGL_Math.cos(angleY);
		float d = JGL_Math.sin(angleY);
		float e = JGL_Math.cos(angleZ);
		float f = JGL_Math.sin(angleZ);
		
		switch(order) {
		
			case 10 : // XYZ order
				float ad = a*d;
				float bd = b*d;
				
				mr0 = c*e;
				mr1 = -c*f;
				mr2 = -d;
				
				mr4 = -(bd*e) + (a*f);
				mr5 = (bd*f) + (a*e);
				mr6 = -b*c;
				
				mr8 = (ad*e) + (b*f);
				mr9 = -(ad*f) + (b*e);
				mr10 = a*c;
				
				break;
				
			case 11 : // XZY order
				float af = a*f;
				float bf = b*f;
				
				mr0 = c*e;
				mr1 = -f;
				mr2 = -d*e;
				
				mr4 = (af*c) - (b*d);
				mr5 = a*e;
				mr6 = -(af*d) - (b*c);
				
				mr8 = (bf*c) + (a*d);
				mr9 = b*e;
				mr10 = -(bf*d) + (a*c);
				
				break;
				
			case 12 : // YXZ order
				float bc = b*c;
				float db = d*b;
				
				mr0 = (c*e) - (db*f);
				mr1 = -(c*f) - (db*e);
				mr2 = -d*a;
				
				mr4 = a*f;
				mr5 = a*e;
				mr6 = -b;
				
				mr8 = (d*e) + (bc*f);
				mr9 = -(d*f) + (bc*e);
				mr10 = c*a;
				
				break;
				
			case 13 : // YZX order
				float cf = c*f;
				float df = d*f;
				
				mr0 = c*e;
				mr1 = -(cf*a) - (d*b);
				mr2 = (cf*b) - (d*a);
				
				mr4 = f;
				mr5 = a*e;
				mr6 = -b*e;
				
				mr8 = d*e;
				mr9 = -(df*a) + (b*c);
				mr10 = (df*b) + (a*c);
				
				break;
				
			case 14 : // ZXY order
				float eb = e*b;
				float fb = f*b;
				
				mr0 = (c*e) + (fb*d);
				mr1 = -f*a;
				mr2 = -(d*e) + (fb*c);
				
				mr4 = (c*f) - (eb*d);
				mr5 = e*a;
				mr6 = -(d*f) - (eb*c);
				
				mr8 = a*d;
				mr9 = b;
				mr10 = a*c;
				
				break;
				
			case 15 : // ZYX order
				float ed = e*d;
				float fd = f*d;
				
				mr0 = e*c;
				mr1 = -(f*a) - (ed*b);
				mr2 = (f*b) - (ed*a);
				
				mr4 = f*c;
				mr5 = (e*a) - (fd*b);
				mr6 = -(e*b) - (fd*a);
				
				mr8 = d;
				mr9 = c*b;
				mr10 = c*a;
				
				break;
				
			default : return;
		}
		
		//	( current matrix * rotation matrix ) multiplication
		float mt0 = (m11*mr0) + (m12*mr4) + (m13*mr8);
		float mt1 = (m11*mr1) + (m12*mr5) + (m13*mr9);
		float mt2 = (m11*mr2) + (m12*mr6) + (m13*mr10);
		
		float mt4 = (m21*mr0) + (m22*mr4) + (m23*mr8);
		float mt5 = (m21*mr1) + (m22*mr5) + (m23*mr9);
		float mt6 = (m21*mr2) + (m22*mr6) + (m23*mr10);

		float mt8 = (m31*mr0) + (m32*mr4) + (m33*mr8);
		float mt9 = (m31*mr1) + (m32*mr5) + (m33*mr9);
		float mt10 = (m31*mr2) + (m32*mr6) + (m33*mr10);
		
		m11 = mt0; m12 = mt1; m13 = mt2;
		m21 = mt4; m22 = mt5; m23 = mt6;
		m31 = mt8; m32 = mt9; m33 = mt10;
	}
	
	
	
	/**
	 * Applies a scale transformation on 3 axis to the matrix.
	 * 
	 * @param x : the X axis scale value
	 * @param y : the Y axis scale value
	 * @param z : the Z axis scale value
	 */
	public final void scale(float x, float y, float z) {
		
		m11 *= x;
		m21 *= x;
		m31 *= x;
		
		m12 *= y;
		m22 *= y;
		m32 *= y;
		
		m13 *= z;
		m23 *= z;
		m33 *= z;
	}
	
	
	
	/**
	 * Right-multiplies this matrix by the specified matrix.
	 * 
	 * @param m : the matrix to multiply
	 */
	public final void multiplyRight(JGL_3DMatrix m) {
		
		float mt0 = (m11*m.m11) + (m12*m.m21) + (m13*m.m31);
		float mt1 = (m11*m.m12) + (m12*m.m22) + (m13*m.m32);
		float mt2 = (m11*m.m13) + (m12*m.m23) + (m13*m.m33);
		float mt3 = (m11*m.m14) + (m12*m.m24) + (m13*m.m34) + m14;
		
		float mt4 = (m21*m.m11) + (m22*m.m21) + (m23*m.m31);
		float mt5 = (m21*m.m12) + (m22*m.m22) + (m23*m.m32);
		float mt6 = (m21*m.m13) + (m22*m.m23) + (m23*m.m33);
		float mt7 = (m21*m.m14) + (m22*m.m24) + (m23*m.m34) + m24;

		float mt8 = (m31*m.m11) + (m32*m.m21) + (m33*m.m31);
		float mt9 = (m31*m.m12) + (m32*m.m22) + (m33*m.m32);
		float mt10 = (m31*m.m13) + (m32*m.m23) + (m33*m.m33);
		float mt11 = (m31*m.m14) + (m32*m.m24) + (m33*m.m34) + m34;
		
		m11 = mt0; m12 = mt1; m13 = mt2; m14 = mt3;
		m21 = mt4; m22 = mt5; m23 = mt6; m24 = mt7;
		m31 = mt8; m32 = mt9; m33 = mt10; m34 = mt11;
	}
	
	
	/**
	 * Left-multiplies this matrix by the specified matrix.
	 * 
	 * @param m : the matrix to multiply
	 */
	public final void multiplyLeft(JGL_3DMatrix m) {
		
		float mt0 = (m.m11*m11) + (m.m12*m21) + (m.m13*m31);
		float mt1 = (m.m11*m12) + (m.m12*m22) + (m.m13*m32);
		float mt2 = (m.m11*m13) + (m.m12*m23) + (m.m13*m33);
		float mt3 = (m.m11*m14) + (m.m12*m24) + (m.m13*m34) + m.m14;
		
		float mt4 = (m.m21*m11) + (m.m22*m21) + (m.m23*m31);
		float mt5 = (m.m21*m12) + (m.m22*m22) + (m.m23*m32);
		float mt6 = (m.m21*m13) + (m.m22*m23) + (m.m23*m33);
		float mt7 = (m.m21*m14) + (m.m22*m24) + (m.m23*m34) + m.m24;

		float mt8 = (m.m31*m11) + (m.m32*m21) + (m.m33*m31);
		float mt9 = (m.m31*m12) + (m.m32*m22) + (m.m33*m32);
		float mt10 = (m.m31*m13) + (m.m32*m23) + (m.m33*m33);
		float mt11 = (m.m31*m14) + (m.m32*m24) + (m.m33*m34) + m.m34;
		
		m11 = mt0; m12 = mt1; m13 = mt2; m14 = mt3;
		m21 = mt4; m22 = mt5; m23 = mt6; m24 = mt7;
		m31 = mt8; m32 = mt9; m33 = mt10; m34 = mt11;
	}
	
	
	
	/**
	 * Inverts the matrix.
	 */
	public final void invert() {
		
		float a = m22 * m33;	float b = m23 * m32;
		float c = m21 * m33;	float d = m23 * m31;
		float e = m21 * m32;	float f = m22 * m31;
		float g = m12 * m33;	float h = m13 * m32;
		float i = m11 * m33;	float j = m13 * m31;
		float k = m11 * m32;	float l = m12 * m31;
		float m = m12 * m23;	float n = m13 * m22;
		float o = m11 * m23;	float p = m13 * m21;
		float q = m11 * m22;	float r = m12 * m21;
		
		float mt14 = (m14*b) + (m24*g) + (m34*n) - (m*m34) - (a*m14) - (h*m24);
		float mt24 = (o*m34) + (c*m14) + (j*m24) - (m14*d) - (m24*i) - (m34*p);
		float mt34 = (m14*f) + (m24*k) + (m34*r) - (q*m34) - (e*m14) - (l*m24);
		
		m11 = a - b;
		m21 = d - c;
		m31 = e - f;
		
		m12 = h - g;
		m22 = i - j;
		m32 = l - k;
		
		m13 = m - n;
		m23 = p - o;
		m33 = q - r;
		
		m14 = mt14;
		m24 = mt24;
		m34 = mt34;
	}
	
}
