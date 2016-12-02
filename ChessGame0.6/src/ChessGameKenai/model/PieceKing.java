package ChessGameKenai.model;

import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public class PieceKing extends Piece implements PieceInterface {

	protected final int MAX_STEP = 1;
	protected final int CASTLING_STEP_RIGHT = 3;
	protected final int CASTLING_STEP_LEFT = 4;

	@Override
	public void makeMove(NonVisualPiece piece, Chess_Data chessData, int pos,
			int des) {

		ArrayList<NonVisualPiece> _activePieces = chessData.getActivePieces();
		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (castlingClause(piece, desPiece)) {
			if (isRightPathFree(_activePieces, CASTLING_STEP_RIGHT, pos,
					des)) {

				makeCastleOnRight(piece, chessData, pos, des, desPiece);
				return;

			} else if (isLeftPathFree(_activePieces, CASTLING_STEP_LEFT,
					pos, des)) {

				makeCastleOnLeft(piece, chessData, pos, des, desPiece);
				return;
			}
		}

		super.makeMove(piece, chessData, pos, des);
	}

	private void makeCastleOnLeft(NonVisualPiece p, Chess_Data chessData,
			int pos, int des, NonVisualPiece desPiece) {
		int posNew = pos;
		int desNew = pos - 3;
		super.makeMove(p, chessData, posNew, desNew);

		posNew = des;
		desNew = des + 2;
		super.makeMove(desPiece, chessData, posNew, desNew);
	}

	private void makeCastleOnRight(NonVisualPiece p, Chess_Data chessData,
			int pos, int des, NonVisualPiece desPiece) {
		int posNew = pos;
		int desNew = pos + 2;
		super.makeMove(p, chessData, posNew, desNew);

		posNew = des;
		desNew = des - 2;
		super.makeMove(desPiece, chessData, posNew, desNew);
	}

	@Override
	public boolean isMoveable(ArrayList<NonVisualPiece> _activePieces, int pos,
			int des) {

		NonVisualPiece piece = _activePieces.get(pos - 1);
		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (castlingClause(piece, desPiece)) {
			if (isRightPathFree(_activePieces, CASTLING_STEP_RIGHT,
					pos, des))
				return true;
			else if (isLeftPathFree(_activePieces, CASTLING_STEP_LEFT,
					pos, des))
				return true;
		}

		if (!super.isDesAvailable(piece, desPiece))
			return false;

		if (isUpwardPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isDownwardPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isRightPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
		else if (isLeftPathFree(_activePieces, MAX_STEP, pos, des))
			return true;
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

	private boolean castlingClause(NonVisualPiece piece, NonVisualPiece desPiece) {

		if (desPiece == null)
			return false;

		return canRookCastle(desPiece) && canKingCastle(piece, desPiece);
	}

	private boolean canRookCastle(NonVisualPiece desPiece) {
		return CPiece.isRook(desPiece) && !desPiece.isMoved();
	}

	private boolean canKingCastle(NonVisualPiece piece, NonVisualPiece desPiece) {
		return !piece.isMoved() && piece.getColor() == desPiece.getColor();
	}
}
