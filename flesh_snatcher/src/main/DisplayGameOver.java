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

import script.Script;

import jglcore.JGL_3DVector;
import jglcore.JGL_Time;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;



/**
 * 
 * @author Nicolas Devere
 *
 */
public class DisplayGameOver {
	
	private static Font2D my2dfont;
	private static Text2D game;
	private static Text2D over;
	private static Text2D press;
	private static Text2D ret;
	
	// Camera attributes
	private static JGL_3DVector baseLeft = new JGL_3DVector(-1f, 0f, 0f);
	private static JGL_3DVector baseUp = new JGL_3DVector(0f, 1f, 0f);
	private static JGL_3DVector baseDepth = new JGL_3DVector(0f, 0f, -1f);
	
	private static Vector3f v_loc = new Vector3f();
	private static Vector3f v_left = new Vector3f();
	private static Vector3f v_up = new Vector3f();
	private static Vector3f v_depth = new Vector3f();
	
	
	public static void init() {
		
		float width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		
		my2dfont = new Font2D("data/map/textures/font_fears.tga");
		my2dfont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		
		game = my2dfont.createText("game", 10f, 0);
		game.setLocalTranslation(new Vector3f(width * 0.35f, height * 0.7f, 0f));
		game.setLocalScale(height / 200f);
		game.updateGeometricState( 0.0f, true );
		game.updateRenderState();
		
		over = my2dfont.createText("over", 10f, 0);
		over.setLocalTranslation(new Vector3f(width * 0.52f, height * 0.7f, 0f));
		over.setLocalScale(height / 200f);
		over.updateGeometricState( 0.0f, true );
		over.updateRenderState();
		
		press = my2dfont.createText("press", 10f, 0);
		press.setLocalTranslation(new Vector3f(width * 0.41f, height * 0.5f, 0f));
		press.setLocalScale(height / 400f);
		press.updateGeometricState( 0.0f, true );
		press.updateRenderState();
		
		ret = my2dfont.createText("return", 10f, 0);
		ret.setLocalTranslation(new Vector3f(width * 0.52f, height * 0.5f, 0f));
		ret.setLocalScale(height / 400f);
		ret.updateGeometricState( 0.0f, true );
		ret.updateRenderState();
	}
	
	
	
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {
		
		rootNode.detachAllChildren();
		rootNode.attachChild(game);
		rootNode.attachChild(over);
		rootNode.attachChild(press);
		rootNode.attachChild(ret);
		lightState.detachAll();
        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
        
        Script.execute(new StringTokenizer("stopmusic"));
		Script.execute(new StringTokenizer("playmusic data/map/music/vers_les_dieux.ogg 1"));
        
        JGL_Time.reset();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "intro", KeyInput.KEY_RETURN );
		
		boolean interrupt = false;
		
		while (!interrupt) {
			
			InputSystem.update();
			
			/** Recalculate the framerate. */
	    	JGL_Time.update();
	        /** Update tpf to time per frame according to the Timer. */
	        float tpf = JGL_Time.getTimePerFrame();
	        
	        input.update( tpf );
	        
	        
			v_loc.set(0f, 0f, 0f);
			v_left.set(baseLeft.x, baseLeft.y, baseLeft.z);
			v_up.set(baseUp.x, baseUp.y, baseUp.z);
			v_depth.set(baseDepth.x, baseDepth.y, baseDepth.z);
			camera.setFrame(v_loc, v_left, v_up, v_depth);
			camera.update();
			/** Update controllers/render states/transforms/bounds for rootNode. */
	        
			Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        r.draw(rootNode);
	        r.displayBackBuffer();
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("intro", false ) )
				interrupt = true;
		}
		
		Script.execute(new StringTokenizer("stopmusic"));
		
		KeyBindingManager.getKeyBindingManager().removeAll();
		
		return Main.INTRO;
	}
	
	
}
