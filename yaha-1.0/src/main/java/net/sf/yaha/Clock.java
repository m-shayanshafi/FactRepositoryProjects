/**
 * @(#)Clock.java Version 1.0 98/03/12
 * 
 * Yaha! A math game for everyone. 
 * Copyright (c) 1998, 2007 Huahai Yang <huahai@yyhh.org>
 *
 * This file is part of Yaha!
 *
 * Yaha! is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU Affero General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public  
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package net.sf.yaha;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;

/**
 * a clock, update once per second, fire action when time limit
 * is reached
 */
public class Clock extends Canvas implements Runnable
{
   ActionListener actionListener = null;
   String actionCommand;
   Thread clockThread = null;
   int timeLimit,
       timeUnit,
       timer;
       
	Dimension offScreenDimension;
	Image offScreenImage;
	Graphics offScreenGraphics;
   
   public Clock(int limit)
   {
      timeUnit = 1000;
      timeLimit = limit;
      timer = 0;
   } // 1 param constructor

   public Clock(int unit, int limit)
   {
      timeUnit = unit;
      timeLimit = limit;
      timer = 0;
   } // 2 param constructor
   
   public void setTimeLimit(int limit)
   {
      timeLimit = limit;
   } // setTimeLimit
   
   public int getTimeLimit()
   {
      return timeLimit;
   } // getTimeLimit   
   
   public int getTime()
   {
      return timer;
   } //getTime   

   private void createOffScreen()
   {
      // create offscreen context
      Dimension d = getSize();
      if ( (offScreenGraphics == null)
            || (d.width != offScreenDimension.width)
            || (d.height != offScreenDimension.height) ) 
      {
         offScreenDimension = d;
         offScreenImage = createImage(d.width, d.height);
         offScreenGraphics = offScreenImage.getGraphics();
      } // if
   } //createOffScreen
   
   public Dimension getPreferredSize()
   {
      return getMinimumSize();
   } // getPreferredSize

   public Dimension getMinimumSize()
   {
      return new Dimension(80, 50);
   } // getMinimumSize


   public void start()
   {
      if (clockThread == null)
      {
         clockThread = new Thread(this, "Clock");
      } // if
      clockThread.start();
   } // start
   
   public void stop()
   {
      clockThread = null;
      timer = 0;
      repaint();
   } //stop
   
   public void run()
   {
	   Thread myThread = Thread.currentThread();
      while (clockThread == myThread)
      {
         repaint();
         if(timeLimit == timer++)
         {
            //time limit reached, fire action
            sourceActionEvent();
         } //if
         try
         {
            Thread.sleep(timeUnit);
         } // try
         catch (InterruptedException e)
         {
         } // catch
      } //while
   } //run

   public void paint(Graphics g) 
   {
      update(g);
   } // paint
   
    public void update(Graphics g) 
    {
      createOffScreen(); 
      
      // Erase the previous image.
      offScreenGraphics.setColor(getBackground());
      offScreenGraphics.fillRect(0, 0, getSize().width, getSize().height);
      
      // draw clock
      offScreenGraphics.drawOval(0, 0, 50, 50);
      offScreenGraphics.setColor(new Color(128, 128, 192));
      offScreenGraphics.fillOval(0, 0, 50, 50);
      
      offScreenGraphics.setColor(Color.red);
      offScreenGraphics.drawLine( 25, 0, 25, 25 );
      offScreenGraphics.drawArc( 0, 0, 49, 49, 90, 
          - 360 * timer / timeLimit );
      offScreenGraphics.fillArc( 0, 0, 49, 49, 90, 
          - 360 * timer / timeLimit );
      
      //Paint the image onto the screen.
      g.drawImage(offScreenImage, 0, 0, this);
      
   } // update
   

	public void addActionListener(ActionListener l)
	{
	   actionListener = AWTEventMulticaster.add(actionListener, l);
	} // addActionListener

	public void removeActionListener(ActionListener l)
	{
	   actionListener = AWTEventMulticaster.remove(actionListener, l);
	} // removeActionListener

	public void setActionCommand(String command)
    {
    	  actionCommand = command;
    } //setActionCommand

   public void sourceActionEvent()
	{
		if (actionListener != null)
		{
		   actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
		} //if
	} // sourceActionEvent

} // Clock
