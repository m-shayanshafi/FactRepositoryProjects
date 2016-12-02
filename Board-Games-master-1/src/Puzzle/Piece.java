package Puzzle;

/*
 * class Piece stores and controls the data and functions of a puzzle Piece
 * data: int initialTop, int initialBottom, int initialRight, int initialLeft, int left 
 * int bottom, int right, int top, int homeRow, and int homeColumn
 * constructor: takes 4 integers and delegates work to the 6 argument constructor,
 * sending it a default home location of (0, 0)
 * constructor: takes a home row, home column, and 4 integers. Sets the four sides corresponding
 * to the 4 integers and sets the home location values based on the values in the argument 
 * getters: getShape(), getHomeRow(), and getHomeColumn()
 * methods: rotateClockwise(), rotateCounterClockwise(), reset(), fits()
 * canFit(int, int, Piece[][]), canFit(int, int, PieceHolder) toString(), and main()
 */
public class Piece{
	private final int initialTop; //the top side
	private final int initialBottom; //the bottom side
	private final int initialRight; //the right side
	private final int initialLeft; //the left side
	protected final int homeRow; //home row in tray:
	protected final int homeColumn; //home column in tray
	private int top; //the top side
	private int bottom; //the bottom side
	private int right; //the right side
	private int left; //the left side
	
	/*
	 * @param the 4 integers to represent the sides of the piece
	 * a positive integer means the shape is sticking out and a negative integer means 
	 * the piece is sticking in
	 * 1 means a spade
	 * 2 means a club
	 * 3 means a diamond
	 * 4 means a heart
	 */
	public Piece(int t, int r, int b, int l){
		//sends the default location of (0, 0) for the home location
		this(0, 0, t, r, b, l);
	}
	
	/*
	 * @param the home row, home column, and 4 integers to represent the sides of the piece
	 * a positive integer means the shape is sticking out and a negative integer means 
	 * the piece is sticking in
	 * 1 means a spade
	 * 2 means a club
	 * 3 means a diamond
	 * 4 means a heart 
	 */
	public Piece(int row, int col, int t, int r, int b, int l){
		initialTop = t;
		top = t;
		initialRight = r;
		right = r;
		initialBottom = b;
		bottom = b;
		initialLeft = l;
		left = l;
		homeRow = row;
		homeColumn = col;
	}
	
	/*
	 * @param the integer representing the side to be returned
	 * @return the integer representing the shape at the specified side
	 */
	public int getShape(int side){
		if(side == Side.top)
			return top;
		else if(side == Side.bottom)
			return bottom;
		else if(side == Side.right)
			return right;
		else if(side == Side.left)
			return left;
		else
			return 0;
	}
	
	//@return the homeRow
	public int getHomeRow(){return homeRow;}
	
	//@return the homeColumn
	public int getHomeColumn(){return homeColumn;}
	
	/*
	 * rotates the sides of the piece 90 degrees clockwise
	 */
	public void rotateClockwise(){
		int tempN = top;
		int tempE = right;
		int tempS = bottom;
		int tempW = left;
		top = tempW;
		right = tempN;
		bottom = tempE;
		left = tempS;
	}
	
	/*
	 * rotates the sides of the piece 90 degrees counterclockwise
	 */
	public void rotateCounterClockwise(){
		int tempN = top;
		int tempE = right;
		int tempS = bottom;
		int tempW = left;
		top = tempE;
		right = tempS;
		bottom = tempW;
		left = tempN;
	}
	
	/*
	 * resets the sides of the piece to the way they were when the piece was constructed
	 */
	public void reset(){
		top = initialTop;
		right = initialRight;
		bottom = initialBottom;
		left = initialLeft;
	}
	
	/*
	 * checks whether a Piece can fit in a specific location in a Piece[][]
	 * @param the row where the Piece is to be added
	 * @param the column where the Piece is to be added
	 * @param the Piece[][] that the Piece is to be added into
	 * @return true if the Piece fits
	 * @return false if it does not
	 */
	public boolean canFit(int row, int column, Piece[][] p){
		return canFit(row, column, new PieceHolder(p));
	}
	
