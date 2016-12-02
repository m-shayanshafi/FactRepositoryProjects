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

import com.oranda.pacdasher.uimodel.MobileVisualObject;
import com.oranda.pacdasher.uimodel.util.XY;
import com.oranda.pacdasher.uimodel.util.XYCoarse;
import com.oranda.pacdasher.uimodel.util.XYCManager;
 
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/*
 * Encapsulate the implementation of a dirty area
 * Currently tile-based.
 * This is the queue implementation - too slow. 
 * THIS CLASS NOT CURRENTLY USED.
 */ 
public class DirtyAreaQueue
{

     private LinkedList<XYCoarse> queueTiles = new LinkedList<XYCoarse>();
     private final static int QUEUE_SIZE = 8;
     
     private DirtyAreaQueue()
     {
     }    
     
     public DirtyAreaQueue(XYCoarse xyCoarse)
     {
     	 // initialize 
     	 for (int i=0; i<QUEUE_SIZE; i++)
     	 {
         	queueTiles.add(xyCoarse);
         }
     }    
     
     /* 
      * Dirty the area around a Mobile Visual Object
      */
     public void addMVOArea(MobileVisualObject mvo)
     {
         XYCoarse xyCoarse = mvo.getXYCoarse();
         int xCoarse = xyCoarse.getX();
         int yCoarse = xyCoarse.getY();
         XYCManager xycm = XYCManager.getInstance();
         add(xycm.createXYC(xCoarse - 1, yCoarse - 1));
         add(xycm.createXYC(xCoarse, yCoarse - 1));
         add(xycm.createXYC(xCoarse - 1, yCoarse));
         add(xycm.createXYC(xCoarse, yCoarse));
         /*add(xycm.createXYC(xCoarse + 1, yCoarse);
         add(xycm.createXYC(xCoarse + 1, yCoarse - 1);
         add(xycm.createXYC(xCoarse - 1, yCoarse + 1);
         add(xycm.createXYC(xCoarse, yCoarse);
         */
     }
     
     
     
     public void add(XYCoarse xyCoarse)
     {
         if (!queueTiles.contains(xyCoarse))
         {
             queueTiles.addFirst(xyCoarse);
             queueTiles.removeLast();
         }
     }
     
     public List getDirtyTiles()
     {
         return queueTiles;
     }
     
     public String toString()
     {
     	 StringBuffer sb = new StringBuffer("");
     	 for (int i=0; i<QUEUE_SIZE; i++)
     	 {
         	XYCoarse xyCoarse = queueTiles.get(i);
         	sb.append(" ");
         	sb.append(xyCoarse);
         }
         return sb.toString();
     }
     
 }     
     
         