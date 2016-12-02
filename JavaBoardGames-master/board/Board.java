package board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class Board extends JFrame{
	private static final long serialVersionUID = 1L;
	protected int maxSize;
	private Cell[][] cell;
	JLabel displayTurn, displayNotification;
	protected Color currentPlayer, player1, player2;

	public Board(Color p1, Color p2, int size, Color m, Color n) {
		maxSize = size;
		cell = new Cell[maxSize][maxSize];
		player1 = p1;
		player2 = p2;
		displayTurn = new JLabel();
		displayNotification = new JLabel();

		JPanel panelTop = new JPanel();
		panelTop.setBackground(Color.gray);
		panelTop.add(displayTurn);

		JPanel panelBottom = new JPanel();
		panelBottom.setBackground(Color.gray);
		panelBottom.add(displayNotification);
		
		setSize(maxSize*70,maxSize*70);
		setLocationRelativeTo(null);
		setTitle("Board Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		add(panelTop,BorderLayout.NORTH);
		add(getBoard(m,n));
		add(panelBottom,BorderLayout.SOUTH);
		
		setPlayer(player1);

		initialSetup();
	}

	public void initialSetup() {}
	
	private JPanel getBoard(Color m, Color n){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(maxSize,maxSize));
				
		for(int i=0; i<maxSize; i++){
			for(int j=0; j<maxSize; j++){
				Color c = (i+j)%2 == 0 ? m : n;
				cell[i][j] = newCell(i,j,c);
				cell[i][j].addActionListener(getCellActionListener(this,i,j));
				
				panel.add(cell[i][j]);
			}
		}	
		
		return panel;
	}

	public Cell newCell(int i, int j, Color c){
		Cell temp = new Cell(i,j,c);
		return temp;
	}

	abstract public ActionListener getCellActionListener(Board b, int i, int j);
	
	public Cell getCell(int i, int j){
		if(i<maxSize && i>=0 && j>=0 && j<maxSize)
			return cell[i][j];
		else
			return null;
	}
	
	public Cell nextCell(Indexer i){
		return getCell(i.nextRow(), i.nextCol());
	}
	
	public void setPlayer(Color c){
		currentPlayer = c;
		displayTurn.setText("Player "+ (c == player1 ? "1" : "2") +"'s Turn");
		displayTurn.setForeground(c);
	}
	
	public void changeTurn(){
		setPlayer(currentPlayer == player1 ? player2 : player1);
	}

	public Color getPlayer(){
		return currentPlayer;
	}
	
	public boolean isPlayer1(Color c){
		return c == player1 ? true : false;
	}
}
