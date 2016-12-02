/* RWLock.java
 
   A simple read-write lock listed on book JAVA Threads, page 182 , 
   author Scott Oaks and Henry Wong, publisher O'REILLY (c)1997

   Modified by Yu Zhang (c)2004
 
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.common;

import java.util.*;

class RWNode {
	static final int READER = 0;
	static final int WRITER = 1;
	Thread t;
	int state;
	int nAcquires;
	RWNode(Thread t, int state) {
		this.t = t;
		this.state = state;
		nAcquires = 0;
	}
}

public class RWLock {
	private Vector waiters;

	private int firstWriter() {
		Enumeration e;
		int index;
		for (index = 0, e = waiters.elements(); e.hasMoreElements(); index++) {
			RWNode node = (RWNode) e.nextElement();
			if (node.state == RWNode.WRITER)
				return index;
		}
		return Integer.MAX_VALUE;
	}

	private int getIndex(Thread t) {
		Enumeration e;
		int index;
		for (index = 0, e = waiters.elements(); e.hasMoreElements(); index++) {
			RWNode node = (RWNode) e.nextElement();
			if (node.t == t)
				return index;
		}
		return -1;
	}

	public RWLock() {
		waiters = new Vector();
	}

	public synchronized void lockRead() {
		RWNode node;
		Thread me = Thread.currentThread();
		int index = getIndex(me);
		if (index == -1) {
			node = new RWNode(me, RWNode.READER);
			waiters.addElement(node);
		} else
			node = (RWNode) waiters.elementAt(index);
		while (getIndex(me) > firstWriter()) {
			try {
				wait();
			} catch (Exception e) {
			}
		}
		node.nAcquires++;
	}
	public synchronized void lockWrite() {
		RWNode node;
		Thread me = Thread.currentThread();
		int index = getIndex(me);
		if (index == -1) {
			node = new RWNode(me, RWNode.WRITER);
			waiters.addElement(node);
		} else {
			node = (RWNode) waiters.elementAt(index);
			if (node.state == RWNode.READER)
				throw new IllegalArgumentException("Upgrade lock");
			node.state = RWNode.WRITER;
		}
		while (getIndex(me) != 0) {
			try {
				wait();
			} catch (Exception e) {
			}
		}
		node.nAcquires++;
	}

	public synchronized void unlock() {
		RWNode node;
		Thread me = Thread.currentThread();
		int index;
		index = getIndex(me);
		if (index > firstWriter())
			throw new IllegalArgumentException("Lock not held");
		node = (RWNode) waiters.elementAt(index);
		node.nAcquires--;
		if (node.nAcquires == 0) {
			waiters.removeElementAt(index);
			notifyAll();
		}
	}
}
