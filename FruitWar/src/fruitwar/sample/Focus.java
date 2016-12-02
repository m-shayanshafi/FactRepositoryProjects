package fruitwar.sample;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

/**
 * 
 * This robot tries to make all throwers hit the same position in 
 * opponent team, so it's likely that the opponent thrower at our 
 * "focus" will be "out of play" quickly. Then we have 3 throwers, 
 * while our opponent has only 2...
 *   
 */
public class Focus implements IFruitWarRobot{

	int focus = 0;	//our focus: the position we throw at.
	
	public void strategy(IFruitThrower[] throwers) {
		for(int i = 0; i < throwers.length; i++){
			
			//all throwers focus on the same enemy!
			throwers[i].throwAt(focus);
		}
	}

	//get opponent strategy here.
	public void result(EnemyInfo[] enemies) {
		
		//at our focus, get the enemy info 
		EnemyInfo e = enemies[focus];
		
		//if there's no one at focus (e == null), 
		//or it's just hit down (e.hp <= 0) 
		if(e == null || e.hp <= 0){
			//changeFocus
			focus++;
			
			//In Fruit War, the max position is Rules.BASKET_ID_MAX().
			//If the focus is bigger than that, we strat from 0.
			if(focus > Rules.BASKET_ID_MAX())
				focus = 0;
		}
	}
}
