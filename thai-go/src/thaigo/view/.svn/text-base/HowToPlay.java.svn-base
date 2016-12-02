package thaigo.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import thaigo.utility.CenterDeterminer;
import thaigo.utility.ImageLoader;

/** Shows the rule of the game and the basic of the game.
 * 
 * @author Nol
 *
 */
public class HowToPlay {

	private static HowToPlay instance;
	private JFrame frame;
	private JPanel[] theRules;
	private JLabel[] pictures;
	private JLabel[] descriptions;
	private JPanel button;
	private JButton back,close,next;
	
	private GameUI ui;
	private final int PAGES = 4;
	private int currentPage;
	
	/** Get the instance of this object if it's already declared.
	 *  Singleton so user can't call many HowToPlay window.
	 * 
	 * @param ui GameUI
	 * @return instance of this object
	 */
	public static HowToPlay getInstance(GameUI ui){
		if(instance == null){
			instance = new HowToPlay(ui);
		}
		return instance;
	}
	
	/** Initializes the <code>HowToPlay</code> window.
	 * 
	 * @param ui The GameUI that call this
	 */
	private HowToPlay(GameUI ui){
		frame = new JFrame("How To Play ??");
		frame.setIconImage( (new ImageLoader("images/help.png")).getImage() );
		currentPage = 1;
		this.ui = ui;
		initComponent();
	}
	
	/** Initializes the components. */
	private void initComponent() {
		ActionListener listener = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if( e.getSource() == close ){
					frame.dispose();
				}
				if( e.getSource() == next ){
					if( currentPage + 1 <= PAGES){
						back.setEnabled(true);
						currentPage++;
						if(currentPage == PAGES)
							next.setEnabled(false);
						theRules[currentPage-2].setVisible(false);
						frame.add( theRules[currentPage - 1], BorderLayout.CENTER );
					}
				}
				if( e.getSource() == back ){
					if( currentPage - 1 >= 1){
						next.setEnabled(true);
						currentPage--;
						if(currentPage == 1)
							back.setEnabled(false);
						theRules[currentPage - 1].setVisible(true);
					}
				}
			}
			
		};
		
		theRules = new JPanel[4];
		pictures = new JLabel[4];
		descriptions = new JLabel[4];
		for(int k = 0; k < theRules.length ; k++){
			theRules[k] = new JPanel();
			
		}
		for(int k = 0; k < pictures.length ; k++){
			pictures[k] = new JLabel();
			pictures[k].setPreferredSize( new Dimension(150,150) );
		}
		for(int k = 0; k < descriptions.length ; k++){
			descriptions[k] = new JLabel();
			descriptions[k].setPreferredSize( new Dimension(200,150) );
			descriptions[k].setBorder( new BevelBorder(0));
		}
		
		pictures[0].setIcon( (new ImageLoader("images/logo.png",150,100)).getImageIcon());
		descriptions[0].setText("<html><u>Thai Go! (mak neeb)</u><br> is played on a chessboard, a square board divided into 64 squares (eight-by-eight) of alternating " +
				"color, which is similar to that used in draughts. " + 
				"At the beginning of the game, pawn are arranged at bottom and top of the chessboard.</html>");
		
		pictures[1].setIcon( (new ImageLoader("images/legalmove.png",150,100)).getImageIcon());
		descriptions[1].setText("<html><u>Move</u><br>pawn move like rook in kingdom chess.</html>");
		
		pictures[2].setIcon( (new ImageLoader("images/eli1.png",150,100)).getImageIcon() );
		descriptions[2].setText("<html><u>Eliminate</u><br> When we move our pawn make pincer movement of enermy that<br> enermy's pawn is" +
								" eiliminate and If we put our pawn between 2 of<br> enermy's pawn , enermy's pawn will eiliminate.</html>");
		
		pictures[3].setIcon( (new ImageLoader("images/crown.png",150,100)).getImageIcon() );
		descriptions[3].setText("<html><u>Win/Lose</u><br> You win when enermy doesn't have any pawn in there control." + 
								"You lose when you don't have any pawn in there control." + 
								"Draw both of your can't eliminate enermy pawn.</html>");
		
		for(int k = 0 ; k < theRules.length ; k++){
			theRules[k].add(pictures[k]);
			theRules[k].add(descriptions[k]);
		}
		
		button = new JPanel();
		back = new JButton(" << ");
		back.addActionListener(listener);
		back.setEnabled(false);
		close = new JButton(" CLOSE ");
		close.addActionListener(listener);
		next = new JButton(" >> ");
		next.addActionListener(listener);
		button.add(back);
		button.add(close);
		button.add(next);
		
		/*for(int k = 0 ; k < theRules.length ; k++)
			frame.add(theRules[k], BorderLayout.CENTER);
			/*
		theRules[1].setVisible(false);
		theRules[2].setVisible(false);
		theRules[3].setVisible(false);
		theRules[0].setVisible(true);
		*/
		frame.add(theRules[0], BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
	}
	
	/** Shows this window and set its location related to GameUI. */
	public void run(){
		frame.pack();
		frame.setLocation( CenterDeterminer.determine(ui, frame));
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
