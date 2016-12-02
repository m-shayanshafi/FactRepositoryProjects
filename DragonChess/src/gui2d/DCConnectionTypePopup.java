/**
 * Classname        : DCConnectionTypePopup.
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Sunday, December 08 2002, 13:13:31
 * Last Updated     : Sunday, December 08 2002, 13:13:31
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
//import java.beans.*;
//import java.io.*;
//import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
//import connectivity.*;
import main.*;

/**
 * Option popup to choose between connection types (local, network server,
 * network client, spectator)
 *
 * @author      Christophe Hertigers
 * @version     Sunday, December 08 2002, 13:13:31
 */
public class DCConnectionTypePopup extends JInternalFrame {

    private int             connInt = DCConstants.CONN_LOCAL;

	private	DC2dGUI			parent;

	private DCOptions		userOptions;

    private JPanel          cPanel      = new JPanel(),
                            sPanel      = new JPanel(),
                            clientPanel = new JPanel(),
                            serverPanel = new JPanel();

    private Border          popupBorder = BorderFactory.createEmptyBorder(
                                                                    5,5,5,5);
    private JLabel          msgLabel    = new JLabel(
                                            "Choose your connection type:",
                                            JLabel.LEFT),
                            clientServerLabel = new JLabel("Server:"),
                            clientPortLabel = new JLabel("Port:"),
                            serverDescrLabel = new JLabel("Description:"),
                            serverPortLabel = new JLabel("Port:");


    private ButtonGroup     connGroup   = new ButtonGroup();

    private JRadioButton    localRB     = new JRadioButton("Local Game",
                                                                        true),
                            serverRB    = new JRadioButton(
                                                    "Network Game - Server"),
                            clientRB    = new JRadioButton(
                                                    "Network Game - Client");

    private JTextField      clientServerTextField   = new JTextField(15),
                            clientPortTextField     = new JTextField(4),
                            serverDescrTextField    = new JTextField(15),
                            serverPortTextField     = new JTextField(4);

    private JButton         okButton    = new JButton("OK");

    public DCConnectionTypePopup(DC2dGUI parent) {
        super("Connection Type");

		this.parent = parent;

		userOptions = parent.getPreferences();

        setSize(400,200);
        setLocation((parent.getWidth()-400)/2,
					(parent.getHeight()-200)/2);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        localRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        localRB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    connInt = DCConstants.CONN_LOCAL;
                }
            });

        serverRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        serverRB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (serverRB.isSelected()) {
                            connInt = DCConstants.CONN_SERVER;
                            serverPanel.setVisible(true);
                    } else {
                            serverPanel.setVisible(false);
                    }
                }
            });

        clientRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
        clientRB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (clientRB.isSelected()) {
                            connInt = DCConstants.CONN_CLIENT;
                            clientPanel.setVisible(true);
                    } else {
                            clientPanel.setVisible(false);
                    }
                }
            });

        connGroup.add(localRB);
        connGroup.add(serverRB);
        connGroup.add(clientRB);

        okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   if (connGroup.getSelection() != null) {
                       switch (connInt) {
                       case DCConstants.CONN_LOCAL:
                            setVisible(false);
                            getGui().setupConnection(connInt, 
														"***local***", -1);
                            break;
                        case DCConstants.CONN_SERVER:
                            if (!serverPortTextField.getText().equals("")) {
                                int port = -1;
                                try {
                                    port = Integer.parseInt(
                                            serverPortTextField.getText());
                                } catch (java.lang.NumberFormatException f){
                                    port = -1;
                                }
                                if (port > 0) {
                                    setVisible(false);
                                    getGui().setupConnection(connInt,
                                            serverDescrTextField.getText(),
                                            port);
                                }
                            }
                            break;
                        case DCConstants.CONN_CLIENT:
                            if (!clientServerTextField.getText().equals("")
                             && !clientPortTextField.getText().equals("")) {
                                int port = -1;
                                try {
                                    port = Integer.parseInt(
                                            clientPortTextField.getText());
                                } catch (java.lang.NumberFormatException f){
                                    port = -1;
                                }
                                if (port > 0) {
                                    setVisible(false);
                                    getGui().setupConnection(connInt,
                                            clientServerTextField.getText(),
                                            port);
                                }
                            }
                            break;
                        }
                    }
                }
            });

        serverPortTextField.setText("22666");
        serverPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        serverPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        serverPanel.add(serverDescrLabel);
        serverPanel.add(serverDescrTextField);
        serverPanel.add(serverPortLabel);
        serverPanel.add(serverPortTextField);
        serverPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        serverPanel.setVisible(false);

        clientPortTextField.setText("22666");
        clientPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        clientPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        clientPanel.add(clientServerLabel);
        clientPanel.add(clientServerTextField);
        clientPanel.add(clientPortLabel);
        clientPanel.add(clientPortTextField);
        clientPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        clientPanel.setVisible(false);

        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        cPanel.add(msgLabel);
        cPanel.add(Box.createRigidArea(new Dimension(0,5)));
        cPanel.add(localRB);
        cPanel.add(serverRB);
        cPanel.add(serverPanel);
        cPanel.add(clientRB);
        cPanel.add(clientPanel);

        sPanel.add(okButton);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(cPanel, BorderLayout.CENTER);
        this.getContentPane().add(sPanel, BorderLayout.SOUTH);

        this.getRootPane().setDefaultButton(okButton);

        switch(getGui().getConnectionType()) {
        case DCConstants.CONN_LOCAL:
            localRB.setSelected(true);
            break;
        case DCConstants.CONN_SERVER:
            serverRB.setSelected(true);
            break;
        case DCConstants.CONN_CLIENT:
            clientRB.setSelected(true);
            break;
        default:
            break;
        }

    }

	/**
	 * Sets the default values
	 *
	 */
	private void setDefaultValues() {
		String server = userOptions.getDefaultServer();
		String serverDesc = userOptions.getDefaultServerDescription();
		int portInt = userOptions.getDefaultPort();

		if ((portInt < 0) || (portInt > 65536)) { portInt = 22666; }
		
		String port = String.valueOf(portInt);
		
		serverDescrTextField.setText(serverDesc);
		clientServerTextField.setText(server);
		serverPortTextField.setText(port);
		clientPortTextField.setText(port);
		
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
	 * @return	reference to the parent
	 */
	private DC2dGUI	getGui() {
		return	parent;
	}
}

