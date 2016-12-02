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

import java.util.Vector;

import jglcore.JGL_Math;
import jglcore.JGL_3DVector;
import jglcore.JGL_Time;
import entity.Entity;
import entity.Zombie01;
import phys.Shape_aabb;
import phys.Trace;
import world.World;
import main.Player;


public class ZombieAIpath implements ZombieAI {
	
	//-------- STATIC
	
	// BEHAVE Parameters
	private static int FOLLOW_PATH		= 0;
	private static int FOLLOW_PLAYER	= 1;
	private static int FOLLOW_NOTHING	= 2;
	private static int SEARCH			= 3;
	private static int MOVE				= 4;
	
	// FOCUS Parameters
	private static float maxTurnStep = 4f;
	private static float midTurnStep = 1f;
	private static float minTurnStep = 0.25f;
	private static float ratio4focus = 0.995f;
	
	// TARGETTING Parameters
	private static float pointOffset = 0.1f;
	
	// Character avoid parameters
	private static float avoidSphereDistance2 = 121f;
	
	// Path points collision
	private static Trace impact = new Trace();
	
	// Optimization
	private static JGL_3DVector s_v = new JGL_3DVector();
	
	//-------- INSTANCE
	
	private int behave;
	private int pathBehave;
	private ZombieAIsimple simpleAI;
	
	private Zombie01 z;
	private PathGraph graph;
	private Vector path;
	private int iPoint;
	private Shape_aabb point;
	
	// Focus variables
	private JGL_3DVector relativePos;
	private JGL_3DVector hDir;
	private float turnStepH;
	
	// Character avoid
	private JGL_3DVector sDir;
	
	// Time variables
	private float timer;
	
	
	
	public ZombieAIpath(Zombie01 zombie, PathGraph pathgraph) {
		
		z = zombie;
		graph = pathgraph;
		path = new Vector();
		iPoint = 0;
		point = new Shape_aabb(	new JGL_3DVector(), 
								new JGL_3DVector(-pointOffset, -pointOffset, -pointOffset), 
								new JGL_3DVector(pointOffset, pointOffset, pointOffset));
		simpleAI = new ZombieAIsimple(z);
		behave = FOLLOW_PLAYER;
		pathBehave = SEARCH;
		
		hDir = new JGL_3DVector();
		relativePos = new JGL_3DVector();
		turnStepH = maxTurnStep;
		sDir = new JGL_3DVector();
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		if (behave==FOLLOW_PLAYER)
			followPlayer();
		else if (behave==FOLLOW_PATH)
			followPath();
		else if (behave==FOLLOW_NOTHING)
			followNothing();
	}
	
	
	private void followPlayer() {
		
		simpleAI.update();
		if (World.map.intersect(z.getPosition(), Player.entity.getPosition()))
			if (initPath())
				behave = FOLLOW_PATH;
			else
				behave = FOLLOW_NOTHING;
	}
	
	
	
