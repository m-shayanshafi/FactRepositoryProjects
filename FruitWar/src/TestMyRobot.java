import fruitwar.IFruitWarRobot;
import fruitwar.core.BattleManager;
import fruitwar.core.BattleResult;
import fruitwar.sample.Focus;
import fruitwar.sample.FocusKiller;


/**
 * Test battle between the two given robots.
 * 
 * @author wangnan
 *
 */
public class TestMyRobot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//IFruitWarRobot r1 = new MyFirstRobot();
		//IFruitWarRobot r2 = new RobotTester();
		
		IFruitWarRobot r1 = new Focus();
		IFruitWarRobot r2 = new FocusKiller();

		//IFruitWarRobot r1 = new RandKillerAuto();
		//IFruitWarRobot r2 = new ScrunchRandKillerAuto();

		boolean verbose = true;	//whether to show details of each round.
		
		//do battle
		BattleResult r = new BattleManager(r1, r2, verbose).doBattle();
		
		//show result
		System.out.println(r.formatString());
		System.out.println("------------------");
		System.out.println(r.formatBattleLogs(true));
		
		
		String winner = r.getWinnerName();
		if(winner != null)
			System.out.println("Winner is: " + winner);
		else
			System.out.println("Draw game.");
		
		//in case your robot hangs the worker thread.
		System.exit(0);
	}
}
