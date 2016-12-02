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


/**
 * Mover implementation for motionless objects.
 * 
 * @author Nicolas Devere
 *
 */
public final class Mover_none implements Mover {
	
	private static JGL_3DVector neutral = new JGL_3DVector();

	public JGL_3DVector getMove() {
		// TODO Auto-generated method stub
		return neutral;
	}

	public float getSpeed() {
		// TODO Auto-generated method stub
		return 0f;
	}

	public JGL_3DVector getVelocity() {
		// TODO Auto-generated method stub
		return neutral;
	}

	public boolean impactReaction(Trace trace, Tracable tracable) {return true;}

	public void setSpeed(float arg) {
		// TODO Auto-generated method stub

	}

	public void setVelocity(JGL_3DVector arg) {
		// TODO Auto-generated method stub

	}
	
	public void addMover(Mover arg) {}

	public void update() {
		// TODO Auto-generated method stub

	}
	
	public Object clone() {
		return new Mover_none();
	}

}
