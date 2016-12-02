package Chess;


public class Queen extends ChessPiece{

	public Queen(int c){
		super(c);
	}
	public boolean canMove(ChessBoard board, int newRow, int newCol){
		// currentLoc = newRow,newCol
		if(getRow() == newRow && getCol() == newCol)
			return false;
		Bishop bis = new Bishop(getColor());
		bis.setRow(getRow());
		bis.setCol(getCol());
		Rook rok = new Rook(getColor());
		rok.setRow(getRow());
		rok.setCol(getCol());
		return (rok.canMove(board, newRow, newCol)) || (bis.canMove(board, newRow, newCol));
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
			s = "q";
		else 
			s = "Q";
		return s;
	}
}

/*
 * ChessPiece[][] pieces = board.getPieces();
//		int r = getRow();
//		int c = getCol();
//		double newR = newRow;
//		double newC = newCol;
//		double oldR = getRow();
//		double oldC = getCol();
//		boolean move = false;
//		
//		boolean blocked = false;
//		boolean rook = false;
//		boolean bishop = false;
//		boolean attacking = false;
//		if(newRow==r&&newCol==c||board.isInbound(newRow,newCol)==false)
//			return move;
//		if(newRow==getRow()||newCol==getCol())
//			rook=true;
//		if(((newRow - getRow())/ ((newR - oldR)/ (newC-oldC))==1 || (newR - oldR)/ (newC-oldC)==-1))
//			bishop = true;
//		if(rook){
//			if(newRow==getRow()&&newCol>getCol())
//				for(int i=getCol()+1; i<newCol; i++){
//					if(pieces[getRow()][i]!=null)
//						blocked = true;
//				}
//			else if(newRow==getRow()&&newCol<getCol()){
//				for(int i=newCol; i<getCol(); i++){
//					if(pieces[getRow()][i]!=null)
//						blocked = true;
//				}
//			}
//				
//			else if(newCol==getCol()&&newRow>getRow()){
//				for(int i = getRow(); i <newRow; i++){
//					if(pieces[i][getCol()]!=null)
//						blocked = true;
//				}
//			}
//			else if(newCol==getCol()&&newRow<getRow()){
//				for(int i = newRow; i <getRow(); i++){
//					if(pieces[i][getCol()]!=null)
//						blocked = true;
//				}
//			}
//			
//				
//				if((pieces[newRow][newCol]==null || pieces[newRow][newCol].getColor()!=getColor()) && (newRow==getRow() || newCol==getCol()) && blocked==false &&this.ownInCheck(board)==false && board.isInbound(newRow,newCol))
//					move = true;
//				return move;
//		}
//		if(bishop){
//			if(pieces[newRow][newCol]!=null&&pieces[newRow][newCol].getColor()!=getColor())
//				attacking=true;
//				
//			if(newRow<getRow()&&newCol>getCol())
//				while(r!=newRow&&c!=newCol){
//					r--;
//					c++;
//					if(pieces[r][c]!=null)
//						blocked=true;
//				}
//			if(newRow>getRow()&&newCol>getCol())
//				while(r!=newRow&&c!=newCol){
//					r++;
//					c++;
//					if(pieces[r][c]!=null)
//						blocked = true;
//				}
//			if(newRow>getRow()&&newCol<getCol())
//				while(r!=newRow&&c!=newCol){
//					r++;
//					c--;
//					if(pieces[r][c]!=null)
//						blocked = true;
//				}
//			if(newRow<getRow()&&newCol<getCol())
//				while(r!=newRow&&c!=newCol){
//					r--;
//					c--;
//					if(pieces[r][c]!=null)
//						blocked = true;
//				}
//			
//			if(board.isInbound(newRow,newCol)&&(pieces[newRow][newCol]==null || attacking) && (((newRow - getRow())/ (newCol-getCol()))==1 || (newRow - getRow())/ (newCol-getCol())==-1)&&blocked==false&&(ownInCheck(board)==false||willBlockCheck(board,newRow,newCol,this)))
//				move=true;
//		}
//			
//			
//	return move;
 */