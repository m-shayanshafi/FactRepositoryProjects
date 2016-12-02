//	Copyright 2008 Nicolas Devere
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

package jglcore;

import com.jme.util.NanoTimer;


/**
 * Real-time manager.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL_Time {
	
	private static float tpf;
	private static float rtFactor;
	private static float minTime;
	private static long timeAbsolute;
	
	private static NanoTimer timer;
	private static NanoTimer timerLimiter;
	private static NanoTimer timerAbsolute;
	
	/**
	 * Resets the real time system.
	 */
	public static final void reset() {
		timer = new NanoTimer();
		timer.reset();
		
		timerLimiter = new NanoTimer();
		timerLimiter.reset();
		
		timerAbsolute = new NanoTimer();
		timerAbsolute.reset();
		
		tpf = 1f / 60f;
		rtFactor = 1f;
		minTime = 1f / 75f;
		timeAbsolute = (long)(1000f / 60f);
	}
	
	
	/**
	 * Computes the real time factor since the last call of this method.
	 * A 1 value means a 60 frames/second frame-rate.
	 */
	public static final void update() {
		
		if (timer.getTimePerFrame()<minTime) {
			timerLimiter.reset();
			while ((timer.getTimePerFrame() + timerLimiter.getTimeInSeconds())<minTime) {}
		}
		
		tpf = timer.getTimePerFrame();
		rtFactor = tpf * 60f;
		timer.update();
		
		timeAbsolute = (long)(timerAbsolute.getTime() * 0.000001f);
	}
	
	
	/**
	 * Returns the current time per frame in seconds.
	 * @return the current time per frame in seconds
	 */
	public static final float getTimePerFrame() {
		return tpf;
	}
	
	
	/**
	 * Returns the real-time factor.
	 * 
	 * @return the real-time factor
	 */
	public static final float getTimer() {
		return rtFactor;
	}
	
	
	/**
	 * Returns the current time since the last reset() call, in milliseconds.
	 * 
	 * @return the current time since the last reset() call, in milliseconds.
	 */
	public static final long getTimeInMillis() {
		return timeAbsolute;
	}
}