	/*
	 * checks whether a Piece can fit in a specific location in the PieceHolder
	 * @param the row where the Piece is to be added
	 * @param the column where the Piece is to be added
	 * @param the PieceHolder that the Piece is to be added into
	 * @return true if the Piece fits
	 * @return false if it does not
	 */
	public boolean canFit(int r, int c, PieceHolder pieceHolder){
		
		if(pieceHolder.getOccupant(r, c) != null || !pieceHolder.isValid(r, c))
			return false;
		
		/*
		 *if any of these Pieces is null, then the location sent in the getOccupant()
		 * method is either empty or invalid 
		 */
		Piece topPiece = null;
		Piece rightPiece = null;
		Piece bottomPiece = null;
		Piece leftPiece = null;
		if(pieceHolder.getOccupant(r - 1, c) != null && pieceHolder.isValid(r-1, c))
			topPiece = pieceHolder.getOccupant(r - 1, c);
		if(pieceHolder.getOccupant(r, c + 1) != null && pieceHolder.isValid(r, c+1))	
			rightPiece = pieceHolder.getOccupant(r, c + 1);
		if(pieceHolder.getOccupant(r + 1, c) != null && pieceHolder.isValid(r+1,c))
			bottomPiece = pieceHolder.getOccupant(r + 1, c);
		if(pieceHolder.getOccupant(r, c - 1) != null && pieceHolder.isValid(r, c-1))
			leftPiece = pieceHolder.getOccupant(r, c - 1);
		boolean fits = true;
		
		//if the location above the specified location is occupied
		if(topPiece != null){
			//checks the Piece to be fit against the one above it
			if(top != -topPiece.getShape(Side.bottom))
				fits = false;
		}
		//if the location to the right the specified location is occupied
		if(rightPiece != null){
			//checks the Piece to be fit against the one to its right
			if(right != -rightPiece.getShape(Side.left))
				fits = false;
		}
		//if the location to the left the specified location is occupied
		if(leftPiece != null){
			//checks the Piece to be fit against the one to its left
			if(left != -leftPiece.getShape(Side.right))
				fits = false;
		}

		//if the location underneath the specified location is occupied
		if(bottomPiece != null){
			//checks the Piece to be fit against the one below it
			if(bottom != -bottomPiece.getShape(Side.top)){
				fits = false;
			}
		}

		//System.out.println("fits = " + fits);
		return fits;
	}
	
	/*
	 * checks whether a Piece fits in a given spot in a Piece[][] according to the
	 * rules of the Puzzle
	 * @param the intended row of the Piece
	 * @param the intended column of the Piece
	 * @param the Piece[][] to check the Piece in
	 * @return true if the Piece fits in the location of the Piece[][] in any orientation
	 * @return false if the Piece does not fit in the location of Piece[][] in any
	 * orientation
	 */
	public boolean fits(int row, int column, Piece[][] p){
		/*
		 * allows four rotations so that after the loop, 
		 * the Piece will be back in its original orientation
		 */
		for(int i = 1; i <= 4; i++){
			if(canFit(row, column, p))
				return true;
			rotateClockwise();
		}
		return false;
	}
		
	/*
	 * @param the Piece to compare this Piece to
	 * @return true if this Piece fits the Piece in the parameter in any orientation
	 * @return false if this Piece does not fit the Piece in the parameter in any orientation
	 */
	public boolean fits(Piece p){
		int testTop = - p.getShape(Side.top);
		int testRight = - p.getShape(Side.right);
		int testBottom = - p.getShape(Side.bottom);
		int testLeft = - p.getShape(Side.left);
		return top == testTop || top == testRight || top == testBottom || top == testLeft
				|| right == testTop || right == testRight ||right == testBottom || right == testLeft 
				|| bottom == testTop || bottom == testRight || bottom == testBottom || bottom == testLeft
				|| left == testTop || left == testRight || left == testBottom || left == testLeft;
	}
	
	/*
	 * @param the Piece to compare this Piece to
	 * @param the side of the Piece in the parameter to check for a fit
	 * @return true if this Piece fits the Piece in the parameter
	 *  at the specified side in any orientation
	 * @return false if this Piece does not fit the Piece in the parameter at 
	 *  the specified side in its current orientation
	 */
	public boolean fits(Piece p, int side){
		int shape = p.getShape(side);
		return top == -shape || bottom == -shape || left == -shape || right == -shape;
	}
	
