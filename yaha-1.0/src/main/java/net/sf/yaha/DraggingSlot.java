/*
 * @(#)DraggingSlot.java Version 1.0 98/03/12
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
 * slot to put a dragging image onto it. 
 */
public class DraggingSlot 
{
   int x;
   int y;
   int width;
   int height;
   
   public int type;         
   static final int CARD_SLOT = 0,
                    OPERATOR_SLOT = 1;
   
   DraggingImage holdenImage = null;
   
   static final Color emptyColor = Color.cyan,
                      fillColor = Color.red;
   
   public DraggingSlot()
   {
      x = 0;
      y = 0;
      width = 0;
      height = 0;
   } // 0 param constructor
   
   public DraggingSlot(int x, int y, int width, int height)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   } // 4 param constructor   
   
   public boolean isEmpty()
   {
      return holdenImage == null;
   } // isEmpty
   
   public Point getLocation ()
   {
      return new Point(x, y);
   } // getLocation   
   
   public void setLocation ( int x, int y )
   {
      this.x = x;
      this.y = y;
   } // setLocation   
   
   public Dimension getSize ()
   {
      return new Dimension(width, height);
   } // getSize
   
   public void setSize ( int width, int height )
   {
      this.width = width;
      this.height = height;
   } // setSize   
   
   /**
    * see if slot contains a point
    */
   public boolean contains ( int xp, int yp )
   {
      return (xp >= x) && (xp < x + width) && (yp >= y) && (yp < y + height);
   } // contains
   
   /**
    * see if slot hold a draggingImage
    */
   public boolean holds ( DraggingImage image )
   {
      return holdenImage == image;
   } // holds
   
   /**
    * Judge whether a draggingImage is right on my top and
    * should be hold by me.  If its center is inside me, it should.
    */
   public boolean underneath ( DraggingImage drag )
   {
      int centerX = drag.getLocation().x + drag.getSize().width / 2;
      int centerY = drag.getLocation().y + drag.getSize().height / 2;
      return contains( centerX, centerY );
   } // underneath
   
   /**
    * fill me with a draggingImage
    */
   public void fillWith ( DraggingImage drag )
   {
      int centerX = x + width / 2;
      int centerY = y + height / 2; 
      drag.centerAt( centerX, centerY );
      drag.settle(this);
      holdenImage = drag;
      
   } // fillWith
   
   public DraggingImage getHoldenImage()
   {
      return holdenImage;
   } // getHoldImage
   
   /**
    * kick off the invader
    */
   public void kickOff ( DraggingImage invader )
   {
      invader.setLocation( invader.getLocation().x, 
                           invader.getLocation().y - height );
   } // kickOff
   
   /**
    * empty this slot
    */
   public void empty ()
   {
      if(holdenImage != null)
      {
         holdenImage.unsettle();
         holdenImage = null;
      } // if   
   } // getRideOf
   
   /**
    * remove the holden image
    */
   public void remove()
   {
      if(holdenImage != null)
      {
         kickOff(holdenImage);
         empty();
      } // if
   } // remove   
   
   public void paint (Graphics g) 
   {
      Color oldColor = g.getColor();
      
      if( isEmpty() ) g.setColor( emptyColor );
      else g.setColor( fillColor );
     
      g.drawRect(x, y, width, height);
      
      g.setColor( new Color(64, 128, 128) );
      g.drawRect(x + 2, y + 2, width - 4, height - 4);
      g.fillRect(x + 2, y + 2, width - 4, height - 4);
      g.setColor( oldColor );
   } // paint
   
} // DraggingSlot
