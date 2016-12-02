package fruitwar.web.server;

import fruitwar.core.BattleResult;
import fruitwar.util.Logger;
import fruitwar.util.SyncFlag;

/**
 * This class is used to do battles in background.
 * This daemon should always run in background, and push
 * the following things (must-have features):
 * 		"generation of battle"	(depends on/uses RobotRepository)
 * 		"do battle"				(depends on/uses RobotRepository)
 * 		"update results"		(depends on/uses RobotRepository)
 * 
 * In addition, this class should support the 
 * following functionalities (should-have features):
 * 
 * 		query recent done battles.
 *		query battles to be done.
 * 		
 * @author wangnan
 *
 */
class BattleDaemon {

	public static final int STATUS_STOPPED = 0;
	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_END = 2;
	
	//how many time to wait between two battles.
	public static final int IDLE_INTERVAL = 10 * 1000;

	private static Thread daemonThread = null;
	private static SyncFlag runFlag = null;
	private static volatile boolean quit = false;
	
	private static BattleUtil battleUtil = null;
	
	synchronized static void init() {
		Logger.log("BattleDaemon.init start.");
	
		if(daemonThread == null){
			
			battleUtil = new BattleUtil();
			
			daemonThread = new Thread(new DaemonRunnable(), "Fruit War Battle Daemon");
			runFlag = new SyncFlag();
			
			runFlag.setAndNotify(true);
			daemonThread.start();
		
			quit = false;
		}else{
			throw new RuntimeException("Should assert: Do not re-init BattleDaemon.");
		}
		
		Logger.log("BattleDaemon.init end.");
	}


	/**
	 * Quit battle daemon. This should be called when server stops.
	 * @return
	 */
	synchronized static void destroy(){
		if(daemonThread != null){
			quit = true;
			
			//stop work
			setRunFlag(false);
			
			//interrupt to make it going immediately
			daemonThread.interrupt();
			
			//wait daemon thread.
			try {
				daemonThread.join();
			} catch (InterruptedException e) {
			}
			
			daemonThread = null;
			runFlag = null;

			battleUtil.destroy();
			battleUtil = null;
		}
	}

	
	/**
	 * Do battle between two robots, update record.
	 * @param robot1
	 * @param robot2
	 */
	private synchronized static void doRankingBattle(String robot1, String robot2){
		
		// --- SYNC ---
		//actually we should lock the robot identity here.
		//if during this time, the robot is deleted, the robot can not be found.
		//Robot not found is acceptable, which just means this battle is wasted.
		//Sync will cause a big performance issue, if the battle
		//is taking very often.
		
		BattleResult result = battleUtil.doBattle(robot1, robot2);
		
		if(!result.isValid() || result.getExceptionType() != BattleResult.EXCEPTION_NONE)
			Logger.log("> " + robot1 + " vs " + robot2 + ". R=" + result.getWinner() + ", E=" + result.getExceptionType());

		RobotDataCentre.updateBattleResult(result);
		
		// --- End of SYNC ---
		
		ServerInfo.instance().addBattleCount();
	}

	/**
	 * Thread to schedule battles. Run always.
	 *
	 */
	static class DaemonRunnable implements Runnable {

		public void run() {
			
			Logger.log("BattleDaemon.DaemonThread start.");
			
			//sleep some before actual start.
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
			}
			
			while(!quit){
				
				//infinitely wait for run flag until runFlag is true.
				if(!runFlag.waitFlag(true, 0)){
					//we're interrupted	or flag is false.
					
					//check quit in case we're interrupted
					if(quit)
						break;
					
					//check if we're running and wait again.
					if(!runFlag.get())
						continue;
				}
				
				//get robot pair
				String[] robotPair = new String[2];
				RobotDataCentre.popNextBattlePair(robotPair);
				String robot1 = robotPair[0];
				String robot2 = robotPair[1];
				
				//if we have not enough robots, stop.
				if(robot1 == null || robot2 == null){
					Logger.log("Not enough robots.");
					setRunFlag(false);
					continue;
				}
				
				//do battle.
				BattleDaemon.doRankingBattle(robot1, robot2);
				
				//wait some time.
				try {
					Thread.sleep(IDLE_INTERVAL);
				} catch (InterruptedException e) {
				}
			}
			
			Logger.log("BattleDaemon.WrokerThread ended normally.");
		}
	}

	/**
	 * Set flag to indicate whether we should run or not.
	 * @param start
	 * @return
	 */
	synchronized static boolean setRunFlag(boolean start) {
		//if we're already in this status, say we've done.
		if(runFlag.get() == start)
			return start;
		
		runFlag.setAndNotify(start);
				
		Logger.log("BattleDaemon.setRunFlag: " + start);
		
		return runFlag.get();
	}
	
	synchronized static int getStatus() {
		if(quit)
			return STATUS_END;
		if(runFlag.get())
			return STATUS_RUNNING;
		return STATUS_STOPPED;
	}
	
	synchronized static BattleUtil getBattleUtil(){
		return battleUtil;
	}
}
