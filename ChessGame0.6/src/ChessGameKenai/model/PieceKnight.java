package ChessGameKenai.model;

import java.awt.Point;
import java.util.ArrayList;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;

public class PieceKnight extends Piece implements PieceInterface {

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

		Point posPoint = pointAt(pos);
		Point desPoint = pointAt(des);

		int xDelta = Math.abs(posPoint.x - desPoint.x);
		int yDelta = Math.abs(posPoint.y - desPoint.y);

		if (xDelta == 2 && yDelta == 1)
			return true;
		else if (xDelta == 1 && yDelta == 2)
			return true;

		return false;

	}
}
