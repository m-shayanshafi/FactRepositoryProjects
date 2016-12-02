/**
 * @(#)DraggingImage.java Version 1.0 98/03/12
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

/**
 * a draggable image ( requires container to handle event )
 */
class DraggingImage
{
   Image image;
   DraggingArea container;
   DraggingSlot slot;  // a slot on the container for me to settle down
   
   boolean trueSizeKnown;
   MediaTracker tracker;
   
   final int INITIAL_WIDTH = 1;
   final int INITIAL_HEIGHT = 1;
   
   private int tmpWidth, tmpHeight;
   
   boolean draggable;
   
   int x;
   int y;
   int width;
   int height;
   
   public DraggingImage(Image image, DraggingArea container)
   {
      this.image = image;
      this.container = container;

      draggable = true;
      slot = null;
      x = 0;
      y = 0;
      
      width = tmpWidth  = INITIAL_WIDTH;
      height = tmpHeight = INITIAL_HEIGHT;

      tracker = new MediaTracker(container);
      tracker.addImage(image, 0);

   } // 2 param constructor
   
   public DraggingImage(int x, int y, Image image, DraggingArea container)
   {
      this.image = image;
      this.container = container;
      
      draggable = true;
      slot = null;
      this.x = x;
      this.y = y;
      
      width = tmpWidth  = INITIAL_WIDTH;
      height = tmpHeight = INITIAL_HEIGHT;

      tracker = new MediaTracker(container);
      tracker.addImage(image, 0);

   } // 4 param constructor
   
   
   public Point getLocation ()
   {
      return new Point(x, y);
   } // getLocation   
   
   public void setLocation ( int x, int y )
   {
      this.x = x;
      this.y = y;
   } // setLocation   
   
   public void centerAt ( int centerX, int centerY )
   {
      x = centerX - width / 2;
      y = centerY - height / 2;
   } // centerAt   
   
   public Dimension getSize ()
   {
      return new Dimension(width, height);
   } // getSize
   
   public void setSize ( int width, int height )
   {
      this.width = width;
      this.height = height;
   } // setSize   

   public boolean contains ( int xp, int yp )
   {
      return (xp >= x) && (xp < x + width) 
             && (yp >= y) && (yp < y + height);
   } // contains
   

   public void enableDrag()
   {
      draggable = true;
   } // enableDrag
   
   public void disableDrag()
   {
      draggable = false;
   } // disableDrag
   
   public boolean isDraggable()
   {
      return draggable;
   } //isDraggable
   
   public void settle(DraggingSlot slot)
   {
      this.slot = slot;
   } // settleDown
   
   public void unsettle()
   {
      slot = null;
   } // unsettle
   
   public boolean isSettled()
   {
      return slot != null;
   } // isSettled   
   
   public DraggingSlot getSlot()
   {
      return slot;
   } // getSlot
   
   public Dimension getPreferredSize() 
   {
      return getMinimumSize();
   } // getPreferredSize

   public Dimension getMinimumSize() 
   {
      return new Dimension(width, height);
   } // getMinimumSize
	
   public void paint (Graphics g) 
   {
      setSize(tmpWidth, tmpHeight);
      
      if (image != null) 
      {
         if (!trueSizeKnown) 
         {
            int imageWidth = image.getWidth(container);
            int imageHeight = image.getHeight(container);

            if (tracker.checkAll(true)) 
            {
               trueSizeKnown = true;
               if (tracker.isErrorAny()) 
               {
                  System.err.println("Error loading image: " + image);
               } // if is ErrorAny
            } // if checkAll

            if (((imageWidth > 0) && (tmpWidth != imageWidth)) ||
               ((imageHeight > 0) && (tmpHeight != imageHeight))) 
            {
               tmpWidth = imageWidth;
               tmpHeight = imageHeight;
               setSize(tmpWidth, tmpHeight);
               container.validate();
            } // if 
         } // if trueSize not Known
      } // if has image
      
      g.drawImage(image, x, y, container);
      g.drawRect(x, y, width - 1, height - 1);
   } // paint
	
} // DraggingImage
