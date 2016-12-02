/**
 * Copyright statement.
 * e.g. Copyright (c) 2003-2005 James McCabe
 */
package com.oranda;

// Rules for qualifying imports:
// 1. All com.oranda imports must be qualified
// 2. External (outside com.oranda) imports may be unqualified (i.e., .*) if:
//    a) There are more than two imports from the same package, and
//    b) There is no ambiguity of class names between packages imported.

// com.oranda.* imports
import com.oranda.util.Output;

// java.* imports
import java.util.ArrayList;
import java.util.List;

// javax.* imports
import javax.xml.parsers.SAXParserFactory;

// Third-party imports
import org.xml.sax.helpers.DefaultHandler;

/**
 * Oranda Java Coding Standards. Description of the class goes here.
 */
public class CodingStandards // max class name 24 chars (30 for Exceptions)
{ // Open brace style. 4 spaces for indentation, 8 for continuation.
    
    static
    {
        // Avoid doing any static initialization, because the order in which
        // static blocks is executed is nondeterministic and this causes 
        // great confusion, especially if I inadventently call real code like 
        // the getInstance() method of a singleton
    }
    
	/**
	 * Constants. Order by accessibility.
	 */
	// Optional comment
	public static String CODING_STANDARDS_PUB_VAR = "Coding Standards Pub Var";
	// Optional comment
	protected static long TIME_TO_SLEEP = 4000;
	
	/**
	 * Class variables. Order by accessibility.
	 */
	// codingStandards is the one true instance of this singleton
	private static CodingStandards codingStandards;
	
	/**
	 * Member variables. Order by accessibility.
	 */
	protected int[] arrayInt = { 0, 1, 2 };
	
	// avoid names of collections in var names in case the type changes
	protected List<Integer> ages; // "listOfAges" would be inflexible
	
    // Optional comment
	protected String memberVar1;
	// Optional comment
	protected String memberVarLongName; // max var name 24 chars

	
	/***************************************************************************
	 * Constructors.
	 **************************************************************************/
	 
	/**
	 * Suppose this were not a singleton and there were other constructors.
	 * Start with the constructors with the less number of parameters.
	 * The simpler constructor should call the more complex constructor with
     * defaults...
	 * Comments optional for constructors.
	 */
	public CodingStandards(String memberVar1)
    {
		this(memberVar1, memberVar1 + "longName");
    }

	/**
	 * Public constructor 2.
	 */
	public CodingStandards(String memberVar1, String memberVarLongName)
	{
		setMemberVar1(memberVar1);
		setMemberVarLongName(memberVarLongName);
	}

	/**
	 * For a singleton or other special purpose, put constructor on a
	 * single line. Usually use the open brace style.
	 */
	private CodingStandards() {}

	/**
	 * Method comments.
	 * getInstance() immediately follows constructor because it is involved
	 * in construction.
	 */
	public synchronized static CodingStandards getInstance()
	{
		if (codingStandards == null)
		{
		    codingStandards = new CodingStandards();
		}
		return codingStandards;
    }
	
	/***************************************************************************
	 * Getters and setters (always after the constructors).
	 **************************************************************************/

	public String getMemberVar1() { return memberVar1; }
	// Note: a line may go up to 84 chars, but if there has to be a break,
	// it should be before 80.
	public void setMemberVar1(String memberVar1) { this.memberVar1 = memberVar1; }

	public String getMemberVarLongName() { return memberVarLongName; }
	public void setMemberVarLongName(String memberVarLongName) 
	{ this.memberVarLongName = memberVarLongName; }
	

	/***************************************************************************
	 * Core functionality methods. 
	 * Class-specific. Group by functionality, not accessibility.
	 **************************************************************************/
	 
