package tictactoe.view;

import java.awt.Color;

import common.model.GameBoard;
import common.view.GameBoardView;

/**
 * Classe vue du Morpion
 * @see GameBoardView.java
 * @author LETOURNEUR Léo
 *
 */
public class TicTacToeView extends GameBoardView {
	private static final long serialVersionUID = 4727415632038426947L;

	public TicTacToeView(GameBoard game) {
		super(game);
		
		largeurCase = 150;
		
		loadIcons();
		loadPanels();
		loadMenu();
		setTitle("Tic Tac Toe");
		setVisible(true);
	}

	@Override
	public void loadPanels() {

		super.loadPanels();
		labelPane.setBackground(Color.white);
	}
}
