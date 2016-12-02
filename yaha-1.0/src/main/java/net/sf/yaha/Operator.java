/**
 * @(#)Operator.java Version 1.0 98/03/12
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
 * an arithmetic operator, has an image associated with it and 
 * can be drawn and dragged around
 */
public class Operator extends DraggingImage implements Type
{
   /*
    * names of operaters
    */
   static final String [] OP_NAMES = 
            { 
               "add", "minus",  "multi", 
               "div", "oparen", "cparen" 
            };
   /*
    * charactor symbols of operators
    */
   static final char [] OP_SYMBOLS = 
            { '+', '-', '*', '/', '(', ')'};
   
   /*
    * enum of operators
    */
   static final int ADD = 0, 
                    MINUS = 1, 
                    MULTI = 2,
                    DIV = 3,
                    OPAREN = 4,
                    CPAREN = 5;
   /*
    * total number of operators
    */
   static final int OP_NUMBER = 6;
   
   int opId;         // ADD ... CPAREN
   char opSymbol;    // +, -, *, /, (, )
   
   public Operator(int opId, Image image, DraggingArea highestContainer)
   {
      super(image, highestContainer);
      
      
      this.opId = opId;
      opSymbol = OP_SYMBOLS[opId];
      
   } // 3 param id constructor   
   
   public Operator(int opId, int x, int y, Image image, DraggingArea container)
   {
      super(x, y, image, container);
      
      
      this.opId = opId;
      opSymbol = OP_SYMBOLS[opId];
      
   } // 5 param id constructor   
   
   
   public Operator(char symbol, Image image, DraggingArea highestContainer)
   {
      super(image, highestContainer);
      
      opSymbol = symbol;
      for(int i = 0; i < OP_NUMBER; i++)
      {
         if( symbol == OP_SYMBOLS[i] )
         {
            opId = i;
            break;
         } // if
      } // for  
   } // symbol constructor   
   
   public char getOpSymbol()
   {
      return opSymbol;
   } // getOpSymbol   
   
   public Character getValue()
   {
      return new Character(opSymbol);
   } // getOpSymbol   
   
   public int getType()
   {
      return OPERATOR;
   } // getType
   
} // Operator
