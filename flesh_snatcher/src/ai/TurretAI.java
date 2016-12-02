//	Copyright 2008 Nicolas Devere
//
//	This file is part of TESSERACT.
//
//	TESSERACT is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	TESSERACT is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with TESSERACT; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package ai;

import world.World;
import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import jglcore.JGL_Time;
import entity.Entity;
import entity.Turret01;
import phys.Tracer;


/**
 * Class providing automatic behaviors to the Turret entities.
 * 
 * @author Nicolas Devere
 *
 */
public final class TurretAI {
	

	//-------- STATIC
	
	// Behave parameters
	private static int SURVEY 	= 0;
	private static int ATTACK 	= 1;
	private int behave;
	
	// FOCUS Parameters
	private static int TURN_LEFT 	= 4;
	private static int TURN_RIGHT 	= 5;
	private static int TURN_UP 		= 6;
	private static int TURN_DOWN 	= 7;
	private static float maxTurnStep = 0.6f;
	private static float midTurnStep = 0.6f;
	private static float minTurnStep = 0.3f;
	
	// TARGETTING Parameters
	private static float move4reset = 2f;
	
	// SHOOT Parameters
	private static float ratio4shoot = 0.99995f;
	
	// Vision cone
	private static JGL_3DVector[] viewCone = new JGL_3DVector[] {
		new JGL_3DVector(-6f, 6f, -10f), //left - up
		new JGL_3DVector(-6f, -6f, -10f), //left - down
		new JGL_3DVector(6f, -6f, -10f), //right - down
		new JGL_3DVector(6f, 6f, -10f) //right - up
	};
	
	
	//-------- OBJECT
	
	private Turret01 t;
	
	// List of surveyed points
	private float[] ax;
	private float[] ay;
	private int curPoint;
	
	// Focus variables
	private JGL_3DVector relativePosOld;
	private JGL_3DVector relativePos;
	private JGL_3DVector hDir;
	private Entity target;
	private float turnStepH;
	private float turnStepV;
	private int turnSideH;
	private int turnSideV;
	private long shootFrequency;
	
	// Vision cone
	private static JGL_3DVector[] curCone;
	
