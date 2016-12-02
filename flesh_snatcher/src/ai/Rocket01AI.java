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

package ai;

import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import jglcore.JGL_Time;
import phys.Shape;
import phys.Trace;
import phys.Util4Phys;
import main.Player;
import entity.Rocket01;
import world.World;

public class Rocket01AI {
	

	//-------- STATIC
	
	// Behave parameters
	private static int ATTACK 	= 0;
	private static int ESCAPE 	= 1;
	private int behave;
	
	// FOCUS Parameters
	private static int TURN_LEFT 	= 4;
	private static int TURN_RIGHT 	= 5;
	private static int TURN_UP 		= 6;
	private static int TURN_DOWN 	= 7;
	private static float maxTurnStep = 4f;
	//private static float midTurnStep = 2f;
	private static float minTurnStep = 0.25f;
	
	// TARGETTING Parameters
	private static float escapeHeight = 150f;
	private static float move4reset = 2f;
	
	// Map collision for escape
	private static Trace impact = new Trace();
	
	// Optimization
	private static JGL_3DVector s_v = new JGL_3DVector();
	
	
	//-------- INSTANCE
	
	private Rocket01 z;
	
	// Focus variables
	private JGL_3DVector relativePosOld;
	private JGL_3DVector relativePos;
	private JGL_3DVector hDir;
	private JGL_3DVector target;
	private float turnStepH;
	private float turnStepV;
	private int turnSideH;
	private int turnSideV;
	
	// Escape point
	private Shape point;
	
	// Time variables
	private float timer;
	
	
	/**
	 * Constructs a AI associated to the specified zombie.
	 * 
	 * @param zombie : the zombie to associate
	 */
	public Rocket01AI(Rocket01 zombie) {
		
		z = zombie;
		
		behave = ATTACK;
		target = Player.entity.getPosition();
		hDir = new JGL_3DVector();
		relativePos = new JGL_3DVector();
		relativePosOld = new JGL_3DVector();
		turnStepH = turnStepV = maxTurnStep;
		turnSideH = turnSideV = -1;
		
		point = (Shape)z.getCShape().clone();
		timer = 1f;
	}
	
	
	public void setEscape() {
		if (behave==ESCAPE)
			return;
		
		point.getPosition().assign(	z.getPosition().x, 
									z.getPosition().y + 
									z.getCShape().getOffset(Util4Phys.down.normal) + 
									point.getOffset(Util4Phys.up.normal) + 
									Util4Phys.MIN_DISTANCE, 
									z.getPosition().z);
		s_v.assign(point.getPosition());
		s_v.y += escapeHeight;
		impact.reset(point, point.getPosition(), s_v);
		World.map.trace(impact);
		if (!impact.dummy) {
			point.getPosition().assign(	impact.start.x + (impact.segment.x * impact.fractionImpact), 
										impact.start.y + (impact.segment.y * impact.fractionImpact), 
										impact.start.z + (impact.segment.z * impact.fractionImpact));
			target = point.getPosition();
			behave = ESCAPE;
		}
	}
	
	
	
	/**
	 * Updates the AI behavior.
	 */
	public void update() {
		
		float dist;
		float ax, ay;
		boolean reset = false;
		timer = JGL_Time.getTimer();
		
		if (behave==ESCAPE) {
			// Test reaching the current point
			JGL_Math.vector_add(z.getMover().getMove(), z.getPosition(), s_v);
			impact.reset(z.getCShape(), z.getPosition(), s_v);
			if (point.trace(impact)) {
				behave = ATTACK;
				target = Player.entity.getPosition();
			}
		}
		
		// Check the shoot
		JGL_Math.vector_subtract(target, z.getPosition(), relativePos);
		dist = relativePos.norm();
		JGL_3DVector angles = z.getOrientation();
		JGL_Math.vector_fastYXrotate(angles.x, angles.y, hDir);
		
		// Check to reset the movement
		if (Math.abs(relativePos.x - relativePosOld.x)>move4reset || 
			Math.abs(relativePos.y - relativePosOld.y)>move4reset || 
			Math.abs(relativePos.z - relativePosOld.z)>move4reset) {
			reset = true;
			turnStepH = turnStepV = maxTurnStep;
			relativePosOld.assign(relativePos);
		}
		
		// Check the vertical rotation
		JGL_Math.vector_multiply(hDir, dist, hDir);
		if (hDir.y < relativePos.y) {
			if (!reset && turnSideV == TURN_DOWN && turnStepV > minTurnStep)
				turnStepV *= 0.5f;
			turnSideV = TURN_UP;
			ax = turnStepV * timer;
		}
		else {
			if (!reset && turnSideV == TURN_UP && turnStepV > minTurnStep)
				turnStepV *= 0.5f;
			turnSideV = TURN_DOWN;
			ax = -turnStepV * timer;
		}
		
		// Check the horizontal rotation
		if ( (hDir.z * relativePos.x) - (hDir.x * relativePos.z) >= 0f ) {
			if (!reset && turnSideH == TURN_RIGHT && turnStepH > minTurnStep)
				turnStepH *= 0.5f;
			turnSideH = TURN_LEFT;
			ay = -turnStepH * timer;
		}
		else {
			if (!reset && turnSideH == TURN_LEFT && turnStepH > minTurnStep)
				turnStepH *= 0.5f;
			turnSideH = TURN_RIGHT;
			ay = turnStepH * timer;
		}
		
		z.increaseAngles(ax, ay);
	}
}
