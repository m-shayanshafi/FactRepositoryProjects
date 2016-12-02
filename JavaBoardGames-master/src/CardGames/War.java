package CardGames;
import java.util.ArrayList;
import java.util.Scanner;

import Tools.Card;
import Tools.CardDeck;
import Tools.CardGame;
import Tools.Player;

/**
 * Plays the classic card game 'War'.
 * @author Dean_Johnson
 *
 */
public class War extends CardGame{
	ArrayList<ArrayList<Card>> playerDecks = new ArrayList<ArrayList<Card>>();
	ArrayList<Player> players = new ArrayList<Player>();
	final int numOfPlayers;
	final private int MIN_PLAYERS = 2;
	final private int MAX_PLAYERS = 4;
	boolean isWar;
		
	// Date Created: 11/23/11
	// Date Modified: 11/27/11
	// Description: creates a war game with the default number of players
	public War(){
		numOfPlayers=2;
		for(int i=0; i<numOfPlayers; i++){
			playerDecks.add(new ArrayList<Card>());
		}// if i used arrayLists it could cycle through cards by adding a new card for the winner to the winner's, then removing old card.
		for(int i=0; i<numOfPlayers; i++){
			players.add(new Player(i+1));
		}
		CardDeck deck = new CardDeck();
		playerDecks = deck.deal(numOfPlayers);
		for(int i=0; i<numOfPlayers; i++){
			players.get(i).addAll(playerDecks.get(i));
		}
	}
	
