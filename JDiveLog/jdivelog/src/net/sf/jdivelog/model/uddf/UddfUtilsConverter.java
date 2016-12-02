package net.sf.jdivelog.model.uddf;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**Utils class to types convertions
 * 
 * @author Levtraru
 *
 */
public class UddfUtilsConverter {
	private UddfUtilsConverter(){
		
	}
	private static  UddfUtilsConverter instance;
	
	public static UddfUtilsConverter getInstance(){
		if (instance==null){
			instance= new UddfUtilsConverter();
		}
		return instance;
	}
	
	/**
	 * Returns a Date from year, month day Strings
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
    public Date getDate(String y, String m, String d) {
        GregorianCalendar gc = new GregorianCalendar();
        if (y != null && m != null && d != null && !"".equals(y) && !"".equals(m) && !"".equals(d)) {
	        int year = Integer.parseInt(y);
	        int month = Integer.parseInt(m);
	        int day = Integer.parseInt(d);
	        gc.set(Calendar.YEAR, year);
	        gc.set(Calendar.MONTH, month-1);
	        gc.set(Calendar.DAY_OF_MONTH, day);
        }
        return gc.getTime();
    }	
    /**Returns a Date from hour, minute Strings
     * 
     * @param hour
     * @param minute
     * @return
     */
    public Date getTime(String hour, String minute) {
        GregorianCalendar gc = new GregorianCalendar();
        if (hour != null && minute != null && !"".equals(hour) && !"".equals(minute)) {
            gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            gc.set(Calendar.MINUTE, Integer.parseInt(minute));
        }
        return gc.getTime();
    }

    /**Converts an String to Double
     * 
     * @param str
     * @return
     */
	public Double convertToDouble(String str) {
        if (str == null || "".equals(str.trim())) {
            return null;
        } else {
            return new Double(str);
        }
    }    
}
