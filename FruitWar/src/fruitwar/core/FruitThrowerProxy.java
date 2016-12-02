package fruitwar.core;

import fruitwar.IFruitThrower;


/**
 * The proxy of a Fruit Thrower. 
 * The proxy is introduced so as to forbid direct access to 
 * the actual game object from custom bots. 
 *  
 * @author wangnan
 *
 */
class FruitThrowerProxy implements IFruitThrower{

	private IFruitThrower peer;
	
	public FruitThrowerProxy(IFruitThrower peer){
		this.peer = peer;
	}
	
	public int getCowardCount() {
		return peer.getCowardCount();
	}

	public int getPosition() {
		return peer.getPosition();
	}

	public void hide() {
		peer.hide();
	}

	public void throwAt(int target) {
		peer.throwAt(target);
	}

	public int getHP() {
		return peer.getHP();
	}

}
