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
import phys.Motion_stop;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Trace;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public final class StaticObject implements Entity {
	
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	private Quaternion qx;
	private Quaternion qy;
	private Quaternion qz;
	
	private Node objNode;
	
	
	public StaticObject(Shape shape, float angleX, float angleY, float angleZ, Node node) {
		cmover = new Mover_none();
		cmotion = new Motion_stop();
		cshape = shape;
		angles = new JGL_3DVector(angleX, angleY, angleZ);
		qx = new Quaternion();
		qy = new Quaternion();
		qz = new Quaternion();
		objNode = node;
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
		return 0;
	}

	@Override
	public float getLife() {
		// TODO Auto-generated method stub
		return 1;
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
		return true;
	}

	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub

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

	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub

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

	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub
		objNode.setLocalTranslation(getPosition().x, getPosition().y, getPosition().z);
		
		//	 X axis Quaternion compute
		float angle = angles.x * 0.5f;
		qx.set(JGL_Math.sin(angle), 0f, 0f, JGL_Math.cos(angle));
		
		//	 Y axis Quaternion compute
		angle = angles.y * 0.5f;
		qy.set(0f, -JGL_Math.sin(angle), 0f, JGL_Math.cos(angle));
		
		//	 Z axis Quaternion compute
		angle = angles.z * 0.5f;
		qz.set(0f, 0f, JGL_Math.sin(angle), JGL_Math.cos(angle));
		
		objNode.setLocalRotation(qy.multLocal(qx.multLocal(qz)));
		objNode.updateGeometricState(JGL_Time.getTimePerFrame(), true);
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		cmover.update();
	}
	
	@Override
	public void render() {
		DisplaySystem.getDisplaySystem().getRenderer().draw(objNode);
	}
	
	public Object clone() {
		return new StaticObject((Shape)cshape.clone(), angles.x, angles.y, angles.z, objNode);
		
	}
}
