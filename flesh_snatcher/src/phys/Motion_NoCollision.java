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

import jglcore.JGL_Math;

public class Motion_NoCollision implements Motion {

	public boolean process(Shape cshape, Mover mover, Tracable tracable) {
		// TODO Auto-generated method stub
		JGL_Math.vector_add(cshape.getPosition(), mover.getMove(), cshape.getPosition());
		return false;
	}
	
	public Trace getTrace() {
		return new Trace();
	}
	
	public Object clone() {
		return new Motion_NoCollision();
	}
}
