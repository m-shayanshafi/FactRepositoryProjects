package CardGames;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Tools.Card;
import Tools.CardDeck;
import Tools.CardGame;
import Tools.Player;

/** Plays GoFish
 * TODO:Finish this game
 * @author Dean_Johnson
 *
 */
// Coding it for 2 players currently
// TODO: Check out the incorrectGuess method, how it checks players for cards in general -- need to check for value instead of same card
// TODO: Make it pairs are added if someone is handed a pair of cards
public class GoFish extends CardGame{
	CardDeck deck;
	ArrayList<ArrayList<Card>> playerDecks = new ArrayList<ArrayList<Card>>();
	ArrayList<Card> pile = new ArrayList<Card>();
	ArrayList<Player> players = new ArrayList<Player>();
	final private int MIN_PLAYERS = 2;
	final private int MAX_PLAYERS = 6;
	int numOfPlayers;
	
	
	public GoFish(){
		numOfPlayers=2;
		for(int i=0; i<numOfPlayers; i++){
			playerDecks.add(new ArrayList<Card>());
		}// if i used arrayLists it could cycle through cards by adding a new card for the winner to the winner's, then removing old card.
		for(int i=0; i<numOfPlayers; i++){
			players.add(new Player(i+1));
		}
		deck = new CardDeck();
		playerDecks = deck.deal(numOfPlayers, 6);
		for(int i=0; i<numOfPlayers; i++){
			players.get(i).addAll(playerDecks.get(i));
		}
		pile.addAll(playerDecks.get(playerDecks.size()-1));
	}
	
	public GoFish(int numOfPlayers){
		for(int i=0; i<numOfPlayers; i++){
			playerDecks.add(new ArrayList<Card>());
		}// if i used arrayLists it could cycle through cards by adding a new card for the winner to the winner's, then removing old card.
		for(int i=0; i<numOfPlayers; i++){
			players.add(new Player(i+1));
		}
		deck = new CardDeck();
		playerDecks = deck.deal(numOfPlayers, 6);
		for(int i=0; i<numOfPlayers; i++){
			players.get(i).addAll(playerDecks.get(i));
		}
		pile.addAll(playerDecks.get(playerDecks.size()-1));
		
	}
	
