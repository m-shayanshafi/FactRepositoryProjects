package net.sf.bloodball.view.event;

import java.awt.event.*;
import net.sf.bloodball.view.GameBoard;

public abstract class MouseHandler extends MouseAdapter implements ActionListener {
	protected GameBoard gameBoard;

	public MouseHandler(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
			gameBoard.getGameBoardListener().rightClick();
		}
	}
}