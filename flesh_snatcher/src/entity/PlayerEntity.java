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

package entity;

import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import jglcore.JGL_Time;
import phys.Motion;
import phys.Mover;
import phys.Mover_gravity;
import phys.Shape;
import phys.Shape_aabb;
import phys.Trace;
import java.util.Vector;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.math.Quaternion;



public final class PlayerEntity implements Entity {
	
	private static float SHOOT_MAX_ANGLE = 10f;
	private static float WALK_ANGLE = 2f;
	
	private int state;
	
	private Weapon weapon;
	private int team;
	private float lifeMax;
	private float life;
	private float dam;
	
	private Node node;
	
	private boolean collidable;
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	
	private JGL_3DVector angles;
	private static Quaternion qx = new Quaternion();
	private static Quaternion qy = new Quaternion();
	private static Quaternion qz = new Quaternion();
	
	private float shootAngle;
	private float shakeScreenX;
	private float shakeScreenZ;
	private JGL_3DVector way;
	private int forward;
	private int asides;
	private float diag;
	private float walk;
	
	private Vector inventory;
	
	
	public PlayerEntity(float posX, float posY, float posZ, float angleX, float angleY, float angleZ, Node node, 
						Motion motion) {
		
		state = ACTIVE;
		
		weapon = null;
		team = 0;
		life = lifeMax = 100f;
		dam = 0f;
		
		this.node = node;
		
		collidable = true;
		cmover = new Mover_gravity(new JGL_3DVector());
		cmover.setSpeed(1.5f);
		cshape = new Shape_aabb(new JGL_3DVector(posX, posY, posZ), new JGL_3DVector(-2.9f, -15f, -2.9f), new JGL_3DVector(2.9f, 2f, 2.9f));
		cmotion = motion;
		
		angles = new JGL_3DVector(angleX, angleY, angleZ);
		shootAngle = 0f;
		shakeScreenX = 0f;
		shakeScreenZ = 0f;
		way = new JGL_3DVector();
		forward = 0;
		asides = 0;
		diag = JGL_Math.cos(45f);
		walk = 0f;
		
		inventory = new Vector();
	}
	
	
	public Vector getInventory() {
		return inventory;
	}
	

	@Override
	public String getID() {
		return "";
	}

	@Override
	public Shape getCShape() {
		// TODO Auto-generated method stub
		return cshape;
	}

	@Override
	public Motion getCollider() {
		// TODO Auto-generated method stub
		return cmotion;
	}

	@Override
	public float getDamage() {
		// TODO Auto-generated method stub
		return dam;
	}

	@Override
	public float getLife() {
		// TODO Auto-generated method stub
		return life;
	}

	@Override
	public Mover getMover() {
		// TODO Auto-generated method stub
		return cmover;
	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return node;
	}

	@Override
	public JGL_3DVector getOrientation() {
		// TODO Auto-generated method stub
		return angles;
	}

	@Override
	public JGL_3DVector getPosition() {
		// TODO Auto-generated method stub
		return cshape.getPosition();
	}

