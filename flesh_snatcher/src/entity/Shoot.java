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

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

import phys.Motion;
import phys.Motion_stop;
import phys.Mover;
import phys.Shape;
import phys.Trace;
import world.World;
import jglcore.JGL_3DVector;
import jglcore.JGL_Math;


/**
 * Class representing the shoot entities.
 * 
 * @author Nicolas Devere
 *
 */
public final class Shoot implements Entity {
	
	private static long LIFETIME = 5000l;
	
	private int state;
	
	private boolean displayable;
	
	private Node node;
	private Explosion exp;
	
	private Weapon weapon;
	
	private boolean collidable;
	private Shape cshape;
	private Mover cmover;
	private Motion cmotion;
	
	private float sp;
	private float lif;
	private float lifMax;
	private float dam;
	private int team;
	
	private JGL_3DVector angles;
	private static Quaternion qx = new Quaternion();
	private static Quaternion qy = new Quaternion();
	private static Quaternion qz = new Quaternion();
	
	private long starttime;
	
	
	/**
	 * Constructs a Shoot entity.
	 * 
	 * @param model : the shoot 3D model
	 * @param explodeModel : the shoot explosion model
	 * @param collisionShape : the shoot collision shape
	 * @param mover : the shoot mover
	 * @param maxLife : the shoot maximum life
	 * @param damage : the damage that inflicts the shoot's contact
	 * @param speed : the shoot speed
	 */
	public Shoot(	Node model, Explosion explosion, Shape collisionShape, Mover mover, 
					float maxLife, float damage, float speed) {
		
		angles = new JGL_3DVector();
		
		node = model;
		exp = explosion;
		
		weapon = null;
		
		cshape = collisionShape;
		cmover = mover;
		cmotion = new Motion_stop();
		
		sp = speed;
		lifMax = maxLife;
		dam = damage;
		team = -1;
		
		reset();
	}
	
	/**
	 * Resets the shoot ready to start.
	 */
	public void reset() {
		
		setActive();
		
		lif = lifMax;
		displayable = true;
		collidable = true;
		cmover.setSpeed(sp);
		starttime = System.currentTimeMillis();
	}
	
	
	/**
	 * Resets the shoot with a start position and an orientation.
	 * 
	 * @param position : the start position
	 * @param orientation : the orientation angles
	 * @param shootTeam : the team number
	 */
	public void start(JGL_3DVector position, JGL_3DVector orientation, int shootTeam) {
		
		reset();
		
		team = shootTeam;
		cshape.getPosition().assign(position);
		angles.assign(orientation);
		JGL_Math.vector_fastYXrotate(angles.x, angles.y, cmover.getVelocity());
	}
	
	
	/**
	 * Resets the shoot with a start position and a normalized velocity.
	 * 
	 * @param position : the start position
	 * @param velocity : the normalized velocity
	 * @param shootTeam : the team number
	 */
	public void start2(JGL_3DVector position, JGL_3DVector velocity, int shootTeam) {
		
		reset();
		
		team = shootTeam;
		cshape.getPosition().assign(position);
		angles.assign(0f, 0f, 0f);
		cmover.getVelocity().assign(velocity);
	}
	
	
	/**
	 * Sets the new weapon which owns the shoot.
	 * 
	 * @param arg : the new weapon which owns the shoot
	 */
	public void setWeapon(Weapon arg) {
		weapon = arg;
	}
	
	
	/**
	 * returns the weapon which owns the shoot.
	 * 
	 * @return the weapon which owns the shoot
	 */
	public Weapon getWeapon() {
		return weapon;
	}
	

	@Override
	public String getID() {
		return "";
	}
	

