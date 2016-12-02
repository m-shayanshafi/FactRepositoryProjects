package fruitwar.sample.test;
import java.util.Random;
import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;

class OutterHelper{
	Random rand = new Random();
	public int rand(int n){
		return rand.nextInt(n);
	}
}

/**
 * Test whether robot with inner class can be handled by
 * Fruit War server correctly, including compile, run, 
 * view source, delete.
 * 
 * @author wangnan
 *
 */
public class Test_InnerClass implements IFruitWarRobot {

	static class InnerHelper{
		OutterHelper h = new OutterHelper();
		
		int rand(int n){
			return h.rand(n);
		}
	}
	
	class InnerHelper2{
		InnerHelper h2 = new InnerHelper();
		
		int rand(int n){
			return h2.rand(n);
		}
	}
	
	InnerHelper2 h = new InnerHelper2();
	
	
	
	// Customize this method to set action of each thrower in the current round.
	public void strategy(IFruitThrower[] throwers) {
		
		for(int i = 0; i < throwers.length; i++){
			IFruitThrower t = throwers[i];
		
			//throw at a random enemy position
			t.throwAt(h.rand(Rules.BASKET_CNT()));
		}
	}

	// This method is called after a round. Get enemy strategy here.
	public void result(EnemyInfo[] enemies) {
		//nothing. This robot does not care about results.
	}
}
