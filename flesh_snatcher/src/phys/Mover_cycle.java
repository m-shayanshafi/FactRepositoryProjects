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
 * Mover implementation for position keys motion objects.
 * 
 * @author Nicolas Devere
 *
 */
public final class Mover_cycle implements Mover {
	
	/** Linear interpolation */
	public static int LINEAR = 0;
	
	/** Cubic interpolation */
	public static int CUBIC  = 1;
	
	private int interpol;
	
	private JGL_3DVector vel;
	private JGL_3DVector mov;
	private JGL_3DVector trans;
	private float sp;
	
	private JGL_3DVector shapePos;
	private JGL_3DVector nextPos;
	
	private JGL_3DVector[] keys;
	private float t;
	
	
	/**
	 * Constructor.
	 * 
	 * @param shapePosition : the start position
	 * @param translationKeys : the position keys list
	 * @param speed : the mover speed
	 * @param interpolation : the interpolation style
	 */
	public Mover_cycle(JGL_3DVector shapePosition, JGL_3DVector[] translationKeys, float speed, int interpolation) {
		
		interpol = interpolation;
		keys = translationKeys;
		sp = speed;
		vel = new JGL_3DVector();
		mov = new JGL_3DVector();
		trans = new JGL_3DVector();
		shapePos = shapePosition;
		shapePos.assign(keys[0]);
		nextPos = new JGL_3DVector();
		nextPos.assign(keys[0]);
		t = 0f;
	}
	
	
	public JGL_3DVector getMove() {
		// TODO Auto-generated method stub
		return mov;
	}

	public float getSpeed() {
		// TODO Auto-generated method stub
		return sp;
	}

	public JGL_3DVector getVelocity() {
		// TODO Auto-generated method stub
		vel.assign(mov);
		vel.normalize();
		return vel;
	}

	public boolean impactReaction(Trace trace, Tracable tracable) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setSpeed(float arg) {
		// TODO Auto-generated method stub
		sp = arg;
	}

	public void setVelocity(JGL_3DVector arg) {
		// TODO Auto-generated method stub
		vel = arg;
	}
	
	public void addMover(Mover arg) {
		JGL_Math.vector_add(trans, arg.getMove(), trans);
	}
	
	/**
	 * Returns the current parametric value.
	 * 
	 * @return the current parametric value
	 */
	public float getParametric() {
		// TODO Auto-generated method stub
		return t;
	}
	
	/**
	 * Sets the parametric value.
	 * 
	 * @param arg : the parametric value
	 */
	public void setParametric(float arg) {
		// TODO Auto-generated method stub
		t = arg;
	}

	public void update() {
		
		int i1, i2, i3, i4;
		float tr;
		
		if (interpol==LINEAR) {
			while(t >= keys.length)
				t -= keys.length;
			
			i1 = (int)Math.floor(t);
			if(i1 != keys.length - 1)
				i2 = i1 + 1;
			else
				i2 = 0;
			
			tr = t - i1;
			JGL_Math.vector_interpolationLinear(keys[i1], keys[i2], tr, nextPos);
		}
		else if (interpol==CUBIC) {
			while(t >= keys.length)
				t -= keys.length;
			
			i1 = (int)Math.floor(t);
			if(i1 != keys.length - 1)
				i2 = i1 + 1;
			else
				i2 = 0;
			
			if(i2 != keys.length - 1)
				i3 = i2 + 1;
			else
				i3 = 0;
			
			if(i3 != keys.length - 1)
				i4 = i3 + 1;
			else
				i4 = 0;
			
			tr = t - i1;
			JGL_Math.vector_interpolationCubic(keys[i1], keys[i2], keys[i3], keys[i4], tr, nextPos);
		}
		
		mov.x = (nextPos.x - shapePos.x) + trans.x;
		mov.y = (nextPos.y - shapePos.y) + trans.y;
		mov.z = (nextPos.z - shapePos.z) + trans.z;
		
		trans.assign(0f, 0f, 0f);
		
		t += sp * JGL_Time.getTimer();
	}
	
	public Object clone() {
		return new Mover_cycle(shapePos, keys, sp, interpol);
	}

}
