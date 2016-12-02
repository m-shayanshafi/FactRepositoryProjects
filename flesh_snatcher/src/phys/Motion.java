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


/**
 * Interface for collision-managed motion processors.
 * 
 * @author Nicolas Devere
 *
 */
public interface Motion {
	
	/**
	 * Processes a collision-managed motion to the specified bounding shape, according to the specified mover, 
	 * against the specified tracable object.
	 * 
	 * @param cshape : the bounding shape to move
	 * @param mover : the mover object
	 * @param tracable : the object to collide against
	 * @return if a collision occured
	 */
	public boolean process(Shape cshape, Mover mover, Tracable tracable);
	
	/**
	 * Returns the Motion trace in its current state.
	 * 
	 * @return the Motion trace in its current state
	 */
	public Trace getTrace();
	
	public Object clone();

}
