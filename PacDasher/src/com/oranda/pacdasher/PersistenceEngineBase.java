/**
 *  PacDasher application. For explanation of this class, see below. 
 *  Copyright (c) 2003-2005 James McCabe. Email: code@oranda.com 
 *  http://www.oranda.com/java/pacdasher/
 * 
 *  PacDasher is free software under the Aladdin license (see license  
 *  directory). You are free to play, copy, distribute, and modify it
 *  except for commercial purposes. You may not sell this code, or
 *  compiled versions of it, or anything which incorporates either of these.
 * 
 */

package com.oranda.pacdasher;

import com.oranda.pacdasher.uimodel.UIModel;
import java.io.*;

/**
 *
 */
public abstract class PersistenceEngineBase implements IPersistenceEngine
{
    public abstract void read(UIModel uiModel);
    public abstract void write();
    
   /**
    * Any data in PacDasher which should survive from one run to the next
    * MUST be specified here.
    */
    protected abstract int readHighScore();
    protected abstract void writeHighScore(int highScore);
}