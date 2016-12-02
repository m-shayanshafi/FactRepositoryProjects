package Chess;


public class Rook extends ChessPiece{
	
	private int moveCount=0;
	
	public Rook(int c){
		super(c);
	}

	public int getMoveCount(){return moveCount;}

	public boolean canMove(ChessBoard board, int newRow, int newCol){
		//	make sure the piece's new location is in bounds
		if(!board.isInbound(newRow,newCol))
			return false;
		if(board.getOccupant(newRow, newCol) == null || (board.getOccupant(newRow, newCol).getColor() != getColor())){
			//	Case 1: piece does not change location
			if(getRow() == newRow && getCol() == newCol)
				return false;
			return clearPath(board,newRow,newCol);
		}
		return false;
	}

	public boolean clearPath(ChessBoard board, int newRow, int newCol){
		//	Case1: Vertically to the down
		if(getCol() == newCol && getRow() > newRow){
			// adjacent spot
			if(newRow == getRow() -1)
				return true;
			int x = getRow() -1;
			while(board.getOccupant(x, getCol()) == null && x != newRow){
				if(x == newRow+1)
					return true;
				x--;
			}
			return false;
		}
		//	Case2: Vertically to the up
		if(getCol() == newCol && getRow() < newRow){
			// adjacent spot
			if(newRow == getRow() +1)
				return true;
			int x = getRow() +1;
			while(board.getOccupant(x, getCol()) == null && x != newRow){
				if(x == newRow-1)
					return true;
				x++;
			}
			return false;
		}
		//	Case3: horizontally going left
		if(getCol() > newCol && getRow() == newRow){
			// adjacent spot
			if(newCol == getCol() -1)
				return true;
			int x = getCol() -1;
			while(board.getOccupant(getRow(), x) == null && x != newCol){
				if(x == newCol+1)
					return true;
				x--;
			}
			return false;
		}
		//	Case4: horizontally going right
		if(getCol() < newCol && getRow() == newRow){
			// adjacent spot
			System.out.println("slow");
			if(newCol == getCol() +1)
				return true;
			int x = getCol() +1;
			while(board.getOccupant(getRow(), x) == null && x != newCol){
				if(x == newCol-1)
					return true;
				x++;
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
			moveCount++;
		}
		else
		System.out.println("Invalid Move");
}

public String toString(){
	String s = "";
	if(getColor()==0)
		s = "r";
		else 
			s = "R";
			return s;
}
}

/*
 * //		ChessPiece[][] pieces = board.getPieces();
//		boolean move = false;
//		boolean blocked =false;
//		if(newRow==getRow()&&newCol==getCol()||board.isInbound(newRow,newCol)==false)
//			return move;
//		if(newRow==getRow()&&newCol>getCol())
//			for(int i=getCol(); i<newCol; i++){
//				if(pieces[getRow()][i]!=null)
//					blocked = true;
//			}
//		else if(newRow==getRow()&&newCol<getCol()){
//			for(int i=newCol; i<getCol(); i++){
//				if(pieces[getRow()][i]!=null)
//					blocked = true;
//			}
//		}
//
//		else if(newCol==getCol()&&newRow>getRow()){
//			for(int i = getRow(); i <newRow; i++){
//				if(pieces[i][getCol()]!=null)
//					blocked = true;
//			}
//		}
//		else if(newCol==getCol()&&newRow<getRow()){
//			for(int i = newRow; i <getRow(); i++){
//				if(pieces[i][getCol()]!=null)
//					blocked = true;
//			}
//		}
//
//
//		if((pieces[newRow][newCol]==null || pieces[newRow][newCol].getColor()!=getColor()) && (newRow==getRow() || newCol==getCol()) && blocked==false &&this.ownInCheck(board)==false && board.isInbound(newRow,newCol))
//			move = true;
//		return move;
 */
