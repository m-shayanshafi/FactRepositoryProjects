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
package jmines.view.persistence;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The class used to manage sounds.
 *
 * @author Zleurtor
 */
public final class AudioAccess {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new sound Manager. Do nothing. Never used.
     */
    private AudioAccess() {
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
    /**
     * Read the given file and return the audio stream corresponding.
     *
     * @param url The URL to read.
     * @return The audio stream corresponding to the sound file.
     * @throws IOException If an exception occurs while loading the sound.
     * @throws UnsupportedAudioFileException If an exception occurs while
     *                                       loading the sound.
     */
    public static AudioInputStream loadSound(final URL url) throws IOException, UnsupportedAudioFileException {
        return AudioSystem.getAudioInputStream(url);
    }

}