	// Date Created: 11/23/11
	// Date Modified: 11/29/11
	// Description: Constructor that has the parameters for any number of players
	public War(int numOfPlayers){
		this.numOfPlayers=numOfPlayers;
		for(int i=0; i<numOfPlayers; i++){
			playerDecks.add(new ArrayList<Card>());
		}
		for(int i=0; i<numOfPlayers; i++){
			players.add(new Player(i+1));
		}
		CardDeck deck = new CardDeck();
		playerDecks = deck.deal(numOfPlayers);
		for(int i=0; i<numOfPlayers; i++){
			players.get(i).addAll(playerDecks.get(i));
		}
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: returns number of players in a war
	public int getNumOfPlayersInWar(ArrayList<Player> players){
		int playersInWar=0;
		for(int i=0; i<players.size(); i++){
			if(players.get(i).inWar())
				playersInWar++;
		}
		return playersInWar;
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: gets the cards from all players and puts them in the cards at stake
	public void getCardsFromPlayers(ArrayList<Player> players, ArrayList<Card> cardsAtStake, int numOfCardsToGet){
		for (int i=0; i<players.size();i++){
			if(players.get(i).hasCards()){
				for(int j=0; j<numOfCardsToGet; j++){
					try{
						cardsAtStake.add(players.get(i).getNextCard());
					}
					catch(IndexOutOfBoundsException e){
						System.out.println(players.get(i).getName()+" has ran out of cards!");
					}
				}
			}
		}
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: gets the cards from all players in a war and puts them in the cards at stake
	public void getCardsFromPlayersInWar(ArrayList<Player> players, ArrayList<Card> cardsAtStake, int numOfCardsToGet){
		for (int i=0; i<players.size();i++){
			if(players.get(i).hasCards()){
				if(players.get(i).inWar()){
					for(int j=0; j<numOfCardsToGet; j++){
						try{
							cardsAtStake.add(players.get(i).getNextCard());
						}
						catch(IndexOutOfBoundsException e){
							System.out.println(players.get(i).getName()+" has ran out of cards!");
						}
					}
				}
			}
		}
	}
	
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: prints the battle
	public void printBattle(ArrayList<Player> players, ArrayList<Card> cardsAtStake){
		int onPlayer = 0;
		for(int i=0; i<players.size();i++){ // I still use players.size() and not a new array so down the road I can add in a point system for players (Maybe with some file I/O!
			if(players.get(i).hasCards()){ // Gah, removing players from arraylist would be so much easier...
				if (onPlayer<getNumOfPlayersWithCards(players)-1)
					System.out.print(players.get(i).getName()+": "+cardsAtStake.get(onPlayer).getCardName()+" vs. ");
				else
					System.out.print(players.get(i).getName()+": "+cardsAtStake.get(onPlayer).getCardName()+"\n");
				onPlayer++;
			}
		}
	}
	
	// Date Created: 12/6/11
	// Date Modified: 12/6/11
	// Description: prints the battle (For a war)
	public void printWarBattle(ArrayList<Player> players, ArrayList<Card> cardsAtStake, int numOfPlayersInWar){
		int onWarPlayer=0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).inWar()){
				if (i<numOfPlayersInWar-1)
					System.out.println(players.get(i).getName()+": "+cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer).getCardName()+" vs. ");
				else
					System.out.print(players.get(i).getName()+": "+cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer).getCardName()+"\n");
				onWarPlayer++;
			}
		}
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: returns isWar's state
	public boolean getWar(){
		return isWar;
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: gets isWar's state
	public void setWar(boolean isWar){
		this.isWar=isWar;
	}
	
	// Date Created: 11/30/11
	// Date Modified: 11/30/11
	// Description: plays War
	public void play(){
		int playersWithCards=getNumOfPlayersWithCards(players);
		
		while (!isWinner(players)){
			ArrayList<Card> cardsAtStake = new ArrayList<Card>();
			
			printAllPlayersRemainingCards(players);
			
			getCardsFromPlayers(players, cardsAtStake, 1);

			printBattle(players, cardsAtStake);
			
			int currentHighest=-1;
			int winner = -1;
			
			Player winningPlayer= new Player();
			Card winningCard = new Card();
			
			isWar=false;
			ArrayList<Integer> playersInWar = new ArrayList<Integer>();
			int onPlayer=0;
			for(int i=0; i<players.size();i++){
				if (players.get(i).hasCards()){
					// Handles if there is a war
					if (cardsAtStake.get(onPlayer).getValue()==currentHighest){
						if(!players.get(winner).inWar()){
							players.get(winner).setWarStatus(true);
						}
						players.get(i).setWarStatus(true);
						setWar(true);
					}
					// End of handling if war occurs
					if (cardsAtStake.get(onPlayer).getValue()>currentHighest){
						for(int j=0; j<players.size(); j++){
							players.get(j).setWarStatus(false);
						}
						currentHighest=cardsAtStake.get(onPlayer).getValue();
						winningPlayer=players.get(i);
						winningCard=cardsAtStake.get(onPlayer);
						winner=i;
						setWar(false);
					}
					onPlayer++;
				}
			}
			isWar=getWar();
			
			if (isWar){
				
				System.out.println("WARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR!!!");
				while (isWar){
					
					int numOfPlayersInWar=getNumOfPlayersInWar(players);
					
					getCardsFromPlayersInWar(players, cardsAtStake, 3);
					
					if(isWinner(players))
						continue;
					
					getCardsFromPlayersInWar(players, cardsAtStake, 1);

					if (isWinner(players))
						continue;
					
					printWarBattle(players, cardsAtStake, numOfPlayersInWar);
					
					currentHighest=-1;
					winner = -1;
					
					int onWarPlayer=0;
					for(int i=0; i<players.size();i++){
						// Handles if there is another war
						if(players.get(i).inWar()){
							if (cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer).getValue()==currentHighest){
									if(!players.get(winner).inWar()){
										players.get(winner).setWarStatus(true);
									}
									players.get(i).setWarStatus(true);
									isWar=true;
							}
							// End of handling if another war occurs
							else if (cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer).getValue()>currentHighest){
								players.get(i).setWarStatus(false);
								currentHighest=cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer).getValue();
								isWar=false;
								winningPlayer=players.get(i);
								winningCard=cardsAtStake.get(cardsAtStake.size()-numOfPlayersInWar+onWarPlayer);
								winner=i;
							}
							else {
								players.get(i).setWarStatus(false);
							}
							onWarPlayer++;
						}
					}	
				}
			}
			System.out.println("Winning Card: "+winningCard.getCardName()+" from "+winningPlayer.getName()+"!!");
			playersInWar.clear();
			
			// why not just make an arrayList to add all cards at stake together, then remove them from each arraylist.
			// then winner gets all those cards... much much easier.
			
			players.get(winner).addAll(cardsAtStake);
			
			playersWithCards=getNumOfPlayersWithCards(players);
			
			Scanner input = new Scanner(System.in);
			input.nextLine();
			
		}	
	}
	// Date Created: 11/24/11
	// Date Modified: 11/24/11
	// Description:Returns if there is a winner.
	// Note This is used slightly, but done in many other ways.
	public boolean isWinner(ArrayList<Player> players){
		for(int i=0; i<players.size();i++){
			if (players.get(i).hasCards()){
				if (players.get(i).handSize()==52){
					System.out.println(players.get(i).getName()+", you win!!");
					return true;
				}
			}
		}
		return false;
	}
	
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
		return "The amount of players that can play War at once is "+MIN_PLAYERS+"-"+MAX_PLAYERS+"."; 
	}
}
