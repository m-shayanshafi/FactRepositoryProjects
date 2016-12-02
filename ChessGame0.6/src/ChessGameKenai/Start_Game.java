package ChessGameKenai;

/**
 * Start_Game class is used to run the application Instead of having all this
 * unnecessary code in our ChessBoardView class We hide this code in this class
 * and by calling its instance. By hiding the code we are achieving one of the
 * fundamental rules in programming which is "encapsulation" Hiding the data
 * from the user
 * 
 * @author Dimitri Pankov
 * @see ChessBoardView class
 * @version 1.0
 */
public class Start_Game {

	/**
	 * Empty Constructor of the class When the object of this type is
	 * constructed it follows the strict rules and instruction statements which
	 * are specified in the constructor
	 */
	public Start_Game() {

		// CONSTRUCT THE MODEL OBJECT
		final Chess_Data data = Chess_Data.getInstance();
				//new Chess_Data();

		// CONSTRUCT THE MAIN VIEW WHICH IS ALSO A CONTROLLER
		//new ChessBoardView(data);
		ChessBoardView.getInstance(data);
		// USE A SQUARE OBJECT TO CONSTRUCT AN OBJECT OUT OF ITS NESTED CLASS
		final Square square = new Square();
		square.new SendData(data);

		data.notifyView();
	}
}
