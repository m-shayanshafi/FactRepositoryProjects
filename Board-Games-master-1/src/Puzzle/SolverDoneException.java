package Puzzle;
/*
 * class SolverDoneException extends RuntimeException
 * this exception is used to signal the completion of the solving process for the puzzle
 * default constructor: creates a SolverDoneException
 * constructor: takes a message and creates a SolverDoneException with the message
 * specified in the parameter
 */
public class SolverDoneException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//creates a SolverDoneException	
	public SolverDoneException(){};
	
	/*
	 * @param a message for the exception
	 * creates a SolverDoneException with the message sent in the parameter
	 */
	public SolverDoneException(String message){super(message);}
}

