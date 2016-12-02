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
import phys.Mover_none;
import phys.Shape;
import phys.Trace;
import world.World;
import ai.TurretAI;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;


/**
 * 
 * @author Nicolas Devere
 *
 */
public final class Turret01 implements Entity, Scriptable {
	
	
	private int state;
	
	private int team;
	private float lifeMax;
	private float life;
	private float dam;
	private float sp;
	
	private Node base;
	private Node arms;
	private Node canon;
	
	private float fireHeight;
	private JGL_3DVector fireWay;
	private JGL_3DVector firePosition;
	
	private Explosion expNode;
	
	private Blood[] bloods;
	private int bloodIndex;
	
	private boolean collidable;
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	
	private JGL_3DVector angles;
	private Quaternion qx, qy;
	
	private Weapon weapon;
	private TurretAI ai;
	
	private ScriptBox scriptbox;
	
	
	
	public Turret01(int team, float posX, float posY, float posZ, 
					float angleX, float angleY, float angleZ, 
					float firey, float firez, 
					float maxLife, float damage, float speed, 
					Node protoBase, Node protoArms, Node protoCanon, 
					Explosion explode, Blood[] bloods, 
					Shape shape, Mover mover, Motion motion) {
		
		state = ACTIVE;
		
		this.team = team;
		life = lifeMax = maxLife;
		dam = damage;
		sp = speed;
		
		base = protoBase;
		arms = protoArms;
		canon = protoCanon;
		
		fireHeight = firey;
		fireWay = new JGL_3DVector(0f, 0f, firez);
		firePosition = new JGL_3DVector();
		
		expNode = explode;
		
        this.bloods = bloods;
        bloodIndex = 0;
        
		collidable = true;
		cmover = mover;
		setSpeed(sp);
		cshape = shape;
		cshape.setPosition(new JGL_3DVector(posX, posY, posZ));
		cmotion = motion;
		
		angles = new JGL_3DVector(angleX, angleY, angleZ);
		qx = new Quaternion();
		qy = new Quaternion();
		
		weapon = null;
		ai = null;
		scriptbox = null;
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
		return dam * JGL_Time.getTimer();
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "";
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
		return canon;
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
	public void render() {
		// TODO Auto-generated method stub
		base.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		arms.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		canon.setLocalTranslation(getPosition().x, getPosition().y + fireHeight, getPosition().z);
		
		// X axis Quaternion compute
		float angle = (angles.x) * 0.5f;
		qx.set(JGL_Math.sin(angle), 0f, 0f, JGL_Math.cos(angle));
		
		// Y axis Quaternion compute
		angle = angles.y * 0.5f;
		qy.set(0f, -JGL_Math.sin(angle), 0f, JGL_Math.cos(angle));
		
		arms.setLocalRotation(qy);
		canon.setLocalRotation(qy.multLocal(qx));
		
		base.updateGeometricState(0f, true);
		arms.updateGeometricState(0f, true);
		canon.updateGeometricState(0f, true);
		DisplaySystem.getDisplaySystem().getRenderer().draw(base);
		DisplaySystem.getDisplaySystem().getRenderer().draw(arms);
		DisplaySystem.getDisplaySystem().getRenderer().draw(canon);
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
		cmover.setSpeed(0f);
		expNode.reset(getPosition().x, getPosition().y, getPosition().z);
		World.map.addObject(expNode);
		executeScripts();
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		setCollidable(false);
		cmover = new Mover_none();
		ai = null;
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
		sp = arg;
		cmover.setSpeed(arg);
	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub
		team = arg;
	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub

		if (entity.getDamage()>0f && trace.isImpact()) {
			life -= entity.getDamage();
			float x = trace.start.x + (trace.segment.x * trace.fractionImpact);
			float y = trace.start.y + (trace.segment.y * trace.fractionImpact);
			float z = trace.start.z + (trace.segment.z * trace.fractionImpact);
			bloods[bloodIndex].reset(new JGL_3DVector(x, y, z));
			World.map.addObject(bloods[bloodIndex]);
			bloodIndex++;
			if (bloodIndex>=bloods.length)
				bloodIndex = 0;
		}
		
		if (life<=0f)
			setDying();
		return true;
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
	
	
	/**
	 * Affiliates the specified weapon to this turret.
	 * 
	 * @param _weapon : the weapon to affiliate to this turret
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
		if (weapon!=null && state==ACTIVE)
			weapon.shoot(firePosition);
	}
	
	
	public JGL_3DVector getFirePosition() {
		return firePosition;
	}
	
	
	/**
	 * Affiliates a AI object to this Turret.
	 * 
	 * @param turretAI : the AI object to affiliate to this Turret
	 */
	public void linkAI(TurretAI turretAI) {
		ai = turretAI;
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (isActive()) {
			ai.update();
			cmover.update();
		}
		else if (isDying())
			setDead();
		
		JGL_Math.vector_fastYXrotate(fireWay, angles.x, angles.y, firePosition);
		firePosition.y += fireHeight;
		JGL_Math.vector_add(firePosition, getPosition(), firePosition);
	}

	@Override
	public void executeScripts() {
		// TODO Auto-generated method stub
		if (scriptbox!=null)
			scriptbox.execute();
	}

	@Override
	public void storeScriptBox(ScriptBox arg) {
		// TODO Auto-generated method stub
		scriptbox = arg;
	}
	
	public Object clone() {
		Blood bs[] = new Blood[bloods.length];
	    for (int i=0; i<bloods.length; i++)
	    	bs[i] = (Blood)bloods[i].clone();
	    return new Turret01(team, getPosition().x, getPosition().y, getPosition().z, 
	    					angles.x, angles.y, angles.z, fireHeight, fireWay.z, lifeMax, dam, sp, 
	    					base, arms, canon, (Explosion)expNode.clone(), bs, 
	    					(Shape)cshape.clone(), (Mover)cmover.clone(), (Motion)cmotion.clone());
	}
}
