package thaigo.utility;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/** Loads a sound and become the player. Can play or stop the sound or play in loop.
 * 
 * @author Nol 5510546018
 * @version 2013.04.11
 */
public class AudioPlayer{
	
	/** The audio player. */
	private AudioClip player;
	
	/** Loads the sound and initializes the player.
	 * 
	 * @param path Location of sound file
	 */
	public AudioPlayer(String path){
		try{
			ClassLoader loader = this.getClass().getClassLoader();
			URL url = loader.getResource(path);
			player = Applet.newAudioClip(url);
		}
		catch(Exception e){}
	}
	
	/** Plays the sound in loop. */
	public void loop() {
		player.loop();
	}

	/** Stops the sound. */
	public void stop() {
		player.stop();
	}

	/** Plays the sound. */
	public void play() {
		player.play();
	}
}
