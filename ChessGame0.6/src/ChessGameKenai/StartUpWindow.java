package ChessGameKenai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * The StartUpWindow class is a JWindow object that is used to start up the
 * chess game The Window is Runnable object that only appears at the beginning
 * of the game
 * 
 * @author Dimitri Pankov
 * @see JWindow
 * @see Runnable
 * @version 1.0
 */
public class StartUpWindow extends JWindow implements Runnable {

	final private Container container;
	private JPanel mainPanel;
	final private Progress progress;
	final private Dimension screenDimension = Toolkit.getDefaultToolkit()
			.getScreenSize();
	final private int x = screenDimension.width / 2;
	final private int y = screenDimension.height / 2;

	public StartUpWindow() {
		container = this.getContentPane();

		progress = new Progress();
		createMainPanel();

		new Thread(this).start();

		container.add(mainPanel);
		container.add(progress, BorderLayout.SOUTH);

		setJframeProperties();
	}

	private void setJframeProperties() {
		this.setSize(600, 455);
		this.setLocation((int) (x - this.getWidth() / 1.5),
				(int) (y - this.getHeight() / 1.5));
		this.setVisible(true);
	}

	private void createMainPanel() {
		mainPanel = new JPanel() {

			/**
			 * The method painComponent of blackCapturedPiecesPanel is used here
			 * to paint the JPanel as we want
			 * 
			 * @param graphics
			 *            Graphics object used to paint this object
			 */
			@Override
			public void paintComponent(final Graphics graphics) {
				super.paintComponent(graphics);
				final int mainPanelWidth = mainPanel.getWidth();
				final int mainPanelHeight = mainPanel.getHeight();

				final URL url = mainPanel.getClass().getResource(
						"Icons/board.jpg");
				final Toolkit toolkit = this.getToolkit();
				final Image image = toolkit.getImage(url);
				graphics.drawImage(image, 0, 0, mainPanelWidth,
						mainPanelHeight, mainPanel);
				graphics.setColor(Color.WHITE);
				graphics.setFont(new Font("Verdana", Font.PLAIN, 20));
				graphics.drawString(
						"All Rights Reserved @Dimitri_Mario_Valeria", 88, 50);
			}
		};
	}

	/**
	 * The run method of the Runnable object It is executed when the Thread on
	 * this object is started
	 */
	public void run() {
		int counter = 0;

		new Thread(progress).start();

		try {
			while (counter != 10) {
				Thread.sleep(400);
				counter++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);

		} finally {

			this.dispose();

			new Thread(new Runnable() {

				public void run() {
					new Start_Game();
				}
			}).start();
		}
	}
}
