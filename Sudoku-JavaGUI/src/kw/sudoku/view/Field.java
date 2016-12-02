package kw.sudoku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * This class represents a field on the SudokuPanel.
 *
 * @author Ken Wu @ New York
 */
public class Field extends JLabel {
    private int x;      // X position in game.
    private int y;      // Y position in game.
    private int num;
    private static final long serialVersionUID = 1L;
    private boolean isThisInputByAutoSuggested = false;
    
    /**
     * Constructs the label and sets x and y positions in game.
     *
     * @param x     X position in game.
     * @param y     Y position in game.
     */
    public Field(int x, int y) {
        super("", CENTER);
        this.x = x;
        this.y = y;
        
        setPreferredSize(new Dimension(40, 40));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFont(new Font(Font.DIALOG, Font.TRUETYPE_FONT, 20));
        setOpaque(true);
    }

    /**
     * Sets number and foreground color according to userInput.
     *
     * @param number        Number to be set.
     * @param userInput     Boolean indicating number is user input or not.
     */
    public void setNumber(int number, boolean userInput) {
        setForeground(userInput ? Color.BLUE : Color.BLACK);
        if(number > 0)
        	setText(number + "");
        else
        	setText("");
        num = number;
    }
    
    public int getNumber() {
    	return num;
    }

    /**
     * Returns x position in game.
     *
     * @return  X position in game.
     */
    public int getFieldX() {
        return x;
    }

    /**
     * Return y position in game.
     *
     * @return  Y position in game.
     */
    public int getFieldY() {
        return y;
    }

	/**
	 * @return the isThisInputByHumanBeing
	 */
	public boolean isThisInputByAutoSuggested() {
		return isThisInputByAutoSuggested;
	}

	/**
	 * @param isThisInputByHumanBeing the isThisInputByHumanBeing to set
	 */
	public void setThisInputByAutoSuggested(boolean isThisInputByAutoSuggested) {
		this.isThisInputByAutoSuggested = isThisInputByAutoSuggested;
	}
}