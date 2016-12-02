package fruitwar.web.server;

import fruitwar.util.Logger;

/**
 * Daemon thread to drive flush of server data periodically.
 * 
 * @author wangnan
 *
 */
class DataFlusherDaemon {
	
	/**
	 * Time interval to flush data
	 */
	public static final int DATA_FLUSH_INTERVAL = 5 * 60 * 1000;
	
	private static Thread workerThread = null;
	private static volatile boolean quit;
	
	static void init(){
		
		Logger.log("Entry.");
		
		quit = false;
		
		workerThread = new Thread("Fruit War Data flusher"){
			public void run(){
				
				Logger.log("DataFlusherDaemon.thread start.");
				
				while(!quit){
					try {
						Thread.sleep(DATA_FLUSH_INTERVAL);
					} catch (InterruptedException e) {
						break;
					}
					
					flush();
				}
				
				Logger.log("DataFlusherDaemon.thread end.");
			}
		};
		
		workerThread.start();
		
		Logger.log("Exit.");
	}
	
	static void destroy(){
		Logger.log("Entry.");
		
		quit = true;
		workerThread.interrupt();
		
		try {
			workerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Logger.log("Exit.");
	}
	
	
	private static void flush(){
		//save server info
		ServerInfo.instance().save();

		//save RobotDataCentre
		//RobotDataCentre.save();
	}
}
