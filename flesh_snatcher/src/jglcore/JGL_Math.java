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

import java.util.Random;


/**
 * Static class gathering mathematics functions (trigonometry, linear algebra).
 * 
 * @author Nicolas Devere
 */
public final class JGL_Math {
	
	/** Small value used for float comparisons */
	public static float EPSILON = 0.0005f;
	
	/** Degrees to radians conversion factor */
	public static float DEGREES_TO_RADIANS = (float)(Math.PI / 180d);
	
	/** Radians to degrees conversion factor */
	public static float RADIANS_TO_DEGREES = (float)(180d / Math.PI);
	
	
	
	private static float sine[] = new float[3601];
	private static float cosine[] = new float[3601];
	private static JGL_3DVector vect1 = new JGL_3DVector();
	private static JGL_3DVector vect2 = new JGL_3DVector();
	private static JGL_3DVector vect3 = new JGL_3DVector();
	private static JGL_3DVector vect4 = new JGL_3DVector();
	
	private static int nbRand = 253;
	private static float[] rand = new float[nbRand];
	private static int ir;
	
	
	static {
		double conv = DEGREES_TO_RADIANS * 0.1d;
		double radian = 0d;
		double i = 0d;
		while(i<3601d) {
			radian = i*conv;
			sine[(int)i] = (float)Math.sin(radian);
			cosine[(int)i] = (float)Math.cos(radian);
			i++;
		}
		
		Random r = new Random(nbRand);
		for (int j=0; j<nbRand; j++)
			rand[j] = r.nextFloat();
		ir = 0;
	}
	
	
	
	/////////////////////////
	////  Trigonometry  ////
	/////////////////////////
	
	/**
	 * Returns the sine of the specified angle. 
	 * This one is in degrees and must be included between -360 and +360.
	 * 
	 * @param angle : the angle in degrees
	 * @return the sine
	 */
	public static final float sin(float angle) {
		if(angle<0f)
			return -sine[(int)-(angle*10f)];
		
		return sine[(int)(angle*10f)];		
	}
	
	/**
	 * Returns the cosine of the specified angle. 
	 * This one is in degrees and must be included between -360 and +360.
	 * 
	 * @param angle : the angle in degrees
	 * @return the cosine
	 */
	public static final float cos(float angle) {
		if(angle<0f)
			angle = -angle;
		
		return cosine[(int)(angle*10f)];

	}
	
	
	//////////////////
	////  RANDOM  ////
	//////////////////
	
	/**
	 * Returns a float random value in the [0, 1] range.
	 * 
	 * @return the float random value
	 */
	public static final float rnd() {
		
		ir++;
		if (ir>=nbRand)
			ir = 0;
		return rand[ir];
	}
	
	
	
	//////////////////////////
	////  Linear algebra  ////
	//////////////////////////
	
	
	/**
	 * Computes the ( [v1] + [v2] ) addition.
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @param result : the addition result
	 */
	public static final void vector_add(JGL_3DVector v1, JGL_3DVector v2, JGL_3DVector result) {
		result.x = v1.x + v2.x;
		result.y = v1.y + v2.y;
		result.z = v1.z + v2.z;
	}
	
	
	
	/**
	 * Computes the ( [v1] - [v2] ) subtraction.
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @param result : the subtraction result
	 */
	public static final void vector_subtract(JGL_3DVector v1, JGL_3DVector v2, JGL_3DVector result) {
		result.x = v1.x - v2.x;
		result.y = v1.y - v2.y;
		result.z = v1.z - v2.z;
	}
	
	
	
	
	/**
	 * Computes the ( [v1] * [factor] ) multiplication.
	 * 
	 * @param v : the vector
	 * @param factor : the factor
	 * @param result : the multiplication result
	 */
	public static final void vector_multiply(JGL_3DVector v, float factor, JGL_3DVector result) {
		result.x = v.x * factor;
		result.y = v.y * factor;
		result.z = v.z * factor;
	}
	
	
	
