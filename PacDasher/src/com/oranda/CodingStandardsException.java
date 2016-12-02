/**
 * Copyright statement.
 * e.g. Copyright (c) 2003-2005 James McCabe
 */
package com.oranda;

/** 
 * Description of the type of exception this class handles.
 * 
 * Note: The book Effective Java recommends that you should
 * only throw a checked exception if you think some piece of code
 * up the chain of calling can handle it -- in other words it is recoverable.
 * If the caller is just going to print it out or do System.exit() then it
 * would be better to consider some other approach. 
 * (An unchecked exception is best thrown when there is a programming
 * error, such as when the caller breaks the contract.) 
 */
public class CodingStandardsException extends Exception
{
    public CodingStandardsException() { super(); }
    public CodingStandardsException(String s) { super(s); }
    public CodingStandardsException(Throwable cause){ super(cause); }
    public CodingStandardsException(String s, Throwable c) { super(s, c); }
}