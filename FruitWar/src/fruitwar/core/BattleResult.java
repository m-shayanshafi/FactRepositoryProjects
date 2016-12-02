package fruitwar.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import fruitwar.Rules;
import fruitwar.util.Logger;


/**
 * Result of a battle.
 * 
 * @author wangnan
 *
 */
public class BattleResult {

	public static final String BATTLE_KEY = "BATTLE";
	public static final String RESULT_KEY = "RESULT";
	public static final String EXCEPTION_START_KEY = "EXCEPTION_START";
	public static final String EXCEPTION_END_KEY = "EXCEPTION_END";
	public static final String BATTLE_LOG_START_KEY = "BATTLE_LOG_START";
	public static final String BATTLE_LOG_END_KEY = "BATTLE_LOG_END";
	
	public static final int INVALID = -1;
	public static final int DRAW = 0;
	
	public static final int TEAM_1 = 1;
	public static final int TEAM_2 = 2;
	
	public static final int EXCEPTION_NONE = 0;
	public static final int EXCEPTION_OTHER = 3;
	
	private String name1;
	private String name2;
	
	private int score1;	//total hp left of the team after a match.
	private int score2;
	
	private String exception;
	private int exceptionType;	//type of exception. Valid only when exception != null.
	private int winner;
	
	private Vector battleLog;
	
	private static class IDGenerator{
		static long lastID = 0;
		static synchronized long generateID(){
			long id = System.currentTimeMillis();
			if(id == lastID)
				id++;
			lastID = id;
			return id;
		}
	}
	private long timeID;
	
	private boolean enableRecord;
	
	public BattleResult(String name1, String name2, boolean enableRecord) {
		this.name1 = name1;
		this.name2 = name2;
		this.enableRecord = enableRecord;
		reset();
		
		timeID = IDGenerator.generateID();
	}
		
	private void reset(){
		score1 = 0;
		score2 = 0;
		exceptionType = EXCEPTION_NONE;
		exception = null;
		winner = INVALID;
		battleLog = enableRecord ? new Vector() : null;
	}
	
	public void recordActions(FruitThrower[] team1, FruitThrower[] team2) {
		
		if(!enableRecord)
			return;
		
		//*
		Action[] actions1 = new Action[team1.length];
		Action[] actions2 = new Action[team1.length];
		
		for(int i = 0; i < team1.length; i++){
			FruitThrower t = team1[i];
			actions1[i] = t == null ? null : t.getAction();
			t = team2[i];
			actions2[i] = t == null ? null : t.getAction();
		}
		battleLog.add(Action.convertActionsToString(actions1));
		battleLog.add(Action.convertActionsToString(actions2));
		//*/
		
		//simply recording only the action is a good idea, which also saves many
		//memory for server. 
		//The problem is that we need to make a whole redesign of battle manager 
		//(or create new) to make something like "replayer". 
		//Too much effort. Simple record all here.
		
		/*
		
		battleLog.add("=== Round " + battleLog.size() / 5 + " ===");
		
		String sHP = "";
		String sAct = "";
		for(int i = 0; i < team1.length; i++){
			FruitThrower t = team1[i];
			//make hp string
			int hp = (t == null ? 0 : t.getHP());
			sHP += hp + " ";
			if(hp < 100){
				sHP += ' ';
				if(hp < 10)
					sHP += ' ';
			}
			//make action string
			Action a = t == null ? null : t.getAction();
			sAct += Action.encodeAction(a) + ".  ";
		}

		battleLog.add("1(hp) - " + sHP);
		battleLog.add("1(at) - " + sAct);
		
		sHP = "";
		sAct = "";
		for(int i = 0; i < team2.length; i++){
			FruitThrower t = team2[i];
			//make hp string
			int hp = (t == null ? 0 : t.getHP());
			sHP += hp + " ";
			if(hp < 100){
				sHP += ' ';
				if(hp < 10)
					sHP += ' ';
			}
			//make action string
			Action a = t == null ? null : t.getAction();
			sAct += Action.encodeAction(a) + ".  ";
		}

		battleLog.add("2(at) - " + sAct);
		battleLog.add("2(hp) - " + sHP);
		
		
		//*/
	}
	
