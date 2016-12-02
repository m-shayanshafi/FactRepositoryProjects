package BoardGames.Battleship;
import Tools.Node;
/**
 * This class represents a board space in the classic game of Battleship.
 */
public class BattleshipNode extends Node{

	private boolean shipSpace;
	private Ship ship = null;
	
	/**
	 * @param x
	 * 		Position of x (sent to Node)
	 * @param y
	 */
	public BattleshipNode(int x, int y){
		super(x,y);
	}
	
	/**
	 * @param isShipSpace
	 * 				declares whether or not a ship is there (not if it's hit or not though)
	 */
	public void setShipSpace(boolean isShipSpace){
		this.shipSpace = isShipSpace;
	}
	
	/**
	 * @return
	 * 		isShipSpace
	 */
	public boolean isShipSpace(){
		return this.shipSpace;
	}
	
	/**
	 * @returns the ship created for this Node
	 */
	public Ship getShip(){
		return ship;
	}
	
	/**
	 * @param shipID
	 * 			ID of the ship so they can be put together to form 1 ship theoretically	
	 * Creates a ship on this Node
	 */
	public void setShip(int shipID){
		ship = new Ship(this, shipID);
	}
}
