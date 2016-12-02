package fruitwar.sample;

import java.util.Random;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

public class RandomRobot implements IFruitWarRobot {

	Random rand = new Random();
	
	public void strategy(IFruitThrower[] throwers) {
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];
			
			//we randomly hide
			if(rand.nextInt(20) == 0)
				t.hide();
			else
				//throw at a random enemy position
				t.throwAt(rand.nextInt(Rules.BASKET_CNT()));
		}
	}

	public void result(EnemyInfo[] enemies) {
		//nothing.
		//This robot does not care about results.
	}
}
