/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JOptionPane;

import jmines.view.persistence.AudioAccess;
import jmines.view.persistence.Configuration;

/**
 * The class used to manage the sounds of JMines.
 *
 * @author Zleurtor
 */
public class AudioPlayer {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The URL of the sound used for the "timer" event.
     */
    private URL timerURL;
    /**
     * The URL of the sound used for the "defeat" event.
     */
    private URL defeatURL;
    /**
     * The URL of the sound used for the "victory" event.
     */
    private URL victoryURL;
    /**
     * The URL of the sound used for the "cell flagged" event.
     */
    private URL flagURL;
    /**
     * The URL of the sound used for the "cell open" event.
     */
    private URL openURL;
    /**
     * The URL of the sound used for the "new game" event.
     */
    private URL newURL;
    /**
     * The sound used for the times of JMines.
     */
    private Clip timerClip;
    /**
     * The sound used when the user loose a game.
     */
    private Clip defeatClip;
    /**
     * The sound used when the user win a game.
     */
    private Clip victoryClip;
    /**
     * The sound used when the user flag a cell.
     */
    private Clip flagClip;
    /**
     * The sound used when the user open a cell.
     */
    private Clip openClip;
    /**
     * The sound used when the user start a new game.
     */
    private Clip newClip;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new AudioPlayer using a given configuration (used to
     * retrieve JMines sound files).
     */
    public AudioPlayer() {
        Configuration configuration = Configuration.getInstance();

        timerURL = configuration.getSoundURL(Configuration.KEY_SOUND_TIMER_FILENAME);
        defeatURL = configuration.getSoundURL(Configuration.KEY_SOUND_DEFEAT_FILENAME);
        victoryURL = configuration.getSoundURL(Configuration.KEY_SOUND_VICTORY_FILENAME);
        flagURL = configuration.getSoundURL(Configuration.KEY_SOUND_FLAG_FILENAME);
        openURL = configuration.getSoundURL(Configuration.KEY_SOUND_OPEN_FILENAME);
        newURL = configuration.getSoundURL(Configuration.KEY_SOUND_NEW_FILENAME);

        timerClip = clip(timerURL);
        defeatClip = clip(defeatURL);
        victoryClip = clip(victoryURL);
        flagClip = clip(flagURL);
        openClip = clip(openURL);
        newClip = clip(newURL);

        // Add a hook to automatically close the clip when the application is
        // closed
        Runtime.getRuntime().addShutdownHook(new Thread() {
            /**
             * One of the hook launched at the virtual machine closing. This
             * only closed access to the sound files.
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                close(timerClip);
                close(defeatClip);
                close(victoryClip);
                close(flagClip);
                close(openClip);
                close(newClip);
            }
        });
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Create a clip using the given URL then open it.
     *
     * @param url The URL of the sound file we want the clip.
     * @return An open clip using the given URL.
     */
    private Clip clip(final URL url) {
        // Retrieve the audio streams corresponding to audio files
        AudioInputStream audioStream = null;

        try {
            audioStream = AudioAccess.loadSound(url);
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            audioStream = null;
        }
        // Retrieve the audio format of previous stream
        AudioFormat audioFormat = null;

        try {
            audioFormat = audioStream.getFormat();
        } catch (NullPointerException e) {
            audioFormat = null;
        }

        // Retrieve the sound we'll stock in a Clip object
        Info info = null;

        try {
            info = new Info(Clip.class, audioFormat, ((int) audioStream.getFrameLength() * audioFormat.getFrameSize()));
        } catch (NullPointerException e) {
            info = null;
        }

        // Retrieve the clip corresponding to previous stream
        Clip clip = null;

        try {
            clip = (Clip) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            clip = null;
        }

        // Open previous clip
        open(clip, audioStream);

        return clip;
    }

    /**
     * Open an audio stream.
     *
     * @param clip The audio clip to open.
     * @param audioStream The audio stream to open.
     * @return true if the stream has been correctly open, <code>false</code>
     *         otherwise.
     */
    private boolean open(final Clip clip, final AudioInputStream audioStream) {
        if (clip != null && !clip.isOpen()) {
            try {
                clip.open(audioStream);
            } catch (LineUnavailableException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                return false;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * Play an audio clip.
     *
     * @param clip The audio clip to play.
     */
    private void play(final Clip clip) {
        if (clip != null && clip.isOpen()) {
            clip.setMicrosecondPosition(0);
            clip.start();
        }
    }

    /**
     * Close an audio clip.
     *
     * @param clip The audio clip to close.
     */
    private void close(final Clip clip) {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Play the timer sound.
     */
    public final void playTimer() {
        play(timerClip);
    }

    /**
     * Play the defeat sound.
     */
    public final void playDefeat() {
        play(defeatClip);
    }

    /**
     * Play the victory sound.
     */
    public final void playVictory() {
        play(victoryClip);
    }

    /**
     * Play the timer sound.
     */
    public final void playFlag() {
        play(flagClip);
    }

    /**
     * Play the defeat sound.
     */
    public final void playOpen() {
        play(openClip);
    }

    /**
     * Play the victory sound.
     */
    public final void playNew() {
        play(newClip);
    }

}
