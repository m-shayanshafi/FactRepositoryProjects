import java.util.Random;
import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

public class MyFirstRobot implements IFruitWarRobot {

	Random rand = new Random();
	
	// Customize this method to set action of each thrower in the current round.
	public void strategy(IFruitThrower[] throwers) {
		
		//for each thrower in our team
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];
		
			//throw at a random enemy position
			t.throwAt(rand.nextInt(Rules.BASKET_CNT()));
		}
	}

	// This method is called after a round. Get enemy strategy here.
	public void result(EnemyInfo[] enemies) {
		//nothing. This robot does not care about results.
	}
}
