/**
 * @(#)DraggingArea.java Version 1.0 98/03/12
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
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import java.lang.Math;
import java.net.*;

/**
 * Main playing area. Cards and operators are put on it and can be
 * dragged around.  Card deck is also put on it and caculate solutions
 * in a seperate thread.
 */
public class DraggingArea extends Panel
                          implements Runnable
{
   Thread dragThread;

	Applet applet;

	Dimension offScreenDimension;
	Image offScreenImage;
	Graphics offScreenGraphics;

	Image[] cardImages,
	        operatorImages;
	Image cardDeckImage;

	MediaTracker tracker;
   int imageCount;

   CardDeck cardDeck;
   PlayingStatus status;

   Card [] currentCards;
   Operator [] operators;

   DraggingSlot [] draggingSlots;

   Vector dragImages;
   DraggingImage theMoving;
   Point grabPoint;

   Cursor defaultCursor,
          handCursor;

   int updateLeft, updateTop, updateRight, updateBottom;
   boolean animating;
   boolean imagesLoaded = false;

   public DraggingArea()
	{
      setLayout(null);
      setForeground(new Color(0));
   	setBackground(new Color(32768));

   	handCursor = new Cursor(Cursor.HAND_CURSOR);
   	defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

   	addMouseListener(new MyMouseAdapter());
   	addMouseMotionListener(new MyMouseMotionAdapter());

	} // constructor

	public void start()
	{
	   applet = (Applet)getParent();

	   if (imagesLoaded == false) loadAllImages();

	   status = new PlayingStatus();
   	status.addObserver((Yaha)applet);
	   status.set(PlayingStatus.WAITING);

	   animating = false;
   	dragImages = new Vector();
   	theMoving = null;
   	grabPoint = new Point();

      // add cardDeck and run a seperate thread to calculate all of
      // this deck's 13 deal's solutions
      cardDeck = new CardDeck(30, 10, cardDeckImage, this);
   	dragImages.addElement(cardDeck);

      // create offscreen context
      Dimension d = getSize();
      if ( (offScreenGraphics == null)
            || (d.width != offScreenDimension.width)
            || (d.height != offScreenDimension.height) )
      {
         offScreenDimension = d;
         offScreenImage = createImage(d.width, d.height);
         offScreenGraphics = offScreenImage.getGraphics();
      } //if
      resetClip();

   	// add slots
      addSlots();

   	currentCards = new Card [4];
   	operators = new Operator [18];

      // add operators, each operator has 3 copies
      // arrange operaters this way:
      //    + * (
      //    - / )
      int count = 0;
      for(int i = 0; i < 3; i++)
      {
         for(int j = 0; j < 6; j++)
         {
            dragImages.addElement( operators[count++] = new Operator(
                  j, 510 + j / 2 * 30, 15 + j % 2 * 45,
                  operatorImages[j], this ) );
         } // for j
      } // for i

      if (dragThread == null)
      {
         dragThread = new Thread(this, "Dragging");
         dragThread.start();
      } // if

   } // start

   /**
    * put operaters back to original positions
    */
   public void arrangeOperators()
   {
      for(int i = 0; i < 13; i++)
      {
         if(draggingSlots[i] instanceof OperatorSlot)
         {
            draggingSlots[i].empty();
         } // if
      } // for

      for(int i = 0; i < 18; i++)
      {
         operators[i]. setLocation(510 + i % 6 / 2 * 30,
                  15 + i % 6 % 2 * 45);
      } // for
   } //arrangeOperators

   // solts arranged like this:
   // o c o o c o o o c o o c o
   // "o" is operator slot, "c" is card slot
   protected void addSlots()
   {
      int widthPointer;
      DraggingSlot slotPointer = null;

      draggingSlots = new DraggingSlot [13];

      slotPointer = draggingSlots[0] = new OperatorSlot(10, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[1] = new CardSlot(widthPointer + 6, 130);
      widthPointer = slotPointer.getLocation().x + CardSlot.WIDTH;

      slotPointer = draggingSlots[2] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[3] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[4] = new CardSlot(widthPointer + 6, 130);
      widthPointer = slotPointer.getLocation().x + CardSlot.WIDTH;

      slotPointer = draggingSlots[5] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[6] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[7] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[8] = new CardSlot(widthPointer + 6, 130);
      widthPointer = slotPointer.getLocation().x + CardSlot.WIDTH;

      slotPointer = draggingSlots[9] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[10] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

      slotPointer = draggingSlots[11] = new CardSlot(widthPointer + 6, 130);
      widthPointer = slotPointer.getLocation().x + CardSlot.WIDTH;

      slotPointer = draggingSlots[12] = new OperatorSlot(widthPointer + 6, 160);
      widthPointer = slotPointer.getLocation().x + OperatorSlot.WIDTH;

   } // addSlots

   /**
    * stop the running of dragging area
    */
   public void stop()
   {
      animating = false;
      cardDeck.stop();
      dragImages.removeAllElements();

      if (dragThread != null)
      {
         dragThread = null;
      } //if
   } // stop


   public void run()
   {
      Thread myThread = Thread.currentThread();
      while (dragThread == myThread)
      {
         if( animating )
         {
            presentSolution();
         } // if
         else
         {
            try
            {
               clipRepaint();
               Thread.sleep(200L);
            } // try
            catch(InterruptedException e)
            {
            } // catch
         } // else
      } // while
   } // run

   public Dimension getPreferredSize()
   {
      return getMinimumSize();
   } // getPreferredSize

   public Dimension getMinimumSize()
   {
      return new Dimension(640, 280);
   } // getMinimumSize

   public Vector userCreatedExpression()
   {
      Vector expression = new Vector();
      DraggingImage image = null;

      for(int i = 0; i < 13; i ++)
      {
         image = draggingSlots[i].getHoldenImage();
         if(image != null)
         {
            if(image instanceof Card)
               expression.addElement(((Card)image).getValue());
            else expression.addElement(((Operator)image).getValue());
         } // if
         else
         {
            expression.addElement(new Character(' '));
         } // if
      } // for
      return expression;
   } // userCreatedExpression


   // full expression should has at least 4 cards, 3 operators
   public boolean isFullExpression()
   {
      int cardCount = 0,
          operatorCount = 0;
      DraggingImage image = null;

      for(int i = 0; i < 13; i ++)
      {
         image = draggingSlots[i].getHoldenImage();
         if(image != null)
         {
            if(image instanceof Card) cardCount++;
            else operatorCount++;
         } // if
      } // for
      return ( cardCount > 3 && operatorCount > 2 );
   } // isFullExpression

   public Vector currentSolution()
   {
      return ( cardDeck.currentSolution() );
   } // currentSolution

   public void beginAnimation()
   {
      animating = true;
   } //beginAnimation

   public void endAnimation()
   {
      animating = false;
   } //endAnimation

   public void setStatus(int value)
   {
      status.set(value);
   } //setStatus

   /**
    * presents correct solution in animation
    */
   public void presentSolution()
   {
      int speed = 20,   //move 20 pixels each time
          startX,
          startY,
          endX,
          endY,
          steps,
          stepX,
          stepY;
      double distance;
      Point location;
      Dimension size;

      DraggingSlot slot = null;
      Vector solution = null;
      solution = currentSolution();
      Object element = null;

      resetClip();
      clearSlots();
      clipRepaint();
      try
      {
         Thread.sleep(100L);
      } // try
      catch(InterruptedException e)
      {
      } // catch

      for(int i = 0; i < 13; i++)
      {
         slot = draggingSlots[i];
         element = solution.elementAt(i);
         if( element instanceof Integer )
         {
            for(int j = 0; j < 4; j++)
            {
               if( !(currentCards[j].isSettled()) &&
                     currentCards[j].getCardValue() == ((Integer)element).intValue() )
               {
                  theMoving = currentCards[j];
                  break;
               } // if
            } // for j
         } // if is card
         else
         {
            boolean isOperator = false;
            for(int j = 0; j < 18; j++)
            {
               if( !(operators[j].isSettled()) &&
                  operators[j].getOpSymbol() == ((Character)element).charValue() )
               {
                  theMoving = operators[j];
                  isOperator = true;
                  break;
               } //if
            } // for j
            // is white space, skip
            if(!isOperator) continue;
         } // else is character

         //put theMoving on top
         dragImages.removeElement(theMoving);
         dragImages.addElement(theMoving);

         location = theMoving.getLocation();
         size = theMoving.getSize();

         //initiate clipping area
         updateLeft = location.x;
         updateTop = location.y;
         updateRight = updateLeft + size.width;
         updateBottom = updateTop + size.height;

         //start point of animation
         startX = location.x;
         startY = location.y;

         //end point of animation
         location = slot.getLocation();
         endX = location.x;
         endY = location.y;

         distance = Math.sqrt( (endX - startX) * (endX - startX)
            + (endY - startY) * (endY - startY) );

         //steps needed moving from start point to the end point
         steps = (int)(distance / speed);
         if(steps != 0)
         {
            stepX = (endX - startX) / steps;
            stepY = (endY - startY) / steps;
         } // if
         else
         {
            stepX = 0;
            stepY = 0;
         } //else

         //animation loop
         for(int s = 0; s < steps; s++)
         {
            theMoving.setLocation(startX + stepX * s,
                  startY + stepY * s);
            clipRepaint();
            try
            {
               Thread.sleep(100L);
            } // try
            catch(InterruptedException e)
            {
            } // catch
         } // for
         slot.fillWith(theMoving);
         resetClip();
         clipRepaint();
         ((Yaha)applet).soundList.playClip(
                  ((Yaha)applet).fillSlotSound );
         try
         {
            Thread.sleep(100L);
         } // try
         catch(InterruptedException e)
         {
         } // catch
      } // for
      theMoving = null;
      animating = false;
      resetClip();
   } //presentSolution

   public void clearSlots()
   {
      for(int i = 0; i < 13; i++)
      {
         draggingSlots[i].empty();

      } //for

      for(int i = 0; i < 18; i++)
      {
         operators[i].setLocation(510 + i % 6 / 2 * 30, 15 + i % 6 % 2 * 45);
      } // for
   } //clearSlots

   public void removeCards()
   {
      // empty card slots
      for(int i = 0; i < 13; i++)
      {
         if(draggingSlots[i] instanceof CardSlot)
         {
            draggingSlots[i].empty();
         } // if
      } // for

      for(int i = 0; i < 4; i++)
      {
         if(currentCards[i] != null)
         {
            dragImages.removeElement(currentCards[i]);
         } //if
      } // for
      clipRepaint();
   } // removeCards

   public void addCards()
   {
      for(int j = 0; j < 4; j++)
      {
         currentCards[j].setLocation( 150 + j * 80, 10 );
         dragImages.addElement( currentCards[j] );
      } // for
   } // addCards


   /*
    * track all card and operator images loading
    */
   public void loadAllImages()
   {
      tracker = new MediaTracker(this);
      cardImages = new Image[52];
      operatorImages = new Image[6];

      imageCount = 0;

      // track card images
      for(int i = 0; i < 4; i++)
      {
         for(int j = 1; j <= 13; j++)
         {
/*            cardImages[imageCount] = applet.getImage( applet.getCodeBase(),
                  "image/" + Card.SUIT_NAMES[i] + "-" + j + ".GIF" );*/
           URL url = this.getClass().getResource("/image/" +
              Card.SUIT_NAMES[i] + "-" + j + ".GIF" );
            cardImages[imageCount] = applet.getImage(url);
            tracker.addImage(cardImages[imageCount], imageCount);
            imageCount++;
         } // for j
      } // for i

      // track operator images
      for(int i = 0; i < 6; i++)
      {
/*         operatorImages[i] = applet.getImage( applet.getCodeBase(),
                     "image/" + Operator.OP_NAMES[i] + ".GIF" );*/
        URL url = this.getClass().getResource("/image/" + Operator.OP_NAMES[i]
            + ".GIF" );
        operatorImages[i] = applet.getImage(url);
          tracker.addImage(operatorImages[i], imageCount);
         imageCount++;
      } // for i

      // track cardDeck image
/*      cardDeckImage = applet.getImage( applet.getCodeBase(),
                     "image/CardSet.GIF" );*/
      cardDeckImage = applet.getImage(this.getClass().getResource("/image/CardSet.GIF"));

      tracker.addImage(cardDeckImage, imageCount);
      imageCount++;

      // begin loading
      tracker.checkAll(true);
      imagesLoaded = true;
	} // loadAllImages

   public synchronized void resetClip()
   {
      updateLeft = updateTop = 0;
      updateRight = getSize().width;
      updateBottom = getSize().height;
   } // resetClip

   public void paint(Graphics g)
   {
      g.setClip(updateLeft, updateTop,
         updateRight - updateLeft, updateBottom - updateTop);

      //Paint the image onto the screen.
      try {
        g.drawImage(offScreenImage, 0, 0, this);
      }
      catch (Exception e)
      {
      }

      //set update area again.
      if(theMoving != null)
      {
		   Point location = theMoving.getLocation();
		   Dimension size = theMoving.getSize();

		   updateLeft = location.x;
		   updateTop = location.y;
		   updateRight = updateLeft + size.width;
         updateBottom = updateTop + size.height;
      } // if

   } // paint

   /**
    * use double-buffering and clipping to update dragging area
    */
   public synchronized void update(Graphics g)
   {
     //String copyRight = "A simulation of a traditional Chinese math game, � Copyright 1998 by Huahai Yang, All rights reserved.";
     String copyRight = "http://yaha.sf.net";
     Font copyRightFont = new Font(null, Font.PLAIN, 10);
		// if not all image loaded, show status bar
      if(!tracker.checkAll(true))
      {
         int loadedCount = 0;
         for(int i = 0; i < imageCount; i++)
         {
            if(tracker.statusID(i, true) == 8)
               loadedCount++;
         }
         int barWidth = getSize().width - 100;
         int fillWidth = (barWidth * loadedCount) / imageCount;
         offScreenGraphics.setColor(Color.yellow);
         offScreenGraphics.drawString("Loading images:",
               50, getSize().height - 50);
         offScreenGraphics.drawRect(50, getSize().height - 30, barWidth, 15);
         offScreenGraphics.fillRect(50, getSize().height - 30, fillWidth, 15);

         offScreenGraphics.drawString(copyRight, getSize().width - 100, getSize().height - 5);
         paint(g);
         return;
      } //if

	   // clip, only draw changed area
	   offScreenGraphics.setClip(updateLeft, updateTop, updateRight -
           updateLeft, updateBottom - updateTop);

	   // Erase the previous image.
      offScreenGraphics.setColor(getBackground());
      offScreenGraphics.fillRect(0, 0, getSize().width, getSize().height);
      offScreenGraphics.setColor(Color.black);
      offScreenGraphics.setFont(copyRightFont);
      offScreenGraphics.drawString(copyRight, getSize().width - 110, getSize().height - 5);
      offScreenGraphics.setColor(getBackground());

      // draw all things
      drawDragSlots(offScreenGraphics);
      drawDragImages(offScreenGraphics);

      paint(g);

   } // update

   /**
    * Updates the clip area, then request repaint
    */
   public synchronized void clipRepaint()
   {
      if(theMoving != null)
		{
		   Point location = theMoving.getLocation();
		   Dimension size = theMoving.getSize();

		   // enlarge update area to include newly changed area
		   updateLeft = Math.min(updateLeft, location.x);
		   updateTop = Math.min(updateTop, location.y);
		   updateRight = Math.max(updateRight, location.x + size.width);
		   updateBottom = Math.max(updateBottom, location.y + size.height);
	   } // if
      repaint();
   } //clipRepaint

   // draw all the slots
   protected void drawDragSlots(Graphics g)
   {
      for( int i = 0; i < 13; i++ )
      {
         draggingSlots[i].paint(g);
      } // for
   } // drawDragSlots

	// draw all of the dragging images
   protected void drawDragImages(Graphics g)
   {
      for(Enumeration e = dragImages.elements(); e.hasMoreElements(); )
      {
         ((DraggingImage)e.nextElement()).paint(g);
      } // for
   } // drawDragImages

   // inner class to handle mouse events
	class MyMouseAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
         if( animating ) return;

         for( int i = dragImages.size() - 1; i >= 0; i--  )
         {
            DraggingImage d = (DraggingImage)dragImages.elementAt(i);
            if( d.contains(event.getX(), event.getY()) )
            {
               if( d.isDraggable() )
               {
                  theMoving = d;

                  synchronized(dragImages)
                  {
                     //put the dragged on top
                     dragImages.removeElement(theMoving);
                     dragImages.addElement(theMoving);
                  } //synchronized

                  grabPoint.x = event.getX() - d.getLocation().x;
                  grabPoint.y = event.getY() - d.getLocation().y;

                  updateLeft = d.getLocation().x;
                  updateTop = d.getLocation().y;
                  updateRight = updateLeft + d.getSize().width;
                  updateBottom = updateTop + d.getSize().height;
               } // if
               else
               {
                  // clicked on deck
                  if( d == cardDeck && ((CardDeck)d).isClickable() )
                  {
                     ((Yaha)applet).soundList.playClip(
                           ((Yaha)applet).clickDeckSound );
                     removeCards();
                     arrangeOperators();

                     currentCards = ((CardDeck)d).deal();

                     if( currentCards == null )
                     {
                        // finished a round
                        // TO DO: record user name if his score is
                        // high, some networking staff needed
                        dragImages.removeElement( cardDeck );
                        status.set(PlayingStatus.ROUND_OVER);
                     } //if
                     else
                     {
                        addCards();
                        ((CardDeck)d).disableClick();
                        status.set(PlayingStatus.DEALED);
                     } // else
                     clipRepaint();
                  } // if*/
               } // else
               return;
            } // if
         } // for

		} // mousePressed

		public void mouseReleased(MouseEvent event)
		{
		   if( animating ) return;
		   if( theMoving == null ) return;

		   boolean isOnSlot = false;
		   for(int i = 0; i < 13; i++)
		   {
		      DraggingSlot slot = draggingSlots[i];

		      if( slot.underneath(theMoving) )
		      {
		         int slotType, imageType;
		         if(slot instanceof CardSlot)
		              slotType = ((CardSlot)slot).getType();
		         else slotType = ((OperatorSlot)slot).getType();
		         if(theMoving instanceof Card)
		              imageType = ((Card)theMoving).getType();
		         else imageType = ((Operator)theMoving).getType();
		         if( slotType != imageType ) continue;

		         if( slot.isEmpty() )
		         {
		            if(theMoving.isSettled())
		            {
		               //remove from original slot
		               theMoving.getSlot().empty();
		            } // if
		            slot.fillWith(theMoving);
		            ((Yaha)applet).soundList.playClip(
                           ((Yaha)applet).fillSlotSound );
		         } // if slot empty
		         else
		         {
		            if(theMoving != slot.getHoldenImage())
		            {
		               slot.kickOff(theMoving);
		               if(theMoving.isSettled())
		               {
		                  theMoving.getSlot().empty();
		               } // if
		            } // if
		            else slot.fillWith(theMoving);
		         } // else
		         isOnSlot = true;
		         break;
		      } // if underneath
		   } // for

		   if( !isOnSlot )
		   {
		      if( theMoving.isSettled() )
		      {
		         //remove from the original slot
		         theMoving.getSlot().empty();
		      } // if
		   } // if
		   resetClip();
		   clipRepaint();
		   theMoving = null;
		} // mouseReleased

	} // MyMouseAdapter

   class MyMouseMotionAdapter extends MouseMotionAdapter
   {
      public synchronized void mouseDragged(MouseEvent event)
		{
		   if( animating ) return;
		   if(theMoving != null)
		   {
		      theMoving.setLocation( event.getX() - grabPoint.x,
		                             event.getY() - grabPoint.y );
		      clipRepaint();
		   } // if
		} // mouseDrgged

		public void mouseMoved(MouseEvent event)
		{
		   if( animating ) return;
         for( Enumeration e = dragImages.elements(); e.hasMoreElements(); )
         {
            DraggingImage d = (DraggingImage)e.nextElement();
            if( d.contains(event.getX(), event.getY()) )
            {
               setCursor(handCursor);
               return;
            } // if
         } // for
         if(getCursor() != defaultCursor) setCursor(defaultCursor);
		} // mouseMoved

	} // MyMouseMotionAdapter

} // DraggingArea
