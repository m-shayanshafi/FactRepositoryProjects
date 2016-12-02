package Chess;


public class King extends ChessPiece{
	private boolean kcastle = false;
	private boolean qcastle = false;
	private int moveCount = 0;

	public King(int c){
		super(c);
	}
	public boolean canMove(ChessBoard board,int newRow, int newCol){
		//	Case1: if currentLoc equals newRow, newCol
		if(getRow() == newRow && getCol() == newCol)
			return false;
		//	Case2: can castle king-side
		// check if king is in right spot and hasn't moved
		if(((getRow() == 7 && getCol() == 4) || (getRow() == 0 && getCol() == 4)) && 
				moveCount == 0){
			//	check is rook in right spot/color and hasnt moved
			if(board.getOccupant(getRow(),getCol()+3) != null
					&& board.getOccupant(getRow(),7) instanceof Rook 
					&& board.getOccupant(getRow(),7).getColor() == getColor()
					&& ((Rook)board.getOccupant(getRow(),7)).getMoveCount() == 0
					&& !isAttacked(board,getRow(),getCol())
					&& !isAttacked(board,getRow(),5)
					&& !isAttacked(board,getRow(),6)
					&& !isAttacked(board,getRow(),7)
					&& newRow == getRow()
					&& newCol == getCol()+2
					&& board.getOccupant(getRow(), 5) == null
					&& board.getOccupant(getRow(), 6) == null){
				kcastle = true;
				return true;
			}

			//Castle Queen Side
			if(board.getOccupant(getRow(), getCol()-4) != null
					&& board.getOccupant(getRow(), getCol()-4) instanceof Rook
					&& board.getOccupant(getRow(),getCol()-4).getColor() == getColor()
					&& ((Rook)board.getOccupant(getRow(),getCol()-4)).getMoveCount() == 0
					&& !isAttacked(board,getRow(),getCol())
					&& !isAttacked(board,getRow(),getCol()-1)
					&& !isAttacked(board,getRow(),getCol()-2)
					&& newRow == getRow()
					&& newCol == getCol()-2
					&& board.getOccupant(getRow(), getCol()-1) == null
					&& board.getOccupant(getRow(), getCol()-2) == null
					&& board.getOccupant(getRow(), getCol()-3) == null){
				qcastle = true;
				return true;
			}
		}
		if(board.getOccupant(newRow,newCol) == null || board.getOccupant(newRow,newCol).getColor() != getColor()){
			if((getRow() == newRow && (getCol() +1 == newCol || getCol()-1 == newCol))
					|| ((getCol() == newCol && (getRow() +1 == newRow || getRow()-1 == newRow)))){
				return true;
			}
			//	diagonals
			if(getRow()+1 == newRow){
				if(getCol() +1 == newCol
						|| getCol() -1 == newCol)
					return true;
			}
			if(getRow()-1 == newRow){
				if(getCol() +1 == newCol
						|| getCol() -1 == newCol)
					return true;
			}
		}
		return false;
	}

	public boolean isAttacked(ChessBoard board, int r, int c){
		ChessPiece [][] pieces = board.getPieces();
		boolean isAttacked=false;
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null && pieces[i][j].getColor()!=getColor()){
					if(pieces[i][j] instanceof Pawn)
						if(((Pawn) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
					if(pieces[i][j] instanceof Rook)
						if(((Rook) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
					if(pieces[i][j] instanceof Bishop)
						if(((Bishop) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
					if(pieces[i][j] instanceof Nite)
						if(((Nite) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
					if(pieces[i][j] instanceof Queen)
						if(((Queen) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
					if(pieces[i][j] instanceof King && pieces[i][j].getRow() != getRow() && pieces[i][j].getCol() != getCol())
						if(((King) pieces[i][j]).canMove(board,r,c))
							isAttacked=true;
				}
			}
		}
		return isAttacked;
	}

	public void moveTo(ChessBoard board, int newRow, int newCol){
		System.out.println("before " + newRow + ", " + newCol+3);
		if(canMove(board, newRow , newCol)){
			if(qcastle){
				System.out.println("qqqqqqqqqqqqqqqqq"  + "   " + "newRow: " + newRow + " newCol: " + newCol);
				System.out.println("qqqqqqqqqqqqqqqqq"  + "   " + board.getOccupant(getRow(), getCol()-4));
				if(board.getOccupant(getRow(), getCol()-4) instanceof Rook){
					Rook r = (Rook) board.getOccupant(getRow(), getCol()-4);
					board.remove(getRow(), getCol()-4);
					r.setRow(getRow());
					r.setCol(getCol()-1);
					board.setPiece(getRow(), getCol()-1, r);
					board.remove(getRow(),getCol());
					setRow(newRow);
					setCol(newCol);
					board.setPiece(newRow,newCol,this);
					moveCount++;
					return;
				}		
			}
			if(kcastle){
				System.out.println("kkkkkkkkkkkkkkk " + "   " + "newRow: " + newRow + " newCol: " + newCol);
					if(board.getOccupant(newRow, newCol+1) instanceof Rook){
						Rook r = (Rook) board.getOccupant(newRow,newCol+1);
						board.remove(newRow,newCol+1);
						System.out.println("Rook Gone: " + board.getOccupant(newRow, newCol-4)==null);
						r.setRow(getRow());
						r.setCol(newCol-1);
						board.setPiece(getRow(), newCol-1, r);
						board.remove(getRow(),getCol());
						setRow(newRow);
						setCol(newCol);
						board.setPiece(newRow,newCol,this);
						moveCount++;
						return;
					}
			}
			board.remove(getRow(),getCol());
			setRow(newRow);
			setCol(newCol);
			board.setPiece(newRow,newCol,this);
			moveCount++;
		}
		else
			System.out.println("Invalid Move");
	}

	public void castle(int x, ChessBoard board){

		if(x==0){
			board.setPiece(7,6,board.getOccupant(7,4));
			board.setPiece(7, 5, board.getOccupant(7,7));
		}
		if(x==1){
			board.setPiece(7,6,board.getOccupant(7,4));
			board.setPiece(7, 5, board.getOccupant(7,0));
		}
		if(x==2){
			board.setPiece(7,6,board.getOccupant(0,4));
			board.setPiece(7, 5, board.getOccupant(0,7));
		}
		if(x==3){
			board.setPiece(7,6,board.getOccupant(0,4));
			board.setPiece(7, 5, board.getOccupant(0,0));
		}
	}
	/*
	 * returns false if the king cannot move
	 */
	private boolean checkHelper(ChessBoard board, int r, int c){
		if(canMove(board,r,c) && board.getOccupant(r,c).getColor() != getColor()){
			// add for loop here
		}
		if(canMove(board,r,c) == false){
			return false;
		}
		else if(isAttacked(board,r,c) == true){
			return false;
		}
		else return true;
	}
	/*
	 * return true if in checkmate
	 */
	public boolean isCheckMate(ChessBoard board){
		int r = getRow();
		int c = getCol();
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if(checkHelper(board,r+i,c+j) == false){
					return true;
				}
			}
		}
		return false;
		//return (isAttacked(board,r,c) && isAttacked(board,r,c+1) && isAttacked(board,r,c-1) && isAttacked(board,r+1,c) && isAttacked(board,r-1,c) && isAttacked(board,r+1,c+1) && isAttacked(board,r+1,c-1) && isAttacked(board,r-1,c+1) && isAttacked(board,r-1,c-1));
	}


	public String toString(){
		String s = "";
		if(getColor()==0)
			s = "k";
		else 
			s = "K";
		return s;
	}
}