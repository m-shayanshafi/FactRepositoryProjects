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
import world.World;
import main.Player;
import entity.Entity;
import entity.Shoot;
import entity.Zombie01;

public final class ZombieAIsimple implements ZombieAI {
	
	
	//-------- STATIC
	
	// FOCUS Parameters
	private static int TURN_LEFT 	= 4;
	private static int TURN_RIGHT 	= 5;
	private static float maxTurnStep = 4f;
	//private static float midTurnStep = 2f;
	private static float minTurnStep = 0.25f;
	
	// TARGETTING Parameters
	private static float move4reset = 2f;
	
	
	// Shoot avoid parameters
	private static float avoidSphereDistance2 = 2500f;
	private static float avoidShooterDistance2 = 2500f;
	
	
	// Vision cone
	/*private static JGL_3DVector[] viewCone = new JGL_3DVector[] {
		new JGL_3DVector(-10f, 10f, -10f), //left - up
		new JGL_3DVector(-10f, -10f, -10f), //left - down
		new JGL_3DVector(10f, -10f, -10f), //right - down
		new JGL_3DVector(10f, 10f, -10f) //right - up
	};*/
	
	
	//-------- INSTANCE
	
	private Zombie01 z;
	
	// Focus variables
	private JGL_3DVector relativePosOld;
	private JGL_3DVector relativePos;
	private JGL_3DVector hDir;
	//private Entity target;
	private float turnStepH;
	private int turnSideH;
	
	// Shoot avoid
	private JGL_3DVector sDir;
	
	// Time variables
	private float timer;
	
	
	/**
	 * Constructs a AI associated to the specified zombie.
	 * 
	 * @param zombie : the zombie to associate
	 */
	public ZombieAIsimple(Zombie01 zombie) {
		
		z = zombie;
		
		//target = Player.entity;
		hDir = new JGL_3DVector();
		relativePos = new JGL_3DVector();
		relativePosOld = new JGL_3DVector();
		
		sDir = new JGL_3DVector();
		
		timer = 1f;
	}
	
	
	/**
	 * Updates the AI behavior.
	 */
	public void update() {
		
		timer = JGL_Time.getTimer();

		float ay;
		boolean reset = false;
		
		z.setForwardMove(1);
		
		// Shoot avoid management
		boolean avoidShoot = false;
		Entity shoot, shootTarget = null;
		float td, ed;
		float shootDist2 = Float.MAX_VALUE;
		for (int i=0; i<World.map.shoots.size(); i++) {
			shoot = (Shoot)World.map.shoots.get(i);
			if (shoot!=z && shoot.getTeam()!=z.getTeam() && shoot.getTeam()>=0) {
				td = JGL_Math.vector_squareDistance(z.getPosition(), shoot.getPosition());
				ed = JGL_Math.vector_squareDistance(z.getPosition(), ((Shoot)shoot).getWeapon().getOwner().getPosition());
				if (td<avoidSphereDistance2 && td<shootDist2 && ed>avoidShooterDistance2) {
					if (!World.map.intersect(z.getPosition(), shoot.getPosition())) {
						avoidShoot = true;
						shootTarget = shoot;
						shootDist2 = td;
					}
				}
			}
		}
		z.setSideMove(0);
		if (avoidShoot) {
			JGL_Math.vector_subtract(shootTarget.getPosition(), z.getPosition(), relativePos);
			JGL_3DVector angles = z.getOrientation();
			JGL_Math.vector_fastYXrotate(angles.x, angles.y, sDir);
			
			z.setForwardMove(0);
			if ( (sDir.z * relativePos.x) - (sDir.x * relativePos.z) >= 0f )
				z.setSideMove(1);
			else
				z.setSideMove(-1);
		}
		
		// Check the shoot
		JGL_Math.vector_subtract(Player.entity.getPosition(), z.getPosition(), relativePos);
		JGL_3DVector angles = z.getOrientation();
		JGL_Math.vector_fastYXrotate(angles.x, angles.y, hDir);
		
		// Check to reset the movement
		if (Math.abs(relativePos.x - relativePosOld.x)>move4reset || 
			Math.abs(relativePos.y - relativePosOld.y)>move4reset || 
			Math.abs(relativePos.z - relativePosOld.z)>move4reset) {
			reset = true;
			turnStepH = maxTurnStep;
			relativePosOld.assign(relativePos);
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
		
		z.increaseAngles(0f, ay);
		
		// Check to jump
		if (!z.getCollider().getTrace().dummy && 
			z.getCollider().getTrace().isImpact() && 
			(Math.abs(z.getCollider().getTrace().correction.normal.y)<0.00005f || 
			z.getCollider().getTrace().fractionImpact<=0.00005f))
			z.jump();
	}
	
	
	public ZombieAI clone(Zombie01 arg) {
		return new ZombieAIsimple(arg);
	}
	
}
