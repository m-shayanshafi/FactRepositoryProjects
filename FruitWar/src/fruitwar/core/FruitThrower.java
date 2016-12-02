package fruitwar.core;

import fruitwar.IFruitThrower;
import fruitwar.Rules;


/**
 * The actual Fruit Thrower object.
 * Packet private class.
 * 
 * @author wangnan
 *
 */
class FruitThrower implements IFruitThrower{

	/**
	 * Hit points
	 */
	private int HP;
	
	/**
	 * Continuous round count without attack action.
	 */
	private int cowardCount;
	
	/**
	 * Action in the next round.
	 */
	private Action action;
	
	/**
	 * For position information
	 */
	private FruitThrower[] myTeam;
	
	
	
	FruitThrower(FruitThrower[] myTeam){
		HP = Rules.FRUIT_THROWER_HP();
		cowardCount = 0;
		action = new Action();
		this.myTeam = myTeam;
	}

	public int getPosition() {
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			if(myTeam[i] == this)
				return i;
		}
		
		throw new RuntimeException("Something wrong...");
	}

	public int getHP() {
		return HP;
	}
	
	public void hide() {
		action.action = Action.HIDE;
	}

	public void throwAt(int target) {
		if(target < 0 || target > Rules.BASKET_ID_MAX())
			throw new RuntimeException("Throwing target out of range: " + target);
		action.action = Action.THROW;
		action.target = target;
	}

	/**
	 * Called when this thrower is hit. Reduce HP.
	 */
	void onHit(){
		--HP;
	}
	
	/**
	 * Get current action. 
	 * @return Current action.
	 */
	Action getAction(){
		return action;
	}
	
	/**
	 * Whether this thrower is doing an attack
	 * @return
	 */
	boolean isAttacking(){
		return action.action == Action.THROW;
	}
	
	/**
	 * Clear the current action
	 */
	void clearAction(){
		//use hide as default dummy action.
		hide();
	}

	
	public int getCowardCount() {
		return cowardCount;
	}
	
	/**
	 * Package private function for the framework to increase 
	 * coward count. If it's a coward (hide more than Rules.COWARD_LIMIT 
	 * times continuously), it's out. 
	 */
	void addCowardCount() {
		cowardCount++;
		
		//if I'm a coward, I'm out! 
		if(cowardCount > Rules.COWARD_LIMIT())
			HP = 0;
	}
	
	/**
	 * Clear coward count.
	 */
	void resetCowardCount(){
		cowardCount = 0;
	}
}
