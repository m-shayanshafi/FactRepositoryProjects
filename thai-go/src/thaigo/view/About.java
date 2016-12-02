package thaigo.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import thaigo.utility.CenterDeterminer;
import thaigo.utility.ImageLoader;

/** About the developers.
 *  This class is singleton so user cannot open this more than one at the same time.
 * 
 * @author Nol
 */
public class About {
	
	private DialogFrame frame;
	private static About instance;
	JLabel gameTitle;
	JLabel teamName,nol,bat,ice,kaset,faculty;
	JLabel version;
	JButton close;
	GameUI ui;
	
	/** Initializes the <code>About</code> window.
	 * 
	 * @param ui The GameUI that call this
	 */
	private About(GameUI ui){
		frame = new DialogFrame("About");
		frame.setLayout( new GridBagLayout() );
		this.ui = ui;
		initComponent();
	}
	
	/** Get the instance of this object if it's already declared.
	 *  Singleton so user can't call many About window.
	 * 
	 * @param ui GameUI
	 * @return instance of this object
	 */
	public static About getInstance(GameUI ui){
		if( instance == null){
			instance = new About(ui);
		}
		return instance;
	}

	/** Initializes components. */
	private void initComponent() {
		ImageLoader icon = new ImageLoader("images/help.png");
		frame.setIconImage(icon.getImage());
		JPanel theTitle = new JPanel();
		ImageLoader logo = new ImageLoader("images/logo.png",355,133);
		gameTitle = new JLabel(logo.getImageIcon());
		gameTitle.setFont( new Font( Font.SANS_SERIF , Font.BOLD , 30) );
		gameTitle.setForeground(Color.BLUE);
		theTitle.add(gameTitle);
		
		JPanel aboutUs = new JPanel(new GridBagLayout());
		aboutUs.setBorder( new TitledBorder("About Developers") );
		teamName =  new JLabel("TG Dream Team");
		nol = new JLabel(String.format("%2$-20s : %1$s", "Nol Pasurapunya", "5510546018"));
		bat = new JLabel(String.format("%2$-20s : %1$s", "Poramate Homprakob", "5510546077"));
		ice = new JLabel(String.format("%2$-20s : %1$s", "Rungroj Maipradit", "5510546654"));
		kaset = new JLabel("Kasetsart University");
		faculty = new JLabel("Faculty of Engineering : Software and Knowledge Engineering");
		aboutUs.add(teamName, setPosition(0,0,1,2,10,5,10,3));
		aboutUs.add(nol, setPosition(0,1,1,2,5,5,5,10));
		aboutUs.add(bat, setPosition(0,2,1,2,5,5,5,10));
		aboutUs.add(ice, setPosition(0,3,1,2,5,5,5,10));
		aboutUs.add(kaset, setPosition(0,4,1,2,15,5,5,10));
		aboutUs.add(faculty, setPosition(0,5,1,2,5,5,5,10));
		
		version = new JLabel("@game_version 1.0.92");
		
		ActionListener lis = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
			
		};
		close = new JButton("Close");
		close.addActionListener(lis);
		
		frame.add(theTitle, setPosition(0,0,2,2,10,10,10,10));
		frame.add(aboutUs, setPosition(0,1,2,2,10,10,10,10));
		frame.add(version, setPosition(0,2,1,2,10,10,10,10));
		frame.add(close, setPosition(1,2,1,2,10,10,10,10));
	}
	
	/** Creates a new GridBagConstraints to set the position of components for the container that use GridBagLayout.
	 * 
	 * @param x X-Axis
	 * @param y Y-Axis
	 * @param width Width
	 * @param fill Fill the grid
	 * @param top Top insets
	 * @param left Left insets
	 * @param bottom Bottom insets
	 * @param right Right insets
	 * @return GridBagConstaints
	 */
	private GridBagConstraints setPosition( int x, int y , int width , int fill , int top , int left , int bottom , int right){
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.fill = fill;
		c.insets = new Insets(top,left,bottom,right);
		return c;
	}
	
	/** Shows this window and set its location related to GameUI. */
	public void run(){
		frame.pack();
		frame.setResizable(false);
		frame.setLocation( CenterDeterminer.determine(ui, frame));
		frame.setVisible(true);
	}
}
