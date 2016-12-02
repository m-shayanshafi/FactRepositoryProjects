/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.game;

import java.applet.AudioClip;

/**
 * A thread to deal with Sounds.  Currently, this is only active
 * when you are using a .nits scenario file.  If you are in debug
 * mode, it will not use a separate thread for sounds.
 * @author Darren Watts
 * 2/23/08
 *
 */
public class SoundThread implements Runnable{
	private static SoundThread instance;
	//private AudioStream as;
	private AudioClip sound;
	private boolean running;
	private boolean playing = false;

	/**
	 * Gets the instance of the sound thread.
	 * @return SoundThread : the instance
	 */
	public static SoundThread getInstance(){
		if(instance == null)
			instance = new SoundThread();
		return instance;
	}

	/**
	 * Constructor for the sound thread.  Sets the running flag to true.
	 */
	private SoundThread(){
		running = true;
	}

	public synchronized void setSound(AudioClip sound){
		this.sound = null;
		this.sound = sound;
		playing = true;
	}

	/**
	 * While running, play the current audio stream.
	 */
	public void run(){
		while(running){
			try {
				if(playing){
					playing = false;
					sound.play();
				}
				Thread.sleep(30);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}