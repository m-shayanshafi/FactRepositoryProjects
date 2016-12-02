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
import jglcore.JGL_Time;
import phys.Motion;
import phys.Motion_NoCollision;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Shape_aabb;
import phys.Trace;
import world.DecalFactory;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.model.animation.KeyframeController;


public class Blood implements Entity {
	
	private int state;
	
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	private Node objNode;
	private long deltaTime, cumulTime;
	private KeyframeController kc;
	private float kspeed;
	
	private int nbFrames;
	private float t;
	
	private int minKey;
	private int maxKey;
	private int length;
	
	
	public Blood(Node node, long time) {
		
		state = ACTIVE;
		
		objNode = node;
		
		
		cmover = new Mover_none();
		cmotion = new Motion_NoCollision();
		cshape = new Shape_aabb(new JGL_3DVector(), new JGL_3DVector(), new JGL_3DVector());
		angles = new JGL_3DVector();
		
		deltaTime = time;
		cumulTime = System.currentTimeMillis();
		
		if (node.getChild(0).getControllerCount()>0) {
			kc = (KeyframeController)node.getChild(0).getController(0);
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
	
	
	public void reset(JGL_3DVector pos) {
		state = ACTIVE;
		cumulTime = 0l;
		setAnimationFrames(0, nbFrames - 1);
		cshape.getPosition().assign(pos);
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
		return 0f;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public float getLife() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public Mover getMover() {
		// TODO Auto-generated method stub
		return cmover;
	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return objNode;
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
		return -1;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return state == ACTIVE;
	}

	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return false;
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
		
	}

	@Override
	public void setCollider(Motion arg) {
		// TODO Auto-generated method stub
		cmotion = arg;
	}

	@Override
	public void setDamage(float arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDead() {
		// TODO Auto-generated method stub
		state = DEAD;
		DecalFactory.addBloodDecal(getPosition());
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
	}

	@Override
	public void setLife(float arg) {
		// TODO Auto-generated method stub

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
		return true;
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
	public void update() {
		// TODO Auto-generated method stub
		cumulTime += JGL_Time.getTimePerFrame() * 1000f;
		if (cumulTime > deltaTime)
			setDead();
	}
	
	@Override
	public void render() {
		if (kc!=null)
			kc.setCurTime(t);
		objNode.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		objNode.updateGeometricState(0f, true);
		DisplaySystem.getDisplaySystem().getRenderer().draw(objNode);
	}
	
	public Object clone() {
		return new Blood(objNode, deltaTime);
	}

}
