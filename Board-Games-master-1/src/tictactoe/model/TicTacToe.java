package tictactoe.model;

import java.util.List;

import common.model.GameBoard;
import common.model.Turn;

/**
 * Classe modele du Morpion
 * @see GameBoard.java
 * @author LETOURNEUR Léo
 *
 */
public class TicTacToe extends GameBoard {
	private static final long serialVersionUID = -2476801123377458868L;

	public TicTacToe(int width, int length) {
		super(width, length);
		nomJeu = "tictactoe";
		nbCasesGagnante = 3;
		pointVictoire = 1;
	}

	public TicTacToe(int width, int length, List<Turn> history) {
		super(width, length, history);
		nomJeu = "tictactoe";
		nbCasesGagnante = 3;
	}
}
