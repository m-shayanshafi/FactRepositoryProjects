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
import phys.Motion;
import phys.Motion_NoCollision;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Shape_aabb;
import phys.Trace;
import sound.Sounds;
import struct.Explode;

import com.jme.scene.Node;


/**
 * 
 * @author Nicolas Devere
 *
 */
public class Explosion implements Entity {
	
	private int state;
	
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	private String sound;
	
	private Explode exp[];
	
	
	public Explosion(Explode[] explosions, String soundID) {
		
		state = ACTIVE;
		exp = explosions;
		cmover = new Mover_none();
		cmotion = new Motion_NoCollision();
		cshape = new Shape_aabb(new JGL_3DVector(), new JGL_3DVector(), new JGL_3DVector());
		angles = new JGL_3DVector();
		sound = soundID;
	}
	
	public void reset(float x, float y, float z) {
		state = ACTIVE;
		for (int i=0; i<exp.length; i++) {
			exp[i].reset();
			exp[i].setPosition(x, y, z);
		}
		Sounds.play(sound);
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
		return null;
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
	public void render() {
		// TODO Auto-generated method stub
		for (int i=0; i<exp.length; i++)
			exp[i].render();
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
		for (int i=0; i<exp.length; i++)
			exp[i].synchronizeNode();
	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int nbFinished = 0;
		for (int i=0; i<exp.length; i++) {
			exp[i].update();
			if (exp[i].isFinished())
				nbFinished++;
		}
		if (nbFinished==exp.length)
			setDead();
	}
	
	
	public Object clone() {
		
		Explode newExp[] = new Explode[exp.length];
		for (int i=0; i<exp.length; i++)
			newExp[i] = (Explode)exp[i].clone();
		return new Explosion(newExp, sound);
	}

}
