/**
 * @(#)CardDeck.java Version 1.1 98/04/22
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
import java.util.Vector;
import java.lang.Math;

/**
 * A card deck, holds 52 cards, deal them and provides their solutions
 * Runs as a thread to caculate solution.
 */
public class CardDeck extends DraggingImage implements Runnable
{
   Thread solutionThread;
   SynchronizedVector [] solutions;
   Vector deck;
   int cardPointer;

   boolean clickable;
   DraggingArea container;

   public CardDeck(int x, int y, Image image, DraggingArea container)
   {
      super(x, y, image, container);
      draggable = false;
      clickable = true;

      deck = new Vector();
      for(int i = 0; i < 52; i++)
      {
         deck.addElement(new Card(i, container.cardImages[i], container));
      } // for
      shuffle();

      solutionThread = new Thread(this, "Solution");
      solutionThread.setPriority(Thread.MIN_PRIORITY);
      solutionThread.start();
	} // 4 param constructor

	public void setThreadPriority(int priority)
	{
	   if(solutionThread != null && solutionThread.isAlive())
	   {
	      solutionThread.setPriority(priority);
	   } //if
	} //setThreadPriority

	/**
	 * Stop solution thread
	 */
    public void stop()
    {
      if (solutionThread != null)
      {
         solutionThread = null;
      } //if
    } //stop

   /**
    * find solutions for all of the 13 deals
    */
   public void run()
   {
      int pointer = 0;
      solutions = new SynchronizedVector [13];

      // initiate solutions
      for(int i = 0; i < 13; i++)
      {
         solutions[i] = new SynchronizedVector();
      } // for

      for(int i = 0; i < 13; i++)
      {
         // search solution
         Solution sol = new Solution(
            ( (Card)deck.elementAt(pointer++) ).getCardValue(),
            ( (Card)deck.elementAt(pointer++) ).getCardValue(),
            ( (Card)deck.elementAt(pointer++) ).getCardValue(),
            ( (Card)deck.elementAt(pointer++) ).getCardValue() );

         // put solution string into a synchronized place, if the
         // solution is not yet available, its consumers
         // have to wait
         solutions[i].put( sol.getSolution() );
      } // for
   } // run

   public void enableClick()
   {
      clickable = true;
   } // enableClick

   public void disableClick()
   {
      clickable = false;
   } // disableClick

   public boolean isClickable()
   {
      return clickable;
   } // isClickable

   /**
    * return a deal of four cards,
    * these four card are selected from head of the card deck
    */
   public Card [] deal()
   {
      Card [] cards = new Card [4];

      if(cardPointer == 52) return null;

      for(int i = 0; i < 4; i++)
      {
         cards[i] = (Card)deck.elementAt(cardPointer);
         cardPointer++;

      } // for
      return cards ;
   } // deal

   /**
    * get number of current deal
    */
   public int currentDealNumber()
   {
      int deal = cardPointer / 4 - 1;
      if(deal < 0) deal = 0;
      return deal;
   } // currentDealNumber

   /**
    * get current deal's solution
    */
   public Vector currentSolution()
   {
      // if solution not yet available, will wait here
      return ( solutions[currentDealNumber()].get() );
   } // currentSolution

   /**
    * rearrange order of cards and reset cardPointer to zero
    */
   public void shuffle()
   {
      Vector tmpDeck = new Vector();
      int cardNumber = deck.size();
      int pick;

      for(int i = cardNumber - 1; i >= 0; i--)
      {
         pick = (int)( Math.random() * i );
         tmpDeck.addElement( deck.elementAt(pick) );
         deck.removeElementAt(pick);
      } // for

      deck = tmpDeck;
      cardPointer = 0;
   } // shuffle

}// CardDeck
