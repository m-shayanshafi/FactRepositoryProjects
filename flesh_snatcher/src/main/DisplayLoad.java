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

import file.FSFile;
import file.FSFileList;
import world.World;

import java.util.Vector;

import jglcore.JGL_3DVector;
import jglcore.JGL_Time;

import com.jme.input.InputHandler;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;



public class DisplayLoad {
	
	private static PointLight menuLight;
	
	// Camera attributes
	private static JGL_3DVector baseLeft = new JGL_3DVector(-1f, 0f, 0f);
	private static JGL_3DVector baseUp = new JGL_3DVector(0f, 1f, 0f);
	private static JGL_3DVector baseDepth = new JGL_3DVector(0f, 0f, -1f);
	
	private static Vector3f v_loc = new Vector3f();
	private static Vector3f v_left = new Vector3f();
	private static Vector3f v_up = new Vector3f();
	private static Vector3f v_depth = new Vector3f();
	
	private static Vector files;
	
	
	
	public static void init() {
		
		try {
	        menuLight = new PointLight();
	        menuLight.setDiffuse( new ColorRGBA(1f, 1f, 1f, 1f) );
	        menuLight.setAmbient( new ColorRGBA(0.6f, 0.6f, 0.6f, 0.6f) );
	        menuLight.setLocation( new Vector3f(0f, 100f, 100f) );
	        menuLight.setEnabled(true);
		}
		catch(Exception ex) {
			
		}
	}
	
	
	
	/**
	 * 
	 * @param rootNode
	 * @param lightState
	 * @param camera
	 * @param input
	 * @return
	 */
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {
		
		files = new FSFileList().getFiles();
		
		if (files.size()>0)
			return displayLoad(rootNode, lightState, camera, input);
		else
			return displayNoFile(rootNode, lightState, camera, input);
	}
	
	
	/**
	 * 
	 * @param rootNode
	 * @param lightState
	 * @param camera
	 * @param input
	 * @return
	 */
	private static int displayLoad(Node rootNode, LightState lightState, 
									Camera camera, InputHandler input) {
		
		Node root = new Node("root");
		LightState light = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        light.setEnabled( true );
        root.setRenderState( light );
        light.attach(menuLight);
        
		int index = 0;
		
		int fontSize = (int)(DisplaySystem.getDisplaySystem().getHeight()/60f);
		int top = (int)(DisplaySystem.getDisplaySystem().getHeight()*0.8f);
		int lineHeight = (int)((15f / 600) * DisplaySystem.getDisplaySystem().getHeight());
		
		Font2D my2dfont = new Font2D();
		Text2D[] texts = new Text2D[files.size()];
		
		for (int i=0; i<files.size(); i++) {
			int test = ((FSFile)files.get(i)).getMap();
			if (test>=0 && test<World.getNameList().size()) {
				String map = "" + test;
				String mapName = (String)World.getNameList().get(test);
				texts[i] = my2dfont.createText("" + i + "  MAP : " + mapName + " (map " + map + ")", fontSize, 0);
				texts[i].setLocalTranslation(new Vector3f(50, top - (i*lineHeight), 0));
				texts[i].setRenderQueueMode(Renderer.QUEUE_ORTHO);
		        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		        zbs.setFunction(ZBufferState.TestFunction.Always);
		        texts[i].setRenderState(zbs);
		        root.attachChild(texts[i]);
			}
		}
		Text2D arrow = my2dfont.createText("->", fontSize, 0);
		arrow.setLocalTranslation(new Vector3f(30, top - (index*lineHeight), 0));
		arrow.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        zbs.setFunction(ZBufferState.TestFunction.Always);
        arrow.setRenderState(zbs);
        root.attachChild(arrow);
		
        root.updateGeometricState( 0.0f, true );
        root.updateRenderState();
        
        JGL_Time.reset();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "back", KeyInput.KEY_ESCAPE );
		KeyBindingManager.getKeyBindingManager().set( "up", KeyInput.KEY_UP );
		KeyBindingManager.getKeyBindingManager().set( "down", KeyInput.KEY_DOWN );
		KeyBindingManager.getKeyBindingManager().set( "load", KeyInput.KEY_RETURN );
		
		
		boolean back = false;
		boolean load = false;
		while (!back && !load) {
			
			InputSystem.update();
			
			/** Recalculate the framerate. */
	    	JGL_Time.update();
	        /** Update tpf to time per frame according to the Timer. */
	        float tpf = JGL_Time.getTimePerFrame();
	        
	        input.update( tpf );
	        
	        arrow.setLocalTranslation(new Vector3f(30, top - (index*lineHeight), 0));
	        
			v_loc.set(0f, 0f, 0f);
			v_left.set(baseLeft.x, baseLeft.y, baseLeft.z);
			v_up.set(baseUp.x, baseUp.y, baseUp.z);
			v_depth.set(baseDepth.x, baseDepth.y, baseDepth.z);
			camera.setFrame(v_loc, v_left, v_up, v_depth);
			camera.update();
			/** Update controllers/render states/transforms/bounds for rootNode. */
			root.updateGeometricState(tpf, true);
			
	        
			Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        r.draw(root);
	        r.displayBackBuffer();
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("back", false ) )
				back = true;
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("up", false ) ) {
				index--;
				if (index<0)
					index = files.size() - 1;
	        }
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("down", false ) ) {
				index++;
				if (index>=files.size())
					index = 0;
	        }
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("load", false ) ) {
	        	load = true;
	        }
		}
		
		KeyBindingManager.getKeyBindingManager().removeAll();
		
		if (load) {
			World.setCurrentMap(((FSFile)files.get(index)).getMap());
			return Main.LOADING;
		}
		
		return Main.INTRO;
	}
	
	
	
	/**
	 * 
	 * @param rootNode
	 * @param lightState
	 * @param camera
	 * @param input
	 * @return
	 */
	private static int displayNoFile(Node rootNode, LightState lightState, 
									Camera camera, InputHandler input) {
		
		Node root = new Node("root");
		LightState light = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        light.setEnabled( true );
        root.setRenderState( light );
        light.attach(menuLight);
        root.updateGeometricState( 0.0f, true );
        root.updateRenderState();
        
        int fontSize = (int)(DisplaySystem.getDisplaySystem().getHeight()/60f);
		int top = (int)(DisplaySystem.getDisplaySystem().getHeight()*0.8f);
		
		Font2D my2dfont = new Font2D();
		Text2D text = my2dfont.createText("No file to load", fontSize, 0);
		text.setLocalTranslation(new Vector3f(50, top, 0));
		text.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        zbs.setFunction(ZBufferState.TestFunction.Always);
        text.setRenderState(zbs);
        root.attachChild(text);
        
        root.updateGeometricState( 0.0f, true );
        root.updateRenderState();
        
        
        JGL_Time.reset();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "back", KeyInput.KEY_ESCAPE );
        
		boolean back = false;
		while (!back) {
			
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
			root.updateGeometricState(tpf, true);
			
	        
			Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the rootNode and all its children. */
	        r.draw(root);
	        r.displayBackBuffer();
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("back", false ) )
				back = true;
		}
		
		KeyBindingManager.getKeyBindingManager().removeAll();
		
		return Main.INTRO;
	}
}
