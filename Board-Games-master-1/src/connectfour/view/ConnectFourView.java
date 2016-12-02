package connectfour.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import common.model.GameBoard;
import common.view.GameBoardView;

import java.util.Observable;

/**
 * Classe vue du Puissance 4
 * @see GameBoardView.java
 * @author LETOURNEUR Léo
 *
 */
public class ConnectFourView extends GameBoardView {
	private static final long serialVersionUID = -103676131428960668L;
	
	private JPanel buttonPane;
	private JButton[] casesButton;
	private JComboBox<String> nombreAAligner;

	public ConnectFourView(GameBoard game) {
		super(game);
		
		largeurCase = 54;
		
		loadIcons();
		loadPanels();
		loadMenu();

		setTitle("Connect Four");
		setSize(this.getWidth(), this.getHeight()+40);
		setVisible(true);
	}
	
	/*@Override
	public void resetGrid() {

		super.resetGrid();
		
		for (int j = 0; j < length; j++)
		{
			casesButton[j].setEnabled(true);
			casesButton[j].setSize(casesButton[j].getSize().width,buttonPane.getSize().height);
		}
	}*/

	/**
	 * Methode qui permet de charger la combo box de choix du nombre de cases gagantes
	 * 
	 */
	@Override
	public void loadMenu() {
		
		super.loadMenu();
		
		String[] pattern = {"3","4","5","6","7","8"};
		nombreAAligner = new JComboBox<String>(pattern);
		nombreAAligner.setMaximumSize(new Dimension(60,28));
		nombreAAligner.setEditable(false);
		nombreAAligner.setSelectedItem("4");
		menuBar.add(nombreAAligner);
		menuBar.addSeparator();
	}
	
	/**
	 * Methode qui permet de charger les boutons au dessus de la grille de jeu
	 * 
	 */
	@Override
	public void loadPanels() {

		super.loadPanels();
		
		ImageIcon iconButton = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/button.png"));
		
		buttonPane = new JPanel();
		buttonPane.setBounds(50, 50, largeurCase * length, largeurCase);
		buttonPane.setLayout(new GridLayout(1, length));
		contentPane.add(this.buttonPane);
		casesButton = new JButton[length];
		for (int j = 0; j < length; j++) {
			casesButton[j] = new JButton();
			casesButton[j].setIcon(iconButton);
			buttonPane.add(casesButton[j]);
		}
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				casesLabel[i][j].setBorder(null);
				casesLabel[i][j].setRolloverEnabled(false);
			}
		}

		labelPane.setBounds(50, 110, largeurCase * length, largeurCase * width);
	}

	public void update(Observable arg0, Object arg1) {}

	public JPanel getButtonPane() {
		return buttonPane;
	}

	public void setButtonPane(JPanel buttonPane) {
		this.buttonPane = buttonPane;
	}

	public JButton[] getCasesButton() {
		return casesButton;
	}

	public void setCasesButton(JButton[] casesButton) {
		this.casesButton = casesButton;
	}

	public JComboBox<String> getNombreAAligner() {
		return nombreAAligner;
	}

	public void setNombreAAligner(JComboBox<String> nombreAAligner) {
		this.nombreAAligner = nombreAAligner;
	}
}
