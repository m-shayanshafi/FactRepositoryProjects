package Chess;


public class ChessBoard {

	public static final int col = 8;
	public static final int row = 8;
	private ChessPiece [][] board;
	private boolean moved = false;


	public ChessBoard(){
		board = new ChessPiece [row][col];
		for(int i = 0; i<row; i++){
			for(int j=0; j<col; j++){
				board [i][j]=null;
			}
		}
	}
	
	public ChessPiece[][] getPieces(){return board;}
	public boolean getMoved(){return moved;}
	public void setMoved(boolean b){moved = b;}


	public  boolean isInbound(int r, int c){
		boolean inbound = true;
		if(r>row-1 || c>col-1 || r<0 || c<0)
			inbound = false;
		return inbound;
	}

	public void fillWithDefaultPieces(){
		// white pawns
		Pawn p1 = new Pawn(0);
		Pawn p2 = new Pawn(0);
		Pawn p3 = new Pawn(0);
		Pawn p4 = new Pawn(0);
		Pawn p5 = new Pawn(0);
		Pawn p6 = new Pawn(0);
		Pawn p7 = new Pawn(0);
		Pawn p8 = new Pawn(0);
		// black pawns
		Pawn p9 = new Pawn(1);
		Pawn p10 = new Pawn(1);
		Pawn p11 = new Pawn(1);
		Pawn p12 = new Pawn(1);
		Pawn p13 = new Pawn(1);
		Pawn p14 = new Pawn(1);
		Pawn p15 = new Pawn(1);
		Pawn p16 = new Pawn(1);
		//	white nites
		Nite n1 = new Nite(0);
		Nite n2 = new Nite(0);
		//	black nites
		Nite n3 = new Nite(1);
		Nite n4 = new Nite(1);
		//	white rooks
		Rook r1 = new Rook(0);
		Rook r2 = new Rook(0);
		// black rooks
		Rook r3 = new Rook(1);
		Rook r4 = new Rook(1);
		//	white bishops
		Bishop b1 = new Bishop(0);
		Bishop b2 = new Bishop(0);
		//	black bishops
		Bishop b3 = new Bishop(1);
		Bishop b4 = new Bishop(1);
		//	white king
		King k1 = new King(0);
		//	black king
		King k2 = new King(1);
		//	white queen
		Queen q1 = new Queen(0);
		//	black queen
		Queen q2 = new Queen(1);
		// add white pawns
		addPiece(1,0,p9);
		addPiece(1,1,p10);
		addPiece(1,2,p11);
		addPiece(1,3,p12);
		addPiece(1,4,p13);
		addPiece(1,5,p14);
		addPiece(1,6,p15);
		addPiece(1,7,p16);
		//	add black pawn
		addPiece(6,0,p1);
		addPiece(6,1,p2);
		addPiece(6,2,p3);
		addPiece(6,3,p4);
		addPiece(6,4,p5);
		addPiece(6,5,p6);
		addPiece(6,6,p7);
		addPiece(6,7,p8);
		//	add white nites
		addPiece(7,1,n1);
		addPiece(7,6,n2);
		//	add black nites
		addPiece(0,1,n3);
		addPiece(0,6,n4);
		//	add white rooks
		addPiece(7,0,r1);
		addPiece(7,7,r2);
		//	add black rooks
		addPiece(0,0,r3);
		addPiece(0,7,r4);
		//	add white bishops
		addPiece(7,2,b1);
		addPiece(7,5,b2);
		//	add black bishops
		addPiece(0,2,b3);
		addPiece(0,5,b4);
		//	add white queen
		addPiece(7,3,q1);
		//	add black queen
		addPiece(0,3,q2);
		//	add white king
		addPiece(7,4,k1);
		//	add black king
		addPiece(0,4,k2);
	}


	public void remove(int r, int c){
		if(isInbound(r,c))
			if(board[r][c]!=null){
				board[r][c]=null;				
			}
				
	}

	public void setPiece(int r, int c, ChessPiece p){
		getPieces()[r][c]=p;
	}

	public void addPiece(int r, int c, ChessPiece piece){
		if(isInbound(r,c)&&board[r][c]==null){
			board[r][c]=piece;
			piece.setRow(r);
			piece.setCol(c);
		}
	}
	public boolean gameOver(){
		boolean gameOver=false;
		ChessPiece[][] pieces = getPieces();
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null)
					if(pieces[i][j] instanceof King)
						if(((King)pieces[i][j]).isCheckMate(this))
							gameOver = true;
			}
		}
		return gameOver;
	}

	public ChessPiece getOccupant(int row, int column){
		if(isInbound(row, column))
			return board[row][column];

		//if the specified location is not valid
		return null;
	}

	public void movePiece(int r, int c, int newRow, int newCol){
		ChessPiece[][] pieces = this.getPieces();
		int color = 2;
		moved = false;
		if(getOccupant(r,c)!=null)
			color = pieces[r][c].getColor();
		if(pieces[r][c]!=null){
			if(this.getOccupant(r, c) instanceof Pawn){
				System.out.println("Pawn moved from " + r + ", " + c + " to " + newRow + ", " + newCol);
				Pawn p = (Pawn)pieces[r][c];
				p.moveTo(this,newRow,newCol);
			}
			if(this.getOccupant(r, c) instanceof Nite){
				Nite n = (Nite)pieces[r][c];
				n.moveTo(this,newRow,newCol);
			}
			if(this.getOccupant(r, c) instanceof Rook){
				Rook rook = (Rook)pieces[r][c];
				rook.moveTo(this,newRow,newCol);
			}
			if(this.getOccupant(r, c) instanceof Bishop){
				Bishop b = (Bishop)pieces[r][c];
				b.moveTo(this,newRow,newCol);
			}
			if(this.getOccupant(r, c) instanceof King){
				King k = (King)pieces[r][c];
				System.out.println("moveTO: " + newRow + ", " + newCol);
				k.moveTo(this,newRow,newCol);
			}
			if(this.getOccupant(r, c) instanceof Queen){
				Queen q = (Queen)pieces[r][c];
				q.moveTo(this,newRow,newCol);
			}
		}
		if(getOccupant(newRow,newCol)!=null&&getOccupant(newRow,newCol).getColor()==color)
			setMoved(true);
	}

	public boolean onlyOne(){
		ChessPiece[][] pieces = this.getPieces();
		boolean one = true;
		int count =0;
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null)
					count++;
			}
		}
		if(count!=1)
			one=false;
		return one;
	}

	public int getOnlyOneRow(){
		ChessPiece[][] pieces = this.getPieces();
		int row = 0; 
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null)
					row = pieces[i][j].getRow();
			}
		}
		return row;
	}

	public int getOnlyOneCol(){
		ChessPiece[][] pieces = this.getPieces();
		int col = 0; 
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null)
					col = pieces[i][j].getCol();
			}
		}
		return col;
	}
	public String toString(){
		String b = "";
		String colNum = "  ";
		for(int j =0; j<col; j++){
			colNum +=j + " ";
		}
		colNum+="\n";
		for(int i=0; i<row; i++){
			b = b+i+"|";
			for(int j =0; j<col; j++){
				if(board[i][j]!=null)
					b = b + board[i][j];
				else
					b = b + " ";
				b = b + "|";
			}
			b = b + "\n";
		}
		b = b + "\n";
		b = colNum + b;
		return b;
	}

}


