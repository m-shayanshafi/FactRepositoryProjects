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

package ai;

import jglcore.JGL_3DVector;
import jglcore.JGL_Math;


/**
 * AI functions and parameters.
 * 
 * @author Nicolas Devere
 *
 */
public final class Util4AI {
	
	
	private static JGL_3DVector s_v = new JGL_3DVector();
	private static JGL_3DVector s_seg1 = new JGL_3DVector();
	private static JGL_3DVector s_seg2 = new JGL_3DVector();
	private static JGL_3DVector s_cross = new JGL_3DVector();
	
	
	/**
	 * Returns if the specified point is in the cone.
	 * 
	 * @param p : the point to test
	 * @param base : the cone base
	 * @param cone : the 4 cones edges
	 * @return if the point is in the cone
	 */
	public static boolean isInCone(JGL_3DVector p, JGL_3DVector base, JGL_3DVector[] cone) {
		
		JGL_Math.vector_subtract(p, base, s_v);
		
		JGL_Math.vector_subtract(cone[0], base, s_seg1);
		JGL_Math.vector_subtract(cone[1], base, s_seg2);
		JGL_Math.vector_crossProduct(s_seg2, s_seg1, s_cross);
		if (JGL_Math.vector_dotProduct(s_v, s_cross)<0f) return false;
		
		JGL_Math.vector_subtract(cone[2], base, s_seg1);
		JGL_Math.vector_subtract(cone[3], base, s_seg2);
		JGL_Math.vector_crossProduct(s_seg2, s_seg1, s_cross);
		if (JGL_Math.vector_dotProduct(s_v, s_cross)<0f) return false;
		
		JGL_Math.vector_subtract(cone[0], base, s_seg1);
		JGL_Math.vector_subtract(cone[3], base, s_seg2);
		JGL_Math.vector_crossProduct(s_seg1, s_seg2, s_cross);
		if (JGL_Math.vector_dotProduct(s_v, s_cross)<0f) return false;
		
		JGL_Math.vector_subtract(cone[1], base, s_seg1);
		JGL_Math.vector_subtract(cone[2], base, s_seg2);
		JGL_Math.vector_crossProduct(s_seg2, s_seg1, s_cross);
		if (JGL_Math.vector_dotProduct(s_v, s_cross)<0f) return false;
		
		return true;
	}

}
