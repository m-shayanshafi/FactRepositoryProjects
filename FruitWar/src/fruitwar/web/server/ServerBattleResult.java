package fruitwar.web.server;

import fruitwar.core.BattleResult;

public class ServerBattleResult {

	private int deltaRanking1;
	private BattleResult result;
	/**
	 * 
	 * @param name1
	 * @param name2
	 * @param deltaRanking The delta ranking of robot1.
	 */
	public ServerBattleResult(BattleResult result, int deltaRanking1) {
		this.result = result;
		this.deltaRanking1 = deltaRanking1;
	}
	
	public int getDeltaRanking1(){
		return deltaRanking1;
	}
	
	public BattleResult getPrimalResult(){
		return result;
	}
	
	public String formatString(){
		//format raw battle log if necessary
		StringBuffer buf = new StringBuffer();
		buf.append(result.formatString() + '\n');
		buf.append("Delta ranking1: " + deltaRanking1 + "\n\n");
		
		String winner = result.getWinnerName();
		if(winner != null)
			buf.append("Winner is: " + winner + "\n");
		else
			buf.append("Draw game.\n");
		
		buf.append("\n\n");
		buf.append("Details:\n\n");
		buf.append(result.formatBattleLogs(true));
		
		return buf.toString();
	}

}
