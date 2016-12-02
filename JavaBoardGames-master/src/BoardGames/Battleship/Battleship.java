package BoardGames.Battleship;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

import Tools.Game;
/**
 * This class plays the game of bsttleship
 * @author Dean_Johnson
 *
 */
public class Battleship extends Game{

	private final int MIN_PLAYERS = 2;
	private final int MAX_PLAYERS = 2;
	private BattleshipBoard p1, computer;
	private JFrame frame;
	
	/**
	 * Takes in 2 files for playing the game Battleship
	 * @param p1Board
	 * @param p2Board
	 */
	public Battleship(File p1Board, File p2Board){
		frame = new JFrame("Battleship Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			p1 = new BattleshipBoard(p1Board, "Dean", true, Color.GRAY);
			computer = new BattleshipBoard(p2Board, "Computer", false, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		frame.setSize(950, 550);
		GridLayout gl = new GridLayout(0, 2, 20, 0);
		frame.setLayout(gl);
		frame.add(p1);
		computer.hideShips();
		frame.add(computer);
		frame.setVisible(true);
		play("sequential_smart");// Available strategies are "random", "sequential", and "sequential_smart";
	}
	/**
	 * Plays a default game with a provided strategy for the AI
	 * @param strategy
	 */
	public void play(String strategy){
		BattleshipBoard[] boards = new BattleshipBoard[2];
		boards[0] = p1;
		boards[1] = computer;
		while(!p1.isGameOver() && !computer.isGameOver()){
			for(int i=0; i<2; i++){
				boards[i].setPlayable(true);
				while(!boards[i].firedCorrectly()){
					if(boards[i].implementsStrategy()){
						boards[i].playStrategicMove(strategy);
					}
				}
				boards[i].setPlayable(false);
			}
		}
		for(int i=0; i<2; i++){
			boards[i].setPlayable(false); 
		}
		
		JFrame winnerFrame = new JFrame("Winnerrrrr!");
		winnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel winner = new JLabel();
		winner.setSize(100, 400);
		for(int i=0; i<2; i++){
			if(!boards[i].isGameOver()){ //The board that has no battleships left is the one that lost! 
				winner.setText("Congratulations to "+boards[i].getOwner()+" for winning!");
			}
		}
		winnerFrame.setSize(400, 100);
		winnerFrame.add(winner, BorderLayout.CENTER);
		winnerFrame.setVisible(true);
	}

	// Created on 1/9/12
	// Used to see if people have enough players or too many
	public int getMinPlayers(){
		return MIN_PLAYERS;
	}
	
	// Returns the max amount of players for a Battleship game
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	// Returns the max amount of players for a Battleship game
	public String playerAmountMessage(){
		return "The amount of players that can play Battleship at once is 2."; 
	}
}
