/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatusInterface.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui.statusbar;

/**
 * The StatusInterface provides the mechanisms to inform Users about ongoing events.
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public interface StatusInterface {
    
    /**
     * should be called when data is sent through a network or comm port.
     */
    public void commSend();
    
    /**
     * should be called when data is received from a network or comm port.
     */
    public void commReceive();
    
    /**
     * should be called if the user should be notified about an error which has occured.
     * @param message the message to show to the user.
     */
    public void messageError(String message);
    
    /**
     * should be called on problems occured, but can be handled. 
     * @param message the message to show to the user.
     */
    public void messageWarn(String message);
    
    /**
     * can be called on any information what is happening in the system. should be called periodically while progressbar is running.
     * @param message the message to show to the user.
     */
    public void messageInfo(String message);

    /**
     * should be called after an operation has been done.
     * clears all status messages.
     */
    public void messageClear();
    
    /**
     * shows a progressbar which has no defined endpoint, just to signal "there's still runnning something" to the user. 
     */
    public void infiniteProgressbarStart();
    
    /**
     * stops the progressbar, should be called on completion of a task.
     */
    public void infiniteProgressbarEnd();
    
    /**
     * starts a preogressbar with a defined endpoint
     * @param maxCount maximum value at end.
     * @param showInPercent <code>true</code> for displaying progress inpercent, <code>false</code> for displaying 1/15, 2/15, ...
     */
    public void countingProgressbarStart(int maxCount, boolean showInPercent);

    /**
     * increments the counter of the progressbar. shows 1/8, 2/8, ... or 10%, 20%, ...
     */
    public void countingProgressbarIncrement();
    
    /**
     * stops the progressbar, should be called on completion of a task.
     */
    public void countingProgressbarEnd();

}
