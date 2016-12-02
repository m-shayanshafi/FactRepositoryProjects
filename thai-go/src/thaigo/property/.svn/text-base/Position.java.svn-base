package thaigo.property;
import java.io.Serializable;
/**
 * For assignment position to object.
 * 
 * @author Poramate Homprakob 5510546077
 * @version 2013.04.21
 *
 */
public class Position implements Serializable {

	private int x;
	private int y;
	
	/**
	 * Constructor for initializing position with <code>int x</code> and <code>int y</code>.
	 * @param x integer value of position in x-axis
	 * @param y integer value of position in y-axis
	 */
	public Position(int x, int y) {
		setPosition(x, y);
	}
	
	/**
	 * Set the position.
	 * @param x integer value of position in x-axis
	 * @param y integer value of position in y-axis
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get x-axis position.
	 * @return x-axis position value
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get y-axis position.
	 * @return y-axis position value
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Check whether is the same position or not.
	 * @param object another position to be compared with
	 * @return true if is the same position, otherwise false.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof Position))
			return false;
		
		Position position = (Position)object;
		if (position.getX() != this.getX() || position.getY() != this.getY())
			return false;
		
		return true;
	}
	
	/**
	 * Get position as universal value, such as 1A.
	 * @return position as universal value, such as 1A
	 */
	@Override
	public String toString() {;
		return x + "," + y;
	}
}
