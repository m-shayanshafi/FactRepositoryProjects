package ChessGameKenai.model;

import java.awt.Color;

import ChessGameKenai.NonVisualPiece;

public class CPiece {

	public static final String BRook = "BRook";
	public static final String BKnight = "BKnight";
	public static final String BBishop = "BBishop";
	public static final String BQueen = "BQueen";
	public static final String BKing = "BKing";
	public static final String BPawn = "BPawn";

	public static final String WPawn = "WPawn";
	public static final String WRook = "WRook";
	public static final String WKnight = "WKnight";
	public static final String WBishop = "WBishop";
	public static final String WQueen = "WQueen";
	public static final String WKing = "WKing";

	public static final String Rook = "Rook";
	public static final String Knight = "Knight";
	public static final String Bishop = "Bishop";
	public static final String Queen = "Queen";
	public static final String King = "King";
	public static final String Pawn = "Pawn";

	public static Color getColor(String pieceColoredType) {
		if (pieceColoredType.charAt(0) == 'B')
			return Color.BLACK;
		return Color.WHITE;
	}

	public static boolean isBRookDefaultPosition(int position) {
		return isEqual(position, 0, 7);
	}

	public static boolean isBKnightDefaultPosition(int position) {
		return isEqual(position, 1, 6);
	}

	public static boolean isBBishopDefaultPosition(int position) {
		return isEqual(position, 2, 5);
	}

	public static boolean isBQueenDefaultPosition(int position) {
		return (position == 3);
	}

	public static boolean isBKingDefaultPosition(int position) {
		return (position == 4);
	}

	public static boolean isBPawnDefaultPosition(int position) {
		return isInRange(7, position, 16);
	}

	public static boolean isWRookDefaultPosition(int position) {
		return isEqual(position, 56, 63);
	}

	public static boolean isWKnightDefaultPosition(int position) {
		return isEqual(position, 57, 62);
	}

	public static boolean isWBishopDefaultPosition(int position) {
		return isEqual(position, 58, 61);
	}

	public static boolean isWQueenDefaultPosition(int position) {
		return (position == 59);
	}

	public static boolean isWKingDefaultPosition(int position) {
		return (position == 60);
	}

	public static boolean isWPawnDefaultPosition(int position) {
		return isInRange(47, position, 56);
	}

	public static boolean isEmptyDefaultPosition(int position) {
		return isInRange(15, position, 48);
	}

	public static boolean isKing(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(King);
	}

	public static boolean isQueen(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(Queen);
	}

	public static boolean isPawn(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(Pawn);
	}

	public static boolean isRook(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(Rook);
	}

	public static boolean isKnight(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(Knight);
	}

	public static boolean isBishop(NonVisualPiece piece) {
		return piece.getPieceTypeOnly().equals(Bishop);
	}

	private static boolean isInRange(int start, int value, int end) {
		return (value > start && value < end);
	}

	private static boolean isEqual(int value, int ifEqual1, int ifEqual2) {
		return ((value == ifEqual1) || (value == ifEqual2));
	}

}
