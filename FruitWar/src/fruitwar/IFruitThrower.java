package fruitwar;

/**
 * Represent a thrower in Fruit War. 
 * 
 * @author wangnan
 *
 */
public interface IFruitThrower {
	
	/**
	 * Throw at the rival thrower at the given position.  Range from
	 * 0 to {@code fruitwar.Rules.BASKET_ID_MAX}. If the position is
	 * out of range, your whole team loses! (Exception will be thrown). 
	 * 
	 * This is an action command. You can call only one action command 
	 * each round. If action commands are invoked multiple times, only
	 * the last one takes effect.
	 * 
	 * @param target Position of a rival fruit thrower. Range from
	 * 					0 to {@code fruitwar.Rules.BASKET_ID_MAX}
	 */
	void throwAt(int target);
	
	/**
	 * Hide behind basket this round.
	 * 
	 * This is an action command. You can call only one action command 
	 * each round. If action commands are invoked multiple times, only
	 * the last one takes effect.
	 */
	void hide();
	
	/**
	 * Get the position of the thrower.
	 * @return Basket ID, range from 0 to {@code fruitwar.Rules.BASKET_ID_MAX}
	 */
	int getPosition();
	
	/**
	 * Get hit points left of the thrower.
	 * @return Hit points left.
	 */
	int getHP();
	
	/**
	 * Get how many continuous rounds this thrower has hide.
	 * @return Continuous rounds this thrower has hide.
	 */
	int getCowardCount();
}
