package fruitwar.web.util;


/**
 * 
 * @author wangnan
 *
 */
public class TimeInterval {

	long interval;
	
	/**
	 * Time interval. Initialized by start/end time in milliseconds
	 * @param start
	 * @param end
	 */
	public TimeInterval(long start, long end){
		interval = end - start;
	}
	
	public TimeInterval(long start){
		interval = System.currentTimeMillis() - start;
	}
	
	public String toString(){
		String s = "";
		long tmp = interval;
		
		final int ONE_SECOND = 1000;
		final int ONE_MINUTE = ONE_SECOND * 60;
		final int ONE_HOUR = ONE_MINUTE * 60;
		final int ONE_DAY = ONE_HOUR * 24;
		
		long n = tmp / ONE_DAY;
		if(n > 0)
			s += n + "d ";

		tmp %= ONE_DAY;
		n = tmp / ONE_HOUR;
		if(n > 0 || s.length() > 0)
			s += n + "h ";
		
		tmp %= ONE_HOUR;
		n = tmp / ONE_MINUTE;
		if(n > 0 || s.length() > 0)
			s += n + "m ";
		
		tmp %= ONE_MINUTE;
		n = tmp / ONE_SECOND;
		if(n > 0 || s.length() > 0)
			s += n + "s ";
		
		tmp %= ONE_SECOND;
		n = tmp / ONE_SECOND;
		if(n > 0 || s.length() > 0)
			s += n + "mi";
		
		return s;
	}
}
