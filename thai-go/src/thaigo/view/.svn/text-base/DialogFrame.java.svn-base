package thaigo.view;


import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JFrame;

/** Frame without Minimize / Maximize Button.
 * 
 * @author Nol
 *
 */
public class DialogFrame extends JDialog {

	MyFrame frame;

	/** Initializes the frame. */
	public DialogFrame( String title ) {
		super(new MyFrame(title),title);
	}

	/** @see JDialog.setVisible( boolean b ) */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			((MyFrame)getParent()).dispose();
		}
	}

	/** @see Window.setIconImage( Image image ) */
	@Override
	public void setIconImage(Image img){
		super.setIconImage(img);
		((JFrame)super.getParent()).setIconImage(img);
	}
}

/** JFrame to use with DialogFrame. */
class MyFrame extends JFrame {

	/** Initializes the frame.
	 * 
	 * @param title Title of the frame
	 */
	MyFrame(String title){
		super(title);
		setUndecorated(true);
		setVisible(true);
	}
}