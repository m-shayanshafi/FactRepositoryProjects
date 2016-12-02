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

import java.util.HashMap;

/*
 * Manage events for an application
 */
public class AppEventManagerSimple
{
    private static AppEventManagerSimple appEventMgr;
	
	// eventStr -> eventDetails
    private HashMap eventTable = new HashMap(); 
	
    private AppEventManagerSimple() 
    {  
    }
    
    public synchronized static AppEventManagerSimple getInstance()
    {
        if (appEventMgr == null)
		{
			appEventMgr = new AppEventManagerSimple();
			return appEventMgr;
		}
		else
		{
			return appEventMgr;
		} 
    }
    
    public void addEvent(int order, String eventStr)
    {
		AppEventDetails eventDetails = new AppEventDetails(order, 0);
		eventTable.put(eventStr, eventDetails);
    }  
	
	public AppEventDetails setEventRan(String eventStr)
	{
		AppEventDetails eventDetails = (AppEventDetails) eventTable.get(eventStr);
		eventDetails.setTime(System.currentTimeMillis());
		return eventDetails;
    }
	
	public void setEventRan(String eventStr, long freeMemory)
	{
		AppEventDetails eventDetails = setEventRan(eventStr);
		eventDetails.setFreeMemory(freeMemory);
	}
	
	// return milliseconds
	public String getTimeBetweenEvents(String firstEventStr, 
									   String secondEventStr)
	{
		AppEventDetails details1 = (AppEventDetails) eventTable.get(firstEventStr);
		AppEventDetails details2 = (AppEventDetails) eventTable.get(secondEventStr);
		long time1 = details1.getTime();
		long time2 = details2.getTime();
		if (time1 == 0)
		{
			return "Time for " + firstEventStr + " not set";
		}
		if (time2 == 0)
		{
			return "Time for " + secondEventStr + " not set";
		}
		return Long.toString(time2 - time1);
	}
}	
