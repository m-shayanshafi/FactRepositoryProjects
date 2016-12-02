/**
 *  For explanation of this class, see below. 
 *  Copyright (c) 2003-2004 James McCabe. Email: code@oranda.com 
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

/*
 * Details for an application event. Each detail is only set once.
 */
public class AppEventDetails
{	
	// only used if useAppTime = true;
	private long initTime = -1;
	
	private int order = -1;
	private long time = -1;
	private long freeMemory = -1;
	
	private AppEventDetails() {}
	
	AppEventDetails(int order, long initTime) 
	{
		// Set initTime to the first time this class is used (rather than
		// when it is loaded).
		this.initTime = initTime;
		setOrder(order);
	}
	
    int getOrder() { return order; }
	private void setOrder(int order) { this.order = order; }
	
	public long getTime() { return time; }
	void setTime(long time) 
	{ 
	    boolean useAppTime = AppEventManager.getInstance().getUseAppTime();
			
		// overwrite any previous values
		if (useAppTime)
		{
			this.time = time - initTime; 
		}
		else
		{
			this.time = time;
		}
	}
	
	public long getFreeMemory() { return freeMemory; }
	void setFreeMemory(long freeMemory) 
	{ 
	    //if (this.freeMemory == -1) // if uninitialized
		//{
		
		// overwrite any previous values
		this.freeMemory = freeMemory; 
		
		//}
	}
	
	public String toString()
	{
		String s = "TIME " +  this.getTime();
		if (this.getFreeMemory() != -1)
		{ 
			s += ", MEMORY " + this.getFreeMemory();
		}
		return s;
	}
	
}	
