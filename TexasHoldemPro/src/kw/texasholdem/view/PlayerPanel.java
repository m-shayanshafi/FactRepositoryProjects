package kw.texasholdem.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import kw.texasholdem.ai.impl.Player;
import kw.texasholdem.config.Action;
import kw.texasholdem.config.UIConstants;
import kw.texasholdem.tool.Card;
import kw.texasholdem.util.ResourceManager;

/**
 * Panel representing a player at the table.
 * 
 * @author Ken Wu
 */
public class PlayerPanel extends JPanel implements Serializable {
    
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Filled dealer button image when player is dealer. */
    transient private static final Icon BUTTON_PRESENT_ICON =
            ResourceManager.getIcon("/images/dealer.png");
    
    /** Empty dealer button image when player is not dealer. */
    transient private static final Icon BUTTON_ABSENT_ICON =
            ResourceManager.getIcon("/images/button_absent.png");
    
    transient private static final Icon CARD_PLACEHOLDER_ICON =
        ResourceManager.getIcon("/images/card_placeholder.png");

    transient private static final Icon CARD_BACK_ICON =
            ResourceManager.getIcon("/images/card_back.png");
    
    /** The border. */
    transient private static final Border BORDER = new EmptyBorder(10, 10, 10, 10);
    
    /** The label with the player's name. */
    transient private JLabel nameLabel;
    
    /** The label with the player's amount of cash. */
    transient private JLabel cashLabel;
    
    /** The label with the last action performed. */
    transient private JLabel actionLabel;
    
    /** The label with the player's current bet. */
    transient private JLabel betLabel;

    /** The label for the first hole card. */
    transient private JLabel card1Label;

    /** The label for the second hole card. */
    transient private JLabel card2Label;

    /** The label for the dealer button image. */
    transient private JLabel dealerButton;
    
    /**
     * Constructor.
     * 
     * @param player
     *            The player.
     */
    public PlayerPanel() {
        setBorder(BORDER);
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        nameLabel = new MyLabel();
        cashLabel = new MyLabel();
        actionLabel = new MyLabel();
        betLabel = new MyLabel();
        card1Label = new JLabel(CARD_PLACEHOLDER_ICON);
        card2Label = new JLabel(CARD_PLACEHOLDER_ICON);
        dealerButton = new JLabel(BUTTON_ABSENT_ICON);
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(dealerButton, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(1, 1, 1, 1);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(nameLabel, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(cashLabel, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(actionLabel, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(betLabel, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card1Label, gc);
        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card2Label, gc);

        setInTurn(false);
        setDealer(false);
    }
    
    /**
     * Updates the panel.
     * 
     * @param player
     *            The player.
     */
    public void update(Player player, boolean isResumeFromLastGame) {
        nameLabel.setText(player.getName());
        cashLabel.setText("$ " + player.getCash());
        int bet = player.getBet();
        if (bet == 0) {
            betLabel.setText(" ");
        } else {
            betLabel.setText("$ " + bet);
        }
        Action action = player.getAction();
        if (action != null) {
            actionLabel.setText(action.getName());
        } else {
            actionLabel.setText(" ");
        }
        if (player.hasCards() && !isResumeFromLastGame) {
            Card[] cards = player.getCards();
            if (cards.length == 2) {
                // Visible cards.
                card1Label.setIcon(ResourceManager.getCardImage(cards[0]));
                card2Label.setIcon(ResourceManager.getCardImage(cards[1]));
            } else {
                // Hidden cards (face-down).
                card1Label.setIcon(CARD_BACK_ICON);
                card2Label.setIcon(CARD_BACK_ICON);
            }
        } else {
            // No cards.
            card1Label.setIcon(CARD_PLACEHOLDER_ICON);
            card2Label.setIcon(CARD_PLACEHOLDER_ICON);
        }
    }
    
    /**
     * Sets whether the player is the dealer.
     * 
     * @param isDealer
     *            True if the dealer, otherwise false.
     */
    public void setDealer(boolean isDealer) {
        if (isDealer) {
            dealerButton.setIcon(BUTTON_PRESENT_ICON);
        } else {
            dealerButton.setIcon(BUTTON_ABSENT_ICON);
        }
    }
    
    /**
     * Sets whether it's this player's turn to act.
     * 
     * @param inTurn
     *            True if it's the player's turn, otherwise false.
     */
    public void setInTurn(boolean inTurn) {
        if (inTurn) {
            nameLabel.setForeground(Color.RED);
            cashLabel.setForeground(Color.RED);
        } else {
            nameLabel.setForeground(Color.GREEN);
            cashLabel.setForeground(Color.GREEN);
        }
    }
    
    /**
     * Custom label for a player panel.
     * 
     * @author Ken Wu
     */
    private class MyLabel extends JLabel {

        private static final long serialVersionUID = 1L;

        public MyLabel() {
            //setBorder(UIConstants.LABEL_BORDER);
            setForeground(UIConstants.TEXT_COLOR);
            setHorizontalAlignment(JLabel.HORIZONTAL);
            setText(" ");
        }
        
    } // MyLabel
    
}
