/**
 * 
 */
package kw.texasholdem;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import kw.texasholdem.view.TexasHoldemMainPanel;

/**
 * @author ken
 *
 */
public class RunMain {
    
    /**
     * The application's entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
    	try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        //javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
            	new TexasHoldemMainPanel(0);
        //    }
        //});
        
    }
}
