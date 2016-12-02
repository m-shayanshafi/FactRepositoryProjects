/**
 * @(#)Expression.java Version 1.1 2001/06/30
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

import java.util.*;

/**
 * Expression implements an simple arithmetic expression
 * ( can only handle 6 operators: +, -, *, /, ( and ) )
 * evaluation algorithem called operator precedence parsing.
 * Treats a String or a Vector as expression and return its
 * value (double) or throw an exception. It can skip whitespace.
 *
 * @version 	1.0, 03/12/98
 * @author     Huahai Yang
 */
public class Expression
{
   protected String inputExpression;

   private Stack operatorStack,  //operator stack for conversion
                 postFixStack;   //stack for postfix machine

   //define tokens
   private final int EOL = 0,    //end of line
                     VALUE = 1,  //number
                     OPAREN = 2, //open parenthesis
                     CPAREN = 3, //close parenthesis
                     MULT = 4,   //multiply
                     DIV = 5,    //division
                     PLUS = 6,
                     MINUS = 7;

   //define token precedences
   private final int [] INPUT_PRECEDENCE =
                           { 0, 0, 100, 0, 3, 3, 1, 1 },
                        STACK_PRECEDENCE =
                           { -1, 0, 0, 99, 4, 4, 2, 2 };

   private double currentValue;
   private double theResult;
   private int lastToken;

   /**
    * Constructs an empty exprssion
    */
   public Expression()
   {
      inputExpression = null;

      operatorStack = new Stack();
      postFixStack = new Stack();

      operatorStack.push(new Integer(EOL));
   } // 0 param constructor

   /**
    * Takes a String as an expression
    * @param inString input string
    */
   public Expression(String inString)
   {
      inputExpression = inString.trim();
      operatorStack = new Stack();
      postFixStack = new Stack();

      operatorStack.push(new Integer(EOL));
   } // string param constructor

   /**
    * Takes a vector as an expression, by converting elements of
    * vector into strings
    * @param inVector input vector
    */
   public Expression(Vector inVector)
   {
      StringBuffer tmpString = new StringBuffer();

      for (Enumeration e = inVector.elements(); e.hasMoreElements();)
      {
         tmpString.append( e.nextElement() ) ;
      } // for

      inputExpression = tmpString.toString().trim();

      operatorStack = new Stack();
      postFixStack = new Stack();

      operatorStack.push(new Integer(EOL));
   } // vector param constructor

   /**
    * Sets the input string
    * @param inString input string
    */
   public void setExpression(String inString)
   {
      inputExpression = inString.trim();
   } // setExpression

   /**
    * Sets the input string
    * @param inString input string
    */
   public String getExpression()
   {
      return removeSpace(inputExpression);
   } // setExpression

   /**
    * Gets the result of expression
    * @return     result value of this expression
    * @exception  IllegalExpressionException if encounter error while
    *             while parsing input string as an expression
    */
   public double getValue() throws IllegalExpressionException
   {
      String token;
      char firstChar;
      StringTokenizer parser =
            new StringTokenizer(inputExpression, "+-*/() ", true);

      do
      {
         //end of expression
         if(!parser.hasMoreTokens())
         {
            lastToken = EOL;
            processToken();
            continue;
         } // if

         token = parser.nextToken();
         firstChar = token.charAt(0);

         // skip the ' '
         if(Character.isWhitespace(firstChar))
         {
            continue;
         }

         if( (token.length() == 1) && isOperator(firstChar) )
         {
            switch (firstChar)
            {
               case '+':
                  lastToken = PLUS;
                  break;
               case '-':
                  lastToken = MINUS;
                  break;
               case '*':
                  lastToken = MULT;
                  break;
               case '/':
                  lastToken = DIV;
                  break;
               case '(':
                  if (lastToken != VALUE) lastToken = OPAREN;
                  else throw new IllegalExpressionException("Missing operator");
                  break;
               case ')':
                  lastToken = CPAREN;
                  break;
            } // switch
         } // if is operator
         else
         {
            try
            {
               currentValue = Double.valueOf(token).doubleValue();

            } //try
            catch ( NumberFormatException e )
            {
               throw new IllegalExpressionException("Unknown symbol");
            } //catch

            if (lastToken != VALUE && lastToken != CPAREN) lastToken = VALUE;
            else throw new IllegalExpressionException("missing operator");
         } // else suppose a number

         processToken();

      } while( lastToken != EOL );

      if( postFixStack.empty() )
      {
         throw new IllegalExpressionException("Missing operand");
      } // if missing operand

      theResult = ((Double)postFixStack.pop()).doubleValue();

      if( !postFixStack.empty() )
      {
         throw new IllegalExpressionException("Missing operator");
      } // if missing operator

      return theResult;

   } // getValue

   private String removeSpace(String inputString)
   {
      char c;
      StringBuffer s = new StringBuffer();
      for (int i = 0; i < inputString.length(); i ++ )
      {
        if ((c = inputString.charAt(i)) == ' ') continue;
        s.append(inputString.charAt(i));
      }
      return s.toString();
   }

   private void processToken() throws IllegalExpressionException
   {
      int topOperator;

      switch(lastToken)
      {
         case VALUE:
            postFixStack.push(new Double(currentValue));
            return;
         case CPAREN:
            while( (topOperator =
                  ((Integer)operatorStack.peek()).intValue())
                  != OPAREN && topOperator != EOL )
            {
               applyOperation( topOperator );
            } // while
            if( topOperator == OPAREN )
            {
               operatorStack.pop();
            } // if
            else
            {
               throw new IllegalExpressionException("Missing " +
                     "open parenthesis");
            } //else
            break;
         default:
            while( INPUT_PRECEDENCE[lastToken] <=
                  STACK_PRECEDENCE[ topOperator =
                  ((Integer)operatorStack.peek()).intValue() ] )
            {
               applyOperation( topOperator );
            } //while
            if( lastToken != EOL )
            {
               operatorStack.push(new Integer(lastToken));
            } // if
            break;
         } //switch
   } //processToken

   private void applyOperation(int topOperator)
                        throws IllegalExpressionException
   {
      double leftOperand,
             rightOperand;

      if( topOperator == OPAREN )
      {
         throw new IllegalExpressionException("Unbalanced parenthesis");
      } //if

      rightOperand = getPostStackTop();
      leftOperand = getPostStackTop();

      if( topOperator == PLUS )
      {
         postFixStack.push(new Double(leftOperand + rightOperand));
      } // if +
      else if( topOperator == MINUS )
      {
         postFixStack.push(new Double(leftOperand - rightOperand));
      } // else if -
      else if( topOperator == MULT )
      {
         postFixStack.push(new Double(leftOperand * rightOperand));
      } // else if *
      else if( topOperator == DIV )
      {
         if( rightOperand != 0)
         {
            postFixStack.push(new Double(leftOperand / rightOperand));
         } // if
         else
         {
            throw new IllegalExpressionException("Division by zero");
         } // else erro
      } // if /

      operatorStack.pop();
   } // applyOperation

   private double getPostStackTop() throws IllegalExpressionException
   {
      if( postFixStack.empty() )
      {
         throw new IllegalExpressionException("Missing operand");
      } // if
      return ((Double)postFixStack.pop()).doubleValue();
   } //getPostStackTop

   private boolean isOperator(char c)
   {
      return (c == '+' || c =='-' || c == '*' || c == '/'
              || c == '(' || c == ')' );
   } // isOperator

} // Expression

