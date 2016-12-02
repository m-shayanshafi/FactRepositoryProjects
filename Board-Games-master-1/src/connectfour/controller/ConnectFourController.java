package connectfour.controller;

import java.awt.event.ActionEvent;

import common.controller.GameBoardController;
import common.model.GameBoard.Cell;
import common.model.Position;
import common.model.Random;
import connectfour.model.ConnectFour;
import connectfour.view.ConnectFourView;

/**
 * Classe controleur du Puissance 4
 * @see GameBoardController.java
 * @author LETOURNEUR Léo
 *
 */
public class ConnectFourController extends GameBoardController {

	public ConnectFourController(ConnectFour modele, ConnectFourView vue) {
		super(modele, vue);

		this.vue = (ConnectFourView) vue;
		this.modele = (ConnectFour) modele;

		for (int i = 0; i < modele.LENGTH; i++)
			((ConnectFourView) this.vue).getCasesButton()[i].addActionListener(this);
		((ConnectFourView) this.vue).getNombreAAligner().addActionListener(this);
	}


	@Override
	public void computerPlay()
	{
		boolean hasPlay = false;
		while(!hasPlay && !modele.isFin())
		{
			Position posRandom = Random.playRandom(modele.WIDTH-1, modele.LENGTH-1);
			int newRow = ((ConnectFour) modele).getNextRow(posRandom.getY());
			posRandom.setX(newRow);
			if(posRandom.getX() != -1)
				hasPlay = play(posRandom.getX(),posRandom.getY());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == ((ConnectFourView)vue).getNombreAAligner()) {
			String nbAlignement = ((ConnectFourView)vue).getNombreAAligner().getSelectedItem().toString();
			modele.setNbCasesGagnante(Integer.parseInt(nbAlignement));
			
		} else if (e.getSource() == vue.undoButton || e.getSource() == vue.exitButton 
				 || e.getSource() == vue.resetButton || e.getSource() == vue.loadButton
				 || e.getSource() == vue.saveButton) {
			
			super.actionPerformed(e);
		}
		else
		{
			//Recherche du bouton source
			for (int col = 0; col < modele.LENGTH; col++) {
				if (e.getSource() == ((ConnectFourView) vue).getCasesButton()[col]) {
					
					int row = ((ConnectFour) modele).getNextRow(col);
					if(row!=-1)
					{
						//Modification du bouton de la grille
						//et non du bouton cliqué
						e.setSource(vue.casesLabel[row][col]);
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
	}
}
