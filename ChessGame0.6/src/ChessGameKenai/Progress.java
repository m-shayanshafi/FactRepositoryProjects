package ChessGameKenai;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 * The Progress class is a JProgressBar object that is Runnable whenever the
 * Thread is started on the object it will execute its run method
 * 
 * @author Dimitri Pankov
 * @see JProgressBar
 * @see Runnable
 * @version 1.0
 */
public class Progress extends JProgressBar implements Runnable {

	public Progress() {
		this.setValue(0);
		this.setForeground(new Color(47, 79, 79));
		this.setStringPainted(true);
		this.setPreferredSize(new Dimension(600, 25));
	}

	/**
	 * The run method of the class is executed whenever the Thread on this
	 * object is started
	 */
	public void run() {
		int counter = 0;
		try {
			while (counter != 20) {
				Thread.sleep(200);
				this.setValue((int) (counter * (5.5)));
				counter++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}
}