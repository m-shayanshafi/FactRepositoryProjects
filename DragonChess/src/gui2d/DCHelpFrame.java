/*
 * Classname            : DCHelpFrame
 * Author               : Christophe Hertigers <christophe.hertigers@pandora.be>
 * Creation Date        : 2002/03/25
 * Last Updated         : Thursday, October 17 2002, 23:30:51
 * Description          : 2-Dimensional user interface for DragonChess.
 * GPL disclaimer       :
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 */

package gui2d;

/* package import */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;              //for layout managers
import java.net.URL;
import java.io.IOException;

/**
 * The Frame to display the rules of DragonChess
 *
 * @author   Christophe Hertigers
 * @version  Thursday, October 17 2002, 23:31:00
 */		  
public class DCHelpFrame extends JFrame {

	/* Instance Variables */
	private JEditorPane editorPane;
	private JScrollPane editorScrollPane;

	/**
	 * Inner Class. Handles all HyperlinkEvents.
	 *
	 */
	class HyperlinkAdapter implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			try {
				editorPane.setPage(e.getURL());
			} catch (IOException ioe) {
				System.err.println("Attempted to read a bad URL: " 
																+ e.getURL());
			}
		}
	}

	/**
	 * Class constructor. Creates the HelpFrame, a basic HTML viewer.
	 *
	 * @param width		the width of the helpframe.
	 * @param height	the height of the helpframe.
	 */
	public DCHelpFrame(int width, int height) {
		super("DragonChess Rules");
	
		this.getContentPane().setLayout(new GridLayout(1,1));
	
        //Create an editor pane
		editorPane = createEditorPane();
		editorPane.addHyperlinkListener(new HyperlinkAdapter());
		editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setMinimumSize(new	Dimension(10,10));

		this.getContentPane().add(editorScrollPane); 
		this.setSize(width, height);
		this.setVisible(true);
	}

	/**
	 * Creates the Editor Pane.
	 *
	 * @return		the editorPane
	 */
    private JEditorPane createEditorPane() {
       	JEditorPane editPane = new JEditorPane();
		editPane.setEditable(false);
		String s = null;
		try {
			s = "file:"
				+ System.getProperty("user.dir")
				+ System.getProperty("file.separator")
				+ "dcrules.html";
			URL helpURL = new URL(s);
			displayURL(helpURL,editPane);
		} catch (Exception e) {
			System.err.println("Couldn't create help URL: " + s);
		}
		return editPane;
	}
	
	/**
	 * Displays the selected URL on the JEditorPane.
	 *
	 * @param	the URL that is to be displayed
	 * @param	the JEditorPane on which it is to be displayed
	 */
	private void displayURL(URL url, JEditorPane editPane) {
		try {
			editPane.setPage(url);
		} catch (IOException e) {
			System.err.println("Attempted to read a bad URL: " + url);
		}
	}
}
