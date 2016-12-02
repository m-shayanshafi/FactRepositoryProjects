package Tools;
/**
 * This class represents a Node that may be used in many different types of games. Will likely be a superclass
 * to more specific uses of the class.
 */
public class Node {
	private final int x;
	private final int y;
	
	/**
	 * @param x 
	 * 		Position of X
	 * @param y 
	 * 		Position of Y
	 */
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * @return y
	 */
	public int getY(){
		return x;
	}
}