	/**
	 * Method comments.
	 */
	public void execute()
	{
		// Sleeping can be put on or two lines. For other exceptions, use the
		// full open brace style.
		try { Thread.currentThread().sleep(TIME_TO_SLEEP); } 
		catch (InterruptedException ie) {}
		
		// Line breaks.
		// Follow the Sun convention of breaking BEFORE an operator
		// http://java.sun.com/docs/codeconv/CodeConventions.pdf
		// (although Geosoft and others say break AFTER
		// http://geosoft.no/development/javastyle.html)
		String localMsg = "Break after a comma. Before an operator. Like this "
		        + "for example. Note the breaking space is on the first line.";
			
		/* 
		 * Disallowed:
		 * if (var == true) System.out.println("no curlies for if");
		 * for (int i = 0; i < 10; i++) System.out.println("no curlies for for");
		 */
	}
	
	/**
	 * There are two styles of indenting method parameters, depending on
	 * how far out the parameters start. First style:
	 */
	void someMethod(int anArg, Object anotherArg, String yetAnotherArg,
                    Object andStillAnother) 
	{
        return;
    }
	
    /**
	 * Second style. Indent 8 spaces to avoid very large indents.
	 * @param  anArg optional description of parameter, carried on to the next
	 * line without indentation
	 * @return optional description of return value
	 * @throws very optional description of exception thrown
	 * @see very optional reference to another method, class, package, etc.
	 */
    private synchronized String horkingLongMethodName(int anArg,
            Object anotherArg, String yetAnotherArg,
            Object andStillAnother) // max method name 27 chars
			throws CodingStandardsException // continuation indent. for throws
	{
		// For similar local or member variables, it is acceptable to separate
		// them with commas.
		boolean condition1 = true, condition2 = true, condition3 = true,
		    condition4 = true, condition5 = true, condition6 = true;
			
		// Use 8-space wraps where seeing the body of a block might otherwise
		// be difficult.
        if ((condition1 && condition2)
		        || (condition3 && condition4)
                ||!(condition5 && condition6)) 
		{
            return "whatever";
        }
		
        // This is also ok.
        if ((condition1 && condition2) || (condition3 && condition4)
                ||!(condition5 && condition6)) 
	    {
			int arrayValue;
			try
			{
				arrayValue = arrayInt[1];
			}
			catch (IndexOutOfBoundsException ioobe)
			{
                throw new CodingStandardsException("Could not get arrayInt[1]",
				        ioobe);
			}
        }
		
		int sum = 0;
		
		// Spacing in a for loop
		for (int i = 0; i < arrayInt.length; i++) 
		{
			sum += arrayInt[i];
		}
			 
        return "whatever";
    } // A method can have a maximum of 60 lines including comments

	/***************************************************************************
	 * Standard Object methods like clone, equals, hashKey, and toString.
	 * Comments optional.
	 **************************************************************************/
	
	public boolean equals(Object obj)
	{
		CodingStandards other = (CodingStandards) obj;
		if ((this.memberVar1 == other.memberVar1)
			    && (this.memberVarLongName == other.memberVarLongName))
	    {
			return true;
		}
		return false;
	}
	
    public String toString()
    {
		// For objects with a lot of state, only give the summary or 
		// identifying data. For a full dump, use a separate method.
		return this.memberVar1 + " " + this.memberVarLongName;
	}

	/***************************************************************************
	 * main() method for simple tests. Always the last method.
	 * To do tests across multiple classes, create a separate test class 
	 * (JUnit preferred).
	 * Comments optional.
	 **************************************************************************/
	public static void main(String[] args)
	{
		CodingStandards codingStandards = CodingStandards.getInstance();
		Output.debug("Coding standards is " + codingStandards);
		// this is the standard for stuff remaining to be done:
		// TODO
		// or
		Output.debug("more stuff to do"); // TODO
	}
	 
} // A class can have a maximum of 500 lines including comments



/**
 * MISCELLANEOUS NOTES ON CODING STANDARDS
 * 
 * o  For variable names use "fileName" and "pathName" instead of "filename" 
 *   and "pathname". The J2SE API itself is inconsistent, but "name" is a 
 *   separate concept.
 * 
 * o  Catch exceptions when you can; declare them when you must. Translate 
 *   them to higher levels of abstractions as appropriate.
 *   Deal with exceptions as soon as possible, but in the right place.
 * 
 */


