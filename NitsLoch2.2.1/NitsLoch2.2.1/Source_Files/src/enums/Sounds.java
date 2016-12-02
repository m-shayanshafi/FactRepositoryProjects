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

package src.enums;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Enum for sounds that play during the game.
 * @author Darren Watts
 * date 2/15/08
 */
public enum Sounds {
	ENEMY_HIT,
	PLAYER_HIT,
	ENEMY_HIT_DUN,
	PLAYER_HIT_DUN,
	SCENARIO_INFO,
	ENEMY_MELEE_ATTACK,
	ENEMY_MART_ARTS_ATTACK,
	ENEMY_MARKSMAN_ATTACK,
	ENEMY_FLAME_ATTACK,
	ENEMY_OTHER_ATTACK,
	PLAYER_MELEE_ATTACK,
	PLAYER_MART_ARTS_ATTACK,
	PLAYER_MARKSMAN_ATTACK,
	PLAYER_FLAME_ATTACK,
	PLAYER_OTHER_ATTACK,
	EXPLOSION,
	PLAYER_DIES,
	ENEMY_DIES,
	PICKUP_ITEM;

	private AudioClip sound;
	private boolean isUsed = false;
	private String path;
	//private AudioStream as;
	//private byte[] soundBytes;

	/**
	 * Sets the sound of this type with the path to the sound file.
	 * @param path String : path to sound file
	 */
	public void setSound(String path){
		try {
			URL u;
			u = new URL("file:///" + System.getProperty("user.dir") + "/" + path);
			sound = Applet.newAudioClip(u);
			/*if(src.Constants.SCENARIO_DEBUG){
				u = new URL("file:///" + System.getProperty("user.dir") + "/" + path);
				sound = Applet.newAudioClip(u);
			}
			else {
				soundBytes = ScenarioFile.getInstance().getByteArray(path);
			}*/
			isUsed = true;
			this.path = path;
		} catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Cannot find sound: " + path);
		}
	}

	/**
	 * Sets the stream in the sound thread.
	 */
	public void playSound(){
		try{
			if(sound != null)
				src.game.SoundThread.getInstance().setSound(sound);
			/*if(src.Constants.SCENARIO_DEBUG)
				sound.play();
			else {
				as = new AudioStream(new ByteArrayInputStream(soundBytes));
				src.game.SoundThread.getInstance().setAudioStream(as);
			}*/
		} catch(Exception ex) { }
	}
	
	public boolean getUsed() {
		return isUsed;
	}
	
	public String getPath() {
		return path;
	}
	
	public static void clearAll() {
		for(Sounds s : Sounds.values()) {
			s.path = "";
		}
	}

}
