package ChessGameKenai.model;

import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public class PieceBishop extends Piece implements PieceInterface {

	@Override
	public void makeMove(NonVisualPiece p, Chess_Data chessData, int pos,
			int des) {
		super.makeMove(p, chessData, pos, des);
	}

	@Override
	public boolean isMoveable(ArrayList<NonVisualPiece> _activePieces, int pos,
			int des) {

		NonVisualPiece piece = _activePieces.get(pos - 1);
		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (!super.isDesAvailable(piece, desPiece))
			return false;

		if (isUpRightPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isUpLeftPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isDownRightPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isDownLeftPathFree(_activePieces, MAX_STEP, pos, des))
			return true;

		return false;
	}
}
