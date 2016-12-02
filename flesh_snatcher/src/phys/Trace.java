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

import jglcore.JGL_3DVector;
import jglcore.JGL_3DPlane;


/**
 * Stores a 3D segment and some impact data on this segment.
 * 
 * @author Nicolas Devere
 *
 */
public final class Trace {
	
	/** The collision shape */
	public Shape cshape;
	
	/** The trace start point */
	public JGL_3DVector start;
	
	/** The trace end point */
	public JGL_3DVector end;
	
	/** The trace segment */
	public JGL_3DVector segment;
	
	/** The trajectory correction. */
	public JGL_3DPlane correction;
	
	/** The impact fraction of the full distance. */
	public float fractionImpact;
	
	/** The true intersection fraction of the full distance */
	public float fractionReal;
	
	/** State of the trace */
	public boolean dummy;
	
	
	/**
	 * Constructs a new trace with no impact.
	 */
	public Trace() {
		cshape = null;
		start = new JGL_3DVector();
		end = new JGL_3DVector();
		segment = new JGL_3DVector();
		clearImpact();
	}
	
	
	/**
	 * Resets the trace as if there's no impact.
	 * the <code>isImpact()</code> method will return <code>false</code>.
	 * 
	 * @param _cshape : the collision shape
	 * @param _start : the start trace segment point
	 * @param _end : the end trace segment point
	 */
	public void reset(Shape _cshape, JGL_3DVector _start, JGL_3DVector _end) {
		cshape = _cshape;
		start.assign(_start);
		end.assign(_end);
		segment.x = end.x - start.x;
		segment.y = end.y - start.y;
		segment.z = end.z - start.z;
		clearImpact();
	}
	
	
	/**
	 * Stores the specified impact data.
	 *  
	 * @param _correction : the impact plane to correct the segment
	 * @param fraction_impact : the impact fraction compared to the full segment
	 * @param fraction_real : the real intersection fraction compared to the full segment
	 */
	public void setImpact(JGL_3DPlane _correction, float fraction_impact, float fraction_real) {
		correction = _correction;
		fractionImpact = fraction_impact;
		fractionReal = fraction_real;
	}
	
	
	/**
	 * Stores the specified impact data, only if the specified real fraction 
	 * is lesser than the object's one.
	 * 
	 * @param _correction : the impact plane to correct the segment
	 * @param fraction_impact : the impact fraction compared to the full segment
	 * @param fraction_real : the real intersection fraction compared to the full segment
	 * @return if the impact is stored or not
	 */
	public boolean setNearerImpact(JGL_3DPlane _correction, float fraction_impact, float fraction_real) {
		
		if(fraction_real < fractionReal) {
			correction = _correction;
			fractionReal = fraction_real;
			fractionImpact = fraction_impact;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Clears the trace as if there's no impact.
	 */
	public void clearImpact() {
		correction = null;
		fractionImpact = fractionReal = 1f;
		dummy = false;
	}
	
	
	/**
	 * Returns if the trace stores an impact point.
	 * 
	 * @return if the trace stores an impact point
	 */
	public boolean isImpact() {
		return fractionReal < 1f;
	}
	
}
