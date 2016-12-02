/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatusBar.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import net.sf.jdivelog.gui.statusbar.LedCanvas.LedShape;


/**
 * The StatusBar on bottom of MainWindow.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class StatusBar extends JPanel implements StatusInterface {
    
    private static final long serialVersionUID = -8373083612057228448L;
    private LedCanvas sendLed;
    private LedCanvas receiveLed;
    private JProgressBar progressBar;
    private JTextField messageField;
    private boolean showInPercent;
    private int maxCount;
    private int count;
    private int percent;
    private long duration = 1000;
    private ProgressThread progressThread;

    public StatusBar() {
        setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout());
        innerPanel.add(getSendLed());
        innerPanel.add(getReceiveLed());
        innerPanel.add(getProgressBar());
        
        add(innerPanel, BorderLayout.WEST);
        add(getMessageField(), BorderLayout.CENTER);
    }

    public void commSend() {
        getSendLed().flash();
    }

    public void commReceive() {
        getReceiveLed().flash();
    }

    public void messageError(String message) {
        Font f = getMessageField().getFont();
        f = f.deriveFont(Font.BOLD);
        getMessageField().setFont(f);
        getMessageField().setDisabledTextColor(Color.RED);
        getMessageField().setText(message);
    }

    public void messageWarn(String message) {
        Font f = getMessageField().getFont();
        f = f.deriveFont(Font.BOLD);
        getMessageField().setFont(f);
        getMessageField().setDisabledTextColor(Color.RED);
        getMessageField().setText(message);
    }

    public void messageInfo(String message) {
        Font f = getMessageField().getFont();
        f = f.deriveFont(Font.BOLD);
        getMessageField().setFont(f);
        getMessageField().setDisabledTextColor(Color.BLACK);
        getMessageField().setText(message);
    }

    public void messageClear() {
        getMessageField().setText("");
    }

    public void infiniteProgressbarStart() {
        if (progressThread == null) {
            progressThread = new ProgressThread();
            progressThread.setPriority(Thread.MIN_PRIORITY);
            progressThread.start();
        }
    }

    public void infiniteProgressbarEnd() {
        if (progressThread != null) {
            progressThread.stopRunning();
        }
    }

    public void countingProgressbarStart(int maxCount, boolean showInPercent) {
        this.maxCount = maxCount;
        this.showInPercent = showInPercent;
        getProgressBar().setMaximum(maxCount);
        getProgressBar().setStringPainted(true);
        count = 0;
        percent = 0;
        if (showInPercent) {
            getProgressBar().setMaximum(10);
            getProgressBar().setString(count +" %");
        } else {
            getProgressBar().setValue(count);
            getProgressBar().setString(count+" / "+maxCount);
        }
        repaintProgressBar();
    }

    public void countingProgressbarIncrement() {
        count++;
        if (showInPercent) {
            int newPercent = 100*count/maxCount;

            if (newPercent > percent) {
                percent = newPercent;
                getProgressBar().setValue(percent / 10);
                getProgressBar().setString(percent+" %");
                repaintProgressBar();
            }
        } else {
            getProgressBar().setValue(count);
            getProgressBar().setString(count+" / "+maxCount);
            repaintProgressBar();
        }
    }

    public void countingProgressbarEnd() {
        getProgressBar().setValue(0);
        getProgressBar().setString("");
        getProgressBar().setStringPainted(false);
        repaintProgressBar();
    }
    
    private LedCanvas getSendLed() {
        if (sendLed == null) {
            sendLed = new LedCanvas(LedShape.RECTANGLE);
            sendLed.setPreferredSize(new Dimension(15, 10));
        }
        return sendLed;
    }
    
    private LedCanvas getReceiveLed() {
        if (receiveLed == null) {
            receiveLed = new LedCanvas(LedShape.RECTANGLE);
            receiveLed.setPreferredSize(new Dimension(15, 10));
        }
        return receiveLed;
    }
    
    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
        }
        return progressBar;
    }
    
    private JTextField getMessageField() {
        if (messageField == null) {
            messageField = new JTextField();
            messageField.setEnabled(false);
            messageField.setPreferredSize(new Dimension(1000, 20));
        }
        return messageField;
    }
    
    private void repaintProgressBar() {
        getProgressBar().paint(getProgressBar().getGraphics());
    }
    
    //
    // inner classes
    //
    
    private class ProgressThread extends Thread {
        
        private boolean running;
        private int count;
        
        public ProgressThread() {
            running = true;
            count = 0;
        }
        
        @Override
        public void run() {
            getProgressBar().setString("");
            getProgressBar().setStringPainted(false);
            getProgressBar().setMaximum(10);
            while (running) {
                getProgressBar().setValue(count);
                repaintProgressBar();
                count = (count + 1) % 11;
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    // stop progressbar
                    running = false;
                }
            }
            getProgressBar().setValue(0);
            repaintProgressBar();
            progressThread = null;
        }
        
        public void stopRunning() {
            running = false;
        }
    }

}
