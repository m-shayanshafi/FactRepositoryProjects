package Output;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Puzzle.DisplayComponent;
import TicTacToe.PlayTicTacToe;

import Chess.ChessComponent;

public class Output {
	
	private class ChessListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Thread t = new Thread(new ChessComponent());
			t.start();
		}
	}
	
	private class TicTacToeListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Thread t = new Thread(new PlayTicTacToe());
			t.start();
		}
	}
	
	private class PuzzleListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Thread t = new Thread(new DisplayComponent());
			t.start();
		}
	}
	
	
	public void addButtons(JFrame f){
		JButton chessButton = new JButton("Play Chess");
		chessButton.addActionListener(new ChessListener());
		
		JButton ticTacToeButton = new JButton("Play Tic-Tac-Toe");
		ticTacToeButton.addActionListener(new TicTacToeListener());
		
		JButton puzzleButton = new JButton("Play Puzzle");
		puzzleButton.addActionListener(new PuzzleListener());
		
		JPanel p = new JPanel(new FlowLayout());
	
		p.add(chessButton);
		p.add(ticTacToeButton);
		p.add(puzzleButton);
		
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	
	public static void main(String[] args){
		JFrame games = new JFrame("Choose Your Game");
		games.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Output o = new Output();
		o.addButtons(games);
		
	}
}
