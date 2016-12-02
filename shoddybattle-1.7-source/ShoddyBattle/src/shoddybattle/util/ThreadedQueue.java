/*
 * ThreadedQueue.java
 *
 * Created on July 27, 2007, 12:50 AM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
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
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattle.util;
import java.util.*;

/**
 * Java 1.5 has this built in as java.util.BlockingQueue, but the version we
 * are using - 1.4 - does not. Here I roll my own simple version.
 * 
 * @author Colin
 */
public class ThreadedQueue {
    private final List m_queue = Collections.synchronizedList(new LinkedList());
    private final QueueDelegate m_delegate;
    private volatile boolean m_running = false;
    private final Thread m_poster = new Thread(new Runnable() {
        /** This is the main logic of the ThreadedQueue.
         */
        public void run() {
            try {
                while (m_running) {
                    /** This logic seems to be redundant but it is actually
                     *  required to be exactly this way or a deadlock will
                     *  result. */
                    synchronized (m_queue) {
                        if (m_queue.size() == 0) {
                            do {
                                // Wait for another thread to post us a message.
                                m_queue.wait();
                            } while (m_queue.size() == 0);
                        }
                    }
                    /** If we get here, the queue is not empty.
                     *  Take off the first message in the queue. */
                    Object message = m_queue.remove(0);
                    /** Process the message using the QueueDelegate. */
                    m_delegate.handleItem(message);
                }
            } catch (InterruptedException e) {
                // Allow this thread to be killed in a safe manner.
            }
        }
    });
    
    /** Callback used to handle items in the queue as they are pulled off. */
    public static interface QueueDelegate {
        /** @param o the item to handle */
        public void handleItem(Object o);
    }
    
    /** Creates a new instance of ThreadedQueue.
     *  @param delegate object to invoke for each item in the queue
     */
    public ThreadedQueue(QueueDelegate delegate) {
        m_delegate = delegate;
    }
    
    /** Start the threaded queue. */
    public void start() {
        if (!m_running) {
            m_running = true;
            m_poster.start();
        }
    }
    
    /** Stop the threaded queue. */
    public void stop() {
        if (m_running) {
            m_running = false;
            m_poster.interrupt();
        }
    }
    
    /** Just in case we forget to stop the queue, this method will save us. */
    public void finalize() throws Throwable {
        stop();
    }
    
    /** Post a message to the threaded queue. */
    public void post(Object o) {
        m_queue.add(o);
        synchronized (m_queue) {
            m_queue.notify();
        }
    }

}
