package fruitwar.sample;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;

public class CarpetBombing implements IFruitWarRobot{

	
	public void strategy(IFruitThrower[] throwers) {
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];
			
			//throw at enemy at the same position as t.
			t.throwAt(t.getPosition());
		}
	}

	public void result(EnemyInfo[] enemies) {
		//nothing.
		//This robot does not care about results.
	}
}
