package ChessGameKenai;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * HelpTopics class extends JDialog and is the class which creates the Chess
 * Rules windows that you see when the user clicks Chess Rules in the Help menu.
 * It contains a JTextArea and it gets the text from the file ChessRules.txt and
 * appends it to the text area.
 * 
 * @author Val
 */
public class HelpTopics extends JDialog {

	private Container container;
	private JTextArea txtArea;
	private JScrollPane scrollPane;
	private ChessBoardView chessBoardView;

	/**
	 * Empty constructor that creates the graphically objects and has a
	 * reference to the ChessBoardView to set the location of the JDialog.
	 * 
	 * @param view
	 *            as a ChessBoardView object
	 */
	public HelpTopics(ChessBoardView view) {
		this.chessBoardView = view;
		container = this.getContentPane();

		setTextArea();

		createFile(view);

		scrollPane = new JScrollPane(txtArea);
		txtArea.setCaretPosition(0);
		container.add(scrollPane);

		setView(view);
	}

	private void createFile(ChessBoardView view) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					this.getClass().getResourceAsStream(
							"TxtFiles/chessrules.txt")));

			try {
				String line = null;
				
				while ((line = input.readLine()) != null) {

					txtArea.append(line + "\n");
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(view, "chessrules.txt Not Found",
					"File Not Found", JOptionPane.ERROR_MESSAGE);
			HelpTopics.this.dispose();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	private void setTextArea() {
		txtArea = new JTextArea() {

			/**
			 * The method painComponent of chatA is used here to paint the
			 * JTexchatA as we want
			 * 
			 * @param graphics
			 *            Graphics object used to paint this object
			 */
			@Override
			public void paintComponent(Graphics graphics) {
				int width = txtArea.getWidth();
				int height = txtArea.getHeight();

				URL url = txtArea.getClass()
						.getResource("Icons/background.jpg");
				Toolkit toolkit = this.getToolkit();
				Image image = toolkit.getImage(url);
				graphics.drawImage(image, 0, 0, width, height, txtArea);
				super.paintComponent(graphics);
			}
		};

		txtArea.setForeground(Color.WHITE);
		txtArea.setFont(new Font("Verdana", Font.PLAIN, 16));
		txtArea.setOpaque(false);
		txtArea.setLineWrap(true);
		txtArea.setEditable(false);
	}

	private void setView(ChessBoardView view) {
		this.setTitle(" CHESS RULES ");
		this.setSize(800, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(view);
		this.setVisible(true);
	}
}
