package thaigo.object;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.state.UpdateTask;

/**
 * Panel of Board.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class GOPanel extends JPanel {

	/** Store position of <code>GOPanel</code> object. */
	private Position position;

	/** Add ActionListener in GOPanel. */
	private static UpdateTask updateTask;

	private static AbstractRuler ruler;

	/**
	 * Create <code>GOPanel</code> object.
	 * @param position position of <code>GOPanel</code> object
	 * @param color color of <code>GOPanel</code> object
	 * @param ruler ruler of <code>GOPanel</code> object
	 */
	public GOPanel(Position position, Color color, AbstractRuler ruler) {
		super.setBackground(color);
		this.position = position;
		this.ruler = ruler;
		updateTask = UpdateTask.getInstance(ruler);
		this.addMouseListener(new GOPanelAction());
	}

	/**
	 * Get position of <code>Pawn</code> object.
	 * @return position of <code>Pawn</code> object
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Set position of <code>Pawn</code> object.
	 * @param position new position of <code>Pawn</code> object
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Highlights the legal move on the board.
	 */
	public void changeColor() {
		Color color = this.getBackground();
		//Method is still not good enough
		for(BoardModel c : BoardModel.values()){
			if(c.getColor1().equals(color))
				this.setBackground(c.getHighlightColor1());
			if(c.getColor2().equals(color))
				this.setBackground(c.getHighlightColor2());
		}
	}

	/**
	 * Handler mouse event of <code>GOPanel</code> object.
	 */
	public class GOPanelAction extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			updateTask.GOPanelCommand(((GOPanel)e.getSource()).getPosition(), e);
		}
	}

	/**
	 * Set color of <code>GOPanel</code> object.
	 * @param color new color of <code>GOPanel</code> object
	 */
	public void setColor(Color color) {
		this.setBackground(color);
	}
}
