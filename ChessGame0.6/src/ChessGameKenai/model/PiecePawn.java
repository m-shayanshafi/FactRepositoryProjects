package ChessGameKenai.model;

import java.awt.Point;
import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public class PiecePawn extends Piece implements PieceInterface {

	@Override
	public void makeMove(NonVisualPiece piece, Chess_Data chessData, int pos,
			int des) {

		
		if (isReachedToBoarder(des)) {
			becomeQueen(piece, chessData, pos, des);
			return;
		}
		
		if (piece.isWhite()) {

			if (killEnemyOfWhite(piece, chessData, pos, des))
				return;
			else
				super.makeMove(piece, chessData, pos, des);

		} else {
			if (killEnemyOfBlack(piece, chessData, pos, des))
				return;
			else
				super.makeMove(piece, chessData, pos, des);
		}

	}

	private boolean isReachedToBoarder(int des) {
		Point point = pointAt(des);
		return point.y == 0 || point.y == 7;
	}

	private void becomeQueen(NonVisualPiece piece, Chess_Data chessData,
			int pos, int des) {
		ArrayList<NonVisualPiece> activePieces = chessData
				.getActivePieces();
		ArrayList<NonVisualPiece> capturedPieces = chessData
				.getCapturedPieces();

		if (activePieces.get(des - 1) != null) {
			activePieces.get(des - 1).isCaptured(true);
			capturedPieces.add(activePieces.get(des - 1));
			activePieces.set((des - 1), null);
		}

		String pieceQueen = CPiece.BQueen;
		if (piece.isWhite())
			pieceQueen = CPiece.WQueen;
		
		activePieces.set((des - 1), NonVisualPiece.create(chessData,
				pieceQueen, des));
		activePieces.get(des - 1).setPreviousPosition(pos);
		activePieces.set((pos - 1), null);
		activePieces.get(des - 1).isQueenFromPawn(true);
		activePieces.remove(piece);
	}

	private boolean killEnemyOfBlack(NonVisualPiece piece,
			Chess_Data chessData, int pos, int des) {

		ArrayList<NonVisualPiece> _activePieces = chessData.getActivePieces();

		if (isBottomRightEnemy(_activePieces, pos, des)) {

			int posNew = pos;
			int desNew = pos + 9;
			super.makeMove(piece, chessData, posNew, desNew);

			return true;

		} else if (isBottomLeftEnemy(_activePieces, pos, des)) {

			int posNew = pos;
			int desNew = pos + 7;
			super.makeMove(piece, chessData, posNew, desNew);

			return true;
		}
		return false;
	}

	private boolean killEnemyOfWhite(NonVisualPiece piece,
			Chess_Data chessData, int pos, int des) {

		ArrayList<NonVisualPiece> _activePieces = chessData.getActivePieces();

		if (isTopRightEnemy(_activePieces, pos, des)) {

			int posNew = pos;
			int desNew = pos - 7;
			super.makeMove(piece, chessData, posNew, desNew);

			return true;

		} else if (isTopLeftEnemy(_activePieces, pos, des)) {

			int posNew = pos;
			int desNew = pos - 9;
			super.makeMove(piece, chessData, posNew, desNew);

			return true;
		}
		return false;
	}

	@Override
	public boolean isMoveable(ArrayList<NonVisualPiece> _activePieces, int pos,
			int des) {

		NonVisualPiece piece = _activePieces.get(pos - 1);

		if (piece.isWhite()) {
			return isWhiteMoveable(_activePieces, pos, des);
		} else {
			return isBlackMoveable(_activePieces, pos, des);
		}
	}

	private boolean isWhiteMoveable(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		NonVisualPiece piece = _activePieces.get(pos - 1);

		int steps = 1;
		if (!piece.isMoved())
			steps = 2;

		if (isUpwardPathFree(true, _activePieces, steps, pos, des))
			return true;
		else if (isTopRightEnemy(_activePieces, pos, des))
			return true;
		else if (isTopLeftEnemy(_activePieces, pos, des))
			return true;

		return false;
	}

	private boolean isBlackMoveable(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		NonVisualPiece piece = _activePieces.get(pos - 1);

		int steps = 1;
		if (!piece.isMoved())
			steps = 2;

		if (isDownwardPathFree(true, _activePieces, steps, pos, des))
			return true;
		else if (isBottomRightEnemy(_activePieces, pos, des))
			return true;
		else if (isBottomLeftEnemy(_activePieces, pos, des))
			return true;

		return false;
	}

	private boolean isTopRightEnemy(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		Point posPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (desPiece == null)
			return false;
		else if ((posPoint.x + 1) == desPoint.x
				&& (posPoint.y - 1) == desPoint.y && desPiece.isBlack())
			return true;

		return false;
	}

	private boolean isTopLeftEnemy(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		Point posPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (desPiece == null)
			return false;
		else if ((posPoint.x - 1) == desPoint.x
				&& (posPoint.y - 1) == desPoint.y && desPiece.isBlack())
			return true;

		return false;
	}

	private boolean isBottomRightEnemy(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		Point posPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (desPiece == null)
			return false;
		else if ((posPoint.x + 1) == desPoint.x
				&& (posPoint.y + 1) == desPoint.y && desPiece.isWhite())
			return true;

		return false;
	}

	private boolean isBottomLeftEnemy(ArrayList<NonVisualPiece> _activePieces,
			int pos, int des) {

		Point posPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		NonVisualPiece desPiece = _activePieces.get(des - 1);

		if (desPiece == null)
			return false;
		else if ((posPoint.x - 1) == desPoint.x
				&& (posPoint.y + 1) == desPoint.y && desPiece.isWhite())
			return true;

		return false;
	}
}
