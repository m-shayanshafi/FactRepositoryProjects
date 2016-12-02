package fruitwar.web.server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

/**
 * BattleQueue is a queue for to-be-start battles. Each time, two robots
 * are popped up from the queue head, and these two robots should do a 
 * battle.
 * 
 * In actual, the queue is a priority queue sorted by the LAST_BATTLE_TIME
 * attribute of the robots. Each time the two robots popped up are calculated
 * considering the following factors:
 * 	1. The first robot is the one with the longest time without a battle.
 * 	2. The opponent is selected considering both time without a battle, together
 *     with the ranking difference comparing with the first selected robot.
 *     
 * @author wangnan
 *
 */
class BattleQueue {

	private static final int MAX_CANDIDATES_TO_CHECK = 50;	//max robots to check for next battle candidate.
	private static final int RANDOM_CANDIDATE_PROBABILITY = 30;
	
	private static Random rand = new Random();
	private LinkedList robots = new LinkedList();

	private static class BattlePair{
		String robot1;
		String robot2;
		public BattlePair(String robot1, String robot2) {
			this.robot1 = robot1;
			this.robot2 = robot2;
		}
	}
	
	//arrangedBattles is a list of vectors. Each vector represents a series of
	//arranged battles. For example, when we arrange battle for a robot, it makes
	//pair of name of the given robot and all other robots. This makes a vector
	//and is stored in arrangedBattles. Further battles take candidates firstly from
	//arranged battles. The first vector is always retrieved from arrangedBattles, and
	//a pair of name is removed from the vector and used for the battle candidate. 
	//If the vecton is not null, it will be moved to the tail of arrangedBattles.
	//This is to balance processing of multiple arranged battles.
	private LinkedList arrangedBattles = new LinkedList();
	
	public synchronized void popNextBattlePair(String[] robotPair) {
		robotPair[0] = null;
		robotPair[1] = null;
		
		if(robots.size() < 2)
			return;
		
		//first check whether we have arranged battle...
		if(!arrangedBattles.isEmpty()){
			Vector v = (Vector) arrangedBattles.removeFirst();
			BattlePair bp = (BattlePair) v.remove(v.size() - 1);
			robotPair[0] = bp.robot1;
			robotPair[1] = bp.robot2;
			if(!v.isEmpty())
				arrangedBattles.add(v);
			return;
		}
		
		//get the first robot. It's always the first one.
		ServerRobotProps p1 = (ServerRobotProps) robots.removeFirst();
		
		//get the second robot. Try at most MAX_CHECK_CNT robots here
		Vector v = new Vector();
		int n = 0;
		for(Iterator i = robots.iterator(); i.hasNext() && n < MAX_CANDIDATES_TO_CHECK; n++){
			v.add(i.next());
		}
		
		//find which one is more suitable
		//first we give 10% chance to choose a robot randomly, without calculating weight.
		int robotSelected = 0;
		if(rand.nextInt(100) < RANDOM_CANDIDATE_PROBABILITY){
			robotSelected = rand.nextInt(v.size());
		}else{
			//calculate weight and find the most proper one.
			int maxWeight = 0;
			for(int i = 0; i < v.size(); i++){
				ServerRobotProps p = (ServerRobotProps)v.get(i);
				int weight = calcWeight(p1.getRanking() - p.getRanking(), i);
				if(weight > maxWeight){
					maxWeight = weight;
					robotSelected = i;
				}
			}
		}
		
		ListIterator i = robots.listIterator(robotSelected);
		//it's it. We also remove it from the list
		ServerRobotProps p2 = (ServerRobotProps)i.next();
		i.remove();
		
		//add the two robots back to tail.
		robots.add(p1);
		robots.add(p2);

		
		//return
		robotPair[0] = p1.getName();
		robotPair[1] = p2.getName();
	}

	/**
	 * Calculate the robot weight for next battle.
	 * Weight are calculated by two factors:
	 * 1. The more the rankings are close, the more weight is.
	 * 2. The longer the robot has not done a battle, the more the chance
	 * 
	 * @param p
	 * @return
	 */
	private int calcWeight(int rankingDifference, int timeRankWithoutBattle) {
		//the max ranking difference is 15 * 15 = 225.
		if(rankingDifference < 0)
			rankingDifference = -rankingDifference;
		int factor1 = 100 - rankingDifference * 100 / 225;
		int factor2 = 100 - timeRankWithoutBattle * 100 / MAX_CANDIDATES_TO_CHECK; 
		return factor1 + factor2;
	}

	synchronized void add(ServerRobotProps prop) {
		if(!prop.isExceptionalRobot())
			robots.addFirst(prop);
	}

	synchronized void clear() {
		robots.clear();
		arrangedBattles.clear();
	}
	
	/**
	 * Arrange battles between the given robot and all other robots.
	 * The arranged battles will take place prior to other battles.
	 * @param name
	 */
	synchronized void arrangeBattle(String name){
		Vector v = new Vector(robots.size() * 2);
		for(Iterator i = robots.iterator(); i.hasNext();){
			String opponent = ((ServerRobotProps)i.next()).getName();
			if(!name.equals(opponent)){
				v.add(new BattlePair(name, opponent));
				v.add(new BattlePair(opponent, name));
			}
		}
		if(!v.isEmpty())
			arrangedBattles.add(v);
	}

	/**
	 * Remove the given name from battle queue.
	 * Clear arranged battles if necessary.
	 * 
	 * @param name
	 */
	synchronized boolean remove(String name) {
		//if there're arranged battles, remove all
		for(Iterator i = arrangedBattles.iterator(); i.hasNext(); ){
			Vector v = (Vector)i.next();
			for(int j = v.size() - 1; j >= 0; j--){
				BattlePair bp = (BattlePair)v.get(j);
				if(bp.robot1.equals(name) || bp.robot2.equals(name)){
					v.remove(j);
				}
			}
			
			//if this vector has nothing, remove it from arrangedBattles.
			if(v.isEmpty()){
				i.remove();
			}
		}
		
		//remove from our robot list
		for(Iterator i = robots.iterator(); i.hasNext();){
			String t = ((ServerRobotProps)i.next()).getName();
			if(name.equals(t)){
				i.remove();
				return true;	//it's unique.
			}
		}
		return false;
	}

}
