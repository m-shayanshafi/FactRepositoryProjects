package CardGames;
import java.util.ArrayList;
import java.util.Scanner;

import Tools.Card;
import Tools.CardDeck;
import Tools.CardGame;
import Tools.Player;


public class EgyptianRatscrew extends CardGame{

	private final int MIN_PLAYERS = 2;
	private final int MAX_PLAYERS = 5;
	private ArrayList<Player> players = new ArrayList<Player>(); 
	private ArrayList<ArrayList<Card>> playerDecks = new ArrayList<ArrayList<Card>>();
	private ArrayList<Card> pile = new ArrayList<Card>();
	private boolean someoneHasCards = true;
	// Would be a fun game to program :D
	public EgyptianRatscrew() {
		for(int i=0; i<MIN_PLAYERS; i++){
			players.add(new Player(i+1));
		}
		for(int i=0; i<players.size(); i++){
			playerDecks.add(new ArrayList<Card>());
		}
		CardDeck deck = new CardDeck();
		playerDecks = deck.deal(players.size());
		for(int i=0; i<players.size(); i++){
			players.get(i).addAll(playerDecks.get(i));
		}
	}
	
	public EgyptianRatscrew(int numOfPlayers) {
		for(int i=0; i<numOfPlayers; i++){
			players.add(new Player(i+1));
		}
		for(int i=0; i<players.size(); i++){
			playerDecks.add(new ArrayList<Card>());
		}
		CardDeck deck = new CardDeck();
		playerDecks = deck.deal(players.size());
		for(int i=0; i<players.size(); i++){
			players.get(i).addAll(playerDecks.get(i));
		}
	}
	// Created on 1/10/12
	// Plays a default game of Egyptian Ratscrew
	public void play(){
		Scanner input = new Scanner(System.in);
		p(printWelcomeMessage("Egyptian Ratscrew", players.size(), players));
	
		while (someoneHasCards){
			String playersWithCards = "";
			for(int i=0; i<players.size(); i++){
				if(players.get(i).hasCards()){
					playersWithCards+=(i);
				}
			}
			if(playersWithCards.length()<2){
				p(players.get(Integer.parseInt(playersWithCards)).getName()+" has won!");
				break;
			}
			for(int i=0; i<players.size(); i++){ // TODO:Add else statement then just continue game development
				if(players.get(i).hasCards()){
					if(!players.get(i).mustLayCards()){
						pile.add(players.get(i).getNextCard());
						p(players.get(i).getName()+ " played a "+getTopCard().getCardName()+" onto the pile!");
						if(isFaceCard(getTopCard())){
							getNextPlayer(i).setMustLayCardsStatus(true, getNumOfCardsToLay(getTopCard()));
						}
					}
					else {
						boolean playerSurvived = false;
						for(int j=0; j<players.get(i).getAmountOfCardsToLay(); j++){
							pile.add(players.get(i).getNextCard());
							p(players.get(i).getName()+ " played a "+getTopCard().getCardName()+" onto the pile!");
							if(isFaceCard(getTopCard())){
								getNextPlayer(i).setMustLayCardsStatus(true, getNumOfCardsToLay(getTopCard()));
								playerSurvived=true;
								break;
							}
							else if(!players.get(i).hasCards()){
								p(players.get(i).getName()+" has ran out of Cards!!");
								break;
							}
						}
						if(!playerSurvived){
							getPreviousPlayer(i).addAll(pile);
							pile.clear();
						}
						players.get(i).setMustLayCardsStatus(false, 0);
					}
				}
				else {
					p(players.get(i).getName()+ "has ran out of cards!!");
				}
				
				printAllPlayersRemainingCards(players);
				input.nextLine();
			}
		}
	}
	
	public boolean isFaceCard(Card card){
		return card.getValue() > 10 ? true:false;
	}
	
	public int getNumOfCardsToLay(Card card){
		return card.getValue() - 10;
	}
	
	// Created on 1/9/12
	// Used to see if people have enough players or too many
	public int getMinPlayers(){
		return MIN_PLAYERS;
	}
	
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	public String playerAmountMessage(){
		return "The amount of players that can play Egyptian Ratscrew at once is "+MIN_PLAYERS+"-"+MAX_PLAYERS+"."; 
	}
	
	public Player getNextPlayer(int onPlayer){
		return onPlayer==players.size()-1 ? players.get(0) : players.get(onPlayer+1);
	}
	
	public Player getPreviousPlayer(int onPlayer){
		return onPlayer==0 ? players.get(players.size()-1) : players.get(onPlayer-1);
	}
	
	public Card getTopCard(){
		return pile.get(pile.size()-1);
	}
}
