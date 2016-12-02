package fruitwar.util;

/**
 * If we use jdk 1.5, we'll have Semaphore...
 * 
 * This is something like one-count semaphore, but somehow different.
 * 
 * java.util.concurrent.SynchronousQueue
 * 
 * 
 * @author wangnan
 *
 */
public class SyncFlag {
	private volatile boolean value = false;
	private boolean cancel = false;

	
	public synchronized void cancel(){
		cancel = true;
		notifyAll();
	}
	
	/**
	 * Change the value and notify ALL objects calling "waitFlag"
	 * @param val
	 */
	public synchronized void setAndNotify(boolean val){
		value = val;
		notifyAll();
	}
	
	public boolean get(){
		return value;
	}
	
	/**
	 * wait for the value become the expected one.
	 * Return false if timeout or interrupted for simplicity.
	 */
	public synchronized boolean waitFlag(boolean expected, long timeout){
		if(cancel)
			return false;
		
		if(value == expected)
			return true;
		
		//java doc of Object.wait says that, this method should be always used in the 
		//following format to avoid *spurious wake up*. 
		//	synchronized (obj) {
		//		while (<condition does not hold>)
		//			obj.wait();
		//		... // Perform action appropriate to condition
		//	}
		//
		//But...it's a bit hard for us to comply with that. The 
		//condition we're waiting is:
		// "Time out" or "value has been changed by a thread EXCLUDE this"
		//
		//Leave this a TODO issue...After more than 60 days (CPU time) test,
		//no problem has been met, on Windows & OS400.
		//
		
		try {
			wait(timeout);
		} catch (InterruptedException e) {
			return false;	//interrupted.
		}
		
		if(cancel)
			return false;
		
		return value == expected;
	}
}
