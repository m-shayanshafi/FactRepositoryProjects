package thaigo.object;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JLabel;

import thaigo.property.AbstractRuler;
import thaigo.property.Owner;
import thaigo.property.Position;
import thaigo.state.UpdateTask;
import thaigo.utility.ImageLoader;
import thaigo.utility.PropertyManager;

/**
 * For creation <code>Pawn</code> object instead making pawn label directly
 * on panel on board.
 * 
 * @author Poramate Homprakob 5510546077
 * @version 2013.4.21
 *
 */
public class Pawn extends JLabel implements Serializable {

	/** For determine color of <code>Pawn</code>. */
	private static int color;

	/** Store position of <code>Pawn</code> object. */
	private Position position;

	private static UpdateTask updateTask;

	private static AbstractRuler ruler;

	private Owner owner;

	/**
	 * Constructs <code>Pawn</code> by using constants <code>Pawn.BLACK</code>
	 * or <code>Pawn.WHITE</code> as parameter.
	 * @param color constants of <code>Pawn</code>,
	 * <code>Pawn.BLACK</code> is 0 or <code>Pawn.WHITE</code> is 1
	 */	
	public Pawn(String path, Position position, AbstractRuler ruler, Owner owner) {
		this(path);
		this.position = position;
		this.ruler = ruler;
		updateTask = UpdateTask.getInstance(ruler);
		this.setColor(color);
		this.addMouseListener(new PawnAction());
		this.owner = owner;
	}

	/**
	 * Sub constructor for initializing Pawn picture label. 
	 * @param path path of pawn image
	 */
	private Pawn(String path) {
		super((new ImageLoader(path)).getImageIcon());
	}

	/**
	 * Get color as integer, <code>BLACK</code> is 0 or <code>WHITE</code> is 1.
	 * @return color as integer value
	 */
	public static int getColor() {
		return color;
	}

	/**
	 * Set color, used by only constructor when construct <code>Pawn</code> object.
	 * @param color as integer value
	 */
	private static void setColor(int color) {
		Pawn.color = color;
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
	 * Check is this <code>Pawn</code> equal to another or not.
	 * @param another <code>Pawn</code> object
	 * @return true if another has same position with this, otherwise false.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;

		if (object instanceof Pawn) {
			Pawn p = (Pawn)object;
			return p.getPosition().equals(this.getPosition());
		}

		if (object instanceof Position) {
			Position p = (Position)object;
			return p.equals(this.getPosition());
		}
		else
			return false;
	}

	/**
	 * Check is owner of this <code>Pawn</code> equal to another's or not.
	 * @param another <code>Pawn</code> object
	 * @return true if another has same owner with this, otherwise false.
	 */
	public boolean equalsOwner(Pawn pawn) {
		return owner.equals(pawn.owner);
	}

	/**
	 * Return each object to inner class.
	 * @return each <code>Pawn</code>
	 */
	private Pawn getPawn() {
		return this;
	}

	/**
	 * Handler mouse event of <code>Pawn</code> object.
	 */
	public class PawnAction extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (getPawn().owner.equals(new Owner())) {
				updateTask.PawnCommand(((Pawn)arg0.getSource()).getPosition());
			}
		}
	}
}
