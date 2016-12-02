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

package main;

import java.util.Vector;
import input.LoadHelper;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import com.jme.scene.Node;
import com.jme.renderer.ColorRGBA;

import entity.KeyObject;
import entity.PlayerEntity;
import struct.Bitmap2D;
import world.World;


public class Player {
	
	public static PlayerEntity entity;
	
	private static Font2D illicitFont;
	
	private static Node textNode;
	
	private static float width;
	private static float height;
	private static float rw;
	private static float rh;
	
	private static Bitmap2D infos;
	private static Bitmap2D weapon;
	
	private static Text2D weaponText;
	private static Text2D ammoText;
	private static int oldAmmo;
	
	private static float oldLife;
	private static Bitmap2D life;
	private static Texture lifeRay;
	private static float lifeMax;
	//private static float lifeHeight = 0.68f;
	private static float lifeWidth = 0.185f;
	
	private static Bitmap2D bitmap1;
	private static Bitmap2D bitmap2;
	private static Bitmap2D bitmap3;
	private static boolean render1;
	private static boolean render2;
	private static boolean render3;
	private static int nbBitmap;
	
	private static Text2D illicitsText;
	private static float oldIllicits;
	private static Bitmap2D illicits;
	
	private static Bitmap2D target;
	
	private static Bitmap2D damage;
	
