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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Manage events for an application
 */
public class AppEventManager
{
    private static AppEventManager appEventMgr;
	  
	/**
	 * If this is true, the time variable will be relative to the beginning
	 * of the application.
	 */
	private boolean useAppTime = false;
	
	private long initTime;
	
	// eventStr -> eventDetails
    private Map eventTable = new HashMap();
	
	// eventStr2 -> eventStr1 and int allowedDur
	private Map allowedDurs = new HashMap();
	
    private AppEventManager() 
    {
		initTime = System.currentTimeMillis();
    }
    
    public synchronized static AppEventManager getInstance()
    {
        if (appEventMgr == null)
		{
			appEventMgr = new AppEventManager();
			return appEventMgr;
		}
		else
		{
			return appEventMgr;
		} 
    }
    
	/**
	 * If called, times will be relative to the start of the app. 
	 */
	public void setUseAppTime()
	{
		useAppTime = true;
	}
	
	public boolean getUseAppTime()
	{
		return useAppTime;
	}
	
	public long getTime()
	{
		if (useAppTime)
		{
			return System.currentTimeMillis() - initTime;
		}
		else
		{
			return System.currentTimeMillis();
		}
	}
	
    public void addEvent(int order, String eventStr)
    {
		AppEventDetails eventDetails = new AppEventDetails(order, initTime);
		eventTable.put(eventStr, eventDetails);
    }  
	
	public AppEventDetails setEventRan(String eventStr)
	{
		AppEventDetails eventDetails = (AppEventDetails) eventTable.get(eventStr);
		// TODO: if it's null, throw an exception
		eventDetails.setTime(System.currentTimeMillis());

		return eventDetails;
    }
	
	public AppEventDetails setEventRan(String eventStr, long freeMemory)
	{
		AppEventDetails eventDetails = setEventRan(eventStr);
		eventDetails.setFreeMemory(freeMemory);
		return eventDetails;
	}
	
	/**
	 * Set the time allowed between the first event and the second event.
	 */
	public void setAllowedDur(String eventStr1, String eventStr2, 
			int allowedDur)
	{
		if (!eventTable.containsKey(eventStr1) 
				|| !eventTable.containsKey(eventStr2))
		{
			throw new RuntimeException("eventStr1 " + eventStr1 
			        + " or eventStr2 " + eventStr2 + " not in eventTable "
					+ eventTable);
		}
		EventStr1AndAllowedDur dataAlreadyThere = (EventStr1AndAllowedDur) 
				allowedDurs.get(eventStr2);
		if (dataAlreadyThere != null)
		{
			throw new RuntimeException("eventStr2 " + eventStr2 
			        + " already has an allowed time set: " + dataAlreadyThere);
		}
		EventStr1AndAllowedDur eventStr1AndAllowedDur 
				= new EventStr1AndAllowedDur(eventStr1, allowedDur);
		allowedDurs.put(eventStr2, eventStr1AndAllowedDur);
	}
	
	public int getAllowedDur(String eventStr2)
	{
		EventStr1AndAllowedDur eventStr1AndAllowedDur 
				= (EventStr1AndAllowedDur) allowedDurs.get(eventStr2);
		if (eventStr1AndAllowedDur == null)
		{
			return -1;
		}
		return eventStr1AndAllowedDur.getAllowedDur();
	}
	
	public String getAllowedDurEventStr1(String eventStr2)
	{
		EventStr1AndAllowedDur eventStr1AndAllowedDur 
				= (EventStr1AndAllowedDur) allowedDurs.get(eventStr2);
		if (eventStr1AndAllowedDur == null)
		{
			return null;
		}
		return eventStr1AndAllowedDur.getEventStr1();
	}
	
	public int getAllowedDur(String eventStr1, String eventStr2)
	{
		EventStr1AndAllowedDur eventStr1AndAllowedDur 
				= (EventStr1AndAllowedDur) allowedDurs.get(eventStr2);
		if (eventStr1AndAllowedDur == null)
		{
			throw new RuntimeException("eventStr2 " + eventStr2 
			        + " does not have any allowed time set");
		}
		String foundEventStr1 = eventStr1AndAllowedDur.getEventStr1();
		if (!foundEventStr1.equals(eventStr1))
		{
			throw new RuntimeException("eventStr2 " + eventStr2 
			        + " has allowedDur set for " + foundEventStr1 
					+ " not " + eventStr1);
		}
		return eventStr1AndAllowedDur.getAllowedDur();
	}
	
	// return milliseconds
	public String getTimeBetweenEvents(String firstEventStr, 
									   String secondEventStr)
	{
		AppEventDetails details1 = (AppEventDetails) eventTable.get(firstEventStr);
		AppEventDetails details2 = (AppEventDetails) eventTable.get(secondEventStr);
		long time1 = details1.getTime();
		long time2 = details2.getTime();
		if (time1 == -1)
		{
			return "Time for " + firstEventStr + " not set";
		}
		if (time2 == -1)
		{
			return "Time for " + secondEventStr + " not set";
		}
		return Long.toString(time2 - time1);
	}
	
	public String getHistory()
	{
		StringBuffer sb = new StringBuffer("\n");
		Set eventSet = eventTable.keySet();
		List eventList = new ArrayList(eventSet);
		Collections.sort(eventList, new EventTimeComparator());
		Iterator iEventTable = eventList.iterator();
		while (iEventTable.hasNext())
		{
			String eventStr = (String) iEventTable.next();
			AppEventDetails appEventDetails
					= (AppEventDetails) eventTable.get(eventStr);
			if (appEventDetails.getTime() > -1)
			{
				sb.append("  " + appEventDetails.toString() + ": " + eventStr
						+ "\n");
			}
		}
		return sb.toString();
	}
	
	private long getTime(String eventStr)
	{
	    AppEventDetails appEventDetails
					= (AppEventDetails) eventTable.get(eventStr);
		return appEventDetails.getTime();
	}

	private long getOrder(String eventStr)
	{
	    AppEventDetails appEventDetails
					= (AppEventDetails) eventTable.get(eventStr);
		return appEventDetails.getOrder();
	}
	
	class EventTimeComparator implements Comparator 
	{
		public int compare(Object o1, Object o2)
		{
			String eventStr1 = (String) o1;
			String eventStr2 = (String) o2;
			if (getTime(eventStr1) > getTime(eventStr2))
			{
				return 1;
			}
			else if (getTime(eventStr1) < getTime(eventStr2))
			{
				return -1;
			}
			if (getOrder(eventStr1) > getOrder(eventStr2))
			{
				return 1;
			}
			else if (getOrder(eventStr1) < getOrder(eventStr2))
			{
				return -1;
			}
			return 0;
		}
	}
	
	class EventStr1AndAllowedDur
	{
		String eventStr1;
		int allowedDur;
		
		EventStr1AndAllowedDur(String eventStr1, int allowedDur)
		{
			this.eventStr1 = eventStr1;
			this.allowedDur = allowedDur;
		}
		
		String getEventStr1()
		{
			return eventStr1;
		}
		
		int getAllowedDur()
		{
			return allowedDur;
		}
		
		public String toString()
		{
			return "eventStr1: " + eventStr1 + "; allowedDur: " + allowedDur;
		}
	}
}	
