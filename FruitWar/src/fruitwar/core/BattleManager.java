package fruitwar.core;

import java.util.Vector;

import fruitwar.EnemyInfo;
import fruitwar.IFruitThrower;
import fruitwar.IFruitWarRobot;
import fruitwar.Rules;
import fruitwar.util.Logger;

/**
 * This class is used to simulate a Fruit War between two teams (robots). 
 * It takes two robots as input, and gives BattleResult as result.
 * 
 * @author wangnan
 *
 */
public class BattleManager {
	
	RobotRunner robotRunner1;
	RobotRunner robotRunner2;
	
	FruitThrower[] team1;
	FruitThrower[] team2;
	
	BattleResult battleResult;
	
	boolean exceptionOccurs;
	
	public BattleManager(IFruitWarRobot r1, IFruitWarRobot r2, boolean enableRecord){
		robotRunner1 = new RobotRunner(r1);
		robotRunner2 = new RobotRunner(r2);
		
		team1 = new FruitThrower [Rules.BASKET_CNT()];
		team2 = new FruitThrower [Rules.BASKET_CNT()];
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			team1[i] = new FruitThrower(team1);
			team2[i] = new FruitThrower(team2);
		}	
		
		battleResult = new BattleResult(
				robotRunner1.robot.getClass().getName(),
				robotRunner2.robot.getClass().getName(), 
				enableRecord);
		
