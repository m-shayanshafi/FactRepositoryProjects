package thaigo.view;

import java.awt.Dimension;

import thaigo.utility.CenterDeterminer;
import thaigo.utility.ImageFrame;
/** Image to intrduce the game when run the program.
 * 
 * @author Nol
 *
 */
public class Intro extends ImageFrame{

	/** Initializes the <code>Intro</code>. */
	public Intro(){
		super("images/intro.png",350,350);
		super.setPreferredSize( new Dimension(350,350) );
		
	}
	
	/** Shows the image at the middle of the screen. */
	public void run(){
		pack();
		setLocation( CenterDeterminer.determineWithScreen(this) );
		setVisible(true);
	}
	
	/** Closes the image. */
	public void close(){
		dispose();
	}
}
