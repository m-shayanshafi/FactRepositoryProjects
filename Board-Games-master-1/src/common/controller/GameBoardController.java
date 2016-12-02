package common.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import common.model.GameBoard;
import common.model.Position;
import common.model.Score;
import common.model.Turn;
import common.model.GameBoard.Cell;
import common.view.GameBoardView;

/**
 * Cette classe definie le l'interaction entre interface/utilisateur et la logique de jeu.
 * 
 * @author LETOURNEUR Léo
 *
 */
public class GameBoardController implements ActionListener {

	protected GameBoard modele;
	protected GameBoardView vue;
	
	public int coef;
	public Position computerPosition;

	public GameBoardController(GameBoard modele, GameBoardView vue) {

		this.modele = modele;
		this.vue = vue;

		this.vue.undoButton.addActionListener(this);
		this.vue.resetButton.addActionListener(this);
		this.vue.loadButton.addActionListener(this);
		this.vue.saveButton.addActionListener(this);
		this.vue.exitButton.addActionListener(this);

	}

	/**
	 * Methode qui verifie la victoire
	 * 
	 * @param Tableau de case à verifier et joueur.
	 * @return true si toute les cases = joueur, false sinon
	 */
	public boolean checkWin(Position[] cases, int player) {
		int compteur = 0;
		Position potentialPosition = new Position(0,0);
		for (Position cas : cases) {
			if (modele.getBoard()[cas.getX()][cas.getY()] == player)
				compteur++;
			else if (modele.getBoard()[cas.getX()][cas.getY()] != Cell.EMPTY.value)
				compteur--;
			else { 
				potentialPosition.setX(cas.getX());
				potentialPosition.setY(cas.getY());
			}
		}
		if(compteur==-2 || compteur==2) {
			this.coef = compteur;
			this.computerPosition = potentialPosition;
		}
		//Si toutes les cases appartiennent au même joueur
		if (compteur == cases.length) {
			ImageIcon iconWin = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/win" + player + ".png"));

			for (Position cas : cases)
				vue.casesLabel[cas.getX()][cas.getY()].setIcon(iconWin);
			vue.nextPlayer.setIcon(null);
			vue.nextPlayer.setHorizontalAlignment(JLabel.CENTER);
			vue.nextPlayer.setText("Click replay");
			
			vue.undoButton.setEnabled(false);
			modele.getScore().add(player, modele.getPointVictoire());
			vue.updateScore();
			
			if(!vue.aloneCheck.isSelected())
				modele.setPreviousFirstPlayer(modele.getHistory().get(0).getPlayer());
			modele.setFin(true);
			javax.swing.JOptionPane.showMessageDialog(vue, "Player " + player + " win !");
			return true;
		}
		return false;
	}

	/**
	 * Methode qui envoie la verification des lignes dans le checkwin()
	 * 
	 */
	public void checkLine(int line, int player) {
		int nbCases = modele.getNbCasesGagnante();
		Position[] cases = new Position[nbCases];
		//Vérification de toute les cases de la ligne
		for (int col = 0; col < modele.LENGTH - (nbCases - 1); col++) {
			for (int currentCase = 0; currentCase < nbCases; currentCase++) {
				cases[currentCase] = new Position(line, col + currentCase);
			}
			if(modele.isFin()) break;
			checkWin(cases, player);
		}
	}

	/**
	 * Methode qui envoie la verification des colonnes dans le checkwin()
	 * 
	 */
	public void checkColumn(int col, int player) {
		int nbCases = modele.getNbCasesGagnante();
		Position[] cases = new Position[nbCases];
		//Vérification de toute les cases de la colonne
		for (int line = 0; line < modele.WIDTH - (nbCases - 1); line++) {
			for (int currentCase = 0; currentCase < nbCases; currentCase++) {
				cases[currentCase] = new Position(line + currentCase, col);
			}
			if(modele.isFin()) break;
			checkWin(cases, player);
		}
	}