		exceptionOccurs = false;
	}
	
	public BattleResult doBattle(){
		showTeamNames();
		
		robotRunner1.start();
		robotRunner2.start();
		
		for(int i = 0; ; i++){
			if(!doRound())
				break;
			
			//if we reach round limit...
			//set result to draw. 
			if(i >= Rules.ROUND_TOTAL() - 1){
				battleResult.setWinner(BattleResult.DRAW);
				break;
			}
		}

		//battle is over, finalize the result.
		battleResult.finalizeResult(team1, team2);
		
		//clean up.
		robotRunner1.stop();
		robotRunner2.stop();
		
		return battleResult;
	}
	
	private void showTeamNames() {
		Logger.log(robotRunner1.robot.getClass().getName() 
				+ " VS " + robotRunner2.robot.getClass().getName() + "\n");
	}

	/**
	 * 
	 * @return false if the battle is over.
	 */
	private boolean doRound(){
		
		//
		//let each team set their strategies
		//
		doStrategy();
		if(exceptionOccurs)
			return false;
		
		//
		//record the actions
		//
		battleResult.recordActions(team1, team2);
		
		//
		//process the actions
		//
		processActions();
		
		//
		//notify each team the result of last round
		//
		notifyResults();
		if(exceptionOccurs)
			return false;
		
		//
		//	Clear actions of every Thrower.
		//
		resetActions();
		
		//
		//	remove out-of-play 
		//
		checkOutOfPlay();
		
		return !checkBattleOver();
	}
	
	private void resetActions() {
		//clear actions after processed hit calculation for both sides.
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			if(team1[i] != null)
				team1[i].clearAction();
			if(team2[i] != null)
				team2[i].clearAction();
		}
	}

	/**
	 * Let robot set strategy.
	 */
	private void doStrategy() {

		/**
		 * Create proxy for teams. These objects will be passed to robots.
		 * Add this proxy to prevent potential manipulating of actual data object. 
		 */

		Vector v1 = new Vector();
		Vector v2 = new Vector();
		
		//Recreate proxy array.
		//Proxy arrays may change because of move, so simply
		//recreate them each time to make a refresh 
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			IFruitThrower t;
			
			t = team1[i];
			if(t != null)
				v1.add(new FruitThrowerProxy(t));
			
			t = team2[i];
			if(t != null)
				v2.add(new FruitThrowerProxy(t));
		}
		
		IFruitThrower[] teamProxy1 = new IFruitThrower[v1.size()];
		v1.toArray(teamProxy1);
		IFruitThrower[] teamProxy2 = new IFruitThrower[v2.size()];
		v2.toArray(teamProxy2);
		
		
		try {
			robotRunner1.strategy(teamProxy1);
		} catch (RobotException e) {
			battleResult.setErrorTeam1(e);
			exceptionOccurs = true;
			return;
		}

		try {
			robotRunner2.strategy(teamProxy2);
		} catch (RobotException e) {
			battleResult.setErrorTeam2(e);
			exceptionOccurs = true;
			return;
		}
	}

	/**
	 * Process actions of each thrower
	 */
	private void processActions(){
		
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			FruitThrower t;
			
			//
			//do action
			//
			t = team1[i];
			if(t != null){
				if(t.isAttacking() && t.getAction().isValidTarget()){
					int target = t.getAction().target;
					FruitThrower tgt = team2[target];
					if(tgt != null && tgt.isAttacking()){	//only players who are attacking can be hit.
						//hit!
						tgt.onHit();
					}
					//also, it's attacking, so not a coward. Reset coward count.
					t.resetCowardCount();
				}else{
					//it's hiding. Add coward count.
					//This will possible cause this Thrower out of play.
					t.addCowardCount();
				}
			}
			
			//(same for team2)
			t = team2[i];
			if(t != null){
				if(t.isAttacking() && t.getAction().isValidTarget()){
				
					int target = t.getAction().target;
					FruitThrower tgt = team1[target];
					if(tgt != null && tgt.isAttacking()){
						tgt.onHit();
					}
					t.resetCowardCount();
				}else{
					t.addCowardCount();
				}
			}
		}
	}
	
	/**
	 * Send results to each robot after round.
	 */
	private void notifyResults(){

		EnemyInfo[] info1 = new EnemyInfo [Rules.BASKET_CNT()];
		EnemyInfo[] info2 = new EnemyInfo [Rules.BASKET_CNT()];
		
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			info1[i] = makeEnemyInfo(team1[i]);
			info2[i] = makeEnemyInfo(team2[i]);
		}

		//send result to robots. If a team has no scout, it gets nothing.
		
		
		try {
			robotRunner1.notifyResult(info2);
		} catch (RobotException e) {
			battleResult.setErrorTeam1(e);
			exceptionOccurs = true;
			return;
		}
		

		try {
			robotRunner2.notifyResult(info1);
		} catch (RobotException e) {
			battleResult.setErrorTeam2(e);
			exceptionOccurs = true;
			return;
		}
	}
	
	/**
	 * Make EnemyInfo of FruitThrower t. The returned info is used to tell
	 * opponent robot, what does "t" look like.
	 * @param t
	 * @return
	 */
	private static EnemyInfo makeEnemyInfo(FruitThrower t){
		EnemyInfo i = null;
		if(t != null){
			//if it's attacking, add it to enemy info
			if(t.isAttacking()){
				i = new EnemyInfo(t.getHP(), t.getAction().target);
			}else{
				//it's hiding. 
				//We have a special case here: if it's out of
				//play because of hiding, we also set enemy info. This will
				//simplify the user logic for identify whether an enemy thrower
				//is down. Note that, from next round, the element will still
				//be null: the identical behavior of out-of-play thrower.
				if(t.getHP() == 0){
					i = new EnemyInfo(0, -1);
				}
			}
		}
		return i;
	}
	
	/**
	 * Check whether there're players out of play 
	 */
	private void checkOutOfPlay(){

		//
		//	check whether someone is out of play:
		//	HP <= 0 or coward_count >= Rules.COWARD_LIMIT
		//
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			FruitThrower t;
			
			t = team1[i];
			if(t != null){
				if(t.getHP() <= 0){
					team1[i] = null;
				}
			}
			
			t = team2[i];
			if(t != null){
				if(t.getHP() <= 0){
					team2[i] = null;
				}
			}
		}	
	}
	
	/**
	 * Check whether the battle is over.
	 * This method checks only the HP status of each team. If a team has no more
	 * active thrower, then the battle is over.
	 * @return
	 */
	private boolean checkBattleOver(){
		boolean team1Alive = false;

		//check team 1
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			FruitThrower t = team1[i];
			if(t != null){
				team1Alive = true;
				break;
			}
		}


		boolean team2Alive = false;
		
		//check team 2
		for(int i = 0; i < Rules.BASKET_CNT(); i++){
			FruitThrower t = team2[i];
			if(t != null){
				team2Alive = true;
				break;
			}
		}

		//if both team are still up, battle is not over.
		if(team1Alive && team2Alive)
			return false;
		
		//at least one team is down.
		
		//if both are donw: draw game
		if(!team1Alive && !team2Alive)
			battleResult.setWinner(BattleResult.DRAW);
		else{
			//one team is donw. check who wins.
			if(team1Alive)
				battleResult.setWinner(BattleResult.TEAM_1);
			else
				battleResult.setWinner(BattleResult.TEAM_2);
		}
		
		return true;	//battle is over.
	}
}
