package fruitwar;

/**
 * This is the base of Fruit War robot. Implement this interface
 * to create your own robot. 
 * 
 * @author wangnan
 *
 */
public interface IFruitWarRobot {

	/**
	 * This method is used to set actions in the next round. Customize
	 * this method to set action of each thrower in the current round.
	 * 
	 * @param throwers Array with dynamic length 
	 * {max @code fruitwar.core.Rules.BASKET_CNT}, which contains only 
	 * active throwers. If at position n the thrower is out of play,
	 * it will not be included in this array.
	 */
	public void strategy(IFruitThrower[] throwers);
	
	/**
	 * The enemy info of the last round. The length of parameter
	 * "enemies" is fixed value: {@code fruitwar.Rules.BASKET_CNT}. 
	 * If there's no enemy thrower at a position, the element in
	 * the array is null.
	 * 
	 */
	public void result(EnemyInfo[] enemies);
}