	@Override
	public int getTeam() {
		// TODO Auto-generated method stub
		return team;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return state == ACTIVE;
	}

	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return collidable;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return state == DEAD;
	}

	@Override
	public boolean isDying() {
		// TODO Auto-generated method stub
		return state == DYING;
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		state = ACTIVE;
	}

	@Override
	public void setCShape(Shape arg) {
		// TODO Auto-generated method stub
		cshape = arg;
	}

	@Override
	public void setCollidable(boolean arg) {
		// TODO Auto-generated method stub
		collidable = arg;
	}

	@Override
	public void setCollider(Motion arg) {
		// TODO Auto-generated method stub
		cmotion = arg;
	}

	@Override
	public void setDamage(float arg) {
		// TODO Auto-generated method stub
		dam = arg;
	}

	@Override
	public void setDead() {
		// TODO Auto-generated method stub
		state = DEAD;
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
	}

	@Override
	public void setLife(float arg) {
		// TODO Auto-generated method stub
		if (arg>lifeMax)
			arg = lifeMax;
		life = arg;
	}

	@Override
	public void setMover(Mover arg) {
		// TODO Auto-generated method stub
		cmover = arg;
	}

	@Override
	public void setSpeed(float arg) {
		// TODO Auto-generated method stub
		cmover.setSpeed(arg);
	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub
		team = arg;
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		life -= entity.getDamage();
		if (life<=0f)
			setDying();
		return true;
	}
	
	
	/**
	 * Go forward/backward.
	 */
	public void setForwardMove(int arg) {
		if (isActive()) {
			forward = arg;
			setMove();
		}
	}
	
	
	
	/**
	 * Go left/right.
	 */
	public void setSideMove(int arg) {
		if (isActive()) {
			asides = arg;
			setMove();
		}
	}
	
	
	// Updates the base direction
	private void setMove() {

		if (forward==1) {
			if (asides==1) {
				way.x = diag;
				way.z = -diag;
			}
			else if (asides==0) {
				way.x = 0f;
				way.z = -1f;
			}
			else {
				way.x = -diag;
				way.z = -diag;
			}
			
		}
		else if (forward==0) {
			if (asides==1) {
				way.x = 1f;
				way.z = 0f;
			}
			else if (asides==0) {
				way.x = 0f;
				way.z = 0f;
			}
			else {
				way.x = -1f;
				way.z = 0f;
			}
		}
		else {
			if (asides==1) {
				way.x = diag;
				way.z = diag;
			}
			else if (asides==0) {
				way.x = 0f;
				way.z = 1f;
			}
			else {
				way.x = -diag;
				way.z = diag;
			}
		}
	}
	
	/**
	 * Increase the entity rotation on X and Y axis.
	 * 
	 * @param deltaX : the X angle to add
	 * @param deltaY : the Y angle to add
	 */
	public void increaseAngles(float deltaX, float deltaY) {
		
		angles.x += deltaX;
		angles.y += deltaY;
		
		if (angles.x>90f) angles.x = 90f;
		if (angles.x<-90f) angles.x = -90f;
		while (angles.y>360f) angles.y -= 360f;
		while (angles.y<-360f) angles.y += 360f;
	}
	
	public void jump() {
			((Mover_gravity)cmover).jump();
	}
	
	/**
	 * Affiliates the specified weapon to this Human.
	 * 
	 * @param _weapon : the weapon to affiliate to this Human
	 */
	public void linkWeapon(Weapon _weapon) {
		weapon = _weapon;
		weapon.setOwner(this);
	}
	
	
	/**
	 * Returns the linked weapon, or null otherwise.
	 * 
	 * @return the linked weapon, or null otherwise
	 */
	public Weapon getWeapon() {
		return weapon;
	}
	
	
	/**
	 * Makes the entity shoot if a weapon is linked, nothing otherwise.
	 */
	public void shoot() {
		if (weapon!=null && state==ACTIVE && shootAngle==0f) {
			weapon.shoot(getPosition());
			shootAngle = SHOOT_MAX_ANGLE;
			shakeScreenX = 1f;
		}
	}
	
	
	public float getShakeScreenX() {
		return shakeScreenX;
	}
	
	public float getShakeScreenZ() {
		return shakeScreenZ;
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		JGL_Math.vector_fastYXrotate(way, 0f, angles.y, cmover.getVelocity());
		cmover.update();
		
		if (shootAngle>0) shootAngle -= 1.25f * JGL_Time.getTimer();
		if (shootAngle<0f) shootAngle = 0f;
		
		if ( (way.x!=0f || way.z!=0f) && !((Mover_gravity)cmover).isJumping()) 
			walk += (JGL_Time.getTimePerFrame() * 400f);
		else 
			walk = 0f;
		while (walk>360f) walk -= 360f;
		angles.z = JGL_Math.sin(walk) * WALK_ANGLE;
		
		if (shakeScreenX>0) {
			shakeScreenX -= 0.1f * JGL_Time.getTimer();
			if (shakeScreenX<0f) shakeScreenX = 0f;
		}
		
		if (isDying())
			setDead();
	}
	
	@Override
	public void synchronizeNode() {
		node.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		
		//	 X axis Quaternion compute
		float angle = (angles.x + shootAngle) * 0.5f;
		qx.set(JGL_Math.sin(angle), 0f, 0f, JGL_Math.cos(angle));
		
		//	 Y axis Quaternion compute
		angle = angles.y * 0.5f;
		qy.set(0f, -JGL_Math.sin(angle), 0f, JGL_Math.cos(angle));
		
		//	 Z axis Quaternion compute
		angle = angles.z * 0.5f;
		qz.set(0f, 0f, JGL_Math.sin(angle), JGL_Math.cos(angle));
		
		node.setLocalRotation(qy.multLocal(qx.multLocal(qz)));
		node.updateGeometricState(JGL_Time.getTimePerFrame(), true);
	}
	
	@Override
	public void render() {
		DisplaySystem.getDisplaySystem().getRenderer().draw(node);
	}
	
	public Object clone() {
		return new PlayerEntity(getPosition().x, getPosition().y, getPosition().z, 
								getOrientation().x, getOrientation().y, getOrientation().z, node, 
								(Motion)cmotion.clone());
	}
}
