package ChessGameKenai;

import java.util.ArrayList;

public class FlippedBoard extends Board {

	public FlippedBoard(Chess_Data data, ChessBoardView view) {

		this.setLayout(null);
		squares = new ArrayList<Square>();
		this.chessData = data;
		this.chessBoardView = view;
		this.setSquares();
		this.setOpaque(false);

		setPositions();

		setImageMap();

		this.populateBoard();
	}

	private void setImageMap() {
		imageMap.put("WKing", "ChessPieces/wKing46.gif");
		imageMap.put("BKing", "ChessPieces/bKing46.gif");
		imageMap.put("WQueen", "ChessPieces/wQueen46.gif");
		imageMap.put("BQueen", "ChessPieces/bQueen46.gif");
		imageMap.put("WBishop", "ChessPieces/wBishop46.gif");
		imageMap.put("BBishop", "ChessPieces/bBishop46.gif");
		imageMap.put("WKnight", "ChessPieces/wKnight46.gif");
		imageMap.put("BKnight", "ChessPieces/bKnight46.gif");
		imageMap.put("WRook", "ChessPieces/wRook46.gif");
		imageMap.put("BRook", "ChessPieces/bRook46.gif");
		imageMap.put("WPawn", "ChessPieces/wPawn46.gif");
		imageMap.put("BPawn", "ChessPieces/bPawn46.gif");
	}

	private void setPositions() {
		positions.add("a");
		positions.add("b");
		positions.add("c");
		positions.add("d");
		positions.add("e");
		positions.add("f");
		positions.add("g");
		positions.add("h");
	}

	@Override
	public void flipBoard() {
		for (int i = 0; i < squares.size(); i++) {
			squares.get(i).setBounds(
					(int) (455 - squares.get(i).getBounds().getX()),
					(int) (455 - squares.get(i).getBounds().getY()), 65, 65);
			squares.get(i).repaint();
			this.add(squares.get(i));
		}
		this.revalidate();
		this.repaint();
	}

}
