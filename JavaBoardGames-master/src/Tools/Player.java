package Tools;
import java.util.ArrayList;
import java.util.Scanner;


/**
* Author: Dean Johnson
* Date: November 26, 2011
* Description: Creates a player with multiple properties that can track
* data for that player. I.e. Name, wins, specific game data.
**/
public class Player {
	private String name;
	private int numOfWins, numOfLosses, numOfTies, amountOfCardsToLay;
	private boolean inWar, myTurn, inGame, mustLayCards;
	private ArrayList<Card> hand= new ArrayList<Card>();
	private ArrayList<ArrayList<Card>> pairs = new ArrayList<ArrayList<Card>>(); // GoFish only
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description: Constructor for player that asks the user for their name
	public Player(){
		//createName();
	}
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description: Constructor for player that creates the name based on an integer value provided
	public Player(int name){
		this.name="Player "+name;
	}
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description: Constructor for player that creates the name based on a String provided
	public Player(String name){
		this.name=name;
	}
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description: returns whether a player's hand is empty or not
	public boolean hasCards(){
		return hand.size()>0;
	}
	
	// Will eventually make a constructor for each game, so variables are more specific
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Adds a card to the hand 
	// Note: Shortcut so i never access the variables directly
	public void add(Card c){
		hand.add(c);
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Adds an arraylist of card to the hand
	// Note: Shortcut so i never access the variables directly
	public void addAll(ArrayList<Card> c){
		hand.addAll(c);
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Removes a card from the hand 
	// Note: This usually isn't used due to getNextCard(), Shortcuts so i never access the variables directly
	public void remove(){
		hand.remove(0);
	}
	
	// Date Created: 11/27/11
	// Date Modified: 11/27/11
	// Description:Removes a card from a specific index in the hand
	// Note: This usually isn't used due to getNextCard(), 	Shortcut so i never access the variables directly
	
	public void remove(int element){
		hand.remove(element);
	}
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Returns the card on the "top" of the deck (0) then removes it from the hand.
	public Card getNextCard(){
		Card nextCard = hand.get(0);
		remove();
		return nextCard;
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Returns the players hand
	// Note: Shortcut so i never access the variables directly
	public ArrayList<Card> getDeck(){
		return hand;
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Returns the size of the hand 
	// Note: Shortcut so i never access the variables directly
	public int handSize(){
		return hand.size();
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Creates a name for the player
	public void createName(){
		Scanner input = new Scanner(System.in);
		System.out.println("What is your name?");
		this.name = input.nextLine();
	}
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Returns the name of the player
	// Note: shortcut so I don't access variable directly
	public String getName(){
		return this.name;
	}
	
	// _~_~_~_~_~_~__~__~_~_WAR EXCLUSIVE METHODS_~_~_~__~_~_~_~_~__~_~_~
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Returns if the player is in a war
	public boolean inWar(){
		return inWar;
	}
	
	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Sets if the status is in a war or not
	public void setWarStatus(boolean warStatus){
		this.inWar = warStatus;
	}
	
	// _~_~_~_~_~_~__~__~_~_WAR EXCLUSIVE METHODS_~_~_~__~_~_~_~_~__~_~_~ 
	
	// _~_~_~_~_~_~__~__~_~_GO FISH EXCLUSIVE METHODS_~_~_~__~_~_~_~_~__~_~_~ 
	
	// Created 12/12/11
	// Returns the number of pairs a player has
	public int getNumOfPairs(){
		return pairs.size();
	}
	
	// Created 12/12/11
	// adds a pair to the players list of pairs
	public void addPair(ArrayList<Card> pair){
		pairs.add(pair);
	}
	
	// _~_~_~_~_~_~__~__~_~_GO FISH EXCLUSIVE METHODS_~_~_~__~_~_~_~_~__~_~_~ 
	
	
	// Date Created: 1/12/12
	// Description: Checks the mustLayCards variable
	public boolean mustLayCards(){
		return this.mustLayCards;
	}
	
	// Date Created: 1/12/12
	// Description: Sets the mustLayCards variable
	// Inputs: The status
	public void setMustLayCardsStatus(boolean mustLayCardsStatus, int amountOfCardsToLay){
		this.mustLayCards=mustLayCardsStatus;
		this.amountOfCardsToLay = amountOfCardsToLay;
	}
	
	public int getAmountOfCardsToLay(){
		return this.amountOfCardsToLay;
	}
	
	// Date Created: 12/11/11
	// Description: Sets the turn of the player to true or false
	// Inputs: the boolean value stating whether it's the players turn or not
	public void setTurn(boolean myTurn){
		this.myTurn=myTurn;
	}
	
	// Date Created: 12/11/11
	// Description: Checks to see if it's the players turn or not
	// Returns: boolean value to whether it's the players turn or not
	public boolean isTurn(){
		return myTurn;
	}
	
	// Date Created: 12/11/11
	// Description: Sets whether the player is in the current game or not
	// Inputs: boolean value to see if the player is in the game
	public void setInGameStatus(boolean status){
		this.inGame = status;
	}
	
	// Date Created: 12/11/11
	// Description: Checks to see if the player is in the game or not
	// Returns: boolean stating whether the player in the game or not
	public boolean inGame(){
		return inGame;
	}
	
	// Created on 12/12/11
	// For resetting variables if more than one game is played
	public void resetPlayerVariables(){
		pairs.clear();
		mustLayCards = false;
		inWar=false;
		myTurn=false; 
		inGame=false;
	}
}