	/**
	 * Methode qui envoie la verification des diagonales vers le bas dans le checkwin()
	 * 
	 */
	public void checkDiagonalToBottom(int line, int column, int player) {

		int recule = line;
		int avanceMax = modele.WIDTH - 1 - line;
		if (recule > column)
			recule = column;
		if (avanceMax > modele.LENGTH - 1 - column)
			avanceMax = modele.LENGTH - 1 - column;

		if (recule > modele.getNbCasesGagnante() - 1)
			recule = modele.getNbCasesGagnante() - 1;
		if (avanceMax > modele.getNbCasesGagnante() - 1)
			avanceMax = modele.getNbCasesGagnante() - 1;

		//Nous connaissons maintenant le nombre de case en diagonale 
		//avant et après celle jouée
		
		int x = line;
		int y = column;

		for (int i = 0; i < recule; i++) {
			x--;
			y--;
		}

		int nbCases = modele.getNbCasesGagnante();
		Position[] cases = new Position[nbCases];
		for (int j = 0; j <= recule + avanceMax - (nbCases - 1); j++) {
			for (int currentCase = 0; currentCase < nbCases; currentCase++) {
				cases[currentCase] = new Position(x + j + currentCase, y + j + currentCase);
			}
			if(modele.isFin()) break;
			checkWin(cases, player);
		}
	}

	/**
	 * Methode qui envoie la verification des diagonales vers le haut dans le checkwin()
	 * 
	 */
	public void checkDiagonalToTop(int line, int column, int player) {

		int recule = modele.WIDTH - 1 - line;
		int avanceMax = line;
		if (recule > column)
			recule = column;
		if (avanceMax > modele.LENGTH - 1 - column)
			avanceMax = modele.LENGTH - 1 - column;

		if (recule > modele.getNbCasesGagnante() - 1)
			recule = modele.getNbCasesGagnante() - 1;
		if (avanceMax > modele.getNbCasesGagnante() - 1)
			avanceMax = modele.getNbCasesGagnante() - 1;
		
		//Nous connaissons maintenant le nombre de case en diagonale 
		//avant et après celle jouée
		int x = line;
		int y = column;

		for (int i = 0; i < recule; i++) {
			x++;
			y--;
		}
		int nbCases = modele.getNbCasesGagnante();
		Position[] cases = new Position[nbCases];
		for (int j = 0; j <= recule + avanceMax - (nbCases - 1); j++) {
			for (int currentCase = 0; currentCase < nbCases; currentCase++) {
				cases[currentCase] = new Position(x - j - currentCase, y + j + currentCase);
			}
			if(modele.isFin()) break;
			checkWin(cases, player);
		}
	}

	/**
	 * Methode qui reinitialise le modele
	 * 
	 */
	private void resetModel()
	{
		modele.setFin(false);
		modele.getHistory().clear();
		for (int row = 0; row < modele.WIDTH ; row++) {
		      for (int col = 0; col < modele.LENGTH; col++) {
		    	  modele.getBoard()[row][col] = Cell.EMPTY.value;
		      }
		}
		vue.resetGrid();
	}
	
	/**
	 * Methode qui joue sur la grille pour le joueur
	 * 
	 * @param Position de la case (int x, int y)
	 * @return true si la position est possible, false sinon
	 */
	public boolean play(int row, int col)
	{
		if(!modele.isFin() 
		&& modele.getBoard()[row][col]==Cell.EMPTY.value)
		{
			Position pos = new Position(row, col);
			Turn tour = new Turn(modele.nextPlayer(),pos);
			modele.play(tour);
			if (modele.lastPlayer() == Cell.PLAYER1.value)
			{
				vue.nextPlayer.setIcon(vue.iconNext2);
				vue.casesLabel[row][col].setIcon(vue.iconJoueur1);
			}
			else
			{
				vue.nextPlayer.setIcon(vue.iconNext1);
				vue.casesLabel[row][col].setIcon(vue.iconJoueur2);
			}
			
			checkLine(row, modele.lastPlayer());
			checkColumn(col, modele.lastPlayer());
			checkDiagonalToBottom(row,col, modele.lastPlayer());
			checkDiagonalToTop(row,col, modele.lastPlayer());
			
			if (modele.getHistory().size() == modele.LENGTH * modele.WIDTH && !modele.isFin())
			{
				vue.undoButton.setEnabled(false);
				JOptionPane.showMessageDialog(vue, "Tie !", "End", JOptionPane.OK_OPTION);
				modele.setFin(true);
				if(!vue.aloneCheck.isSelected())
					modele.setPreviousFirstPlayer(modele.getHistory().get(0).getPlayer());
			}
			
			return true;
		}
		return false;
	}

