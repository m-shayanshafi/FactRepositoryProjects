package fruitwar.sample.test;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;

public class Buggy_ThrowOutOfRange implements IFruitWarRobot{

	
	public void strategy(IFruitThrower[] throwers) {
		//cause a violation of rule: throw out of range 
		throwers[0].throwAt(10000);
	}

	public void result(EnemyInfo[] enemies) {
	}
}
