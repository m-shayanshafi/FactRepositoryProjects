package fruitwar.core;

class RobotException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5425648681268811304L;

	RobotException(String message){
		super(message);
	}
	
	RobotException(String message, Throwable cause){
		super(message, cause);
	}
}
