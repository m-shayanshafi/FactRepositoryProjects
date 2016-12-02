package inf101.games.gui;

import javax.swing.JLabel;

/**
 * En merkelapp for å vise koordinater i spillet.
 * 
 * @author Anya Helene Bagge
 *
 */
public class CoordLabel extends JLabel {
	private static final long serialVersionUID = -7463325058983028689L;

	public CoordLabel(String s) {
		 super(s);
		 this.setForeground(Style.FOREGROUND);
		 this.setBackground(Style.BACKGROUND);
		 this.setOpaque(true);
	}
	
	public CoordLabel(int n) {
		this(String.valueOf(n + 1));
	}
}
