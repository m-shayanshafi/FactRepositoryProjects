package Main;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import BoardGames.Battleship.Battleship;
import BoardGames.Hangman;
import BoardGames.TicTacToe;
import CardGames.EgyptianRatscrew;
import CardGames.GoFish;
import CardGames.War;
import Tools.Game;

/**
* Author: Dean Johnson
* Date: November 7, 2011
* Description: Class that handles the executing 
* of games based on the user input.
**/
public class PlayGames {
	
	//Possibility to load player profiles and then merely count how many people are playing. (dropdown menu?)

	// Created: 11/7/2011
	// Last Modified: 11/15/2011
	// Description: Constructor for PlayGame, lets the user play the game they wish to play.
	public PlayGames(String args[]){
		String[] availableGames={"Tic Tac Toe", "Hangman", "War (The Card Game)", "Go Fish", "Egyptian Ratscrew", "Battleship"};
		Scanner input = new Scanner(System.in);
		String game, answer;
		System.out.println("Hi there! Welcome to my Game Suite! Do you want to play a game?");
		answer = input.next();
		input.nextLine();
		while(answer.equals("yes")||answer.equals("Yes")){
				System.out.print("Our available games are: ");

				for (int i=0; i<availableGames.length; i++){
					System.out.print(availableGames[i]);
					if(i==availableGames.length-2)
						System.out.print(", and ");
					else if(i<availableGames.length-1 && availableGames.length!=2)
						System.out.print(", ");
					else
						System.out.print(".");
				}
				System.out.print("\nWhich of these games would you like to play?: ");
				game = input.next();
				input.nextLine();
				System.out.println();

				if (game.toLowerCase().equals("tic")){
					TicTacToe t = new TicTacToe();
					t.play();
				}
				else if(game.toLowerCase().equals("hangman")){
					Hangman h = new Hangman();
					h.play();
				}
				else if(game.toLowerCase().equals("war")){
					War w = new War(getNumOfPlayersPlaying(new War()));
					w.play();
				}
				else if(game.toLowerCase().equals("go")){
					GoFish f = new GoFish(getNumOfPlayersPlaying(new GoFish()));
					f.play();
				}
				else if(game.toLowerCase().equals("egyptian")){
					EgyptianRatscrew e = new EgyptianRatscrew(getNumOfPlayersPlaying(new EgyptianRatscrew()));
					e.play();
				}
				else if(game.toLowerCase().equals("battleship")){
					Battleship b = new Battleship(new File(args[0]), new File(args[1]));
				}
				System.out.print("Would you like to play another game?: ");
				answer=input.next();
				input.nextLine();
		}// end of while loop
		System.exit(0);
	} // Lets comment this method and add dates and etc. Definitely needs to be done
	public int getNumOfPlayersPlaying(Game game){
		System.out.println(game.playerAmountMessage());
		System.out.println("And how many of you will be playing today?");
		Scanner input = new Scanner(System.in);
		int numOfRequestedPlayers = input.nextInt();
		input.nextLine();
		if(!isValidNumOfPlayers(numOfRequestedPlayers, game)){
			if(numOfRequestedPlayers<game.getMinPlayers()){
				System.out.println("You have too few players to play this game! \n" +
						"Please gather more players and re-run the program with the correct number of players.");
			}
			else
				System.out.println("You have too many players to play this game! \n" +
				"Please choose who is playing and re-run the program with the correct number of players.");
			System.exit(0);
		}
		
		return numOfRequestedPlayers;

	}
	
	public boolean isValidNumOfPlayers(int n, Game g){
		return n >= g.getMinPlayers() && n <= g.getMaxPlayers(); 
	}
}
