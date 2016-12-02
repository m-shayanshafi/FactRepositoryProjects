package thaigo.view;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import thaigo.utility.CenterDeterminer;
import thaigo.utility.ImageFrame;
/** ImageFrame that shows user that he/she's won.
 * 
 * @author Nol
 *
 */
public class WinGame extends ImageFrame {

	private GameUI ui;
	private static WinGame instance;
	
	/** Gets the instance of this object.
	 * 
	 * @param ui GameUI
	 * @return instance of this object
	 */
	public static WinGame getInstance(GameUI ui){
		if( instance == null)
			instance = new WinGame(ui);
		return instance;
	}
	
	/** Initializes the image frame.
	 * 
	 * @param ui GameUI
	 */
	private WinGame(GameUI ui){
		super("images/win.png",550,325);
		super.setPreferredSize( new Dimension(550,325) );
		this.ui = ui;
		super.getImageLabel().addMouseListener(new LabelListener() );
	}
	
	/** Shows the image and set its location related to GameUI. */
	public void run(){
		pack();
		setAlwaysOnTop(true);
		setLocation( CenterDeterminer.determine(ui, this) );
		setVisible(true);
	}

	/** Closes this image frame. */
	public void close(){
		dispose();
	}
	
	/** Listener for image frame. */
	class LabelListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {	
			System.exit(0);
			/*close();
			ui.newGame();*/
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {	}

		@Override
		public void mouseExited(MouseEvent arg0) {	}

		@Override
		public void mousePressed(MouseEvent arg0) {	}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
}
