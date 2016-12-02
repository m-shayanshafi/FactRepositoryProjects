package BoardGames.Battleship;

/**
 * This class represents a Piece of a ship in the  game of Battleship. A ship is contained on each of the BattleshipNodes with
 * that are defined as a 'shipSpace'.
 */

public class Ship {
	private boolean hit = false;
	private final int shipID, xPos, yPos; // xPos and yPos are not currently needed
	// but may be needed later
	
	/**
	 * This is a constructor for the ship class, defines the position of the ship via the BattleshipNode
	 * @param BattlshipNode 
	 * @param shipID 
	 */
	public Ship (BattleshipNode bn, int shipID){
		this.xPos = bn.getX();
		this.yPos = bn.getY();
		this.shipID = shipID;
	}
	

	/**
	 * @return shipID
	 */
	public int getShipID(){
		return shipID;
	}
	
	
	/**
	 * @return isHit
	 */
	public boolean isHit(){
		return this.hit;
	}
	
	/**
	 * Hit's the ship
	 */
	public void hit(){
		this.hit = true;
	}
	
}
