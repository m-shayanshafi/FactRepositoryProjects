package ChessGameKenai;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * UserManual class extends JDialog and is used to display the User manual in a
 * JEditorPane. If the user clicks Help--User Manual a new JDialog pops up with
 * the html file loaded.
 * 
 * @author Mario Bruno
 */
public class UserManual extends JDialog {

	final private JEditorPane pane;
	final private JScrollPane scroll;
	final private ChessBoardView chessBoardView;

	/**
	 * Overloaded constructor which receives a reference to the ChessBoardView
	 * class only to set the location of the dialog in front of the main game.
	 * 
	 * @param view
	 *            ChessBoardView object
	 */
	public UserManual(final ChessBoardView view) {

		this.chessBoardView = view;
		pane = new JEditorPane();

		scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setEditable(false);
		pane.setOpaque(false);

		final URL url = UserManual.class.getResource("TxtFiles/userguide.htm");

		setHtmlFile(url);

		this.add(scroll, "Center");

		setJDialogueProperties(view);
	}

	private void setJDialogueProperties(final ChessBoardView view) {
		this.setSize(700, 650);
		this.setTitle("User Manual");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(view);
		this.setVisible(true);
	}

	private void setHtmlFile(final URL url) {
		try {
			pane.setPage(url);
		} catch (FileNotFoundException ex) {
			pane.setText("userguide.htm File Not Found");
		} catch (IOException ee) {
			JOptionPane.showMessageDialog(this, ee.getMessage());
		}
	}
}
