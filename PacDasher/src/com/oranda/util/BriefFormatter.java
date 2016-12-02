package com.oranda.util;

import java.text.*;
import java.util.Date;
import java.util.logging.*;


public class BriefFormatter extends Formatter 
{
    public String format(LogRecord rec) 
    {
        StringBuffer buf = new StringBuffer(1000);
        buf.append(Str.abbrevFQClassname(rec.getSourceClassName()) + " " 
                + rec.getSourceMethodName());
        buf.append(": ");
        buf.append(formatMessage(rec));
        buf.append("\n");
        //System.out.println("BriefFormatter: " + buf.toString());
        return buf.toString();
    }
    
    // Called just after the handler for this formatter is created
    public String getHead(Handler h) 
    {
        Date now = new Date();
        StringBuffer buf = new StringBuffer();
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.format(now, buf, new FieldPosition(0));
        return "Starting logging at " + buf.toString() + "\n";
    }
    
    // Called just after the handler for this formatter is closed
    public String getTail(Handler h) {
        return "";
    }
}


