package ChessGameKenai.model;

import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public interface PieceInterface {

	public boolean isMoveable(ArrayList<NonVisualPiece> _activePieces, int pos,
			int des);

	public void makeMove(NonVisualPiece p, Chess_Data chessData, int pos,
			int des);
}
