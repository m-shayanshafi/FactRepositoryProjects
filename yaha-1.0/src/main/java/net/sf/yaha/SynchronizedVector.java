/**
 * @(#)SynchronizedVector.java Version 1.0 98/03/12
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
 * A vector to store the found solution, if solution not available yet,
 * the access method will wait here until the solution become available
 */
public class SynchronizedVector
{
   private Vector vector;
   private boolean available;
   
   public SynchronizedVector()
   {
      vector = new Vector();
      available = false;
   } // constructor   

   public synchronized Vector get() 
   {
      while (available == false) 
      {
         try 
         {
            wait();
         } //try
         catch (InterruptedException e) 
         { 
         } //catch
      } //while
      notifyAll();
      return vector;
    } // get

    public synchronized void put(Vector vector) 
    {
        this.vector = vector;
        available = true;
        notifyAll();
    } //put
}  // SynchronizedVector
   

