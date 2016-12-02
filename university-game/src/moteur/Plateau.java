// Thierrys

package moteur;

import pieces.*;
import cases.*;

public class Plateau {
	private static int line;
	private static int column;
	private Case [][] plateauCases;
	private Piece [][] plateauPieces;
	
	public Plateau(){
		line = column = 9;
		plateauCases = new Case[line][column];
		plateauPieces = new Piece[line][column];
		
		for(int i=0;i<line;i++)
		{
			for(int j=0;j<column;j++)
			{
				if (i == 0 || j == 0 || i == line - 1 || j == column - 1)
					plateauCases[i][j] = new CaseBord();
				else
					plateauCases[i][j] = new CaseVide();
				plateauPieces[i][j] = new PieceVide(i,j);
			}
		}
		plateauCases[4][4] = new CaseKonakis();
		
		plateauCases[0][0] = new CaseAngle();
		plateauCases[line-1][column-1] = new CaseAngle();
		plateauCases[0][column-1] = new CaseAngle();
		plateauCases[line-1][0] = new CaseAngle();
		
		plateauPieces[4][4] = new PieceRoi(4,4);
		
		plateauPieces[0][3] = new PieceMoscovite(0,3);
		plateauPieces[0][4] = new PieceMoscovite(0,4);
		plateauPieces[0][5] = new PieceMoscovite(0,5);
		plateauPieces[1][4] = new PieceMoscovite(1,4);
		
		plateauPieces[3][0] = new PieceMoscovite(3,0);
		plateauPieces[4][0] = new PieceMoscovite(4,0);
		plateauPieces[5][0] = new PieceMoscovite(5,0);
		plateauPieces[4][1] = new PieceMoscovite(4,1);
		
		plateauPieces[3][column-1] = new PieceMoscovite(3,column-1);
		plateauPieces[4][column-1] = new PieceMoscovite(4, column-1);
		plateauPieces[5][column-1] = new PieceMoscovite(5, column-1);
		plateauPieces[4][column-2] = new PieceMoscovite(4, column-2);
		
		plateauPieces[line-1][3] = new PieceMoscovite(line-1,3);
		plateauPieces[line-1][4] = new PieceMoscovite(line-1,4);
		plateauPieces[line-1][5] = new PieceMoscovite(line-1,5);
		plateauPieces[line-2][4] = new PieceMoscovite(line-2,4);

		plateauPieces[2][4] = new PieceSuedois(2,4);
		plateauPieces[3][4] = new PieceSuedois(3,4);
		plateauPieces[5][4] = new PieceSuedois(5,4);
		plateauPieces[6][4] = new PieceSuedois(6,4);
		plateauPieces[4][3] = new PieceSuedois(4,3);
		plateauPieces[4][5] = new PieceSuedois(4,5);
		plateauPieces[4][2] = new PieceSuedois(4,2);
		plateauPieces[4][6] = new PieceSuedois(4,6);
	}
	
	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	public Case getPlateauCases(int l, int c) {
		return plateauCases[l][c];
	}

	public void setPlateauCases(int l, int c, Case elt) {
		this.plateauCases[l][c] = elt;
	}
	
	public Piece getPlateauPieces(int l, int c) {
		return plateauPieces[l][c];
	}

	public void setPlateauPieces(int l, int c, Piece elt) {
		this.plateauPieces[l][c] = elt;
	}
	
	public Object clone() {
		Plateau p2 = new Plateau();
		for(int i=0;i<line;i++) {
			for(int j=0;j<column;j++) {
				p2.plateauCases[i][j] = this.plateauCases[i][j];
				p2.plateauPieces[i][j] = this.plateauPieces[i][j];
			}
		}
		return p2;
	}
}