	/**
	 * Applies the reaction to the contact of the specified entity.
	 * @param entity : the entity witch made the contact
	 */
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		return true;
	}

	
	/**
	 * Returns the collision shape.
	 * @return the collision shape
	 */
	public Shape getCShape() {
		// TODO Auto-generated method stub
		return cshape;
	}
	
	
	/**
	 * Returns the entity mover.
	 * @return the entity mover
	 */
	public Mover getMover() {
		return cmover;
	}

	
	/**
	 * Returns the collision process object.
	 * @return the collision process object
	 */
	public Motion getCollider() {
		// TODO Auto-generated method stub
		return cmotion;
	}

	
	/**
	 * Returns the damage that the entity inflicts.
	 * @return the damage that the entity inflicts
	 */
	public float getDamage() {
		// TODO Auto-generated method stub
		return dam;
	}

	
	/**
	 * Returns the current life of the entity.
	 * @return the current life of the entity
	 */
	public float getLife() {
		// TODO Auto-generated method stub
		return lif;
	}

	
	/**
	 * Returns the 3D movable model.
	 * @return the 3D movable model
	 */
	public Node getNode() {
		// TODO Auto-generated method stub
		return node;
	}

	
	/**
	 * Returns the entity orientation (rotation angles on X, Y and Z axis, in degrees).
	 * @return the entity orientation
	 */
	public JGL_3DVector getOrientation() {
		// TODO Auto-generated method stub
		return angles;
	}

	
	/**
	 * Returns the entity position.
	 * @return the entity position
	 */
	public JGL_3DVector getPosition() {
		// TODO Auto-generated method stub
		return cshape.getPosition();
	}

	
	/**
	 * Returns the identifiant of the entity's team.
	 * @return the identifiant of the entity's team
	 */
	public int getTeam() {
		// TODO Auto-generated method stub
		return team;
	}

	
	/**
	 * Returns if the entity is active.
	 * @return if the entity is active
	 */
	public boolean isActive() {
		// TODO Auto-generated method stub
		return state == ACTIVE;
	}

	
	/**
	 * Returns if the entity is collidable or not.
	 * @return if the entity is collidable or not
	 */
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return collidable;
	}

	
	/**
	 * Returns if the <code>display(JGL_3DVector eye)</code> method 
	 * displays the entity or not.
	 * @return if the entity is displayable
	 */
	public boolean isDisplayable() {
		// TODO Auto-generated method stub
		return displayable;
	}

	
	/**
	 * Returns if the entity is exploding.
	 * @return if the entity is exploding
	 */
	public boolean isDying() {
		// TODO Auto-generated method stub
		return state == DYING;
	}

	
	/**
	 * Returns if the entity is finished.
	 * @return if the entity is finished
	 */
	public boolean isDead() {
		// TODO Auto-generated method stub
		return state == DEAD;
	}

	
	
	/**
	 * Sets the current life of the entity.
	 */
	public void setLife(float arg) {
		lif = arg;
	}
	
	/**
	 * Sets the damage that the entity inflicts.
	 */
	public void setDamage(float arg) {
		dam = arg;
	}
	
	/**
	 * Sets the identifiant of the entity's team.
	 */
	public void setTeam(int arg) {
		team = arg;
	}
	
	
	public void setCShape(Shape arg) {
		cshape = arg;
	}
	
	public void setMover(Mover arg) {
		cmover = arg;
	}
	
	public void setCollider(Motion arg) {
		cmotion = arg;
	}
	
	
	/**
	 * Sets the speed of the entity.
	 */
	public void setSpeed(float arg) {
		sp = arg;
	}

	
	/**
	 * Sets the entity active : it will be updated.
	 */
	public void setActive() {
		// TODO Auto-generated method stub
		state = ACTIVE;
	}

	
	/**
	 * Sets if the entity is collidable or not (true by default).
	 * @param arg : true or false
	 */
	public void setCollidable(boolean arg) {
		// TODO Auto-generated method stub
		collidable = arg;
	}

	
	/**
	 * Sets if the <code>display(JGL_3DVector eye)</code> method 
	 * displays the entity or not (true by default).
	 * @param arg : true or false
	 */
	public void setDisplayable(boolean arg) {
		// TODO Auto-generated method stub
		displayable = arg;
	}

	
	/**
	 * Sets the entity exploding.
	 */
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
	}

	
	/**
	 * Sets the entity finished : ready to be removed.
	 */
	public void setDead() {
		// TODO Auto-generated method stub
		state = DEAD;
		exp.reset(getPosition().x, getPosition().y, getPosition().z);
		World.map.addObject(exp);
	}
	
	
	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub
	}

	
	/**
	 * Updates the situation of the entity.
	 */
	public void update() {
		// TODO Auto-generated method stub
		if (state==DEAD)
			return;
		
		if (state==ACTIVE){
			cmover.update();
			if ((System.currentTimeMillis()-starttime)>LIFETIME)
				setDead();
		}
		else if (state==DYING) {
			cmover.setSpeed(0f);
			setDead();
		}
	}
	
	@Override
	public void render() {
		
		node.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		
		//	 X axis Quaternion compute
		float angle = angles.x * 0.5f;
		qx.set(JGL_Math.sin(angle), 0f, 0f, JGL_Math.cos(angle));
		
		//	 Y axis Quaternion compute
		angle = angles.y * 0.5f;
		qy.set(0f, -JGL_Math.sin(angle), 0f, JGL_Math.cos(angle));
		
		//	 Z axis Quaternion compute
		angle = angles.z * 0.5f;
		qz.set(0f, 0f, JGL_Math.sin(angle), JGL_Math.cos(angle));
		
		node.setLocalRotation(qy.multLocal(qx.multLocal(qz)));
		
		node.updateGeometricState(0f, true);
		DisplaySystem.getDisplaySystem().getRenderer().draw(node);
	}
	
	
	public Object clone() {
		return new Shoot(node, (Explosion)exp.clone(), (Shape)cshape.clone(), (Mover)cmover.clone(), lifMax, dam, sp);
	}
	
	
	
}
