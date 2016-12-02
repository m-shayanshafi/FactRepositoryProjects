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
 
package com.oranda.pacdasher.uimodel.ghosts;

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.*;

public class GhostClyde extends Ghost
{
    public void initialize(XYCoarse xyCoarseInit)
    {
        super.initialize(xyCoarseInit);
        this.iRight = this.resources.gClydeRight;        
        this.iLeft = this.resources.gClydeLeft;
        this.iUp = this.resources.gClydeUp;
        this.iDown = this.resources.gClydeDown;
        this.ghostStrategy = new GhostStrategyClyde(this);
    }

}