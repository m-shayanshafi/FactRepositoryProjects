package ChessGameKenai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 * The Choose_Icon class is a Simple JDialog that is used to display the
 * possible player icons to the user when the user clicks on the icon then
 * clicks next the icon he chose will represent him This JDialog is only used if
 * the user wants to choose another icon to represent himself
 * 
 * @author Dimitri Pankov
 * @see JDialog
 * @author 1.5
 */
public class ChooseIcon extends JDialog {

	private Container container;
	private JPanel southPanel;
	private JScrollPane scroll;
	private TreeMap<Integer, Thumbnail> treeMap = new TreeMap<Integer, Thumbnail>();
	private JRadioButton pl1Button, pl2Button;
	private ButtonGroup btnGroup;
	private JPanel panel;
	private JButton applyButton;
	private Chess_Data chessData;
	private ConnectionBridge bridge;
	private String playerIconPath;
	private static final int FONT_SIZE = 16;
	private static final Font FONT = new Font("Verdana", Font.BOLD, FONT_SIZE);

	/**
	 * Overloaded constructor of the class Has all needed GUI to represent
	 * itself graphically on the screen
	 * 
	 * @param data
	 *            as Chess_Data
	 * @param bridge
	 *            as a ConnectionBridge
	 */
	public ChooseIcon(Chess_Data data, ConnectionBridge bridge) {
		this.bridge = bridge;
		this.chessData = data;
		container = this.getContentPane();

		this.showPossiblePlayerIcons();

		configureButtons();

		this.setModal(true);

		configurePanels();

		addSmileysToDialog();

		scroll = new JScrollPane(southPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		addContainerComponents();

		setJFrameProperties();
	}

	private void configureButtons() {
		applyButton = new JButton(new ImageIcon(getClass().getResource(
				"Icons/next.gif")));
		applyButton.setBackground(new Color(139, 69, 19));
		applyButton.setPreferredSize(new Dimension(100, 30));
		applyButton.addActionListener(new ActionListener() {

			/**
			 * The actionPerfomed method of our class
			 * 
			 * @param actionEvent
			 *            ActionEvent object that is generated when button is
			 *            clicked
			 */
			public void actionPerformed(ActionEvent actionEvent) {
				if (ChooseIcon.this.chessData.isGameOnLine()) {
					ChooseIcon.this.sendPlayerIcon();
				}
				ChooseIcon.this.dispose();
			}
		});

		pl1Button = new JRadioButton("Player 1");
		pl1Button.setBackground(Color.GRAY);
		pl1Button.setFont(FONT);

		pl2Button = new JRadioButton("Player 2");
		pl2Button.setBackground(Color.GRAY);
		pl2Button.setFont(FONT);

		btnGroup = new ButtonGroup();
		btnGroup.add(pl1Button);
		btnGroup.add(pl2Button);
	}

	private void configurePanels() {
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.add(pl1Button);
		panel.add(pl2Button);
		panel.add(applyButton);

		southPanel = new JPanel();
		southPanel.setBackground(Color.GRAY);
	}

	private void setJFrameProperties() {
		this.setTitle("Choose Icon");
		this.setLocation(500, 300);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setSize(600, 200);
		this.setVisible(true);
	}

	private void addContainerComponents() {
		container.add(scroll, BorderLayout.CENTER);
		container.add(panel, BorderLayout.SOUTH);
	}

	private void addSmileysToDialog() {
		Thumbnail thumbNail = new Thumbnail("");
		for (int i = 1; i < 16; i++) {
			thumbNail.setImagePath("Icons/hercules" + i + ".gif");
			treeMap.put(Integer.valueOf(i), thumbNail);
			southPanel.add(treeMap.get(i));
			treeMap.get(i).addMouseListener(new MouseAdapter() {

				/**
				 * The method mousePressed is overwritten in our class
				 * 
				 * @param mouseEvent
				 *            MouseEvent object that is generated when mouse is
				 *            clicked
				 */
				@Override
				public void mousePressed(MouseEvent mouseEvent) {
					Thumbnail thumbnail = (Thumbnail) mouseEvent.getSource();
					Set set = treeMap.entrySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry mapEntry = (Map.Entry) iterator.next();
						if (((Thumbnail) mapEntry.getValue()).getBorder() != null) {
							((Thumbnail) mapEntry.getValue())
									.setBorder(new LineBorder(Color.GRAY, 5));
						}
					}

					if (pl1Button.isSelected()) {
						playerIconPath = thumbnail.getImagePath();
						ChooseIcon.this.chessData.getPlayers().get(0)
								.setImagePath(thumbnail.getImagePath());
					} else if (pl2Button.isSelected()) {
						playerIconPath = thumbnail.getImagePath();
						ChooseIcon.this.chessData.getPlayers().get(1)
								.setImagePath(thumbnail.getImagePath());
					}
					ChooseIcon.this.chessData
							.setPlayers(ChooseIcon.this.chessData.getPlayers());
					thumbnail.setBorder(new LineBorder(Color.GREEN, 5));
				}
			});
		}
	}

	/**
	 * The method sendPlayerIcon sends the path to the icon to the other player
	 * that he is playing against if the game is online if the game is online of
	 * not his method is skipped
	 */
	public void sendPlayerIcon() {
		try {
			Packet packet = new Packet();
			packet.setPlayerIconPath(playerIconPath);
			bridge.getOutputStream().writeObject(packet);
			bridge.getOutputStream().flush();
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(this, exp.toString());
		}
	}

	/**
	 * The method showPossiblePlayerIcons is used if the game is online so the
	 * player can only choose icon for him self that is done by disabling the
	 * radio button of the other player
	 */
	private void showPossiblePlayerIcons() {
		if (chessData.isGameOnLine()) {
			if (chessData.isServer()) {
				pl2Button.setEnabled(false);
				pl1Button.setSelected(true);
			} else {
				pl1Button.setEnabled(false);
				pl2Button.setSelected(true);
			}
		}
	}
}
