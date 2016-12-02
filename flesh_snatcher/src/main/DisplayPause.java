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

import struct.Bitmap2D;
import input.LoadHelper;

import world.World;

import jglcore.JGL;
import jglcore.JGL_3DMatrix;
import jglcore.JGL_3DVector;
import jglcore.JGL_Time;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;


public class DisplayPause {
	
	
	private static int action;
	
	private static Bitmap2D bloods[];
	
	private static Font2D my2dfont;
	private static Text2D pause;
	private static Text2D back;
	private static Text2D save;
	private static Text2D quit;
	
	private static Text2D texts[];
	private static ColorRGBA selected = new ColorRGBA(1f, 0f, 0f, 1f);
	private static ColorRGBA unselected = new ColorRGBA(1f, 1f, 1f, 1f);
	
	// Camera attributes
	private static JGL_3DMatrix matrix = new JGL_3DMatrix();
	
	private static JGL_3DVector baseLeft = new JGL_3DVector(-1f, 0f, 0f);
	private static JGL_3DVector baseUp = new JGL_3DVector(0f, 1f, 0f);
	private static JGL_3DVector baseDepth = new JGL_3DVector(0f, 0f, -1f);
	
	private static JGL_3DVector left = new JGL_3DVector(-1f, 0f, 0f);
	private static JGL_3DVector up = new JGL_3DVector(0f, 1f, 0f);
	private static JGL_3DVector depth = new JGL_3DVector(0f, 0f, -1f);

	private static Vector3f v_loc = new Vector3f();
	private static Vector3f v_left = new Vector3f();
	private static Vector3f v_up = new Vector3f();
	private static Vector3f v_depth = new Vector3f();
	
	
	
	public static void init() {
		
		Texture tb = LoadHelper.getTexture("menu_blood.png", true);
		bloods = new Bitmap2D[3];
		bloods[0] = new Bitmap2D(tb, 0.44f, 0.51f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		bloods[1] = new Bitmap2D(tb, 0.44f, 0.41f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		bloods[2] = new Bitmap2D(tb, 0.44f, 0.31f, 0.1f, 0.1f, Bitmap2D.ONE_MINUS_SOURCE_ALPHA);
		
		float width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		
		my2dfont = new Font2D("data/map/textures/font_fears.tga");
		my2dfont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		
		pause = my2dfont.createText("pause", 10f, 0);
		pause.setLocalTranslation(new Vector3f(width * 0.425f, height * 0.7f, 0f));
		pause.setLocalScale(height / 300f);
		pause.updateGeometricState( 0.0f, true );
		pause.updateRenderState();
		
		back = my2dfont.createText("back", 10f, 0);
		back.setLocalTranslation(new Vector3f(width * 0.47f, height * 0.55f, 0f));
		back.setLocalScale(height / 600f);
		back.updateGeometricState( 0.0f, true );
		back.updateRenderState();
		
		save = my2dfont.createText("save", 10f, 0);
		save.setLocalTranslation(new Vector3f(width * 0.47f, height * 0.45f, 0f));
		save.setLocalScale(height / 600f);
		save.updateGeometricState( 0.0f, true );
		save.updateRenderState();
		
		quit = my2dfont.createText("quit", 10f, 0);
		quit.setLocalTranslation(new Vector3f(width * 0.47f, height * 0.35f, 0f));
		quit.setLocalScale(height / 600f);
		quit.updateGeometricState( 0.0f, true );
		quit.updateRenderState();
		
		texts = new Text2D[3];
		texts[0] = back;
		texts[1] = save;
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
		
		rootNode.detachAllChildren();
		rootNode.attachChild(pause);
		rootNode.attachChild(back);
		rootNode.attachChild(save);
		rootNode.attachChild(quit);
		lightState.detachAll();
        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "action", KeyInput.KEY_RETURN );
		KeyBindingManager.getKeyBindingManager().set( "up", KeyInput.KEY_UP );
		KeyBindingManager.getKeyBindingManager().set( "down", KeyInput.KEY_DOWN );
		
		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		
		JGL_Time.reset();
		
		action = 0;
		
		boolean back = false;
		boolean save = false;
		boolean intro = false;
		while (!back && !save && !intro) {
			
			InputSystem.update();
			
			/** Recalculate the framerate. */
	    	JGL_Time.update();
	        /** Update tpf to time per frame according to the Timer. */
	        float tpf = JGL_Time.getTimePerFrame();
	        
	        input.update( tpf );
	        
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        
			// Camera update
			JGL_3DVector pos = Player.entity.getPosition();
			JGL_3DVector or = Player.entity.getOrientation();
			
			matrix.identity();
			matrix.rotate(or.x, or.y, or.z, JGL.YXZ);
			
			left.x = (matrix.m11*baseLeft.x) + (matrix.m12*baseLeft.y) + (matrix.m13*baseLeft.z);
			left.y = (matrix.m21*baseLeft.x) + (matrix.m22*baseLeft.y) + (matrix.m23*baseLeft.z);
			left.z = (matrix.m31*baseLeft.x) + (matrix.m32*baseLeft.y) + (matrix.m33*baseLeft.z);
			
			up.x = (matrix.m11*baseUp.x) + (matrix.m12*baseUp.y) + (matrix.m13*baseUp.z);
			up.y = (matrix.m21*baseUp.x) + (matrix.m22*baseUp.y) + (matrix.m23*baseUp.z);
			up.z = (matrix.m31*baseUp.x) + (matrix.m32*baseUp.y) + (matrix.m33*baseUp.z);
			
			depth.x = (matrix.m11*baseDepth.x) + (matrix.m12*baseDepth.y) + (matrix.m13*baseDepth.z);
			depth.y = (matrix.m21*baseDepth.x) + (matrix.m22*baseDepth.y) + (matrix.m23*baseDepth.z);
			depth.z = (matrix.m31*baseDepth.x) + (matrix.m32*baseDepth.y) + (matrix.m33*baseDepth.z);
			
			v_loc.set(pos.x, pos.y, pos.z);
			v_left.set(left.x, left.y, left.z);
			v_up.set(up.x, up.y, up.z);
			v_depth.set(depth.x, depth.y, depth.z);
			camera.setFrame(v_loc, v_left, v_up, v_depth);
			camera.update();
	        
			World.map.render(Player.entity.getPosition());
	        
			v_loc.set(0f, 0f, 0f);
			v_left.set(baseLeft.x, baseLeft.y, baseLeft.z);
			v_up.set(baseUp.x, baseUp.y, baseUp.z);
			v_depth.set(baseDepth.x, baseDepth.y, baseDepth.z);
			camera.setFrame(v_loc, v_left, v_up, v_depth);
			camera.update();
			/** Update controllers/render states/transforms/bounds for rootNode. */
            //root.updateGeometricState(tpf, true);
            
            //AudioSystem.getSystem().update();
	        
	        /** Draw the rootNode and all its children. */
			for (int i=0; i<texts.length; i++)
	        	texts[i].setTextColor(unselected);
	        texts[action].setTextColor(selected);
	        r.draw(rootNode);
	        bloods[action].render();
	        Player.render();
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
					back = true;
				if (action==1)
					save = true;
				if (action==2)
					intro = true;
			}
		}
		
		rootNode.detachAllChildren();
		KeyBindingManager.getKeyBindingManager().removeAll();
		
		if (back)
			return Main.INGAME;
		
		if (save)
			return Main.SAVE;
		
		return Main.INTRO;
	}
	
	
}