	/**
	 * Record error thrown by team1
	 * @param e
	 */
	void setErrorTeam1(RobotException e) {
		recordError(e, TEAM_1);
		score1 = -1;
		winner = TEAM_2;
	}
	
	/**
	 * Record error thrown by team2
	 * @param e
	 */
	void setErrorTeam2(RobotException e) {
		recordError(e, TEAM_2);
		score2 = -1;
		winner = TEAM_1;
	}
	
	private void recordError(RobotException e, int team){

		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		String exceptionRobotName;
		String opponentName;
		if(team == TEAM_1){
			exceptionRobotName = name1;
			opponentName = name2;
		}else{
			exceptionRobotName = name2;
			opponentName = name1;
		}
		w.println(EXCEPTION_START_KEY + " " + team);
		w.println("--- Exception occured in robot [" + exceptionRobotName + "], the opponent was [" + opponentName + "] ---");
		//if it's a message exception, display the message only.
		if(e.getCause() == null)
			w.println(e.getMessage());
		else	//our RobotException is caused by something else, display it.
			e.getCause().printStackTrace(w);
		w.println("--- End of exception ---");
		w.println(EXCEPTION_END_KEY);
		w.flush();
		
		exception = sw.toString();
		exceptionType = team;
	}
	

	public String formatString(){
		
		if(exceptionType == EXCEPTION_OTHER)
			return "Unknown error occurred. Check list:\n"
				+ "  1. If your robot has a constructor, it must be public.\n"
				+ "  2. If your robot has a constructor, it must NOT have parameter.\n"
				+ "If none of the above errors match your case, please contact developer.\n\n Output:\n" + exception;
		
		String s = BATTLE_KEY + " " + name1 + " " + name2 + "\n"; 
		
		s += RESULT_KEY + " " + winner + " " + score1 + " " + score2 + '\n';
		
		if(exception != null){
			s += exception + '\n';
		}
		
		return s;
	}
	
