package teeko.model;

import java.util.List;

import common.model.GameBoard;
import common.model.Turn;

/**
 * Classe modele du Teeko
 * @see GameBoard.java
 * @author LETOURNEUR Léo
 *
 */
public class Teeko extends GameBoard {
	private static final long serialVersionUID = 1139098941636275928L;

	public Teeko(int width, int length) {
		super(width, length);
		nomJeu = "teeko";
		nbCasesGagnante = 4;
		pointVictoire = 5;
	}

	public Teeko(int width, int length, List<Turn> history) {
		super(width, length, history);
		nomJeu = "teeko";
		nbCasesGagnante = 4;
		pointVictoire = 5;
	}
}
