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

package ai;

import jglcore.JGL_3DVector;
import jglcore.JGL_Math;
import java.util.Vector;


/**
 * Class representing a graph of reachable points in space.
 * 
 * @author Nicolas Devere
 *
 */
public class PathGraph {
	
	private JGL_3DVector points[];
	private float weights[][];
	
	// Dantzig
	private Vector a;
	private float t[];
	private float temp[];
	private int before[];
	private Vector paths[];
	private Vector optNodes;
	
	
	
	public PathGraph(JGL_3DVector[] nodes, boolean[][] links) {
		
		points = nodes;
		
		weights = new float[points.length][points.length];
		for (int i=0; i<points.length; i++)
			for (int j=0; j<points.length; j++)
				if (links[i][j])
					weights[i][j] = (float)Math.sqrt(JGL_Math.vector_squareDistance(points[i], points[j]));
				else
					weights[i][j] = Float.POSITIVE_INFINITY;
		
		a = new Vector();
		t = new float[points.length];
		temp = new float[points.length];
		before = new int[points.length];
		paths = new Vector[points.length];
		for (int i=0; i<points.length; i++)
			paths[i] = new Vector();
		optNodes = new Vector();
	}
	
	
	
	
	
	public float searchPathDantzig(int index1, int index2, Vector result) {
		
		int i, j;
		int iStart, iEnd;
		optNodes.clear();
		
		for (i=0; i<points.length; i++) {
			t[i] = 0f;
			paths[i].clear();
		}
		
		a.clear();
		a.add(points[index1]);
		paths[index1].add(points[index1]);
		
		boolean search = true;
		
		while (search) {
			
			for (i=0; i<points.length; i++) {
				temp[i] = Float.POSITIVE_INFINITY;
				before[i] = -1;
			}
			
			float minW = Float.POSITIVE_INFINITY;
			
			for (i=0; i<a.size(); i++) {
				iStart = indexOf(a.get(i));
				float w = getOptimalNodes(iStart, optNodes);
				if (w<minW)
					minW = w;
				for (j=0; j<optNodes.size(); j++) {
					iEnd = indexOf(optNodes.get(j));
					if ((t[iStart] + minW)<temp[iEnd]) {
						temp[iEnd] = t[iStart] + minW;
						before[iEnd] = iStart;
					}
				}
			}
			
			float minT = Float.POSITIVE_INFINITY;
			for (i=0; i<points.length; i++)
				if (temp[i]<minT)
					minT = temp[i];
			for (i=0; i<points.length; i++)
				if (temp[i]==minT) {
					t[i] = temp[i];
					a.add(points[i]);
					paths[i].addAll(paths[before[i]]);
					paths[i].add(points[i]);
					if (i==index2)
						search = false;
				}
		}
		
		result.clear();
		result.addAll(paths[index2]);
		
		return t[index2];
	}
	
	
	private int indexOf(Object arg) {
		for (int i=0; i<points.length; i++)
			if (points[i]==arg)
				return i;
		return -1;
	}
	
	
	private float getOptimalNodes(int index, Vector result) {
		
		result.clear();
		
		float w = Float.POSITIVE_INFINITY;
		
		for (int i=0; i<points.length; i++)
			if (weights[index][i]<w && !a.contains(points[i]))
				w = weights[index][i];
		
		if (w!=Float.POSITIVE_INFINITY)
			for (int i=0; i<points.length; i++)
				if (weights[index][i]==w && !a.contains(points[i]))
					result.add(points[i]);
		
		return w;
	}
	
	
	public JGL_3DVector[] getPoints() {
		return points;
	}
	
}
