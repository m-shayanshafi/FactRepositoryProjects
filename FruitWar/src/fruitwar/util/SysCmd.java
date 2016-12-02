package fruitwar.util;

import java.io.*;

/**
 * Utility class to run a system command, with:
 * 	blocking call.
 * 	blocking call with customized output stream and error stream.
 * 	blocking call with time limit.
 * 
 * @author wangnan
 *
 */
public class SysCmd {

	/**
	 * For passing parameters to Runtime.exec.
	 * 
	 *
	 */
	public static class CmdWrapper{
		//Union. Only one is available
		String[] cmds = null;
		String cmd = null;
		
		//
		String[] envp = null;
		File dir = null;
		
		PrintWriter out = new PrintWriter(System.out);
		PrintWriter err = new PrintWriter(System.err);
		
		public CmdWrapper(){
		}
		
		public CmdWrapper(String cmd){
			this.cmd = cmd;
		}
		
		public CmdWrapper(String cmd, PrintWriter out, PrintWriter err){
			this.cmd = cmd;
			this.out = out;
			this.err = err;
		}

		public CmdWrapper(String[] cmds) {
			this.cmds = cmds;
		}
	}
	
	/**
	 * Helper to retrieve output
	 * @author wangnan
	 *
	 */
	private static class StreamGobbler extends Thread{
		InputStream is;
		PrintWriter out;
		
		StreamGobbler(InputStream is, PrintWriter out){
			this.is = is;
			this.out = out;
		}

		public void run(){
			InputStreamReader isr = null;
			BufferedReader br = null;
			try{
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line = null;
				while((line = br.readLine()) != null){
					if(out != null){
						out.println(line);
						out.flush();
					}
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}finally{
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {}
				}
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {}
				}
			}
		}
	}


	/**
	 * Run the given system command.
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int exec(String cmd) throws IOException, InterruptedException{
		return exec(new CmdWrapper(cmd));
	}
	
	/**
	 * Run the given system command. 
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */	
	public static int exec(CmdWrapper cmd) throws IOException, InterruptedException{
		return _exec(cmd, null);
	}

	/**
	 * Execute the given command, wait at most timeLimit milliseconds.
	 * If timeLimit is exceeded, try to kill the sub process.
	 * 
	 * @param cmd The command to run
	 * @param timeout The time to wait, in milliseconds.
	 * @return
	 */
	public static int exec(String cmd, long timeLimit) {
		return exec(new CmdWrapper(cmd), timeLimit);
	}
	
	/**
	 * Execute the given command, wait at most timeLimit milliseconds.
	 * If timeLimit is exceeded, try to kill the sub process.
	 * 
	 * @param cmd The command to run
	 * @param timeout The time to wait, in milliseconds.
	 * @return
	 */
	public static int exec(CmdWrapper cmd, long timeLimit) {
		
		//start the thread to run the command
		WorkerRunnable runner = new WorkerRunnable(cmd);
		Thread t = new Thread(runner, "SysCmd runner");
		t.start();
		
		//wait for the work done
		if(!runner.workDone.waitFlag(true, timeLimit)){
			
			//Timeout.
			//Interrupt the runner thread. 
			//kill signal will be sent to sub-process
			t.interrupt();
			if(runner.exception == null)
				runner.exception = new Exception(
					"Time out executing command: " + cmd);
		}
		
		return runner.ret;
	}
	

	private static int _exec(CmdWrapper cmd, Process[] processHook) throws IOException, InterruptedException{
		Runtime rt = Runtime.getRuntime();
		Process proc;
		if(cmd.cmd != null)
			proc = rt.exec(cmd.cmd, cmd.envp, cmd.dir);
		else
			proc = rt.exec(cmd.cmds, cmd.envp, cmd.dir);
		
		//set hook
		if(processHook != null && processHook.length > 0)
			processHook[0] = proc;
		
		//Consume error and output to avoid blocking
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), cmd.err);
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), cmd.out);

		//kick them off
		errorGobbler.start();
		outputGobbler.start();

		//wait for the process done
		int ret = proc.waitFor();
		
		errorGobbler.join();
		outputGobbler.join();
		
		return ret;
	}
		
	/**
	 * Thread to run system command for time out mode.
	 * @author wangnan
	 *
	 */
	private static class WorkerRunnable implements Runnable{

		CmdWrapper cmd;
		Exception exception;
		int ret = -101;
		Process[] processHook = new Process[1];
		SyncFlag workDone = new SyncFlag();
		
		WorkerRunnable(CmdWrapper cmd){
			this.cmd = cmd;
		}
		
		public void run() {
			try {
				//do the actual job
				ret = _exec(cmd, processHook);
				
				cmd.out.flush();
				cmd.err.flush();
				
				//notify that we're done.
				workDone.setAndNotify(true);
				return;
			} catch (IOException e) {
				e.printStackTrace(cmd.err);
				exception = e;
				ret = -103;
			} catch (InterruptedException e) {
				//interrupted. Kill sub process
				Process proc = processHook[0];
				if(proc != null){
					proc.destroy();
				}
				ret = -102;
			}
			
			cmd.out.flush();
			cmd.err.flush();
			
			//we've done, with failure
			workDone.setAndNotify(false);
		}
	}
	
	//*
	//test time limit mode, whether the sub process is killed.
	public static void main(String[] args) throws InterruptedException{
		
		System.out.println("Test inexist program. This should return immediately with exception.");
		try{
			System.out.println(exec("d:/inexist.exe"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Test inexist program with timelimit mode. This should return immediately.");
		System.out.println(exec("d:/inexist.exe", 10000));
		
		System.out.println("Test no responding program. This will open a notepad, and kill it in 10 seconds.");
		System.out.println(exec("notepad.exe", 10000));
	}
	//*/
}
