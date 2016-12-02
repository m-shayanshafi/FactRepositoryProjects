/*
 * Classname			: DCButtonBoard
 * Author			: Christophe Hertigers <christophe.hertigers@pandora.e>
 * Creation Date		: 2002/02/27
 * Last Updated			: Thursday, October 17 2002, 23:30:34
 * Description			: GUI representation of the DCBoard.
 * GPL disclaimer		:
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gui2d;

/* package import */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import main.DCConstants;

/**
 * The GUI representation of the DCBoard. A subclass of JPanel, on 
 * which an array of buttons is drawn. It knows its foreground color 
 * and background color.
 * 
 * @author	Christophe Hertigers
 * @version	Thursday, October 17 2002, 23:30:30
 */
public class DCButtonBoard extends JPanel {

	/*
	 * CLASS VARIABLES
	 */
	private static final int	COLOR_BACK_DARK=0;
	private static final int	COLOR_BACK_LIGHT=1;
	private static final int	COLOR_SELECTION_MOVE=0;
	private static final int	COLOR_SELECTION_CAPTURE=1;
	private static final int	COLOR_SELECTION_CAFAR=2;
		
	/* 
	 * INSTANCE VARIABLES 
	 */
	private DC2dGUI ref2dGUI;
	public DCImageButton[][] buttons;
	private JLabel[] rLabelArray, fLabelArray;
	private Color[] backColors;
	private Color[] selectionColors;
	private JLabel fillerLabel;
	private JPanel cBPanel, wBPanel, sBPanel, wsBPanel, csBPanel;
	private Border compoundBorder, ranksEmptyBorder, filesEmptyBorder;
	private int boardInt;
	
	
	/* 
	 * CONSTRUCTORS
	 */
	public DCButtonBoard(DC2dGUI parentRef, int bInt, Color darkColor, 
						 Color lightColor, Color moveColor, 
						 Color captColor, Color cafarColor) {
		super();
		
		ref2dGUI = parentRef;
		boardInt = bInt;
		backColors = new Color[2];
		backColors[COLOR_BACK_DARK] = darkColor;
		backColors[COLOR_BACK_LIGHT] = lightColor;
		selectionColors = new Color[3];
		selectionColors[COLOR_SELECTION_MOVE] = moveColor;
		selectionColors[COLOR_SELECTION_CAPTURE] = captColor;
		selectionColors[COLOR_SELECTION_CAFAR] = cafarColor;

		//Declarations
		String fileLabels[] = {"A", "B", "C", "D", "E", "F", 
	    	                   "G", "H", "I", "J", "K", "L"};
								   
		//create borders
		ranksEmptyBorder = BorderFactory.createEmptyBorder(0,0,0,5);
		filesEmptyBorder = BorderFactory.createEmptyBorder(0,0,0,0);
		Border tempEmpty = BorderFactory.createEmptyBorder(5,5,5,5);
		Border tempLowered = BorderFactory.createLoweredBevelBorder();
		Border tempCompound = BorderFactory.createCompoundBorder(
													tempEmpty,
													tempLowered);
		compoundBorder = BorderFactory.createCompoundBorder(
													tempCompound, 
													tempEmpty);
		this.setLayout(new BorderLayout());
		this.setBorder(compoundBorder);
			
		//create panels
		cBPanel = new JPanel();
		cBPanel.setLayout(new GridLayout(DC2dGUI.RANKS, DC2dGUI.FILES));
		wBPanel = new JPanel();
		wBPanel.setLayout(new GridLayout(DC2dGUI.RANKS, 1));
		wBPanel.setBorder(ranksEmptyBorder);
		sBPanel = new JPanel();
		sBPanel.setLayout(new BorderLayout());
		wsBPanel = new JPanel();
		wsBPanel.setLayout(new GridLayout(1, 1));
		csBPanel = new JPanel();
		csBPanel.setLayout(new GridLayout(1, DC2dGUI.FILES));
		csBPanel.setBorder(filesEmptyBorder);
		
		//create buttons (array)
		buttons = new DCImageButton[DC2dGUI.RANKS][DC2dGUI.FILES];	

		//the ranks labels on the left
		rLabelArray = new JLabel[DC2dGUI.RANKS];
			
		//the files labels on the bottom
		fLabelArray = new JLabel[DC2dGUI.FILES];
			
		//create individual buttons, labels + add to panel
		for (int i = DC2dGUI.RANKS-1; i >= 0; i--) {
			rLabelArray[i] = new JLabel(Integer.toString(i + 1));
			wBPanel.add(rLabelArray[i]);
			for (int j = 0; j < DC2dGUI.FILES; j++) {
				buttons[i][j] = new DCImageButton(ref2dGUI,
				                                  boardInt, 
												  j, 
												  i, 
				                                  calcBgColor(i,j));
				cBPanel.add(buttons[i][j]);
			}
		}
			
		//to force the layoutmanager to space out the files
		//labels evenly and directly under the buttons
		fillerLabel = new JLabel("A");
		fillerLabel.setVisible(false);
		wsBPanel.add(fillerLabel);
			
		//add files labels
		for (int i = 0; i < DC2dGUI.FILES; i++) {
			fLabelArray[i] = new JLabel(fileLabels[i], JLabel.CENTER);
			csBPanel.add(fLabelArray[i]);
		}
			
		//add panels to DCButtonBoard
		this.add(cBPanel, BorderLayout.CENTER);
		this.add(wBPanel, BorderLayout.WEST);
		sBPanel.add(csBPanel, BorderLayout.CENTER);
		sBPanel.add(wsBPanel, BorderLayout.WEST);
		this.add(sBPanel, BorderLayout.SOUTH);

		refresh();
	}

