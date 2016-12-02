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

package world;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Vector;

import script.Script;

import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

import jglanim.JGL_Keyframe;
import jglanim.JGL_KeyframesArray;
import jglcore.JGL_Time;


/**
 * Class representing an in-game kinematic.
 * 
 * @author Nicolas Devere
 *
 */
public class Kinematic {
	
	private float endDate;
	private float speed;
	private float t;
	
	private JGL_KeyframesArray kfs;
	private JGL_Keyframe kf;
	
	//private Vector dates;
	//private Vector scripts;
	
	private Vector nodes;
	private Vector scripts;
	
	private boolean finished;
	
	
	/**
	 * Constructor.
	 * 
	 * @param nodes
	 * @param dates
	 * @param scripts
	 * @param kfs
	 * @param speed
	 * @param endDate
	 */
	public Kinematic(	Vector nodes, JGL_KeyframesArray kfs, float speed, float endDate, Vector scripts) {
		
		this.nodes = nodes;
		//this.scripts = scripts;
		//this.dates = dates;
		this.kfs = kfs;
		this.speed = speed;
		this.endDate = endDate;
		this.scripts = scripts;
		
		kf = new JGL_Keyframe();
		
		reset();
	}
	
	
	public void reset() {
		
		for (int i=0; i<nodes.size(); i++) {
			Node node = (Node)nodes.get(i);
			ArrayList controllers = node.getChild(0).getControllers();
			if (!controllers.isEmpty()) {
				Controller kc = (Controller)controllers.get(0);
				if (kc!=null) {
					kc.setMaxTime(kc.getMaxTime());
					kc.setMinTime(kc.getMinTime());
				}
			}
		}
		
		t = 0f;
		kfs.interpolationLinear(t, kf);
		finished = false;
	}
	
	
	public JGL_Keyframe getCameraKeyframe() {
		return kf;
	}
	
	
	
	public void render() {
		for (int i=0; i<nodes.size(); i++) {
			Node node = (Node)nodes.get(i);
			node.updateGeometricState(JGL_Time.getTimePerFrame(), true);
			DisplaySystem.getDisplaySystem().getRenderer().draw(node);
		}
		
		kfs.interpolationLinear(t, kf);
		
		t += speed * JGL_Time.getTimer();
		if (t>endDate)
			finished = true;
	}
	
	
	public boolean isFinished() {
		return finished;
	}
	
	
	/**
	 * Executes the final scripts.
	 */
	public void finalScripts() {
		for (int i=0; i<scripts.size(); i++)
			Script.execute(new StringTokenizer((String)scripts.get(i)));
	}
	
}
