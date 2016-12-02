/**
 * @(#)Solution.java Version 1.1 98/04/22
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

import java.util.Vector;

/**
 * Given 4 integers, trying to form an expression using +, -, *, /,
 * ( and ) so that the value of this expression equals 24
 */
public class Solution
{
   // vector to store 4 integers
   static Vector numbers;

   // flag to indicate whether there is solution
   boolean hasSolution;

   // final expression
   Vector theSolution = null;

   // the value of final expression
   double theResult;

   /* in order to avoid combination explosion, construct a list of
    * valid positions for both numbers and operators as following:
    *    0  1  2  3  4  5  6  7  8  9 10 11 12
    *    (  n  o  (  n  )  o  (  n  )  o  n  )
    *
    *    n - number, o - operator
    */

   static final int TOTAL_POSITION = 13;

   // valid number locations
   static final int [] NUM_POSITION = { 1, 4, 8, 11 };

   // valid number combinations
   static int [][] numCombin = new int [24][4];
   static boolean hasNumCombin = false;

   // valid operator locations
   static final int [] OP_POSITION = { 2, 6, 10 };

   // valid operator combinations
   static int [][] opCombin = new int [64][3];
   static boolean hasOpCombin = false;

   static Character whiteSpace = new Character(' '),
                openParen = new Character('('),
                closeParen = new Character(')'),
                addition = new Character('+'),
                subtract = new Character('-'),
                multiply = new Character('*'),
                division = new Character('/');

   // valid parenthesis locations
   static final int [] PAREN_POSITION = { 0, 3, 5, 7, 9, 12 };

   // valid parenthesis combination, 0-no, 1-open, 2-close
   static final int [][] PAREN_COMBIN =
   {
      { 0, 0, 0, 0, 0, 0 },
      { 1, 0, 2, 0, 0, 0 },
      { 1, 0, 0, 0, 2, 0 },
      { 0, 1, 0, 0, 2, 0 },
      { 0, 1, 0, 0, 0, 2 },
      { 0, 0, 0, 1, 0, 2 },
      { 1, 0, 2, 1, 0, 2 },
   };  // PAREN_COMBIN

   /**
    * Constructs a solution, given 4 integers
    * @param num0 the first number
    * @param num1 the second number
    * @param num2 the third number
    * @param num3 the fouth number
    */
   public Solution(int num0, int num1, int num2, int num3)
   {
      numbers = new Vector(4);

      numbers.addElement(new Integer(num0));
      numbers.addElement(new Integer(num1));
      numbers.addElement(new Integer(num2));
      numbers.addElement(new Integer(num3));

      searchSolution();

   } // 4 param constructor

   /**
    * Gets solution expression
    * @return one solution expressio as a vector
    */
   public Vector getSolution()
   {
      return theSolution;
   } //getSolution

   /**
    * Judges if there is solution for this 4 number
    * @return  <code>true</code> if has solution
    *          <code>false</code> if no solution
    */
   public boolean hasSolution()
   {
      return hasSolution;
   } // hasSolution

