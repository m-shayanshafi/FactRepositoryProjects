package fruitwar.util;

import java.io.PrintStream;
import java.util.Date;

public class Logger {
	
	private static final String thisClass = Logger.class.getName();
	private static PrintStream out = System.err;
	
	public static void error(String msg){
		log("ERROR: " + msg);
	}
	
	public static void exception(Exception e){
		e.printStackTrace(out);
	}
	
	public static void logSimple(String msg){
		out.println(msg);
	}
	
	public static void log(String msg) {
        String cname = null;
        String method = "";
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        // First, search back to a method in the Logger class.
        int ix = 0;
        while (ix < stack.length)
        {
            StackTraceElement frame = stack[ix];
            cname = frame.getClassName();
            if (cname.equals(thisClass))
            {
                break;
            }
            ix++;
        }
        // Now search for the first frame before the "Logger" class.
        while (ix < stack.length)
        {
            StackTraceElement frame = stack[ix];
            cname = frame.getClassName();
            if (!cname.equals(thisClass))
            {
                // We've found the relevant frame.
                method = frame.getMethodName();
                break;
            }
            ix++;
        }            
        
        
        //logger.logp(level, cname, method, msg);
        logp(msg + "	::" + cname + "." + method 
        		+ " Thread=" + Thread.currentThread().getName() 
        		+ " ::"	+ Thread.currentThread().getClass().getName() );
	}
	
	private static void logp(String msg){
		Date d = new Date();
		out.println(d.toString() + " - " + msg);
	}
}
