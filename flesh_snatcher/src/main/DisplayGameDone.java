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

import audio.AudioSystem;


public class DisplayGameDone {
	
	
	private static Font2D my2dfont;
	private static Text2D congrats;
	private static Text2D thanks;
	private static Text2D line01;
	private static Text2D line02;
	private static Text2D line03;
	private static Text2D line04;
	private static Text2D line05;
	
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
		
		my2dfont = new Font2D();
		my2dfont.getFontTextureState().getTexture().setApply(Texture.ApplyMode.Combine);
		
		congrats = my2dfont.createText("Congratulations !!", 10f, 0);
		congrats.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.75f, 0f));
		congrats.setLocalScale(height / 300f);
		congrats.updateGeometricState( 0.0f, true );
		congrats.updateRenderState();
		
		thanks = my2dfont.createText("Thanks for testing this demo !", 10f, 0);
		thanks.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.65f, 0f));
		thanks.setLocalScale(height / 300f);
		thanks.updateGeometricState( 0.0f, true );
		thanks.updateRenderState();
		
		line01 = my2dfont.createText("Conception / Programming / 3D design : Nicolas Devere", 10f, 0);
		line01.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.55f, 0f));
		line01.setLocalScale(height / 600f);
		line01.updateGeometricState( 0.0f, true );
		line01.updateRenderState();
		
		line02 = my2dfont.createText("2D design / 3D design : Thibaud Giovannetti, egaronia, Sake", 10f, 0);
		line02.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.5f, 0f));
		line02.setLocalScale(height / 600f);
		line02.updateGeometricState( 0.0f, true );
		line02.updateRenderState();
		
		line03 = my2dfont.createText("Musics : Gregory Devere", 10f, 0);
		line03.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.45f, 0f));
		line03.setLocalScale(height / 600f);
		line03.updateGeometricState( 0.0f, true );
		line03.updateRenderState();
		
		line04 = my2dfont.createText("Thanks to : linkboss, Julien \"TUER Man\" Gouesse, ", 10f, 0);
		line04.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.35f, 0f));
		line04.setLocalScale(height / 600f);
		line04.updateGeometricState( 0.0f, true );
		line04.updateRenderState();
		
		line05 = my2dfont.createText("XCodetam, Merwyyn", 10f, 0);
		line05.setLocalTranslation(new Vector3f(width * 0.01f, height * 0.3f, 0f));
		line05.setLocalScale(height / 600f);
		line05.updateGeometricState( 0.0f, true );
		line05.updateRenderState();
	}
	
	
	public static int display(Node rootNode, LightState lightState, 
								Camera camera, InputHandler input) {

		rootNode.detachAllChildren();
		rootNode.attachChild(congrats);
		rootNode.attachChild(thanks);
		rootNode.attachChild(line01);
		rootNode.attachChild(line02);
		rootNode.attachChild(line03);
		rootNode.attachChild(line04);
		rootNode.attachChild(line05);
		lightState.detachAll();
        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
		
		KeyBindingManager.getKeyBindingManager().removeAll();
		input = new DummyHandler();
		KeyBindingManager.getKeyBindingManager().set( "intro", KeyInput.KEY_RETURN );
		
		JGL_Time.reset();
		
		Script.execute(new StringTokenizer("stopmusic"));
		Script.execute(new StringTokenizer("playmusic data/map/music/maison_des_saints.ogg 1"));
		
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
			rootNode.updateGeometricState(tpf, true);
			
			AudioSystem.getSystem().update();

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
