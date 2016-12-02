/*
 * Classname        : DCPlayerNamePopup
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Sunday, December 08 2002, 12:28:06
 * Last Updated     : Saturday, October 19 2002, 13:13:09
 * Description      : 2-Dimensional user interface for DragonChess.
 * GPL disclaimer   :
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gui2d;
    
/* package import */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.*;

/**
 * An option popup on an JInternalFrame to specify the player's name and to
 * let him choose which player he wants to be.
 * 
 * @author	Christophe Hertigers <xof@pandora.be>
 * @version	Sunday, December 08 2002, 12:28:06
 */

class DCPlayerNamePopup extends JInternalFrame {

	private int prefPlayer1Int = -1;
	private int prefPlayer2Int = -1;
	private int connType = -1;

	private DC2dGUI parent;

	private DCOptions userOptions;

	private JPanel cPanel = new JPanel(),
		sPanel = new JPanel(),
		player1Panel = new JPanel(),
		player2Panel = new JPanel(),
		p1LabelPanel = new JPanel(),
		p1ContentPanel = new JPanel(),
		p2LabelPanel = new JPanel(),
		p2ContentPanel = new JPanel();

	private JLabel name1Label = new JLabel("Player name:"),
		name2Label = new JLabel("Player 2 name:"),
		player1Label = new JLabel("Prefered player:"),
		player2Label = new JLabel("Prefered player 2:");

	private JTextField name1TextField = new JTextField(5),
		name2TextField = new JTextField(5);

	private ButtonGroup player1ButtonGroup = new ButtonGroup(),
		player2ButtonGroup = new ButtonGroup();

	private JRadioButton gold1RB = new JRadioButton("Gold"),
		scarlet1RB = new JRadioButton("Scarlet"),
		gold2RB = new JRadioButton("Gold"),
		scarlet2RB = new JRadioButton("Scarlet");
	private JButton okButton = new JButton("OK");

