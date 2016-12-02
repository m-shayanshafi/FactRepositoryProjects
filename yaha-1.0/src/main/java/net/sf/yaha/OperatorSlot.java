/**
 * @(#)OperatorSlot.java Version 1.0 98/03/12
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

/**
 * Slot to hold an operator
 */
public class OperatorSlot extends DraggingSlot implements Type
{
   static final int WIDTH = 27,
                    HEIGHT = 40;
   public OperatorSlot( int x, int y)
   {
      super( x, y, 0, 0 );
      type = OPERATOR_SLOT;
      width = WIDTH;
      height = HEIGHT;
   } // constructor   
   
   public int getType()
   {
      return OPERATOR;
   } // getType
   
} // OperatorSlot
