
package gui2d;

/* package import */
import java.lang.*;

/**
 * Exception declaring that a parsing error has occured while parsing the 
 * svg file
 * @author	Davy Herben
 * @version 2002/03/25
 */
public class DCSvgParserException extends Exception {
	/*
	 * CONSTRUCTORS
	 */
	
	public DCSvgParserException() {
		super();
	}

	public DCSvgParserException(String s) {
		super(s);
	}
}	
