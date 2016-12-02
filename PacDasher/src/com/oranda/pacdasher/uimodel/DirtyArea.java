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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.uimodel.util.XY;
import com.oranda.pacdasher.uimodel.util.XYCoarse;
import com.oranda.pacdasher.uimodel.util.XYCManager;
 
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/*
 * Encapsulate the implementation of a dirty area
 * Currently tile-based.
 */ 
public class DirtyArea
{
    private XYCoarse xycLeftTop;
    private XYCoarse xycRightBottom;
     
    public DirtyArea()
    {
    }    
     
    public DirtyArea(XYCoarse xycLeftTop, XYCoarse xycRightBottom)
    {
        this.xycLeftTop = xycLeftTop;
        this.xycRightBottom = xycRightBottom;
    }    
     
    public void set(XYCoarse xycLeftTop, XYCoarse xycRightBottom)
    {
        this.xycLeftTop = xycLeftTop;
        this.xycRightBottom = xycRightBottom;
    }
    
    public XYCoarse getXYCLeftTop()
    {
        return xycLeftTop;
    }    
    
    public XYCoarse getXYCRightBottom()
    {
        return xycRightBottom;
    }
    
    public String toString()
    {
        return xycLeftTop + " to " + xycRightBottom;
    }
}     
     
         