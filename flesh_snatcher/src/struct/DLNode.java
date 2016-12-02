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

package struct;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.image.Texture;
import com.jme.renderer.Renderer;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

import java.util.Vector;
import jglcore.JGL_Time;


public final class DLNode extends Node {
	
	private static final long serialVersionUID = 1L;
	
	public static int LOOP 		= 0;
	public static int REVERSE	= 1;
	private int mode;
	
	private boolean animated;
	private boolean fwd;
	private float currentTime;
	private float maxTime;
	private float step;
	
	private Texture bkTexture;
	private Texture textures[];
	
	
	public DLNode(String name, Node triNode, Texture[] lightmaps) {
		super(name);
		
		bkTexture = ((TextureState)triNode.getChild(0).getRenderState(RenderState.StateType.Texture)).getTexture(1);
		Vector list = new Vector();
		for (int i=0; i<triNode.getChildren().size(); i++)
			list.add(triNode.getChild(i));
		for (int i=0; i<list.size(); i++)
			attachChild((Spatial)list.get(i));
		
        textures = lightmaps;
		
		mode = REVERSE;
		animated = false;
		fwd = true;
	}
	
	
	public void preloadAnimation() {
		for (int i=0; i<textures.length; i++) {
			for (int j=0; j<getChildren().size(); j++) {
				((TextureState)getChild(j).getRenderState(RenderState.StateType.Texture)).setTexture(textures[i], 1);
			}
			updateRenderState();
	        super.draw(DisplaySystem.getDisplaySystem().getRenderer());
		}
		for (int i=0; i<getChildren().size(); i++)
			((TextureState)getChild(i).getRenderState(RenderState.StateType.Texture)).setTexture(bkTexture, 1);
	}
	
	
	public void startAnimation(float fps) {
		animated = true;
		fwd = true;
		currentTime = 0f;
		step = fps;
		maxTime = textures.length / step;
	}
	
	public void stopAnimation() {
		animated = false;
		for (int i=0; i<getChildren().size(); i++)
			((TextureState)getChild(i).getRenderState(RenderState.StateType.Texture)).setTexture(bkTexture, 1);
	}
	
	
	public void setAnimationTexture(int index) {
		if (index<0)
			index = 0;
		if (index>=textures.length)
			index = textures.length - 1;
		
		animated = false;
		for (int i=0; i<getChildren().size(); i++)
			((TextureState)getChild(i).getRenderState(RenderState.StateType.Texture)).setTexture(textures[index], 1);
	}
	
	
	public void draw(Renderer r) {
		if (animated) {
			currentTime += JGL_Time.getTimePerFrame();
			while (currentTime>=maxTime) {
				currentTime -= maxTime;
				fwd = !fwd;
			}
			int index = (int)Math.floor(currentTime * step);
			if (mode==REVERSE && !fwd) index = textures.length - 1 - index;
			for (int i=0; i<getChildren().size(); i++)
				((TextureState)getChild(i).getRenderState(RenderState.StateType.Texture)).setTexture(textures[index], 1);
		}
		super.draw(r);
	}
}
