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
 
package com.oranda.pacdasher.uimodel.visualobjects;

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

import java.awt.Image;

/**
 * There are different kinds of Fruit in different mazes, which serve as
 * a sort of bonus for PacDasher.
 */
public abstract class Fruit extends AnimatedVisualObject
{
    protected Image img;
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_FRUIT;
    }
    
    public Image getImage()
    {
        return img;
    }
    
    public void setImage(Image img)
    {
        this.img = img;
    }
}
