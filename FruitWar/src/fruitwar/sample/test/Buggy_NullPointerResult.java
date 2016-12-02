package fruitwar.sample.test;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;

public class Buggy_NullPointerResult implements IFruitWarRobot{

	
	public void strategy(IFruitThrower[] throwers) {

	}

	public void result(EnemyInfo[] enemies) {
		IFruitThrower t = null;
		t.throwAt(0);
	}
}
