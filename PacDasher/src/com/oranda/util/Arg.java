package com.oranda.util;

import com.oranda.util.Output;

import java.util.Iterator;
import java.util.List;

/**
 * Principally for validating arguments.
 */
public class Arg
{
	
	public static void validateObjects(Object o1, Object o2)
	{		
		validateObject(o1);
		validateObject(o2);
	}

	public static Object validateObject(Object o)
	{	
		if (o == null)
		{
			throw new IllegalArgumentException("Null object");
		}
		return o;
    }
	
	public static void validateStrings(String s1, String s2)
	{
		validateString(s1);
		validateString(s2);
	}
	
	public static String validateString(String s)
	{
		validateObject(s);
	    if ("".equals(s))
		{
			throw new IllegalArgumentException("Empty string");
		}
		return s;
	}
	
	/**
	 * A negative degree indicates the opposite of the relation. E.g. 
	 * "militates against" instead of "leads to".
	 */
	public static float validateDegree(float degree) 
	{
		if (degree < -1.0f || degree > 1.0f)
		{
			throw new IllegalArgumentException("degree " + degree
			    + " out of range");
		}
		return degree;
	}
	
	public static Float validateDegree(Float degree)
	{
		validateDegree(degree.floatValue());
		return degree;
	}
	
	public static List validateList(List list)
	{
	    Iterator i = list.iterator();
		try
		{
			while (i.hasNext())
			{
				Object o = i.next();
				validateObject(o);
			}
		}
		catch (IllegalArgumentException iae)
		{
			Output.error("null in list, list contents: " + list);
			throw iae;
		}
		return list;
	}
	
	public static long validatePositive(long number)
	{
		if (number < 0)
		{
			throw new IllegalArgumentException("Number is negative: " + number);
		}
		return number;
	}
	
	public static int validatePositive(int number)
	{
		if (number < 0)
		{
			throw new IllegalArgumentException("Number is negative: " + number);
		}
		return number;
	}
	
	public static void validatePositives(int number1, int number2)
	{
		if (number1 < 0 || number2 < 0)
		{
			throw new IllegalArgumentException("A number is negative: " 
					+ number1 + " or " + number2);
		}
	}
}
