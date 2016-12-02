//	Copyright 2008 - 2010 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package jglanim;

import java.util.Vector;

import jglcore.JGL_Math;


/**
 * Represents a keyframes array list. 
 * A JGL_KeyframesArray can store several keyframes and manage 
 * linear and cubic interpolations along the list.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL_KeyframesArray {
	
	private JGL_Keyframe[] array;
	
	private int minKey;
	private int maxKey;
	private int length;
	
	
	/**
	 * Constructs a new keyframes array list.
	 */
	public JGL_KeyframesArray() {
		clear();
	}
	
	
	
	/**
	 * Adds a keyframe to the list.
	 * 
	 * @param keyframe : the keyframe to add
	 */
	public void add(JGL_Keyframe keyframe) {
		
		Vector list = new Vector();
		for (int i=0; i<array.length; i++)
			list.add(array[i]);
		list.add(keyframe);
		array = (JGL_Keyframe[])list.toArray(array);
		
		setAnimationFrames(0, array.length - 1);
	}
	
	
	/**
	 * Returns the keyframes number in the list.
	 * 
	 * @return the keyframes number
	 */
	public int size() {
		return array.length;
	}
	
	
	/**
	 * Returns the keyframe at the specified position in the list, 
	 * or null if the list is empty.
	 * 
	 * @param index : the keyframe's index
	 * @return the keyframe or null if the list is empty
	 */
	public JGL_Keyframe get(int index) {
		if(array.length == 0)
			return null;
		
		if(index < 0)
			index = 0;
		
		if(index >= array.length)
			index = array.length - 1;
		
		return array[index];
		
	}
	
	
	/**
	 * Removes the keyframe at the specified position in the list.
	 * 
	 * @param index : the keyframe's index to remove
	 */
	public void remove(int index) {
		if(index>=0 && index<array.length) {
			Vector list = new Vector();
			for(int i=0; i<array.length; i++)
				list.add(array[i]);
			list.remove(index);
			array = (JGL_Keyframe[])list.toArray(array);
		}
		setAnimationFrames(0, array.length - 1);
	}
	
	
	/**
	 * Removes all the keyframes from the list.
	 */
	public void clear() {
		array = new JGL_Keyframe[0];
	}
	
	
	
	public void setAnimationFrames(int minKeyframe, int maxKeyframe) {
		
		if (maxKeyframe<minKeyframe) return;
		if (minKeyframe<0) minKeyframe = 0;
		if (maxKeyframe<0) maxKeyframe = 0;
		if (minKeyframe>array.length - 1) minKeyframe = array.length - 1;
		if (maxKeyframe>array.length - 1) maxKeyframe = array.length - 1;
		
		minKey = minKeyframe;
		maxKey = maxKeyframe;
		length = (maxKey - minKey) + 1;
	}
	
	
	/**
	 * Assigns to <code>result</code> the list's linear interpolation keyframe 
	 * according to the specified parametric variable.
	 * 
	 * @param p : the parametric variable
	 * @param result : the interpolated keyframe result
	 */
	public final void interpolationLinear(float p, JGL_Keyframe result) {
		int i1, i2;
		float paramRelatif;
		
		if (array.length==0) return;
		
		while (p < minKey)
			p += length;
		
		while (p >= maxKey + 1)
			p -= length;
		
		i1 = (int)Math.floor(p);
		if (i1 != maxKey)
			i2 = i1 + 1;
		else
			i2 = minKey;
		
		paramRelatif = p - i1;
		JGL_Math.vector_interpolationLinear(array[i1].position, array[i2].position, paramRelatif, result.position);
		JGL_Math.vector_interpolationLinear(array[i1].orientation, array[i2].orientation, paramRelatif, result.orientation);
	}
	
	
	/**
	 * Assigns to <code>result</code> the list's cubic interpolation keyframe 
	 * according to the specified parametric variable.
	 * 
	 * @param p : the parametric variable
	 * @param result : the interpolated keyframe result
	 */
	public final void interpolationCubic(float p, JGL_Keyframe result) {
		int i1, i2, i3, i4;
		float paramRelatif;
		
		if (array.length==0) return;
		
		while (p < minKey)
			p += length;
		
		while (p >= maxKey + 1)
			p -= length;
		
		i1 = (int)Math.floor(p);
		if (i1 != maxKey)
			i2 = i1 + 1;
		else
			i2 = minKey;
		
		if (i2 != maxKey)
			i3 = i2 + 1;
		else
			i3 = minKey;
		
		if (i3 != maxKey)
			i4 = i3 + 1;
		else
			i4 = minKey;
		
		paramRelatif = p - i1;
		JGL_Math.vector_interpolationCubic(	array[i1].position, array[i2].position, 
											array[i3].position, array[i4].position, 
											paramRelatif, result.position);
		JGL_Math.vector_interpolationCubic(	array[i1].orientation, array[i2].orientation, 
											array[i3].orientation, array[i4].orientation, 
											paramRelatif, result.orientation);
	}

}
