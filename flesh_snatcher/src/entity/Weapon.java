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

import com.jme.image.Texture;
import jglcore.JGL_3DVector;
import jglcore.JGL_3DStruct;
import jglcore.JGL_3DMovable;
import jglcore.JGL_Math;
import entity.Shoot;
import sound.Sounds;
import world.World;


/**
 * Class representing the weapons which can be linked to entities.
 * 
 * @author Nicolas Devere
 *
 */
public final class Weapon {
	
	private JGL_3DMovable model;
	private Texture texture;
	private String name;
	
	private int ammo;
	private Shoot[] shoots;
	private boolean infinite;
	private int infiniteAmmo;
	
	private String soundID;
	
	private JGL_3DVector pos;
	private JGL_3DVector angles;
	private JGL_3DVector posFire;
	private JGL_3DVector curPosFire;
	
	private int team;
	
	private Entity owner;
	
	
	
	/**
	 * Constructs a weapon.
	 * 
	 * @param weaponModel : the weapon display model
	 * @param shoots : the shoots array
	 * @param infiniteShoots : sets if the shoots are infinite
	 * @param position : the weapon position
	 * @param orientation : the weapon orientation angles
	 */
	public Weapon(	JGL_3DStruct weaponModel, Texture weaponTexture, String weaponName, 
					Shoot[] shootsArray, boolean infiniteShoots, String soundID, 
					Entity owner, JGL_3DVector fire) {
		
		model = new JGL_3DMovable(weaponModel);
		texture = weaponTexture;
		name = weaponName;
		
		shoots = shootsArray;
		for (int i=0; i<shoots.length; i++)
			shoots[i].setWeapon(this);
		
		ammo = infiniteAmmo = shoots.length;
		
		infinite = infiniteShoots;
		
		this.soundID = soundID;
		
		if (owner!=null)
			setOwner(owner);
		
		posFire = fire;
		curPosFire = new JGL_3DVector();
		team = -1;
	}
	
	public Texture getTexture() { return texture; }
	
	public String getName() { return name; }
	
	/**
	 * Returns the ID of the shoot sound.
	 * 
	 * @return the ID of the shoot sound
	 */
	public String getSoundID() { return soundID; }
	
	/**
	 * Returns the weapon position vector object.
	 * 
	 * @return the weapon position.
	 */
	public JGL_3DVector getPosition() { return pos; }
	
	/**
	 * Returns the weapon orientation angles vector object.
	 * 
	 * @return the weapon orientation angles.
	 */
	public JGL_3DVector getOrientation() { return angles; }
	
	/**
	 * Returns the current ammo.
	 * 
	 * @return the current ammo
	 */
	public int getAmmo() { return ammo; }
	
	/**
	 * Returns the team number.
	 * 
	 * @return the team number
	 */
	public int getTeam() { return team; }
	
	/**
	 * Sets the weapon position vector object.
	 * 
	 * @param position : the weapon position vector object
	 */
	public void setPosition(JGL_3DVector position) { pos = position; }
	
	/**
	 * Sets the weapon orientation angles vector object.
	 * 
	 * @param orientation : the weapon orientation angles vector object
	 */
	public void setOrientation(JGL_3DVector orientation) { angles = orientation; }
	
	
	/**
	 * Sets the weapon team number.
	 * 
	 * @param arg : the weapon team number
	 */
	public void setTeam(int arg) { team = arg; }
	
	
	/**
	 * Sets the new owner entity of the weapon.
	 * 
	 * @param arg : the new owner entity of the weapon
	 */
	public void setOwner(Entity arg) {
		owner = arg;
		pos = owner.getPosition();
		angles = owner.getOrientation();
		team = owner.getTeam();
	}
	
	
	/**
	 * returns the owner entity of the weapon.
	 * 
	 * @return the owner entity of the weapon
	 */
	public Entity getOwner() { return owner; }
	
	
	/**
	 * Launches a shoot and inserts it in the current map.
	 */
	public void shoot(JGL_3DVector worldPos) {
		
		JGL_Math.vector_fastYXrotate(posFire, angles.x, angles.y, curPosFire);
		JGL_Math.vector_add(curPosFire, worldPos, curPosFire);
		
		if (ammo>0) {
			ammo--;
			shoots[ammo].start(curPosFire, angles, team);
			World.map.addShoot(shoots[ammo]);
			Sounds.play(soundID);
		}
		else if (infinite) {
			ammo = infiniteAmmo - 1;
			shoots[ammo].start(curPosFire, angles, team);
			World.map.addShoot(shoots[ammo]);
			Sounds.play(soundID);
		}
	}
	
	
	/**
	 * Launches a shoot and inserts it in the current map.
	 */
	public void shoot(JGL_3DVector worldPos, JGL_3DVector velocity) {
		
		JGL_Math.vector_fastYXrotate(posFire, angles.x, angles.y, curPosFire);
		JGL_Math.vector_add(curPosFire, worldPos, curPosFire);
		
		if (ammo>0) {
			ammo--;
			shoots[ammo].start2(curPosFire, velocity, team);
			World.map.addShoot(shoots[ammo]);
			Sounds.play(soundID);
		}
		else if (infinite) {
			ammo = infiniteAmmo - 1;
			shoots[ammo].start2(curPosFire, velocity, team);
			World.map.addShoot(shoots[ammo]);
			Sounds.play(soundID);
		}
	}
	
	
	/**
	 * Displays the weapon.
	 * 
	 * @param eye : the eye position
	 */
	public void display(JGL_3DVector eye) {
		model.display(eye);
	}
	
}
