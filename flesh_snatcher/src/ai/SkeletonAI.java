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

import phys.Shape_aabb;
import phys.Trace;
import jglcore.JGL_Time;
import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import entity.Entity;
import entity.Skeleton01;
import world.World;


/**
 * Class providing automatic behaviors to the Human entities.
 * 
 * @author Nicolas Devere
 *
 */
public final class SkeletonAI {
	
	//-------- STATIC
	
	// Behave parameters
	private static int SURVEY 	= 0;
	private static int ATTACK 	= 1;
	private int behave;
	
	// SURVEY sub-behave
	private static int SEARCH 	= 2;
	private static int MOVE 	= 3;
	private int surveyBehave;
	
	// FOCUS Parameters
	private static int TURN_LEFT 	= 4;
	private static int TURN_RIGHT 	= 5;
	private static int TURN_UP 		= 6;
	private static int TURN_DOWN 	= 7;
	private static float maxTurnStep = 4f;
	private static float midTurnStep = 2f;
	private static float minTurnStep = 0.25f;
	private static float ratio4focus = 0.9996f;
	private static float pointOffset = 1f;
	
	// TARGETTING Parameters
	private static float maxDistance2 = 1000000f;
	private static float minDistShoot2 = 1600f;
	private static float move4reset = 2f;
	
	// SHOOT Parameters
	private static float ratio4shoot = 0.9995f;
	
	// Vision cone
	private static JGL_3DVector[] viewCone = new JGL_3DVector[] {
		new JGL_3DVector(-10f, 10f, -10f), //left - up
		new JGL_3DVector(-10f, -10f, -10f), //left - down
		new JGL_3DVector(10f, -10f, -10f), //right - down
		new JGL_3DVector(10f, 10f, -10f) //right - up
	};
	
	// Map collision for visibility
	private static Trace impact = new Trace();
	
	// Optimization
	private static JGL_3DVector s_v = new JGL_3DVector();
	
	
	
	//-------- INSTANCE
	
	private Skeleton01 s;
	
	// List of surveyed points
	private Shape_aabb[] points;
	private int curPoint;
	
	// Focus variables
	private JGL_3DVector relativePosOld;
	private JGL_3DVector relativePos;
	private JGL_3DVector hDir;
	private Entity target;
	private float targetDist2;
	private float turnStepH;
	private float turnStepV;
	private int turnSideH;
	private int turnSideV;
	private boolean runWhileShoot;
	private long shootFrequency;
	
	// Vision cone
	private static JGL_3DVector[] curCone;
	
