package net.sf.bloodball.view.event;

import java.awt.Point;
import java.awt.event.*;
import net.sf.bloodball.view.GameBoard;

public class SquareHandler extends MouseHandler {
	private Point square;

	public SquareHandler(Point square, GameBoard gameBoard) {
		super(gameBoard);
		this.square = square;
	}

	public void actionPerformed(ActionEvent e) {
		gameBoard.getGameBoardListener().leftClick(square);
	}

}