package Chess;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Pawn extends ChessPiece{
	private int moveCount = 0;
	private int value = 1;
	public Pawn(int c){
		super(c);
	}

	public int getMoveCount(){return moveCount;}
	public int getValue(){return value;}
	public void setValue(int s){value = s;}
	public boolean canMove(ChessBoard board, int newRow, int newCol){
		ChessPiece [][]pieces = board.getPieces();

		boolean move = false;
		if((newRow==getRow()&&newCol==getCol())||board.isInbound(newRow,newCol)==false){
			return move;
		}

		if(getColor()==0){
			if(moveCount>0){
				if(pieces[newRow][newCol]==null && newRow==getRow()-1 && newCol==getCol())
					move=

					true;
			}

			if(moveCount==0) {
				if((newRow==getRow()-2 || newRow==getRow()-1) && newCol==getCol() && pieces[newRow][newCol]==null){
					move = true;
					if(newRow==getRow()-2){
						if(board.getOccupant(newRow-1,newCol)!=null)
							move= false;
					}

				}

			}

			if((newCol==getCol()-1 || newCol==getCol()+1)&&newRow==getRow()-1 &&(pieces[newRow][newCol]!=null&& pieces[newRow][newCol].getColor()!=getColor()))
				move = true;
		}

		if(getColor()==1){
			if(moveCount>0){
				if(pieces[newRow][newCol]==null && newRow==getRow()+1 && newCol==getCol())
					move= true;
			}

			if(moveCount==0){
				if((newRow==getRow()+2 || newRow==getRow()+1 )&& newCol==getCol() && pieces[newRow][newCol]==null){
					move = true;
					if(newRow==getRow()+2){
						if(board.getOccupant(newRow+1,newCol)!=null)
							move= false;
					}

				}

			}

			if((newCol==getCol()-1 || newCol==getCol()+1)&&newRow==getRow()+1 &&(pieces[newRow][newCol]!=null&& pieces[newRow][newCol].getColor()!=getColor()))
				move =

				true;
		}

		if((move||enPoissant(board,newRow,newCol))&&stillInCheck(board,newRow,newCol)==false)
			move =

			true;
		else move = false;
		return move;
	}
	public boolean stillInCheck(ChessBoard board, int newRow, int newCol){
		return false;
	}
	public boolean isAttacking(ChessBoard board, int row, int col){
		ChessPiece [][]pieces = board.getPieces();

		boolean attacking = false;
		if((row==getRow()&&col==getCol())||board.isInbound(row,col)==false)
			return attacking;
		if(getColor()==0){
			if((col==getCol()-1 || col==getCol()+1)&&row==getRow()-1 &&(pieces[row][col]==null ||(pieces[row][col]!=null&& pieces[row][col].getColor()!=getColor())))
				attacking =

				true;
		}

		if(getColor()==1){
			if(((col==getCol()-1 || col==getCol()+1)&&row==getRow()+1) && (pieces[row][col]==null ||(pieces[row][col]!=null&& pieces[row][col].getColor()!=getColor()))){
				attacking =

						true;
			}

		}

		return attacking;
	}

	public boolean isDefending(ChessBoard board, int row, int col){
		boolean defending = false;
		if((row==getRow()&&col==getCol())||board.isInbound(row,col)==false)
			return defending;
		if(getColor()==0){
			if((col==getCol()-1 || col==getCol()+1)&&row==getRow()-1)
				defending =

				true;
		}

		if(getColor()==1){
			if((col==getCol()-1 || col==getCol()+1)&&row==getRow()+1){
				defending =

						true;
			}

		}

		return defending;
	}

	public boolean enPoissant(ChessBoard board, int newRow, int newCol){
		boolean enPoissant = false;
		int row = getRow();
		int col = getCol();
		if(getColor()==0){
			if(newRow==row-1&&(newCol==col-1 || newCol == col+1)){
				if(board.getOccupant(newRow,newCol)==null){
					if(board.getOccupant(newRow+1,newCol) instanceof Pawn){
						if(((Pawn)board.getOccupant(newRow+1, newCol)).getMoveCount()==1&&board.getOccupant(newRow+1, newCol).getColor()==1){
							if(stillInCheck(board,newRow,newCol)==false&&newRow==2)
								enPoissant =

								true;
						}

					}

				}

			}

		}

		else if(getColor()==1){
			if(newRow==row+1&&(newCol==col+1 || newCol==col-1)){
				if(board.getOccupant(newRow,newCol)==null){
					if(board.getOccupant(newRow-1, newCol) instanceof Pawn){
						if(((Pawn)board.getOccupant(newRow-1,newCol)).getMoveCount()==1&&board.getOccupant(newRow-1, newCol).getColor()==0){
							if(stillInCheck(board,newRow,newCol)==false&&newRow==5){
								enPoissant=true;
							}

						}

					}

				}

			}

		}

		return enPoissant;
	}

	public boolean isPromoting(ChessBoard board){
		boolean promotion = false;
		if(this.getColor()==0 && this.getRow()==0)
			promotion =

			true;
		if(this.getColor()==1 && this.getRow()==7)
			promotion =

			true;
		return promotion;
	}

	public void moveTo(ChessBoard board, int newRow, int newCol){
		if(canMove(board, newRow , newCol)){
			if(enPoissant(board,newRow,newCol)){
				if(getColor()==0){
					board.remove(newRow+1,newCol);

					board.remove(getRow(),getCol());

					setRow(newRow);

					setCol(newCol);

					board.setPiece(newRow,newCol,

							this);
					moveCount++;
				}

				else if(getColor()==1){
					board.remove(newRow-1,newCol);

					board.remove(getRow(),getCol());

					setRow(newRow);

					setCol(newCol);

					board.setPiece(newRow,newCol,

							this);
					moveCount++;
				}

			}

			else{
				board.remove(getRow(),getCol());

				setRow(newRow);

				setCol(newCol);

				board.setPiece(newRow,newCol,

						this);
			}

			moveCount++;
			if(isPromoting(board)){
				JFrame frame = new JFrame();
				Object [] options = {

						"Queen", "Knight"};
				int ans = JOptionPane.showOptionDialog(frame,
						"Which piece would you like?", "Promotion",
						JOptionPane.

						YES_NO_OPTION, JOptionPane.DEFAULT_OPTION,
						null, options, options[0]);
				if(ans==JOptionPane.YES_OPTION){
					board.remove(newRow,newCol);

					Queen queen =

							new Queen(getColor());
					board.addPiece(newRow,newCol,queen);

				}

				if(ans==JOptionPane.NO_OPTION){
					board.remove(newRow,newCol);

					Nite nite =

							new Nite(getColor());
					board.addPiece(newRow, newCol, nite);

				}

			}

		}

		else
			System.out.println("Invalid Move");
	}

	public String toString(){
		String s =

				"";
		if(getColor()==0)
			s =

			"p";
		else
			s =

			"P";
		return s;
	}

}