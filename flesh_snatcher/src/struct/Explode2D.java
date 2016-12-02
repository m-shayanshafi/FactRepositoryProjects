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

import jglcore.JGL_Time;

import com.jme.image.Texture;
import com.jme.scene.BillboardNode;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;


/**
 * 
 * @author Nicolas Devere
 *
 */
public class Explode2D implements Explode {
	
	private float posX;
	private float posY;
	private float posZ;
	private float rx;
	private float ry;
	private float rz;
	private Texture tex;
	private BillboardNode node;
	private long start;
	private long end;
	private long cumulTime;
	private boolean isDrawing;
	private boolean isFinished;
	
	private int wt;
	private int ht;
	private float xtr[];
	private float ytr[];
	private long timeStep;
	
	
	
	public Explode2D(	BillboardNode billboardNode, int wt, int ht, 
						long startTime, long endTime, float relativeX, float relativeY, float relativeZ) {
		
		node = billboardNode;
		tex = ((TextureState)node.getChild(0).getRenderState(RenderState.StateType.Texture)).getTexture(0);
		
		posX = 0f;
		posY = 0f;
		posZ = 0f;
		
		rx = relativeX;
		ry = relativeY;
		rz = relativeZ;
		
		this.wt = wt;
		this.ht = ht;
		float xe = 1f / (float)wt;
		float ye = 1f / (float)ht;
		
		timeStep = (long)((float)(endTime - startTime) / (float)(wt * ht));
		
		int index = 0;
		xtr = new float[wt * ht];
		ytr = new float[wt * ht];
		for (int j=ht-1; j>=0; j--)
			for (int i=0; i<wt; i++) {
				xtr[index] = i * xe;
				ytr[index] = j * ye;
				index++;
			}
		
		start = startTime;
		end = endTime;
		
		reset();
	}
	
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return isFinished;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if (cumulTime >= start) {
			isDrawing = true;
			int index = (int)Math.floor( (float)(cumulTime - start) / (float)timeStep);
			if (index>=xtr.length) index = xtr.length - 1;
			tex.getTranslation().x = xtr[index];
			tex.getTranslation().y = ytr[index];
		}
		if (cumulTime > end) {
			isFinished = true;
			isDrawing = false;
		}
		if (isDrawing) {
			node.setLocalTranslation(posX, posY, posZ);
			node.updateGeometricState(0f, true);
			DisplaySystem.getDisplaySystem().getRenderer().draw(node);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		isFinished = isDrawing = false;
		cumulTime = 0l;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		// TODO Auto-generated method stub
		posX = x + rx;
		posY = y + ry;
		posZ = z + rz;
	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub
		//if (isDrawing)
		//	node.updateGeometricState(JGL_Time.getTimePerFrame(), true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		cumulTime += (long)(JGL_Time.getTimePerFrame() * 1000f);
	}
	
	
	public Object clone() {
		return new Explode2D(node, wt, ht, start, end, rx, ry, rz);
	}
	
}
