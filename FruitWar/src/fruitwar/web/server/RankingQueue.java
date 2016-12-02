package fruitwar.web.server;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Manager of names sorted by ranking.
 * Position range: [0, size()).
 * Ranking should >= 0.
 * 
 * @author wangnan
 *
 */
class RankingQueue {

	/*
	 * This should be a priority queue. For compatibility reason 
	 * jdk 1.4 is used, so we have this nightmare.
	 */
	
	Vector nodes;
	
	public static class Node{
		int ranking;
		String name;
	}
	
	public RankingQueue(){
		nodes = new Vector();
	}
	
	/**
	 * Insert a new name into the queue with the given ranking.
	 * If the name exists, the ranking will be updated.
	 * 
	 * @param name
	 * @param ranking
	 */
	public void add(String name, int ranking){
		Node n = new Node();
		n.name = name;
		n.ranking = 0;
		nodes.add(n);
		//this is slow but acceptable.
		update(name, ranking);
	}

	
	/**
	 * Update the ranking of the given name.
	 * Return false if the name does not exist. 
	 * @param name
	 * @param ranking
	 */
	public boolean update(String name, int ranking){
		int i;
		for(i = 0; i < nodes.size(); i++){
			Node n = (Node) nodes.get(i);
			if(n.name.equals(name)){
				break;
			}
		}
		
		//if it's not found..
		if(i == nodes.size())
			return false;
		
		//found. It's located at "i".
		//move it
		int myPos = i;
		Node n = (Node) nodes.get(myPos);
		if(n.ranking != ranking){
			if(n.ranking > ranking){
				//rank decreased, move to tail
				
				while(true){
					int nextPos = myPos + 1;
					//if we're already at tail, out.
					if(nextPos >= nodes.size())
						break;
					Node nextNode = (Node) nodes.get(nextPos);
					//if the next one has a lower ranking than us, stop.
					if(ranking > nextNode.ranking)
						break;
					//swap me with next one.
					nodes.set(nextPos, n);
					nodes.set(myPos, nextNode);
					myPos = nextPos;
				} 
			}else{
				//rank increased, move to head
				
				while(true){
					int nextPos = myPos - 1;
					//if we're already at head, out.
					if(nextPos < 0)
						break;
					Node nextNode = (Node) nodes.get(nextPos);
					//if the next one has a higher ranking than us, stop.
					if(ranking < nextNode.ranking)
						break;
					//swap me with next one.
					nodes.set(nextPos, n);
					nodes.set(myPos, nextNode);
					myPos = nextPos;
				} 
			}
			
			n.ranking = ranking;
		}
		return true;
	}
	
	/**
	 * Remove the given name from queue.
	 * Return true if success, otherwise false.
	 * @param name
	 */
	public boolean remove(String name){
		for(Iterator i = nodes.iterator(); i.hasNext();){
			Node n = (Node) i.next();
			if(n.name.equals(name)){
				i.remove();
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Get name/ranking pairs, start from the given position.
	 * @param from
	 * @param count
	 * @return
	 */
	public Node[] query(int from, int count){
		if(nodes.isEmpty())
			return null;
		if(from < 0 || from >= nodes.size())
			return null;
		if(count > nodes.size() - from)
			count = nodes.size() - from;
		if(count < 1)
			return null;

		Node[] ret = new Node [count];
		List sub = nodes.subList(from, from + count);
		sub.toArray(ret);
		return ret;
	}
	
	/**
	 * Return total names we have.
	 * @return
	 */
	public int size(){
		return nodes.size();
	}
	
	/**
	 * Get the ranking of the name.
	 * Return -1 if no such name.
	 * @param name
	 * @return
	 */
	public int getRanking(String name){
		for(Iterator i = nodes.iterator(); i.hasNext();){
			Node n = (Node) i.next();
			if(n.name.equals(name))
				return n.ranking;
		}
		return -1;
	}
	
	/**
	 * Get the position of the given name.
	 * Position range: [0, size()).
	 * Return -1 if the name is not found.
	 * @param name
	 * @return
	 */
	public int getPosition(String name){
		int position = 0;
		for(Iterator i = nodes.iterator(); i.hasNext();){
			Node n = (Node) i.next();
			if(n.name.equals(name))
				return position;
			++position;
		}
		return -1;
	}

	public void clear() {
		nodes.clear();
	}
}
