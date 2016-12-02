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
 * Mover implementation for straight-moved objects.
 * 
 * @author Nicolas Devere
 *
 */
public final class Mover_linear implements Mover {
	
	
	private JGL_3DVector vel;
	private JGL_3DVector mov;
	private JGL_3DVector trans;
	private float sp;
	
	
	
	/**
	 * Constructs a simple mover given a normalized velocity vector.
	 * 
	 * @param velocity : normalized vector showing the mover's direction
	 */
	public Mover_linear(JGL_3DVector velocity) {
		vel = velocity;
		mov = new JGL_3DVector();
		trans = new JGL_3DVector();
		sp = 0f;
	}
	
	
	
	public void setSpeed(float speed) {
		sp = speed;
	}
	
	
	
	public void update() {
		JGL_Math.vector_multiply(vel, sp * JGL_Time.getTimer(), mov);
		JGL_Math.vector_add(mov, trans, mov);
		trans.assign(0f, 0f, 0f);
		
	}
	
	
	/**
	 * Sets a normalized velocity vector to the mover.
	 * 
	 * @param arg : the normalized velocity vector
	 */
	public void setVelocity(JGL_3DVector arg) {
		vel = arg;
	}
	
	
	/**
	 * Returns the mover's velocity vector.
	 * 
	 * @return the mover's velocity vector
	 */
	public JGL_3DVector getVelocity() {
		return vel;
	}
	
	
	
	public float getSpeed() {
		return sp;
	}
	
	
	
	public JGL_3DVector getMove() {
		return mov;
	}
	
	
	public void addMover(Mover arg) {
		JGL_Math.vector_add(trans, arg.getMove(), trans);
	}
	
	
	public boolean impactReaction(Trace trace, Tracable tracable) {return true;}
	
	
	
	public Object clone() {
		
		Mover mover = new Mover_linear(new JGL_3DVector());
		mover.getVelocity().assign(vel);
		return mover;
	}
	
}
