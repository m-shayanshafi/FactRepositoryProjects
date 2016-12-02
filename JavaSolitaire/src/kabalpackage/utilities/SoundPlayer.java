package kabalpackage.utilities;

import java.io.*;
import kabalpackage.*;
import sun.audio.*;


/**
 * Runnable class that plays the sound effects in the game.
 */
public class SoundPlayer implements Runnable{
    
    private AudioStream sound;
    private InputStream soundStream;
    
    // Variable indicating whether or not the sound has been played. We need
    // this to be able to let the run() method finish, and thereby terminate
    // the thread after a sound has been played.
    private boolean SOUND_PLAYED = false;
    
    /**
     * Creates a new instance of SoundPlayer
     */
    public SoundPlayer(){        
        try{
            soundStream = getClass().getResourceAsStream("sounds/card.wav");
            sound = new AudioStream(soundStream);
        }
        catch(IOException ioe){
            System.err.println("IOE");
        }
        catch(NullPointerException npe){
            System.err.println("NPE");
        }
        
    }
    
    /**
     * Plays the sound.
     */
    synchronized public void playSound() {
        AudioPlayer.player.start(sound);
        SOUND_PLAYED = true;
    }
    

    public void run() {

        while(!SOUND_PLAYED){
            try {
                // We don't want the loop to loop constantly, even if we play
                // the sound instantly upon creation of a SoundPlayer thread.
                Thread.sleep(1000);
            } 
            catch(Exception e) {}
        }   
        
    }

}
