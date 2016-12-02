/*
 * Classname		: DCImageButton
 * Author			: Christophe Hertigers <xof@pandora.be>
 * Creation Date	: 2002/02/27
 * Last Updated		: Thursday, October 17 2002, 23:34:51
 * Description		: GUI representation of the DCField.
 * GPL disclaimer	:
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


/**
 * The 2d GUI representation of a DCField. An DCImageButton is aware of its 
 * location in the array of ImageButtons. It knows which background color it is
 * supposed to have when not highlighted. It also knows if it is highlighted or 
 * frozen. A piece occupying the DCImageButton is drawn on the canvas of the
 * DCImageButton.
 * 
 * @author	Christophe Hertigers
 * @author	Koenraad Heijlen
 * @version Thursday, October 17 2002, 23:34:51
 */
class DCImageButton extends JButton {
		
	/* INSTANCE VARIABLES */ 
	private DC2dGUI		ref2dGUI;
	private DCImage2D	occupyingPiece;
	private Color		bgColor;
	private	int			locBoardInt;
	private	int			locFileInt;
	private	int			locRankInt;
	private boolean		highlightedBoolean;
	private	boolean		frozenBoolean;
	
	
	/* CONSTRUCTOR */

	/**
	 * Constructs a new DCImageButton with the specified DC2dGUI as parent, its
 	 * BoardInt, FileInt, RankInt and background color.
	 * 
	 * @param	parentRef	reference to its parent DC2dGUI.
	 * @param	bInt		the BoardInt on which it is located.
	 * @param	fInt		the FileInt of the DCImageButton.
	 * @param	rInt		the RankInt of the DCImageButton.
	 * @param	bgColor		its background color.
	 */
	public DCImageButton(DC2dGUI parentRef,
					     int bInt, 
					     int fInt, 
					     int rInt, 
					     Color c) {
		super();
		
		ref2dGUI = parentRef;
		locBoardInt = bInt;
		locFileInt = fInt;
		locRankInt = rInt;
		highlightedBoolean = false;
		frozenBoolean = false;
		occupyingPiece = null;
		bgColor = c;

		// set background
		this.setBackground(bgColor);

		//set insets
		this.setMargin(new Insets(2, 2, 2, 2));
		
		//could this speed things up ? - vipie
		this.setOpaque(true);

		//add event listeners + add to frame
		this.addActionListener(ref2dGUI.getActionHandler());
		this.addMouseListener(ref2dGUI.getMouseHandler());

	}
		
	/* METHODS */

	/**
	 * Returns the BoardInt on which this DCImageButton is located.
	 * @return			the boardInt on which the DCImageButton is located
	 * 
	 */
	public int getBoard() {
		return locBoardInt;
	}
		
	/**
	 * Returns the FileInt of this DCImageButton. It is the number of the row
	 * of the DCBoard on which is is located.
	 *
	 * @return			the fileInt on which the DCImageButton is located 
	 */
	public int getFile()  {
		return locFileInt;
	}
		
	/**
	 * Returns the RankInt of this DCImageButton. It is the number of the
	 * column of the DCBoard on which it is located.
	 *
	 * @return			the rankInt on which the DCImageButton is located 
	 */
	public int getRank()  {
		return locRankInt;
	}
		
	/**
	 * Returns the current background color of this DCImageButton.
	 *
	 * @return			the current background color 
	 */
	public Color getBgColor() {
		return bgColor;
	}
		
	/**
	 * Returns true if this DCImageButton is currently highlighted.
	 * 
	 */
	public boolean isHighlighted() {
		return highlightedBoolean;
	}
		
	/**
	 * Sets wether this DCImageButton is currently highlighted.
	 * 
	 */
	public void setHighlighted(boolean hBool) {
		highlightedBoolean = hBool;
	}
		
	/**
	 * Returns true if this DCImageButton is currently frozen. This means that
	 * if a piece is currently occupying this field, it cannot move.
	 *
	 */
	public boolean isFrozen() {
		return frozenBoolean;
	}
		
	/**
	 * Sets wether this DCImageButton is currently frozen.
	 * 
	 */
	public void setFrozen(boolean fBool) {
		frozenBoolean = fBool;
	}

	/**
	 * Paints this DCImageButton. It paints the occupyingPiece if it is set,
	 * otherwise only the background color.
	 * 
	 * @param	g	The graphics context to use for painting.
	 */
	public void paintComponent (Graphics g) {

		super.paintComponent(g);
	
		/* First cast the Graphics to a Graphicd2D for greater functionality */
		Graphics2D g2 = (Graphics2D) g;
		
		if (occupyingPiece != null) {
				occupyingPiece.paintComponent(g2);
		}
		
		
	}

	/**
	 * Sets the piece that is currently occupying the DCImageButton.
	 * 
	 * @param	op		the DCImage2D representing the piece that's occupying
	 *				    this field.
	 */
	public void setOccupyingPiece(DCImage2D op) {
			
		//System.out.println(locBoardInt + "," + locRankInt + "," + locFileInt);
						
		occupyingPiece = op;
	}

	/**
	 * Returns the piece that is currently occupying the DCImageButton.
	 *
	 * @return			the DCImage2D representing the piece currently
	 * 					occupying this field. 
	 */
	public DCImage2D getOccupyingPiece() {
		return occupyingPiece;
	}

	/**
	 * Removes the piece that is currently occupying the DCImageButton.
	 *
	 */
	public void removeOccupyingPiece() {
		occupyingPiece = null;
	}
}


