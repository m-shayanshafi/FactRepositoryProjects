package fruitwar.web;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	static LinkedList sessions = new LinkedList();
	static int historySessionCount = 0;
	
	public void sessionCreated(HttpSessionEvent arg0) {
		
		historySessionCount++;
		
		HttpSession session = arg0.getSession();
		session.setMaxInactiveInterval(1800);
		synchronized(sessions){
			sessions.add(session);
		}
	}

	public synchronized void sessionDestroyed(HttpSessionEvent arg0) {
		
		//logout
		HttpSession session = arg0.getSession();
		//Account a = (Account) session.getAttribute("account");
		//AccountManager.logout(a);
		synchronized(sessions){
			sessions.remove(session);
		}
	}

	public static int getSessionCount(){
		synchronized(sessions){
			return sessions.size();
		}
	}
	
	public static List getSessions(){
		synchronized(sessions){
			return (List) sessions.clone();
		}
	}
	
	public static int getHistorySessionCount(){
		return historySessionCount;
	}
}