	public DCButtonBoard(DC2dGUI parentRef, int bInt, Color darkColor, 
						 								Color lightColor) {
		this(parentRef, bInt, darkColor, lightColor, 
						new Color(255,246,97), 
						new Color(255,246,97), 
						new Color(255,246,97)); 


	}
	
	/* 
	 * METHODS 
	 */

	/**
	 * Clears all buttons on the board. Removes highlighting, frozen states,
	 * tooltip, and occupying pieces.
	 *
	 */
	public void clearBoard() {
		
		for (int i=0; i < DCConstants.RANKS; i++) {
			for (int j=0; j < DCConstants.FILES; j++) {
				clearButton(i,j);
			}
		}
					
	}
	
	/**
	 * Calculates the right background color for the 
	 * DCImageButton. Is only used
	 * at design time. The calculated color is stored in the DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @return 			the background color of the DCImageButton
	 */
	private Color calcBgColor(int rInt, int fInt) {
		return backColors[(((rInt % 2) + (fInt % 2)) % 2)];
	}

	/**
	 * Sets the background of the selected DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @param 	c		the background color of the DCImageButton
	 */
	public void setButtonBackground(int rInt, int fInt, Color c) {
		buttons[rInt][fInt].setBackground(c);
	}

	/**
	 * Returns the background color of the selected DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @return			the background color of the DCImageButton
	 */
	public Color getButtonBgColor(int rInt, int fInt) {
		return buttons[rInt][fInt].getBgColor();
	}

	/**
	 * Returns true if the selected DCImageButton is highlighted.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @return	true if the selected DCImageButton is highlighted.
	 */
	public boolean isButtonHighlighted(int rInt, int fInt) {
		return buttons[rInt][fInt].isHighlighted();
	}
	
	/**
	 * Sets whether the selected DCImageButton is highlighted.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @param	b		true when the button is highligthed
	 */
	public void setButtonHighlighted(int rInt, int fInt, boolean b) {
		buttons[rInt][fInt].setHighlighted(b);
	}

	/**
	 * Returns true if the selected DCImageButton is frozen.
	 *
	 * @param	rInt	the rank of the DCImageButton
	 * @param	fInt	the file of the DCImageButton
	 * @return	true if the DCImageButton is frozen
	 */
	public boolean isButtonFrozen(int rInt, int fInt) {
		return buttons[rInt][fInt].isFrozen();
	}
	
	/**
	 * Sets whether the selected DCImageButton is frozen.
	 *
	 * @param rInt		the rank of the DCImageButton
	 * @param fInt		the file of the DCImageButton
	 * @param b			true when the button is frozen
	 */
	public void setButtonFrozen(int rInt, int fInt, boolean b) {
		buttons[rInt][fInt].setFrozen(b);
	}
	
