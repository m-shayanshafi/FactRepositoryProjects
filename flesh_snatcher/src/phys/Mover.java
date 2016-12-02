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
 * Interface for movers objects describing a 3D translation.
 * 
 * @author Nicolas Devere
 *
 */
public interface Mover {
	
	
	/**
	 * Sets the mover's speed.
	 * 
	 * @param arg : the mover's speed
	 */
	public void setSpeed(float arg);
	
	
	/**
	 * Returns the current mover's speed.
	 * 
	 * @return the current mover's speed
	 */
	public float getSpeed();
	
	
	/**
	 * Sets the velocity normalized vector.
	 * 
	 * @param arg : the velocity vector
	 */
	public void setVelocity(JGL_3DVector arg);
	
	
	/**
	 * Returns the normalized velocity vector.
	 * 
	 * @return the velocity vector
	 */
	public JGL_3DVector getVelocity();
	
	
	/**
	 * Returns the move vector currently described in the mover.
	 * 
	 * @return the current move vector
	 */
	public JGL_3DVector getMove();
	
	
	/**
	 * Adds the move vector described by the specified Mover to this Mover.
	 * This move vector will be removed after the next <code>update()</code> method call.
	 * 
	 * @param arg : the Mover to add
	 */
	public void addMover(Mover arg);
	
	
	/**
	 * Updates the mover's data.
	 */
	public void update();
	
	
	/**
	 * Modifies the mover's data according to the specified impact trace and the current Tracable. 
	 * 
	 * @param trace : the impact trace to make the mover react
	 * @param tracable : the current tracable object
	 */
	public boolean impactReaction(Trace trace, Tracable tracable);
	
	
	/**
	 * Returns a copy of the mover.
	 * 
	 * @return a copy of the mover
	 */
	public Object clone();
	
}
