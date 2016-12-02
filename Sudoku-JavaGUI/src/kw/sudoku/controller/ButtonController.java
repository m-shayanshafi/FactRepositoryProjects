package kw.sudoku.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import kw.sudoku.model.Game;
import kw.sudoku.view.AboutDialog;
import kw.sudoku.view.Sudoku;

import javax.swing.JComboBox;

/**
 *
 * @author Ken Wu @ New York
 */
public class ButtonController implements ActionListener {
    private Game game;
    private Sudoku m_parentFrame;

    public ButtonController(Sudoku sudoku, Game game2) {
    	   this.game = game2;
    	   m_parentFrame = sudoku;
	}

	/**
     * Performs action after user pressed button.
     *
     * @param e ActionEvent.
     */
    public void actionPerformed(ActionEvent e) {
    	
    	if(e.getSource() instanceof JComboBox) {
    		//Other solution is clicked
    		m_parentFrame.getSudokuPanel().setGameSolution(this.game, ((JComboBox)e.getSource()).getSelectedIndex(), true);
    	} else if (e.getActionCommand().equals("New"))
            game.newGame();
        else if (e.getActionCommand().equals("Check"))
            game.checkGame();
        else if (e.getActionCommand().equals("Exit"))
            System.exit(0);
        
        else if (e.getActionCommand().equals("Save")) {
        	@SuppressWarnings("unused")
			AboutDialog dlg = new AboutDialog(m_parentFrame, "Unimplemented yet...", "It will be coming soon.");
        
        } else if (e.getActionCommand().equals("Undo")) {
        	@SuppressWarnings("unused")
			AboutDialog dlg = new AboutDialog(m_parentFrame, "Unimplemented yet...", "It will be coming soon.");
        } 
        else if (e.getActionCommand().equals("Solution")) {
        	//AboutDialog dlg = new AboutDialog(m_parentFrame, "Unimplemented yet...", "It will be coming soon.");
        	game.showSolution();
        } else if (e.getActionCommand().equals("About")) {
        	@SuppressWarnings("unused")
			AboutDialog dlg = new AboutDialog(m_parentFrame, AboutDialog.TITLE, AboutDialog.MESSAGE);
        } 

        else if (e.getActionCommand().equals("Level - Very Easy")) {
        	game.setGameLevel(Game.VERYEASY);
        	this.m_parentFrame.setTitle(game.getLevelString());
        }
        
        else if (e.getActionCommand().equals("Level - Easy")) {
        	game.setGameLevel(Game.EASY);
        	this.m_parentFrame.setTitle(game.getLevelString());
        }
        
		else if (e.getActionCommand().equals("Level - Medium")) {
			game.setGameLevel(Game.MEDIUM);    	
			this.m_parentFrame.setTitle(game.getLevelString());
		}
		        
		else if (e.getActionCommand().equals("Level - Hard")) {
			game.setGameLevel(Game.HARD);
			this.m_parentFrame.setTitle(game.getLevelString());
		}
        
		else if (e.getActionCommand().equals("Level - Very Hard")) {
			game.setGameLevel(Game.VERYHARD);
			this.m_parentFrame.setTitle(game.getLevelString());
		}
        
		else if (e.getActionCommand().equals("Level - Expert")) {
			game.setGameLevel(Game.EXPERT);
			this.m_parentFrame.setTitle(game.getLevelString());
		}
        
        else
            game.setSelectedNumber(Integer.parseInt(e.getActionCommand()));
    }
}