   /**
    * Constructs a 4 number permutation matrix
    */
   static private void numberCombination()
   {
      Vector tmpNumbers,
             numbers = new Vector(4);
      int count = 0;

      // Once we have aleady have a number permutation matrix, we
      // need not compute it again
      if (hasNumCombin) return;

      for(int i = 0; i < 4; i++)
      {
         numbers.addElement(new Integer(i));
      }

      for(int i = 0; i < 4; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            for(int k = 0; k < 2; k++)
            {
               tmpNumbers = (Vector)numbers.clone();

               numCombin[count][0] = ((Integer)tmpNumbers.elementAt(i)).intValue();
               tmpNumbers.removeElementAt(i);

               numCombin[count][1] = ((Integer)tmpNumbers.elementAt(j)).intValue();
               tmpNumbers.removeElementAt(j);

               numCombin[count][2] = ((Integer)tmpNumbers.elementAt(k)).intValue();
               tmpNumbers.removeElementAt(k);

               numCombin[count][3] = ((Integer)tmpNumbers.elementAt(0)).intValue();
               tmpNumbers.removeElementAt(0);

               count++;

            } // for k
         } // for j
      } // for i
      hasNumCombin = true;
   } // numberCombination

   /**
    * Constructs an operator combination matrix
    */
   static private void operatorCombination()
   {
      int count = 0;

      // Once we have aleady have an operator combination matrix, we
      // need not compute it again
      if (hasOpCombin) return;

      for(int i = 0; i < 4; i++)
      {
         for(int j = 0; j < 4; j++)
         {
            for(int k = 0; k < 4; k++)
            {
               opCombin[count][0] = i;
               opCombin[count][1] = j;
               opCombin[count][2] = k;

               count++;

            } // for k
         } // for j
      } // for i
      hasOpCombin = true;
   } // operatorCombination

   public void haveRest(int time)
   {
      try
      {
         Thread.sleep(time);
      }
      catch(InterruptedException e)
      {
      }
   } //haveRest

   /**
    * Uses enumeration to find a valid solution
    */
   private void searchSolution()
   {
      Vector tmpExpression = new Vector(TOTAL_POSITION);

      int i, j, k;  // three simple loop variables
      int count = 0;

      numberCombination();
      operatorCombination();

      // fill with whitespaces
      for(i = 0; i < TOTAL_POSITION; i++)
      {
         tmpExpression.addElement(whiteSpace);
      } // for

      // search all the possible combination:
      // number * parenthesis * operator
      for(int num = 0; num < 24; num++)
      {
         for(int paren = 0; paren < 7; paren++)
         {
            for(int op = 0; op < 64; op++)
            {
               /* it's impossible that can't find solution after 8000 iterations
               if(count == 8000)
               {
                  hasSolution = false;
	               theSolution = null;
	               return;
               }*/

               // sleep 100ms every 500 iterations
               if(count++ % 500 == 0)
               {
                  haveRest(100);
               }

               // fill in numbers
               for(i = 0; i < 4; i++)
               {
                  tmpExpression.setElementAt(
                        numbers.elementAt(numCombin[num][i]),
                        NUM_POSITION[i]);
               } // for numbers

               // fill in parenthesis
               for(i = 0; i < 6; i++)
               {
                  switch ( PAREN_COMBIN[paren][i] )
                  {
                     case 1:
                        tmpExpression.setElementAt(
                           openParen, PAREN_POSITION[i]);
                        break;
                     case 2:
                        tmpExpression.setElementAt(
                           closeParen, PAREN_POSITION[i]);
                        break;
                     case 0:
                        tmpExpression.setElementAt(
                           whiteSpace, PAREN_POSITION[i]);
                        break;
                  } // switch
               } // for parenthesis

               // fill in operators
               for(i = 0; i < 3; i++)
               {
                  switch ( opCombin[op][i] )
                  {
                     case 0:
                        tmpExpression.setElementAt(
                           addition, OP_POSITION[i]);
                        break;
                     case 1:
                        tmpExpression.setElementAt(
                           subtract, OP_POSITION[i]);
                        break;
                     case 2:
                        tmpExpression.setElementAt(
                           multiply, OP_POSITION[i]);
                        break;
                     case 3:
                        tmpExpression.setElementAt(
                           division, OP_POSITION[i]);
                        break;
                  } // switch
               } // for operator

               // evaluate the generated expression
               try
               {
                  theResult = new Expression(tmpExpression).getValue();
               } // try
               catch (IllegalExpressionException e)
	            {
	               // expression format error, keep trying
	               continue;
	            } // catch

	            if( theResult == 24.0 )
	            {
	               // we found the solution
	               hasSolution = true;
	               theSolution = tmpExpression;
	               //System.out.println(count);
	               return;
	            } // if hasSolution
	         } // for op
	      } // for paren
	   } // for num

	   //searched all the combination, no solution
	   //System.out.println("no solution");
	   hasSolution = false;
	   theSolution = null;
	} // searchSolution

} // Solution
