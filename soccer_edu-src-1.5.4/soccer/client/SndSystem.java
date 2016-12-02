/* SndSystem.java
   This class manages the musci and sound effects of TOS

   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
	
	Modifications by Vadim Kyrylov 
							January 2006
*/

package soccer.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

public class SndSystem {

	private Hashtable clips;
	private ArrayList music;
	private boolean musicOn;
	private boolean soundOn;
	private AudioClip currentMusic;

	public SndSystem() {
		clips = new Hashtable();
		music = new ArrayList();
		musicOn = false;
		soundOn = true;
		currentMusic = null;

	}

	public void playClip(String id) {

		AudioClip clip = (AudioClip) clips.get(id);
		if (clip == null) {
			URL clipURL; 
			try {				
				clipURL =
					SoccerMaster.class.getResource("/sound/" + id + ".wav");
			} catch (Exception e ) {
				clipURL =
					SoccerMaster.class.getResource("/sound/" + id + ".mp3");
			}
			try {				
				clip = Applet.newAudioClip(clipURL);
				clips.put(id, clip);
			} catch (Exception e ) {
				//System.out.println("invalid audio clipURL = " + clipURL);
			}
		}
		if (clip != null && soundOn)
			clip.play();
	}

	/**
	 * @return boolean musicOn
	 */
	public boolean isMusicOn() {
		return musicOn;
	}

	private void playMusic() {
		if (music.size() > 0) {
			stopMusic();

			int pos = (int) Math.floor((Math.random() * music.size()));
			currentMusic = (AudioClip) music.get(pos);
			currentMusic.loop();
		}
	}

	private void stopMusic() {
		if (currentMusic != null)
			currentMusic.stop();
		currentMusic = null;
	}

	/**
	 * @param b musicOn
	 */
	public void setMusicOn(boolean b) {

		musicOn = b;
		if (musicOn)
			playMusic();
		else
			stopMusic();
	}

	public void addMusic(String name) {
		URL musicURL = SoccerMaster.class.getResource("/midi/" + name + ".mid");
		AudioClip musicClip = Applet.newAudioClip(musicURL);
		music.add(musicClip);
	}

	/**
	 * @return boolean effectsOn
	 */
	public boolean isSoundOn() {
		return soundOn;
	}

	/**
	 * @param b effectsOn
	 */
	public void setSoundOn(boolean b) {
		soundOn = b;
	}

}
