//	Copyright 2008 - 2010 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package jglanim;

import jglcore.JGL_3DVector;


/**
 * Represents a spatial key situation. It's composed by 
 * a 3D position and orientation angles (in degrees) on the 3 axis.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL_Keyframe {
	
	/** Descibes the spatial position */
	public JGL_3DVector position;
	
	/** Descibes the spatial orientation on the 3 axis */
	public JGL_3DVector orientation;
	
	
	/**
	 * Constructs a neutral keyframe (all values equal 0).
	 *
	 */
	public JGL_Keyframe() {
		position = new JGL_3DVector();
		orientation = new JGL_3DVector();
	}
	
	
	
	
	/**
	 * Constructs a keyframe given a position and an orientation on the 3 axis.
	 * 
	 * @param _position : the position
	 * @param _orientation : the orientation
	 */
	public JGL_Keyframe(JGL_3DVector _position, JGL_3DVector _orientation) {
		position = _position;
		orientation = _orientation;
	}
	
}