	//private static Text2D coordText;
	
	
	public static void initHud() {
		illicitFont = new Font2D();
		illicitFont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		
		width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		if (width>1024f) {
			rw = 1024f / width;
			rh = 768f / height;
		}
		else {
			rw = rh = 1f;
		}
		
		illicits = new Bitmap2D(LoadHelper.getTexture("hud_illicits.png", true), 0f*rw, 1f-(0.25f*rh), 0.2f*rw, 0.25f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		infos = new Bitmap2D(LoadHelper.getTexture("hud_infos.png", true), 0.001f*rw, 0.001f*rh, 0.224f*rw, 0.15f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		weapon = new Bitmap2D(LoadHelper.getTexture("blood2.png", true), 0.02f*rw, 0.018f*rh, 0.1f*rw, 0.08f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		lifeRay = LoadHelper.getTexture("blood2.png", true);
		life = new Bitmap2D(lifeRay, 0.008f*rw, 0.135f*rh, lifeWidth*rw, 0.01f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
        bitmap1 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 0.207f*rw, 0.104f*rh, 0.014f*rw, 0.02f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        bitmap2 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 0.207f*rw, 0.069f*rh, 0.014f*rw, 0.02f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        bitmap3 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 0.207f*rw, 0.033f*rh, 0.014f*rw, 0.02f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        render1 = false;
        render2 = false;
        render3 = false;
        nbBitmap = 0;
		
		target = new Bitmap2D(LoadHelper.getTexture("hud_target_2.png", true), 0.485f, 0.48f, 0.03f, 0.04f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		damage = new Bitmap2D(LoadHelper.getTexture("hud_blood05.png", true), 0f, 0f, 1f, 1f, Bitmap2D.ONE);
	}
	
	
	
	public static void init(PlayerEntity arg) {
		entity = arg;
		
		weapon.setTexture(arg.getWeapon().getTexture());
		
		setInventory(arg.getInventory());
		
		textNode = new Node("textNode");
		
        oldLife = 0f;
        lifeMax = entity.getLife();
        
        if (World.map!=null) illicitsText = illicitFont.createText("" + (World.map.characters.size()-1), 10f, 0);
		else illicitsText = illicitFont.createText("0", 10f, 0);
        illicitsText.setLocalTranslation(new Vector3f(width * 0.095f * rw, height - (height * 0.06f * rh), 0f));
        illicitsText.setLocalScale((height / 700f) * rh);
        illicitsText.setTextColor(new ColorRGBA(1f, 1f, 1f, 1f));
        textNode.attachChild(illicitsText);
        oldIllicits = 0;
        
        weaponText = illicitFont.createText(arg.getWeapon().getName(), 10f, 0);
        weaponText.setLocalTranslation(new Vector3f(width * 0.055f * rw, height * 0.1f * rh, 0f));
        weaponText.setLocalScale((height / 700f) * rh);
        weaponText.setTextColor(new ColorRGBA(1f, 0.6f, 0f, 1f));
        textNode.attachChild(weaponText);
        
        ammoText = illicitFont.createText("" + arg.getWeapon().getAmmo(), 10f, 0);
        ammoText.setLocalTranslation(new Vector3f(width * 0.135f * rw, height * 0.055f * rh, 0f));
        ammoText.setLocalScale((height / 750f) * rh);
        ammoText.setTextColor(new ColorRGBA(1f, 0.6f, 0f, 1f));
        textNode.attachChild(ammoText);
        oldAmmo = 0;
        
        //***TO REMOVE IN PRODUCTION***
        /*coordText = illicitFont.createText("", 10f, 0);
        coordText.setLocalTranslation(new Vector3f(width * 0.01f * rw, height * 0.5f * rh, 0f));
        coordText.setLocalScale((height / 700f) * rh);
        coordText.setTextColor(new ColorRGBA(1f, 1f, 1f, 1f));
        textNode.attachChild(coordText);*/
        //*****************************
        
        textNode.updateRenderState();
        
	}
	
	
	public static void setInventory(Vector inventory) {
		
		Texture ts[] = new Texture[3];
		ts[0] = ts[1] = ts[2] = null;
		for (int i=0; i<inventory.size(); i++)
			ts[i] = ((KeyObject)inventory.get(i)).getTextureMini();
		
        if (ts[0]!=null) {
        	bitmap1.setTexture(ts[0]);
        	render1 = true;
        }
        else
        	render1 = false;
        
        if (ts[1]!=null) {
        	bitmap2.setTexture(ts[1]);
        	render2 = true;
        }
        else
        	render2 = false;
        
        if (ts[2]!=null) {
        	bitmap3.setTexture(ts[2]);
        	render3 = true;
        }
        else
        	render3 = false;
        
        nbBitmap = inventory.size();
	}
	
	
	
	public static void render() {
		
		boolean isDamage = false;
		
		if (entity.getLife() != oldLife) {
			float lifeRatio = entity.getLife() / lifeMax;
			life = new Bitmap2D(lifeRay, 0.008f*rw, 0.135f*rh, lifeWidth*lifeRatio*rw, 0.01f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
			if (entity.getLife()<oldLife)
				isDamage = true;
	        oldLife = entity.getLife();
		}
		
		if (World.map.characters.size() != oldIllicits) {
			if (World.map!=null) illicitsText.setText("" + (World.map.characters.size()-1));
			else illicitsText.setText("0");
	        oldIllicits = World.map.characters.size();
	        textNode.updateGeometricState( 0.0f, true );
		}
		
		if (entity.getWeapon().getAmmo() != oldAmmo) {
			ammoText.setText("" + entity.getWeapon().getAmmo());
	        oldAmmo = entity.getWeapon().getAmmo();
	        textNode.updateGeometricState( 0.0f, true );
		}
		
		if (entity.getInventory().size() != nbBitmap)
			setInventory(entity.getInventory());
		
		//***TO REMOVE IN PRODUCTION***
		/*coordText.setText(	"" + (int)entity.getPosition().x + ", " + 
							(int)entity.getPosition().y + ", " + 
							(int)entity.getPosition().z);
		textNode.updateGeometricState( 0.0f, true );*/
		//*****************************
		
		DisplaySystem.getDisplaySystem().getRenderer().draw(textNode);
		infos.render();
		weapon.render();
		life.render();
		if (render1) bitmap1.render();
		if (render2) bitmap2.render();
		if (render3) bitmap3.render();
		illicits.render();
		target.render();
		if (isDamage)
			damage.render();
	}
	
}
