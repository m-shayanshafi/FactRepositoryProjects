package tictactoe.controller;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import tictactoe.model.TicTacToe;
import tictactoe.view.TicTacToeView;
import common.controller.GameBoardController;
import common.model.Position;
import common.model.Random;
import common.model.GameBoard.Cell;

/**
 * Classe controleur du Morpion
 * @see GameBoardController.java
 * @author LETOURNEUR Léo
 *
 */
public class TicTacToeController extends GameBoardController{
	
	public TicTacToeController(TicTacToe modele, TicTacToeView vue) {
		super(modele,vue);
		
		this.vue = (TicTacToeView)vue;
		this.modele = (TicTacToe)modele;
		
		for (int row = 0; row < modele.WIDTH; row++)
		      for (int col = 0; col < modele.LENGTH; col++)
		    	  this.vue.casesLabel[row][col].addActionListener(this);
    }
	
	public void computerPlay()
	{
		coef = 0;
		
		for(int ct =0; ct < 3; ct ++)
		{
			checkLine(ct, modele.nextPlayer());
			checkColumn(ct, modele.nextPlayer());
		}
		checkDiagonalToBottom(1,1, modele.nextPlayer()); 
		checkDiagonalToTop(1,1, modele.nextPlayer());
		
		if(coef == 2 || coef ==-2) { 
			play(this.computerPosition.getX(),this.computerPosition.getY());
		} else {
			boolean hasPlay = false;
			while(!hasPlay && !modele.isFin())
			{
				Position posRandom = Random.playRandom(modele.WIDTH-1, modele.LENGTH-1);
				hasPlay = play(posRandom.getX(),posRandom.getY());
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vue.undoButton || e.getSource() == vue.exitButton 
		 || e.getSource() == vue.resetButton || e.getSource() == vue.loadButton
		 || e.getSource() == vue.saveButton) {
			
			super.actionPerformed(e);
		}
		else if(((JButton)e.getSource()).getActionCommand()!="")
		{
			if(((JButton)e.getSource()).getActionCommand().split(",").length != 2)
				return;
			int row = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[0]);
			int col = Integer.parseInt(((JButton)e.getSource()).getActionCommand().split(",")[1]);
			if ( e.getSource() == vue.casesLabel[row][col])
			{
				if(modele.getBoard()[row][col] == Cell.EMPTY.value)
				{
					super.actionPerformed(e);
					if(vue.aloneCheck.isSelected())
						computerPlay();
				}
			}
		}
	}
}
