import java.util.Random;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

/**
 * This is the robot used to test whether the opponent
 * robot is basically functional (no exception).
 * 
 * @author wangnan
 *
 */
public class RobotTester implements IFruitWarRobot {
	
	private Random rand = new Random();
	
	public void strategy(IFruitThrower[] throwers) {
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];

			//Cover possible cases
			switch(i){
			case 0:	t.throwAt(0);									break;
			case 1:	t.throwAt(rand.nextInt(Rules.BASKET_CNT()));	break;
			case 2:	t.hide();										break;
			default:												break;
			}
		}
	}

	public void result(EnemyInfo[] enemies) {
	}
}
