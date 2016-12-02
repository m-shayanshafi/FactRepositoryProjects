/**
 * @(#)Card.java Version 1.0 98/03/12
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
 * A playing card, can be drawn and dragged around
 * user should take care of the match between image and suit/value
 */
public class Card extends DraggingImage implements Type
{
   // class varibles
   static final String [] SUIT_NAMES = 
            { 
               "club", "diamond", "heart", "spade" 
            };
            
   static final int CLUB = 0,
                    DIAMOND = 1,
                    HEART = 2,
                    SPADE = 3;
   
   int suit;        // either CLUB, DIAMOND, HEART or SPADE
   int value;       // 1 - 13 
   
   public Card(int suit, int value, Image image, DraggingArea container) 
   {
      super(image, container);
      this.suit = suit;
      this.value = value;
      
   } // 4 param constructor

   public Card(int cardId, Image image, DraggingArea container)
   {
      super(image, container);
      
      // id should follow a suit-first order
      suit  = cardId / 13;
      value = cardId % 13 + 1;
      
   } // 3 param constructor
   
   public Card(int cardId, int x, int y, Image image, DraggingArea container)
   {
      super(x, y, image, container);
      
      // id should follow a suit-first order
      suit  = cardId / 13;
      value = cardId % 13 + 1;
      
   } // 5 param constructor
   
   
   public int getCardValue()
   {
      return value;
   } // getCardValue   
   
   public Integer getValue()
   {
      return new Integer(value);
   } // getValue  
   
   public int getType()
   {
      return CARD;
   } // getType

} // Card
