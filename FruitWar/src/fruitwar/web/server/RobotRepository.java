package fruitwar.web.server;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class RobotRepository {
	private static Hashtable robots = new Hashtable();
	
	void clear() {
		robots.clear();
	}

	int size() {
		return robots.size();
	}

	void put(String name, ServerRobotProps prop) {
		robots.put(name, prop);
	}

	ServerRobotProps get(String name) {
		return (ServerRobotProps)robots.get(name);
	}

	boolean containsKey(String name) {
		return robots.containsKey(name);
	}

	ServerRobotProps[] queryRobotsByAuthor(String author) {
		Vector v = new Vector();
		
		Enumeration e = robots.elements();
		while(e.hasMoreElements()){
			ServerRobotProps p = (ServerRobotProps)e.nextElement();
			if(p.getAuthor().equalsIgnoreCase(author))
				v.add(p);
		}
		
		ServerRobotProps[] ret = new ServerRobotProps[v.size()];
		for(int i = 0; i < v.size(); i++)
			ret[i] = (ServerRobotProps)v.get(i);
		return ret;
	}
	
	ServerRobotProps delete(String name){
		return (ServerRobotProps) robots.remove(name);
	}

	/**
	 * Try to save any unsaved robot properties files.
	 */
	void save() {
		Enumeration e = robots.elements();
		while(e.hasMoreElements()){
			ServerRobotProps p = (ServerRobotProps)e.nextElement();
			p.save();
		}
	}
}
