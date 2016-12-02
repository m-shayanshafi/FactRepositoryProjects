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

import java.util.Vector;
import java.util.StringTokenizer;

import com.jme.scene.Node;

import jglcore.JGL_3DVector;
import phys.Motion;
import phys.Motion_NoCollision;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Trace;
import main.Player;
import script.Script;


/**
 * Class representing a bounding shape executing some scripts when collision with it.
 * 
 * @author Nicolas Devere
 *
 */
public final class ScriptBox implements Entity {
	
	private int state;
	
	private Shape cshape;
	private Mover cmover;
	private Motion cmotion;
	private int team;
	private float life;
	private float dam;
	private boolean collidable;
	
	private Vector scr;
	
	
	/**
	 * Constructor.
	 * 
	 * @param shape : the bounding shape
	 * @param scripts : the scripts list to execute
	 */
	public ScriptBox(Shape shape, Vector scripts) {
		
		state = ACTIVE;
		
		cshape = (Shape)shape.clone();
		cmover = new Mover_none();
		cmotion = new Motion_NoCollision();
		scr = scripts;
		
		team = -4;
		life = 1f;
		dam = 0f;
		collidable = true;
	}
	
	
	public String getID() {
		return "";
	}
	
	
	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Shape getCShape() {
		// TODO Auto-generated method stub
		return cshape;
	}

	public Motion getCollider() {
		// TODO Auto-generated method stub
		return cmotion;
	}

	public float getDamage() {
		// TODO Auto-generated method stub
		return dam;
	}

	public float getLife() {
		// TODO Auto-generated method stub
		return life;
	}


	public Mover getMover() {
		// TODO Auto-generated method stub
		return cmover;
	}

	public JGL_3DVector getOrientation() {
		// TODO Auto-generated method stub
		//return new JGL_3DVector();
		return null;
	}

	public JGL_3DVector getPosition() {
		// TODO Auto-generated method stub
		return cshape.getPosition();
	}

	public int getTeam() {
		// TODO Auto-generated method stub
		return team;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return state==ACTIVE;
	}

	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return collidable;
	}

	

	public void setActive() {
		// TODO Auto-generated method stub
		state = ACTIVE;
	}

	public void setCollidable(boolean arg) {
		// TODO Auto-generated method stub
		collidable = arg;
	}

	public void setDamage(float arg) {
		// TODO Auto-generated method stub
		dam = arg;
	}


	public void setLife(float arg) {
		// TODO Auto-generated method stub
		
	}

	public void setSpeed(float arg) {
		// TODO Auto-generated method stub

	}


	public boolean isDead() {
		// TODO Auto-generated method stub
		return state == DEAD;
	}
	
	public void setDead() {
		state = DEAD;
	}

	public boolean isDying() {
		// TODO Auto-generated method stub
		return state == DYING;
	}
	
	public void setDying() {
		state = DYING;
	}
	
	public void setTeam(int arg) {
		// TODO Auto-generated method stub

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

	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		if (entity==Player.entity)
			execute();
		return false;
	}

	public void update() {
		// TODO Auto-generated method stub

	}
	
	
	public void render() {
		
	}
	
	public void synchronizeNode() {
		
	}
	
	
	public Object clone() {
		return new ScriptBox(cshape, scr);
	}
	
	
	/**
	 * Scripts execution
	 */
	public void execute() {
		if (isActive()) {
			for (int i=0; i<scr.size(); i++)
				Script.execute(new StringTokenizer((String)scr.get(i)));
			setDead();
		}
	}
	

}