	/**
	 * Returns the ( [v1] . [v2] ) dot product .
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @return the dot product
	 */
	public static final float vector_dotProduct(JGL_3DVector v1, JGL_3DVector v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}
	
	
	/**
	 * Computes the ( [v1] * [v2] ) cross product .
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @param result : the cross product result
	 */
	public static final void vector_crossProduct(JGL_3DVector v1, JGL_3DVector v2, JGL_3DVector result) {
		result.x = (v1.y*v2.z) - (v1.z*v2.y);
		result.y = (v1.z*v2.x) - (v1.x*v2.z);
		result.z = (v1.x*v2.y) - (v1.y*v2.x);
	}
	
	
	
	/**
	 * Returns the square of the distance between the 2 vectors.
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @return the square of the distance
	 */
	public static final float vector_squareDistance(JGL_3DVector v1, JGL_3DVector v2) {
		float x = v2.x - v1.x;
		float y = v2.y - v1.y;
		float z = v2.z - v1.z;
		return (x * x) + (y * y) + (z * z);
	}
	
	
	
	
	/**
	 * Applies a rotation to a (0, 0, -1) vector with the specified angles 
	 * around the Y axis, then the X axis.
	 * 
	 * @param angleX : the X axis rotation angle
	 * @param angleY : the Y axis rotation angle
	 * @param result : the rotation result
	 */
	public static final void vector_fastYXrotate(float angleX, float angleY, JGL_3DVector result) {	
		result.x = sin(angleY) * cos(angleX);
		result.y = sin(angleX);
		result.z = -cos(angleY) * cos(angleX);
	}
	
	
	
	/**
	 * Applies a rotation to the specified vector with the specified angles 
	 * around the Y axis, then the X axis.
	 * 
	 * @param vector : the vector to rotate
	 * @param angleX : the X axis rotation angle
	 * @param angleY : the Y axis rotation angle
	 * @param result : the rotation result
	 */
	public static final void vector_fastYXrotate(JGL_3DVector vector, float angleX, float angleY, JGL_3DVector result) {
		
		float cosX = cos(angleX);
		float sinX = sin(angleX);
		float cosY = cos(angleY);
		float sinY = sin(angleY);
		
		float x = vector.x;
		float y = vector.y;
		float z = vector.z;
		
		result.x = (cosY * x) - (sinY * sinX * y) - (sinY * cosX * z);
		result.y = (cosX * y) - (sinX * z);
		result.z = (sinY * x) + (cosY * sinX * y) + (cosY * cosX * z);
	}
	
	
	
	/**
	 * Computes the linear interpolation between the 2 vectors according to the 
	 * specified parametric variable.
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @param p : the parametric variable
	 * @param result : the interpolation result
	 */
	public static final void vector_interpolationLinear(JGL_3DVector v1, JGL_3DVector v2, float p, JGL_3DVector result) {
		if(p<0f || p>1f)
			return;
		
		float z = 1 - p;
		result.x = ( z * v1.x ) + ( p * v2.x );
		result.y = ( z * v1.y ) + ( p * v2.y );
		result.z = ( z * v1.z ) + ( p * v2.z );
	}
	
	
	
	/**
	 * Computes the cubic interpolation between the 4 vectors according to the 
	 * specified parametric variable.
	 * 
	 * @param v1 : the first vector
	 * @param v2 : the second vector
	 * @param v3 : the third vector
	 * @param v4 : the fourth vector
	 * @param p : the parametric variable
	 * @param result : the interpolation result
	 */
	public static final void vector_interpolationCubic(	JGL_3DVector v1, JGL_3DVector v2, JGL_3DVector v3, JGL_3DVector v4, 
														float p, JGL_3DVector result ) {
		if(p<0f || p>1f)
			return;
		
		float a1, a2, a3, a4;
		float p2 = p * p;
		
		a1 = v4.x - v3.x - v1.x + v2.x;
		a2 = v1.x - v2.x - a1;
		a3 = v3.x - v1.x;
		a4 = v2.x;
		result.x = ( a1 * p * p2 ) + ( a2 * p2 ) + ( a3 * p ) + a4;
		
		a1 = v4.y - v3.y - v1.y + v2.y;
		a2 = v1.y - v2.y - a1;
		a3 = v3.y - v1.y;
		a4 = v2.y;
		result.y = ( a1 * p * p2 ) + ( a2 * p2 ) + ( a3 * p ) + a4;
		
		a1 = v4.z - v3.z - v1.z + v2.z;
		a2 = v1.z - v2.z - a1;
		a3 = v3.z - v1.z;
		a4 = v2.z;
		result.z = ( a1 * p * p2 ) + ( a2 * p2 ) + ( a3 * p ) + a4;
		
	}
	
	
	
	
	
