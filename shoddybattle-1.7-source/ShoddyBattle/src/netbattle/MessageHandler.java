/*
 * MessageHandler.java
 *
 * Created on December 20, 2006, 7:01 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package netbattle;
import java.net.*;
import java.io.*;
import netbattle.messages.NetMessage;
import java.util.*;
import shoddybattle.util.ThreadedQueue;

/**
 * Processes input from a socket.
 * @author Colin
 */
public abstract class MessageHandler extends Thread {
    
    protected Socket m_socket;
    protected ObjectInputStream m_input;
    protected ObjectOutputStream m_output;
    private volatile boolean m_running = true;
    private ThreadedQueue m_queue = new ThreadedQueue(
        new ThreadedQueue.QueueDelegate() {
            /** The goal here is to allow all threads to post messages to this
             *  client without the possibility of crashing the application if one
             *  client lags in the sending of the message.
             */
            public void handleItem(Object message) {
                /** Write the message to the socket's output stream. */
                try {
                    synchronized (m_output) {
                        if (message instanceof NetMessage) {
                            m_output.writeObject(message);
                        } else if (message instanceof Runnable) {
                            ((Runnable)message).run();
                        }
                    }
                } catch (IOException e) {
                    informWriteError(e);
                }
            }
        });
    
    /**
     * Prevents creation of MessageHandler externally.
     */
    protected MessageHandler() {
        m_queue.start();
    }
    
    protected boolean isRunning() {
        return m_running;
    }
    
    protected void stopRunning() {
        m_running = false;
        m_queue.stop();
    }
    
    /**
     * Exexcute a message.
     */
    protected abstract void executeMessage(NetMessage msg);
    
    /**
     * Inform of a read error.
     */
    protected abstract void informReadError(Throwable e);
    
    /**
     * Inform of a write error.
     */
    protected void informWriteError(IOException e) {
        
    }
    
    /**
     * Send a runnable message.
     */
    public void sendMessage(Runnable message) {
        m_queue.post(message);
    }
    
    /**
     * Send a message.
     */
    public void sendMessage(NetMessage message) {
        m_queue.post(message);
    }
    
    /**
     * Pull a message off the queue, with the default timeout.
     */
    public synchronized NetMessage getNextMessage() throws IOException, ClassNotFoundException {
        return (NetMessage)m_input.readObject();
    }
    
    /**
     * Pull a message off the queue, with the specified timeout.
     */
    public synchronized NetMessage getNextMessage(int timeout) throws IOException, ClassNotFoundException {
        m_socket.setSoTimeout(timeout);
        NetMessage msg;
        try {
            msg = (NetMessage)m_input.readObject();
        } catch (SocketTimeoutException e) {
            msg = null;
        }
        m_socket.setSoTimeout(0);
        return msg;
    }
    
    public void run() {
        while (m_running) {
            NetMessage message = null;
            try {
                message = getNextMessage();
            } catch (Throwable e) {
                e.printStackTrace();
                informReadError(e);
                return;
            }
            try {
                executeMessage(message);
            } catch (Throwable e) {
                /** This is the top level error handler of the application, so
                 *  catching Throwable here is not really that bad. */
                //e.printStackTrace();
            }
        }
    }
    
}
