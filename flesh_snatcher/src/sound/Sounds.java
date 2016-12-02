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

package sound;

import java.util.Vector;
import audio.AudioTrack;
import audio.AudioTrack.TrackType;
import input.LoadHelper;


/**
 * Class storing the game sounds list and the current music.
 * 
 * @author Nicolas Devere
 *
 */
public final class Sounds {
	
	private static AudioTrack music = null;
	
	private static Vector ids = new Vector();
	private static Vector sounds = new Vector();
	
	
	/**
	 * Class representing a single sound. 
	 * This one can be copied to be played correctly.
	 * 
	 * @author Nicolas Devere
	 *
	 */
	public static class SoundBuffer {
		
		private int depth;
		private int index;
		private AudioTrack[] buffer;
		
		/**
		 * Constructor.
		 * 
		 * @param path : the path of the sound file
		 * @param copies : the number of copy instances in the buffer
		 */
		public SoundBuffer(String path, int copies, float volume) throws Exception {
			
			try {
				
				if (copies<=0)
					throw new Exception("Copies number <= 0 !");
				
				if (volume<0f) volume = 0f;
				if (volume>1f) volume = 1f;
				
				depth = copies;
				index = 0;
				buffer = new AudioTrack[depth];
				int i;
				
				for (i=0; i<depth; i++) {
					buffer[i] = LoadHelper.getAudioTrack(path, false);
					buffer[i].setEnabled(true);
					buffer[i].setVolume(volume);
					buffer[i].setTargetVolume(volume);
				}
			}
			catch(Exception ex) {
				ex.printStackTrace(System.out);
				throw ex;
			}
		}
		
		
		/**
		 * Plays the sound.
		 */
		public void play() {
			buffer[index].stop();
			buffer[index].play();
			if ( (++index)>=depth)
				index = 0;
		}
		
		
		/**
		 * Plays the sound in an infinite loop.
		 */
		public void loop() {
			stop();
			index = 0;
			//buffer[index].loop();
		}
		
		
		/**
		 * Plays the sound in a loop during the specified turns.
		 * 
		 * @param count : the loop turns
		 */
		public void loop(int count) {
			stop();
			index = 0;
			//buffer[index].loop(count);
		}
		
		
		/**
		 * Stops and resets the sound.
		 */
		public void stop() {
			for (int i=0; i<depth; i++) {
				buffer[i].stop();
			}
		}
		
		
		/**
		 * Clears the buffer.
		 */
		public void clear() {
			
			for (int i=0; i<depth; i++) {
				buffer[i].stop();
				buffer[i] = null;
			}
		}
		
		
		/**
		 * Returns the number of instances in the buffer.
		 * @return the number of instances in the buffer
		 */
		public int getSize() {
			return depth;
		}
	}
	
	
	
	
	/**
	 * Adds a sound to the list given its file path, its ID and its copies number.
	 * 
	 * @param path : the sound path
	 * @param id : the sound ID
	 * @param copies : the number of copies
	 */
	public static void add(String path, String id, int copies, float volume) {
		SoundBuffer sb;
		
		try {
			sb = new SoundBuffer(path, copies, volume);
			sounds.add(sb);
			ids.add(id);
		}
		catch (Exception ex) {
			sb = null;
			return;
		}
	}
	
	
	/**
	 * Plays the specified sound.
	 * 
	 * @param id : the sound ID
	 */
	public static void play(String id) {
		
		for (int i=0; i<ids.size(); i++)
			if ( ((String)ids.get(i)).equals(id) ) {
				((SoundBuffer)sounds.get(i)).play();
				return;
			}
	}
	
	
	/**
	 * Plays the specified sound in an infinite loop.
	 * 
	 * @param index : the sound ID
	 */
	public static void loop(String id) {
		
		for (int i=0; i<ids.size(); i++)
			if ( ((String)ids.get(i)).equals(id) ) {
				((SoundBuffer)sounds.get(i)).loop();
				return;
			}
	}
	
	
	/**
	 * Stops the specified sound.
	 * 
	 * @param index : the sound ID
	 */
	public static void stop(String id) {
		
		for (int i=0; i<ids.size(); i++)
			if ( ((String)ids.get(i)).equals(id) ) {
				((SoundBuffer)sounds.get(i)).stop();
				return;
			}
	}
	
	
	/**
	 * Plays the specified music.
	 * @param path : the music file path
	 * @param volume : the music volume
	 */
	public static void playMusic(String path, float volume) {
		
		try {
	        music = LoadHelper.getAudioTrack(path, true);
	        music.setType(TrackType.MUSIC);
	        music.setRelative(true);
	        music.setVolume(volume);
	        music.setTargetVolume(volume);
	        music.setLooping(true);
	        music.play();
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return;
		}
	}
	
	
	/**
	 * Stops the music currently played.
	 */
	public static void stopMusic() {
		if (music!=null) {
			music.stop();
		}
	}
	
	
	
	
	/**
	 * Clears the sounds list.
	 */
	public static void clear() {
		
		for (int i=0; i<sounds.size(); i++)
			((SoundBuffer)sounds.get(i)).clear();
		
		ids.clear();
		sounds.clear();
	}
	
}