	private void followPath() {
		
		timer = JGL_Time.getTimer();

		float ay;
		
		
		if (!World.map.intersect(z.getPosition(), Player.entity.getPosition())) {
			behave = FOLLOW_PLAYER;
			return;
		}
		
		
		// Character avoid management
		boolean avoidCharac = false;
		Entity charac, characTarget = null;
		float td;
		float characDist2 = Float.POSITIVE_INFINITY;
		for (int i=0; i<World.map.characters.size(); i++) {
			charac = (Entity)World.map.characters.get(i);
			if (charac!=z && charac.getTeam()==z.getTeam() && charac.getTeam()>=0) {
				td = JGL_Math.vector_squareDistance(z.getPosition(), charac.getPosition());
				if (td<avoidSphereDistance2 && td<characDist2) {
					if (!World.map.intersect(z.getPosition(), charac.getPosition())) {
						avoidCharac = true;
						characTarget = charac;
						characDist2 = td;
					}
				}
			}
		}
		z.setSideMove(0);
		if (avoidCharac) {
			JGL_Math.vector_subtract(characTarget.getPosition(), z.getPosition(), relativePos);
			JGL_3DVector angles = z.getOrientation();
			JGL_Math.vector_fastYXrotate(angles.x, angles.y, sDir);
			
			z.setForwardMove(0);
			if ( (sDir.z * relativePos.x) - (sDir.x * relativePos.z) >= 0f )
				z.setSideMove(1);
			else
				z.setSideMove(-1);
		}
		
		
		// Test reaching the current point
		JGL_Math.vector_add(z.getMover().getMove(), z.getPosition(), s_v);
		impact.reset(z.getCShape(), z.getPosition(), s_v);
		if (point.trace(impact) || impact.dummy) {
			iPoint++;
			if (iPoint<path.size())
				point.setPosition((JGL_3DVector)path.get(iPoint));
			else {
				behave = FOLLOW_NOTHING;
				return;
			}
			turnStepH = midTurnStep;
			pathBehave = SEARCH;
		}
		
		
		JGL_Math.vector_subtract(point.getPosition(), z.getPosition(), relativePos);
		float dist = (float)Math.sqrt( (relativePos.x * relativePos.x) + (relativePos.z * relativePos.z) );
		JGL_Math.vector_fastYXrotate(0f, z.getOrientation().y, hDir);
		
		if ( (hDir.x * relativePos.x) + (hDir.z * relativePos.z) > dist * ratio4focus) {
			if (turnStepH>minTurnStep)
				turnStepH *= 0.5f;
			pathBehave = MOVE;
		}
		else {
			pathBehave = SEARCH;
			if (turnStepH<maxTurnStep)
				turnStepH *= 2f;
		}
		
		// Check the horizontal rotation
		if ( (hDir.z * relativePos.x) - (hDir.x * relativePos.z) >= 0f )
			ay = -turnStepH * timer;
		else
			ay = turnStepH * timer;
		
		z.increaseAngles(0f, ay);
		
		z.setSideMove(0);
		
		if (pathBehave==MOVE)
			z.setForwardMove(1);
		else
			z.setForwardMove(0);
		
		// Check to jump
		/*if (!z.getCollider().getTrace().dummy && 
			z.getCollider().getTrace().isImpact() && 
			(Math.abs(z.getCollider().getTrace().correction.y)<0.00005f || 
			z.getCollider().getTrace().fractionImpact<=0.00005f))
			z.jump();*/
	}
	
	
	private void followNothing() {
		
		z.setForwardMove(1);
		
		// Character avoid management
		boolean avoidCharac = false;
		Entity charac, characTarget = null;
		float td;
		float characDist2 = Float.POSITIVE_INFINITY;
		for (int i=0; i<World.map.characters.size(); i++) {
			charac = (Entity)World.map.characters.get(i);
			if (charac!=z && charac.getTeam()==z.getTeam() && charac.getTeam()>=0) {
				td = JGL_Math.vector_squareDistance(z.getPosition(), charac.getPosition());
				if (td<avoidSphereDistance2 && td<characDist2) {
					if (!World.map.intersect(z.getPosition(), charac.getPosition())) {
						avoidCharac = true;
						characTarget = charac;
						characDist2 = td;
					}
				}
			}
		}
		z.setSideMove(0);
		if (avoidCharac) {
			JGL_Math.vector_subtract(characTarget.getPosition(), z.getPosition(), relativePos);
			JGL_3DVector angles = z.getOrientation();
			JGL_Math.vector_fastYXrotate(angles.x, angles.y, sDir);
			
			z.setForwardMove(0);
			if ( (sDir.z * relativePos.x) - (sDir.x * relativePos.z) >= 0f )
				z.setSideMove(1);
			else
				z.setSideMove(-1);
		}
		
		if(!World.map.intersect(z.getPosition(), Player.entity.getPosition())) {
			behave = FOLLOW_PLAYER;
			return;
		}
		if (initPath())
			behave = FOLLOW_PATH;
	}
	
	
	
	private boolean initPath() {
		
		int i1 = index(z.getPosition());
		if (i1==-1) return false;
		int i2 = index(Player.entity.getPosition());
		if (i2==-1) return false;
		
		if (i1!=i2) {
			graph.searchPathDantzig(i1, i2, path);
			iPoint = 0;
			for (int i=0; i<path.size(); i++)
				if (!World.map.intersect(z.getPosition(), (JGL_3DVector)path.get(i)))
					iPoint = i;
			
			point.setPosition((JGL_3DVector)path.get(iPoint));
			return true;
		}
		return false;
	}
	
	
	private int index(JGL_3DVector arg) {
		
		int result = -1;
		float dist = Float.POSITIVE_INFINITY;
		float dTest;
		JGL_3DVector points[] = graph.getPoints();
		
		for (int i=0; i<points.length; i++) {
			if (!World.map.intersect(arg, points[i])) {
				dTest = JGL_Math.vector_squareDistance(arg, points[i]);
				if (dTest<dist) {
					dist = dTest;
					result = i;
				}
			}
		}
		return result;
	}
	
	
	@Override
	public ZombieAI clone(Zombie01 arg) {
		// TODO Auto-generated method stub
		return new ZombieAIpath(arg, graph);
	}

}
