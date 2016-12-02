package fruitwar;

/**
 * Info of one enemy thrower in the last round.
 * 
 * @author wangnan
 *
 */
public class EnemyInfo {
	
	/**
	 * Which position was the thrower throwing at, 
	 * in the last round.
	 */
	public int target;
	
	/**
	 * How many hp does the thrower left 
	 */
	public int hp;
	
	public EnemyInfo(int hp, int target) {
		this.hp = hp;
		this.target = target;
	}
}