	/**
	 * Methode qui joue sur la grille aleatoirement pour l'ordinateur
	 * 
	 */
	public void computerPlay() {}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == vue.undoButton)
		{
			if (modele.getHistory().size() == 0)
				return;
			
			Position previousPos = modele.lastTurn().getPosition();
			vue.casesLabel[previousPos.getX()][previousPos.getY()].setIcon(vue.iconEmpty);
			modele.cancel();
			if(modele.lastPlayer() == Cell.PLAYER1.value)
				vue.nextPlayer.setIcon(vue.iconNext2);
			else
				vue.nextPlayer.setIcon(vue.iconNext1);
			
			if(vue.aloneCheck.isSelected())
			{
				Position previousPosComputer = modele.lastTurn().getPosition();
				vue.casesLabel[previousPosComputer.getX()][previousPosComputer.getY()].setIcon(vue.iconEmpty);
				modele.cancel();
			}
			
		} 
		else if (e.getSource() == vue.loadButton)
		{
			if (modele.isFin() || JOptionPane.showConfirmDialog(vue, 
				"Would you like to cancel this game to replay ?", "Cancel",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				resetModel();
				modele.setHistory(GameBoard.deserializeHistory(modele.getNomJeu()));
				
				if (modele.getHistory().isEmpty())
				{
					JOptionPane.showMessageDialog(vue, "No saved game");
					return;
				}
				
				vue.resetGrid();
				
				for (int i = 0; i < modele.getHistory().size(); i++) {
					Position currentPos = modele.getHistory().get(i).getPosition();
					if (modele.getHistory().get(i).getPlayer() == Cell.PLAYER1.value)
					{
						vue.casesLabel[currentPos.getX()][currentPos.getY()].setIcon(vue.iconJoueur1);
						modele.getBoard()[currentPos.getX()][currentPos.getY()] = Cell.PLAYER1.value;
					}
					else
					{
						vue.casesLabel[currentPos.getX()][currentPos.getY()].setIcon(vue.iconJoueur2);
						modele.getBoard()[currentPos.getX()][currentPos.getY()] = Cell.PLAYER2.value;
					}
				}
			}
		} else if (e.getSource() == vue.resetButton)
		{
			if (modele.isFin() || JOptionPane.showConfirmDialog(vue, 
				"Would you like to cancel this game to replay ?", "Cancel",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
			{	
				resetModel();
			}
		}
		else if (e.getSource() == vue.saveButton)
		{
			if(!modele.isFin())
			{
				boolean saved = GameBoard.serializeHistory(modele.getHistory(), modele.getNomJeu());
				if(saved)
					JOptionPane.showMessageDialog(vue, "Game saved !");
				else
					JOptionPane.showMessageDialog(vue, "Error during backup");
			}
			else
				JOptionPane.showMessageDialog(vue, "Can't save a completed game");
		}
		else if (e.getSource() == vue.exitButton) {
			Score.serializeScore(modele.getScore());
			vue.dispose();
		}
		else if(((JButton)e.getSource()).getActionCommand()!="")
		{
			if(((JButton)e.getSource()).getActionCommand().split(",").length != 2)
				return;
			int row = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[0]);
			int col = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[1]);
		
			if (e.getSource() == vue.casesLabel[row][col])
				play(row, col);
			
		}
	}
}
