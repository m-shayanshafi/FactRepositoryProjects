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
 * Code is the same in both 1.0 and 1.1.
 */

package net.sf.yaha;

import java.applet.*;
import java.net.URL;

class SoundLoader extends Thread {
    Applet applet;
    SoundList soundList;
    String relativeURL;

    public SoundLoader(Applet applet, SoundList soundList,
                       String relativeURL) {
        this.applet = applet;
        this.soundList = soundList;
        this.relativeURL = relativeURL;
        setPriority(MIN_PRIORITY);
        start();
    }

    public void run() {

        //AudioClip audioClip = applet.getAudioClip(baseURL, relativeURL);
        AudioClip audioClip = applet.getAudioClip(this.getClass().getResource("/"
            + relativeURL));
        //AudioClips load too fast for me!
        //Simulate slow loading by adding a delay of up to 1 seconds.
        try {
            sleep((int)(Math.random()*1000));
        } catch (InterruptedException e) {}

        soundList.putClip(audioClip, relativeURL);
    }
}
