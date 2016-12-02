package Chess;

public class Bishop extends ChessPiece{

	public Bishop(int c){
		super(c);
	}
	public boolean canMove(ChessBoard board, int newRow, int newCol){
		//	Case 1: if the spot the piece is moving to is the current location
		if(getRow() == newRow && getCol() == newCol)
			return false;
		// the next cases all deal with the fact that newRow, newCol is a valid, empty spot on the board
		if(board.isInbound(newRow,newCol) == true && board.getOccupant(newRow, newCol) == null){
			//	Case 2: can move forwards or backwards diagonally
			//	find slope of line
			//	if the slope is undefined, return false
			double slope = 0;
			try{
				slope = (newRow - getRow())/(newCol - getCol());
			}
			catch(Exception e){
				return false;
			}
			// slope must be + or - 1
			if(Math.abs(slope) == 1){
				// check if the path is clear of obstacles
				if(clearPath(board,newRow,newCol))
					return true;
			}
			else return false;
		}
		//	capturing a piece
		if(board.getOccupant(newRow,newCol) != null){
			//	if the colors are opposites
			if(getColor() != board.getOccupant(newRow, newCol).getColor()){
				double slope = 0;
				try{
					slope = (newRow - getRow())/(newCol - getCol());
				}
				catch(Exception e){
					return false;
				}
				// slope must be + or - 1
				if(Math.abs(slope) == 1){
					// check if the path is clear of obstacles
					if(clearPath(board,newRow,newCol))
						return true;
				}
				else return false;
			}
		}
		return false;
	}
	public boolean clearPath(ChessBoard board, int newRow,int newCol){
		// if the new point is underneath the piece to the right
		if((newCol - getCol()) > 0 && newRow - getRow() > 0){
			if(getCol() +1 == newCol && getRow() +1 == newRow)
				return true;
			int x = getRow()+1;
			int y = getCol()+1;
			while(board.getOccupant(x, y) == null && x != newRow && y != newCol){
				if(x == newRow-1 && y == newCol-1)
					return true;
				x++;
				y++;
			}
			return false;
		}
		//	if the new point if above the piece to the left
		if((newCol - getCol()) < 0 && newRow - getRow() < 0){
			if(getCol() -1 == newCol && getRow() -1 == newRow)
				return true;
			int x = getRow()-1;
			int y = getCol()-1;
			System.out.println("upper left: " + ((board.getOccupant(x, y) == null && x != newRow && y != newCol)));
			System.out.println("start: " + x + " , " + y);
			while(board.getOccupant(x, y) == null && x != newRow && y != newCol){
				System.out.println("upper leftWHILE: " + ((board.getOccupant(x, y) == null && x != newRow && y != newCol)));
				if(x == newRow+1 && y == newCol+1)
					return true;
				x--;
				y--;
			}
			return false;
		}
		//	if the new point if above the piece to the right
		if( newCol - getCol() > 0 && newRow - getRow() < 0){
			if(getCol() +1 == newCol && getRow() -1 == newRow)
				return true;
			int x = getRow()-1;
			int y = getCol()+1;
			while(board.getOccupant(x, y) == null && x != newRow && y != newCol){
				if(x == newRow+1 && y == newCol-1)
					return true;
				x--;
				y++;
			}
			return false;
		}
		//	if the new point is bottom left
		if(newRow - getRow() > 0 && newCol - getCol() < 0){
			if(getCol() -1 == newCol && getRow() +1 == newRow)
				return true;
			int x = getRow()+1;
			int y = getCol()-1;
			while(board.getOccupant(x, y) == null && x != newRow && y != newCol){
				if(x == newRow-1 && y == newCol+1)
					return true;
				x++;
				y--;
			}
			return false;
		}
		return false;
	}
	public void moveTo(ChessBoard board, int newRow, int newCol){
		if(canMove(board, newRow , newCol)){
			board.remove(getRow(),getCol());
			setRow(newRow);
			setCol(newCol);
			board.setPiece(newRow,newCol,this);
		}
		else
			System.out.println("Invalid Move");
	}
	
	public String toString(){
		String s = "";
		if(getColor()==0)
			s = "b";
			else 
				s = "B";
				return s;
		
		
	}
}
