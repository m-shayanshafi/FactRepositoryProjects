package Chess;


public class Nite extends ChessPiece{
	public Nite(int c){
		super(c);
	}

public boolean canMove(ChessBoard board, int newRow, int newCol){
	ChessPiece[][] pieces = board.getPieces();
	boolean move = false;
	if((newRow==getRow()&&newCol==getCol())||board.isInbound(newRow,newCol)==false)
		return move;
		if((pieces[newRow][newCol]==null || pieces[newRow][newCol].getColor()!=getColor()) && ((newRow==getRow()-2 && newCol==getCol()+1) || (newRow==getRow()-1 && newCol==getCol()+2) || (newRow==getRow()+1 && newCol==getCol()+2) || (newRow==getRow()+2 && newCol==getCol()+1) || (newRow==getRow()+2 && newCol==getCol()-1) || (newRow==getRow()+1 && newCol==getCol()-2) || (newRow==getRow()-1 && newCol==getCol()-2) || (newRow==getRow()-2 && newCol==getCol()-1))&&(ownInCheck(board)==false||willBlockCheck(board,newRow,newCol,this)))
			move = true;
	return move;
}

public void moveTo(ChessBoard board, int newRow, int newCol){
	if(canMove(board, newRow, newCol)){
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
		s = "n";
		else 
			s = "N";
			return s;
}
}
