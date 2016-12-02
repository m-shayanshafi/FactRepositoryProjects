package Chess;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public abstract class ChessPiece {
	private int row;
	private int col;
	public int color;
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	protected boolean isCapturing = false;
	private Image wk = null;
	private Image bk = null;
	private Image wr = null;
	private Image br = null;
	private Image wp = null;
	private Image bp = null;
	private Image wq = null;
	private Image bq = null;
	private Image wb = null;
	private Image bb = null;
	private Image wn = null;
	private Image bn = null;
	public ChessPiece(int c){
		//0 is white
		//1 is black
		color=c;
		initImages();
	}


	public int getRow(){return row;}
	public int getCol(){return col;}
	public int getColor(){return color;}
	public void setRow(int r){row = r;}
	public void setCol(int c){col = c;}
	public void setColor(int co){color = co;}
	/*
	 * checks if king is in check
	 */
	public boolean ownInCheck(ChessBoard board){
		ChessPiece[][] pieces = board.getPieces();
		boolean ownInCheck = false;
		for(int i = 0; i<8; i++){
			for(int j =0; j<8; j++){
				if(pieces[i][j]!=null&&pieces[i][j].getColor()==getColor())
					if(pieces[i][j] instanceof King){
						
					}
//						if(((King)pieces[i][j]).isInCheck(board))
//							ownInCheck = true;
			}
		}
		return ownInCheck;
	}

	public boolean willBlockCheck(ChessBoard board,int newRow, int newCol,ChessPiece piece){
		int r = piece.getRow();
		int c = piece.getCol();
		boolean willBlockCheck = false;
		if(ownInCheck(board)){
			board.addPiece(newRow,newCol,piece);
			if(ownInCheck(board)==false)
				willBlockCheck=true;
			board.remove(newRow,newCol);
			board.addPiece(r, c, piece);
		}
		return willBlockCheck;
	}
	public boolean equals(ChessPiece p2){
		boolean equals = false;
		if(this.getRow()==p2.getRow()&&this.getCol()==p2.getCol()&&this.getColor()==p2.getColor())
			equals = true;
		return equals;
	}
	/*
	 * will return an image representation of this ChessPiece
	 */
	public Image getImage(){

		// if a king
		if(this instanceof King){
			
			//	white
			if(getColor() == 0)
				return wk;
			//	black
			if(getColor() == 1)
				return bk;
		}
		// if a rook
		if(this instanceof Rook){
		
			//	white
			if(getColor() == 0)
				return wr;
			//	black
			if(getColor() == 1)
				return br;
		}
		// if a pawn
		if( this instanceof Pawn){
			//	white
			if(getColor() == 0)
				return wp;
			//	black
			if(getColor() == 1)
				return bp;
		}
		// if a queen
		if(this instanceof Queen){
			
			//	white
			if(getColor() == 0)
				return wq;
			//	black
			if(getColor() == 1)
				return bq;
		}
		//	if a bishop
		if(this instanceof Bishop){
			//	white
			if(getColor() == 0)
				return wb;
			//	black
			if(getColor() == 1)
				return bb;
		}
		//	if a nite
		if(this instanceof Nite){
			//	white
			if(getColor() == 0)
				return wn;
			//	black
			if(getColor() == 1)
				return bn;
		}
		return null;
			
	}
	
	public abstract boolean canMove(ChessBoard board, int newRow, int newCol);

	public void initImages(){
		// ------------------------ KING --------------------------//
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream input = cl.getResourceAsStream("White_King.png");
		
		try {
			wk = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_King.png");
		
		try {
			bk = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------ ROOK --------------------------//
		input = cl.getResourceAsStream("White_Rook.png");
		
		try {
			wr = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_Rook.png");
		
		try {
			br = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------ PAWN --------------------------//
		input = cl.getResourceAsStream("White_Pawn.png");
		
		try {
			wp = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_Pawn.png");
		
		try {
			bp = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------ QUEEN --------------------------//
		input = cl.getResourceAsStream("White_Queen.png");
		
		try {
			wq = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_Queen.png");
		
		try {
			bq = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------ BISHOP --------------------------//
		input = cl.getResourceAsStream("White_Bishop.png");
		
		try {
			wb = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_Bishop.png");
		
		try {
			bb = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		// ------------------------ NITE --------------------------//
		input = cl.getResourceAsStream("White_Nite.png");

		try {
			wn = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = cl.getResourceAsStream("Black_Nite.png");

		try {
			bn = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}