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

import input.LoadHelper;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import jglcore.JGL_Time;
import struct.Bitmap2D;


public class Message {
	
	private static float width;
	private static float height;
	private static float rw;
	private static float rh;
	
	private static Font2D font2d;
	private static Text2D text;
	
	private static Node displayNode;
	private static Bitmap2D bitmap1;
	private static Bitmap2D bitmap2;
	private static Bitmap2D bitmap3;
	private static boolean render1;
	private static boolean render2;
	private static boolean render3;
	
	private static long timeCumul;
	private static long delta;
	private static boolean isDisplayed;
	
	
	public static void init() {
		
		width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		if (width>1024f) {
			rw = 1024f / width;
			rh = 768f / height;
		}
		else {
			rw = rh = 1f;
		}
		
		font2d = new Font2D();
		font2d.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		text = font2d.createText("", 10, 0);
		text.setTextColor(new ColorRGBA(1f, 0.6f, 0f, 1f));
		text.setLocalTranslation(new Vector3f(width - (width * 0.2f * rw), height - (height * 0.05f * rh), 0));
        text.setLocalScale((height / 600f) * rh);
        
		displayNode = new Node("displayNode");
		displayNode.attachChild(text);
		displayNode.updateGeometricState( 0.0f, true );
        displayNode.updateRenderState();
        
        bitmap1 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 1f-(0.2f*rw), 1f-(0.20f*rh), 0.18f*rw, 0.15f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        bitmap2 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 1f-(0.2f*rw), 1f-(0.36f*rh), 0.18f*rw, 0.15f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        bitmap3 = new Bitmap2D(LoadHelper.getTexture("white.png", true), 1f-(0.2f*rw), 1f-(0.52f*rh), 0.18f*rw, 0.15f*rh, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
        
        render1 = false;
        render2 = false;
        render3 = false;
		
		timeCumul = 0l;
		delta = 3500l;
		isDisplayed = false;
	}
	
	public static void displayMessage(String message, Texture t1, Texture t2, Texture t3) {
        
        text.setText(message);
        displayNode.updateGeometricState( 0.0f, true );
        
        if (t1!=null) {
        	bitmap1.setTexture(t1);
        	render1 = true;
        }
        else
        	render1 = false;
        
        if (t2!=null) {
        	bitmap2.setTexture(t2);
        	render2 = true;
        }
        else
        	render2 = false;
        
        if (t3!=null) {
        	bitmap3.setTexture(t3);
        	render3 = true;
        }
        else
        	render3 = false;
        
        isDisplayed = true;
        timeCumul = 0l;
	}
	
	
	public static void render() {
		
		if (isDisplayed) {
			DisplaySystem.getDisplaySystem().getRenderer().draw(displayNode);
			if (render1) bitmap1.render();
			if (render2) bitmap2.render();
			if (render3) bitmap3.render();
			timeCumul += JGL_Time.getTimePerFrame() * 1000l;
			if (timeCumul>delta)
				isDisplayed = false;
		}
	}
	
	public static void clear() {
		render1 = false;
        render2 = false;
        render3 = false;
		isDisplayed = false;
		timeCumul = 0l;
	}
	
}
