package fruitwar.sample.test;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;

public class Buggy_NullPointerStrategy implements IFruitWarRobot{

	
	public void strategy(IFruitThrower[] throwers) {
		IFruitThrower t = null;
		t.throwAt(0);
	}

	public void result(EnemyInfo[] enemies) {

	}
}
