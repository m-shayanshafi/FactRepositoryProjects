package teeko.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;

import teeko.view.TeekoView;
import teeko.model.Teeko;
import common.controller.GameBoardController;
import common.model.Position;
import common.model.Turn;

/**
 * Classe controleur du Teeko
 * @see GameBoardController.java
 * @author LETOURNEUR Léo
 *
 */
public class TeekoController extends GameBoardController{
	
	private boolean caseSelected;
	private Position posCaseSelected;
	
	public TeekoController(Teeko modele, TeekoView vue) {
		super(modele,vue);
		
		caseSelected = false;
		
		this.vue = (TeekoView)vue;
		this.modele = (Teeko)modele;

		for (int row = 0; row < modele.WIDTH; row++) 
		      for (int col = 0; col < modele.LENGTH; col++)
		    	  this.vue.casesLabel[row][col].addActionListener(this);
	}
	
	/**
	 * Methode qui envoie la verification des carres dans le checkwin()
	 * 
	 */
	private void verificationCarre(int row, int col) {
		if(row>0)
		{
			if(col>0)
				checkWin(new Position[]{new Position(row,col),new Position(row-1,col),new Position(row-1,col-1),new Position(row,col-1)}, modele.lastPlayer());
			if(col<4)
				checkWin(new Position[]{new Position(row,col),new Position(row-1,col),new Position(row-1,col+1),new Position(row,col+1)}, modele.lastPlayer());
		}
		if(row<4)
		{
			if(col>0)
				checkWin(new Position[]{new Position(row,col),new Position(row+1,col),new Position(row+1,col-1),new Position(row,col-1)}, modele.lastPlayer());
			if(col<4)
				checkWin(new Position[]{new Position(row,col),new Position(row+1,col),new Position(row+1,col+1),new Position(row,col+1)}, modele.lastPlayer());
		}
	}

	/**
	 * Methode qui verifie si la case jouee est adjacente a la precedente.
	 * 
	 */
	private boolean clikableCase(int row, int col) {
		if(Math.abs(row-posCaseSelected.getX())<=1 && Math.abs(col-posCaseSelected.getY())<=1)
			return true;
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vue.undoButton || e.getSource() == vue.exitButton 
		 || e.getSource() == vue.resetButton || e.getSource() == vue.loadButton) {
			
			super.actionPerformed(e);
		}
		else if (e.getSource() == vue.saveButton) {
			//Copie des jetons en jeu et non de toute l'histoire
			ArrayList<Turn> newHistory = new ArrayList<Turn>();
			for (int i = 0; i < modele.WIDTH; i++)
			      for (int j = 0; j < modele.LENGTH; j++)
			    	  if(modele.getBoard()[i][j] != 0)
			    		  newHistory.add(new Turn(modele.getBoard()[i][j], new Position(i,j)));
			modele.setHistory(newHistory);
			
			super.actionPerformed(e);
		}
		else if(((JButton)e.getSource()).getActionCommand()!="")
		{
			if(((JButton)e.getSource()).getActionCommand().split(",").length != 2)
				return;
			int row = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[0]);
			int col = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[1]);
		
			if (e.getSource() == vue.casesLabel[row][col])
			{
				//Si les 8 premiers tours ont été joués
				if (modele.getHistory().size() >= 8)
				{
					vue.undoButton.setEnabled(false);
					//Si une case a été selectionnée
					if(caseSelected)
					{
						if(this.modele.getBoard()[row][col] == 0 && clikableCase(row,col))
						{
							this.vue.casesLabel[posCaseSelected.getX()][posCaseSelected.getY()].setIcon(vue.iconEmpty);
							this.modele.getBoard()[posCaseSelected.getX()][posCaseSelected.getY()] = 0;
							
							super.actionPerformed(e);
							
							//L'history peut être illimitée donc nous la maintenons à une taille 8
							//i=2 pour savoir qui a été le premier joueur de la partie
							for(int i = 2;i<modele.getHistory().size();i++)
								modele.getHistory().set(i-1, modele.getHistory().get(i));
							modele.getHistory().remove(modele.getHistory().size() - 1);
							verificationCarre(row, col);
							caseSelected = false;
						}
						else
							caseSelected = false;
					}
					else
					{
						if(this.modele.getBoard()[row][col] != 0
						&& this.modele.getBoard()[row][col] == modele.nextPlayer())
						{
							posCaseSelected = new Position(row,col);
							caseSelected = true;
						}
					}
				}
				else
				{
					if(this.modele.getBoard()[row][col] == 0)
					{
						super.actionPerformed(e);
						verificationCarre(row, col);
					}
				}
			}
		}
	}
}
