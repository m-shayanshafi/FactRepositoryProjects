package fruitwar.web.server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * A simple class to record history of battles.
 * Actually a FIFO queue. 
 * 
 * @author wangnan
 *
 */
class BattleHistory {
	
	//We're under 1.4, use LinkedList to simulate a FIFO queue.
	private LinkedList history = new LinkedList();
	private int limit;
	
	BattleHistory(int limit){
		this.limit = limit;
	}
	
	synchronized void add(ServerBattleResult result){
		if(history.size() >= limit)
			history.removeFirst();
		history.add(result);
	}
	
	synchronized ServerBattleResult[] getHistory(){
		ServerBattleResult[] h = new ServerBattleResult[history.size()];
		history.toArray(h);
		return h;
	}

	synchronized ServerBattleResult[] getHistory(String robotName){
		Vector v = new Vector();
		for(Iterator i = history.iterator(); i.hasNext();){
			ServerBattleResult r = (ServerBattleResult) i.next();
			if(r.getPrimalResult().getName1().equals(robotName)
					|| r.getPrimalResult().getName2().equals(robotName))
				v.add(r);
		}
		ServerBattleResult[] h = new ServerBattleResult[v.size()];
		v.toArray(h);
		return h;
	}
	
	synchronized ServerBattleResult get(String id) {
		if(id == null)
			return null;
		long n = 0;
		try{
			n = Long.valueOf(id).longValue();
		} catch(NumberFormatException e) {
		}
		if(n == 0)
			return null;
		
		for(Iterator i = history.iterator(); i.hasNext();){
			ServerBattleResult r = (ServerBattleResult) i.next();
			if(r.getPrimalResult().getTimeID() == n)
				return r;
		}
		
		return null;
	}

	synchronized void clear() {
		history.clear();
	}
	
	
	/*
	synchronized boolean load(String fileName){
		PropertiesFile storage = new PropertiesFile(fileName);
		boolean ret = storage.loadOrCreate();
		if(!ret)
			return false;
		
		String history = storage.get("history");
		if(history == null)
			return false;
		
		String[] tmp = history.split("|");
		for(int i = 0; i < tmp.length; i++){
			String[] h = tmp[i].split(",");
			
			
			//this is not good. The exception info is lost. We need somewhere else
			//to store the exception.

			//actually BattleHistory does NOT need to save!
		}
	}
	
	synchronized boolean save(String fileName){
		StringBuffer buf = new StringBuffer();
		
		for(Iterator i = history.iterator(); i.hasNext();){
			ServerBattleResult h = (ServerBattleResult) i.next();
			buf.append(h.getTimeID());
			buf.append(',');
			buf.append(h.getName1());
			buf.append(',');
			buf.append(h.getName2());
			buf.append(',');
			buf.append(h.getScore1());
			buf.append(',');
			buf.append(h.getScore2());
			buf.append(',');
			buf.append(h.getDeltaRanking1());
			buf.append('|');
		}
		PropertiesFile storage = new PropertiesFile(fileName);
		storage.set("history", buf.toString());
		return storage.save();
	}
	*/
}
