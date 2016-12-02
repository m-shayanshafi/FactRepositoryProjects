package thaigo.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** Text Panel that can display texts with different color.
 * 
 * @author Nol
 */
public class ColorTextPane extends JTextPane {

	StyledDocument doc;
	Style style;
	
	/** Initializes the text panel. */
	public ColorTextPane() {
		this.doc = super.getStyledDocument();
		style = this.addStyle("I'm a Style", null);
	}
	
	/** Adds text to panel.
	 * 
	 * @param text New text
	 * @param color Color of the text
	 */
	public void addText(String text, Color color){
		StyleConstants.setForeground(style, color);
		try { doc.insertString(doc.getLength(), text + "\n" ,style); }
        catch (BadLocationException e){}
	}
	
	/** Clears the text panel. */
	public void clear(){
		this.setText("");
	}

}
