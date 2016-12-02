package fruitwar.sample.test;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;

public class Buggy_TimeoutStrategy implements IFruitWarRobot{

	
	public void strategy(IFruitThrower[] throwers) {
		//while(true);
		//use sleep this case, to distinguish with Buggy_TimeoutResult
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void result(EnemyInfo[] enemies) {
	}
}
