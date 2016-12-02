package Tools;
import java.util.ArrayList;



/**
* Author: Dean Johnson
* Date: November 23, 2011
* Description: Class that uses an Array of Cards to create a deck,
* and then has multiple functions for that deck. I.e. shuffling and dealing.
**/

public class CardDeck extends Card{
	Card[] deck;
	
	// Date Created: 11/23/11
	// Date Modified: 11/29/11
	// Description: Creates a new CardDeck (Constructor)
	public CardDeck(){
		deck = new Card[52];
		initDeck();
		shuffle();
	}
	
	// Date Created: 11/23/11
	// Date Modified: 11/29/11
	// Description:Assigns the right values and descriptions to Cards in the deck.
	private void initDeck(){
		String[] types={"Hearts", "Diamonds", "Clubs", "Spades"};
		for (int i=0;i<13;i++){
			for(int j=0; j<4; j++){
				deck[i+j*13]=new Card(i+2, types[j]);
			}
		}
	}
	
	// Date Created: 11/23/11
	// Date Modified: 11/26/11
	// Description:Returns multiple decks with sizes as equal as possible.
	public ArrayList<ArrayList<Card>> deal(int numOfPlayers){
		ArrayList<ArrayList<Card>> deck = new ArrayList<ArrayList<Card>>(); 
		for(int i =0; i<numOfPlayers; i++){
			deck.add(new ArrayList<Card>());
		}
		int i=0;
		while (true){
			for(int j=0; j<numOfPlayers; j++, i++){
				deck.get(j).add(this.deck[i]);
				if(i==51)
					return deck;
			}
		}
	}
	
	// Date Created: 11/23/11
	// Date Modified: 11/26/11
	// Description:Returns multiple decks with sizes as equal as possible. The last arraylist of cards
	// in the deck is the pile to draw from.
	public ArrayList<ArrayList<Card>> deal(int numOfPlayers, int cardsPerPlayer){
		ArrayList<ArrayList<Card>> deck = new ArrayList<ArrayList<Card>>(); 
		for(int i =0; i<=numOfPlayers; i++){
			deck.add(new ArrayList<Card>());
		}
		// Last arraylist will be remaining deck
		int i=0;
		while (true){
			for(int j=0; j<numOfPlayers; j++, i++){
				if(deck.get(j).size()==cardsPerPlayer)
					deck.get(deck.size()-1).add(this.deck[i]);
				else
					deck.get(j).add(this.deck[i]);
				if(i==51)
					return deck;
			}
		}
	}
	
	// Date Created: 11/23/11
	// Date Modified: 11/23/11
	// Description:Shuffles the deck
	public void shuffle(){
		int randNum=0;
		Card holdSpot = new Card();
		for (int i=0; i<deck.length; i++){
			randNum=((int)(51 * Math.random()));
			holdSpot=deck[i];
			deck[i]=deck[randNum];
			deck[randNum]=holdSpot;
		}
	}

	// Date Created: 11/26/11
	// Date Modified: 11/26/11
	// Description:Prints the contents of the deck
	public void printDeck(){
		for (Card c : deck)
			System.out.println(c.getCardName());
	}
	
}
