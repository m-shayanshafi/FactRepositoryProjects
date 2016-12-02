/**
 *  For explanation of this class, see below. 
 *  Copyright (c) 2003-2004 James McCabe. 
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.oranda.util;

/**
 * IOutput - interface that all Oranda output classes must conform to
 */
public interface IOutput
{   
    // Type of error
    public final int DEBUG = 0;
    public final int NOTE = 1;
    public final int ERROR = 2;
    public final int DIALOGERROR = 3;
    public final int DIALOGALERT = 4;
    
    public final int HIGHESTTYPENUMBER = 4;
    
    public boolean isDebugging();
	public void setDebugging(boolean isDebugging);
   
    public void debug(String message);
    public void error(String message);
    public void error(String message, Throwable e);
    public void print(int typeOfOutput, String message);
    public void print(int typeOfOutput, final String message, Throwable e);
}