	public void play(){
		p(printWelcomeMessage("Go Fish", players.size(), players));
		
		p(null); // Notice which print method this uses -> first one it encounters in code
		p("We play go fish by dealing 6 cards to everyone, then creating a deck of cards for everyone to draw from.");
		p("Next, a player will ask another player if they have a card and if they do, the card is given to the player who asked");
		p("If not, the player who asked must \"Go Fish\" and draw a card. It then proceeds to the next players turn.");
		
		for(Player p : players)
			handleMatches(p);
		
		Scanner input = new Scanner(System.in);
		while(!gameOver()){
			for(int i=0; i<players.size(); i++){
				printPlayerInfo(i);
				printPlayersCards(players.get(i));
				int indexOfCard;
				boolean error=false;
				do{
					if(error)
						p("You entered an incorrect index! Please try again.");
					
					p("Which card do you want to check to see if anyone else has? (Enter the number displayed by the card):");
					indexOfCard = input.nextInt()-1;
					if(indexOfCard<0||indexOfCard>players.get(i).handSize()-1)
						error=true;
					else
						error = false;
					
				}while(error);
				p("");
				int indexOfPlayer;
				do{
					if(error)
						p("You entered an incorrect index! Please try again.");
					
					p("Which player do you think has "+players.get(i).getDeck().get(indexOfCard).getCardName()+"?:");
					indexOfPlayer = input.nextInt();
					indexOfPlayer--;
					if(indexOfPlayer<0||indexOfPlayer>players.size()-1 || indexOfPlayer==i)
						error=true;
					else
						error = false;
					
				}while(error);
				
				if(incorrectGuess(players.get(i).getDeck().get(indexOfCard), players.get(indexOfPlayer))){
					p("Go Fish!");
				if(pile.size()>0){
						players.get(i).add(pile.get(0));
						pile.remove(0);
					}
				}
				else{
					p("Nice job! "+players.get(indexOfPlayer).getName()+" had a "+players.get(i).getDeck().get(indexOfCard).getCardName());
					int valueOfCard = players.get(i).getDeck().get(indexOfCard).getValue();
					int index = getIndexOfMatchingCard(valueOfCard, players.get(indexOfPlayer));
					players.get(i).add(players.get(indexOfPlayer).getDeck().get(index));
					players.get(indexOfPlayer).remove(index);
				}
				
				handleMatches(players.get(i));
				
				p("You currently have "+players.get(i).getNumOfPairs()+" pairs and there are "+pile.size()+" cards left in the deck!");
				
				if(gameOver()){
					break;
				}
			}
		}
		
		ArrayList<Integer> winningPlayerIndexes = new ArrayList<Integer>();
		int highNumOfPairs = -1;
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getNumOfPairs()>highNumOfPairs){
				winningPlayerIndexes.clear();
				winningPlayerIndexes.add(i);
			}
			else if(players.get(i).getNumOfPairs()==highNumOfPairs){
				winningPlayerIndexes.add(i);
			}
		}
		
		p("Congratulations to ", false);
		for(int i=0; i<winningPlayerIndexes.size(); i++){
			p(players.get(winningPlayerIndexes.get(i)).getName(), false);
			if(winningPlayerIndexes.size()>0){
				if(i!= winningPlayerIndexes.size()-1){
					p(", ", false);
				}
				else{
					p(" and ", false);
				}
			}
		}
		p(" for winning the game!");
		// evaluate the winner based on num of pairs per person
		
		return;
	}
	
	public void printPlayersCards(Player p){
		p("");
		p("Your current cards are: ");
		for(int j=0; j<p.handSize(); j++){
			p((j+1)+": "+p.getDeck().get(j).getCardName(), false);
			if(j!=p.handSize()-1){
				p(", ", false);
			}
			if(j%2!=0 && j!=p.handSize()-1){
				p("");
			}
		}
		p("");
	}
	
	public boolean gameOver(){
		for(int i=0; i<players.size(); i++){
			if(players.get(i).handSize()==0){
				return true;
			}
		}
		return false;
	}
	// This is fucking confusing, I have no idea how i made this work.
	public void handleMatches(Player p){
		HashMap <Integer, Integer> nonpairs = new HashMap<Integer, Integer>();
		
		for(int i=0; i<p.handSize(); i++){
			if(nonpairs.containsKey((Integer)p.getDeck().get(i).getValue())){
				ArrayList<Card> pair = new ArrayList<Card>();
				pair.add(p.getDeck().get(i));
				pair.add(p.getDeck().get(nonpairs.get((Integer)p.getDeck().get(i).getValue())));
				p.remove(nonpairs.get(p.getDeck().get(i).getValue()));
				nonpairs.remove((Integer)p.getDeck().get(i-1).getValue());
				p.remove(i-1);
				i-=2;
				p.addPair(pair);
			}
			else{
				nonpairs.put((Integer)p.getDeck().get(i).getValue(), i);
			}
		}
	}
	
	public void printPlayerInfo(int currentPlayersTurn){
		p("Information on all players:");
		for(int i=0; i<players.size(); i++){
			if(i!=currentPlayersTurn){
				p(players.get(i).getName()+" has "+players.get(i).handSize()+" cards.");
			}
		}
	}
	
	// Created 12/14/11
	// Returns if the guess is appropriate or not
	public boolean incorrectGuess(Card guess, Player playerGuessingAt){
		printPlayersCards(playerGuessingAt);
		for(int i=0; i<playerGuessingAt.handSize(); i++){
			if(guess.getValue() == playerGuessingAt.getDeck().get(i).getValue()){
				p(playerGuessingAt.getDeck().get(i).getCardName()); // Compare values not name of card... duh
				return false;
			}
		}
		// Still need to take that card from a player -- will do that somewhere else, this should just evaluate the guess
		return true;
	}
	
	public int getIndexOfMatchingCard(int cardValue, Player playerGuessingAt){
		for(int i=0; i<playerGuessingAt.handSize(); i++){
			if(cardValue == playerGuessingAt.getDeck().get(i).getValue())
				return i;
		}
		return -1;
	}
	
	// Created 12/12/11
	// Checks to see if a player is out of cards
	public boolean isWinner(ArrayList<Player> players){
		for(int i=0; i<players.size(); i++){
			if(!players.get(i).hasCards())
				return true;
		}
		return false;
	}
	
	// Created 12/12/11
	// Sends which player won
	public Player getWinner(ArrayList<Player> players){
		for(int i=0; i<players.size(); i++){
			if(!players.get(i).hasCards())
				return players.get(i);
		}
		return null;
	}
	// Will have to do a check within the play method to make sure im not returning null
	// Created 12/12/11
	// Finds a player based on their name
	public Player getPlayerFromName(String s, ArrayList<Player> players){
		for(int i=0; i<players.size(); i++){
			if(s.equals(players.get(i).getName()))
				return players.get(i);
		}
		System.out.println("Could not find a player by the name of \""+s+"\"");
		return null;
	}
	
	// Created on 12/11/11 (All 3 methods)
	
	
	// Created on 1/2/12
	// Used to see if people have enough players or too many
	public int getMinPlayers(){
		return MIN_PLAYERS;
	}
	
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	// Created on 1/2/12
	// Used to display to user amount of people that can play the game.
	public String playerAmountMessage(){
		return "The amount of players that can play Go Fish at once is "+MIN_PLAYERS+"-"+MAX_PLAYERS+"."; 
	}
}
