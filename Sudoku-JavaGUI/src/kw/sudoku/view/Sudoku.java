package kw.sudoku.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import kw.sudoku.controller.ButtonController;
import kw.sudoku.controller.SudokuController;
import kw.sudoku.model.Game;

/**
 * Main class of program.
 *
 * @author Ken Wu @ New York
 */
public class Sudoku extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String VERSION = "v1";
	public static final String TITLE = "Sudoku Game " + VERSION;
	
	private final SudokuPanel sudokuPanel;
	
    public SudokuPanel getSudokuPanel() {
		return sudokuPanel;
	}

    public void setTitle(String level) {
    	super.setTitle(TITLE + " - Game Level: " + level);
    }
    
	public Sudoku() {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        
        Game game = new Game();
        game.setGameLevel(Game.MEDIUM);
        setTitle(game.getLevelString());

        ButtonController buttonController = new ButtonController(this, game);
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setController(buttonController);
        add(buttonPanel, BorderLayout.SOUTH);

        sudokuPanel = new SudokuPanel();
        SudokuController sudokuController = new SudokuController(sudokuPanel, game);
        sudokuPanel.setGame(game);
        sudokuPanel.setController(sudokuController);
        add(sudokuPanel, BorderLayout.CENTER);

        //Build the Menu
        MenuPanel menubar = new MenuPanel();
        menubar.setController(buttonController);
        this.setJMenuBar(menubar);
        
        game.addObserver(buttonPanel);
        game.addObserver(sudokuPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        
        
    }

    /**
     * Main entry point of program.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Use System Look and Feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ex) { ex.printStackTrace(); }
        new Sudoku();
    }
}