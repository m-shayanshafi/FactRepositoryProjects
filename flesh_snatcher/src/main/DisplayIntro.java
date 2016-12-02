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

import java.util.StringTokenizer;

import jglcore.JGL_Time;

import world.Entities;
import world.World;

import script.Script;
import input.LoadHelper;
import struct.Bitmap2D;

import com.jme.image.Texture;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.input.InputHandler;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;

//import audio.AudioSystem;


/**
 * 
 * @author Nicolas Devere
 *
 */
public class DisplayIntro {
	
	
	private static int action;
	
	private static Bitmap2D screen;
	private static Bitmap2D bloods[];
	
	private static Font2D my2dfont;
	private static Text2D start;
	private static Text2D load;
	private static Text2D quit;
	
	private static Text2D texts[];
	private static ColorRGBA selected = new ColorRGBA(1f, 0f, 0f, 1f);
	private static ColorRGBA unselected = new ColorRGBA(1f, 1f, 1f, 1f);
	
	
	public static void init() {
		
		Texture ts = LoadHelper.getTexture("menu_start.png", true);
		ts.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		ts.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		screen = new Bitmap2D(ts, 0f, 0f, 1f, 1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		Texture tb = LoadHelper.getTexture("menu_blood.png", true);
		bloods = new Bitmap2D[3];
		bloods[0] = new Bitmap2D(tb, 0.17f, 0.56f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		bloods[1] = new Bitmap2D(tb, 0.17f, 0.46f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		bloods[2] = new Bitmap2D(tb, 0.17f, 0.36f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		float width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		
		my2dfont = new Font2D("data/map/textures/font_fears.tga");
		my2dfont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		
		start = my2dfont.createText("start", 10f, 0);
		start.setLocalTranslation(new Vector3f(width * 0.2f, height * 0.6f, 0f));
		start.setLocalScale(height / 600f);
		start.updateGeometricState( 0.0f, true );
		start.updateRenderState();
		
		load = my2dfont.createText("load", 10f, 0);
		load.setLocalTranslation(new Vector3f(width * 0.2f, height * 0.5f, 0f));
		load.setLocalScale(height / 600f);
		load.updateGeometricState( 0.0f, true );
		load.updateRenderState();
		
		quit = my2dfont.createText("quit", 10f, 0);
		quit.setLocalTranslation(new Vector3f(width * 0.2f, height * 0.4f, 0f));
		quit.setLocalScale(height / 600f);
		quit.updateGeometricState( 0.0f, true );
		quit.updateRenderState();
		
		texts = new Text2D[3];
		texts[0] = start;
		texts[1] = load;
		texts[2] = quit;
	}
	
	
	
	/**
	 * Displays the intro on the specified component.
	 * 
	 * @param screen : the display component
	 * @return the next game sequence
	 */
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {
		
		World.clear();
		Entities.clear();
		DisplaySystem.getDisplaySystem().getRenderer().cleanup();
		rootNode.detachAllChildren();
		rootNode.attachChild(start);
		rootNode.attachChild(load);
		rootNode.attachChild(quit);
		lightState.detachAll();
        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "action", KeyInput.KEY_RETURN );
		KeyBindingManager.getKeyBindingManager().set( "up", KeyInput.KEY_UP );
		KeyBindingManager.getKeyBindingManager().set( "down", KeyInput.KEY_DOWN );
		
		Script.execute(new StringTokenizer("stopmusic"));
		Script.execute(new StringTokenizer("freeall"));
		Script.execute(new StringTokenizer("playmusic data/map/music/theme_principal.ogg 1"));
        
		JGL_Time.reset();
		
		action = 0;
		
		boolean start = false;
		boolean load = false;
		boolean finished = false;
		
		while (!start && !load && !finished) {
			
			InputSystem.update();
			
			/** Recalculate the framerate. */
	    	JGL_Time.update();
	        /** Update tpf to time per frame according to the Timer. */
	        float tpf = JGL_Time.getTimePerFrame();
	        
	        input.update( tpf );
	        
	        
			camera.update();
			/** Update controllers/render states/transforms/bounds for rootNode. */
            rootNode.updateGeometricState(tpf, true);
			
            //AudioSystem.getSystem().update();
            
			Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        for (int i=0; i<texts.length; i++)
	        	texts[i].setTextColor(unselected);
	        texts[action].setTextColor(selected);
	        r.draw(rootNode);
	        screen.render();
	        bloods[action].render();
	        r.displayBackBuffer();
	        
			
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("up", false ) ) {
	        	action--;
	        	if (action<0)
	        		action = 2;
	        }
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("down", false ) ) {
	        	action++;
	        	if (action>2)
	        		action = 0;
	        }
	        
			if ( KeyBindingManager.getKeyBindingManager().isValidCommand("action", false ) ) {
				if (action==0)
					start = true;
				if (action==1)
					load = true;
				if (action==2)
					finished = true;
			}
		}
		
		
		Script.execute(new StringTokenizer("stopmusic"));
		
		KeyBindingManager.getKeyBindingManager().removeAll();
		
		if (start)
			return Main.LOADING;
		
		if (load)
			return Main.LOAD;
		
		return Main.FINISHED;
	}
	
}