	// Time variables
	private long tShoot;
	private long dShoot;
	private float timer;
	
	
	/**
	 * Constructs a AI associated to the specified skeleton, given an array of trajectory points.
	 * 
	 * @param skeleton : the skeleton to associate
	 * @param keyPoints : the trajectory points
	 * @param _shootFrequency : the shoot frequency in milliseconds
	 * @param _runWhileShoot : true makes the skeleton run to the target while shooting
	 */
	public SkeletonAI(Skeleton01 skeleton, JGL_3DVector[] keyPoints, long _shootFrequency, boolean _runWhileShoot) {
		
		int i;
		
		behave = SURVEY;
		surveyBehave = SEARCH;
		s = skeleton;
		runWhileShoot = _runWhileShoot;
		
		JGL_3DVector min = new JGL_3DVector(-pointOffset, -pointOffset, -pointOffset);
		JGL_3DVector max = new JGL_3DVector(pointOffset, pointOffset, pointOffset);
		points = new Shape_aabb[keyPoints.length];
		
		for (i=0; i<keyPoints.length; i++) {
			points[i] = new Shape_aabb(keyPoints[i], min, max);
		}
		curPoint = 0;
		
		target = null;
		hDir = new JGL_3DVector();
		relativePos = new JGL_3DVector();
		relativePosOld = new JGL_3DVector();
		turnStepH = turnStepV = maxTurnStep;
		turnSideH = turnSideV = -1;
		shootFrequency = _shootFrequency;
		
		curCone = new JGL_3DVector[4];
		
		for(i=0; i<4; i++)
			curCone[i] = new JGL_3DVector();
		
		tShoot = System.currentTimeMillis();
		dShoot = shootFrequency;
		timer = 1f;
	}
	
	
	/**
	 * Updates the AI behavior.
	 */
	public void update() {
		
		timer = JGL_Time.getTimer();
		
		int i;
		boolean attackOk = false;
		targetDist2 = Float.MAX_VALUE;
		float td;
		
		if (s.getOrientation().x>60f) s.getOrientation().x = 60f;
		
		for(i = 0; i<4; i++) {
			JGL_Math.vector_fastYXrotate(viewCone[i], s.getOrientation().x, s.getOrientation().y, curCone[i]);
			JGL_Math.vector_add(curCone[i], s.getPosition(), curCone[i]);
		}
		
		for (i=0; i<World.map.characters.size(); i++) {
			Entity ent = (Entity)World.map.characters.get(i);
			if (ent!=s && ent.getTeam()!=s.getTeam() && ent.getTeam()>=0) {
				td = JGL_Math.vector_squareDistance(s.getPosition(), ent.getPosition());
				if (td<maxDistance2 && td<targetDist2) {
					if (Util4AI.isInCone(ent.getPosition(), s.getPosition(), curCone)) {
						if (!World.map.intersect(s.getPosition(), ent.getPosition())) {
							attackOk = true;
							target = ent;
							targetDist2 = td;
						}
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
				turnStepH = midTurnStep;
				surveyBehave = SEARCH;
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
		
		float ay;
		
		// Test reaching the current point
		JGL_Math.vector_add(s.getMover().getMove(), s.getPosition(), s_v);
		impact.reset(s.getCShape(), s.getPosition(), s_v);
		if (points[curPoint].trace(impact)) {
			curPoint++;
			if (curPoint>=points.length)
				curPoint = 0;
			turnStepH = midTurnStep;
			surveyBehave = SEARCH;
		}
		
		
		JGL_Math.vector_subtract(points[curPoint].getPosition(), s.getPosition(), relativePos);
		float dist = (float)Math.sqrt( (relativePos.x * relativePos.x) + (relativePos.z * relativePos.z) );
		JGL_Math.vector_fastYXrotate(0f, s.getOrientation().y, hDir);
		
		if ( (hDir.x * relativePos.x) + (hDir.z * relativePos.z) > dist * ratio4focus) {
			if (turnStepH>minTurnStep)
				turnStepH *= 0.5f;
			surveyBehave = MOVE;
		}
		else if (turnStepH<maxTurnStep)
				turnStepH *= 2f;
		
		// Check the horizontal rotation
		if ( (hDir.z * relativePos.x) - (hDir.x * relativePos.z) >= 0f )
			ay = -turnStepH * timer;
		else
			ay = turnStepH * timer;
		
		s.increaseAngles(0f, ay);
		
		if (surveyBehave==MOVE)
			s.setForwardMove(1);
		else
			s.setForwardMove(0);
	}
	
	
	/**
	 * Provides the ATTACK behavior.
	 */
	private void attack() {
		
		float dist;
		float ax, ay;
		boolean reset = false;
		
		if (runWhileShoot && targetDist2>minDistShoot2)
			s.setForwardMove(1);
		else
			s.setForwardMove(0);
		
		// Check the shoot
		JGL_Math.vector_subtract(target.getPosition(), s.getPosition(), relativePos);
		dist = relativePos.norm();
		JGL_3DVector angles = s.getOrientation();
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
		
		s.increaseAngles(ax, ay);
	}
	
	
	/**
	 * Make the human shoot.
	 */
	private void shoot() {
		
		if (dShoot>=shootFrequency) {
			s.shoot();
			tShoot = System.currentTimeMillis();
		}
		dShoot = System.currentTimeMillis() - tShoot;
	}
	
}