	// Time variables
	private long tShoot;
	private long dShoot;
	private float timer;
	
	
	
	
	/**
	 * Constructs a AI associated to the specified turret, given arrays of orientation angles.
	 * 
	 * @param human : the turret to associate
	 * @param xs : the X axis angles
	 * @param ys : the Y axis angles
	 */
	public TurretAI(Turret01 turret, float[] xs, float[] ys, long _shootFrequency) {
		
		int i;
		
		behave = -1;
		t = turret;
		
		ax = xs;
		ay = ys;
		curPoint = 0;
		
		hDir = new JGL_3DVector();
		relativePos = new JGL_3DVector();
		relativePosOld = new JGL_3DVector();
		turnStepH = midTurnStep;
		shootFrequency = _shootFrequency;
		
		curCone = new JGL_3DVector[4];
		
		for(i=0; i<4; i++)
			curCone[i] = new JGL_3DVector();
		
		tShoot = System.currentTimeMillis();
		dShoot = shootFrequency;
		timer = 1f;
	}
	
	
	/**
	 * Sets the shoot frequency (in millisecond).
	 * @param arg : the shoot frequency (in millisecond).
	 */
	public void setShootFrequency(long arg) {
		shootFrequency = arg;
	}
	
	
	/**
	 * Updates the AI behavior.
	 */
	public void update() {
		

		timer = JGL_Time.getTimer();
		
		int i;
		boolean attackOk = false;
		float targetDist2 = Float.POSITIVE_INFINITY;
		float td;
		
		//if (t.getOrientation().x>60f) t.getOrientation().x = 60f;
		
		for(i = 0; i<4; i++) {
			JGL_Math.vector_fastYXrotate(viewCone[i], t.getOrientation().x, t.getOrientation().y, curCone[i]);
			JGL_Math.vector_add(curCone[i], t.getFirePosition(), curCone[i]);
		}
		
		for (i=0; i<World.map.characters.size(); i++) {
			Entity ent = (Entity)World.map.characters.get(i);
			if (ent!=t && ent.getTeam()!=t.getTeam() && ent.getTeam()>=0) {
				td = JGL_Math.vector_squareDistance(t.getFirePosition(), ent.getPosition());
				if (td<targetDist2) {
					if (Util4AI.isInCone(ent.getPosition(), t.getFirePosition(), curCone)) {
						Tracer.setTracePrecision(Tracer.FACE_PRECISION);
						if (!World.map.intersect(t.getFirePosition(), ent.getPosition())) {
							attackOk = true;
							target = ent;
							targetDist2 = td;
						}
						Tracer.setTracePrecision(Tracer.VOLUME_PRECISION);
					}
				}
			}
		}
		
		
		if (attackOk) {
			if (behave!=ATTACK) {
				relativePosOld.assign(0f, 0f, 0f);
				tShoot = System.currentTimeMillis();
				dShoot = shootFrequency;
			}
			behave = ATTACK;
		}
		else {
			if (behave!=SURVEY) {
				if (ay[curPoint]<=t.getOrientation().y)
					turnSideH = TURN_LEFT;
				else
					turnSideH = TURN_RIGHT;
				
				if (ax[curPoint]>=t.getOrientation().x)
					turnSideV = TURN_UP;
				else
					turnSideV = TURN_DOWN;
				
				turnStepH = midTurnStep;
				turnStepV = midTurnStep;
			}
			behave = SURVEY;
		}
		
		if (behave==SURVEY) survey();
		if (behave==ATTACK) attack();
		
	}
	
	
	/**
	 * Provides the SURVEY behavior.
	 */
	private void survey() {
		
		float rx = 0f;
		float ry = 0f;
		
		// Test reaching the current point
		if (t.getOrientation().x==ax[curPoint] && t.getOrientation().y==ay[curPoint]) {
			curPoint++;
			if (curPoint>=ax.length)
				curPoint = 0;
			
			if (ay[curPoint]<t.getOrientation().y)
				turnSideH = TURN_LEFT;
			else
				turnSideH = TURN_RIGHT;
			
			if (ax[curPoint]<t.getOrientation().x)
				turnSideV = TURN_DOWN;
			else
				turnSideV = TURN_UP;
			
			turnStepH = midTurnStep;
			turnStepV = midTurnStep;
		}
		
		// Check the horizontal rotation
		if ( turnSideH==TURN_LEFT ) {
			if (ay[curPoint]>=t.getOrientation().y) {
				t.getOrientation().y = ay[curPoint];
				ry = 0f;
			}
			else
				ry = -turnStepH * timer;
		}
		if ( turnSideH==TURN_RIGHT ) {
			if (ay[curPoint]<=t.getOrientation().y) {
				t.getOrientation().y = ay[curPoint];
				ry = 0f;
			}
			else
				ry = turnStepH * timer;
		}
		
		// Check the vertical rotation
		if ( turnSideV==TURN_UP ) {
			if (ax[curPoint]<=t.getOrientation().x) {
				t.getOrientation().x = ax[curPoint];
				rx = 0f;
			}
			else
				rx = turnStepV * timer;
		}
		if ( turnSideV==TURN_DOWN ) {
			if (ax[curPoint]>=t.getOrientation().x) {
				t.getOrientation().x = ax[curPoint];
				rx = 0f;
			}
			else
				rx = -turnStepV * timer;
		}
		
		t.increaseAngles(rx, ry);
	}
	
	
	/**
	 * Provides the ATTACK behavior.
	 */
	private void attack() {
		
		float dist;
		float ax, ay;
		boolean reset = false;
		
		// Check the shoot
		JGL_Math.vector_subtract(target.getPosition(), t.getFirePosition(), relativePos);
		dist = relativePos.norm();
		JGL_3DVector angles = t.getOrientation();
		JGL_Math.vector_fastYXrotate(angles.x, angles.y, hDir);
		if (JGL_Math.vector_dotProduct(hDir, relativePos) > dist * ratio4shoot) shoot();
		
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
		
		t.increaseAngles(ax, ay);
	}
	
	
	/**
	 * Make the turret shoot.
	 */
	private void shoot() {
		
		if (dShoot>=shootFrequency) {
			t.shoot();
			tShoot = System.currentTimeMillis();
		}
		dShoot = System.currentTimeMillis() - tShoot;
	}
	
	
}