	public boolean loadFromString(String content){

		//clear before restore.
		reset();
		
		boolean hasError = false;
		boolean resultProcessed = false;
		int exceptionRobot = 0;
		boolean isBattleLogSection = false;
		try{
			String[] lines = content.split("\n");
			for(int i = 0; i < lines.length; i++){
				String s = lines[i].trim();
				
				//if we're not in exception area
				if(exceptionRobot == 0){
					if(!isBattleLogSection){
						if(s.startsWith(RESULT_KEY)){
							//parse result line.
							//sample:
							//RESULT 1 344 222
							String[] tmp = s.split(" ");
							if(tmp.length != 4){
								hasError = true;
								break;
							}
							winner = Integer.valueOf(tmp[1]).intValue();
							if(winner != INVALID && winner != DRAW 
									&& winner != TEAM_1 && winner != TEAM_2){
								hasError = true;
								break;
							}
							score1 = Integer.valueOf(tmp[2]).intValue();
							score2 = Integer.valueOf(tmp[3]).intValue();
							resultProcessed = true;
						}else if(s.startsWith(EXCEPTION_START_KEY)){
							//get which robot has exception
							String[] tmp = s.split(" ");
							if(tmp.length != 2){
								hasError = true;
								break;
							}
							exceptionRobot = Integer.valueOf(tmp[1]).intValue();
							
							if(exceptionRobot == 1)
								exceptionType = TEAM_1;
							else if(exceptionRobot == 2)
								exceptionType = TEAM_2;
							else{
								hasError = true;
								break;
							}
							exception = "";	//init
						}else if(s.startsWith(BATTLE_LOG_START_KEY)){
							isBattleLogSection = true;
							battleLog = new Vector();
						}
					}else{
						//battle log section. restore logs
						if(s.startsWith(BATTLE_LOG_END_KEY))
							isBattleLogSection = false;
						else
							battleLog.add(s);
					}
				}else{
					//in exception area.
					if(s.startsWith(EXCEPTION_END_KEY))
						exceptionRobot = 0;
					else{
						exception += s + "\n";
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			hasError = true;
		}
		
		hasError |= !resultProcessed;
		
		//also check whether sections are closed gracefully
		if(isBattleLogSection || exceptionRobot != 0)
			hasError = true;
		
		if(hasError){
			setUnknownException(content);
		}else{
			finalizeResult(null, null);
		}
	
		return !hasError;
	}
	
	public void setUnknownException(String content) {
		exceptionType = EXCEPTION_OTHER;
		if(exception == null || exception.length() == 0)
			exception = content;
		else
			exception += "\nUnknown exception:\n" + content;
		winner = INVALID;
	}

	/**
	 * If it's a draw, record team HP left as score, and adjust 
	 * result according to scores.
	 */
	void finalizeResult(FruitThrower[] team1, FruitThrower[] team2){
		
		//if there's no exception
		if(exception == null){
			if(team1 != null && team2 != null){
		
				if(score1 != 0 || score2 != 0)
					throw new RuntimeException("How could this be...who messed my logic...");
				
				for(int i = 0; i < team1.length; i++){
					FruitThrower t = team1[i];
					if(t != null)
						score1 += t.getHP();
					t = team2[i];
					if(t != null)
						score2 += t.getHP();
				}
			}
		}
		
		if(winner == DRAW){
			if(score1 > score2)
				winner = TEAM_1;
			if(score1 < score2)
				winner = TEAM_2;	
		}
	}

	void setWinner(int winner) {
		this.winner = winner;
	}
	
	public int getWinner(){
		return winner;
	}
	
	public String getName1(){
		return name1;
	}
	
	public String getName2(){
		return name2;
	}
	
	public int getScore1(){
		return score1;
	}
	
	public int getScore2(){
		return score2;
	}
	
	public String getException(){
		return exception;
	}
	
	public int getExceptionType(){
		return exceptionType;
	}
	
	public boolean isValid(){
		return winner != INVALID; 
	}
	
	public String getWinnerName(){
		if(winner == TEAM_1)
			return name1;
		if(winner == TEAM_2)
			return name2;
		return null;
	}
	
	/**
	 * Get time ID of the battle result. This is unique but may not be accurate.
	 * @return
	 */
	public long getTimeID(){
		return timeID;
	}

	public Vector getBattleLogs() {
		return battleLog;
	}
	
	/**
	 * Format battle logs to a human readable format
	 * @return
	 */
	public String formatBattleLogs(boolean format){
		if(battleLog == null || battleLog.isEmpty())
			return "";
		
		if(battleLog.size() % 2 != 0){
			Logger.error("Incorrect battle log length (should be even number): " + battleLog.size());
			return "";
		}
		StringBuffer buf = new StringBuffer();
		
		//if not format log, we send out raw data
		if(!format){
			buf.append(BATTLE_LOG_START_KEY + '\n');
			for(int i = 0; i < battleLog.size(); i++){
				buf.append((String) battleLog.get(i) + '\n');
			}
			buf.append(BATTLE_LOG_END_KEY + '\n');
			return buf.toString();
		}
		
		
		//
		//	format detailed logs from raw action log. It's a misery to replay
		//	the actions here.
		//
		final int CNT = Rules.BASKET_CNT();
		int[] hp1 = new int [CNT];
		int[] hp2 = new int [CNT];
		int[] cowardCnt1 = new int [CNT];
		int[] cowardCnt2 = new int [CNT];
		for(int i = 0; i < CNT; i++){
			hp1[i] = Rules.FRUIT_THROWER_HP();
			hp2[i] = Rules.FRUIT_THROWER_HP();
			cowardCnt1[i] = 0;
			cowardCnt2[i] = 0;
		}
		
		Vector messages = new Vector();
		
		int line = 0;
		while(true){
		
			if(line >= battleLog.size()){
//				//no more data. Display the last round status and out.
//				buf.append("=== Final HP ===\n");
//				buf.append("1(hp) - " + makeHPString(hp1) + '\n');
//				buf.append("2(hp) - " + makeHPString(hp2) + '\n');
				break;
			}

			//round title
			buf.append("=== Round " + line / 2 + " ===\n");
			
			//
			//get action of one round
			//
			String actionString = (String) battleLog.get(line++);
			
			if(actionString.length() != CNT){
				Logger.error("Incorrect action string length. Length=" + actionString.length());
				return "";
			}
			
			Action actions1[] = Action.convertStringToActions(actionString);
			
			actionString = (String) battleLog.get(line++);
			if(actionString.length() != CNT){
				Logger.error("Incorrect action string length. Length=" + actionString.length());
				return "";
			}
			Action actions2[] = Action.convertStringToActions(actionString);
		

			
			//
			//calculate hit & HP decrease
			//
			for(int i = 0; i < CNT; i++){
				Action a;

				//for team 1
				a = actions1[i];
				//if it's throwing at someone
				if(a != null){
					if(a.action == Action.THROW){
						int t = a.target;
						
						//if the target is still active, and it's not hiding 
						if(hp2[t] > 0 && actions2[t] != null && actions2[t].action != Action.HIDE)
							hp2[t]--;
						//reset coward count
						cowardCnt1[i] = 0;
					}else{
						//hiding. add coward count.
						if(++cowardCnt1[i] > Rules.COWARD_LIMIT()){
							//it's a coward.
							hp1[i] = 0;
							messages.add("(Thrower " + i + " of robot 1 is a coward and it's out of play.)\n");
						}
							
					}
				}
				
				//for team2
				a = actions2[i];
				//if it's throwing at someone
				if(a != null){
					if(a.action == Action.THROW){
						int t = a.target;
						
						//if the target is still active, and it's not hiding 
						if(hp1[t] > 0 && actions1[t] != null && actions1[t].action != Action.HIDE)
							hp1[t]--;
						//reset coward count
						cowardCnt2[i] = 0;
					}else{
						//hiding. add coward count.
						if(++cowardCnt2[i] > Rules.COWARD_LIMIT()){
							//it's a coward.
							hp2[i] = 0;
							messages.add("(Thrower " + i + " of robot 2 is a coward and it's out of play.)\n");
						}
					}
				}
			}
			
			
			//
			//format this round's strings
			//
			String sAct1 = "";
			String sAct2 = "";
			for(int i = 0; i < CNT; i++){
				//make action string
				sAct1 += Action.encodeAction(actions1[i]) + ".  ";
				//make action string
				sAct2 += Action.encodeAction(actions2[i]) + ".  ";
			}

			buf.append("1(hp) - " + makeHPString(hp1) + '\n');
			buf.append("1(at) - " + sAct1 + '\n');
			buf.append("2(at) - " + sAct2 + '\n');
			buf.append("2(hp) - " + makeHPString(hp2) + '\n');
			if(!messages.isEmpty()){
				for(int i = 0; i < messages.size(); i++){
					buf.append(messages.get(i));
				}
				messages.clear();
			}
		}
		
		return buf.toString();
	}

	private static String makeHPString(int[] hpArray){
		String s = "";
		for(int i = 0; i < hpArray.length; i++){
			//make hp string
			int hp = hpArray[i];
			s += hp + " ";
			if(hp < 100){
				s += ' ';
				if(hp < 10)
					s += ' ';
			}
		}
		return s;
	}
	
	/**
	 * Swap robot 1 & robot 2.
	 */
	/*
	public void swapSides() {
		String tmp = name1;
		name1 = name2;
		name2 = tmp;
		
		int t = score1;
		score1 = score2;
		score2 = t;
		
		//private String exception;
		
		if(exceptionType == TEAM_1)
			exceptionType = TEAM_2;
		else if(exceptionType == TEAM_2)
			exceptionType = TEAM_1;
		
		if(winner == TEAM_1)
			winner = TEAM_2;
		else if(winner == TEAM_2)
			winner = TEAM_1;
	}
	*/
}
