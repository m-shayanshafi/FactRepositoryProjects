package fruitwar.core;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;
import fruitwar.util.SyncFlag;

/**	
 * Robots are run in separate thread, to check whether there're deadlock
 * or timeout in robot.
 * 
 * 
 * @author wangnan
 *
 */
class RobotRunner {

	IFruitWarRobot robot;
	
	/**
	 * WorkFlag:
	 * true means a work should be done but not.
	 * false means a work is not there or has been done.
	 * 
	 */
	private SyncFlag pendingWorkStrategy = new SyncFlag();
	private SyncFlag pendingWorkResult = new SyncFlag();
	
	private WorkerThread workerThread;
	
	private volatile IFruitThrower[] throwers;
	private volatile EnemyInfo[] enemyInfo;
	
	
	public RobotRunner(IFruitWarRobot robot){
		this.robot = robot;
	}
	
	public void start(){
		workerThread = new WorkerThread();
		workerThread.start();
	}

	public void strategy(IFruitThrower[] throwers) throws RobotException {
		//set data to be processed
		this.throwers = throwers;
		
		asyncMonitor(pendingWorkStrategy);
	}

	public void notifyResult(EnemyInfo[] enemyInfo) throws RobotException {
		
		this.enemyInfo = enemyInfo;
		
		asyncMonitor(pendingWorkResult);
	}
	
	/**
	 * This function asynchronously execute an operation, and monitor its
	 * execution time. If the function takes too long or error is thrown by
	 * the function, this function throws exceptions.
	 * 
	 * In detail, this function firstly set the given work flag to true 
	 * (let the work start), and wait it be changed to false (done by worker
	 * thread).
	 *  
	 * @param starterLock
	 * @param doneSignal
	 * @throws Throwable
	 */
	private void asyncMonitor(SyncFlag workFlag) throws RobotException{
		
		//if there're pending work... this should never happen
		if(workFlag.get())
			throw new RuntimeException("How could this be?");
		
		
		workFlag.setAndNotify(true);
		
		if(!workFlag.waitFlag(false, Rules.ROBOT_CALCULATION_TIMEOUT())){
			//time out
			stop();
			String functionName = "";
			if(workFlag == pendingWorkStrategy)
				functionName = "IFruitWarRobot.strategy";
			else if(workFlag == pendingWorkResult)
				functionName = "IFruitWarRobot.result";
			throw new RobotException(
					"Robot timeout. Function=" + functionName + ". It can not take longer than " + Rules.ROBOT_CALCULATION_TIMEOUT() + " milliseconds.");
		
		}
		
		//check whether there's error from worker thread
		if(workerThread.error != null){
			throw new RobotException("Exception occurs in robot.", workerThread.error);
		}
	}
	
	/**
	 * Stop this runner and try to clean up the worker thread.
	 * Note that if there's problem (e.g. deadlock) in the bound
	 * robot, the worker thread may not be cleaned up.
	 */
	void stop(){
		//if we've already stopped...
		if(workerThread == null)
			return;
		
		//set stop flag
		pendingWorkStrategy.cancel();
		pendingWorkResult.cancel();
		
		//we just yield, and do not explicitly wait for the 
		//worker to finish it work. It should finish at once due
		//to the simplicity of logic. If it's not finished, 
		//probably there's problem in the corresponding robot.
		//We can not expect a definite finish of that. Simple out.
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}

		//this is useless in most of the cases...
		//if the thread is still alive, interrupt it
		if(workerThread.isAlive()){
			workerThread.interrupt();
		}
		
		//at this point, there's still problem that if the thread
		//has a dead loop, it will not be cleaned up. Simply out.
		
		workerThread = null;	//set complete
	}

	class WorkerThread extends Thread {
		
		private Throwable error = null;
		volatile boolean finish = false;
		
		public void run() {
			
			while(error == null){
				
				//wait for a new strategy task, infinitely.
				if(!pendingWorkStrategy.waitFlag(true, 0))
					break;	//canceled. stop.
				
				//do strategy
				try{
					robot.strategy(throwers);
				}catch(Throwable t){
					error = t;
				}
				
				//tell runner that we've done
				pendingWorkStrategy.setAndNotify(false);

				//if error occurs, out.
				if(error != null)
					break;
				
				//wait for a new Result task
				if(!pendingWorkResult.waitFlag(true, 0))
					break;	//canceled. stop.
				
				//actual work: notify result
				try{
					robot.result(enemyInfo);
				}catch(Throwable t){
					error = t;
				}
				
				//set falg: we've done.
				pendingWorkResult.setAndNotify(false);
			}
			
			finish = true;
		}
	}
}
