package ChessGameKenai.model;

import java.awt.Point;
import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public class Piece implements PieceInterface {

	public ArrayList<NonVisualPiece> pieces;
	protected final int MAX_STEP = 8;
	private static final int MAX_BLOCKS = 8;

	@Override
	public boolean isMoveable(ArrayList<NonVisualPiece> _activePieces, int pos,
			int des) {
		return false;
	}

	@Override
	public void makeMove(NonVisualPiece p, Chess_Data chessData, int pos,
			int des) {

		if (chessData.getPieceAtPosition(des - 1) != null) {
			chessData.getPieceAtPosition(des - 1).isCaptured(true);
			chessData.getCapturedPieces().add(
					chessData.getPieceAtPosition(des - 1));
			chessData.setPieceAtPosition(null, des - 1);
		}

		chessData.setPieceAtPosition(p, des - 1);
		chessData.setPieceAtPosition(null, pos - 1);
		p.setPosition(des);
		p.setClickCount(0);
		p.setPreviousPosition(pos);
	}

	public boolean isKill(NonVisualPiece p, Chess_Data chessData, int pos,
			int des) {
		NonVisualPiece nvp = chessData.getPieceAtPosition(des - 1);
		if (nvp != null) {
			if (nvp.getColor() != p.getColor()) {
				return true;
			}
		}

		return false;
	}

	protected boolean isDesAvailable(NonVisualPiece piece,
			NonVisualPiece desPiece) {

		if (desPiece == null)
			return true;
		else if (desPiece.getColor() != piece.getColor())
			return true;

		return false;

	}

	protected Point pointAt(int pos) {
		int posNew = pos - 1;
		return new Point(posNew % MAX_BLOCKS, posNew / MAX_BLOCKS);
	}

	protected int indexAt(Point point) {
		return ((point.y * MAX_BLOCKS) + point.x) + 1;
	}

	protected boolean isUpwardPathFree(ArrayList<NonVisualPiece> _activePieces,
			int stepsAllowed, int pos, int des) {
		return isUpwardPathFree(false, _activePieces, stepsAllowed, pos, des);
	}

	protected boolean isUpwardPathFree(boolean isPawn,
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x != desPoint.x)
			return false;
		if (posPoint.y <= desPoint.y)
			return false;

		int stepsDiff = (posPoint.y - desPoint.y);
		if (stepsDiff > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiff < stepsAllowed) ? stepsDiff : stepsAllowed;

		int init = 1;
		if (isPawn) // Pawn can't kill in straight move
			init = 0;
		for (int i = init; i < stepsToMove; i++) {
			itPoint.y--;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isDownwardPathFree(
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {
		return isDownwardPathFree(false, _activePieces, stepsAllowed, pos, des);
	}

	protected boolean isDownwardPathFree(boolean isPawn,
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x != desPoint.x)
			return false;
		if (posPoint.y >= desPoint.y)
			return false;

		int stepsDiff = (desPoint.y - posPoint.y);
		if (stepsDiff > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiff < stepsAllowed) ? stepsDiff : stepsAllowed;

		int init = 1;
		if (isPawn) // Pawn can't kill in straight move
			init = 0;
		for (int i = init; i < stepsToMove; i++) {
			itPoint.y++;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isRightPathFree(ArrayList<NonVisualPiece> _activePieces,
			int stepsAllowed, int pos, int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.y != desPoint.y)
			return false;
		if (posPoint.x >= desPoint.x)
			return false;

		int stepsDiff = (desPoint.x - posPoint.x);
		if (stepsDiff > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiff < stepsAllowed) ? stepsDiff : stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x++;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isLeftPathFree(ArrayList<NonVisualPiece> _activePieces,
			int stepsAllowed, int pos, int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.y != desPoint.y)
			return false;
		if (posPoint.x <= desPoint.x)
			return false;

		int stepsDiff = (posPoint.x - desPoint.x);
		if (stepsDiff > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiff < stepsAllowed) ? stepsDiff : stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x--;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isUpRightPathFree(
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x >= desPoint.x)
			return false;
		if (posPoint.y <= desPoint.y)
			return false;

		int stepsDiffX = (desPoint.x - posPoint.x);
		int stepsDiffY = (posPoint.y - desPoint.y);
		if (stepsDiffX != stepsDiffY)
			return false;
		if (stepsDiffX > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiffX < stepsAllowed) ? stepsDiffX
				: stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x++;
			itPoint.y--;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isDownRightPathFree(
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {
		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x >= desPoint.x)
			return false;
		if (posPoint.y >= desPoint.y)
			return false;

		int stepsDiffX = (desPoint.x - posPoint.x);
		int stepsDiffY = (desPoint.y - posPoint.y);
		if (stepsDiffX != stepsDiffY)
			return false;
		if (stepsDiffX > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiffX < stepsAllowed) ? stepsDiffX
				: stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x++;
			itPoint.y++;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isUpLeftPathFree(ArrayList<NonVisualPiece> _activePieces,
			int stepsAllowed, int pos, int des) {

		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x <= desPoint.x)
			return false;
		if (posPoint.y <= desPoint.y)
			return false;

		int stepsDiffX = (posPoint.x - desPoint.x);
		int stepsDiffY = (posPoint.y - desPoint.y);
		if (stepsDiffX != stepsDiffY)
			return false;
		if (stepsDiffX > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiffX < stepsAllowed) ? stepsDiffX
				: stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x--;
			itPoint.y--;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}

	protected boolean isDownLeftPathFree(
			ArrayList<NonVisualPiece> _activePieces, int stepsAllowed, int pos,
			int des) {
		Point posPoint = pointAt(pos);
		Point itPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		if (posPoint.x <= desPoint.x)
			return false;
		if (posPoint.y >= desPoint.y)
			return false;

		int stepsDiffX = (posPoint.x - desPoint.x);
		int stepsDiffY = (desPoint.y - posPoint.y);
		if (stepsDiffX != stepsDiffY)
			return false;
		if (stepsDiffX > stepsAllowed)
			return false;

		int stepsToMove = (stepsDiffX < stepsAllowed) ? stepsDiffX
				: stepsAllowed;

		for (int i = 1; i < stepsToMove; i++) {
			itPoint.x--;
			itPoint.y++;
			int itDes = indexAt(itPoint);
			if (_activePieces.get(itDes - 1) != null)
				return false;
		}

		return true;
	}
}
