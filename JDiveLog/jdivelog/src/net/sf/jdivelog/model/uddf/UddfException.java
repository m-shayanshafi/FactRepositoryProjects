package net.sf.jdivelog.model.uddf;


/**
 * Represents an error during UDDF File Parsing
 * 
 * @author Levtraru
 *
 */
public class UddfException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public UddfException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public UddfException(String message, Throwable e) {
		super(message,e);
	}

}
