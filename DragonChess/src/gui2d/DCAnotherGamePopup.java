/*
 * Classname        : DC2dGUI
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Friday, December 06 2002, 16:15:03
 * Last Updated     : Friday, December 06 2002, 16:15:06
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
 * Option popup to choose if you want to play another game
 * of DragonChess.
 *
 * @author	Christophe Hertigers <xof@pandora.be>
 * @version Friday, December 06 2002, 16:15:06
 */
public class DCAnotherGamePopup extends JInternalFrame {

	private DC2dGUI			parent;
		
    private JPanel          cPanel      = new JPanel(),
                            sPanel      = new JPanel();
            
    private Border          popupBorder = BorderFactory.createEmptyBorder(
                                                                 5,5,5,5);
    private JLabel          msgLabel    = new JLabel("test", JLabel.LEFT),
                            newLabel    = new JLabel("Do you want to " +
                                             "start another game ?",
                                             JLabel.CENTER);
    private JButton         yesButton   = new JButton("Yes"),
                            noButton    = new JButton("No");
                                
    public DCAnotherGamePopup(DC2dGUI parent) {
        super("Another game?");
        
		this.parent = parent;
		
        setSize(400,200);
        setLocation((parent.getWidth()-400)/2,
					(parent.getHeight()-200)/2);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            
        yesButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	if (getGui().hasConnection()) {
                		// there's still a valid connection, so reuse it
	                    switch (getGui().getConnectionType()) {
	                    case DCConstants.CONN_LOCAL:
	                        getGui().registerPlayer(
											getGui().getGoldPlayerName(),
	                                       	DCConstants.PLAYER_GOLD);
	                        getGui().registerPlayer(
										  	getGui().getScarletPlayerName(),
	                                       	DCConstants.PLAYER_SCARLET);
	                    	getGui().newGame();	                        
	                        break;
	                        
	                    case DCConstants.CONN_SERVER:
	                    case DCConstants.CONN_CLIENT:
	                        switch (getGui().getPossibleActivePlayers()) {
	                        case DCConstants.PLAYER_GOLD:
	                            getGui().registerPlayer(
												getGui().getGoldPlayerName(),
	                                           	DCConstants.PLAYER_GOLD);
	                            break;
	                        case DCConstants.PLAYER_SCARLET:
	                            getGui().registerPlayer(
												getGui().getScarletPlayerName(),
	                                           	DCConstants.PLAYER_SCARLET);
	                            break;
	                        }
	                        break;
	                    }
                		setVisible(false);
                		
                	} else {
                	
                		// there's no valid connection, let the player choose another
                		// connection type
                		setVisible(false);
                		getGui().connPopup.showPopup();
                			
                	}
                }
            });
            
        noButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (getGui().getConnectionType()) {
                    case DCConstants.CONN_LOCAL:
                        getGui().unregisterPlayer(DCConstants.PLAYER_GOLD);
                        getGui().unregisterPlayer(DCConstants.PLAYER_SCARLET);
                        break;
                    case DCConstants.CONN_SERVER:
                    case DCConstants.CONN_CLIENT:
                        getGui().unregisterPlayer(
										getGui().getPossibleActivePlayers());
                        break;
                    }
                    setVisible(false);
        	        getGui().quitGame();
        	    }
        	});

        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        cPanel.add(msgLabel);
        cPanel.add(Box.createRigidArea(new Dimension(0,10)));
        cPanel.add(newLabel);

        sPanel.add(yesButton);
        sPanel.add(noButton);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(cPanel, BorderLayout.CENTER);
        this.getContentPane().add(sPanel, BorderLayout.SOUTH);

    	this.getRootPane().setDefaultButton(yesButton);
    }

	/**
	 * Sets the message to be displayed as question
	 * @param	the message
	 */
    public void setMessage(String msg) {
    	this.msgLabel.setText(msg);
	}

	/**
	 * Gets a reference to the parent
	 * @return	reference to the parent
	 */
	private DC2dGUI	getGui() {
		return parent;
	}

}

