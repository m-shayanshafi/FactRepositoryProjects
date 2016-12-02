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
import com.jme.system.DisplaySystem;
import com.jmex.model.animation.KeyframeController;

import jglcore.JGL_Time;


public class Explode3D implements Explode {
	
	private float posX;
	private float posY;
	private float posZ;
	
	private float rx;
	private float ry;
	private float rz;
	
	private Node node;
	private KeyframeController kc;
	private float kspeed;
	private int nbFrames;
	private float t;
	private int minKey;
	private int maxKey;
	private int length;
	
	private long start;
	private long end;
	private long cumulTime;
	private boolean isDrawing;
	private boolean isFinished;
	
	
	public Explode3D(Node protoNode, long startTime, long endTime, float relativeX, float relativeY, float relativeZ) {
		
		posX = 0f;
		posY = 0f;
		posZ = 0f;
		
		rx = relativeX;
		ry = relativeY;
		rz = relativeZ;
		
		node = protoNode;
		if (protoNode.getChild(0).getControllerCount()>0) {
			kc = (KeyframeController)protoNode.getChild(0).getController(0);
			kspeed = kc.getSpeed();
			nbFrames = kc.keyframes.size();
		}
		else {
			kc = null;
			kspeed = 0f;
			nbFrames = 1;
		}
		setAnimationFrames(0, nbFrames - 1);
		
		start = startTime;
		end = endTime;
		reset();
	}
	
	
	/**
	 * Sets the min and max keyframes for the animation to play.
	 * 
	 * @param minKeyframe : the min keyframe
	 * @param maxKeyframe : the max keyframe
	 */
	public void setAnimationFrames(int minKeyframe, int maxKeyframe) {
		if (maxKeyframe<minKeyframe) return;
		if (minKeyframe<0) minKeyframe = 0;
		if (maxKeyframe<0) maxKeyframe = 0;
		if (minKeyframe>nbFrames - 1) minKeyframe = nbFrames - 1;
		if (maxKeyframe>nbFrames - 1) maxKeyframe = nbFrames - 1;
		
		minKey = minKeyframe;
		maxKey = maxKeyframe;
		length = (maxKey - minKey);
		
		t = minKey;
	
	}
	
	
	@Override
	public void setPosition(float x, float y, float z) {
		posX = x + rx;
		posY = y + ry;
		posZ = z + rz;
	}
	
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return isFinished;
	}
	
	@Override
	public void synchronizeNode() {
		if (isDrawing)
			if (length>0) {
				t += JGL_Time.getTimePerFrame() * kspeed;
				while (t >= maxKey)
					t -= length;
			}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if (isDrawing) {
			if (kc!=null)
				kc.setCurTime(t);
			node.setLocalTranslation(posX, posY, posZ);
			node.updateGeometricState(0f, true);
			DisplaySystem.getDisplaySystem().getRenderer().draw(node);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		setAnimationFrames(0, nbFrames - 1);
		isFinished = isDrawing = false;
		cumulTime = 0l;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		cumulTime += (long)(JGL_Time.getTimePerFrame() * 1000f);
		if (cumulTime >= start)
			isDrawing = true;
		if (cumulTime > end) {
			isFinished = true;
			isDrawing = false;
		}
	}
	
	
	public Object clone() {
		return new Explode3D(node, start, end, rx, ry, rz);
	}
}
