package BoardGames;
import java.util.ArrayList;

/**
 * Author: Dean Johnson
 * Date: November 7, 2011
 * Description: Class that handles the gameplay for Hangman. 
 * It is where everything is executed while playing hangman 
 * as well.
 **/

import java.util.Scanner;
public class Hangman {
	Scanner input;
	String secretWord;
	int remainingAttempts;
	char[] word;
	char letter;
	ArrayList<Character> usedLetters;
	
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description:Constructor; Initializes variables
	public Hangman(){
		input = new Scanner(System.in);
		secretWord="";
		remainingAttempts=7;
		letter = ' ';
		usedLetters=new ArrayList<Character>();
	
		initBoard();
	}
	
	// Created: 11/8/2011
	// Last Modified: 11/14/2011
	// Description: Constructor; Initializes variables
	public Hangman(String secretWord){
		input = new Scanner(System.in);
		if(validSecretWord(secretWord)){
			this.secretWord=secretWord;
		}
		else initBoard();
		remainingAttempts=7;
		letter = ' ';
		usedLetters=new ArrayList<Character>();
		
		initBoard();
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Main method for a user to play the game
	public void play(){
		while (remainingAttempts>=1 && !wordComplete()){
			printBoard();
			p("What letter would you like to guess?: ");
			letter=input.next().charAt(0); // My own way of a Scanner.nextChar() 
			// Decides which action to take depending on the letter chosen
			
			if (!letterNotUsed(letter)){
				p("Dude, you already used this letter.");
			}
			else if (isNumber(letter)){
				p("Numbers can not be in the word. Try again.");
			}
			else if(isMatch(letter)){
				usedLetters.add(letter);
				replaceLetters(letter);
			}
			else {
				p("Sorry, the letter was not in the word. ");
				usedLetters.add(letter);
				remainingAttempts--;
			}
		}// end of while
		if(wordComplete()){
			printBoard();
			p("You won! Congratulations!");
		}
		else {
			p("The word was: "+secretWord);
			p("You ran out of attempts, sorry.");
		}
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Initializes the board
	public void initBoard(){
		while (validSecretWord(secretWord)==false) {
			if(secretWord.equals("")){
				p("What is the secret word you would like to have someone guess?(Between 3 and 13 characters long with no numbers)");
				secretWord=input.next();
				input.nextLine();
			}
			else{
				p("You entered an invalid word to guess! Choose a word between 3 and 13 characters long with NO numbers: ");
				secretWord=input.next();
				input.nextLine();
			}
		} 
		word = new char[secretWord.length()];
		
		for (int i=0;i<secretWord.length(); i++){
			word[i]='_';
		}
	}
	// Created: 11/11/2011
	// Last Modified: 11/11/2011
	// Description: Checks to make sure the secret word isn't too big/small and does not have numbers
	public boolean validSecretWord(String sWord){
		if(sWord.length() < 3 || sWord.length() > 13)
			return false;
		else if(containsNumbers(sWord))
			return false;
		else 
			return true;
	}
	// Created: 11/11/2011
	// Last Modified: 11/11/2011
	// Description: Checks to see if the string contains numbers
	public boolean containsNumbers(String s){
		for (int i=0; i<10; i++){
			if (s.contains(""+i))
				return true;
		}
		return false;
	}
	// Created: 11/11/2011
	// Last Modified: 11/11/2011
	// Description: Checks to see if the character is a numer
	public boolean isNumber(char c){
		for (int i=0; i<10; i++){
			if(c==Integer.toString(i).charAt(0))
				return true;
		}
		return false;
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Checks to see if the letter the user entered is used.
	public boolean letterNotUsed(char letter){
		for (int i=0; i<usedLetters.size();i++){
			if (letter==usedLetters.get(i))
				return false;
		}
		return true;
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Checks to see if there is a match between the user's guess and the secret word
	public boolean isMatch(char letter){
		for(int i=0; i<secretWord.length();i++){
			if (secretWord.charAt(i)==letter)
				return true;
		}
		return false;
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Replaces letters that were found in the word that is being guessed
	public void replaceLetters(char letter){
		for(int i=0; i<secretWord.length();i++){
			if (secretWord.charAt(i)==letter){
				word[i]=letter;
			}
		}
	}
	// Created: 11/8/2011
	// Last Modified: 11/14/2011
	// Description: Prints the uncompleted word and attempts left
	public void printBoard(){
		p("You have "+remainingAttempts+" atttempts left.");
		p("Used Letters: ", false);
		for(int i=0; i<usedLetters.size();i++){
			if (i<usedLetters.size()-1)//Fancy output to use to be gramatically correct. (Displays info to user)
				p(usedLetters.get(i)+", ", false);
			else 
				p(""+usedLetters.get(i), true);
		}
		for (int i=0; i<secretWord.length(); i++){
			p(word[i]+" ", false);
		}
		p("");
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Checks to see if the word is completed
	public boolean wordComplete(){
		for (int i=0; i<secretWord.length(); i++){
			if (secretWord.charAt(i)!=word[i])
				return false;
		}
		return true;	
	}
	// Created: 11/8/2011
	// Last Modified: 11/8/2011
	// Description: Shorthand for printing data
	public void p(String s){
		System.out.println(s);
	}
	// Created: 11/14/2011
	// Last Modified: 11/14/2011
	// Description:Shorthand for printing data, with a boolean variable to determine if a carriage return occurs
	public void p(String s, boolean newLine){
		System.out.print(s);
		if (newLine)
			System.out.println();
	}
}
