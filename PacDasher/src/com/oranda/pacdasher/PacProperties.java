/**
 *  PacDasher application. For explanation of this class, see below. 
 *  Copyright (c) 2003-2005 James McCabe. Email: code@oranda.com 
 *  http://www.oranda.com/java/pacdasher/
 * 
 *  PacDasher is free software under the Aladdin license (see license  
 *  directory). You are free to play, copy, distribute, and modify it
 *  except for commercial purposes. You may not sell this code, or
 *  compiled versions of it, or anything which incorporates either of these.
 * 
 */
 
package com.oranda.pacdasher;

import java.util.Properties;

public class PacProperties extends Properties
{

	public int getPropertyInt(String key)
	{
		String value = "";
		int intValue = 0;
		try
		{
			value = getProperty(key);
			intValue = (new Integer(value)).intValue();
		}
		catch (NumberFormatException nfe)
		{
			Const.logger.severe("property value found for " + key 
			    + " is " + value + " : not an integer");
			System.exit(-1);
		}			
		return intValue;
	}
    
}