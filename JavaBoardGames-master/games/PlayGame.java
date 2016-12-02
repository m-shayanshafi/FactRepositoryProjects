package games;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import conway.ConwaysGameOfLife;
import othello.Othello;
import checkers.Checkers;
import chess.Chess;

public class PlayGame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public PlayGame(){
		setSize(300,300);
		setLocationRelativeTo(null);
		setTitle("Select Games");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		JPanel panel = new JPanel();
		
		String[] gameList = {"Chess", "Game of Life", "Othello", "Checkers"};
		int count = gameList.length;
		int size = (count+1) / 2;

		panel.setLayout(new GridLayout(size,size));
		JButton[] b = new JButton[count];
		
		for(int i = 0; i < count; i++){
			b[i] = new JButton(gameList[i]);
			b[i].addActionListener(new selectGameAction());
			panel.add(b[i]);
		}
		add(panel);
	}
	
	class selectGameAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			callClassForButton(b.getText());
		}
		
		private void callClassForButton(String s){
			switch(s){
				case "Chess":
					new Chess();
					break;
				case "Game of Life":
					new ConwaysGameOfLife();
					break;
				case "Othello":
					new Othello();
					break;
				case "Checkers":
					new Checkers();
					break;
			}
			dispose();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new PlayGame();
			}
		});
	}
}