	/**
	 * Computes the intersection point between the specified plane 
	 * and the line defined by the specified 2 vectors.<br>
	 * If there is no intersection, the result object is unchanged.
	 * 
	 * @param plane : the plane
	 * @param p1 : the line first point
	 * @param p2 : the line second point
	 * @param result : the intersection result (unchanged if no intersection)
	 * @return true if intersection, false otherwise
	 */
	public static final boolean plane_lineIntersection(JGL_3DPlane plane, JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector result) {
		
		vect4.x = p2.x - p1.x;
		vect4.y = p2.y - p1.y;
		vect4.z = p2.z - p1.z;
		
		float d1 = plane.distance(p1);
		float d2 = plane.distance(p2);
		float dDiff = d1 - d2;
		
		if(Math.abs(dDiff) < JGL_Math.EPSILON)
			return false;
		
		float frac = d1 / dDiff;
		
		result.x = p1.x + (vect4.x * frac);
		result.y = p1.y + (vect4.y * frac);
		result.z = p1.z + (vect4.z * frac);
		return true;
	}
	
	
	/**
	 * Computes the intersection point between the specified plane 
	 * and the segment defined by the specified 2 vectors.<br>
	 * If there is no intersection, the result object is unchanged.
	 * 
	 * @param plane : the plane
	 * @param p1 : the line first point
	 * @param p2 : the line second point
	 * @param a : parametric variable for the segment first point  (usually 0)
	 * @param b : parametric variable for the segment second point (usually 1)
	 * @param result : the intersection result (unchanged if no intersection)
	 * @return the intersection's parametric variable, or 99 if no intersection
	 */
	public static final float plane_segmentIntersection(JGL_3DPlane plane, JGL_3DVector p1, JGL_3DVector p2, float a, float b, JGL_3DVector result) {
		
		vect4.x = p2.x - p1.x;
		vect4.y = p2.y - p1.y;
		vect4.z = p2.z - p1.z;
		
		float d1 = plane.distance(p1);
		float d2 = plane.distance(p2);
		
		if ( (d1<0f && d2<0f) || (d1>0f && d2>0f))
			return 99f;
		
		float dDiff = d1 - d2;
		if(Math.abs(dDiff) < EPSILON)
			return 99f;
		
		float frac = d1 / dDiff;
		
		if(frac>=a && frac<=b) {
			result.x = p1.x + (vect4.x * frac);
			result.y = p1.y + (vect4.y * frac);
			result.z = p1.z + (vect4.z * frac);
			return frac;
		}
		
		return 99f;
	}
	
	
	