	/*
	 * tells whether this Piece fits into two Pieces at specified sides 
	 * @param p1: the first Piece, side1: the side of the first Piece
	 *  p2: the second Piece, side2: the side of the second Piece
	 * @return true if this Piece fits the specified Pieces at the specified sides
	 * in its current orientation
	 * @return false otherwise
	 */
	public boolean fits(Piece p1, int side1, Piece p2, int side2){
		//none of the specified sides are allowed to be the same
		if(side1 == side2)
			return false;
	
		int shape1 = p1.getShape(side1);
		int shape2 = p2.getShape(side2); 
		
		/*
		 *if the Piece must fit directly between two Pieces (i.e. not in a corner formed
		 *by the two pieces
		 */
		if(side1 == -side2){
			return (top == -shape1 && bottom == -shape2) || (top == -shape2 && bottom == -shape1)
					|| (right == -shape1 && left == -shape2) || (right == -shape2 && left == -shape1);
		}
		//if the Piece must fit in a corner formed by the other two Pieces
		else{
			//checks the possibilities of how the corner is formed
			if(side1 == Side.top && side2 == Side.right)
					return bottom == -shape1 && left == -shape2;
			else if(side1 == Side.right && side2 == Side.bottom)
				return left == -shape1 && top == -shape2;
			else if(side1 == Side.bottom && side2 == Side.left)
				return top == -shape1 && right == -shape2;
			else if(side1 == Side.left && side2 == top)
				return right == -shape1 && bottom == -shape2;
			else
				return fits(p2, side2, p1, side1);
		}
	}
	
	/*
	 * tells whether this Piece fits into three Pieces at specified sides 
	 * @param p1: the first Piece, side1: the side of the first Piece
	 *  p2: the second Piece, side2: the side of the second Piece
	 *  p3: the third Piece, side3: the side of the third Piece
	 * @return true if this Piece fits the specified Pieces at the specified sides
	 * in its current orientation
	 * @return false otherwise
	 */
	public boolean fits(Piece p1, int side1, Piece p2, int side2, Piece p3, int side3){
		int shape3 = p3.getShape(side3);
		
		//none of the specified sides are allowed to be the same
		if(side1 == side2 || side1 == side3 || side2 == side3)
			return false;
		
		//first checks if the Piece fits between two of the three Pieces
		if(fits(p1, side1, p2, side2)){
			//checks the possibilities for the sides of the third Piece
			if(side3 == Side.top)
				return bottom == -shape3;
			else if(side3 == Side.right)
				return left == -shape3;
			else if(side3 == Side.bottom)
				return top == -shape3;
			else if(side3 == Side.left)
				return right == -shape3;
		}
		//will only be reached if the integers sent do not represent the sides of a Piece
		return false; 
	}
	
	/*
	 * tells whether this Piece fits into four Pieces at specified sides 
	 * @param p1: the first Piece, side1: the side of the first Piece
	 *  p2: the second Piece, side2: the side of the second Piece
	 *  p3: the third Piece, side3: the side of the third Piece
	 *  p4: the fourth Piece, side4: the side of the fourth Piece
	 * @return true if this Piece fits the specified Pieces at the specified sides
	 * in its current orientation
	 * @return false otherwise
	 */
	public boolean fits(Piece p1, int side1, Piece p2, int side2, Piece p3, int side3, 
																	Piece p4, int side4){
		//none of the specified sides are allowed to be the same
		if(side1 == side2 || side1 == side3 || side1 == side4 || side2 == side3 
				|| side2 == side4 || side3 == side4)
			return false;
		
		int shape4 = p4.getShape(side4);
		
		//first checks if the Piece fits between three of the four Pieces
		if(fits(p1, side1, p2, side2, p3, side3)){
			//checks the possibilities for the sides of the fourth Piece
			if(side4 == Side.top)
				return bottom == -shape4;
			else if(side4 == Side.right)
				return left == -shape4;
			else if(side4 == Side.bottom)
				return top == -shape4;
			else if(side4 == Side.left)
				return right == -shape4;
		}
		//will only be reached if the integers sent do not represent the sides of a Piece
		return false; 
	}
	
	//@return a String form of the piece
	public String toString(){
		String s = "  " + top + "\n\b" + left + "  " + right + "\n  " + bottom;
		return s;
	}
	public boolean equals(Piece c){
		return toString().equals(c.toString());
	}
}
