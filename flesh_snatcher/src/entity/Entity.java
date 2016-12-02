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

import com.jme.scene.Node;
import phys.Motion;
import phys.Mover;
import phys.Shape;
import phys.Trace;
import jglcore.JGL_3DVector;


/**
 * Interface providing manipulation methods for graphic entities 
 * that can be inserted in a map.
 * 
 * @author Nicolas Devere
 *
 */
public interface Entity {
	
	public static int ACTIVE = 1;
	public static int DYING = 2;
	public static int DEAD = 3;
	
	/**
	 * Returns the entity ID.
	 * @return the entity ID
	 */
	public String getID();
	
	/**
	 * Returns the entity position.
	 * @return the entity position
	 */
	public JGL_3DVector getPosition();
	
	/**
	 * Returns the entity orientation (rotation angles on X, Y and Z axis, in degrees).
	 * @return the entity orientation
	 */
	public JGL_3DVector getOrientation();
	
	/**
	 * Returns the current life of the entity.
	 * @return the current life of the entity
	 */
	public float getLife();
	
	/**
	 * Returns the damage that the entity inflicts.
	 * @return the damage that the entity inflicts
	 */
	public float getDamage();
	
	/**
	 * Returns the identifiant of the entity's team.
	 * @return the identifiant of the entity's team
	 */
	public int getTeam();
	
	/**
	 * Applies the reaction to the contact of the specified entity.
	 * @param entity : the entity witch made the contact
	 * @param trace : the trace describing the current entity impact
	 * @return if a reaction occurs or not
	 */
	public boolean touchReact(Entity entity, Trace trace);
	
	/**
	 * Returns the collision shape.
	 * @return the collision shape
	 */
	public Shape getCShape();
	
	/**
	 * Returns the entity mover.
	 * @return the entity mover
	 */
	public Mover getMover();
	
	/**
	 * Returns the 3D Node.
	 * @return the 3D Node
	 */
	public Node getNode();
	
	/**
	 * Returns the collision process object.
	 * @return the collision process object
	 */
	public Motion getCollider();
	
	/**
	 * Updates the situation of the entity.
	 */
	public void update();
	
	/**
	 * Moves the node to the entity situation.
	 */
	public void synchronizeNode();
	
	/**
	 * Sets the current life of the entity.
	 */
	public void setLife(float arg);
	
	/**
	 * Sets the damage that the entity inflicts.
	 */
	public void setDamage(float arg);
	
	/**
	 * Sets the identifiant of the entity's team.
	 */
	public void setTeam(int arg);
	
	
	/**
	 * Sets the collision shape.
	 */
	public void setCShape(Shape arg);
	
	
	/**
	 * Sets the mover of the entity.
	 */
	public void setMover(Mover arg);
	
	
	/**
	 * Sets the collider of the entity.
	 */
	public void setCollider(Motion arg);
	
	
	/**
	 * Sets the speed of the entity.
	 */
	public void setSpeed(float arg);
	
	/**
	 * Sets the entity active : it will be updated.
	 */
	public void setActive();
	
	/**
	 * Returns if the entity is active.
	 * @return if the entity is active
	 */
	public boolean isActive();
	
	/**
	 * Sets the entity exploding.
	 */
	public void setDying();
	
	/**
	 * Returns if the entity is exploding.
	 * @return if the entity is exploding
	 */
	public boolean isDying();
	
	/**
	 * Sets the entity finished : ready to be removed.
	 */
	public void setDead();
	
	/**
	 * Returns if the entity is finished.
	 * @return if the entity is finished
	 */
	public boolean isDead();
	
	/**
	 * Sets if the entity is collidable or not (true by default).
	 * @param arg : true or false
	 */
	public void setCollidable(boolean arg);
	
	/**
	 * Returns if the entity is collidable or not.
	 * @return if the entity is collidable or not
	 */
	public boolean isCollidable();
	
	/**
	 * Displays the entity
	 * @param tpf : the curent time per frames
	 */
	public void render();
	
	/**
	 * Returns a copy of the entity.
	 * @return a copy of the entity
	 */
	public Object clone();
	
}
