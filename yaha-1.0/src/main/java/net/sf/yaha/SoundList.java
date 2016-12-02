/*
 * Copyright (c) 1995-1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
/*
 * Code modified for Yaha
 */

package net.sf.yaha;

import java.applet.*;
import java.net.URL;

//Loads and holds a bunch of audio files whose locations are specified
//relative to a fixed base URL.
public class SoundList extends java.util.Hashtable {
    Applet applet;


    public SoundList(Applet applet) {
        super(5); //Initialize Hashtable with capacity of 5 entries.
        this.applet = applet;
    }

    public void startLoading(String relativeURL) {
        new SoundLoader(applet, this, relativeURL);
    }

    public AudioClip getClip(String relativeURL) {
        return (AudioClip)get(relativeURL);
    }

    public void putClip(AudioClip clip, String relativeURL) {
        put(relativeURL, clip);
    }

    // added method to play clip
    public synchronized void playClip(String s)
    {
        AudioClip audioclip = getClip(s);
        if(audioclip != null)
        {
            audioclip.play();
            return;
        } else
        {
            applet.showStatus("Sound " + s + " not loaded yet.");
            return;
        }
    }
}
