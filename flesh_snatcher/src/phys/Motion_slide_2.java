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
import jglcore.JGL_Time;


/**
 * collision-managed movement processor implementation for sliding objects.
 * 
 * @author Nicolas Devere
 *
 */
public class Motion_slide_2 implements Motion {

	private static float MIN_MOVE_2;
	
	private float step;
	private JGL_3DVector pos2;
	private JGL_3DVector ref_v;
	private JGL_3DVector tmp_v;
	private JGL_3DVector move;
	private JGL_3DVector crease;
	private JGL_3DVector[] normals;
	private Trace trace;
	private Trace tr;
	private JGL_3DVector act_pos;
	private JGL_3DVector old_pos;
	
	
	/**
	 * Constructor
	 */
	public Motion_slide_2(float stepHeight) {
		
		MIN_MOVE_2 = Util4Phys.MIN_MOVE * Util4Phys.MIN_MOVE;
		
		step = stepHeight;
		pos2 = new JGL_3DVector();
		ref_v = new JGL_3DVector();
		tmp_v = new JGL_3DVector();
		move = new JGL_3DVector();
		crease = new JGL_3DVector();
		trace = new Trace();
		tr = new Trace();
		normals = new JGL_3DVector[Util4Phys.MAX_IMPACT];
		for (int i=0; i<normals.length; i++)
			normals[i] = new JGL_3DVector();
		act_pos = new JGL_3DVector();
		old_pos = new JGL_3DVector();
	}
	
	
	
	/**
	 * Slides the specified bounding shape, according to the specified mover, 
	 * against the specified tracable object.
	 * 
	 * @param cshape : the collision shape to slide
	 * @param mover : the mover object
	 * @param tracable : the object to slide against
	 * @return if a collision occurs
	 */
	public boolean process(Shape cshape, Mover mover, Tracable tracable) {
		
		boolean is_collision = false;
		boolean impact_react;
		short normals_index = 0;
		float frac = 1f;
		float dot;
		
		JGL_3DVector pos = cshape.getPosition();
		act_pos.assign(pos);
		ref_v.assign(mover.getMove());
		tmp_v.assign(ref_v);
		
		for (short loops=0; loops<Util4Phys.MAX_IMPACT; loops++) {
			
			pos2.x = pos.x + (tmp_v.x * frac);
			pos2.y = pos.y + (tmp_v.y * frac);
			pos2.z = pos.z + (tmp_v.z * frac);
			
			trace.reset(cshape, pos, pos2);
			tracable.trace(trace);
			
			if (trace.dummy) {
				pos.assign(old_pos);
				old_pos.assign(act_pos);
				return true;
			}
			
			impact_react = mover.impactReaction(trace, tracable);
			
			if (trace.isImpact() && Math.abs(trace.correction.normal.y)<0.00005f && !impact_react) {
				tr.reset(trace.cshape, trace.start, trace.start);
				tr.end.y += step; tr.segment.y += step;
				tracable.trace(tr);
				if (!tr.isImpact()) {
					tr.reset(trace.cshape, trace.start, trace.end);
					tr.start.y += step; tr.end.y += step;
					tracable.trace(tr);
					if (!tr.isImpact() || !trace.correction.eq(tr.correction)) {
						trace.reset(trace.cshape, trace.start, trace.start);
						float speed = mover.getSpeed() * JGL_Time.getTimer();
						trace.end.y += speed;
						trace.segment.y = speed;
					}
				}
			}
			
			if (trace.isImpact()) {
				is_collision = true;
				if (trace.fractionImpact==0f) {
					pos.assign(trace.start);
					old_pos.assign(act_pos);
					return is_collision;
				}
			}
			else {
				pos.assign(trace.end);
				old_pos.assign(act_pos);
				return is_collision;
			}
			
			frac -= frac * trace.fractionImpact;
			move.x = trace.segment.x * trace.fractionImpact;
			move.y = trace.segment.y * trace.fractionImpact;
			move.z = trace.segment.z * trace.fractionImpact;
			
			if (move.norm2()>=MIN_MOVE_2) normals_index = 0;
			normals[normals_index].assign(trace.correction.normal);
			normals_index++;
			
			pos.x += move.x;
			pos.y += move.y;
			pos.z += move.z;
			
			if (normals_index==1) {
				dot = JGL_Math.vector_dotProduct(ref_v, normals[0]);
				tmp_v.x = (ref_v.x - (normals[0].x * dot));
				tmp_v.y = (ref_v.y - (normals[0].y * dot));
				tmp_v.z = (ref_v.z - (normals[0].z * dot));
			}
			else if (normals_index==2) {
				JGL_Math.vector_crossProduct(normals[0], normals[1], crease);
				crease.normalize();
				dot = JGL_Math.vector_dotProduct(crease, ref_v);
				tmp_v.x = crease.x * dot;
				tmp_v.y = crease.y * dot;
				tmp_v.z = crease.z * dot;
			}
			else {
				old_pos.assign(act_pos);
				return is_collision;
			}
			
			
			
			
			if (JGL_Math.vector_dotProduct(ref_v, tmp_v)<=0f) {
				old_pos.assign(act_pos);
				return is_collision;
			}
		}
		
		//System.out.println("MAX IMPACT !");
		pos.assign(old_pos);
		old_pos.assign(act_pos);
		return is_collision;
	}
	
	
	public Trace getTrace() {
		return trace;
	}
	

	public Object clone() {
		return new Motion_slide_2(step);
	}
	
}
