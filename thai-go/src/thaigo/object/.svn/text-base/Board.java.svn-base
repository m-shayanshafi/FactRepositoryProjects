package thaigo.object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.state.UpdateTask;
import thaigo.utility.PropertyManager;
import thaigo.view.MouseOverHighlighter;
/**
 * Board is made from GOPanels.
 * 
 * @author Nol Pasurapunya 5510546018
 * @version 2013.4.21
 *
 */
public class Board extends JPanel {

	/**
	 * Eliminate magic number, and make it able to change number of pawn
	 * or table later.
	 */
	private final int TABLE;

	/**
	 * Eliminate magic value of colors, and make it able to change color of GOPanels later.
	 */
	// Change from final to not final.
	private Color COLOR1 = new Color(56, 22, 13);
	private Color COLOR2 = new Color(205, 193, 171);

	/**
	 * Eliminate magic value of dimension, and make it able to change size of GOPanels later.
	 */
	private final Dimension DIMENSION = new Dimension(50, 50);

	/**
	 * Ruler for handling rule of the game.
	 */
	private AbstractRuler ruler;

	/**
	 * Create box which type is gopanel.
	 */
	private GOPanel[][] box;

	/**
	 * Constructor of this class.
	 * @param ruler reference of ruler.
	 */
	public Board(AbstractRuler ruler) {
		this.ruler = ruler;
		this.TABLE = Integer.parseInt(PropertyManager.getProperty("table"));
		this.setLayout(new GridLayout(TABLE,TABLE));

		createBoard();
	}

	/**
	 * Creates board.
	 */
	private void createBoard() {
		MouseListener mouseOverListener = new MouseOverHighlighter(this);
		box = new GOPanel[TABLE][TABLE];
		///////////////////////
		UpdateTask task = UpdateTask.getInstance(ruler);
		for (int k = TABLE-1 ; k >= 0 ; k--) {
			for (int j = 0 ; j < TABLE ; j++) {
				if ((j+k) % 2 == 0)
					box[j][k] = new GOPanel(new Position(j,k), COLOR1, ruler);
				else
					box[j][k] = new GOPanel(new Position(j,k), COLOR2, ruler);
				
				box[j][k].setPreferredSize(DIMENSION);
				box[j][k].setToolTipText(j + "," + k);
				box[j][k].addMouseListener(mouseOverListener);
				this.add(box[j][k]);
			}
		}
	}

	/**
	 * Initialize pawn.
	 */
	public void initPawn() {
		ruler.initPawn(this);
	}

	/**
	 * Add pawn to that position.
	 * @param pawn added.
	 * @param position that use for add.
	 */
	public void addPawn(Pawn pawn, Position position) {
		box[position.getX()][position.getY()].add(pawn);
	}

	/**
	 * Change color to show which block can move.
	 * @param position that use for show.
	 */
	public void highLight(Position position){

		box[position.getX()][position.getY()].changeColor();
	}

	/**
	 * Change color of board.
	 * @param color1 first color
	 * @param color2 second color.
	 */
	public void changeColor(Color color1 , Color color2){
		COLOR1 = color1;
		COLOR2 = color2;
		resetColor();
	}

	/**
	 * Set color1 and color2 to board.
	 */
	public void resetColor() {
		for (int k = TABLE-1 ; k >= 0 ; k--) {
			for (int j = 0 ; j < TABLE ; j++) {
				if ((j+k) % 2 == 0) 
					box[j][k].setBackground(COLOR1);
				else
					box[j][k].setBackground(COLOR2);
			}
		}
	}

	/**
	 * Change position to real time.
	 * @param yourPawn reference of yourPawn.
	 * @param foePawn reference of foePawn.
	 */
	public void render(List<Pawn> yourPawn, List<Pawn> foePawn) {
		// remove and repaint
		for (int k = TABLE-1 ; k >= 0 ; k--) {
			for (int j = 0 ; j < TABLE ; j++) {
				box[j][k].removeAll();
				box[j][k].repaint();
			}
		}
		
		for (Pawn p : yourPawn) {
			box[p.getPosition().getX()][p.getPosition().getY()].add(p);
		}

		for (Pawn p : foePawn) {
			box[p.getPosition().getX()][p.getPosition().getY()].add(p);
		}
		resetColor();

		// repaint again
		for (int k = TABLE-1 ; k >= 0 ; k--) {
			for (int j = 0 ; j < TABLE ; j++) {
				box[j][k].repaint();
			}
		}
	}

	/**
	 * Get box in that position.
	 * @param position that want to get box.
	 * @return gopanel of that box.
	 */
	public GOPanel getBox(Position position){
		return box[position.getX()][position.getY()];
	}

	/**
	 * Change model of pawn.
	 * @param you imageIcon of yourPawn.
	 * @param foe imageIcon of foePawn.
	 */
	public void setPawnModel(ImageIcon you, ImageIcon foe){
		List<Pawn> yourPawn = ruler.getYourPawn();
		List<Pawn> foePawn = ruler.getFoePawn();
		for(int k = 0 ; k < yourPawn.size() ; k++){
			yourPawn.get(k).setIcon(you);
		}
		for(int k = 0 ; k < foePawn.size() ; k++){
			foePawn.get(k).setIcon(foe);
		}
	}
}
