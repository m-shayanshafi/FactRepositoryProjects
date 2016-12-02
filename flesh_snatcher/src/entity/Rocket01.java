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
import phys.Shape;
import phys.Trace;
import world.World;
import ai.Rocket01AI;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.model.animation.KeyframeController;



public final class Rocket01 implements Entity, Scriptable {
	
	private int state;
	
	private int team;
	private float life;
	private float dam;
	private float sp;
	
	private Node objNode;
	
	private Node hitNode;
	private boolean isHit;
	private long hitTime;
	private static long hitLapse = 40l;
	
	private Explosion expNode;
	
	private Node curNode;
	private KeyframeController kc;
	private float kspeed;
	private int nbFrames;
	private float t;
	private int minKey;
	private int maxKey;
	private int length;
	
	private boolean collidable;
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	
	private JGL_3DVector angles;
	private float rotateX, rotateY, rotateZ;
	private float rSpeedX, rSpeedY, rSpeedZ;
	private Quaternion qx;
	private Quaternion qy;
	private Quaternion qz;
	
	private Rocket01AI ai;
	
	private ScriptBox scriptbox;
	
	
	public Rocket01(int team, float posX, float posY, float posZ, 
					float angleX, float angleY, float angleZ, 
					float rotX, float rotY, float rotZ, 
					float maxLife, float damage, float speed, 
					Node protoNode, Node hit, Explosion explode, 
					Shape shape, Mover mover, Motion motion) {
		
		state = ACTIVE;
		
		this.team = team;
		life = maxLife;
		dam = damage;
		sp = speed;
		
		collidable = true;
		cmover = mover;
		cmover.setSpeed(sp);
		cmotion = motion;
		cshape = shape;
		cshape.setPosition(new JGL_3DVector(posX, posY, posZ));
		angles = new JGL_3DVector(angleX, angleY, angleZ);
		
		objNode = protoNode;
		objNode.setLocalTranslation(posX, posY, posZ);
		
		hitNode = hit;
		hitNode.setLocalTranslation(posX, posY, posZ);
		isHit = false;
		hitTime = 0l;
		
		expNode = explode;
		
		setCurrentNode(objNode);
		
		rSpeedX = rotX;
		rSpeedY = rotY;
		rSpeedZ = rotZ;
		
		rotateX = rotateY = rotateZ = 0f;
		qx = new Quaternion();
		qy = new Quaternion();
		qz = new Quaternion();
		
		ai = new Rocket01AI(this);
		
		scriptbox = null;
	}
	
	
	private void setCurrentNode(Node n) {
		curNode = n;
		if (n.getChild(0).getControllerCount()>0) {
			kc = (KeyframeController)n.getChild(0).getController(0);
			kspeed = kc.getSpeed();
			nbFrames = kc.keyframes.size();
		}
		else {
			kc = null;
			kspeed = 0f;
			nbFrames = 1;
		}
		setAnimationFrames(0, nbFrames - 1);
	}
	
	
	/**
	 * Sets the min and max keyframes for the animation to play.
	 * 
	 * @param minKeyframe : the min keyframe
	 * @param maxKeyframe : the max keyframe
	 */
	public void setAnimationFrames(int minKeyframe, int maxKeyframe) {
		if (maxKeyframe<minKeyframe) return;
		if (minKeyframe<0) minKeyframe = 0;
		if (maxKeyframe<0) maxKeyframe = 0;
		if (minKeyframe>nbFrames - 1) minKeyframe = nbFrames - 1;
		if (maxKeyframe>nbFrames - 1) maxKeyframe = nbFrames - 1;
		
		minKey = minKeyframe;
		maxKey = maxKeyframe;
		length = (maxKey - minKey);
		
		t = minKey;
	
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
		return curNode;
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
		if (kc!=null)
			kc.setCurTime(t);
		curNode.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		
		//	 X axis Quaternion compute
		float angle = rotateX * 0.5f;
		qx.set(JGL_Math.sin(angle), 0f, 0f, JGL_Math.cos(angle));
		
		//	 Y axis Quaternion compute
		angle = rotateY * 0.5f;
		qy.set(0f, -JGL_Math.sin(angle), 0f, JGL_Math.cos(angle));
		
		//	 Z axis Quaternion compute
		angle = rotateZ * 0.5f;
		qz.set(0f, 0f, JGL_Math.sin(angle), JGL_Math.cos(angle));
		
		curNode.setLocalRotation(qy.multLocal(qx.multLocal(qz)));
		curNode.updateGeometricState(0f, true);
		DisplaySystem.getDisplaySystem().getRenderer().draw(curNode);
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
		expNode.reset(getPosition().x, getPosition().y, getPosition().z);
		World.map.addObject(expNode);
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
		cmover.setSpeed(0f);
		isHit = false;
	}

	@Override
	public void setLife(float arg) {
		// TODO Auto-generated method stub
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
		if (length>0) {
			t += JGL_Time.getTimePerFrame() * kspeed;
			while (t >= maxKey)
				t -= length;
		}
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		if (team!=entity.getTeam() && entity.getDamage()>0f) {
			isHit = true;
			setCurrentNode(hitNode);
			hitTime = System.currentTimeMillis();
			ai.setEscape();
			life -= entity.getDamage();
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
		
		while (angles.x>360f) angles.x -= 360f;
		while (angles.x<-360f) angles.x += 360f;
		while (angles.y>360f) angles.y -= 360f;
		while (angles.y<-360f) angles.y += 360f;
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (isActive()) {
			ai.update();
			if (isHit && (System.currentTimeMillis()-hitTime)>hitLapse) {
				curNode = objNode;
				isHit = false;
			}
		}
		else if (isDying())
			//if ( (System.currentTimeMillis()-expTime)>expLapse )
				setDead();
		
		JGL_Math.vector_fastYXrotate(angles.x, angles.y, cmover.getVelocity());
		
		rotateX += rSpeedX * JGL_Time.getTimer();
		rotateY += rSpeedY * JGL_Time.getTimer();
		rotateZ += rSpeedZ * JGL_Time.getTimer();
		while (rotateX>360f) rotateX -= 360f;
		while (rotateX<-360f) rotateX += 360f;
		while (rotateY>360f) rotateY -= 360f;
		while (rotateY<-360f) rotateY += 360f;
		while (rotateZ>360f) rotateZ -= 360f;
		while (rotateZ<-360f) rotateZ += 360f;
		
		cmover.update();
	}
	
	
	public Object clone() {
		return new Rocket01(getTeam(), getPosition().x, getPosition().y, getPosition().z, 
							angles.x, angles.y, angles.z, rSpeedX, rSpeedY, rSpeedZ, 
							life, dam, sp, objNode, hitNode, (Explosion)expNode.clone(), 
							(Shape)cshape.clone(), (Mover)cmover.clone(), (Motion)cmotion.clone());
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

}
