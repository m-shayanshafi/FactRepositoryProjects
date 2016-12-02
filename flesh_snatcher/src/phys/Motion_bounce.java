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
import jglcore.JGL_Math;


/**
 * collision-managed movement processor implementation for bouncing objects.
 * 
 * @author Nicolas Devere
 *
 */
public final class Motion_bounce implements Motion {
	
	
	private JGL_3DVector pos1;
	private JGL_3DVector pos2;
	private Trace trace;
	
	
	
	/**
	 * Constructor
	 */
	public Motion_bounce() {
		pos1 = new JGL_3DVector();
		pos2 = new JGL_3DVector();
		trace = new Trace();
	}
	
	
	/**
	 * Collides and bounces the specified bounding shape, according to the specified mover, 
	 * against the specified tracable object.
	 * 
	 * @param cshape : the collision shape to collide
	 * @param tracable : the object to collide against
	 * @return if a collision occurs
	 */
	public boolean process(Shape cshape, Mover mover, Tracable tracable) {
		// TODO Auto-generated method stub
		pos1 = cshape.getPosition();
		JGL_Math.vector_add(pos1, mover.getMove(), pos2);
		
		trace.reset(cshape, pos1, pos2);
		boolean test = tracable.trace(trace);
			
		if (test) {
			pos1.assign(trace.start.x + (trace.segment.x * trace.fractionImpact), 
						trace.start.y + (trace.segment.y * trace.fractionImpact), 
						trace.start.z + (trace.segment.z * trace.fractionImpact));
			
			JGL_3DVector vel = mover.getVelocity();
			float dot = Math.abs(JGL_Math.vector_dotProduct(vel, trace.correction.normal));
			vel.x += 2 * dot * trace.correction.normal.x;
			vel.y += 2 * dot * trace.correction.normal.y;
			vel.z += 2 * dot * trace.correction.normal.z;
			vel.normalize();
			mover.impactReaction(trace, tracable);
		}
		else
			pos1.assign(pos2);
		
		return test;
	}
	
	
	public Trace getTrace() {
		return trace;
	}
	
	
	public Object clone() {
		return new Motion_bounce();
	}
}
