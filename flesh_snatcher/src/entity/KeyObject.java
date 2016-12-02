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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import jglcore.JGL_Time;
import phys.Motion;
import phys.Motion_stop;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Trace;

import main.Player;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.image.Texture;


public class KeyObject implements Entity, Scriptable {
	
	private int state;
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	private Quaternion qx;
	private Quaternion qy;
	private Quaternion qz;
	
	private String objId;
	private Node objNode;
	
	private Texture tmini;
	private Texture tok;
	private Texture tnok;
	
	private ScriptBox scriptbox;
	
	
	public KeyObject(Shape shape, float angleX, float angleY, float angleZ, String id, Node node, 
						Texture mini, Texture ok, Texture nok) {
		
		state = ACTIVE;
		cmover = new Mover_none();
		cmotion = new Motion_stop();
		cshape = shape;
		angles = new JGL_3DVector(angleX, angleY, angleZ);
		qx = new Quaternion();
		qy = new Quaternion();
		qz = new Quaternion();
		objId = id;
		objNode = node;
		
		tmini = mini;
		tok = ok;
		tnok = nok;
		tmini.setApply(Texture.ApplyMode.Combine);
		tok.setApply(Texture.ApplyMode.Combine);
		tnok.setApply(Texture.ApplyMode.Combine);
		
		scriptbox = null;
	}
	
	public Texture getTextureMini() {
		return tmini;
	}
	
	public Texture getTextureOK() {
		return tok;
	}
	
	public Texture getTextureNOK() {
		return tnok;
	}
	
	
	@Override
	public String getID() {
		return objId;
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
		executeScripts();
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

	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		if (entity == Player.entity) {
			((PlayerEntity)entity).getInventory().add(this);
			setDead();
			Message.displayMessage("You took", getTextureOK(), null, null);
		}
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		cmover.update();
	}
	
	@Override
	public void render() {
		
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
		
		DisplaySystem.getDisplaySystem().getRenderer().draw(objNode);
	}
	
	public Object clone() {
		try {
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			BinaryExporter.getInstance().save(objNode, BO);
			Node n = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			n.updateGeometricState( 0.0f, true );
	        n.updateRenderState();
	        return new KeyObject((Shape)cshape.clone(), angles.x, angles.y, angles.z, objId, n, tmini, tok, tnok);
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	
	public void storeScriptBox(ScriptBox arg) {
		scriptbox = arg;
	}
	
	public void executeScripts() {
		if (scriptbox!=null)
			scriptbox.execute();
	}
}
