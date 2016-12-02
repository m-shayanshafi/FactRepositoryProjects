package ChessGameKenai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * About class extends JDialog and is used to simply display the about window.
 * The use will see this window when they click Help-About in the game. The
 * about dialog contains simple scrolling animation made in a thread. It draws
 * String and 2 images on the center panel. It also has a close button.
 * 
 * @author Mario
 */
public class About extends JDialog {

	private JButton btnClose = new JButton("Close");
	private CostumPanel pnlClose, pnlText, pnlTop, pnlAll;
	private JLabel lblTitle;
	private Container c;
	private int yPos = 200;
	private volatile boolean isKill = true;

	/**
	 * Overloaded constructor which receives a reference to the ChessBoardView
	 * class only to set the location of the dialog in front of the main game.
	 * 
	 * @param view
	 *            ChessBoardView object
	 */
	public About(ChessBoardView view) {

		c = this.getContentPane();

		startAnimationThread();

		createCostumPanels();

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				isKill = false;
			}
		});

		setLabelTitle();

		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				About.this.dispose();
				isKill = false;
			}
		});

		addPanelComponents();

		c.add(pnlAll);

		setJDialogProperties(view);
	}

	private void setJDialogProperties(ChessBoardView view) {
		this.setSize(350, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(view);
		this.setTitle("About");
		this.setVisible(true);
	}

	private void addPanelComponents() {
		pnlTop.add(lblTitle);
		pnlClose.add(btnClose);
		pnlAll.add(pnlTop, BorderLayout.NORTH);
		pnlAll.add(pnlText, BorderLayout.CENTER);
		pnlAll.add(pnlClose, BorderLayout.SOUTH);
	}

	private void setLabelTitle() {
		lblTitle = new JLabel("Chess'N'Chat");
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 23));
		lblTitle.setForeground(Color.WHITE);
	}

	public JLabel getLabelTitle() {
		return lblTitle;
	}

	private void createCostumPanels() {
		pnlClose = new CostumPanel();
		pnlClose.setOpaque(false);
		pnlTop = new CostumPanel();
		pnlTop.setOpaque(false);
		pnlAll = new CostumPanel("Icons/background.jpg", new BorderLayout());
	}

	private void startAnimationThread() { 
											
		Animation animate = new Animation();
		animate.makePanel();
		animate.start();
	}

	/**
	 * Animation class is a thread used for the vertical scrolling animation. It
	 * works by repainting the panel every 44 milliseconds
	 */
	class Animation extends Thread {

		public static final int FONT_SIZE = 18;
		public static final int AUTHOR_NAME_XPOSITION = 135;
		public static final int VERSION_XPOSITION = 118;
		public static final int CREATEDBY_XPOSITION = 120;
		public static final int IMAGE_XPOSITION = 30;
		public static final int IMAGE2_XPOSITION = 230;
		public static final int OFFSET_YPOSITION = 35;
		public static final int MIN_YPOSITION = 15;

		@Override
		public void run() {
			while (isKill) {
				repaint();
				try {
					Thread.sleep(44);
				} catch (InterruptedException ex) {
					System.err.println(ex.getMessage());
					isKill = false;
				}
			}
		}

		/**
		 * makePanel is the method that creates the center panel. 
		 */
		public void makePanel() {
			final Image image = new ImageIcon(getClass().getResource(
					"Icons/logo.gif")).getImage();
			final Image image2 = new ImageIcon(getClass().getResource(
					"Icons/logo.gif")).getImage();

			pnlText = new CostumPanel() {
				public void paintComponent(Graphics graphics) {

					graphics.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
					graphics.setColor(Color.ORANGE);
					graphics.drawString("Created By:", CREATEDBY_XPOSITION,
							yPos);
					graphics.drawImage(image, IMAGE_XPOSITION, yPos
							+ OFFSET_YPOSITION, this);
					graphics.drawImage(image2, IMAGE2_XPOSITION, yPos
							+ OFFSET_YPOSITION, this);
					graphics.drawString("Dimitri", AUTHOR_NAME_XPOSITION, yPos
							+ OFFSET_YPOSITION);
					graphics.drawString("Mario", AUTHOR_NAME_XPOSITION, yPos
							+ OFFSET_YPOSITION + 30);
					graphics.drawString("Val", AUTHOR_NAME_XPOSITION, yPos
							+ OFFSET_YPOSITION + 60);
					graphics.drawString("Version 2.0", VERSION_XPOSITION, yPos
							+ OFFSET_YPOSITION + 100);
					yPos--;

					if (yPos < MIN_YPOSITION) {
						yPos = MIN_YPOSITION;
						isKill = false;
					}
				}
			};
			pnlText.setOpaque(false);
		}
	}
}
