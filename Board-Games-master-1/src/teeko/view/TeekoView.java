package teeko.view;

import java.awt.Color;
import javax.swing.BorderFactory;

import common.model.GameBoard;
import common.view.GameBoardView;

/**
 * Classe vue du Teeko
 * @see GameBoardView.java
 * @author LETOURNEUR Léo
 *
 */
public class TeekoView extends GameBoardView {
	private static final long serialVersionUID = 1315096911689421301L;

	public TeekoView(GameBoard game) {
		super(game);

		largeurCase = 100;
		
		loadIcons();
		loadPanels();
		loadMenu();
		aloneCheck.setVisible(false);
		
		setTitle("Teeko");
		setVisible(true);
	}

	@Override
	public void loadPanels() {

		super.loadPanels();
		labelPane.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
		
	}
}
