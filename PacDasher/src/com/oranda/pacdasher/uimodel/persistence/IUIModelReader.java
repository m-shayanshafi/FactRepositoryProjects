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
package com.oranda.pacdasher.uimodel.persistence;

import com.oranda.pacdasher.uimodel.GameInfo;

/**
 * General type for retrieving UI Model's from persistent storage.
 */
public interface IUIModelReader
{
    public MazeRaw parseForMaze(int level) throws ParseException;
    public GameInfo parseForGameInfo() throws ParseException;
}
