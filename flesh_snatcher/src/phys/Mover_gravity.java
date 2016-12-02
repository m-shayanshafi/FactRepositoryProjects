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
 * Mover implementation for objects subjects to gravity.
 * Provides also a jump system.
 * 
 * @author Nicolas Devere
 *
 */
public final class Mover_gravity implements Mover {
	
	/** Gravity force */
	public static float GRAVITY = 300f;
	
	/** Max height of a stair step to be climbed */
	public static float STEP = 2.1f;
	
	private static float jumpHeight = 12f;
	private static float jumpSpeed = 2.5f;
	private static float zeroG = 0.02f;
	
	private JGL_3DVector vel;
	private JGL_3DVector mov;
	private JGL_3DVector trans;
	private float sp;
	
	private float gSpeed;
	
	private boolean jump;
	private float yCumul;
	private float yi;
	
	
	/**
	 * Constructs a gravity mover given a normalized velocity vector.
	 * 
	 * @param velocity : normalized vector showing the mover's direction
	 */
	public Mover_gravity(JGL_3DVector velocity) {
		vel = velocity;
		mov = new JGL_3DVector();
		trans = new JGL_3DVector();
		sp = 0f;
		
		resetGravity();
		
		jump = false;
		yi = yCumul = 0f;
	}
	
	
	
	public void setSpeed(float speed) {
		sp = speed;
	}
	
	
	/**
	 * Sets a normalized velocity vector to the mover.
	 * 
	 * @param arg : the normalized velocity vector
	 */
	public void setVelocity(JGL_3DVector arg) {
		vel = arg;
		vel.normalize();
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
	
	
	
	public void update() {
		
		JGL_Math.vector_multiply(vel, sp * JGL_Time.getTimer(), mov);
		
		if (!jump || (jump && yCumul>=jumpHeight)) {
			gSpeed += GRAVITY * JGL_Time.getTimePerFrame();
			mov.y -= (gSpeed * JGL_Time.getTimePerFrame()) + zeroG;
		}
		else {
			if (yCumul>jumpHeight*0.75f) yi = jumpSpeed * 0.1f * JGL_Time.getTimer();
			else if (yCumul>jumpHeight*0.5f) yi = jumpSpeed * 0.3f * JGL_Time.getTimer();
			else if (yCumul>jumpHeight*0.25f) yi = jumpSpeed * 0.6f * JGL_Time.getTimer();
			else yi = jumpSpeed * JGL_Time.getTimer();
			yCumul += yi;
			if (yCumul>jumpHeight) {
				yi = jumpHeight - yCumul;
				yCumul = jumpHeight;
			}
			mov.y = yi;
		}
		
		JGL_Math.vector_add(mov, trans, mov);
		trans.assign(0f, 0f, 0f);
	}
	
	
	/**
	 * Starts a jump sequence.
	 * 
	 * @return true if the jump begins, false if the mover is already jumping
	 */
	public boolean jump() {
		if (!jump) {
			yCumul = yi = 0f;
			resetGravity();
			jump = true;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Returns if the mover is currently jumping.
	 * 
	 * @return if the mover is currently jumping
	 */
	public boolean isJumping() {
		return jump;
	}
	
	
	
	public boolean impactReaction(Trace trace, Tracable tracable) {
		

		if (trace.dummy)
			resetGravity();
		
		if (!trace.isImpact()) return jump;
		
		// Floor management
		if (trace.correction.normal.y > 0.7f) {
			resetGravity();
			if (jump)
				jump = false;
			else if (trace.segment.x==0f && trace.segment.z==0f)
				trace.correction = Util4Phys.up;
		}
		
		// Ceiling management
		if (trace.correction.normal.y < -0.8f) {
			if (mov.y>0f)
				resetGravity();
			if (jump)
				yCumul = jumpHeight;
			else if (trace.segment.x==0f && trace.segment.z==0f)
				trace.correction = Util4Phys.down;
			
		}
		return jump;
	}
	
	
	/**
	 * Resets the gravity parameters as if it's no fall.
	 */
	private void resetGravity() {
		gSpeed = 0f;
		mov.y = -zeroG;
	}
	
	
	public Object clone() {
		
		Mover mover = new Mover_gravity(new JGL_3DVector());
		mover.getVelocity().assign(vel);
		return mover;
	}

}
