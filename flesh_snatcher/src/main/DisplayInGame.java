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

import world.World;
import jglcore.JGL_3DVector;
import jglcore.JGL_3DMatrix;
import jglcore.JGL_Time;
import jglcore.JGL;
import entity.Message;

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
//import audio.AudioSystem;


/**
 * 
 * @author Nicolas Devere
 *
 */
public class DisplayInGame {
	
	
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
	
	
	
	
	/**
	 * Displays the intro on the specified component.
	 * 
	 * @param screen : the display component
	 * @return the next game sequence
	 */
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {
		
		rootNode.detachAllChildren();
		rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
        
        camera.setFrame(v_loc, v_left, v_up, v_depth);
        camera.update();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        input = new PlayerHandler();
        KeyBindingManager.getKeyBindingManager().set( "pause", KeyInput.KEY_ESCAPE );
        
        Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
        JGL_3DVector pos = Player.entity.getPosition();
		JGL_3DVector or = Player.entity.getOrientation();
        
        if (!World.map.characters.contains(Player.entity))
        	World.map.addCharacter(Player.entity);
        
        JGL_Time.reset();
        
        boolean pause = false;
        
        while (!Player.entity.isDead() && !World.map.isFinished() && !(World.mode==World.KINEMATIC) && !pause) {
        	
        	// Inputs update
        	InputSystem.update();
			
			/** Recalculate the framerate. */
	    	JGL_Time.update();
	        /** Update tpf to time per frame according to the Timer. */
	        float tpf = JGL_Time.getTimePerFrame();
	        
	        input.update( tpf );
	        ((PlayerHandler)input).update();
        	
	        
	        // World update
			World.map.update();
			
			// Camera update
			matrix.identity();
			matrix.rotate(or.x + Player.entity.getShakeScreenX(), or.y, or.z, JGL.YXZ);
			
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
			camera.onFrameChange();
            
            //AudioSystem.getSystem().update();
            
			// Render
	        /** Clears the previously rendered information. */
	        r.clearBuffers();
	        /** Draw the map. */
	        World.map.render(pos);
	        /** Draw the rootNode and all its children. */
	        //r.draw(rootNode);
	        
	        v_loc.set(0f, 0f, 0f);
			v_left.set(baseLeft.x, baseLeft.y, baseLeft.z);
			v_up.set(baseUp.x, baseUp.y, baseUp.z);
			v_depth.set(baseDepth.x, baseDepth.y, baseDepth.z);
			camera.onFrameChange();
			
	        Player.render();
	        Message.render();
	        r.displayBackBuffer();
	        
	        //DisplaySystem.getDisplaySystem().setTitle("FPS : " + (int)Math.floor(1f / tpf));
	        /*DisplaySystem.getDisplaySystem().setTitle((int)Math.round(Player.entity.getPosition().x) + ", " + 
	        											(int)Math.round(Player.entity.getPosition().y) + ", " + 
	        											(int)Math.round(Player.entity.getPosition().z));*/
	        
	        
	        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("pause", false ) )
	        	pause = true;
        }
		
        
        Message.clear();
        
        KeyBindingManager.getKeyBindingManager().removeAll();
        
        if (pause) return Main.PAUSE;
        if (Player.entity.isDead()) return Main.GAME_OVER;
		if (World.map.isFinished()) return Main.LOADING;
		if (World.mode==World.KINEMATIC) return Main.KINEMATIC;
		
		
		return Main.FINISHED;
	}
	
}
