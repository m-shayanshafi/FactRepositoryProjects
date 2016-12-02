package BoardGames.Battleship;

import java.awt.Color;
import javax.swing.JButton;

/**@author Dean Johnson
 * FireButton Class:
 * This is the class that will be used when you need a button to click on as
 * a battleship square
 */
public class FireButton extends JButton{
	private BattleshipNode bsNode;	
	private String type = "";
	
	/**
	 * Constructor for FireButton that does not apply the isShipSpace
	 * property
	 * @param col
	 * @param row
	 * @param buttonBgColor
	 */
	public FireButton(int col, int row, Color buttonBgColor){
		bsNode = new BattleshipNode(col,row);
		this.setBackground(buttonBgColor);
		
	}
	
	/**
	 * Constructor for a FireButton
	 * This contains a BattleshipNode on the button.
	 * @param col
	 * @param row
	 * @param isShipSpace
	 * @param buttonBgColor
	 */
	public FireButton(int col, int row, boolean isShipSpace, Color buttonBgColor){
		bsNode = new BattleshipNode(col,row);
		
		if(isShipSpace){
			this.setText("B");
			this.setBackground(Color.BLUE);
		}
		else{
			this.setText("");
			this.setBackground(buttonBgColor);
		}
			
	}
	
	/**
	 * Returns the Node for this button.
	 * @return bsNode
	 */
	public BattleshipNode getNode(){
		return this.bsNode;
	}

}