	/**
	 * Class Constructor.
	 * @param parent
	 */
    public DCPlayerNamePopup(DC2dGUI parent) {
        super("Player Name");

		this.parent = parent;

		
		// get current user preferences
		userOptions = parent.getPreferences();
		
        setSize(400,210);
        setLocation((parent.getWidth()-400)/2, (parent.getHeight()-200)/2);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        gold1RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        gold1RB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer1Int != DCConstants.PLAYER_GOLD) {
                        prefPlayer1Int = DCConstants.PLAYER_GOLD;
                        scarlet2RB.setSelected(true);
                        prefPlayer2Int = DCConstants.PLAYER_SCARLET;
                    }
                }
            });

        scarlet1RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        scarlet1RB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer1Int != DCConstants.PLAYER_SCARLET) {
                        prefPlayer1Int = DCConstants.PLAYER_SCARLET;
                        gold2RB.setSelected(true);
                        prefPlayer2Int = DCConstants.PLAYER_GOLD;
                    }
                }
            });

		player1ButtonGroup.add(gold1RB);
        player1ButtonGroup.add(scarlet1RB);

        p1LabelPanel.setLayout(new BoxLayout(p1LabelPanel,
                                                    BoxLayout.Y_AXIS));
        p1LabelPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        p1LabelPanel.add(name1Label);
        p1LabelPanel.add(Box.createRigidArea(new Dimension(0,8)));
        p1LabelPanel.add(player1Label);

        name1TextField.setMaximumSize(new Dimension(500,20));
        p1ContentPanel.setLayout(new BoxLayout(p1ContentPanel,
                                                    BoxLayout.Y_AXIS));
        p1ContentPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        p1ContentPanel.add(name1TextField);
        p1ContentPanel.add(gold1RB);
        p1ContentPanel.add(scarlet1RB);

        player1Panel.setLayout(new BoxLayout(player1Panel,
                                                    BoxLayout.X_AXIS));
        player1Panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        player1Panel.add(p1LabelPanel);
        player1Panel.add(p1ContentPanel);

        gold2RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        gold2RB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer2Int != DCConstants.PLAYER_GOLD) {
                        prefPlayer2Int = DCConstants.PLAYER_GOLD;
                        scarlet1RB.setSelected(true);
                        prefPlayer1Int = DCConstants.PLAYER_SCARLET;
                    }
                }
            });

        scarlet2RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        scarlet2RB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer2Int != DCConstants.PLAYER_SCARLET) {
                        prefPlayer2Int = DCConstants.PLAYER_SCARLET;
                        gold1RB.setSelected(true);
                        prefPlayer1Int = DCConstants.PLAYER_GOLD;
                    }
                }
            });

        player2ButtonGroup.add(gold2RB);
        player2ButtonGroup.add(scarlet2RB);

        p2LabelPanel.setLayout(new BoxLayout(p2LabelPanel,
                                                    BoxLayout.Y_AXIS));
        p2LabelPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        p2LabelPanel.add(name2Label);
        p2LabelPanel.add(Box.createRigidArea(new Dimension(0,8)));
        p2LabelPanel.add(player2Label);

        name2TextField.setMaximumSize(new Dimension(500,20));
        p2ContentPanel.setLayout(new BoxLayout(p2ContentPanel,
                                                    BoxLayout.Y_AXIS));
        p2ContentPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        p2ContentPanel.add(name2TextField);
        p2ContentPanel.add(gold2RB);
        p2ContentPanel.add(scarlet2RB);

        player2Panel.setLayout(new BoxLayout(player2Panel,
                                                    BoxLayout.X_AXIS));
        player2Panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        player2Panel.add(p2LabelPanel);
        player2Panel.add(p2ContentPanel);

        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.add(player1Panel);
        cPanel.add(player2Panel);

        okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (player1ButtonGroup.getSelection() != null
                        && !name1TextField.getText().equals("")) {
                        switch(connType) {
                        case DCConstants.CONN_LOCAL:
                            if (!name2TextField.getText().equals("")) {
                                setVisible(false);
                                getGui().registerPlayer(
												name1TextField.getText(),
 			                                    prefPlayer1Int);
                                getGui().registerPlayer(
												name2TextField.getText(),
                                        		prefPlayer2Int);
                            }
                            break;
                        default:
                            setVisible(false);
                            getGui().registerPlayer(name1TextField.getText(),
                                        			prefPlayer1Int);
                            break;
                        }
                    }
                }
            });

        sPanel.add(okButton);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(cPanel, BorderLayout.CENTER);
        this.getContentPane().add(sPanel, BorderLayout.SOUTH);

        this.getRootPane().setDefaultButton(okButton);
    }

	/**
	 * Sets the connection type
	 * @param	connection type
	 */
    public void setConnectionType(int connType) {
        this.connType = connType;
        switch (connType) {
        case DCConstants.CONN_LOCAL:
            name1Label.setText("Player 1 name:");
            player1Label.setText("Prefered player 1:");
            player2Panel.setVisible(true);
            break;
        default:
        	name1Label.setText("Player name:");
        	player1Label.setText("Prefered player:");
            player2Panel.setVisible(false);
            break;
        }
    }

	/**
	 * Set the default values
	 */
	private void setDefaultValues() {
		
		String name = userOptions.getDefaultUsername();
		int	color = userOptions.getPreferredColor();

		name1TextField.setText(name);
		switch (color) {
		case DCConstants.PLAYER_GOLD:
			gold1RB.setSelected(true);
	        prefPlayer1Int = DCConstants.PLAYER_GOLD;
            scarlet2RB.setSelected(true);
            prefPlayer2Int = DCConstants.PLAYER_SCARLET;	
			break;
		case DCConstants.PLAYER_SCARLET:
			gold2RB.setSelected(true);
	        prefPlayer2Int = DCConstants.PLAYER_GOLD;
            scarlet1RB.setSelected(true);
            prefPlayer1Int = DCConstants.PLAYER_SCARLET;	
			break;	
		}
	}

	/**
	 * Show the popup
	 */
	public void showPopup() {
		setDefaultValues();
		setVisible(true);
	}

	/**
	 * Gets a reference to the parent
	 * @return reference to the parent
	 */
	public DC2dGUI getGui() {
		return parent;
	}
}

