package kw.sudoku.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import kw.sudoku.controller.SudokuController;
import kw.sudoku.model.Game;
import kw.sudoku.model.Solution;
import kw.sudoku.model.UpdateAction;

/**
 * This class draws the sudoku panel and reacts to updates from the model.
 *
 * @author Ken Wu @ New York
 */
public class SudokuPanel extends JPanel implements Observer {
    // Color constant for candidates.
    private static final Color COLOR_CANDIDATE = new Color(102, 153, 255);
    private static final long serialVersionUID = 1L;
    private Field[][] m_fields;       // Array of fields.
    private JPanel[][] m_panels;      // Panels holding the fields.
    
    /**
     * Constructs the panel, adds sub panels and adds fields to these sub panels.
     */
    public SudokuPanel() {
        super(new GridLayout(3, 3));

        initComponents();
    }

    private void initComponents() {
    	
    	removeAll();
    	
    	m_panels = new JPanel[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
            	m_panels[y][x] = new JPanel(new GridLayout(3, 3));
            	m_panels[y][x].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                add(m_panels[y][x]);
            }
        }

        m_fields = new Field[9][9];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
            	m_fields[y][x] = new Field(x, y);
            	m_panels[y / 3][x / 3].add(m_fields[y][x]);
            }
        }
	}

	/**
     * Method called when model sends update notification.
     *
     * @param o     The model.
     * @param arg   The UpdateAction.
     */
    public void update(Observable o, Object arg) {
        switch ((UpdateAction)arg) {
            case NEW_GAME:
                setGame((Game)o);
                break;
            case CHECK:
                setGameCheck((Game)o);
                break;
            case SHOW_SOLUTIONS:
            	setGameSolution((Game)o);
                break;
            case SELECTED_NUMBER:
            case CANDIDATES:
            case HELP:
                setCandidates((Game)o);
                break;
        }
    }

    private void setGameSolution(Game g) {
		this.setGameSolution(g, 0, false);
	}
    
    public void setGameSolution(Game g, int index, boolean isThisAlternativeSolution) {
		Set<Solution> sols = g.getCalculatedSols();
		int i = 0;
		for(Solution s: sols) {
			if(i == index) {
				setGameSolution(s, isThisAlternativeSolution);
			}
			i++;
		}
		
	}

	/**
     * Sets the fields corresponding to given game.
     *
     * @param game  Game to be set.
     */
    public void setGame(Game game) {
    	
    	//initComponents();
    	
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                m_fields[y][x].setBackground(Color.WHITE);
                m_fields[y][x].setNumber(game.getNumber(x, y), false);
                m_fields[y][x].setThisInputByAutoSuggested(false);
            }
        }
    }
    
    /**
     * Sets the Nums sol
     * @param isThisAlternativeSolution 
     *
     * @param game  Game to be set.
     */
    public void setGameSolution(Solution solution, boolean isThisAlternativeSolution) {
    	int sol[][] = solution.getSolution();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
            	if(m_fields[y][x].getNumber() == 0 || (isThisAlternativeSolution && m_fields[y][x].isThisInputByAutoSuggested()) ) {
            		m_fields[y][x].setBackground(Color.RED);
            		m_fields[y][x].setNumber(sol[y][x], false);
            		m_fields[y][x].setThisInputByAutoSuggested(true);
            	}
            	
            }
        }
    }

	/**
     * Sets fields validity according to given game.
     *
     * @param game  Current game.
     */
    private void setGameCheck(Game game) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
            	m_fields[y][x].setBackground(Color.WHITE);
                if (m_fields[y][x].getForeground().equals(Color.BLUE))
                	m_fields[y][x].setBackground(game.isCheckValid(x, y) ? Color.GREEN : Color.RED);
            }
        }
    }

    /**
     * Shows the candidates according to given game.
     *
     * @param game  Current game.
     */
    private void setCandidates(Game game) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
            	m_fields[y][x].setBackground(Color.WHITE);
                if (game.isHelp() && game.isSelectedNumberCandidate(x, y))
                	m_fields[y][x].setBackground(COLOR_CANDIDATE);
            }
        }
    }

    /**
     * Adds controller to all sub panels.
     *
     * @param sudokuController  Controller which controls all user actions.
     */
    public void setController(SudokuController sudokuController) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++)
            	m_panels[y][x].addMouseListener(sudokuController);
        }
    }

}