	/**
	 * Sets the tool tip text of the selected DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @param 	t		the tool tip text
	 */
	public void setButtonToolTip(int rInt, int fInt, String t) {
		buttons[rInt][fInt].setToolTipText(t);
	}
	
	/**
	 * Sets the tool tip text of the selected DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 */
	public void clearButtonToolTip(int rInt, int fInt) {
		buttons[rInt][fInt].setToolTipText(null);
	}

	/**
	 * Sets the piece that is currently occupying the selected DCImageButton.
	 * 
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @param	piece	the DCImage2D representing the piece that's occupying
	 *				    this field.
	 */
	public void setButtonOccupyingPiece(int rInt, int fInt, DCImage2D piece) {
		buttons[rInt][fInt].setOccupyingPiece(piece);
	}

	/**
	 * Returns the piece that is currently occupying the selected DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 * @return			the DCImage2D representing the piece currently
	 * 					occupying this field. 
	 */
	public DCImage2D getButtonOccupyingPiece(int rInt, int fInt) {
		return buttons[rInt][fInt].getOccupyingPiece();
	}

	/**
	 * Removes the piece that is currently occupying the DCImageButton.
	 *
	 * @param 	rInt	the rank of the DCImageButton
	 * @param 	fInt	the file of the DCImageButton
	 */
	public void removeButtonOccupyingPiece(int rInt, int fInt) {
		buttons[rInt][fInt].removeOccupyingPiece();
	}

	/**
	 * Paints the Gui.
	 *
	 */
	public void refresh() {
	
		if (ref2dGUI.getPreferences().getDebugButtonBoard()) {
			System.out.println("DCButtonBoard.refresh invoked.");
			System.out.println("Button Size : " +
					buttons[0][0].getWidth() +
					"x" + buttons[0][0].getHeight());
		}
		
		this.validate();		
		for (int i=0; i < DC2dGUI.RANKS; i++) {
			for (int j=0; j < DC2dGUI.FILES; j++) {
				buttons[i][j].repaint();
			}
		}

		if (ref2dGUI.getPreferences().getDebugButtonBoard()) {
			System.out.println("Button Size : " +
					buttons[0][0].getWidth() +
					"x" + buttons[0][0].getHeight());
		}
	}

	/**
	 * Gets the current bounds of a button
	 * 
	 * @return		rectangle containing the bounds
	 */
	public Rectangle getButtonBounds() {
		return buttons[0][0].getBounds();
	}

	/**
	 * Repaints the button
	 *
	 * @param	rInt	rank of the button
	 * @param	fInt	file of the button
	 */
	public void repaintButton(int rInt, int fInt) {
		buttons[rInt][fInt].repaint();
	}

	/**
	 * Highlights the button (setBgColor + setHighlight + repaintButton)
	 *
	 * @param	rInt	rank of the button
	 * @param	fInt	file of the button
	 * @param	type	type of highlighting
	 */
	public void highlightButton(int rInt, int fInt, int type) {
		setButtonHighlighted(rInt, fInt, true);
		setButtonBackground(rInt, fInt, selectionColors[COLOR_SELECTION_MOVE]);
		repaintButton(rInt, fInt);
	}

	/**
	 * Unhighlights the button (setBgColor + setHighlight + repaintButton)
	 *
	 * @param	rInt	rank of the button
	 * @param	fInt	file of the button
	 */
	public void unHighlightButton(int rInt, int fInt) {
		Color origColor = getButtonBgColor(rInt, fInt);
		setButtonBackground(rInt, fInt, origColor);
		setButtonHighlighted(rInt, fInt, false);
		repaintButton(rInt, fInt);
	}

	/**
	 * Clears the selected button (unHighlightButton() + 
	 * setButtonFrozen(false) + clearButtonTooltip + 
	 * removeButtonOccupyingPiece + repaintButton)
	 *
	 * @param	rInt	rank of the button
	 * @param	fInt	file of the button
	 */
	public void clearButton(int rInt, int fInt) {
		unHighlightButton(rInt,fInt);
		setButtonFrozen(rInt,fInt,false);
		clearButtonToolTip(rInt,fInt);
		removeButtonOccupyingPiece(rInt,fInt);
		repaintButton(rInt,fInt);
	}
}

