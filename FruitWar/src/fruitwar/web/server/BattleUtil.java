package fruitwar.web.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import fruitwar.core.BattleResult;
import fruitwar.supp.FruitWarLoader;
import fruitwar.util.Logger;
import fruitwar.util.SyncFlag;
import fruitwar.util.SysCmd;
import fruitwar.web.server.tempauthutil.AuthUtil;

/**
 * An utility to do a battle. 
 * The battle is done in a sub process by a user with limited authority.
 * 
 * 
 * The first version of this utility uses the following pattern:
 * "swap user -> create sub process -> swap user back" 
 * But there's a big problem that the "swap back" is not guaranteed to
 * succeed on some OS (IBM i in my case). So the 2nd version is introduced:
 * A Low Authority Thread (LAT) is involved, which is always running
 * with the limited authority user, and spawns sub processes when task
 * arrives. This implementation requires only 1 swap and no swap back.
 * 
 *  
 * @author wangnan
 *
 */

class BattleUtil {
	
	/**
	 * Time limit of wait before the battle process finishes.
	 */
	private static final int SUB_PROCESS_TIME_LIMIT = 30000; 

	private LowAuthorityThread lat;
	
	BattleUtil(){
		init();
	}
	
	private void init(){
		lat = new LowAuthorityThread();
		
		//normally starting thread in c'tor is deprecated because "this" 
		//is exposed before it's ready. But it's OK in our case because 
		//the LAT is a static class and knows nothing about "this".
		lat.start();
	}
	
	/**
	 * We always need "destroy" method because this is a Web application
	 * and we should provide a way to stop it normally.
	 */
	synchronized void destroy(){
		if(lat != null){
			lat.quit = true;
			lat.interrupt();
			try{
				lat.join();
			}catch(InterruptedException e){
			}
			lat = null;
		}
	}
	
	private int doTask(String cmd, PrintWriter out, PrintWriter err){
		
		//there's another problem in swapping user. On some OS
		//(my case IBM i), the swapping has a time limit.
		//Check the limit and recreate the LAT if necessary.
		if(AuthUtil.isExpired()){
			destroy();
			init();
		}
		
		return lat.doTask(cmd, out, err);
	}
	
	synchronized BattleResult doBattle(String robot1, String robot2){
		
		//java -classpath ./fruitwar.jar:./robots/$1.jar:./robots/$2.jar fruitwar.FruitWarLoader ./robots/$1.jar ./robots/$2.jar
		String robotFile1 = ServerConfig.getRobotPath() + robot1 + ".jar"; 
		String robotFile2 = ServerConfig.getRobotPath() + robot2 + ".jar";
		
		String classpath = 
			ServerConfig.basePath + "fruitwar.jar" + File.pathSeparatorChar + 
			robotFile1 + File.pathSeparatorChar +
			robotFile2;
		String cmd = "java -Xms16m -Xmx64m -classpath " + classpath + " "
			+ FruitWarLoader.class.getName() + " "
			+ robotFile1 + " " + robotFile2 
			+ " verbose";
		
		
		//prepare message receiver.
		//TODO
		//err will be discarded. Better to log them. 
		StringWriter swOut = new StringWriter();
		PrintWriter out = new PrintWriter(swOut);
		StringWriter swErr = new StringWriter();
		PrintWriter err = new PrintWriter(swErr);
		
		int ret = doTask(cmd, out, err);
		
		BattleResult result = new BattleResult(robot1, robot2, false);
		
		boolean ok = ret == 0;
		
		if(ok){
			//success!
			ok = result.loadFromString(swOut.toString());
		}
		
		if(!ok){
			//error occured.
			Logger.log("Error occured executing command. ret: " + ret + ", cmd=" + cmd);
			String stdout = "Out: " + swOut.toString();
			Logger.log(stdout);
			String stderr = "Err: " + swErr.toString();
			Logger.log(stderr);
			result.setUnknownException(stdout + "\n" + stderr);
		}
		
		try {
			swOut.close();
		} catch (IOException e) {}
		try {
			swErr.close();
		} catch (IOException e) {}
		
		return result;
	}
	

	
	/**
	 * The LAT.
	 * 
	 * @author wangnan
	 *
	 */
	private static class LowAuthorityThread extends Thread {
		
		volatile boolean quit = false;
		SyncFlag flagHasTask = new SyncFlag();
		volatile String cmd;
		volatile PrintWriter out;
		volatile PrintWriter err;
		volatile int ret;
		
		LowAuthorityThread(){
			super("Fruit War LAT");
		}
		
		public void run(){
			Logger.log("BattleUtil.LAT start.");
			
			AuthUtil.swapToRestrictedUser();
			
			while(!quit){
				if(!task())
					break;
			}
			
			//this is required in case there's resource recycle task
			//in it.
			AuthUtil.swapToServerUser();
			
			Logger.log("BattleUtil.LAT end.");
		}
		
		private boolean task(){
			//wait until we have a task
			if(!flagHasTask.waitFlag(true, 0))
				return false;
			
			ret = -1;
			try{
				ret = SysCmd.exec(new SysCmd.CmdWrapper(cmd, out, err), SUB_PROCESS_TIME_LIMIT);
			}catch(Throwable t){
				t.printStackTrace();
			}

			//we're done
			cmd = null;
			out = null;
			err = null;
			
			flagHasTask.setAndNotify(false);
			
			return true;
		}
		
		int doTask(String cmd, PrintWriter out, PrintWriter err){
			if(flagHasTask.get())
				throw new RuntimeException("Should assert: How could this be?");
			
			this.cmd = cmd;
			this.out = out;
			this.err = err;
			
			//notify worker thread that it has a new task.
			flagHasTask.setAndNotify(true);
			
			//wait until the work thread says that it has no task
			flagHasTask.waitFlag(false, 0);
			
			return ret;
		}
	}
}
