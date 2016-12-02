package thaigo.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.LineBorder;

import thaigo.object.Board;
import thaigo.object.GOPanel;
import thaigo.object.Pawn;
/** Listener that will highlight the GO-Panel when mouse's over it.
 * 
 * @author Nol
 *
 */
public class MouseOverHighlighter extends MouseAdapter {

	private Board board;

	/** Initializes the lister and connect it to the board.
	 * 
	 * @param board Game Board
	 */
	public MouseOverHighlighter(Board board){
		this.board = board;
	}

	/** Highlight the panel when mouse enter this panel */
	public void mouseEntered( MouseEvent event ){
		try{
			GOPanel gop = (GOPanel) event.getComponent();
			gop.setBorder( new LineBorder(Color.BLUE, 2) );
		}
		catch(Exception e){}
		try{
			Pawn pawn = (Pawn) event.getComponent();
			board.getBox(pawn.getPosition()).setBorder( new LineBorder(Color.BLUE, 2) );
		}
		catch(Exception e){}
	}

	/** Cancel highlight the panel when mouse leave this panel */
	public void mouseExited( MouseEvent event ){
		try{
			GOPanel gop = (GOPanel) event.getComponent();
			gop.setBorder( null );
		}
		catch(Exception e){}
		try{
			Pawn pawn = (Pawn) event.getComponent();
			board.getBox(pawn.getPosition()).setBorder( null );
		}
		catch(Exception e){}
	}
}
