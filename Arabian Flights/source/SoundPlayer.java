// SoundPlayer.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import java.net.URL;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/** The sound player keeps a list of sound clips and plays them on demand. */
public class SoundPlayer implements LineListener
{
  // a vector of MediaContainor objects
  private Vector clips = new Vector();

  /** This loads a sound and returns an integer key to refer to the sound clip. */
  public int loadSound(URL filename)
  {
    clips.add(filename);
    return clips.size() - 1;
  }

  private Clip loadClip(URL filename)
  {
	if (GameFrame.silentMode) return null;
  	
    AudioInputStream audioInputStream = null;
    Clip clip = null;
    try
    {
      audioInputStream = AudioSystem.getAudioInputStream(filename);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (audioInputStream != null)
    {
      AudioFormat	format = audioInputStream.getFormat();
      DataLine.Info	info = new DataLine.Info(Clip.class, format);
      try
      {
        clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(this);
        clip.open(audioInputStream);
      }
      catch (LineUnavailableException e)
      {
        //e.printStackTrace();
        System.out.println("Warning - not enough voices");
        return null;
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return null;
      }
    }
    else
    {
      System.out.println("ClipPlayer.<init>(): can't get data from file " + filename);
      return null;
    }

    return clip;
  }

  private Clip getSound(int key)
  {
    return (Clip)clips.get(key);
  }

  /** This plays a specified sound.*/
  public void playSound(int key, Particle source)
  {
	if (GameFrame.silentMode) return;
	
    float distance = source.distance(GameFrame.player);

    if (distance > GameFrame.SOUND_MAX_DISTANCE) return;

    Clip clip = loadClip((URL)clips.get(key));
    if (clip == null) return;

    clip.start();
  }

  /** Needed for the interface, but unused. */
  public void update(LineEvent event)
  {

  }

}