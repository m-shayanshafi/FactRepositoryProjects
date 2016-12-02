/*
 * VideoToons. A Tribute to old Video Games.
 * Copyright (C) 2001 - Bertrand Le Nistour
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package videotoons.game;


//import javax.sound.midi.*;
//import javax.sound.sampled.*;
import java.io.*;


public class SoundLibrary implements javax.sound.midi.MetaEventListener
{
    // some definitions
      public static final byte MAX_VOLUME   = 80;
      public static final byte MUSIC_VOLUME = 100;
      public static final short SOUND_VOLUME = 150;

      public static final byte BUTTON_SOUND      = 0;
      public static final byte JUMP_SOUND        = 1;
      public static final byte HARD_HIT_SOUND    = 2;
      public static final byte HAMMER_SOUND      = 3;
      public static final byte LOW_HURT_SOUND    = 4;
      public static final byte HIGH_HURT_SOUND   = 5;
      public static final byte COLAPSING_SOUND   = 6;
      public static final byte DEAD_SOUND        = 7;

      public static final byte FILLEDHOLE_SOUND  = 8;
      public static final byte LASER_SOUND       = 9;
      public static final byte PAUSE_SOUND       = 10;
      public static final byte DEFEAT_SOUND      = 11;
      public static final byte GAMEOVER_SOUND    = 12;
      public static final byte BROKENSTONE_SOUND = 13;
      public static final byte LIGHT_HIT_SOUND   = 14;
      public static final byte ATTACK_SOUND      = 15;
      public static final byte HELLO_SOUND       = 16;
      public static final byte LONG_HURT_SOUND   = 17;

      public static final byte LAND_SOUND        = 0;

      public static final byte NB_SOUND_SAMPLES  = 18;

   /*******************************************************************************/

    // current_ music, sounds & sequencer
       private javax.sound.midi.Sequencer sequencer;

       private BufferedInputStream current_music;
       private javax.sound.sampled.Clip sounds[];

       private javax.sound.midi.Synthesizer synthesizer;
       private javax.sound.midi.MidiChannel channels[];

   /*********************************************************************************/

      public SoundLibrary()
      {
         open();

         sounds = new javax.sound.sampled.Clip[NB_SOUND_SAMPLES];

         for(int i=0; i<NB_SOUND_SAMPLES; i++)
            sounds[i] = loadSound("sounds/sample-"+i+".wav");
      }

   /*********************************************************************************/

     private void open()
     {
        try{
            sequencer = javax.sound.midi.MidiSystem.getSequencer();

              if (sequencer instanceof javax.sound.midi.Synthesizer) {
                   synthesizer = (javax.sound.midi.Synthesizer)sequencer;
                   channels = synthesizer.getChannels();
              }

              sequencer.open();
              sequencer.addMetaEventListener(this);
        }
        catch (Exception ex) {
           ex.printStackTrace();
           System.exit(1);
        }
     }

   /*********************************************************************************/

     public void close() {
         if (sequencer != null) {
            sequencer.close();
        }
     }

   /*********************************************************************************/

     public void playSound( byte sound_ID )
     {
         if(sounds[sound_ID]==null)
             return;

         setGain( sounds[sound_ID], SOUND_VOLUME );

         sounds[sound_ID].setFramePosition(0);
         
         playSound( sounds[sound_ID] );
     }

   /*********************************************************************************/

     public void setMusic( String sound_name, byte volume )
     { 
     	if(current_music!=null)
     	    sequencer.stop();

        if( !loadMidiMusic( sound_name ) )
            return;

//        double value = (double) volume / 100.0;

//        if(channels!=null)
          int value = (int) ( ( ((double)volume) / 100.0) * 127.0 );

           for (int i = 0; i < channels.length; i++) { 
                channels[i].controlChange(7, value );
           }

        sequencer.start();
     }

   /*********************************************************************************/

     private javax.sound.sampled.Clip loadSound( String sound_name )
     {
        File f = new File(sound_name);
    	javax.sound.sampled.AudioInputStream stream = null;
        javax.sound.sampled.Clip clip = null;
    	
        try {
              stream = javax.sound.sampled.AudioSystem.getAudioInputStream(f);
        }
        catch(Exception e1) {
            e1.printStackTrace();
            return null;
        }

        try
        {
             javax.sound.sampled.AudioFormat format = stream.getFormat();

                /**
                 * we can't yet open the device for ALAW/ULAW playback,
                 * convert ALAW/ULAW to PCM
                 */
             if ((format.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ULAW) ||
                 (format.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ALAW)) 
             {
                    javax.sound.sampled.AudioFormat tmp = new javax.sound.sampled.AudioFormat(
                                              javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, 
                                              format.getSampleRate(),
                                              format.getSampleSizeInBits() * 2,
                                              format.getChannels(),
                                              format.getFrameSize() * 2,
                                              format.getFrameRate(),
                                              true);
                    stream = javax.sound.sampled.AudioSystem.getAudioInputStream(tmp, stream);
                    format = tmp;
             }

             javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(
                                        javax.sound.sampled.Clip.class, 
                                        stream.getFormat(), 
                                        ((int) stream.getFrameLength() *format.getFrameSize()));

             clip = (javax.sound.sampled.Clip) javax.sound.sampled.AudioSystem.getLine(info);
             clip.open(stream);
        }
        catch (Exception ex) { 
           ex.printStackTrace(); 
           return null;
        }

      return clip;
    }

   /*********************************************************************************/

     private boolean loadMidiMusic( String sound_name )
     {
        File f = new File(sound_name);
        javax.sound.sampled.Clip clip = null;

           try{ 
                FileInputStream is = new FileInputStream(f);
                current_music = new BufferedInputStream(is, 1024);
           }
           catch (Exception e) { 
                e.printStackTrace();
                return false;
           }

           try{
                 sequencer.open();
                 sequencer.setSequence(current_music);
           }
           catch (javax.sound.midi.InvalidMidiDataException imde) { 
                System.out.println("Unsupported audio file.");
		return false;
           }
           catch (Exception ex) { 
                ex.printStackTrace(); 
                return false;
	   }

        return true;
     }

   /*********************************************************************************/

    private void playSound( javax.sound.sampled.Clip clip )
    {
    	if(clip==null)
    	   return;
    	
        clip.start();
    }

   /*********************************************************************************/


    private void playSound( javax.sound.sampled.Clip clip, int volume )
    {
    	if(clip==null)
    	   return;
    	
    	setGain(clip,volume);
    	
        clip.start();
    }

   /*********************************************************************************/

    private void stopSound( javax.sound.sampled.Clip clip )
    {
       if(clip!=null) {
           if(clip.isActive())
               clip.stop();

           clip.close();
       }
    }

   /*********************************************************************************/

     public void stopMusic() { 
        sequencer.stop();
     }

   /*********************************************************************************/

    // volume range [0..100]

    private void setGain( javax.sound.sampled.Clip clip, int volume )
    {
        double value = volume / 100.0;

         try
         {
            javax.sound.sampled.FloatControl gainControl = (javax.sound.sampled.FloatControl) clip.getControl( javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(value==0.0?0.0001:value)/Math.log(10.0)*20.0);
            gainControl.setValue(dB);
 
         }catch (Exception ex) {
           ex.printStackTrace();
         }
    }

   /*********************************************************************************/

    public void meta(javax.sound.midi.MetaMessage message) {
        if (message.getType() == 47) {  // 47 is end of track
            if(current_music!=null)
               sequencer.start();
        }
    }

   /*********************************************************************************/
}
