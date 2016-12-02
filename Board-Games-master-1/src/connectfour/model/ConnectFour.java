package connectfour.model;

import java.util.List;

import common.model.GameBoard;
import common.model.Turn;

/**
 * Classe modele du Puissance 4
 * @see GameBoard.java
 * @author LETOURNEUR Léo
 *
 */
public class ConnectFour extends GameBoard {
	private static final long serialVersionUID = -2735399069176225698L;
	
	public ConnectFour(int width, int length) {
		super(width, length);
		nomJeu = "connectfour";
		nbCasesGagnante = 4;
		pointVictoire = 3;
	}

	public ConnectFour(int width, int length, List<Turn> history) {
		super(width, length, history);
		nomJeu = "connectfour";
		nbCasesGagnante = 4;
		pointVictoire = 3;
	}
	
	/**
	 * Methode qui permet de jouer avec la gravite
	 *
	 */
	public int getNextRow(int column) {
		int cleanRow = -1;
		for (int row = 0; row < WIDTH; row++) {
			if (getBoard()[row][column] == Cell.EMPTY.value) {
				cleanRow = row;
			}
		}
		return cleanRow;
	}
}
