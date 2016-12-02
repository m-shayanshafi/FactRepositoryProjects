package fruitwar.supp;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import fruitwar.IFruitWarRobot;
import fruitwar.core.BattleManager;
import fruitwar.core.BattleResult;
import fruitwar.util.Logger;

/**
 * This class is used to load two given Robot jar files,
 * make them start a Fruit War, and generates a BattleResult.
 * 
 * This class generates all output to the given output stream if specified,
 * otherwise the output will be System.out.
 * This class generates abstract output to System.out.
 * 
 * @author wangnan
 *
 */
public class FruitWarLoader {
	
	PrintStream oldStdOut;
	PrintStream dummyOut;
	
	public FruitWarLoader(){
		dummyOut = new PrintStream(new OutputStream(){
			public void write(int b) throws IOException {
			}
		});
	}
	
	public void onError(String msg){
		restoreSystemOut();
		Logger.error(msg);
		hideSystemOut();
	}
	
	private void load(String robotJar1, String robotJar2, boolean verbose){
		
		//hide std out, do not let customized bot trash it.
		//because currently we check battle result according to
		//std out.
		hideSystemOut();
		
		IFruitWarRobot robot1 = loadRobotClass(robotJar1);
		if(robot1 == null){
			onError("Can not load robot1: " + robotJar1);
			restoreSystemOut();
			return;
		}
		IFruitWarRobot robot2 = loadRobotClass(robotJar2);
		if(robot2 == null){
			onError("Can not load robot2: " + robotJar2);
			restoreSystemOut();
			return;
		}
		
		//start battle
		BattleManager bm = new BattleManager(robot1, robot2, verbose);
		BattleResult result = bm.doBattle();
		
		//restore system out
		restoreSystemOut();
		
		System.out.println(result.formatString());
		System.out.println(result.formatBattleLogs(false));
		
	}
	
	private void restoreSystemOut() {
		System.out.flush();
		oldStdOut.flush();
		System.setOut(oldStdOut);
	}

	private void hideSystemOut() {
		System.out.flush();
		dummyOut.flush();
		oldStdOut = System.out;
		System.setOut(dummyOut);
	}

	private IFruitWarRobot loadRobotClass(String jarFileName) {

		//retrieve class name
		RobotProps prop = RobotProps.loadFromFile(jarFileName);
		if(prop == null)
			return null;
		
		String className = prop.get(RobotProps.ROBOT_CLASS);
		if(className.length() == 0){
			onError("No " + RobotProps.ROBOT_CLASS + " defined in file " + jarFileName);
			return null;
		}
		
		//load and initialize class instances
		IFruitWarRobot robot = null;
		try {
			ClassLoader cl = FruitWarLoader.class.getClassLoader();
			Class c = cl.loadClass(className);
			robot = (IFruitWarRobot) c.newInstance();
		} catch (Exception e) {
			onError("Can not load robot: " + className + " in " + jarFileName);
			e.printStackTrace();
		}
		
		return robot;
	}
	
	public static void main(String[] args){
		if(args.length < 2 || args.length > 3){
			System.out.println("Parameters: <robot1> <robot2> [verbose]");
			return;
		}
		
		boolean verbose = false;
		if(args.length == 3){
			verbose = args[2].equalsIgnoreCase("verbose");
		}
		
		new FruitWarLoader().load(args[0], args[1], verbose);
		
		//Custom robots may have infinite loop in them. Exit forcefully
		System.exit(0);
	}
}
