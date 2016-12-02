/**
 * @(#)ObservableInteger.java Version 1.0 98/03/12
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

public class ObservableInteger extends Observable
{
   protected int value;
   
   public ObservableInteger(int value)
   {
      this.value = value;
   } // 1 param constructor
    
   public void set(int newValue)
   {
      if(newValue != value)
      {
         value = newValue;
         setChanged();
         notifyObservers(new Integer(value));
      } //if
   } //setvalue
   
} //ObservableInteger
