package fruitwar.sample;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

/**
 * This robot is specifically designed to defeat the robot 
 * 'Focus'. We know their focus, and we always let our 
 * thrower hide at their focus. So they can never hit us. 
 */
public class FocusKiller implements IFruitWarRobot {

	int enemyFocus = 0;
	
	public void strategy(IFruitThrower[] throwers) {
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];
			
			//if the thrower is at enemy focus, hide.
			//So enemy will not hit us.
			if(t.getPosition() == enemyFocus)
				t.hide();
			else
				//simply, we throw at the same focus they have.
				//Let our opponent taste their focus!
				t.throwAt(enemyFocus);
		}
	}

	public void result(EnemyInfo[] enemies) {
		//enemy focus is guaranteed to work like this.
		enemyFocus++;
		if(enemyFocus > Rules.BASKET_ID_MAX())
			enemyFocus = 0;
	}
}
