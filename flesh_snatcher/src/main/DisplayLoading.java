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

import input.LoadHelper;

import struct.Bitmap2D;
import world.MapLoader;
import world.World;

import jglcore.JGL_Time;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;

import java.util.Observer;
import java.util.Observable;


public class DisplayLoading {
	
	
	private static Texture skull;
	private static Texture ray;
	private static Texture littleBlood;
	
	
	public static void init() {
		skull = LoadHelper.getTexture("loading_skull.png", true);
		ray = LoadHelper.getTexture("loading_ray.png", true);
		littleBlood = LoadHelper.getTexture("menu_blood.png", true);
	}
	
	
	
	static class LoadingScreen implements Observer {
		
		private Bitmap2D skull;
		private Bitmap2D ray;
		private Bitmap2D lblood;
		private Bitmap2D pic;
		
		private Texture blood;
		
		private Font2D my2dfont;
		private Text2D loading;
		private Text2D levelText;
		private Node textNode;
		
		private int nbLines;
		
		public LoadingScreen(Texture skull, Texture ray, Texture lblood, Texture level, String levelName, int lines) {
			
			float width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
			float height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
			
			this.skull = new Bitmap2D(skull, -0.05f, -0.05f, 0.4f, 0.42f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
			this.ray   = new Bitmap2D(ray, 0.11f, 0.1f, 0.8f, 0.07f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
			this.lblood= new Bitmap2D(lblood, 0f, 0.9f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
			this.pic   = new Bitmap2D(level, 0.1f, 0.45f, 0.8f, 0.3f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
			
			blood = LoadHelper.getTexture("blood2.png", true);
			
			my2dfont = new Font2D();
			my2dfont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
			
			loading = my2dfont.createText("Loading...", 10f, 0);
			loading.setLocalTranslation(new Vector3f(width * 0.031f, height * 0.95f, 0f));
			loading.setLocalScale(height / 600f);
			
			levelText = my2dfont.createText(levelName, 10f, 0);
			levelText.setLocalTranslation(new Vector3f(width * 0.45f, height * 0.25f, 0f));
			levelText.setLocalScale(height / 600f);
			
			textNode = new Node("");
			textNode.attachChild(loading);
			textNode.attachChild(levelText);
			textNode.updateGeometricState( 0.0f, true );
			textNode.updateRenderState();
			
			nbLines = lines;
		}
		
		public void update(Observable o, Object obj) {
			
			Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        r.draw(textNode);
	        
	        Bitmap2D bloodFill = new Bitmap2D(blood, 0.11f, 0.11f, 0.8f * ((float)((MapLoader)o).getLines() / (float)nbLines), 0.06f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
	        
	        bloodFill.render();
	        ray.render();
	        skull.render();
	        lblood.render();
	        pic.render();
	        r.displayBackBuffer();
			
		}
	}
	
	
	
	
	
	
	/**
	 * Displays the intro on the specified component.
	 * 
	 * @param screen : the display component
	 * @return the next game sequence
	 */
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {
		try {
			camera.update();
			
			rootNode.detachAllChildren();
			lightState.detachAll();
	        rootNode.updateGeometricState( 0.0f, true );
	        rootNode.updateRenderState();
	        
	        KeyBindingManager.getKeyBindingManager().removeAll();
	        input = new DummyHandler();
	        
	        //Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        //r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        //r.draw(rootNode);
	        //r.displayBackBuffer();
			
	        rootNode.detachAllChildren();
	        
	        if (!World.isFinished()) {
		        LoadingScreen screen = new LoadingScreen(skull, ray, littleBlood, LoadHelper.getTexture(World.getPic(), true), World.getName(), World.getLines());
		        if (World.loadNext(screen)) {
		        	
		        	JGL_Time.reset();
		        	return Main.INGAME;
		        }
		        else
		        	return Main.GAME_DONE;
	        }
	        else
	        	return Main.GAME_DONE;
		}
		catch(Exception ex) {
			return Main.FINISHED;
		}
		
	}

}
