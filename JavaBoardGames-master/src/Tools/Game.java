package Tools;
import java.util.ArrayList;

/**
 * 
 * @author Dean_Johnson
 * This is a superclass for CardGames and BoardGames
 * 
 */
public class Game {

	private final int MIN_PLAYERS = 1;
	private final int MAX_PLAYERS = 10;
	
	public Game(){
		
	}
	
	public int getMinPlayers(){
		return MIN_PLAYERS;
	}
	
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	public String playerAmountMessage(){
		return "The amount of players that can play a default game at once is "+MIN_PLAYERS+"-"+MAX_PLAYERS+"."; 
	}
	
	public String printWelcomeMessage(String gameName, int numOfPlayers, ArrayList<Player> players){
		String welcomeMessage ="Welcome to "+gameName+" ";
		
		for(int i=0; i<numOfPlayers; i++){
			welcomeMessage+=players.get(i).getName(); 
			if(i<numOfPlayers-2)
				welcomeMessage+=", ";
			else if (i==numOfPlayers-2)
				welcomeMessage+=" and ";
		}
		welcomeMessage+="!";
		return welcomeMessage;
	}
	
	// Shorthand for printing
	public void p(String s){
		System.out.println(s);
	}
	public void p(int i){
		System.out.print(i);
	}
	public void p(String s, boolean carriageReturn){
		System.out.print(s);
		if(carriageReturn)
			System.out.println();
	}
}