	/**
	 * Returns the specified face's position against the specified plane.<br>
	 * -1 : behind the plane<br>
	 *  0 : across the plane<br>
	 *  1 : before the plane<br>
	 *  9 : inside the plane - turned the other side<br>
	 *  10 : inside the plane - turned the same side<br>
	 * 
	 * @param plane : the plane
	 * @param triangle : the triangle to test
	 * @return the triangle's position
	 */
	public static final int plane_trianglePosition(JGL_3DPlane plane, JGL_3DTriangle triangle) {
		
		float d1 = plane.distance(triangle.point1);
		float d2 = plane.distance(triangle.point2);
		float d3 = plane.distance(triangle.point3);
		
		if (Math.abs(d1)<=EPSILON && 
			Math.abs(d2)<=EPSILON && 
			Math.abs(d3)<=EPSILON) {
			
			vect1.x = triangle.point2.x - triangle.point1.x;
			vect1.y = triangle.point2.y - triangle.point1.y;
			vect1.z = triangle.point2.z - triangle.point1.z;
			
			vect2.x = triangle.point3.x - triangle.point1.x;
			vect2.y = triangle.point3.y - triangle.point1.y;
			vect2.z = triangle.point3.z - triangle.point1.z;
			
			vector_crossProduct(vect1, vect2, vect3);
				if (vector_dotProduct(plane.normal, vect3)<0f)
					return 9;
				
				return 10;
		}
		
		if (d1<=EPSILON && d2<=EPSILON && d3<=EPSILON)
			return -1;
		
		if (d1>=-EPSILON && d2>=-EPSILON && d3>=-EPSILON)
			return 1;
		
		return 0;
	}
	
	
	
	/**
	 * Splits the specified triangle by the specified plane. A split can 
	 * produce 2 or 3 triangles, the method returns the number of split triangles.
	 * 
	 * @param triangle : the triangle to split
	 * @param plane : the split plane
	 * @param result1 : the first split triangle
	 * @param result2 : the second split triangle
	 * @param result3 : the third split triangle
	 * @return the number of split triangles
	 */
	public static final int triangle_split(JGL_3DTriangle triangle, JGL_3DPlane plane, 
									 	JGL_3DTriangle result1, JGL_3DTriangle result2, JGL_3DTriangle result3) {
		
		float frac;
		short nbSplit = 0;
		short scan = 0;
		
		frac = JGL_Math.plane_segmentIntersection(plane, triangle.point1, triangle.point2, 0f, 1f, vect1);
		if(frac != 99f && !vect1.eq(triangle.point1) && !vect1.eq(triangle.point2)) {
			nbSplit++; scan += 0;
		}
		
		frac = JGL_Math.plane_segmentIntersection(plane, triangle.point2, triangle.point3, 0f, 1f, vect2);
		if(frac != 99f && !vect2.eq(triangle.point2) && !vect2.eq(triangle.point3)) {
			nbSplit++; scan += 2;
		}
		
		frac = JGL_Math.plane_segmentIntersection(plane, triangle.point3, triangle.point1, 0f, 1f, vect3);
		if(frac != 99f && !vect3.eq(triangle.point3) && !vect3.eq(triangle.point1)) {
			nbSplit++; scan += 4;
		}
		
		if(nbSplit == 1) {
			if(scan == 0) {
				result1.assign(triangle.point1, vect1, triangle.point3);
				result2.assign(vect1, triangle.point2, triangle.point3);
			}
			else if(scan == 2) {
				result1.assign(triangle.point2, vect2, triangle.point1);
				result2.assign(vect2, triangle.point3, triangle.point1);
			}
			else if(scan == 4) {
				result1.assign(triangle.point3, vect3, triangle.point2);
				result2.assign(vect3, triangle.point1, triangle.point2);
			}
			result1.color = result2.color  = triangle.color;
			return 2;
		}
		
		if(nbSplit == 2) {
			if(scan == 2) {
				result1.assign(vect1, triangle.point2, vect2);
				result2.assign(vect1, vect2, triangle.point3);
				result3.assign(vect1, triangle.point3, triangle.point1);
			}
			else if(scan == 6) {
				result1.assign(vect2, triangle.point3, vect3);
				result2.assign(vect2, vect3, triangle.point1);
				result3.assign(vect2, triangle.point1, triangle.point2);
			}
			else if(scan == 4) {
				result1.assign(vect3, triangle.point1, vect1);
				result2.assign(vect3, vect1, triangle.point2);
				result3.assign(vect3, triangle.point2, triangle.point3);
			}
			result1.color = result2.color = result3.color = triangle.color;
			return 3;
		}
		return 0;
	}
	
}
