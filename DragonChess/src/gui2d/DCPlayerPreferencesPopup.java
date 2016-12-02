/**
 * Classname        : DCPlayerPreferencesPopup.
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Sunday, December 08 2002, 15:57:28
 * Last Updated     : Sunday, December 08 2002, 15:57:28
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
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import main.*;

/**
 * Option popup to choose between connection types (local, network server,
 * network client, spectator)
 *
 * @author      Christophe Hertigers
 * @version     Sunday, December 08 2002, 15:57:28
 */
public class DCPlayerPreferencesPopup extends JInternalFrame {

	private int prefColorInt = -1;

	private DC2dGUI parent;

	private DCOptions userOptions;

	private GridBagLayout userGridbag = new GridBagLayout(),
		graphicsGridbag = new GridBagLayout(),
		debugGridbag = new GridBagLayout();

	private GridBagConstraints c = new GridBagConstraints();

	private JPanel contentPanel = new JPanel(),
		userPanel = new JPanel(),
		graphicsPanel = new JPanel(),
		debugPanel = new JPanel(),
		buttonPanel = new JPanel();

	private Font titleFont = new Font("SansSerif", Font.BOLD, 11);

	private Color titleColor = Color.DARK_GRAY;

	private Border etched = BorderFactory.createEtchedBorder(),
		popupBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5),
		userBorder =	BorderFactory.createTitledBorder(
				etched,
				"USER OPTIONS",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				titleFont,
				titleColor),
		graphicsBorder = BorderFactory.createTitledBorder(
				etched,
				"GRAPHICS OPTIONS",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				titleFont,
				titleColor),
		debugBorder = BorderFactory.createTitledBorder(
				etched,
				"DEBUG OUTPUT OPTIONS",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				titleFont,
				titleColor);

	private JLabel serverLabel = new JLabel("Default Server :"),
		serverDescLabel = new JLabel("Default Server " + "Description :"),
		portLabel = new JLabel("Default Port :"),
		userLabel = new JLabel("Default Username :"),
		colorLabel = new JLabel("Preferred Color :"),
		antialiasedLabel = new JLabel("Antialiase Pieces ?"),
		placementLabel = new JLabel("Placement :"),
		xLabel = new JLabel("x :", JLabel.RIGHT),
		yLabel = new JLabel("y :", JLabel.RIGHT),
		widthLabel = new JLabel("width :", JLabel.RIGHT),
		heightLabel = new JLabel("height :", JLabel.RIGHT),
		debugDisplayLabel = new JLabel("Display debug output for :"),
		debugGuiLabel = new JLabel("DC2dGUI :"),
		debugBBLabel = new JLabel("DCButtonBoard :"),
		debugFEDecLabel = new JLabel("DCFrontEndDecoder :");

	private ButtonGroup colorGroup = new ButtonGroup();

	private JRadioButton goldRB = new JRadioButton("Gold"),
		scarletRB = new JRadioButton("Scarlet");

	private JCheckBox antialiasedCB = new JCheckBox(),
		debugGuiCB = new JCheckBox(),
		debugBBCB = new JCheckBox(),
		debugFEDecCB = new JCheckBox();

	private JTextField serverTextField = new JTextField(20),
		serverDescTextField = new JTextField(20),
		portTextField = new JTextField(20),
		userTextField = new JTextField(20),
		xTextField = new JTextField(5),
		yTextField = new JTextField(5),
		widthTextField = new JTextField(5),
		heightTextField = new JTextField(5);

	private JButton okButton = new JButton("OK"),
		cancelButton = new JButton("Cancel");

	/**
	 * Class Constructor
	 * @param parent		reference to the gui
	 */
    public DCPlayerPreferencesPopup(DC2dGUI parent) {
        super("Player Preferences");

        this.parent = parent;

		// get current user preferences
		userOptions = parent.getPreferences();

		// parse userOptions
		setDefaultValues();
		
		// set display parameters
        goldRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        goldRB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    prefColorInt = DCConstants.PLAYER_GOLD;
                }
            });

        scarletRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        scarletRB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    prefColorInt = DCConstants.PLAYER_SCARLET;
                }
            });
        
		colorGroup.add(goldRB);
        colorGroup.add(scarletRB);

		antialiasedCB.setHorizontalTextPosition(SwingConstants.LEADING);
		
        okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	/* set user options */
					userOptions.setDefaultServer(serverTextField.getText());
					userOptions.setDefaultServerDescription(
												serverDescTextField.getText());
					int portNum;
					try {
						portNum = Integer.parseInt(portTextField.getText());
					} catch (NumberFormatException ex) {
						portNum = 22666;
					}
					userOptions.setDefaultPort(portNum);
					userOptions.setDefaultUsername(userTextField.getText());
					userOptions.setPreferredColor(prefColorInt);
					/* set graphics options */
					userOptions.setAntialiased(antialiasedCB.isSelected());
					if (xTextField.getText() != null) {
						userOptions.setDefaultFrameX(Integer.parseInt(
													xTextField.getText()));
					}
					if (yTextField.getText() != null) {
						userOptions.setDefaultFrameY(Integer.parseInt(
													yTextField.getText()));
					}
					if (widthTextField.getText() != null) {
						userOptions.setDefaultFrameWidth(Integer.parseInt(
													widthTextField.getText()));
					}
					if (heightTextField.getText() != null) {
						userOptions.setDefaultFrameHeight(Integer.parseInt(
													heightTextField.getText()));
					}
					/* set debug options */
                	userOptions.setDebug2dGUI(debugGuiCB.isSelected());
                	userOptions.setDebugButtonBoard(debugBBCB.isSelected());
                	userOptions.setDebugFrontEndDecoder(debugFEDecCB.isSelected());
                	
                	/* update anti-aliased pref + debug prefs */
                	getGui().executeUserOptions();
                	                	
                	/* hide popup */
					setVisible(false);
				}
            });

        cancelButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
            });

		// setup userPanel
		setupUserPanel();
		
		// setup graphicsPanel
		setupGraphicsPanel();
		
		// setup debugPanel
		setupDebugPanel();	
		
		// setup buttonPanel
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// setup contentPanel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(userPanel);
        contentPanel.add(graphicsPanel);
        contentPanel.add(debugPanel);
		contentPanel.add(buttonPanel);
		contentPanel.setBorder(popupBorder);
		
		this.setContentPane(contentPanel);
        this.getRootPane().setDefaultButton(okButton);

		pack();
        setLocation((parent.getWidth()-getWidth())/2,
                    (parent.getHeight()-getHeight())/2);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Function to set up the User Preferences Panel. This is a part of the
	 * constructor of DCPlayerPreferencesPopup and should not be called outside
	 * of the constructor.
	 */
	private void setupUserPanel() {

		/*
		 * setup userPanel
		 */
		userPanel.setLayout(userGridbag);
		userPanel.setBorder(userBorder);
		c.fill = GridBagConstraints.HORIZONTAL;

		// serverLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0,5,0,0);
		userGridbag.setConstraints(serverLabel, c);
		userPanel.add(serverLabel);

		// serverTextField
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0,5,0,5);
		userGridbag.setConstraints(serverTextField, c);
		userPanel.add(serverTextField);

		// serverDescLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		userGridbag.setConstraints(serverDescLabel, c);
		userPanel.add(serverDescLabel);

		// serverDescTextField
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(5,5,0,5);
		userGridbag.setConstraints(serverDescTextField, c);
		userPanel.add(serverDescTextField);

		// portLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		userGridbag.setConstraints(portLabel, c);
		userPanel.add(portLabel);

		// portTextField
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(5,5,0,5);
		userGridbag.setConstraints(portTextField, c);
		userPanel.add(portTextField);

		// userLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		userGridbag.setConstraints(userLabel, c);
		userPanel.add(userLabel);

		// userTextField
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(5,5,0,5);
		userGridbag.setConstraints(userTextField, c);
		userPanel.add(userTextField);

		// colorLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		userGridbag.setConstraints(colorLabel, c);
		userPanel.add(colorLabel);

		// goldRB
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5,0,0,0);
		userGridbag.setConstraints(goldRB, c);
		userPanel.add(goldRB);

		// scarletRB
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 4;
		userGridbag.setConstraints(scarletRB, c);
		userPanel.add(scarletRB);
		
	}
	
	/**
	 * Function to set up the Graphics Preferences Panel. This is a part of the
	 * constructor of DCPlayerPreferencesPopup and should not be called outside
	 * of the constructor. 	 */
	private void setupGraphicsPanel() {
		/*
		 * setup graphicsPanel
		 */
		graphicsPanel.setLayout(graphicsGridbag);
		graphicsPanel.setBorder(graphicsBorder);
		c.fill = GridBagConstraints.HORIZONTAL;

		// antialiasedLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		graphicsGridbag.setConstraints(antialiasedLabel, c);
		graphicsPanel.add(antialiasedLabel);

		// antialiasedCB
		c.weightx = 0.0;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(0,0,0,0);
		graphicsGridbag.setConstraints(antialiasedCB, c);
		graphicsPanel.add(antialiasedCB);

		// placementLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5,5,0,0);
		graphicsGridbag.setConstraints(placementLabel, c);
		graphicsPanel.add(placementLabel);

		// xLabel
		c.weightx = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		graphicsGridbag.setConstraints(xLabel, c);
		graphicsPanel.add(xLabel);

		// xTextField
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		graphicsGridbag.setConstraints(xTextField, c);
		graphicsPanel.add(xTextField);

		// yLabel
		c.weightx = 0.0;
		c.gridx = 3;
		c.gridy = 1;
		graphicsGridbag.setConstraints(yLabel, c);
		graphicsPanel.add(yLabel);

		// yTextField
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		c.insets = new Insets(5,5,0,5);
		graphicsGridbag.setConstraints(yTextField, c);
		graphicsPanel.add(yTextField);

		// widthLabel
		c.weightx = 0.0;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5,5,0,0);
		graphicsGridbag.setConstraints(widthLabel, c);
		graphicsPanel.add(widthLabel);

		// widthTextField
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 2;
		graphicsGridbag.setConstraints(widthTextField, c);
		graphicsPanel.add(widthTextField);

		// heightLabel
		c.weightx = 0.0;
		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(5,5,5,0);
		graphicsGridbag.setConstraints(heightLabel, c);
		graphicsPanel.add(heightLabel);

		// heightTextField
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		c.insets = new Insets(5,5,5,5);
		graphicsGridbag.setConstraints(heightTextField, c);
		graphicsPanel.add(heightTextField);
	}

	/**
	 * Function to set up the Debug Preferences Panel. This is a part of the
	 * constructor of DCPlayerPreferencesPopup and should not be called outside
	 * of the constructor.
	 */
	private void setupDebugPanel() {
		/*
		 * setup graphicsPanel
		 */
		debugPanel.setLayout(debugGridbag);
		debugPanel.setBorder(debugBorder);
		c.fill = GridBagConstraints.BOTH;

		// debugDisplayLabel
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		debugGridbag.setConstraints(debugDisplayLabel, c);
		debugPanel.add(debugDisplayLabel);

		// debugGuiLabel
		c.gridy = 1;
		c.insets = new Insets(5,5,0,0);
		debugGridbag.setConstraints(debugGuiLabel, c);
		debugPanel.add(debugGuiLabel);

		// debugGuiCB
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		debugGridbag.setConstraints(debugGuiCB, c);
		debugPanel.add(debugGuiCB);
		
		// debugBBLabel
		c.weightx = 0.0;		
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5,5,0,0);
		debugGridbag.setConstraints(debugBBLabel, c);
		debugPanel.add(debugBBLabel);

		// debugBBCB
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 2;
		debugGridbag.setConstraints(debugBBCB, c);
		debugPanel.add(debugBBCB);

		// debugFEDecLabel
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5,5,5,0);
		debugGridbag.setConstraints(debugFEDecLabel, c);
		debugPanel.add(debugFEDecLabel);

		// debugFEDecCB
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		debugGridbag.setConstraints(debugFEDecCB, c);
		debugPanel.add(debugFEDecCB);
	}

    /**
     * Gets a reference to the parent
     * @return  reference to the parent
     */
    private DC2dGUI getGui() {
        return  parent;
    }

	/**
	 * Set the default values
	 */
	private void setDefaultValues() {

		String server = userOptions.getDefaultServer();
		String servDesc = userOptions.getDefaultServerDescription();
		int portNum = userOptions.getDefaultPort();
		String user = userOptions.getDefaultUsername();
		int x = userOptions.getDefaultFrameX();
		int y = userOptions.getDefaultFrameY();
		int w = userOptions.getDefaultFrameWidth();
		int h = userOptions.getDefaultFrameHeight();
		prefColorInt = userOptions.getPreferredColor();

		if (server != null) {
			serverTextField.setText(server);
		}

		if (servDesc != null) {
			serverDescTextField.setText(servDesc);
		}

		if (portNum != -1) {
			portTextField.setText(String.valueOf(portNum));
		} else {
			portTextField.setText("22666");
		}

		if (user != null) {
			userTextField.setText(user);
		}

		if (x != -1) {
			xTextField.setText(String.valueOf(x));
		} else {
			xTextField.setText("0");
		}
		
		if (y != -1) {
			yTextField.setText(String.valueOf(y));
		} else {
			yTextField.setText("0");
		}
		
		if (w != -1) {
			widthTextField.setText(String.valueOf(w));
		} else {
			widthTextField.setText(String.valueOf(getGui().getWidth()));
		}
		
		if (h != -1) {
			heightTextField.setText(String.valueOf(h));
		} else {
			heightTextField.setText(String.valueOf(getGui().getHeight()));
		}
		
		switch(prefColorInt) {
		case DCConstants.PLAYER_GOLD:
			goldRB.setSelected(true);
			break;
		case DCConstants.PLAYER_SCARLET:
			scarletRB.setSelected(true);
			break;
		}

		antialiasedCB.setSelected(userOptions.getAntialiased());
		debugGuiCB.setSelected(userOptions.getDebug2dGUI());
		debugBBCB.setSelected(userOptions.getDebugButtonBoard());
		debugFEDecCB.setSelected(userOptions.getDebugFrontEndDecoder());
	}

	/**
	 * Show the popup
	 */
	public void showPopup() {
		setDefaultValues();
		setVisible(true);
	}
}

