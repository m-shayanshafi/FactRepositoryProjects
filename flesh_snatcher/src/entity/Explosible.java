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
import jglcore.JGL_Math;
import phys.Bsp_tree;
import phys.Motion;
import phys.Motion_NoCollision;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Shape_bsp;
import phys.Shape_sphere;
import phys.Trace;
import world.World;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;


/**
 * 
 * @author Nicolas Devere
 *
 */
public final class Explosible implements Entity {
	
	
	private static Shape_sphere pPoint = new Shape_sphere(new JGL_3DVector(), 0f);
	private static Trace trace = new Trace();
	
	private int state;
	
	private int team;
	
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	
	private Bsp_tree cBsp;
	
	private Node objNode;
	
	private Node hitNode;
	private boolean isHit;
	private long hitTime;
	private static long hitLapse = 40l;
	
	private Explosion expNode;
	
	private Node curNode;
	
	private float life;
	private float dam;
	private float damOffset2;
	
	
	public Explosible(	int team, float posX, float posY, float posZ, 
						float life, float damage, float damageOffset, 
						Node node, Node hit, Explosion explode, Bsp_tree bsp) {
		
		state = ACTIVE;
		
		this.team = team;
		
		cmover = new Mover_none();
		cmotion = new Motion_NoCollision();
		cshape = new Shape_bsp(new JGL_3DVector(posX, posY, posZ), bsp);
		angles = new JGL_3DVector();
		
		cBsp = bsp;
		
		this.life = life;
		dam = damage;
		damOffset2 = damageOffset * damageOffset;
		
		objNode = node;
		objNode.setLocalTranslation(posX, posY, posZ);
		
		hitNode = hit;
		hitNode.setLocalTranslation(posX, posY, posZ);
		isHit = false;
		hitTime = 0l;
		
		expNode = explode;
		
		curNode = objNode;
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
		return true;
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
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
		isHit = false;
		
		Vector v = World.map.characters;
		Entity e;
		for (int i=0; i<v.size(); i++) {
			e = (Entity)v.get(i);
			if (e!=this && JGL_Math.vector_squareDistance(getPosition(), e.getPosition())<damOffset2) {
				trace.reset(pPoint, getPosition(), e.getPosition());
				e.getCShape().trace(trace);
				e.touchReact(this, trace);
			}
		}
		
		v = World.map.objects;
		for (int i=0; i<v.size(); i++) {
			e = (Entity)v.get(i);
			if (e!=this && JGL_Math.vector_squareDistance(getPosition(), e.getPosition())<damOffset2) {
				trace.reset(pPoint, getPosition(), e.getPosition());
				e.getCShape().trace(trace);
				e.touchReact(this, trace);
			}
		}
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

	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub
		team = arg;
	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub
		curNode.updateGeometricState(JGL_Time.getTimePerFrame(), true);
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		if (team!=entity.getTeam() && entity.getDamage()>0f) {
			isHit = true;
			curNode = hitNode;
			hitTime = System.currentTimeMillis();
			life -= entity.getDamage();
		}
		if (life<=0f && isActive())
			setDying();
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (isHit && (System.currentTimeMillis()-hitTime)>hitLapse) {
			curNode = objNode;
			isHit = false;
		}
		if (isDying())
			setDead();
	}
	
	public Object clone() {
		try {
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			BinaryExporter.getInstance().save(objNode, BO);
			Node n = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			n.updateGeometricState( 0.0f, true );
	        n.updateRenderState();
	        
	        BO = new ByteArrayOutputStream();
			BinaryExporter.getInstance().save(hitNode, BO);
			Node h = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			h.updateGeometricState( 0.0f, true );
	        h.updateRenderState();
	        
	        return new Explosible(getTeam(), getPosition().x, getPosition().y, getPosition().z, life, dam, (float)Math.sqrt(damOffset2), n, h, (Explosion)expNode.clone(), cBsp);
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}

}
